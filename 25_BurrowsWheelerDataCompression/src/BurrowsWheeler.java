import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

public class BurrowsWheeler {
    private static final int R = 256;
    // apply Burrows-Wheeler transform, reading from standard input and writing to standard output
    public static void transform() {
        String s = BinaryStdIn.readString();
        int n = s.length();
        CircularSuffixArray csa = new CircularSuffixArray(s);
        for (int i = 0; i < n; i++) {
            if (csa.index(i) == 0) BinaryStdOut.write(i);
        }
        for (int i = 0; i < n; i++) {
            int idx = (csa.index(i) == 0) ? n - 1 : csa.index(i) - 1;
            BinaryStdOut.write(s.charAt(idx));
        }
        BinaryStdOut.close();
    }

    // apply Burrows-Wheeler inverse transform, reading from standard input and writing to standard output
    public static void inverseTransform() {
        int first = BinaryStdIn.readInt();
        String t = BinaryStdIn.readString();
        int n = t.length();
        int[] next = new int[n], count = new int[R + 1];
        for (int i = 0; i < n; i++) count[t.charAt(i) + 1]++;
        for (int r = 0; r < R; r++) count[r + 1] += count[r];
        for (int i = 0; i < n; i++) next[count[t.charAt(i)]++] = i;
        for (int i = 0, idx = next[first]; i < n; idx = next[idx], i++)
            BinaryStdOut.write(t.charAt(idx)); // the next[i] char in t is same as the i char in the first column
        BinaryStdOut.close();
    }

    // if args[0] is '-', apply Burrows-Wheeler transform
    // if args[0] is '+', apply Burrows-Wheeler inverse transform
    public static void main(String[] args) {
        if (args[0].equals("-")) transform();
        else if (args[0].equals("+")) inverseTransform();
    }
}