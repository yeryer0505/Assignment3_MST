package mst;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UnionFind {
    private final Map<String, String> parent;
    private final Map<String, Integer> rank;

    private long findCalls = 0;
    private long unions = 0;

    public UnionFind(List<String> vertices) {
        parent = new HashMap<>();
        rank = new HashMap<>();
        for (String v : vertices) {
            parent.put(v, v);
            rank.put(v, 0);
        }
    }

    public String find(String x) {
        findCalls++;
        String p = parent.get(x);
        if (p == null) throw new IllegalArgumentException("Unknown vertex: " + x);
        if (!p.equals(x)) {
            String root = find(p);
            parent.put(x, root);
            return root;
        }
        return p;
    }

    public boolean union(String a, String b) {
        String ra = find(a);
        String rb = find(b);
        if (ra.equals(rb)) return false;
        int rka = rank.getOrDefault(ra, 0);
        int rkb = rank.getOrDefault(rb, 0);
        if (rka < rkb) parent.put(ra, rb);
        else if (rkb < rka) parent.put(rb, ra);
        else {
            parent.put(rb, ra);
            rank.put(ra, rka + 1);
        }
        unions++;
        return true;
    }

    public long getFindCalls() { return findCalls; }
    public long getUnions() { return unions; }
}
