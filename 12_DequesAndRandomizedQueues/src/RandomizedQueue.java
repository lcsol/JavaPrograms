import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;
import edu.princeton.cs.algs4.StdRandom;

public class RandomizedQueue<Item> implements Iterable<Item> {
    private Item[] queue; // array of items
    private int size; // number of items on queue

    // construct an empty randomized queue
    public RandomizedQueue() {
        queue = (Item[]) new Object[2];
        size = 0;
    }
    // is the randomized queue empty?
    public boolean isEmpty() {
        return size == 0;
    }
    // return the number of items on the randomized queue
    public int size() {
        return size;
    }
    // resize the underlying array
    private void resize(int capacity) {
        assert capacity >= size;
        queue = Arrays.copyOf(queue, capacity);
    }
    // add the item
    public void enqueue(Item item) {
        if (item == null) throw new IllegalArgumentException();
        if (size == queue.length) resize(2*queue.length);
        queue[size++] = item;
    }
    // remove and return a random item
    public Item dequeue() {
        if (size == 0) throw new NoSuchElementException();
        int index = StdRandom.uniform(size);
        Item result = queue[index];
        queue[index] = queue[size - 1];
        queue[--size] = null;
        if (size > 0 && size == queue.length / 4) resize(queue.length / 2);
        return result;
    }
    // return a random item (but do not remove it)
    public Item sample() {
        if (size == 0) throw new NoSuchElementException();
        int index = StdRandom.uniform(size);
        return queue[index];
    }
    // return an independent iterator over items in random order
    public Iterator<Item> iterator() {
        return new RandomIterator();
    }

    private class RandomIterator implements Iterator<Item> {
        private Item[] temp;
        private int i = 0;

        public RandomIterator() {
            temp = (Item[]) new Object[size];
            for (int j = 0; j < size; j++) {
                temp[j] = queue[j];
            }
            StdRandom.shuffle(temp);
        }

        public boolean hasNext() { return i < size; }

        public void remove() { throw new UnsupportedOperationException(); }

        public Item next() {
            if (!hasNext()) throw new NoSuchElementException();
            return temp[i++];
        }
    }
//    public static void main(String[] args) {
//    }
}
