package mst;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import java.io.*;
import java.lang.reflect.Type;
import java.util.*;

public class Graph {
    public final int n;
    public final List<Edge> edges;

    public Graph(int n, List<Edge> edges) {
        this.n = n;
        this.edges = edges;
    }

    public static List<Graph> loadFromFile(String path) throws IOException {
        Gson gson = new Gson();
        Reader r = new FileReader(path);
        Type listType = new TypeToken<List<JsonObject>>(){}.getType();
        List<JsonObject> arr = gson.fromJson(r, listType);
        List<Graph> result = new ArrayList<>();
        for (JsonObject jo : arr) {
            int n = jo.get("n").getAsInt();
            JsonArray eArr = jo.getAsJsonArray("edges");
            List<Edge> edges = new ArrayList<>();
            for (JsonElement e : eArr) {
                JsonObject o = e.getAsJsonObject();
                edges.add(new Edge(
                        o.get("u").getAsInt(),
                        o.get("v").getAsInt(),
                        o.get("w").getAsDouble()
                ));
            }
            result.add(new Graph(n, edges));
        }
        r.close();
        return result;
    }

    public List<List<Edge>> adjacencyList() {
        List<List<Edge>> adj = new ArrayList<>();
        for (int i = 0; i < n; i++) adj.add(new ArrayList<>());
        for (Edge e : edges) {
            adj.get(e.u).add(e);
            adj.get(e.v).add(e);
        }
        return adj;
    }
}