package part1_week2;

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class Permutation {
    public static void main(String[] args) {
        validateParam(args == null || args.length != 1);
        int k = Integer.parseInt(args[0]);

        if (k > 0) {
            RandomizedQueue<String> queue = new RandomizedQueue<>();
            while (!StdIn.isEmpty()) {
                queue.enqueue(StdIn.readString());
            }
            for (int i = 0; i < k; i++)
                StdOut.println(queue.dequeue());
        }
    }

    private static void validateParam(boolean invalid) {
        if (invalid)
            throw new IllegalArgumentException(
                    "Data invalid! please check your input!");
    }
}
