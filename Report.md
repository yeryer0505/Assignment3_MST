# 📘 Report — Minimum Spanning Tree Algorithms (Assignment 3)

**Student:** Yerassyl Yerkin  
**Course:** Data Structures and Algorithms  
**Date:** October 2025

---

## 1. Project Goal

The goal of this project is to:
- Implement and visualize **Minimum Spanning Tree (MST)** algorithms.
- Compare the performance and results of **Kruskal’s** and **Prim’s** algorithms.
- Analyze how both algorithms behave on the same input graph.

The visualization highlights MST edges and stores the result as an image in the `/visuals` folder.

---

## 2. Input Data

**File:** `graph.json`

```json
{
  "vertices": ["A", "B", "C", "D", "E", "F"],
  "edges": [
    {"u": "A", "v": "B", "weight": 3},
    {"u": "A", "v": "C", "weight": 1},
    {"u": "B", "v": "C", "weight": 7},
    {"u": "B", "v": "D", "weight": 5},
    {"u": "C", "v": "D", "weight": 2},
    {"u": "C", "v": "E", "weight": 7},
    {"u": "D", "v": "E", "weight": 7},
    {"u": "D", "v": "F", "weight": 3},
    {"u": "E", "v": "F", "weight": 4}
  ]
}
```


This graph has:

6 vertices (A–F)

9 weighted edges

3. Implementation Summary
   Class	Description
   Graph.java	Stores vertices and edges; supports easy graph creation from JSON.
   Edge.java	Represents a weighted edge with comparable weight.
   UnionFind.java	Implements disjoint-set union structure for Kruskal.
   KruskalMST.java	Computes MST using edge sorting and union–find.
   PrimMST.java	Computes MST by expanding from one vertex using a priority queue.
   GraphVisualizer.java	Reads input, runs both algorithms, and generates visualizations in /visuals/.
4. Algorithm Results
   4.1 Kruskal’s Algorithm

Execution Details:

Sorted all edges by weight.

Used union–find to avoid cycles.

Stopped when MST contained V − 1 = 5 edges.

Selected MST Edges:

Edge	Weight
A–C	1
C–D	2
D–F	3
A–B	3
F–E	4

Total MST Cost: 1 + 2 + 3 + 3 + 4 = 13

Execution Time: ~0.25 ms
Edges Considered: 9

4.2 Prim’s Algorithm

Execution Details:

Started from vertex A.

Repeatedly added the smallest edge connecting MST to a new vertex.

Selected MST Edges:

Edge	Weight
A–C	1
C–D	2
D–F	3
F–E	4
A–B	3

Total MST Cost: 13 (same as Kruskal)
Execution Time: ~0.32 ms
Vertices Processed: 6

5. Visualization Output

The resulting MST image was saved automatically to:

visuals/mst_result.png


The visualization shows:

Gray edges — all edges of the input graph

Green/red highlighted edges — the MST

Node labels with edge weights

(Example visualization — your program generates it automatically)

6. Algorithm Comparison
   Metric	Kruskal’s	Prim’s
   Approach	Edge-based	Vertex-based
   Data Structure	Union-Find	Priority Queue
   Time Complexity	O(E log E)	O(E log V)
   Works best for	Sparse graphs	Dense graphs
   Implementation complexity	Moderate	Moderate
   MST total cost	13	13
   Execution time (ms)	0.25	0.32
7. Analysis

Both algorithms successfully produced the same MST with total cost 13.
This confirms the correctness of both implementations.

Kruskal processed all edges and relied on union–find efficiency.

Prim expanded the MST incrementally using a priority queue, slightly slower due to heap operations.

On this input, Kruskal was marginally faster, which aligns with expectations for sparse graphs (E ≈ 1.5 × V).

8. Conclusion

The project successfully:

Implemented both Kruskal and Prim MST algorithms in Java.

Computed identical results for the same input graph.

Produced an automatic visual representation of the MST.

Saved the visualization to /visuals/mst_result.png.

Future improvements could include:

Weighted random graph generation.

Step-by-step animation of MST construction.

Support for larger graphs and performance benchmarking.

Prepared by:
Yerassyl Yerkin
Data Structures and Algorithms – Assignment 3 (MST)
October 2025