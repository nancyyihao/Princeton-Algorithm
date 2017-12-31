package part1_week2;

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class RandomizedQueue<Item> implements Iterable<Item> {
    private static final int INITIAL_CAPACITY = 16;
    private static final int GROW_CAPACITY = 16;
    private Item[] items;
    private int size;

    public RandomizedQueue() {
        items = (Item[]) new Object[INITIAL_CAPACITY];
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }

    public void enqueue(Item item) {
        if (item == null)
            throw new IllegalArgumentException();

        ensureCapacity(size + 1);
        items[size++] = item;
    }

    public Item dequeue() {
        if (isEmpty())
            throw new NoSuchElementException();

        int index = StdRandom.uniform(size);
        Item oldValue = items[index];
        int numMoved = size - index - 1;
        if (numMoved > 0)
            System.arraycopy(items, index + 1, items, index,
                    numMoved);
        items[--size] = null;

        return oldValue;
    }

    public Item sample() {
        if (isEmpty())
            throw new NoSuchElementException();

        int index = StdRandom.uniform(size);
        return items[index];
    }

    public Iterator<Item> iterator() {
        return new Itr();
    }

    private void ensureCapacity(int minCapacity) {
        if (minCapacity > items.length) {
            int oldCapacity = items.length;
            int newCapacity = oldCapacity + GROW_CAPACITY;//(oldCapacity >> 1);
            if (newCapacity - minCapacity < 0)
                newCapacity = minCapacity;
            items = Arrays.copyOf(items, newCapacity);
        }
    }

    private class Itr implements Iterator<Item> {

        private Item[] randomItems;
        int cursor = 0;

        public Itr() {
            randomItems = Arrays.copyOf(items, size);
            StdRandom.shuffle(randomItems);
        }

        @Override
        public boolean hasNext() {
            return cursor < size;
        }

        @Override
        public Item next() {
            if (!hasNext()) throw new NoSuchElementException();
            return randomItems[cursor++];
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    public static void main(String[] args) {
        RandomizedQueue<Integer> queue = new RandomizedQueue<>();
        queue.enqueue(1);
        queue.enqueue(2);
        queue.enqueue(3);
        queue.enqueue(4);
        queue.dequeue();
        queue.dequeue();

        for (Integer i : queue) {
            StdOut.println(i);
        }
    }

}
