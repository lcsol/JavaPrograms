import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.util.HashSet;
import java.util.Set;

public class BoggleSolver {

    private final Trie dictionary;
    private final int[] adj = {-1, 0, 1, 0, -1, -1, 1, 1, -1};

    private class Node {
        private static final int R = 26;
        private Node[] next;
        private boolean isEnd;

        public Node() {
            next = new Node[R];
        }
        public void setEnd() {
            isEnd = true;
        }
        public boolean isEnd() {
            return isEnd;
        }
    }
    private class Trie {
        private final Node root;

        public Trie() {
            root = new Node();
        }
        public void insert(String s) {
            Node n = root;
            int len = s.length();
            for (int i = 0; i < len; i++) {
                char c = s.charAt(i);
                if (n.next[c - 'A'] == null) n.next[c - 'A'] = new Node();
                n = n.next[c - 'A'];
            }
            n.setEnd();
        }

        public boolean contains(String s) {
            Node n = searchPrefix(s);
            return n != null && n.isEnd();
        }
        private Node searchPrefix(String s) {
            Node n = root;
            int len = s.length();
            for (int i = 0; i < len; i++) {
                char c = s.charAt(i);
                if (n.next[c - 'A'] != null) n = n.next[c - 'A'];
                else return null;
            }
            return n;
        }


    }
    // Initializes the data structure using the given array of strings as the dictionary.
    // (You can assume each word in the dictionary contains only the uppercase letters A through Z.)
    public BoggleSolver(String[] dictionary) {
        this.dictionary = new Trie();
        for (String s : dictionary) {
            this.dictionary.insert(s);
        }
    }
    // Returns the set of all valid words in the given Boggle board, as an Iterable.
    public Iterable<String> getAllValidWords(BoggleBoard board) {
        int m = board.rows(), n = board.cols();
        char[][] b = new char[m][n];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                b[i][j] = board.getLetter(i, j);
            }
        }
        Set<String> allWords = new HashSet<>();
        boolean[][] marked = new boolean[m][n];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                boggleAux(b, i, j, "", allWords, marked);
            }
        }
        return allWords;
    }

    private void boggleAux(char[][] board, int i, int j, String s, Set<String> allWords, boolean[][] marked) {
        int m = board.length, n = board[0].length;
        char c = board[i][j];
//        if (node.next[c - 'A'] == null) return;
        s += (c == 'Q') ?  "QU" : c;
        if (!isPrefix(s)) return;
        if (s.length() > 2 && dictionary.contains(s)) allWords.add(s);
        marked[i][j] = true;
        int x, y;
        for (int a = 0; a < 8; a++) {
            x = i + adj[a];
            y = j + adj[a + 1];
            if (x < 0 || x >= m || y < 0 || y >= n || marked[x][y]) continue;
            boggleAux(board, x, y, s, allWords, marked);
        }
        marked[i][j] = false;
    }
    private boolean isPrefix(String s) {
        Node n = dictionary.searchPrefix(s);
        return n != null;
    }

    // Returns the score of the given word if it is in the dictionary, zero otherwise.
    // (You can assume the word contains only the uppercase letters A through Z.)
    public int scoreOf(String word) {
        if (!dictionary.contains(word)) return 0;
        int len = word.length();
        if (len <= 2) return 0;
        if (len == 3 || len == 4) return 1;
        if (len == 5) return 2;
        if (len == 6) return 3;
        if (len == 7) return 5;
        return 11;
    }

    public static void main(String[] args) {
        In in = new In(args[0]);
        String[] dictionary = in.readAllStrings();
        BoggleSolver solver = new BoggleSolver(dictionary);
        BoggleBoard board = new BoggleBoard(args[1]);
        int score = 0;
        for (String word : solver.getAllValidWords(board)) {
            StdOut.println(word);
            score += solver.scoreOf(word);
        }
        StdOut.println("Score = " + score);
    }
}
