package part1_week4;

import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

public class Board {

    private int manhattan = -1;
    private int hamming = -1;
    private int isGoal = -1;
    private int[][] blocks;
    private int N;

    public Board(int[][] b) {
        N = b.length;
        blocks = new int[N][N];
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                blocks[i][j] = b[i][j];
            }
        }
    }

    public int dimension() {
        return N;
    }

    public int hamming() {
        if (hamming == -1) {
            hamming = 0;
            for(int i = 1; i < N * N; i++) {
                int x = (i - 1) / N;
                int y = (i - 1) % N;
                if(i != blocks[x][y]) hamming++;
            }
        }
        return hamming;
    }

    public int manhattan() {
        if (manhattan == -1) {
            manhattan = 0;
            for (int i = 0; i < N; i++)
                for (int j = 0; j < N; j++)
                    if (blocks[i][j] != 0) {
                        int x = (blocks[i][j] - 1) / N;
                        int y = (blocks[i][j] - 1) % N;
                        manhattan += Math.abs(x - i) + Math.abs(y - j);
                    }
        }
        return manhattan;
    }

    public boolean isGoal() {
        if (isGoal == -1) {
            isGoal = 1;
            for (int i = 0; i < N; i++)
                for (int j = 0; j < N; j++) {
                    if (i == N - 1 && j == N - 1 && blocks[i][j] == 0) continue;
                    if (blocks[i][j] != i * N + (j + 1)) {
                        isGoal = 0;
                        return false;
                    }
                }
        }
        return isGoal == 1;
    }

    public Board twin() {
        Board board = new Board(newBlocks());
        int i = 0;
        int j = 0;
        for (i = 0; i < N; i++) {
            for (j = 0; j < N; j++) {
                if (i + 1 < N && blocks[i][j] != 0 && blocks[i + 1][j] != 0) break;
            }
            if (j < N) break;
        }
        // swap
        int tmp = board.blocks[i + 1][j];
        board.blocks[i + 1][j] = board.blocks[i][j];
        board.blocks[i][j] = tmp;

        return board;
    }

    public boolean equals(Object y) {
        if (y == this) return true;
        if (y == null) return false;
        if (y.getClass() != this.getClass()) return false;

        Board other = (Board) y;
        if (other.N != this.N) return false;
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (this.blocks[i][j] != other.blocks[i][j]) return false;
            }
        }
        return true;
    }


    public Iterable<Board> neighbors() {
        int i, j = 0;
        Stack<Board> boards = new Stack<>();
        for (i = 0; i < N; i++) {
            for (j = 0; j < N; j++) {
                if (blocks[i][j] == 0) break;
            }
            if (j < N) break;
        }
        if (i - 1 >= 0) {
            Board neighbor = new Board(newBlocks());
            neighbor.blocks[i][j] = neighbor.blocks[i - 1][j];
            neighbor.blocks[i - 1][j] = 0;
            boards.push(neighbor);
        }
        if (i + 1 < N) {
            Board neighbor = new Board(newBlocks());
            neighbor.blocks[i][j] = neighbor.blocks[i + 1][j];
            neighbor.blocks[i + 1][j] = 0;
            boards.push(neighbor);
        }
        if (j - 1 >= 0) {
            Board neighbor = new Board(newBlocks());
            neighbor.blocks[i][j] = neighbor.blocks[i][j - 1];
            neighbor.blocks[i][j - 1] = 0;
            boards.push(neighbor);
        }
        if (j + 1 < N) {
            Board neighbor = new Board(newBlocks());
            neighbor.blocks[i][j] = neighbor.blocks[i][j + 1];
            neighbor.blocks[i][j + 1] = 0;
            boards.push(neighbor);
        }

        return boards;
    }

    private int[][] newBlocks() {
        int[][] arr = new int[N][N];
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                arr[i][j] = blocks[i][j];
            }
        }
        return arr;
    }

    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(N + "\n");
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                s.append(String.format("%2d ", blocks[i][j]));
            }
            s.append("\n");
        }
        return s.toString();
    }

    public static void main(String[] args) {
        int[][] blocks = new int[][] { {8, 1, 3}, {4, 0, 2}, {7, 6, 5} };
        Board board = new Board(blocks);

        // 10 expected
        StdOut.println("manhattan distance is: " + board.manhattan());
        // 5 expected
        StdOut.println("hamming distance is: " + board.hamming());

        int[][] b2 = new int[][] { {0, 1, 3}, {4, 2, 5}, {7, 8, 6} };
        Board board2 = new Board(b2);

        int[][] b3 = new int[][] { {1, 2, 3}, {4, 0, 5}, {7, 6, 8} };
        Board board3 = new Board(b3);

        StdOut.println(board3.equals(board2));
    }
}
