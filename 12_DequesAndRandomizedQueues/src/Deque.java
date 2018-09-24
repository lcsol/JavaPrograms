import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {
    private int size;
    private final Node sentinel;
    private Node first, last;

    private class Node {
        Item item;
        Node previous;
        Node next;
    }
    // construct an empty deque
    public Deque() {
        sentinel = new Node();
        sentinel.next = sentinel;
        sentinel.previous = sentinel;
        size = 0;
    }
    // is the deque empty?
    public boolean isEmpty() {
        return sentinel.next == sentinel;
    }
    // return the number of items on the deque
    public int size() {
        return size;
    }
    // add the item to the front
    public void addFirst(Item item) {
        if (item == null) throw new IllegalArgumentException();
        first = new Node();
        first.item = item;
        first.next = sentinel.next;
        first.previous = sentinel;
        sentinel.next.previous = first;
        sentinel.next = first;
        size++;
    }
    // add the item to the end
    public void addLast(Item item) {
        if (item == null) throw new IllegalArgumentException();
        last = new Node();
        last.item = item;
        last.next = sentinel;
        last.previous = sentinel.previous;
        sentinel.previous.next = last;
        sentinel.previous = last;
        size++;
    }
    // remove and return the item from the front
    public Item removeFirst() {
        if (isEmpty()) throw new NoSuchElementException();
        first = sentinel.next;
        Item item = first.item;
        sentinel.next = sentinel.next.next;
        sentinel.next.previous = sentinel;
        first.previous = null;
        first.next = null;
        size--;
        return item;
    }
    // remove and return the item from the end
    public Item removeLast() {
        if (isEmpty()) throw new NoSuchElementException();
        last = sentinel.previous;
        Item item = last.item;
        sentinel.previous = sentinel.previous.previous;
        sentinel.previous.next = sentinel;
        last.previous = null;
        last.next = null;
        size--;
        return item;
    }
    // return an iterator over items in order from front to end
    public Iterator<Item> iterator() {
        return new ListIterator();
    }
    private class ListIterator implements Iterator<Item> {
        private Node current = sentinel.next;

        public boolean hasNext() { return current != sentinel; }

        public void remove() { throw new UnsupportedOperationException(); }

        public Item next() {
            if (!hasNext()) throw new NoSuchElementException();
            Item item = current.item;
            current = current.next;
            return item;
        }
    }

//    public static void main(String[] args) {
//        Deque<String> deque = new Deque<>();
//        deque.addFirst("first");
//        deque.addFirst("newFirst");
//        deque.addLast("last");
//        deque.addLast("newLast");
//        //StdOut.println(deque.iterator());
//        deque.removeFirst();
//        deque.removeLast();
//    }
}
