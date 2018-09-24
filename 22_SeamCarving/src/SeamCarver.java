import edu.princeton.cs.algs4.Picture;

public class SeamCarver {
    private Picture picture;
    private int row, col;
    private int[][] color; // RGB color of each pixel
    // create a seam carver object based on the given picture
    public SeamCarver(Picture picture) {
        if (picture == null) throw new IllegalArgumentException();
        this.picture = new Picture(picture);
        row = this.picture.height();
        col = this.picture.width();
        color = new int[row][col];
        for (int y = 0; y < row; y++) {
            for (int x = 0; x < col; x++) {
                color[y][x] = this.picture.get(x, y).getRGB();
            }
        }
    }
    // current picture
    public Picture picture() {
        Picture pic = new Picture(col, row);
        for (int y = 0; y < row; y++) {
            for (int x = 0; x < col; x++) {
                pic.setRGB(x, y, color[y][x]);
            }
        }
        return pic;
    }
    // width of current picture
    public int width() {
        return col;
    }
    // height of current picture
    public int height() {
        return row;
    }
    // energy of pixel at column x and row y
    public double energy(int x, int y) {
        if (x < 0 || x > col - 1 || y < 0 || y > row - 1) throw new IllegalArgumentException();
        if (y == 0 || y == row - 1 || x == 0 || x == col - 1) return 1000; // border of the image
        int[] x1 = getColor(x - 1, y), x2 = getColor(x + 1, y), y1 = getColor(x, y - 1), y2 = getColor(x, y + 1);
        double sq = energyAux(x1, x2) + energyAux(y1, y2);
        return Math.sqrt(sq);
    }
    private int[] getColor(int x, int y) {
        int[] res = new int[3];
        int rgb = color[y][x];
        res[0] = (rgb >> 16) & 0xff; // red
        res[1] = (rgb >> 8) & 0xff; // green
        res[2] = rgb & 0xff; // blue
        return res;
    }
    private double energyAux(int[] a, int[] b) {
        int red = b[0] - a[0], green = b[1] - a[1], blue = b[2] - a[2];
        return red * red + green * green + blue * blue;
    }
    // sequence of indices for horizontal seam
    public int[] findHorizontalSeam() {
        return findSeam(col, row, true);
    }
    // sequence of indices for vertical seam
    public int[] findVerticalSeam() {
        return findSeam(row, col, false);
    }

    private int[] findSeam(int r, int c, boolean isTransposed) {
        int vertex = r * c, idx, idx1, idx2;
        int[] seam = new int[r], edgeTo = new int[vertex];
        double[] disTo = new double[vertex];
        double[][] energy = new double[r][c]; // energy of each pixel
        for (int v = 0; v < vertex; v++) {
            disTo[v] = Double.POSITIVE_INFINITY;
            edgeTo[v] = 0;
        }
        for (int y = 0; y < r; y++) {
            for (int x = 0; x < c; x++) {
                idx = y * c + x;
                if (y == 0) { // top of image
                    energy[y][x] = 1000;
                    disTo[idx] = 1000;
                } else if (x == 0 || x == c - 1) { // left and right border of image
                    energy[y][x] = 1000;
                    disTo[idx] = disTo[idx - c] + 1000;
                } else {
                    if (y == r - 1) energy[y][x] = 1000;
                    else energy[y][x] = isTransposed ? energy(y, x) : energy(x, y);
                    for (int i = -1; i < 2; i++) { // relax each pixel three times
                        idx2 = y * c + x;
                        idx1 = (y - 1) * c + x + i;
                        if (disTo[idx2] > disTo[idx1] + energy[y][x]) {
                            disTo[idx2] = disTo[idx1] + energy[y][x];
                            edgeTo[idx2] = idx1;
                        }
                    }
                }
            }
        }
        double minDis = Double.POSITIVE_INFINITY, dis;
        int minIdx = 0;
        for (int i = 0, j = r - 1; i < c; i++) {
            idx = j * c + i;
            dis = disTo[idx];
            minDis = Math.min(minDis, dis);
            minIdx = (minDis == dis) ? idx : minIdx;
        }
        for (int i = r - 1; i >= 0; i--) { // backtrace edgeTo
            seam[i] = minIdx % c;
            minIdx = edgeTo[minIdx];
        }
        return seam;
    }
    // remove horizontal seam from current picture
    public void removeHorizontalSeam(int[] seam) {
        check(seam, col, row);
        int[][] transColor = transposeColor(color, row, col);
        removeSeam(seam, col, row, transColor, true);
    }
    private int[][] transposeColor(int[][] paraColor, int r, int c) {
        int[][] transColor = new int[c][r];
        for (int y = 0; y < r; y++) {
            for (int x = 0; x < c; x++) {
                transColor[x][y] = paraColor[y][x];
            }
        }
        return transColor;
    }
    // remove vertical seam from current picture
    public void removeVerticalSeam(int[] seam) {
        check(seam, row, col);
        removeSeam(seam, row, col, color, false);
    }
    private void removeSeam(int[] seam, int r, int c, int[][] paraColor, boolean isTransposed) {
        int[][] copyColor = new int[r][c - 1];
        for (int i = 0; i < r; i++) {
            System.arraycopy(paraColor[i], 0, copyColor[i], 0, seam[i]);
            System.arraycopy(paraColor[i], seam[i] + 1, copyColor[i], seam[i], c - seam[i] - 1);
        }
        if (!isTransposed) {
            col--;
            color = copyColor;
        } else {
            row--;
            color = transposeColor(copyColor, r, c - 1);
        }
        picture = picture();
    }
    private void check(int[] seam, int size, int constrain) {
        if (seam == null || seam.length != size || constrain <= 1) throw new IllegalArgumentException();
        for (int i = 0; i < seam.length; i++) {
            if (seam[i] < 0 || seam[i] >= constrain) throw new IllegalArgumentException();
            if (i > 0 && Math.abs(seam[i - 1] - seam[i]) > 1) throw new IllegalArgumentException();
        }
    }
}