package week1;

import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.Queue;

public class DeluxeBFS {
    private Queue<Integer> queue;
    private boolean[] marked;
    private int[] distTo;
    private Digraph G;

    public DeluxeBFS(Digraph G, Object sources) {
        this.G = G;

        queue = new Queue<>();
        marked = new boolean[G.V()];
        distTo = new int[G.V()];

        bfs(sources);
    }

    public boolean hasPathTo(int v) {
        return marked[v];
    }

    public int distTo(int v) {
        return distTo[v];
    }

    private void bfs(Object sources) {

        for (int i=0 ; i<G.V() ; i++) {
            distTo[i] = Integer.MAX_VALUE;
            marked[i] = false;
        }

        if (sources instanceof Iterable) {
            for (Integer s : (Iterable<? extends Integer>) sources) {
                queue.enqueue(s);
                marked[s] = true;
                distTo[s] = 0;
            }
        } else if (sources instanceof Integer) {
            Integer s = (Integer) sources;
            queue.enqueue(s);
            marked[s] = true;
            distTo[s] = 0;
        }

        while (!queue.isEmpty()) {
            int v = queue.dequeue();
            for (int w : G.adj(v))
                if (distTo[w] > distTo[v] + 1) {
                    distTo[w] = distTo[v] + 1;
                    if (!marked[w]) {
                        marked[w] = true;
                        queue.enqueue(w);
                    }
                }
        }
    }
}
