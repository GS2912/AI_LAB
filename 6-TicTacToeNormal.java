import java.util.Scanner;
import java.util.Random;

public class TicTacToe {
    static char[] board = {'1', '2', '3', '4', '5', '6', '7', '8', '9'};
    static char player = 'X', computer = 'O';


    public static void displayBoard() {
        System.out.println("-------------");
        for (int i = 0; i < 9; i += 3) {
            System.out.println("| " + board[i] + " | " + board[i + 1] + " | " + board[i + 2] + " |");
            System.out.println("-------------");
        }
    }

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

    public static boolean checkTie() {
        for (char slot : board) {
            if (slot != 'X' && slot != 'O') {
                return false;
            }
        }
        return true;
    }


    public static void makeMove(int pos, char mark) {
        board[pos - 1] = mark;
    }


    public static int computerMove() {
        Random rand = new Random();
        int move;
        while (true) {
            move = rand.nextInt(9) + 1;
            if (board[move - 1] != 'X' && board[move - 1] != 'O') {
                return move;
            }
        }
    }

    public static void playGame() {
        Scanner sc = new Scanner(System.in);
        int pos;
        displayBoard();
        while (true) {
            System.out.print("Player, choose your slot: ");
            pos = sc.nextInt();
            if (pos < 1 || pos > 9 || board[pos - 1] == 'X' || board[pos - 1] == 'O') {
                System.out.println("Invalid move, try again.");
                continue;
            }
            makeMove(pos, player);
            displayBoard();

            if (checkWin(player)) {
                System.out.println("Player wins!");
                break;
            }

            if (checkTie()) {
                System.out.println("It's a tie!");
                break;
            }

            int compMove = computerMove();
            System.out.println("Computer chose slot: " + compMove);
            makeMove(compMove, computer);
            displayBoard();

            if (checkWin(computer)) {
                System.out.println("Computer wins!");
                break;
            }

            if (checkTie()) {
                System.out.println("It's a tie!");
                break;
            }
        }
        sc.close();
    }

    public static void main(String[] args) {
        playGame();
    }
}
