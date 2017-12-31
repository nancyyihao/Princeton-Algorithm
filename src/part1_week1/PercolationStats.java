package part1_week1;

import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
    private double[] probability;
    private double stddev = 0.0f;
    private double mean = 0.0f;

    // perform trials independent experiments on an n-by-n grid
    public PercolationStats(int n, int trials) {
        validateParam(n<=0 || trials <=0 );

        probability = new double[trials];
        for (int i=0 ; i<trials; i++) {
            Percolation percolation = new Percolation(n);
            while (!percolation.percolates()) {
                int x= StdRandom.uniform(n) + 1;
                int y= StdRandom.uniform(n) + 1;
                percolation.open(x, y);
            }
            probability[i] = (double) percolation.numberOfOpenSites() / (n*n);
        }
    }

    // sample mean of percolation threshold
    public double mean() {
        if (Double.compare(mean, 0.0f) == 0) {
            mean = StdStats.mean(probability);
        }
        return mean;
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        if (Double.compare(stddev, 0.0f) == 0) {
            stddev = StdStats.stddev(probability);
        }
        return stddev;
    }

    // low  endpoint of 95% confidence interval
    public double confidenceLo() {
        return mean() - 1.96d * stddev() / Math.sqrt(probability.length);
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        return mean() + 1.96d * stddev() / Math.sqrt(probability.length);
    }


    private void validateParam(boolean invalid) {
        if (invalid)
            throw new IllegalArgumentException("Data invalid! Please check your input!");
    }

    // test client (described below)
    public static void main(String[] args) {

        int n = Integer.parseInt(args[0]);
        int times = Integer.parseInt(args[1]);
        PercolationStats stats = new PercolationStats(n, times);

        System.out.println("mean                    = " + stats.mean());
        System.out.println("stddev                  = " + stats.stddev());
        System.out.println("95% confidence interval = ["
                + stats.confidenceLo()
                + ", "
                + stats.confidenceHi()
                + "]");
    }
}
