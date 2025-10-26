package mst;

public class Edge implements Comparable<Edge> {
    private final String u;
    private final String v;
    private final double weight;

    public Edge(String u, String v, double weight) {
        this.u = u;
        this.v = v;
        this.weight = weight;
    }

    public String either() { return u; }
    public String other(String vertex) {
        if (u.equals(vertex)) return v;
        if (v.equals(vertex)) return u;
        throw new IllegalArgumentException("Vertex " + vertex + " is not incident to this edge");
    }
    public String getU() { return u; }
    public String getV() { return v; }
    public double getWeight() { return weight; }

    @Override
    public int compareTo(Edge o) {
        return Double.compare(this.weight, o.weight);
    }

    @Override
    public String toString() {
        return String.format("(%s - %s : %.3f)", u, v, weight);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Edge)) return false;
        Edge e = (Edge) obj;
        return ((u.equals(e.u) && v.equals(e.v)) || (u.equals(e.v) && v.equals(e.u)))
                && Double.compare(weight, e.weight) == 0;
    }

    @Override
    public int hashCode() {
        String a = u.compareTo(v) <= 0 ? u : v;
        String b = u.compareTo(v) <= 0 ? v : u;
        int result = a.hashCode();
        result = 31 * result + b.hashCode();
        long bits = Double.doubleToLongBits(weight);
        result = 31 * result + (int)(bits ^ (bits >>> 32));
        return result;
    }
}
