import edu.princeton.cs.algs4.StdOut;

public class TestDeque {
    public static void main(String[] args) {
        Deque<String> deque = new Deque<>();
        deque.addFirst("first");
        deque.addFirst("newFirst");
        deque.addLast("last");
        deque.addLast("newLast");
        StdOut.println(deque.iterator());
        deque.removeFirst();
        deque.removeLast();
        StdOut.println(deque.iterator());
    }
}
