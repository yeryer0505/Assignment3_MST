package mst;

import java.util.*;

public class KruskalMST {
    public static class Result {
        public final List<Edge> mstEdges;
        public final double totalCost;
        public final Map<String, Long> ops; // e.g., edge_considered, find_calls, unions
        public final double timeMs;

        public Result(List<Edge> mstEdges, double totalCost, Map<String, Long> ops, double timeMs) {
            this.mstEdges = mstEdges;
            this.totalCost = totalCost;
            this.ops = ops;
            this.timeMs = timeMs;
        }
    }

    public static Result compute(Graph g) {
        long t0 = System.nanoTime();
        List<Edge> edges = new ArrayList<>(g.getEdges());
        Collections.sort(edges);

        UnionFind uf = new UnionFind(g.getVertices());
        List<Edge> mst = new ArrayList<>();
        long edgeConsidered = 0;

        for (Edge e : edges) {
            edgeConsidered++;
            String u = e.getU();
            String v = e.getV();
            // find roots
            String ru = uf.find(u);
            String rv = uf.find(v);
            if (!ru.equals(rv)) {
                boolean merged = uf.union(ru, rv);
                if (merged) {
                    mst.add(e);
                }
            }
            if (mst.size() == Math.max(0, g.V() - 1)) break;
        }

        long t1 = System.nanoTime();
        double timeMs = (t1 - t0) / 1_000_000.0;
        Map<String, Long> ops = new HashMap<>();
        ops.put("edge_considered", edgeConsidered);
        ops.put("find_calls", uf.getFindCalls());
        ops.put("unions", uf.getUnions());
        double totalCost = mst.stream().mapToDouble(Edge::getWeight).sum();
        return new Result(mst, totalCost, ops, timeMs);
    }
}
