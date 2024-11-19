import java.util.Scanner;

public class MagicSquareTicTacToe {
    // Magic Square values corresponding to the Tic Tac Toe positions
    private static final int[] magicSquare = {8, 1, 6, 3, 5, 7, 4, 9, 2};
    private static final int[] board = {2, 2, 2, 2, 2, 2, 2, 2, 2};  // Empty slots are represented by 2
    private static final int PLAYER = 3;  // X = 3
    private static final int COMPUTER = 5;  // O = 5
    
    public static void main(String[] args) {
        playGame();
    }

    // Display the current state of the board
    private static void displayBoard() {
        System.out.println("-------------");
        for (int i = 0; i < 9; i += 3) {
            System.out.println("| " + getSymbol(board[i]) + " | " + getSymbol(board[i + 1]) + " | " + getSymbol(board[i + 2]) + " |");
            System.out.println("-------------");
        }
    }

    // Returns the symbol ('X', 'O', or ' ') for display purposes
    private static char getSymbol(int mark) {
        return mark == 2 ? ' ' : (mark == PLAYER ? 'X' : 'O');
    }

    // Check if the given player has won using the magic square logic
    private static boolean checkWin(int mark) {
        int total;
        int[][] winConditions = {
                {0, 1, 2}, {3, 4, 5}, {6, 7, 8}, // Rows
                {0, 3, 6}, {1, 4, 7}, {2, 5, 8}, // Columns
                {0, 4, 8}, {2, 4, 6}  // Diagonals
        };
        for (int[] condition : winConditions) {
            total = 0;
            for (int pos : condition) {
                if (board[pos] == mark) {
                    total += magicSquare[pos];  // Sum magic square values
                }
            }
            if (total == 15) {  // A total of 15 in any combination means a win
                return true;
            }
        }
        return false;
    }

    // Check if all the slots are filled, leading to a tie
    private static boolean checkTie() {
        for (int slot : board) {
            if (slot == 2) {  // If any slot is still empty, it's not a tie
                return false;
            }
        }
        return true;
    }

    // Make a move on the board
    private static void makeMove(int pos, int mark) {
        board[pos - 1] = mark;
    }

    // Check if a move can result in a win for the current player
    private static int posswin(int mark) {
        for (int i = 0; i < 9; i++) {
            if (board[i] == 2) {  // Check if the spot is empty
                board[i] = mark;  // Simulate the move
                if (checkWin(mark)) {  // Check if it results in a win
                    board[i] = 2;  // Undo the move
                    return i + 1;  // Return the winning position (1-based index)
                }
                board[i] = 2;  // Undo the move
            }
        }
        return 0;  // No winning move found
    }

    // Decide the computer's move
    private static int computerMove() {
        // First, check if the computer can win
        int move = posswin(COMPUTER);
        if (move != 0) return move;

        // Then check if the player is about to win, and block them
        move = posswin(PLAYER);
        if (move != 0) return move;

        // Prioritize the center if available
        if (board[4] == 2) {
            return 5;
        }

        // Prioritize the corners
        int[] corners = {1, 3, 7, 9};
        for (int corner : corners) {
            if (board[corner - 1] == 2) {
                return corner;
            }
        }

        // Otherwise, pick any available spot
        for (int i = 1; i <= 9; i++) {
            if (board[i - 1] == 2) {
                return i;
            }
        }
        return 0;  // Should never reach here if there's a valid move
    }

    // Main game loop
    private static void playGame() {
        Scanner scanner = new Scanner(System.in);
        int pos;
        displayBoard();
        
        while (true) {
            // Player's move
            System.out.print("Player, choose your slot (1-9): ");
            pos = scanner.nextInt();
            if (pos < 1 || pos > 9 || board[pos - 1] != 2) {
                System.out.println("Invalid move, try again.");
                continue;
            }
            makeMove(pos, PLAYER);
            displayBoard();

            // Check if the player wins
            if (checkWin(PLAYER)) {
                System.out.println("Player wins!");
                break;
            }

            // Check if it's a tie
            if (checkTie()) {
                System.out.println("It's a tie!");
                break;
            }

            // Computer's move
            int compMove = computerMove();
            System.out.println("Computer chose slot: " + compMove);
            makeMove(compMove, COMPUTER);
            displayBoard();

            // Check if the computer wins
            if (checkWin(COMPUTER)) {
                System.out.println("Computer wins!");
                break;
            }

            // Check if it's a tie after the computer's move
            if (checkTie()) {
                System.out.println("It's a tie!");
                break;
            }
        }
        scanner.close();
    }
}
