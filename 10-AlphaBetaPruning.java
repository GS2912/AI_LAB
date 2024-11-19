import java.util.Arrays;

public class MinMaxGame {
    private int[] gameState; // Game state (0: empty, 1: player, -1: opponent)
    private int depth;

    // Counters for tracking Minimax calls
    private int minimaxCalls = 0;
    private int alphaBetaCalls = 0;

    public MinMaxGame(int depth) {
        this.depth = depth;
        this.gameState = new int[9]; // A simple game board (3x3)
    }

    // Check if the game has ended
    private boolean isTerminal(int[] state) {
        int[][] winConditions = {
            {0, 1, 2}, {3, 4, 5}, {6, 7, 8},
            {0, 3, 6}, {1, 4, 7}, {2, 5, 8},
            {0, 4, 8}, {2, 4, 6}
        };
        for (int[] condition : winConditions) {
            if (state[condition[0]] == state[condition[1]] &&
                state[condition[1]] == state[condition[2]] &&
                state[condition[0]] != 0) {
                return true; // Someone won
            }
        }
        return Arrays.stream(state).allMatch(cell -> cell != 0); // Draw
    }

    // Simple evaluation function
    private int evaluate(int[] state) {
        int[][] winConditions = {
            {0, 1, 2}, {3, 4, 5}, {6, 7, 8},
            {0, 3, 6}, {1, 4, 7}, {2, 5, 8},
            {0, 4, 8}, {2, 4, 6}
        };
        for (int[] condition : winConditions) {
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

    // Minimax algorithm without alpha-beta pruning
    private int minimax(int[] state, int depth, boolean isMaximizing) {
        minimaxCalls++;
        if (isTerminal(state) || depth == 0) {
            return evaluate(state);
        }

        if (isMaximizing) {
            int bestScore = Integer.MIN_VALUE;
            for (int i = 0; i < 9; i++) {
                if (state[i] == 0) {
                    state[i] = 1; // Player's move
                    bestScore = Math.max(bestScore, minimax(state, depth - 1, false));
                    state[i] = 0; // Undo move
                }
            }
            return bestScore;
        } else {
            int bestScore = Integer.MAX_VALUE;
            for (int i = 0; i < 9; i++) {
                if (state[i] == 0) {
                    state[i] = -1; // Opponent's move
                    bestScore = Math.min(bestScore, minimax(state, depth - 1, true));
                    state[i] = 0; // Undo move
                }
            }
            return bestScore;
        }
    }

    // Minimax algorithm with alpha-beta pruning
    private int alphaBetaPruning(int[] state, int depth, int alpha, int beta, boolean isMaximizing) {
        alphaBetaCalls++;
        if (isTerminal(state) || depth == 0) {
            return evaluate(state);
        }

        if (isMaximizing) {
            int bestScore = Integer.MIN_VALUE;
            for (int i = 0; i < 9; i++) {
                if (state[i] == 0) {
                    state[i] = 1; // Player's move
                    bestScore = Math.max(bestScore, alphaBetaPruning(state, depth - 1, alpha, beta, false));
                    state[i] = 0; // Undo move
                    alpha = Math.max(alpha, bestScore);
                    if (beta <= alpha) {
                        break; // Beta cut-off
                    }
                }
            }
            return bestScore;
        } else {
            int bestScore = Integer.MAX_VALUE;
            for (int i = 0; i < 9; i++) {
                if (state[i] == 0) {
                    state[i] = -1; // Opponent's move
                    bestScore = Math.min(bestScore, alphaBetaPruning(state, depth - 1, alpha, beta, true));
                    state[i] = 0; // Undo move
                    beta = Math.min(beta, bestScore);
                    if (beta <= alpha) {
                        break; // Alpha cut-off
                    }
                }
            }
            return bestScore;
        }
    }

    // Simulate moves for both algorithms and compare
    public void compareEfficiency() {
        int[] initialGameState = new int[9];
        minimaxCalls = 0;
        alphaBetaCalls = 0;

        // Call Minimax
        minimax(initialGameState, depth, true);

        // Call Alpha-Beta Pruning
        alphaBetaPruning(initialGameState, depth, Integer.MIN_VALUE, Integer.MAX_VALUE, true);

        System.out.println("Depth: " + depth);
        System.out.println("Minimax Calls: " + minimaxCalls);
        System.out.println("Alpha-Beta Pruning Calls: " + alphaBetaCalls);
        System.out.println("Efficiency Gain: " + ((double) minimaxCalls / alphaBetaCalls) + "x\n");
    }

    // Main method
    public static void main(String[] args) {
        for (int depth = 1; depth <= 6; depth++) {
            MinMaxGame game = new MinMaxGame(depth);
            game.compareEfficiency();
        }
    }
}
