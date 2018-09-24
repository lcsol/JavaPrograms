import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.DirectedCycle;
import edu.princeton.cs.algs4.In;
// import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.HashMap;

public class WordNet {
    private final HashMap<String, ArrayList<Integer>> nounsMap; // map for nouns. key: single noun. value: index of all synsets containing this noun
    private final HashMap<Integer, String> synsetsMap; // map for synsets. key: index of a synset. value: synset
    private final SAP sap;

    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms) {
        if (synsets == null || hypernyms == null) throw new IllegalArgumentException();
        In inSyn = new In(synsets), inHyp = new In(hypernyms);
        nounsMap = new HashMap<>();
        synsetsMap = new HashMap<>();
        ArrayList<Integer> nounIdxList;
        int idx;
        String line;
        String[] split, synSplit;
        while (inSyn.hasNextLine()) {
            line = inSyn.readLine();
            split = line.split(",");
            idx = Integer.parseInt(split[0]);
            synsetsMap.put(idx, split[1]);
            synSplit = split[1].split(" ");
            for (String noun : synSplit) {
                nounIdxList = nounsMap.getOrDefault(noun, new ArrayList<>());
                nounIdxList.add(idx);
                nounsMap.put(noun, nounIdxList);
            }
        }
        Digraph G = new Digraph(synsetsMap.size());
        while (inHyp.hasNextLine()) {
            line = inHyp.readLine();
            split = line.split(",");
            idx = Integer.parseInt(split[0]);
            for (int i = 1; i < split.length; i++) G.addEdge(idx, Integer.parseInt(split[i]));
        }
        DirectedCycle dc = new DirectedCycle(G);
        if (dc.hasCycle()) throw new IllegalArgumentException();
        int rootCount = 0;
        for (int j = 0; j < synsetsMap.size(); j++) {
            if (G.outdegree(j) == 0) rootCount++;
        }
        if (rootCount != 1) throw new IllegalArgumentException();
        sap = new SAP(G);
    }

    // returns all WordNet nouns
    public Iterable<String> nouns() {
        return nounsMap.keySet();
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        if (word == null) throw new IllegalArgumentException();
        return nounsMap.containsKey(word);
    }

    // distance between nounA and nounB
    public int distance(String nounA, String nounB) {
        checkNoun(nounA);
        checkNoun(nounB);
        Iterable<Integer> a = nounsMap.get(nounA);
        Iterable<Integer> b = nounsMap.get(nounB);
        return sap.length(a, b);
    }

    // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
    // in a shortest ancestral path
    public String sap(String nounA, String nounB) {
        checkNoun(nounA);
        checkNoun(nounB);
        Iterable<Integer> a = nounsMap.get(nounA);
        Iterable<Integer> b = nounsMap.get(nounB);
        int ancestor = sap.ancestor(a, b);
        return synsetsMap.get(ancestor);
    }

    private void checkNoun(String s) {
        if (s == null || !isNoun(s)) throw new IllegalArgumentException();
    }

    // do unit testing of this class
    public static void main(String[] args) {
//        WordNet wn = new WordNet(args[0], args[1]);
//        StdOut.println(wn.nouns());
//        StdOut.println(wn.isNoun(null));
//        StdOut.println(wn.isNoun("xyxz"));
//        StdOut.println(wn.isNoun("entity"));
//        StdOut.println(wn.distance("word", "Aberdeen"));
//        StdOut.println(wn.sap("word", "Aberdeen"));
    }
}