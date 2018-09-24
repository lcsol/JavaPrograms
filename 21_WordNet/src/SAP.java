import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class SAP {
    private final Digraph G;
    // constructor takes a digraph (not necessarily a DAG)
    public SAP(Digraph G) {
        this.G = new Digraph(G); // call the copy constructor in order to make SAP immutable
    }

    private int[] sapAux(int v, int w) {
        int[] res = new int[2];
        int pathLen = Integer.MAX_VALUE;
        int ancestor = -1;
        BreadthFirstDirectedPaths vbfs = new BreadthFirstDirectedPaths(G, v);
        BreadthFirstDirectedPaths wbfs = new BreadthFirstDirectedPaths(G, w);
        for (int i = 0; i < G.V(); i++) {
            if (vbfs.hasPathTo(i) && wbfs.hasPathTo(i)) {
                int sum = vbfs.distTo(i) + wbfs.distTo(i);
                if (pathLen > sum) {
                    pathLen = sum;
                    ancestor = i;
                }
            }
        }
        if (pathLen == Integer.MAX_VALUE) pathLen = -1;
        res[0] = pathLen;
        res[1] = ancestor;
        return res;
    }

    // length of shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w) {
        int[] res = sapAux(v, w);
        return res[0];
    }

    // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
    public int ancestor(int v, int w) {
        int[] res = sapAux(v, w);
        return res[1];
    }

    private int[] sapAux(Iterable<Integer> v, Iterable<Integer> w) {
        int[] res = new int[2];
        int pathLen = Integer.MAX_VALUE;
        int ancestor = -1;
        BreadthFirstDirectedPaths vbfs = new BreadthFirstDirectedPaths(G, v);
        BreadthFirstDirectedPaths wbfs = new BreadthFirstDirectedPaths(G, w);
        for (int i = 0; i < G.V(); i++) {
            if (vbfs.hasPathTo(i) && wbfs.hasPathTo(i)) {
                int sum = vbfs.distTo(i) + wbfs.distTo(i);
                if (pathLen > sum) {
                    pathLen = sum;
                    ancestor = i;
                }
            }
        }
        if (pathLen == Integer.MAX_VALUE) pathLen = -1;
        res[0] = pathLen;
        res[1] = ancestor;
        return res;
    }
    // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        int[] res = sapAux(v, w);
        return res[0];
    }

    // a common ancestor that participates in shortest ancestral path; -1 if no such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        int[] res = sapAux(v, w);
        return res[1];
    }

    // do unit testing of this class
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