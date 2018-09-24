
import java.util.Stack;

public class Board {
    private final char[] blocks;
    private final int dimension;
    private int hamming, manhattan;
    // construct a board from an n-by-n array of blocks
    // (where blocks[i][j] = block in row i, column j)
    public Board(int[][] blocks) {
        dimension = blocks.length;
        this.blocks = new char[dimension * dimension];
        hamming = 0;
        manhattan = 0;
        for (int row = 0; row < dimension; row++) {
            for (int col = 0; col < dimension; col++) {
                int val = blocks[row][col];
                int index = row * dimension + col;
                this.blocks[index] = (char) val;
                if (val != 0 && val != index + 1) {
                    hamming++;
                    manhattan += Math.abs(row - (val - 1) / dimension) + Math.abs(col - (val - 1) % dimension);
                }
            }
        }

    }

    // board dimension n
    public int dimension() {
        return dimension;
    }
    // number of blocks out of place
    public int hamming() {
        return hamming;
    }
    // sum of Manhattan distances between blocks and goal
    public int manhattan() {
        return manhattan;
    }
    // is this board the goal board?
    public boolean isGoal() {
        return hamming == 0;
    }
    // a board that is obtained by exchanging any pair of blocks
    public Board twin() {
        int[][] twinBlocks = new int[dimension][dimension];
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                twinBlocks[i][j] = blocks[i * dimension + j];
            }
        }
        if (twinBlocks[0][0] == 0 || twinBlocks[0][1] == 0) swap(twinBlocks, 1, 0, 1, 1);
        else swap(twinBlocks, 0, 0, 0, 1);
        Board twin = new Board(twinBlocks);
        return twin;
    }
    private void swap(int[][] array, int a, int b, int x, int y) {
        int temp = array[a][b];
        array[a][b] = array[x][y];
        array[x][y] = temp;
    }
    // does this board equal y?
    public boolean equals(Object y) {
        if (y == this) return true;
        if (y == null) return false;
        if (y.getClass() != this.getClass()) return false;
        Board that = (Board) y;
        if (this.dimension != that.dimension) return false;
        for (int i = 0; i < dimension * dimension; i++) {
            if (that.blocks[i] != this.blocks[i]) return false;
        }
        return true;
    }
    // all neighboring boards
    public Iterable<Board> neighbors() {
        Stack<Board> neighbors = new Stack<>();
        int[][] temp = new int[dimension][dimension];
        int row = 0, col = 0;
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                temp[i][j] = blocks[i * dimension + j];
                if (temp[i][j] == 0) {
                    row = i;
                    col = j;
                }
            }
        }
        addBoard(temp, dimension, row, col, row + 1, col, neighbors);
        addBoard(temp, dimension, row, col, row - 1, col, neighbors);
        addBoard(temp, dimension, row, col, row, col + 1, neighbors);
        addBoard(temp, dimension, row, col, row, col - 1, neighbors);
        return neighbors;
    }
    private void addBoard(int[][] array, int n, int a, int b, int x, int y, Stack<Board> stack) {
        if (x >= 0 && x < n && y >= 0 && y < n) {
            swap(array, a, b, x, y);
            stack.push(new Board(array));
            swap(array, a, b, x, y);
        }
    }

    // string representation of this board (in the output format specified below)
    public String toString() {
       StringBuilder s = new StringBuilder();
       s.append(dimension + "\n");
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                s.append(String.format("%2d ", (int) blocks[i * dimension + j]));
            }
            s.append("\n");
        }
        return s.toString();
    }

    // unit tests (not graded)
    public static void main(String[] args) {
//        In in = new In(args[0]);
//        int n = in.readInt();
//        int[][] blocks = new int[n][n];

    }
}
