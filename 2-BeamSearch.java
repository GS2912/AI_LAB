import java.util.*;

public class BeamSearch {
    static class Node {
        int id;
        int level;

        Node(int id, int level) {
            this.id = id;
            this.level = level;
        }
    }

    public static void beamSearchLevel(int[][] graph, int startNode, int Wl, int w) {
        Queue<Node> queue = new LinkedList<>();
        boolean[] visited = new boolean[graph.length];
        queue.add(new Node(startNode, 0));
        visited[startNode] = true;

        System.out.println("Beam Search with Level Width (Wl = " + w + "):");
        while (!queue.isEmpty()) {
            int levelSize = queue.size();
            int processedNodes = 0;

            for (int i = 0; i < levelSize; i++) {
                if (processedNodes >= w) break; 

                Node currentNode = queue.poll();
                System.out.print(currentNode.id + " ");

                for (int neighbor = 0; neighbor < graph [currentNode.id].length; neighbor++) {
                    if (graph[currentNode.id][neighbor] == 1 && !visited[neighbor]) {
                        queue.add(new Node(neighbor, currentNode.level + 1));
                        visited[neighbor] = true;
                        processedNodes++;
                    }
                    if (processedNodes >= w) break;
                }
            }
        }
        System.out.println("\n");
    }

    public static void beamSearchNode(int[][] graph, int startNode, int Wn, int w) {
        Stack<Node> stack = new Stack<>();
        boolean[] visited = new boolean[graph.length];
        stack.push(new Node(startNode, 0));
        visited[startNode] = true;

        System.out.println("Beam Search with Node Width (Wn = " + w + "):");
        while (!stack.isEmpty()) { 
            Node currentNode = stack.pop();
            System.out.print(currentNode.id + " ");

            int expandedNodes = 0;
            for (int neighbor = 0; neighbor < graph[currentNode.id].length; neighbor++) {
                if (graph[currentNode.id][neighbor] == 1 && !visited[neighbor]) {
                    stack.push(new Node(neighbor, currentNode.level + 1));
                    visited[neighbor] = true;
                    expandedNodes++;
                }
                if (expandedNodes >= w) break;
            }
        }
        System.out.println("\n");
    }

    public static void main(String[] args) {
        int[][] graph1 = {
            {0, 1, 1, 0, 0, 1, 0, 0, 0, 0}, 
            {1, 0, 1, 1, 0, 0, 0, 0, 0, 0}, 
            {1, 1, 0, 0, 1, 0, 0, 1, 0, 0}, 
            {0, 1, 0, 0, 1, 0, 1, 0, 1, 0}, 
            {0, 0, 1, 1, 0, 1, 1, 0, 0, 0}, 
            {1, 0, 0, 0, 1, 0, 0, 0, 0, 1}, 
            {0, 0, 0, 1, 1, 0, 0, 1, 0, 0}, 
            {0, 0, 1, 0, 0, 0, 1, 0, 0, 0}, 
            {0, 0, 0, 1, 0, 0, 0, 0, 0, 1}, 
            {0, 0, 0, 0, 0, 1, 0, 0, 1, 0}
        };

        int[][] graph2 = {
            {0, 1, 0, 0, 0, 0, 1, 1, 1, 0, 0, 0}, 
            {1, 0, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0}, 
            {0, 1, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0}, 
            {0, 1, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0}, 
            {0, 1, 0, 1, 0, 1, 0, 0, 0, 0, 0, 0}, 
            {0, 0, 1, 0, 1, 0, 0, 0, 0, 0, 0, 0}, 
            {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, 
            {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0}, 
            {1, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 1}, 
            {0, 0, 1, 0, 0, 0, 0, 0, 1, 0, 0, 0}, 
            {0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 1}, 
            {0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 1, 0}
        };


        beamSearchLevel(graph1, 0, 2, 2); 
        beamSearchLevel(graph2, 0, 2, 3); 

        beamSearchNode(graph1, 0, 2, 2); 
        beamSearchNode(graph2, 0, 2, 3); 
    }
}
