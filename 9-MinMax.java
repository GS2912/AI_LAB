import java.util.Arrays;

public class MinMaxGame {
    private int depth;
    private int[] gameState; // Game state (0: empty, 1: player, -1: opponent)

    public MinMaxGame(int depth) {
        this.depth = depth;
        this.gameState = new int[9]; // A simple game board (3x3)
    }

    // Check if the game has ended
    private boolean isTerminal(int[] state) {
        // Win conditions
        int[][] winConditions = {
            {0, 1, 2}, {3, 4, 5}, {6, 7, 8}, 
            {0, 3, 6}, {1, 4, 7}, {2, 5, 8}, 
            {0, 4, 8}, {2, 4, 6}
        };
        for (int[] condition : winConditions) {
            if (state[condition[0]] == state[condition[1]] && 
                state[condition[1]] == state[condition[2]] && 
                state[condition[0]] != 0) {
                return true; // Win condition met
            }
        }
        return Arrays.stream(state).allMatch(cell -> cell != 0); // Check for draw
    }

    // Minimax algorithm
    private int minimax(int[] state, int depth, boolean isMaximizing) {
        if (isTerminal(state)) {
            return evaluate(state);
        }
        if (depth == 0) {
            return 0; // Neutral score for depth limit reached
        }

        if (isMaximizing) {
            int bestScore = Integer.MIN_VALUE;
            for (int i = 0; i < 9; i++) {
                if (state[i] == 0) { // If cell is empty
                    state[i] = 1; // Player's move
                    int score = minimax(state, depth - 1, false);
                    state[i] = 0; // Undo move
                    bestScore = Math.max(score, bestScore);
                }
            }
            return bestScore;
        } else {
            int bestScore = Integer.MAX_VALUE;
            for (int i = 0; i < 9; i++) {
                if (state[i] == 0) { // If cell is empty
                    state[i] = -1; // Opponent's move
                    int score = minimax(state, depth - 1, true);
                    state[i] = 0; // Undo move
                    bestScore = Math.min(score, bestScore);
                }
            }
            return bestScore;
        }
    }

    // Simple evaluation function
    private int evaluate(int[] state) {
        for (int[] condition : new int[][]{
            {0, 1, 2}, {3, 4, 5}, {6, 7, 8}, 
            {0, 3, 6}, {1, 4, 7}, {2, 5, 8}, 
            {0, 4, 8}, {2, 4, 6}
        }) {
            if (state[condition[0]] == state[condition[1]] && 
                state[condition[1]] == state[condition[2]]) {
                if (state[condition[0]] == 1) {
                    return 10; // Player wins
                } else if (state[condition[0]] == -1) {
                    return -10; // Opponent wins
                }
            }
        }
        return 0; // Neutral score
    }

    // Method to find the best move for the player
    public int bestMove(int userMove) {
        gameState[userMove] = -1; // Mark the opponent's move
        int bestScore = Integer.MIN_VALUE;
        int move = -1;

        for (int i = 0; i < 9; i++) {
            if (gameState[i] == 0) { // If cell is empty
                gameState[i] = 1; // Player's move
                int score = minimax(gameState, depth, false);
                gameState[i] = 0; // Undo move
                if (score > bestScore) {
                    bestScore = score;
                    move = i;
                }
            }
        }
        gameState[userMove] = 0; // Reset user move after evaluation
        return move; // Return the best move
    }

    // Main method to run the tests
    public static void main(String[] args) {
        int[] userMoves = {6, 8, 1, 3};
        for (int depth = 1; depth <= 4; depth++) {
            MinMaxGame game = new MinMaxGame(depth);
            System.out.println("Depth: " + depth);
            for (int userMove : userMoves) {
                int computerMove = game.bestMove(userMove);
                System.out.println("User Move: " + userMove + ", Computer Move: " + computerMove);
                // After the computer move, let's also update the game state
                game.gameState[computerMove] = -1; // Simulate computer's move
            }
            System.out.println();
        }
    }
}
