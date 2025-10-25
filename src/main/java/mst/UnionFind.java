package mst;

public class UnionFind {
    private final int[] parent, rank;
    public long unions = 0, finds = 0;

    public UnionFind(int n) {
        parent = new int[n];
        rank = new int[n];
        for (int i = 0; i < n; i++) parent[i] = i;
    }

    public int find(int x) {
        finds++;
        if (parent[x] == x) return x;
        return parent[x] = find(parent[x]);
    }

    public boolean union(int a, int b) {
        unions++;
        int ra = find(a), rb = find(b);
        if (ra == rb) return false;
        if (rank[ra] < rank[rb]) parent[ra] = rb;
        else if (rank[ra] > rank[rb]) parent[rb] = ra;
        else {
            parent[rb] = ra;
            rank[ra]++;
        }
        return true;
    }
}
