package mst;

import com.google.gson.*;
import java.io.*;
import java.nio.file.*;
import java.util.*;

public class Runner {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public static void main(String[] args) throws IOException {
        String dataDir = "data";
        if (args.length >= 1) dataDir = args[0];

        List<Path> inputFiles = new ArrayList<>();
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get(dataDir), "assign_3_input_*.json")) {
            for (Path entry : stream) {
                inputFiles.add(entry);
            }
        }

        if (inputFiles.isEmpty()) {
            System.err.println("No input files found in " + dataDir);
            return;
        }

        System.out.println("Found " + inputFiles.size() + " input datasets.");

        for (Path inputPath : inputFiles) {
            processDataset(inputPath);
        }

        System.out.println("\n All datasets processed successfully!");
    }

    private static void processDataset(Path inputPath) throws IOException {
        String fileName = inputPath.getFileName().toString();
        String baseName = fileName.replace("input", "output").replace(".json", "");
        String outputPath = inputPath.getParent().resolve(baseName + ".json").toString();
        String summaryCsv = inputPath.getParent().resolve(baseName + "_summary.csv").toString();

        System.out.println("\n Processing: " + fileName);

        String inputJson = new String(Files.readAllBytes(inputPath));
        JsonElement rootElem = JsonParser.parseString(inputJson);

        JsonArray graphs;
        if (rootElem.isJsonObject() && rootElem.getAsJsonObject().has("graphs")) {
            graphs = rootElem.getAsJsonObject().getAsJsonArray("graphs");
        } else if (rootElem.isJsonArray()) {
            graphs = rootElem.getAsJsonArray();
        } else {
            throw new IllegalStateException("Invalid JSON format in " + fileName);
        }

        JsonArray outDatasets = new JsonArray();
        List<String[]> csvRows = new ArrayList<>();
        csvRows.add(new String[]{
                "name", "V", "E",
                "prim_cost", "kruskal_cost",
                "prim_time_ms", "kruskal_time_ms",
                "prim_ops", "kr_ops"
        });

        for (JsonElement gElem : graphs) {
            JsonObject gObj = gElem.getAsJsonObject();
            String name = gObj.has("id") ? "graph_" + gObj.get("id").getAsString() : "unnamed";

            List<String> vertices = new ArrayList<>();
            if (gObj.has("vertices")) {
                JsonArray vArr = gObj.getAsJsonArray("vertices");
                for (JsonElement ve : vArr) vertices.add(String.valueOf(ve.getAsInt()));
            } else if (gObj.has("n")) {
                int n = gObj.get("n").getAsInt();
                for (int i = 0; i < n; i++) vertices.add(String.valueOf(i));
            }

            List<Edge> edges = new ArrayList<>();
            JsonArray eArr = gObj.getAsJsonArray("edges");
            for (JsonElement ee : eArr) {
                JsonObject eObj = ee.getAsJsonObject();
                String u = String.valueOf(eObj.get("u").getAsInt());
                String v = String.valueOf(eObj.get("v").getAsInt());
                double w = eObj.get("w").getAsDouble();
                edges.add(new Edge(u, v, w));
            }

            Graph graph = new Graph(vertices, edges);
            PrimMST.Result primRes = PrimMST.compute(graph);
            KruskalMST.Result krRes = KruskalMST.compute(graph);

            boolean primValid = primRes.mstEdges.size() == Math.max(0, graph.V() - 1)
                    && !new Graph(graph.getVertices(), primRes.mstEdges).hasCycle();
            boolean krValid = krRes.mstEdges.size() == Math.max(0, graph.V() - 1)
                    && !new Graph(graph.getVertices(), krRes.mstEdges).hasCycle();

            JsonObject datasetOut = new JsonObject();
            datasetOut.addProperty("name", name);
            datasetOut.addProperty("V", graph.V());
            datasetOut.addProperty("E", graph.E());

            JsonObject primObj = new JsonObject();
            primObj.add("mst_edges", edgesToJson(primRes.mstEdges));
            primObj.addProperty("total_cost", primRes.totalCost);
            primObj.add("ops", mapLongToJson(primRes.ops));
            primObj.addProperty("time_ms", primRes.timeMs);
            primObj.addProperty("valid", primValid);

            JsonObject krObj = new JsonObject();
            krObj.add("mst_edges", edgesToJson(krRes.mstEdges));
            krObj.addProperty("total_cost", krRes.totalCost);
            krObj.add("ops", mapLongToJson(krRes.ops));
            krObj.addProperty("time_ms", krRes.timeMs);
            krObj.addProperty("valid", krValid);

            datasetOut.add("prim", primObj);
            datasetOut.add("kruskal", krObj);
            outDatasets.add(datasetOut);

            csvRows.add(new String[]{
                    name,
                    String.valueOf(graph.V()),
                    String.valueOf(graph.E()),
                    String.valueOf(primRes.totalCost),
                    String.valueOf(krRes.totalCost),
                    String.format(Locale.ROOT, "%.3f", primRes.timeMs),
                    String.format(Locale.ROOT, "%.3f", krRes.timeMs),
                    GSON.toJson(primRes.ops),
                    GSON.toJson(krRes.ops)
            });

            System.out.printf("✅ %s: V=%d E=%d Prim=%.2f Kruskal=%.2f%n",
                    name, graph.V(), graph.E(), primRes.totalCost, krRes.totalCost);
        }

        JsonObject outRoot = new JsonObject();
        outRoot.add("datasets", outDatasets);
        try (Writer w = new FileWriter(outputPath)) {
            GSON.toJson(outRoot, w);
        }

        try (PrintWriter pw = new PrintWriter(new FileWriter(summaryCsv))) {
            for (String[] row : csvRows) {
                pw.println(String.join(",", escapeCsvRow(row)));
            }
        }

        System.out.println("Output JSON saved to: " + outputPath);
        System.out.println("Summary CSV saved to: " + summaryCsv);
    }

    private static JsonArray edgesToJson(List<Edge> edges) {
        JsonArray arr = new JsonArray();
        for (Edge e : edges) {
            JsonArray t = new JsonArray();
            t.add(e.getU());
            t.add(e.getV());
            t.add(e.getWeight());
            arr.add(t);
        }
        return arr;
    }

    private static JsonObject mapLongToJson(Map<String, Long> map) {
        JsonObject o = new JsonObject();
        for (Map.Entry<String, Long> en : map.entrySet()) o.addProperty(en.getKey(), en.getValue());
        return o;
    }

    private static String[] escapeCsvRow(String[] row) {
        String[] out = new String[row.length];
        for (int i = 0; i < row.length; i++) {
            String s = row[i];
            if (s == null) s = "";
            if (s.contains(",") || s.contains("\"") || s.contains("\n")) {
                s = "\"" + s.replace("\"", "\"\"") + "\"";
            }
            out[i] = s;
        }
        return out;
    }
}
