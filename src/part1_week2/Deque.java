package part1_week2;

import edu.princeton.cs.algs4.StdOut;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {

    private Node first;
    private Node last;
    private int size;
    public Deque() {
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }

    public void addFirst(Item item) {
       if (item == null) throw new IllegalArgumentException();
       Node node = new Node();
       node.value = item;
       node.next = first;
       node.prev = null;
       if (first != null) {
           first.prev = node;
       }
        if (last == null) last = node;
        first = node;
       size++;
    }

    public void addLast(Item item) {
        if (item == null) throw new IllegalArgumentException();
        Node node = new Node();
        node.value = item;
        node.next = null;
        node.prev = last;
        if (last != null) {
            last.next = node;
        }
        if (first == null) first = node;
        last = node;
        size++;
    }

    public Item removeFirst() {
        if (isEmpty()) throw new NoSuchElementException();
        Node tempFirst = first;
        first = first.next;
        if (first != null) first.prev = null;
        tempFirst.prev = null;
        tempFirst.next = null;
        size--;
        return tempFirst.value;
    }

    public Item removeLast() {
        if (isEmpty()) throw new NoSuchElementException();
        Node tempLast = last;
        last = last.prev;
        if (last != null) last.next = null;
        tempLast.prev = null;
        tempLast.next = null;
        size--;
        if (isEmpty()) first = null;
        return tempLast.value;
    }

    public Iterator<Item> iterator() {
        return new Itr(first);
    }

    private class Node {
        private Item value;
        private Node prev;
        private Node next;
    }

    public static void main(String[] args) {
//        Deque<Integer> deque = new Deque<>();
//        deque.addFirst(0);
//        deque.addFirst(1);
//        deque.removeFirst();
//        //deque.removeLast();
//        // expected 2 1

        Deque<Integer> deque = new Deque<Integer>();
        deque.addFirst(0);
        deque.removeLast();
        // expected empty

        for (Integer i : deque) {
            StdOut.println(i);
        }
    }

    private class Itr implements Iterator<Item> {

        Node firstNode;

        public Itr(Node first) {
            if (first != null) {
                this.firstNode = new Node();
                this.firstNode.value = first.value;
                this.firstNode.prev = first.prev;
                this.firstNode.next = first.next;
            }
        }

        @Override
        public boolean hasNext() {
            return firstNode != null;
        }

        @Override
        public Item next() {
            if (!hasNext()) throw new NoSuchElementException();
            Item next = firstNode.value;
            firstNode = firstNode.next;
            return next;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }
}
