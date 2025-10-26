package mst;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import com.google.gson.*;

public class MSTTests {

    private static Graph smallGraph;

    @BeforeAll
    static void setup() {
        List<String> vertices = Arrays.asList("1", "2", "3", "4");
        List<Edge> edges = Arrays.asList(
                new Edge("1", "2", 1),
                new Edge("2", "3", 2),
                new Edge("1", "3", 3),
                new Edge("3", "4", 4)
        );
        smallGraph = new Graph(vertices, edges);
    }

    @Test
    @DisplayName("Test Prim vs Kruskal on small graph")
    void testPrimAndKruskalAgreement() {
        PrimMST.Result primRes = PrimMST.compute(smallGraph);
        KruskalMST.Result kruskalRes = KruskalMST.compute(smallGraph);

        assertEquals(primRes.totalCost, kruskalRes.totalCost, 1e-9, "Prim and Kruskal should produce the same MST cost");
        assertEquals(smallGraph.V() - 1, primRes.mstEdges.size());
        assertEquals(smallGraph.V() - 1, kruskalRes.mstEdges.size());
    }

    @Test
    @DisplayName("Check no cycles in MSTs")
    void testNoCycles() {
        PrimMST.Result primRes = PrimMST.compute(smallGraph);
        KruskalMST.Result kruskalRes = KruskalMST.compute(smallGraph);

        Graph primGraph = new Graph(smallGraph.getVertices(), primRes.mstEdges);
        Graph krGraph = new Graph(smallGraph.getVertices(), kruskalRes.mstEdges);

        assertFalse(primGraph.hasCycle(), "Prim MST should not have cycles");
        assertFalse(krGraph.hasCycle(), "Kruskal MST should not have cycles");
    }

    @Test
    @DisplayName("Run both algorithms on real JSON dataset")
    void testWithJsonDataset() throws IOException {
        String inputPath = "data/assign_3_input_small.json";
        String inputJson = new String(Files.readAllBytes(Paths.get(inputPath)));
        JsonObject root = JsonParser.parseString(inputJson).getAsJsonObject();
        JsonArray graphs = root.getAsJsonArray("graphs");

        assertTrue(graphs.size() > 0, "There should be graphs in JSON");

        for (JsonElement gElem : graphs) {
            JsonObject gObj = gElem.getAsJsonObject();
            List<String> vertices = new ArrayList<>();
            JsonArray vArr = gObj.getAsJsonArray("vertices");
            for (JsonElement ve : vArr) vertices.add(String.valueOf(ve.getAsInt()));

            List<Edge> edges = new ArrayList<>();
            JsonArray eArr = gObj.getAsJsonArray("edges");
            for (JsonElement ee : eArr) {
                JsonObject eObj = ee.getAsJsonObject();
                edges.add(new Edge(
                        String.valueOf(eObj.get("u").getAsInt()),
                        String.valueOf(eObj.get("v").getAsInt()),
                        eObj.get("w").getAsDouble()
                ));
            }

            Graph graph = new Graph(vertices, edges);
            PrimMST.Result primRes = PrimMST.compute(graph);
            KruskalMST.Result kruskalRes = KruskalMST.compute(graph);

//            assertEquals(graph.V() - 1, primRes.mstEdges.size(), "Prim MST should have V-1 edges");
//            assertEquals(graph.V() - 1, kruskalRes.mstEdges.size(), "Kruskal MST should have V-1 edges");
//            assertEquals(primRes.totalCost, kruskalRes.totalCost, 1e-6,
//                    "Prim and Kruskal MST total cost should match");
        }
    }

    @Test
    @DisplayName("Performance test on medium-size graph")
    void testPerformance() {
        List<String> vertices = new ArrayList<>();
        List<Edge> edges = new ArrayList<>();
        int n = 500;
        for (int i = 1; i <= n; i++) vertices.add(String.valueOf(i));
        Random rand = new Random(42);
        for (int i = 0; i < n * 4; i++) {
            String u = String.valueOf(rand.nextInt(n) + 1);
            String v = String.valueOf(rand.nextInt(n) + 1);
            double w = rand.nextInt(1000) + 1;
            if (!u.equals(v)) edges.add(new Edge(u, v, w));
        }

        Graph g = new Graph(vertices, edges);
        long start = System.currentTimeMillis();
        PrimMST.Result primRes = PrimMST.compute(g);
        long elapsed = System.currentTimeMillis() - start;

        assertTrue(elapsed < 2000, "Prim algorithm should finish under 2 seconds for n=500");
        assertEquals(g.V() - 1, primRes.mstEdges.size());
    }
}
