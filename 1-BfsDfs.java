import java.util.*;

public class GraphTraversal {

    public static List<Integer> bfs(List<List<Integer>> graph, int startNode) {
        boolean[] visited = new boolean[graph.size()];
        Queue<Integer> queue = new LinkedList<>();
        List<Integer> order = new ArrayList<>();

        queue.add(startNode);
        visited[startNode] = true;

        while (!queue.isEmpty()) {
            int node = queue.poll();
            order.add(node);

            for (int neighbor : graph.get(node)) {
                if (!visited[neighbor]) {
                    queue.add(neighbor);
                    visited[neighbor] = true;
                }
            }
        }

        return order;
    }

    public static List<Integer> dfs(List<List<Integer>> graph, int startNode) {
        boolean[] visited = new boolean[graph.size()];
        Stack<Integer> stack = new Stack<>();
        List<Integer> order = new ArrayList<>();

        stack.push(startNode);
        visited[startNode] = true;

        while (!stack.isEmpty()) {
            int node = stack.pop();
            order.add(node);

            for (int neighbor : graph.get(node)) {
                if (!visited[neighbor]) {
                    stack.push(neighbor);
                    visited[neighbor] = true;
                }
            }
        }

        return order;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        List<List<Integer>> graph = new ArrayList<>();
        graph.add(Arrays.asList(1, 2));     
        graph.add(Arrays.asList(0, 3, 4));  
        graph.add(Arrays.asList(0, 5, 6));  
        graph.add(Arrays.asList(1));        
        graph.add(Arrays.asList(1));        
        graph.add(Arrays.asList(2));        
        graph.add(Arrays.asList(2));        

        
        System.out.println("Enter the starting node (0-6):");
        int startNode = scanner.nextInt();
 
        System.out.println("Enter 1 for BFS, 2 for DFS:");
        int option = scanner.nextInt();

        List<Integer> result;
        if (option == 1) {
            result = bfs(graph, startNode);
        } else if (option == 2) {
            result = dfs(graph, startNode);
        } else {
            System.out.println("Invalid option.");
            scanner.close();
            return;
        }

        System.out.println("Traversal Order: " + result);

        scanner.close();
    }
}
