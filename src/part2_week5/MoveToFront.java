package part2_week5;

import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

public class MoveToFront {

    private static final int R = 256; // extended ASCII

    // apply move-to-front encoding, reading from standard input and writing to standard output
    public static void encode() {
        char[] chars = createArray();
        while (!BinaryStdIn.isEmpty()) {
            char ch = BinaryStdIn.readChar();
            char tmpin, count, tmpout;
            for (count = 0, tmpout = chars[0]; ch != chars[count]; count++) {
                tmpin = chars[count];
                chars[count] = tmpout;
                tmpout = tmpin;
            }
            chars[count] = tmpout;
            BinaryStdOut.write(count);
            chars[0] = ch;
        }
        BinaryStdOut.close();
    }

    // apply move-to-front decoding, reading from standard input and writing to standard output
    public static void decode() {
        char[] chars = createArray();
        while (!BinaryStdIn.isEmpty()) {
            char count = BinaryStdIn.readChar();
            BinaryStdOut.write(chars[count], 8);
            char index = chars[count];
            while (count > 0) {
                chars[count] = chars[--count];
            }
            chars[0] = index;
        }
        BinaryStdOut.close();
    }

    private static char[] createArray() {
        char[] chars = new char[R];
        for (char i = 0; i < R; i++) {
            chars[i] = i;
        }
        return chars;
    }

    private static void validCondition(boolean invalid) {
        if (invalid)
            throw new IllegalArgumentException("input not valid!");
    }

    // if args[0] is '-', apply move-to-front encoding
    // if args[0] is '+', apply move-to-front decoding
    public static void main(String[] args) {
        validCondition(args == null
                || args.length != 1
                || !"+".equals(args[0]) && !"-".equals(args[0]));

        if ("+".equals(args[0])) {
            decode();
        } else if ("-".equals(args[0])){
            encode();
        }
    }
}
