package week1;


import edu.princeton.cs.algs4.DirectedCycle;
import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.ST;

import java.util.ArrayList;

public class WordNet {

    private ST<String, Bag<Integer>> mDataSet = new ST<>();
    private ArrayList<String> mAllNouns = new ArrayList<>();
    private Digraph mDigraph;
    private SAP mSAP;

    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms) {
        validateParam(hypernyms);
        validateParam(synsets);

        In synsetsIn = new In(synsets);
        int V = 0;
        while (synsetsIn.hasNextLine()) {
            String[] data = synsetsIn.readLine().split(",");

            int id = Integer.parseInt(data[0]);
            String[] nouns = data[1].split(" ");
            for (String noun : nouns) {
                if (mDataSet.contains(noun)) {
                    mDataSet.get(noun).add(id);
                } else {
                    Bag<Integer> bag = new Bag<Integer>();
                    bag.add(id);
                    mDataSet.put(noun, bag);
                }
            }
            V++;
            mAllNouns.add(data[1]);
        }

        mDigraph = new Digraph(V);
        boolean[] isNotRoots = new boolean[V];
        In hypernymsIn = new In(hypernyms);
        while (hypernymsIn.hasNextLine()) {
            String[] data = hypernymsIn.readLine().split(",");
            int childId = Integer.parseInt(data[0]);
            isNotRoots[childId] = true;
            for (int i=1; i< data.length; i++) {
                int parentId = Integer.parseInt(data[i]);
                mDigraph.addEdge(childId, parentId);
            }
        }

        if (mSAP == null) {
            mSAP = new SAP(mDigraph);
        }

        int rootCount = 0;
        for (boolean notRoot : isNotRoots)
            if (!notRoot) rootCount++;

        DirectedCycle cycle = new DirectedCycle(mDigraph);
        cycle.hasCycle();
        if (rootCount > 1 || cycle.hasCycle())
            throw new IllegalArgumentException("not a valid WordNet! " +
                    "please check your input!");
    }

    // returns all WordNet nouns
    public Iterable<String> nouns() {
        return mDataSet.keys();
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        validateParam(word);
        return mDataSet.contains(word);
    }

    // distance between nounA and nounB (defined below)
    public int distance(String nounA, String nounB) {
        validateParam(nounA);
        validateParam(nounB);

        Bag<Integer> bagOfNounA = mDataSet.get(nounA);
        Bag<Integer> bagOfNounB = mDataSet.get(nounB);
        return mSAP.length(bagOfNounA, bagOfNounB);
    }

    // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
    // in a shortest ancestral path (defined below)
    public String sap(String nounA, String nounB) {
        validateParam(nounA);
        validateParam(nounB);

        Bag<Integer> bagOfNounA = mDataSet.get(nounA);
        Bag<Integer> bagOfNounB = mDataSet.get(nounB);
        int ancestor = mSAP.ancestor(bagOfNounA, bagOfNounB);
        return mAllNouns.get(ancestor);
    }

    private void validateParam(Object param) {
        if (param == null)
            throw new IllegalArgumentException("param must be not null");
    }

    // do unit testing of this class
    public static void main(String[] args) {

    }
}
