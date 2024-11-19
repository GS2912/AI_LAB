import java.util.*;

class AStar {
    static class Node implements Comparable<Node> {
        int node;
        int gCost;  
        int hCost;  
        int fCost;  
        Node parent; 

        Node(int node, int gCost, int hCost, Node parent) {
            this.node = node;
            this.gCost = gCost;
            this.hCost = hCost;
            this.fCost = gCost + hCost;
            this.parent = parent;
        }

        @Override
        public int compareTo(Node other) {
            return Integer.compare(this.fCost, other.fCost);
        }
    }

    
    static int[][] graph = {
        //  A  B  C  D  E  F  G  H  I  J
        { 0, 6, 6, 3, 1, 0, 0, 0, 0, 0 }, // A
        { 6, 0, 2, 6, 3, 4, 0, 0, 0, 0 }, // B
        { 6, 2, 0, 0, 4, 5, 0, 0, 0, 0 }, // C
        { 3, 6, 0, 0, 7, 0, 9, 0, 0, 0 }, // D
        { 1, 3, 4, 7, 0, 8, 9, 0, 0, 0 }, // E
        { 0, 4, 5, 0, 8, 0, 8, 9, 0, 0 }, // F
        { 0, 0, 0, 9, 9, 8, 0, 11, 12, 0 }, // G
        { 0, 0, 0, 0, 0, 9, 11, 0, 14, 15 }, // H
        { 0, 0, 0, 0, 0, 0, 12, 14, 0, 15 }, // I
        { 0, 0, 0, 0, 0, 0, 0, 15, 15, 0 }  // J
    };

    static int[] heuristic = { 15, 13, 13, 12, 10, 9, 7, 6, 5, 0 };

    static char[] labels = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J' };

    static void aStarSearch(int startNode, int goalNode) {
        PriorityQueue<Node> openList = new PriorityQueue<>();
        boolean[] closedList = new boolean[graph.length];

        openList.add(new Node(startNode, 0, heuristic[startNode], null));

        while (!openList.isEmpty()) {
            Node current = openList.poll();

            if (current.node == goalNode) {
                printPath(current);
                return;
            }

            closedList[current.node] = true;

            for (int neighbor = 0; neighbor < graph.length; neighbor++) {
                int cost = graph[current.node][neighbor];
                if (cost > 0 && !closedList[neighbor]) {
                    int gCost = current.gCost + cost;
                    Node neighborNode = new Node(neighbor, gCost, heuristic[neighbor], current);

                    openList.add(neighborNode);
                }
            }
        }
    }

    static void printPath(Node node) {
        List<Character> path = new ArrayList<>();
        int totalCost = node.gCost;

        while (node != null) {
            path.add(labels[node.node]);
            node = node.parent;
        }

        Collections.reverse(path);

        System.out.println("Path: " + path);
        System.out.println("Total Cost: " + totalCost);
    }

    public static void main(String[] args) {
        int startNode = 4; 
        int goalNode = 9;  

        System.out.println("A* Search from 'E' to 'J':");
        aStarSearch(startNode, goalNode);
    }
}
