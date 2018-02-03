package part2_week4;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.TST;
import part2_week4.test.BoggleBoard;

import java.util.HashSet;
import java.util.Set;

public class BoggleSolver {

    private final Set<String> dict = new HashSet<>();
    // TODO 使用trie优化时间
    //private final TST<String> dict2 = new TST<>();
    private static final int[] DX = new int[] {-1, 0, 1};
    private static final int[] DY = new int[] {-1, 0, 1};
    private boolean valid = false;

    // Initializes the data structure using the given array of strings as the dictionary.
    // (You can assume each word in the dictionary contains only the uppercase letters A through Z.)
    public BoggleSolver(String[] dictionary) {
        validateParam(dictionary);
        for (String word : dictionary) {
            dict.add(word);
            //dict2.put(word, word);
        }
    }

    // Returns the set of all valid words in the given Boggle board, as an Iterable.
    public Iterable<String> getAllValidWords(BoggleBoard board) {
        validateParam(board);
        Queue<String> result = new Queue<>();
        for (String word : dict) {
            if (word.length() >= 3 && check(board, word)) {
                result.enqueue(word);
            }
        }

        return result;//result.isEmpty() ? null : result;
    }

    private boolean check(BoggleBoard board, String word) {
        char c0 = word.charAt(0);
        char c1 = word.charAt(1);
        boolean visited[][] = new boolean[board.rows()][board.cols()];
        valid = false;
        for (int i=0 ; i<board.rows(); i++)
            for (int j=0; j<board.cols(); j++) {
                char cc = board.getLetter(i, j);
                if (cc == 'Q' && c0 == 'Q' && c1 == 'U') {
                    dfs(visited, board, word, i, j, 2);
                    if (valid) return true;

                } else if (cc != 'Q' && cc == c0) {
                    dfs(visited, board, word, i, j, 1);
                    if (valid) return true;
                }
            }
        return valid;
    }

    private void dfs(boolean[][] visited, BoggleBoard board, String word, int i, int j, int depth) {
        if (depth >= word.length()) {
            valid = true;
            return;
        }
        if (i<0 || i>=board.rows() || j<0 || j>=board.cols()) return;
        if (visited[i][j]) return;

        visited[i][j] = true;
        for (int dx : DX) {
            for (int dy : DY) {
                int x = i + dx;
                int y = j + dy;
                if (x==i && y==j) continue;
                if (x>=0 && x<board.rows() && y>=0 && y<board.cols() && !visited[x][y]) {
                    char cc = board.getLetter(x, y) ;//== word.charAt(depth);
                    if (cc == 'Q' && word.charAt(depth) == 'Q'
                            && word.length() > depth+1
                            && word.charAt(depth+1) == 'U') {
                        dfs(visited, board, word, x, y, depth+2);

                    } else if (cc != 'Q' && cc == word.charAt(depth)) {
                        dfs(visited, board, word, x, y, depth+1);
                    }
                }

            }
        }
        visited[i][j] = false;
    }

    // Returns the score of the given word if it is in the dictionary, zero otherwise.
    // (You can assume the word contains only the uppercase letters A through Z.)
    public int scoreOf(String word) {
        validateParam(word);
        return dict.contains(word) ? points(word.length()) : 0;
    }

    private int points(int len) {
        switch (len) {
            case 0:
            case 1:
            case 2: return 0;
            case 3: return 1;
            case 4: return 1;
            case 5: return 2;
            case 6: return 3;
            case 7: return 5;
            case 8: return 11;
        }
        return 11;
    }

    private void validateParam(Object o) {
        if (o == null) throw new IllegalArgumentException("input data not valid!");
    }

    public static void main(String[] args) {
        In in = new In(args[0]);
        String[] dictionary = in.readAllStrings();
        BoggleSolver solver = new BoggleSolver(dictionary);
        BoggleBoard board = new BoggleBoard(args[1]);
        int score = 0;
        int count = 0;
        for (String word : solver.getAllValidWords(board)) {
            StdOut.println(word);
            score += solver.scoreOf(word);
            count++;
        }
        StdOut.println("Score = " + score);
        StdOut.println("valid word count = " + count);

    }
}
