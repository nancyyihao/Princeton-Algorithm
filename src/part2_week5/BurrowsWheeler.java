package part2_week5;

import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

public class BurrowsWheeler {

    private static final int R = 256;

    // apply Burrows-Wheeler transform, reading from standard input and writing to standard output
    public static void transform() {

        String msg = BinaryStdIn.readString();
        CircularSuffixArray array = new CircularSuffixArray(msg);

        int first = 0;
        while (first < array.length() && array.index(first) != 0) {
            first++;
        }
        BinaryStdOut.write(first);
        for (int i = 0; i < array.length(); i++) {
            BinaryStdOut.write(msg.charAt((array.index(i) + msg.length() - 1) % msg.length()));
        }
        BinaryStdOut.close();
    }

    // apply Burrows-Wheeler inverse transform, reading from standard input and writing to standard output
    public static void inverseTransform() {

        int first = BinaryStdIn.readInt();
        String t = BinaryStdIn.readString();
        int n = t.length();
        int[] count = new int[R + 1], next = new int[n];
        for (int i = 0; i < n; i++)
            count[t.charAt(i) + 1]++;
        for (int i = 1; i < R + 1; i++)
            count[i] += count[i - 1];
        for (int i = 0; i < n; i++)
            next[count[t.charAt(i)]++] = i;
        for (int i = next[first], c = 0; c < n; i = next[i], c++)
            BinaryStdOut.write(t.charAt(i));
        BinaryStdOut.close();
    }

    private static void validCondition(boolean invalid) {
        if (invalid)
            throw new IllegalArgumentException("input not valid!");
    }

    // if args[0] is '-', apply Burrows-Wheeler transform
    // if args[0] is '+', apply Burrows-Wheeler inverse transform
    public static void main(String[] args) {

        validCondition(args == null
                || args.length != 1
                || !"+".equals(args[0]) && !"-".equals(args[0]));

        if ("+".equals(args[0])) {
            inverseTransform();
        } else if ("-".equals(args[0])){
            transform();
        }
    }
}
