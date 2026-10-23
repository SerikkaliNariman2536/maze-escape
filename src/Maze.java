/**
 * Missions 1 and 2. Building the maze out of the walls.
 * A wall k joins the cells w.a()[k] and w.b()[k] and costs w.cost()[k]. Indexes start from 0,
 * the line number in walls.txt is the index + 1. The cells are connected by DisjointSet.
 */
public class Maze {

    /**
     * Mission 1. Take the walls in the order of the file. Break a wall when its two cells are in different sets.
     * Returns the indexes of the broken walls, in the order of breaking.
     */
    public static int[] breakWalls(Input.Walls w) {
        DisjointSet dsu = new DisjointSet(w.cells());
        int[] broken = new int[w.count()];
        int k = 0;
        for (int i = 0; i < w.count(); i++) {
            if (dsu.union(w.a()[i], w.b()[i])) {
                broken[k++] = i;
            }
        }
        return java.util.Arrays.copyOf(broken, k);
    }

    /**
     * Mission 2. Kruskal: the same, but take the walls from the cheapest to the most expensive.
     * Returns the indexes of the broken walls.
     */
    public static int[] cheapestMaze(Input.Walls w) {
        Integer[] order = new Integer[w.count()];
        for (int i = 0; i < order.length; i++) order[i] = i;
        java.util.Arrays.sort(order, (x, y) -> Integer.compare(w.cost()[x], w.cost()[y]));

        DisjointSet dsu = new DisjointSet(w.cells());
        int[] broken = new int[w.count()];
        int k = 0;
        for (int idx : order) {                       // от дешёвой к дорогой
            if (dsu.union(w.a()[idx], w.b()[idx])) {
                broken[k++] = idx;
            }
        }
        return java.util.Arrays.copyOf(broken, k);
    }

    /**
     * Mission 2. Prim with a priority queue. Returns the total cost of the cheapest maze.
     * It must be equal to the total cost of cheapestMaze.
     */
    public static long primCost(Input.Walls w) {
        int n = w.cells();

        java.util.List<java.util.List<int[]>> adj = new java.util.ArrayList<>();
        for (int i = 0; i < n; i++) adj.add(new java.util.ArrayList<>());
        for (int i = 0; i < w.count(); i++) {
            adj.get(w.a()[i]).add(new int[]{w.b()[i], w.cost()[i]});
            adj.get(w.b()[i]).add(new int[]{w.a()[i], w.cost()[i]});
        }

        boolean[] inTree = new boolean[n];

        java.util.PriorityQueue<int[]> pq =
                new java.util.PriorityQueue<>((x, y) -> Integer.compare(x[1], y[1]));
        long total = 0;
        int taken = 0;

        pq.add(new int[]{0, 0});
        while (!pq.isEmpty() && taken < n) {
            int[] top = pq.poll();
            int v = top[0];
            if (inTree[v]) continue;
            inTree[v] = true;
            total += top[1];
            taken++;
            for (int[] e : adj.get(v)) {
                if (!inTree[e[0]]) pq.add(new int[]{e[0], e[1]});
            }
        }
        return total;
    }
}
