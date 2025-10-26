package mst;

import java.util.*;

public class PrimMST {
    public static class Result {
        public final List<Edge> mstEdges;
        public final double totalCost;
        public final Map<String, Long> ops;
        public final double timeMs;

        public Result(List<Edge> mstEdges, double totalCost, Map<String, Long> ops, double timeMs) {
            this.mstEdges = mstEdges;
            this.totalCost = totalCost;
            this.ops = ops;
            this.timeMs = timeMs;
        }
    }

    private static class PQNode implements Comparable<PQNode> {
        final String from;
        final String to;
        final double weight;

        PQNode(String from, String to, double weight) {
            this.from = from;
            this.to = to;
            this.weight = weight;
        }

        @Override
        public int compareTo(PQNode o) {
            return Double.compare(this.weight, o.weight);
        }
    }

    public static Result compute(Graph g) {
        long t0 = System.nanoTime();

        Map<String, Long> ops = new HashMap<>();
        long edgeExams = 0;
        long heapPushes = 0;
        long heapPops = 0;

        List<Edge> mst = new ArrayList<>();
        double totalCost = 0.0;
        if (g.V() == 0) {
            ops.put("edge_examinations", edgeExams);
            ops.put("heap_pushes", heapPushes);
            ops.put("heap_pops", heapPops);
            return new Result(mst, totalCost, ops, 0.0);
        }

        Set<String> visited = new HashSet<>();
        PriorityQueue<PQNode> pq = new PriorityQueue<>();

        String start = g.getVertices().get(0);
        visited.add(start);
        for (Edge e : g.adj(start)) {
            pq.add(new PQNode(start, e.other(start), e.getWeight()));
            heapPushes++;
        }

        while (!pq.isEmpty() && visited.size() < g.V()) {
            PQNode node = pq.poll();
            heapPops++;
            if (visited.contains(node.to)) continue;
            visited.add(node.to);
            mst.add(new Edge(node.from, node.to, node.weight));
            totalCost += node.weight;
            for (Edge e : g.adj(node.to)) {
                edgeExams++;
                String other = e.other(node.to);
                if (!visited.contains(other)) {
                    pq.add(new PQNode(node.to, other, e.getWeight()));
                    heapPushes++;
                }
            }
        }

        long t1 = System.nanoTime();
        double timeMs = (t1 - t0) / 1_000_000.0;
        ops.put("edge_examinations", edgeExams);
        ops.put("heap_pushes", heapPushes);
        ops.put("heap_pops", heapPops);

        return new Result(mst, totalCost, ops, timeMs);
    }
}
