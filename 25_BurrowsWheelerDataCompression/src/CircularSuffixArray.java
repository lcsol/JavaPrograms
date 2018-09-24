import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class CircularSuffixArray {
    private final String s;
    private final int[] index;
    private final int len;
    // circular suffix array of s
    public CircularSuffixArray(String s) {
        if (s == null) throw new IllegalArgumentException();
        this.s = s;
        len = this.s.length();
        index = new int[len];
        for (int i = 0; i < len; i++) index[i] = i;
        sort(this.s, 0, len - 1, 0);
    }
    private void sort(String str, int lo, int hi, int d) {
        if (hi - lo <= 15) {
            insertionSort(str, lo, hi, d);
            return;
        }
        int lt = lo, gt = hi;
        int v = charAt(str, index[lo], d);
        int i = lo + 1;
        while (i <= gt) {
            int t = charAt(str, index[i], d);
            if (t < v) exch(lt++, i++);
            else if (t > v) exch(i, gt--);
            else i++;
        }
        sort(str, lo, lt - 1, d);
        if (v >= 0) sort(str, lt, gt, d + 1);
        sort(str, gt + 1, hi, d);
    }
    private void insertionSort(String str, int lo, int hi, int d) {
        for (int i = lo; i <= hi; i++)
            for (int j = i; j > lo && less(str, j, j - 1, d); j--)
                exch(j, j - 1);
    }
    private boolean less(String str, int i, int j, int d) {
        int idxI = index[i], idxJ = index[j];
        for (; d < len; d++) {
            int ci = charAt(str, idxI, d), cj = charAt(str, idxJ, d);
            if (ci < cj) return true;
            if (ci > cj) return false;
        }
        return false;
    }
    private int charAt(String str, int i, int d) {
        int realIdx = (d + i) % len;
        return str.charAt(realIdx);
    }
    private void exch(int i, int j) {
        int temp = index[i];
        index[i] = index[j];
        index[j] = temp;
    }
    // length of s
    public int length() {
        return len;
    }
    // returns index of ith sorted suffix
    public int index(int i) {
        if (i < 0 || i > len - 1) throw new IllegalArgumentException();
        return index[i];
    }

    // unit testing (required)
    public static void main(String[] args) {
        String s = StdIn.readString();
        int n = s.length();
        CircularSuffixArray csa = new CircularSuffixArray(s);
        StdOut.println("String length is " + csa.length());
        for (int i = 0; i < n; i++) {
            StdOut.println(i + " : " + csa.index[i]);
        }
    }
}