package part2_week1;

import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;


public class SAP {

    private static final int INFINITY = Integer.MAX_VALUE;

    private Digraph G;

    public SAP(Digraph G) {
        checkCondition(G == null);
        this.G = new Digraph(G);
    }

    // length of shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w) {
        checkCondition(v < 0 || v >= G.V());
        checkCondition(w < 0 || w >= G.V());
        return getLengthOf(v, w);
    }

    // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
    public int ancestor(int v, int w) {
        checkCondition(v < 0 || v >= G.V());
        checkCondition(w < 0 || w >= G.V());
        return getAncestorOf(v, w);
    }

    // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        checkCondition(v == null);
        checkCondition(w == null);
        for (int v1 : v) checkCondition(v1 < 0 || v1 >= G.V());
        for (int w1 : w) checkCondition(w1 < 0 || w1 >= G.V());

        return getLengthOf(v, w);
    }

    // a common ancestor that participates in shortest ancestral path; -1 if no such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        checkCondition(v == null);
        checkCondition(w == null);
        for (int v1 : v) checkCondition(v1 < 0 || v1 >= G.V());
        for (int w1 : w) checkCondition(w1 < 0 || w1 >= G.V());

        return getAncestorOf(v, w);
    }

    private int getAncestorOf(Object v, Object w) {
        int ancestor = -1;

        DeluxeBFS uv = new DeluxeBFS(G, v);
        DeluxeBFS uw = new DeluxeBFS(G, w);

        int result = INFINITY;
        for(int i = 0; i < G.V(); i++) {
            if(uv.hasPathTo(i) && uw.hasPathTo(i)) {
                int distance = uv.distTo(i) + uw.distTo(i);
                if(distance < result) {
                    result = distance;
                    ancestor = i;
                }
            }
        }
        return ancestor;
    }

    private int getLengthOf(Object v, Object w) {
        DeluxeBFS uv = new DeluxeBFS(G, v);
        DeluxeBFS uw = new DeluxeBFS(G, w);

        int result = INFINITY;
        for(int i = 0; i < G.V(); i++) {
            if(uv.hasPathTo(i) && uw.hasPathTo(i)) {
                int distance = uv.distTo(i) + uw.distTo(i);
                if(distance < result) {
                    result = distance;
                }
            }
        }
        return result == INFINITY ? -1 : result;
    }

    private void checkCondition(boolean inValid) {
        if (inValid)
            throw new IllegalArgumentException("param must be not null");
    }

    public static void main(String[] args) {
        In in = new In(args[0]);
        Digraph G = new Digraph(in);
        SAP sap = new SAP(G);
        while (!StdIn.isEmpty()) {
            int v = StdIn.readInt();
            int w = StdIn.readInt();
            int length   = sap.length(v, w);
            int ancestor = sap.ancestor(v, w);
            StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
        }
    }
}
