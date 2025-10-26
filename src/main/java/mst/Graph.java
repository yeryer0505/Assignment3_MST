package mst;

import java.util.*;

public class Graph {
    private final List<String> vertices;    // ordered list of vertex ids
    private final List<Edge> edges;
    private final Map<String, List<Edge>> adj;

    public Graph(List<String> vertices, List<Edge> edges) {
        this.vertices = new ArrayList<>(vertices);
        this.edges = new ArrayList<>(edges);
        this.adj = new HashMap<>();
        for (String v : vertices) adj.put(v, new ArrayList<>());
        for (Edge e : edges) {
            // ensure vertices exist
            if (!adj.containsKey(e.getU()) || !adj.containsKey(e.getV())) {
                throw new IllegalArgumentException("Edge uses vertex not in vertices list: " + e);
            }
            adj.get(e.getU()).add(e);
            adj.get(e.getV()).add(e);
        }
    }

    public List<String> getVertices() { return Collections.unmodifiableList(vertices); }
    public List<Edge> getEdges() { return Collections.unmodifiableList(edges); }
    public List<Edge> adj(String v) {
        List<Edge> list = adj.get(v);
        return list == null ? Collections.emptyList() : Collections.unmodifiableList(list);
    }

    public int V() { return vertices.size(); }
    public int E() { return edges.size(); }

    public boolean isConnected() {
        if (vertices.isEmpty()) return true;
        Set<String> visited = new HashSet<>();
        Deque<String> stack = new ArrayDeque<>();
        stack.push(vertices.get(0));
        while (!stack.isEmpty()) {
            String cur = stack.pop();
            if (!visited.add(cur)) continue;
            for (Edge e : adj(cur)) {
                String w = e.other(cur);
                if (!visited.contains(w)) stack.push(w);
            }
        }
        return visited.size() == vertices.size();
    }

    public boolean hasCycle() {
        // union-find cycle detection (undirected)
        UnionFind uf = new UnionFind(vertices);
        for (Edge e : edges) {
            String u = e.getU(), v = e.getV();
            if (uf.find(u).equals(uf.find(v))) return true;
            uf.union(u, v);
        }
        return false;
    }
}
