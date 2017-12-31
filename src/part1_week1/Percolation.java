package part1_week1;

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    // create n-by-n grid, with all sites blocked
    private WeightedQuickUnionUF uf1;
    private WeightedQuickUnionUF uf2;
    private boolean[][] opened;
    private int openedCount;
    private int BOTTOM;
    private int TOP;
    private int N;
    public Percolation(int n) {
        validateParam(n <= 0);

        N = n;
        BOTTOM = N * N + 2;
        TOP = N * N + 1;

        uf1 = new WeightedQuickUnionUF((N+2) * (N+2) + 2);
        uf2 = new WeightedQuickUnionUF((N+2) * (N+2) + 1);

        opened = new boolean[N+1][N+1];
        for (int i=1; i<=N; i++)
            for (int j=1; j<=N; j++)
                opened[i][j] = false;
        openedCount = 0;
    }

    // open site (row, col) if it is not open already
    public void open(int row, int col) {

        validateParam(row < 1 || row > N || col < 1 || col > N);

        if (!opened[row][col]) {
            opened[row][col] = true;
            int index = xyTo1D(row, col);
            if (row == N) uf1.union(BOTTOM, index);
            if (row == 1) {
                uf1.union(TOP, index);
                uf2.union(TOP, index);
            }
            if (row-1 >= 1 && opened[row-1][col]) {
                uf1.union( index, xyTo1D(row-1, col) );
                uf2.union( index, xyTo1D(row-1, col) );
            }
            if (row+1 <= N && opened[row+1][col]) {
                uf1.union( index, xyTo1D(row+1, col) );
                uf2.union( index, xyTo1D(row+1, col) );
            }
            if (col-1 >= 1 && opened[row][col-1]) {
                uf1.union( index, xyTo1D(row, col-1) );
                uf2.union( index, xyTo1D(row, col-1) );
            }
            if (col+1 <= N && opened[row][col+1]) {
                uf1.union( index, xyTo1D(row, col+1) );
                uf2.union( index, xyTo1D(row, col+1) );
            }
            openedCount++;
        }
    }

    // is site (row, col) open?
    public boolean isOpen(int row, int col) {
        validateParam(row < 1 || row > N || col < 1 || col > N);
        return opened[row][col];
    }

    // is site (row, col) full?
    public boolean isFull(int row, int col) {
        validateParam(row < 1 || row > N || col < 1 || col > N);
        return uf2.connected(TOP, xyTo1D(row, col) );
    }
    // number of open sites
    public int numberOfOpenSites() {
        return openedCount;
    }
    // does the system percolate?
    public boolean percolates() {
        return uf1.connected(TOP, BOTTOM);
    }

    private int xyTo1D(int row, int col) {
        return (row-1) * N + col;
    }

    private void validateParam(boolean invalid) {
        if (invalid)
            throw new IllegalArgumentException("Data invalid! Please check your input!");
    }

    // test client (optional)
    public static void main(String[] args) {

    }
}
