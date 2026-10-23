/**
 * Mission 3. Escape: DFS from cell 0 to the last cell (rows * cols - 1) in the maze of Mission 1.
 * Two neighbouring cells are connected when the wall between them is broken.
 * The neighbours are tried in this order: up, right, down, left (cell - cols, cell + 1, cell + cols, cell - 1).
 * The ready method Graphs.open(w, broken) gives you the open neighbours of every cell, in no particular order:
 * to visit them in the required order, go through the four cells above one by one and take those that are in the list.
 * The program must not overflow the stack on a maze of 1000 by 1000 cells.
 */
public class Escape {

    /**
     * path: the cells of the way out, from 0 to the last cell.
     * visited: how many cells are marked as visited at the moment the exit is marked (the start and the exit are counted).
     * deadEnds: how many cells of the maze have exactly one open neighbour.
     */
    public record Result(int[] path, int visited, int deadEnds) {}

    public static Result escape(Input.Walls w, int[] broken) {
        int n = w.cells();
        int cols = w.cols();
        int exit = n - 1;
        int[][] g = Graphs.open(w, broken);

        int deadEnds = 0;
        for (int i = 0; i < n; i++) {
            if (g[i].length == 1) deadEnds++;
        }

        boolean[] seen = new boolean[n];
        int[] next = new int[n];
        int[] stack = new int[n];
        int top = 0;

        stack[top++] = 0;
        seen[0] = true;
        int visited = 1;

        while (top > 0 && stack[top - 1] != exit) {
            int cell = stack[top - 1];
            if (next[cell] == 4) {
                top--;
                continue;
            }
            int dir = next[cell]++;
            int nb = neighbour(cell, dir, cols, n);
            if (nb >= 0 && !seen[nb] && isOpen(g[cell], nb)) {
                seen[nb] = true;
                visited++;
                stack[top++] = nb;
            }
        }

        return new Result(java.util.Arrays.copyOf(stack, top), visited, deadEnds);
    }

    private static int neighbour(int cell, int dir, int cols, int n) {
        switch (dir) {
            case 0:  return cell >= cols ? cell - cols : -1;
            case 1:  return cell % cols != cols - 1 ? cell + 1 : -1;
            case 2:  return cell + cols < n ? cell + cols : -1;
            default: return cell % cols != 0 ? cell - 1 : -1;
        }
    }

    private static boolean isOpen(int[] list, int cell) {
        for (int x : list) {
            if (x == cell) return true;
        }
        return false;
    }
}
