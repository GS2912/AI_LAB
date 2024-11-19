import java.util.*;

public class IDDFS {
   
    private int NODES;

    private List<List<Integer>> graph;
    public IDDFS(int nodes) {
        NODES = nodes;
        graph = new ArrayList<>();
        for (int i = 0; i < NODES; i++) {
            graph.add(new ArrayList<>());
        }
    }

    public void addEdge(int u, int v) {
        graph.get(u).add(v);
        graph.get(v).add(u); 
    }

    private boolean DLS(int node, int target, int depth, boolean[] visited) {
        if (depth == 0) {
            return node == target;
        }

        visited[node] = true;

        for (int neighbor : graph.get(node)) {
            if (!visited[neighbor]) {
                if (DLS(neighbor, target, depth - 1, visited)) {
                    return true;
                }
            }
        }

        visited[node] = false;
        return false;
    }

    // Iterative Deepening Depth First Search
    public boolean IDDFS(int start, int target, int maxDepth) {
        for (int depth = 1; depth <= maxDepth; depth++) {
            boolean[] visited = new boolean[NODES]; 
            System.out.println("Searching at depth: " + depth);
            if (DLS(start, target, depth, visited)) {
                System.out.println("Found target " + target + " at depth " + depth);
                return true;
            }
        }
        return false; 
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

       
        System.out.println("Enter number of nodes: ");
        int numNodes = sc.nextInt();
        System.out.println("Enter number of edges: ");
        int numEdges = sc.nextInt();

        IDDFS graph = new IDDFS(numNodes);

        System.out.println("Enter the edges (u v) one by one:");
        for (int i = 0; i < numEdges; i++) {
            int u = sc.nextInt();
            int v = sc.nextInt();
            graph.addEdge(u, v);
        }

        System.out.println("Enter start node: ");
        int startNode = sc.nextInt();
        System.out.println("Enter target node: ");
        int targetNode = sc.nextInt();
        System.out.println("Enter max depth: ");
        int maxDepth = sc.nextInt();

        if (!graph.IDDFS(startNode, targetNode, maxDepth)) {
            System.out.println("Target not reachable within depth " + maxDepth);
        }
    }
}
