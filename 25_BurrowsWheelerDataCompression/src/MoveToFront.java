import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

import java.util.LinkedList;
import java.util.List;

public class MoveToFront {
    private static final int R = 256;
    // apply move-to-front encoding, reading from standard input and writing to standard output
    public static void encode() {
        char[] asc2 = new char[R];
        for (char i = 0; i < R; i++) asc2[i] = i;
        while (!BinaryStdIn.isEmpty()) {
            char c = BinaryStdIn.readChar();
            char i, pre = asc2[0], curr;
            for (i = 0; asc2[i] != c; i++) {
                curr = asc2[i];
                asc2[i] = pre;
                pre = curr;
            }
            BinaryStdOut.write(i, 8);
            asc2[0] = c;
            asc2[i] = pre;
        }
        BinaryStdOut.close();
    }

    // apply move-to-front decoding, reading from standard input and writing to standard output
    public static void decode() {
        List<Character> asc2 = new LinkedList<>();
        for (char i = 0; i < R; i++) asc2.add(i);
        while (!BinaryStdIn.isEmpty()) {
            char c = BinaryStdIn.readChar();
            char idx = asc2.get(c);
            BinaryStdOut.write(idx, 8);
            asc2.remove(c);
            asc2.add(0, idx);
        }
        BinaryStdOut.close();
    }

    // if args[0] is '-', apply move-to-front encoding
    // if args[0] is '+', apply move-to-front decoding
    public static void main(String[] args) {
        if (args[0].equals("-")) encode();
        else if (args[0].equals("+")) decode();
    }
}