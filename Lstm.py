import os
import zipfile
import shutil
import torch

dataset_dir = 'C:\\Users\\acer\\Downloads\\new_dataset'
train_dir = 'C:\\Users\\acer\\Downloads\\new_dataset\\train'
test_dir = 'C:\\Users\\acer\\Downloads\\new_dataset\\test'

device = torch.device("cuda" if torch.cuda.is_available() else "cpu")
print(device)

def get_video_paths_and_labels(base_dir):
    video_paths = []
    labels = []
    for root, _, files in os.walk(base_dir):
        for file in files:
            if file.endswith(('.mp4', '.avi', '.mpg')):
                video_paths.append(os.path.join(root, file))
                labels.append(root.split(os.sep)[-1])
    return video_paths, labels

video_paths_train, train_labels = get_video_paths_and_labels(train_dir)
video_paths_val, val_labels = get_video_paths_and_labels(test_dir)

class_labels = sorted({os.path.basename(os.path.dirname(path)) for path in video_paths_train})

# Create mappings from labels to IDs and vice versa
label2id = {label: i for i, label in enumerate(class_labels)}
id2label = {i: label for label, i in label2id.items()}



print(f"Unique classes: {list(label2id.keys())}.")

import torch
from torch.utils.data import Dataset, DataLoader
import cv2
import numpy as np
import imageio
from deepface import DeepFace
import mediapipe as mp

# Setup for Mediapipe face mesh detector
mp_face_mesh = mp.solutions.face_mesh
face_mesh = mp_face_mesh.FaceMesh()


class EmotionLandmarksDataset(Dataset):
    def __init__(self, video_paths, labels, max_frames=100, detector_backend='opencv'):
        """
        Args:
            video_paths (list): List of paths to video files.
            labels (list): List of labels for each video.
            max_frames (int): Maximum number of frames to extract from each video.
            detector_backend (str): Backend to use for DeepFace face detection.
        """
        self.video_paths = video_paths
        self.labels = labels
        self.max_frames = max_frames
        self.detector_backend = detector_backend

    def __len__(self):
        return len(self.video_paths)

    def __getitem__(self, idx):
        video_path = self.video_paths[idx]
        label = self.labels[idx]

        # Load video frames using imageio
        video_reader = imageio.get_reader(video_path, 'ffmpeg')
        frames = []
        landmarks_list = []

        for i, frame in enumerate(video_reader):
            # Convert frame to RGB for DeepFace
            frame_rgb = cv2.cvtColor(frame, cv2.COLOR_BGR2RGB)

            # Use DeepFace to detect and crop the face
            face_objs = DeepFace.extract_faces(img_path=frame_rgb, detector_backend=self.detector_backend, align=True, enforce_detection=False)

            if face_objs:
                # Extract the face from DeepFace output
                face_data = face_objs[0]
                cropped_face = face_data['face']  # This is in float32 format [0, 1]

                # Convert to uint8
                cropped_face = (cropped_face * 255).astype(np.uint8) 
                cropped_face = cv2.resize(cropped_face, (224, 224))

                # Append the cropped face to frames
                frames.append(cropped_face)

                # Process the cropped face to get landmarks using Mediapipe
                face_rgb = cv2.cvtColor(cropped_face, cv2.COLOR_BGR2RGB)
                results = face_mesh.process(face_rgb)

                if results.multi_face_landmarks:
                    for face_landmarks in results.multi_face_landmarks:
                        landmarks = np.array([[lm.x, lm.y, lm.z] for lm in face_landmarks.landmark]).flatten()
                        landmarks_list.append(landmarks)
                else:
                    # If no landmarks detected, append zeros
                    landmarks_list.append(np.zeros((468 * 3,)))  # 468 landmarks * (x, y, z)
            else:
                # If no face detected, append zeros
                frames.append(np.zeros((224, 224, 3), dtype=np.uint8))
                landmarks_list.append(np.zeros((468 * 3,)))

            if len(frames) >= self.max_frames:
                break

        video_reader.close()

        # Pad frames and landmarks if necessary
        while len(frames) < self.max_frames:
            frames.append(np.zeros((224, 224, 3), dtype=np.uint8))
            landmarks_list.append(np.zeros((468 * 3,)))

        # Convert frames to tensor
        # video_tensor = torch.tensor(np.stack(frames), dtype=torch.float32).permute(0, 3, 1, 2)  # (T, C, H, W)

        # Convert landmarks to tensor
        landmarks_tensor = torch.tensor(np.stack(landmarks_list), dtype=torch.float32)

        # Convert label to tensor (you'll need to map label strings to IDs if not already done)
        label_tensor = torch.tensor(label, dtype=torch.long)

        return landmarks_tensor, label_tensor
    




