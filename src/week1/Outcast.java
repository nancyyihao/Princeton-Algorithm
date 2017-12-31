package week1;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class Outcast {
    private WordNet mWordNet;

    // constructor takes a WordNet object
    public Outcast(WordNet wordnet) {
        validateParam(wordnet);
        mWordNet = wordnet;
    }

    // given an array of WordNet nouns, return an outcast
    public String outcast(String[] nouns) {
        validateParam(nouns);

        int[] distance = new int[nouns.length];
        for (int i=0; i<nouns.length-1; i++){
            for (int j=i+1; j<nouns.length; j++){
                int dist = mWordNet.distance(nouns[i], nouns[j]);
                if (i != j) distance[j] += dist;
                            distance[i] += dist;
            }
        }
        int maxDistance = 0;
        int maxIndex = 0;
        for (int i=0; i < distance.length; i++){
            if (distance[i] > maxDistance){
                maxDistance = distance[i];
                maxIndex = i;
            }
        }
        return nouns[maxIndex];
    }

    private void validateParam(Object param) {
        if (param == null)
            throw new IllegalArgumentException("param must be not null");
    }

    // see test client below
    public static void main(String[] args) {
        WordNet wordnet = new WordNet(args[0], args[1]);
        Outcast outcast = new Outcast(wordnet);
        for (int t = 2; t < args.length; t++) {
            In in = new In(args[t]);
            String[] nouns = in.readAllStrings();
            StdOut.println(args[t] + ": " + outcast.outcast(nouns));
        }
    }
}
