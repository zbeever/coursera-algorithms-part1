import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import java.util.LinkedList;

public class Solver {
    private final MinPQ<Node> moves = new MinPQ<Node>();
    private final MinPQ<Node> twinMoves = new MinPQ<Node>();
    
    private boolean solvable = false;
    private boolean twinSolvable = false;
    private Node solved;
    
    public Solver(Board initial) {
        moves.insert(new Node(initial));
        twinMoves.insert(new Node(initial.twin()));
        
        while (!solvable && !twinSolvable) {
            solvable = trySolve(moves);
            if (solvable) { break; }
            twinSolvable = trySolve(twinMoves);
        }
    }
    
    private class Node implements Comparable<Node> {
        private final Board board;
        private final Node parent;
        private int moves;

        private Node(Board board) {
            this(board, null, 0);
        }
        
        public int compareTo(Node that) {
            return (this.board.manhattan() + this.moves) - (that.board.manhattan() + that.moves);
        }

        private Node(Board board, Node parent, int moves) {
            this.board = board;
            this.parent = parent;
            this.moves = moves;
        }
    }
    
    private boolean trySolve(MinPQ<Node> queue) {
        Node current = queue.delMin();
        
        if (current.board.isGoal()) {
            solved = current;
            return true;
        }

        for (Board board : current.board.neighbors()) {
            if (current.parent == null || !board.equals(current.parent.board)) {
                queue.insert(new Node(board, current, current.moves + 1));
            }
        }
        
        return false;
    }

    public boolean isSolvable() { return solvable; }

    public int moves() {
        if (!solvable) { return -1; }
        return solved.moves;
    }

    public Iterable<Board> solution() {
        LinkedList<Board> solution = null;
        
        if (isSolvable()) {
            Node current = solved;
            solution = new LinkedList<>();
            solution.addFirst(current.board);
            
            while (current.parent != null) {
                current = current.parent;
                solution.addFirst(current.board);
            }
        }
        
        return solution;
    }
    
    public static void main(String[] args) {
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] blocks = new int[n][n];
        
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                blocks[i][j] = in.readInt();
            }
        }
        
        Board initial = new Board(blocks);
        Solver solver = new Solver(initial);

        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            solver.solution().forEach(StdOut::println);
        }
    }
}