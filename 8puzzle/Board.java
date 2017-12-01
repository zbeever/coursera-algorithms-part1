import java.util.Arrays;
import java.util.Stack;

public class Board {
    private final int n;
    private final int[][] tiles;

    private int manhattan;
    private int hamming;

    private int zX;
    private int zY;

    public Board(int[][] blocks) {
        n = blocks.length;
        manhattan = 0;
        hamming = 0;

        if (blocks[0].length != n) { throw new IllegalArgumentException("Board's dimensions are dissimilar"); }

        tiles = new int[n][n];

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                int p = blocks[i][j];

                if (p == 0) {
                    zX = i;
                    zY = j;
                }

                tiles[i][j] = p;
            }
        }

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                int p = blocks[i][j];
                hamming += individualHamming(p, i, j);
                manhattan += individualManhattan(p, i, j);
            }
        }
    }

    private int[][] copy() {
        int[][] copy = tiles.clone();
        for (int i = 0; i < n; i++) {
            copy[i] = tiles[i].clone();
        }
        return copy;
    }

    private int row(int p) {
        return (p - 1) / n;
    }

    private int col(int p) {
        return (p - 1) % n;
    }

    private int num(int i, int j) {
        return (i * n + j + 1);
    }

    private boolean checkSame(int p, int i, int j) {
        int current = num(i, j);
        if (p == current) { return true; }
        return false;
    }

    private int[][] exchange(int[][] array, int row1, int col1, int row2, int col2) {
        int temp = array[row1][col1];
        array[row1][col1] = array[row2][col2];
        array[row2][col2] = temp;

        return array;
    }

    private int individualHamming(int p, int i, int j) {
        if (checkSame(p, i, j)) { return 0; }
        if (zX == i && zY == j) { return 0; }
        return 1;
    }

    private int individualManhattan(int p, int i, int j) {
        if (checkSame(p, i, j)) { return 0; }
        if (zX == i && zY == j) { return 0; }
        return Math.abs(row(p) - i) + Math.abs(col(p) - j);
    }

    public int dimension() { return n; }

    public int hamming() { return hamming; }

    public int manhattan() { return manhattan; }

    public boolean isGoal() { return (hamming == 0); }

    public Board twin() {
        if (zX != 0) {
            return new Board(exchange(copy(), 0, n - 1, 0, n - 2));
        } else {
            return new Board(exchange(copy(), n - 1, n - 1, n - 1, n - 2));
        }
    }

    public Iterable<Board> neighbors() {
        Stack<Board> neighbors = new Stack<Board>();

        if (zX < n - 1) {
            neighbors.push(new Board(exchange(copy(), zX, zY, zX + 1, zY)));
        }
        if (zX > 0) {
            neighbors.push(new Board(exchange(copy(), zX, zY, zX - 1, zY)));
        }

        if (zY < n - 1) {
            neighbors.push(new Board(exchange(copy(), zX, zY, zX, zY + 1)));
        }
        if (zY > 0) {
            neighbors.push(new Board(exchange(copy(), zX, zY, zX, zY - 1)));
        }

        return neighbors;
    }

    public boolean equals(Object y) {
        if (this == y) { return true; }
        if (y == null) { return false; }
        if (y.getClass() != this.getClass()) { return false; }

        Board that = (Board) y;
        return ((this.dimension() == that.dimension()) && (Arrays.deepEquals(this.tiles, that.tiles)));

    }

    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(n + "\n");
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                s.append(String.format("%2d ", tiles[i][j]));
            }
            s.append("\n");
        }
        return s.toString();
    }

    public static void main(String[] args) {
        int n = 3;
        int[][] blocks = new int[n][n];

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if ((i * n + j + 1) == (n *n)) { blocks[i][j] = 0; }
                else { blocks[i][j] = (i * n + j + 1); }
            }
        }

        Board test = new Board(blocks);
        System.out.println(test);
        System.out.println(test.manhattan());
        System.out.println(test.isGoal());

        for (Board item : test.neighbors()) {
            System.out.println();
            System.out.println(item);
            System.out.println(item.manhattan());
            System.out.println(item.isGoal());
        }
    }
}
