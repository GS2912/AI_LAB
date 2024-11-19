import java.util.*;

public class GraphTraversal {
    
    static final int SIZE = 28;

    public static int[][] createAdjacencyMatrix(String text) {
        Set<Character> uniqueChars = new HashSet<>();
        text = text.toLowerCase().replaceAll("[^a-z]", "");
        
        for (char c : text.toCharArray()) {
            uniqueChars.add(c);
        }

        if (uniqueChars.size() > SIZE) {
            System.out.println("Error: More than 28 unique characters found.");
            return new int[SIZE][SIZE];
        }

        Map<Character, Integer> charIndexMap = new HashMap<>();
        int idx = 0;
        for (char c : uniqueChars) {
            charIndexMap.put(c, idx++);
        }

        int[][] adjacencyMatrix = new int[SIZE][SIZE];

        for (int i = 0; i < text.length() - 1; i++) {
            char c1 = text.charAt(i);
            char c2 = text.charAt(i + 1);
            
            // Ensure both characters are in the map (valid)
            if (charIndexMap.containsKey(c1) && charIndexMap.containsKey(c2)) {
                int index1 = charIndexMap.get(c1);
                int index2 = charIndexMap.get(c2);
                adjacencyMatrix[index1][index2] = 1;
                adjacencyMatrix[index2][index1] = 1;  // Undirected graph
            }
        }

        return adjacencyMatrix;
    }

    public static void displayMatrix(int[][] matrix) {
        System.out.println("Adjacency Matrix:");
        for (int[] row : matrix) {
            for (int val : row) {
                System.out.print(val + " ");
            }
            System.out.println();
        }
    }

    public static void bestFirstSearch(int[][] graph, char startChar, Map<Character, Integer> charIndexMap) {
    int startIndex = charIndexMap.get(startChar);
    
    PriorityQueue<Integer> pq = new PriorityQueue<>(Comparator.comparingInt(i -> getCharFromIndex(charIndexMap, i)));
    
    boolean[] visited = new boolean[SIZE];
    pq.add(startIndex);
    visited[startIndex] = true;

    System.out.println("Best First Search starting from character '" + startChar + "':");
    
    while (!pq.isEmpty()) {
        int current = pq.poll();
        System.out.print(getCharFromIndex(charIndexMap, current) + " ");
        
        for (int i = 0; i < SIZE; i++) {
            if (graph[current][i] == 1 && !visited[i]) {
                pq.add(i);
                visited[i] = true;
            }
        }
    }
    System.out.println();
}


    public static char getCharFromIndex(Map<Character, Integer> map, int index) {
        for (Map.Entry<Character, Integer> entry : map.entrySet()) {
            if (entry.getValue() == index) {
                return entry.getKey();
            }
        }
        return '*';  
    }

    public static void main(String[] args) {
        String text = "Everything was in confusion in the Oblonskys' house. The wife had "
                + "discovered that the husband was carrying on an intrigue with a French "
                + "girl, who had been a governess in their family, and she had announced to "
                + "her husband that she could not go on living in the same house with him. "
                + "This position of affairs had now lasted three days, and not only the "
                + "husband and wife themselves, but all the members of their family and "
                + "household, were painfully conscious of it. Every person in the house "
                + "felt that there was no sense in their living together, and that the "
                + "stray people brought together by chance in any inn had more in common "
                + "with one another than they, the members of the family and household of "
                + "the Oblonskys...";  

        int[][] adjacencyMatrix = createAdjacencyMatrix(text);

        displayMatrix(adjacencyMatrix);

        Set<Character> uniqueChars = new HashSet<>();
        for (char c : text.toLowerCase().replaceAll("[^a-z]", "").toCharArray()) {
            uniqueChars.add(c);
        }
        
        Map<Character, Integer> charIndexMap = new HashMap<>();
        int idx = 0;
        for (char c : uniqueChars) {
            charIndexMap.put(c, idx++);
        }

        bestFirstSearch(adjacencyMatrix, 'e', charIndexMap);
    }
}
