import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
    private final double expTimes, mean, stddev, confiLo, confiHi;

    public PercolationStats(int n, int trials) {
        if (n <= 0 || trials <= 0) throw new IllegalArgumentException("parameters should be larger than 0");

        expTimes = trials;
        double[] ratio = new double[trials];
        double size = n * n;
        for (int i = 0; i < trials; i++) {
            Percolation p = new Percolation(n);
            while (!p.percolates()) {
                p.open(StdRandom.uniform(n) + 1, StdRandom.uniform(n) + 1);
            }
            ratio[i] = p.numberOfOpenSites() / size;
        }

        mean = StdStats.mean(ratio);
        stddev = StdStats.stddev(ratio);
        double confi95 =  1.96 * stddev / Math.sqrt(expTimes);
        confiLo = mean - confi95;
        confiHi = mean + confi95;
    }

    public double mean() {
        return mean;
    }

    public double stddev() {
        if (expTimes == 1) return Double.NaN;
        else return stddev;
    }

    public double confidenceLo() {
        return confiLo;
    }

    public double confidenceHi() {
        return confiHi;
    }

    public static void main(String[] args) {
        int n = Integer.parseInt(args[0]);
        int t = Integer.parseInt(args[1]);
        PercolationStats pStats = new PercolationStats(n, t);
        System.out.println("mean                    = " + pStats.mean());
        System.out.println("stddev                  = " + pStats.stddev());
        System.out.println("95% confidence interval = [" + pStats.confidenceLo() + ", " + pStats.confidenceHi() + "]");
    }
}
