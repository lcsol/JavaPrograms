import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private final int dimension;
    private int openNum;
    private boolean[] grid;
    private boolean[] top;
    private boolean[] bot;
    private final WeightedQuickUnionUF uf;
    private boolean percolate;

    public Percolation(int n) {
        if (n <= 0) throw new IllegalArgumentException("n must be larger than 0");

        dimension = n;
        openNum = 0;
        grid = new boolean[n * n];
        top = new boolean[n * n];
        bot = new boolean[n * n];
        uf = new WeightedQuickUnionUF(n * n);
        percolate = false;
    }

    private int xyTo1D(int x, int y) {
        int id = (x - 1) * dimension + y - 1;
        return id;
    }

    private void isValid(int x, int y) {
        if (x <= 0 || x > dimension || y <= 0 || y > dimension)
            throw new IllegalArgumentException("parameters must be between 1 and dimension");
    }

    // open site (row, col) if it is not open already
    public void open(int row, int col) {
        isValid(row, col);

        int index = xyTo1D(row, col);
        boolean cTop = false;
        boolean cBot = false;

        if (!grid[index]) {
            grid[index] = true;
            if (row == 1) cTop = true; // if the site is in the first row, it is connected to the top
            if (row == dimension) cBot = true; // if the site is in the last row, it is connected to the bottom
            // check if the adjacent sites are open. If so, check if connected to the top and bottom and union the sites
            if (col - 1 >= 1 && isOpen(row, col - 1)) {
                if (top[uf.find(index)] || top[uf.find(index - 1)]) cTop = true;
                if (bot[uf.find(index)] || bot[uf.find(index - 1)]) cBot = true;
                uf.union(index, index - 1);
            }
            if (col + 1 <= dimension && isOpen(row, col + 1)) {
                if (top[uf.find(index)] || top[uf.find(index + 1)]) cTop = true;
                if (bot[uf.find(index)] || bot[uf.find(index + 1)]) cBot = true;
                uf.union(index, index + 1);
            }
            if (row - 1 >= 1 && isOpen(row - 1, col)) {
                if (top[uf.find(index)] || top[uf.find(index - dimension)]) cTop = true;
                if (bot[uf.find(index)] || bot[uf.find(index - dimension)]) cBot = true;
                uf.union(index, index - dimension);
            }
            if (row + 1 <= dimension && isOpen(row + 1, col)) {
                if (top[uf.find(index)] || top[uf.find(index + dimension)]) cTop = true;
                if (bot[uf.find(index)] || bot[uf.find(index + dimension)]) cBot = true;
                uf.union(index, index + dimension);
            }
            top[uf.find(index)] = cTop;
            bot[uf.find(index)] = cBot;
            openNum++;
            if (top[uf.find(index)] && bot[uf.find(index)]) percolate = true;
        }
    }
    // check if the site is open
    public boolean isOpen(int row, int col) {
        isValid(row, col);
        return grid[xyTo1D(row, col)];
    }
    // check if the site is full
    public boolean isFull(int row, int col) {
        isValid(row, col);
        return top[uf.find(xyTo1D(row, col))];
    }
    // number of open sites
    public int numberOfOpenSites() {
        return openNum;
    }
    // check if the system percolates
    public boolean percolates() {
        return percolate;
    }
}
