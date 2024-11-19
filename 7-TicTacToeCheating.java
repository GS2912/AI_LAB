import java.util.Scanner;

public class TicTacToe {

    static char[] board = {'1', '2', '3', '4', '5', '6', '7', '8', '9'};
    static char player = 'X', computer = 'O';

    // Display the board
    public static void displayBoard() {
        System.out.println("-------------");
        for (int i = 0; i < 9; i += 3) {
            System.out.println("| " + board[i] + " | " + board[i + 1] + " | " + board[i + 2] + " |");
            System.out.println("-------------");
        }
    }

    // Check if someone has won
    public static boolean checkWin(char mark) {
        int[][] winConditions = {
            {0, 1, 2}, {3, 4, 5}, {6, 7, 8},
            {0, 3, 6}, {1, 4, 7}, {2, 5, 8},
            {0, 4, 8}, {2, 4, 6}
        };

        for (int[] condition : winConditions) {
            if (board[condition[0]] == mark && board[condition[1]] == mark && board[condition[2]] == mark) {
                return true;
            }
        }
        return false;
    }

    // Check for a tie
    public static boolean checkTie() {
        for (char slot : board) {
            if (slot != 'X' && slot != 'O') {
                return false;
            }
        }
        return true;
    }

    // Make a move on the board
    public static void makeMove(int pos, char mark) {
        board[pos - 1] = mark;
    }

    // Computer's strategy to never lose
    public static int computerMove() {
        // Check if the computer can win
        for (int i = 1; i <= 9; i++) {
            if (board[i - 1] != 'X' && board[i - 1] != 'O') {
                char temp = board[i - 1];
                board[i - 1] = computer;
                if (checkWin(computer)) {
                    return i;
                }
                board[i - 1] = temp;
            }
        }

        // Block the player if they are about to win
        for (int i = 1; i <= 9; i++) {
            if (board[i - 1] != 'X' && board[i - 1] != 'O') {
                char temp = board[i - 1];
                board[i - 1] = player;
                if (checkWin(player)) {
                    board[i - 1] = temp;
                    return i;
                }
                board[i - 1] = temp;
            }
        }

        // Take the center if available
        if (board[4] != 'X' && board[4] != 'O') {
            return 5;
        }

        // Take one of the corners if available
        int[] corners = {1, 3, 7, 9};
        for (int corner : corners) {
            if (board[corner - 1] != 'X' && board[corner - 1] != 'O') {
                return corner;
            }
        }

        // Otherwise, take any available spot
        for (int i = 1; i <= 9; i++) {
            if (board[i - 1] != 'X' && board[i - 1] != 'O') {
                return i;
            }
        }
        return -1; // Should never reach here
    }

    // Main game logic
    public static void playGame() {
        Scanner scanner = new Scanner(System.in);
        int pos;
        displayBoard();
        while (true) {
            // Player's move
            System.out.print("Player, choose your slot: ");
            pos = scanner.nextInt();
            if (pos < 1 || pos > 9 || board[pos - 1] == 'X' || board[pos - 1] == 'O') {
                System.out.println("Invalid move, try again.");
                continue;
            }
            makeMove(pos, player);
            displayBoard();

            // Check if the player wins
            if (checkWin(player)) {
                System.out.println("Player wins!");
                break;
            }

            // Check for a tie
            if (checkTie()) {
                System.out.println("It's a tie!");
                break;
            }

            // Computer's move
            int compMove = computerMove();
            System.out.println("Computer chooses slot: " + compMove);
            makeMove(compMove, computer);
            displayBoard();

            // Check if the computer wins
            if (checkWin(computer)) {
                System.out.println("Computer wins!");
                break;
            }

            // Check for a tie after the computer's move
            if (checkTie()) {
                System.out.println("It's a tie!");
                break;
            }
        }
        scanner.close();
    }

    public static void main(String[] args) {
        playGame();
    }
}
