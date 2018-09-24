import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

public class Permutation {
    public static void main(String[] args) {
        int k = Integer.parseInt(args[0]);
        RandomizedQueue<String> q = new RandomizedQueue<>();
        for (double n = 1.0; !StdIn.isEmpty(); n++) {
            String item = StdIn.readString();
            if (k == 0) break;
            if (q.size() < k) q.enqueue(item);
            else if (StdRandom.uniform() < k / n) {
                q.dequeue();
                q.enqueue(item);
            }
        }
        while (!q.isEmpty()) StdOut.println(q.dequeue());
    }
}