from sklearn.preprocessing import LabelEncoder
from torch.utils.data import DataLoader

from tqdm import tqdm
import torch.nn as nn
import torch.optim as optim

if __name__ == '__main__':
    # Encode the labels as integers
    label_encoder = LabelEncoder()
    labels_encoded = label_encoder.fit_transform(train_labels)  # Convert labels to numeric format

    # Create dataset
    emotion_dataset_train = EmotionLandmarksDataset(video_paths_train, labels_encoded)
    emotion_dataset_test = EmotionLandmarksDataset(video_paths_val, labels_encoded)

    # Create DataLoader
    batch_size = 8  # Adjust batch size based on your system's memory capacity
    train_dataloader = DataLoader(emotion_dataset_train, batch_size=batch_size, shuffle=True, drop_last=True, num_workers=6, pin_memory=True)
    test_dataloader = DataLoader(emotion_dataset_test, batch_size=batch_size, shuffle=True, drop_last=True, num_workers=6, pin_memory=True)

    # Define the LSTM-based model
    class EmotionLSTMModel(nn.Module):
        def __init__(self, input_size, hidden_size, num_layers, num_classes):
            super(EmotionLSTMModel, self).__init__()
            self.lstm = nn.LSTM(input_size, hidden_size, num_layers, batch_first=True)
            self.fc = nn.Linear(hidden_size, num_classes)

        def forward(self, x):
            lstm_out, (hn, cn) = self.lstm(x)
            out = self.fc(lstm_out[:, -1, :])
            return out

    # Define model parameters
    input_size = 468 * 3  # 468 landmarks * (x, y, z)
    hidden_size = 128  # Can be tuned
    num_layers = 2  # Can be tuned
    num_classes = 6  # For 6 emotion classes (adjust based on your dataset)
    lr = 1e-3  # Learning rate

    # Initialize the model, loss function, and optimizer
    model = EmotionLSTMModel(input_size, hidden_size, num_layers, num_classes)
    criterion = nn.CrossEntropyLoss()
    optimizer = optim.Adam(model.parameters(), lr=lr)

    model.to(device)

    epochs = 10
    for epoch in range(epochs):
        print(f"Epoch {epoch + 1}/{epochs}")

        # Training phase
        model.train()
        running_train_loss = 0.0
        correct_train = 0
        total_train = 0

        for batch in tqdm(train_dataloader, desc="Training", leave=False):
            inputs, labels = batch
            inputs = inputs.to(device)
            labels = labels.to(device)

            optimizer.zero_grad()
            outputs = model(inputs)
            loss = criterion(outputs, labels)
            loss.backward()
            optimizer.step()

            # Accumulate loss and accuracy
            running_train_loss += loss.item()
            _, predicted = torch.max(outputs, 1)
            total_train += labels.size(0)
            correct_train += (predicted == labels).sum().item()

        train_loss = running_train_loss / len(train_dataloader)
        train_acc = 100 * correct_train / total_train

        print(f"Training Loss: {train_loss:.4f}, Training Accuracy: {train_acc:.2f}%")

        # Validation phase
        model.eval()
        running_val_loss = 0.0
        correct_val = 0
        total_val = 0

        with torch.no_grad():
            for batch in tqdm(test_dataloader, desc="Validating", leave=False):
                inputs, labels = batch
                inputs = inputs.to(device)
                labels = labels.to(device)

                outputs = model(inputs)
                loss = criterion(outputs, labels)

                # Accumulate loss and accuracy
                running_val_loss += loss.item()
                _, predicted = torch.max(outputs, 1)
                total_val += labels.size(0)
                correct_val += (predicted == labels).sum().item()

        val_loss = running_val_loss / len(test_dataloader)
        val_acc = 100 * correct_val / total_val

        print(f"Validation Loss: {val_loss:.4f}, Validation Accuracy: {val_acc:.2f}%")
        print("-" * 50)

    print("Training complete!")
    torch.save(model.state_dict(), 'model2Facial68.pth')
    print("Model saved successfully")



