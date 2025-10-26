package mst;

import com.google.gson.*;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.*;
import java.util.List;
import javax.imageio.ImageIO;

public class GraphVisualizer extends JPanel {
    private final List<String> vertices;
    private final List<Edge> edges;
    private final List<Edge> mstEdges;
    private final Map<String, Point> positions;
    private final String title;

    public GraphVisualizer(String title, List<String> vertices, List<Edge> edges, List<Edge> mstEdges) {
        this.title = title;
        this.vertices = vertices;
        this.edges = edges;
        this.mstEdges = mstEdges;
        this.positions = computePositions(vertices);
        setPreferredSize(new Dimension(800, 600));
        setBackground(Color.WHITE);
    }

    private Map<String, Point> computePositions(List<String> vertices) {
        Map<String, Point> pos = new HashMap<>();
        int n = vertices.size();
        int radius = 220;
        int centerX = 400;
        int centerY = 300;
        for (int i = 0; i < n; i++) {
            double angle = 2 * Math.PI * i / n;
            int x = (int) (centerX + radius * Math.cos(angle));
            int y = (int) (centerY + radius * Math.sin(angle));
            pos.put(vertices.get(i), new Point(x, y));
        }
        return pos;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setStroke(new BasicStroke(2));
        g2.setColor(Color.LIGHT_GRAY);
        for (Edge e : edges) {
            Point p1 = positions.get(e.getU());
            Point p2 = positions.get(e.getV());
            g2.draw(new Line2D.Double(p1.x, p1.y, p2.x, p2.y));

            int midX = (p1.x + p2.x) / 2;
            int midY = (p1.y + p2.y) / 2;
            g2.setColor(Color.DARK_GRAY);
            g2.drawString(String.format("%.0f", e.getWeight()), midX, midY);
            g2.setColor(Color.LIGHT_GRAY);
        }

        g2.setColor(Color.RED);
        g2.setStroke(new BasicStroke(3));
        for (Edge e : mstEdges) {
            Point p1 = positions.get(e.getU());
            Point p2 = positions.get(e.getV());
            g2.draw(new Line2D.Double(p1.x, p1.y, p2.x, p2.y));
        }

        for (String v : vertices) {
            Point p = positions.get(v);
            int r = 22;
            g2.setColor(new Color(255, 230, 150));
            g2.fillOval(p.x - r / 2, p.y - r / 2, r, r);
            g2.setColor(Color.BLACK);
            g2.drawOval(p.x - r / 2, p.y - r / 2, r, r);
            g2.drawString(v, p.x - 5, p.y - 10);
        }

        g2.setFont(new Font("SansSerif", Font.BOLD, 18));
        g2.setColor(Color.BLACK);
        g2.drawString(title + "  (Red = MST edges)", 20, 30);
    }

    public void saveAsImage(String filename) {
        BufferedImage image = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = image.createGraphics();
        this.paint(g2);
        g2.dispose();

        try {
            File output = new File(filename);
            ImageIO.write(image, "png", output);
            System.out.println("✅ Saved: " + filename);
        } catch (IOException e) {
            System.err.println("❌ Error saving image: " + e.getMessage());
        }
    }

    public static void showAndSaveGraph(String title, List<String> vertices, List<Edge> edges, List<Edge> mstEdges, int id) {
        GraphVisualizer panel = new GraphVisualizer(title, vertices, edges, mstEdges);

        JFrame frame = new JFrame(title);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(panel);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        SwingUtilities.invokeLater(() -> {
            String filename = String.format("visuals/%s.png", title.replace(" ", "_"));
            panel.saveAsImage(filename);
        });
    }

    public static void main(String[] args) throws Exception {
        JsonObject root = JsonParser.parseReader(new FileReader("data/assign_3_input_small.json")).getAsJsonObject();
        JsonArray graphs = root.getAsJsonArray("graphs");

        for (JsonElement gElem : graphs) {
            JsonObject gObj = gElem.getAsJsonObject();
            int graphId = gObj.get("id").getAsInt();
            String title = "Graph " + graphId;

            List<String> vertices = new ArrayList<>();
            for (JsonElement v : gObj.getAsJsonArray("vertices")) {
                vertices.add(String.valueOf(v.getAsInt()));
            }

            // Parse edges
            List<Edge> edges = new ArrayList<>();
            for (JsonElement eElem : gObj.getAsJsonArray("edges")) {
                JsonObject eObj = eElem.getAsJsonObject();
                edges.add(new Edge(
                        String.valueOf(eObj.get("u").getAsInt()),
                        String.valueOf(eObj.get("v").getAsInt()),
                        eObj.get("w").getAsDouble()
                ));
            }

            Graph graph = new Graph(vertices, edges);

            KruskalMST.Result result = KruskalMST.compute(graph);
            List<Edge> mstEdges = result.mstEdges;
            double totalCost = result.totalCost;

            SwingUtilities.invokeLater(() -> showAndSaveGraph(title, vertices, edges, mstEdges, graphId));
        }
    }
}
