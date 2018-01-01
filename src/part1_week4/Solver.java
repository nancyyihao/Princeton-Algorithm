package part1_week4;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

public class Solver {

    private boolean isSolvable;
    private Stack<Board> steps = new Stack<>();

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        if (initial == null) throw new IllegalArgumentException(
                "input data not valid! please check you input!");
        MinPQ<Node> twinMinPQ = new MinPQ<>();
        MinPQ<Node> minPQ = new MinPQ<>();

        if (initial.isGoal()) {
            isSolvable = true;
            steps.push(initial);
            return;
        } else if (initial.twin().isGoal()) {
            isSolvable = false;
            return;
        }

        twinMinPQ.insert(new Node(initial.twin(), 0, null));
        minPQ.insert(new Node(initial, 0, null));
        while (true) {
            Node node = minPQ.delMin();
            if (node.board.isGoal()) {
                isSolvable = true;
                // pop up result
                while (node != null) {
                    steps.push(node.board);
                    node = node.previous;
                }
                return;
            } else {
                node.move++;
                for (Board board : node.board.neighbors()) {
                    // opt
                    if (node.previous == null || !node.previous.board.equals(board))
                        minPQ.insert(new Node(board, node.move, node));
                }
            }

            Node tNode = twinMinPQ.delMin();
            if (tNode.board.isGoal()) {
                isSolvable = false;
                return;
            } else {
                tNode.move++;
                for (Board twinBoard : tNode.board.neighbors()) {
                    // opt
                    if (tNode.previous == null || !tNode.previous.board.equals(twinBoard))
                        twinMinPQ.insert(new Node(twinBoard, tNode.move, node));
                }
            }
        }
    }

    // is the initial board solvable?
    public boolean isSolvable() {
        return isSolvable;
    }

    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
        return steps.size() -1;
    }

    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution() {
        return isSolvable ? steps : null;
    }

    private static class Node implements Comparable<Node> {
        Node previous;
        Board board;
        int move;

        public Node(Board board, int move, Node previous) {
            this.previous = previous;
            this.board = board;
            this.move = move;
        }

        @Override
        public int compareTo(Node that) {
            return Integer.compare(this.getPriority() , that.getPriority());
        }

        private int getPriority() {
            return move + board.manhattan();
        }
    }

    // solve a slider puzzle (given below)
    public static void main(String[] args) {
        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] blocks = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                blocks[i][j] = in.readInt();
        Board initial = new Board(blocks);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }
}
