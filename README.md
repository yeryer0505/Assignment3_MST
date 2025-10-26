# 🧮 Assignment 3 Analytical Report
**Topic:** Optimization of a City Transportation Network (MST)  
**Student:** Yerkin Yerassyl  
**Group:** SE-2422  
**Course:** DAA

---

## 1. Summary of Input Data and Algorithm Results

In this experiment, both **Prim’s** and **Kruskal’s** algorithms were implemented in Java to find the **Minimum Spanning Tree (MST)** for multiple datasets of different sizes and densities.  
The algorithms were executed on **30 graphs** divided into four categories:

| Category | Graphs | Vertices Range | Avg Vertices | Avg Edges | Example Graph IDs |
|-----------|--------|----------------|---------------|-------------|-------------------|
| Small     | 5      | 5<30           | ~20           | ~28         | 1–5               |
| Medium    | 10     | 30<300         | ~140          | ~280        | 6–15              |
| Large     | 10     | 300<1000       | ~850          | ~2100       | 16–25             |
| X-Large   | 3      | 1000<2000      | ~2045         | ~6100       | 26–28             |

All graphs were **connected** and **undirected**, ensuring identical MST costs between both algorithms.

### Average Performance Summary (from `mst_metrics.csv`)

| Category | Avg Prim Time (ms) | Avg Kruskal Time (ms) | Avg Prim Ops | Avg Kruskal Ops | Avg MST Cost |
|-----------|--------------------|------------------------|----------------|------------------|----------------|
| Small     | 0.73               | 0.30                   | 52             | 52               | 683            |
| Medium    | 0.33               | 0.38                   | 517            | 515              | 3570           |
| Large     | 1.75               | 1.94                   | 4043           | 4041             | 19000          |
| X-Large   | 3.75               | 5.43                   | 11775          | 11727            | 40460          |

---

## 2. Comparison Between Prim’s and Kruskal’s Algorithms

### 🧠 Theoretical Comparison

| Feature | Prim’s Algorithm | Kruskal’s Algorithm |
|----------|------------------|---------------------|
| **Approach** | Expands MST from a single vertex | Sorts all edges and connects components via Union-Find |
| **Data Structure** | Priority Queue (Min-Heap) | Disjoint Set (Union-Find) |
| **Time Complexity** | O(E log V) | O(E log E) ≈ O(E log V) |
| **Space Complexity** | O(V + E) | O(V + E) |
| **Best Suited For** | Dense graphs | Sparse graphs |
| **Implementation Complexity** | Moderate | Simpler |
| **Edge Sorting Needed** | No | Yes |

---

### ⚙️ Practical Comparison (Based on Results)

| Observation | Prim’s Algorithm | Kruskal’s Algorithm |
|--------------|------------------|----------------------|
| Execution Speed (Small Graphs) | Slightly slower due to heap overhead | Faster on small datasets |
| Execution Speed (Medium–Large) | Faster for dense graphs | Slightly slower due to sorting |
| Scalability (X-Large) | Performs better (3.7 ms vs 5.4 ms) | Sorting cost increases with E |
| Operation Count | Nearly identical | Nearly identical |
| MST Cost | Identical | Identical |

✅ **Conclusion:**  
Both algorithms yield identical MST total costs, confirming correctness.  
**Prim’s** algorithm performs better on **larger and denser** datasets,  
while **Kruskal’s** is slightly faster for **small or sparse** graphs.

---
## 3. Appendix (Example Result Record)
**Graph 16 (Large)**
  - Vertices = 643, Edges = 10265
  - Prim → Cost = 1518.0 | Time = 2.453ms | Ops = 41023
  - Kruskal → Cost = 1518.0 | Time = 4.739ms | Ops = 11578

**Graph 28 (X-Large)**
  - Vertices = 1109, Edges = 18503
  - Prim → Cost = 2596.0 | Time = 6.918ms | Ops = 11503
  - Kruskal → Cost = 2596.0 | Time = 15.415ms | Ops = 23102

---

## 4. Conclusions

- **Correctness:**  
  Both algorithms produce identical MSTs for all datasets.

- **Performance:**
    - **Prim’s algorithm** outperforms on dense or large graphs due to efficient priority queue usage.
    - **Kruskal’s algorithm** runs slightly faster on small or sparse networks due to its simpler structure.

- **Complexity:**  
  Both follow **O(E log V)**, but Prim’s scales better with increasing edge density.

- **Recommendation:**
    - Use **Prim**

---

## 5. References

- **Programiz** — Prim’s Algorithm Explained with Examples

- **Programiz** — Kruskal’s Algorithm Explained with Examples

- **GeeksforGeeks**  — Difference Between Prim’s and Kruskal’s Algorithm

---

📘 Author

Yerassyl Yerkin
Assignment 3 — Minimum Spanning Tree (MST)
October 2025