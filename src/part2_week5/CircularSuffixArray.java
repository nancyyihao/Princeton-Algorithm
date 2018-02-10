package part2_week5;

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;
import java.util.Comparator;

public class CircularSuffixArray {

    private Integer[] index;
    private int N;

    // circular suffix array of s
    public CircularSuffixArray(String s) {
        validCondition(s == null);
        N = s.length();
        index = new Integer[N];
        for (int i = 0; i< index.length; i++) {
            index[i] = i;
        }
        Arrays.sort(index, new Comparator<Integer>() {
            @Override
            public int compare(Integer i, Integer j) {
                if (i >= N || j >= N) return 0;
                if (s.charAt(i) < s.charAt(j)) return -1;
                if (s.charAt(i) > s.charAt(j)) return 1;
                return compare(i+1, j+1);
            }
        });
    }

    // length of s
    public int length() {
        return N;
    }

    // returns index of ith sorted suffix
    public int index(int i) {
        validCondition(i<0 || i>=N);
        return index[i];
    }

    private void validCondition(boolean invalid) {
        if (invalid)
            throw new IllegalArgumentException("input not valid!");
    }

    // unit testing (required)
    public static void main(String[] args) {
        //String s = "ABRACADABRA!";
        String s = StdIn.readString();
        CircularSuffixArray suffixArray = new CircularSuffixArray(s);
        // expected: 11 10 7 0 3 5 8 1 4 6 9 2
        for (int i=0; i< suffixArray.length(); i++) {
            StdOut.println(suffixArray.index(i));
        }
    }
}
