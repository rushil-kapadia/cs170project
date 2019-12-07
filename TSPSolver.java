/*
 * (C) Copyright 2018-2018, by Timofey Chudakov and Contributors.
 *
 * JGraphT : a free Java graph-theory library
 *
 * See the CONTRIBUTORS.md file distributed with this work for additional
 * information regarding copyright ownership.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0, or the
 * GNU Lesser General Public License v2.1 or later
 * which is available at
 * http://www.gnu.org/licenses/old-licenses/lgpl-2.1-standalone.html.
 *
 * SPDX-License-Identifier: EPL-2.0 OR LGPL-2.1-or-later
 */
package org.jgrapht;

import org.jgrapht.alg.cycle.*;
import org.jgrapht.alg.interfaces.*;
import org.jgrapht.alg.matching.blossom.v5.*;
import org.jgrapht.alg.spanning.*;
import org.jgrapht.graph.*;

import java.util.*;
import java.util.stream.*;
import java.io.*;
import org.jgrapht.alg.tour.TwoApproxMetricTSP;
import org.jgrapht.alg.tour.ChristofidesThreeHalvesApproxMetricTSP;
import org.jgrapht.alg.tour.HeldKarpTSP;



/**
 * A $3/2$-approximation algorithm for the metric TSP problem.
 * <p>
 * The <a href="https://en.wikipedia.org/wiki/Travelling_salesman_problem">travelling salesman
 * problem</a> (TSP) asks the following question: "Given a list of cities and the distances between
 * each pair of cities, what is the shortest possible route that visits each city exactly once and
 * returns to the origin city?". In the metric TSP, the intercity distances satisfy the triangle
 * inequality.
 * <p>
 * This is an implementation of the <a href="https://en.wikipedia.org/wiki/Christofides_algorithm">
 * Christofides algorithm</a>. The algorithms is a $3/2$-approximation assuming that the input graph
 * satisfies triangle inequality and all edge weights are nonnegative. The implementation requires
 * the input graph to be <i>undirected</i> and <i>complete</i>. The worst case running time
 * complexity is $\mathcal{O}(V^3E)$.
 * <p>
 * The algorithm performs following steps to compute the resulting tour:
 * <ol>
 * <li>Compute a minimum spanning tree in the input graph.</li>
 * <li>Find vertices with odd degree in the MST.</li>
 * <li>Compute minimum weight perfect matching in the induced subgraph on odd degree vertices.</li>
 * <li>Add edges from the minimum weight perfect matching to the MST (forming a pseudograph).</li>
 * <li>Compute an Eulerian cycle in the obtained pseudograph and form a closed tour in this
 * cycle.</li>
 * </ol>
 * <p>
 * The following two observations yield the $3/2$ approximation bound:
 * <ul>
 * <li>The cost of every minimum spanning tree is less than or equal to the cost of every
 * Hamiltonian cycle since after one edge removal every Hamiltonian cycle becomes a spanning
 * tree</li>
 * <li>Twice the cost of a perfect matching in a graph is less than or equal to the cost of every
 * Hamiltonian cycle. This follows from the fact that after forming a closed tour using the edges of
 * a perfect matching the cost of the edges not from the matching is greater than or equal to the
 * cost of the matching edges.</li>
 * </ul>
 * <p>
 * For more details, see <i>Christofides, N.: Worst-case analysis of a new heuristic for the
 * travelling salesman problem. Graduate School of Industrial Administration, Carnegie Mellon
 * University (1976).</i>
 *
 * @param <V> the graph vertex type
 * @param <E> the graph edge type
 * @author Timofey Chudakov
 * @author Dimitrios Michail
 */
public class TSPSolver<V, E>
{

    public static void main(String[] args) throws IOException {
        //UNCOMMENT TO RUN CHRIS OR DOUBLE
        run_chris();
        //run_double();
        //run_karp();
    }

    public static void run_chris() throws IOException  {
        Scanner main = new Scanner(new FileReader("christofides_inputs_and_outputs/christofides_inputs/main.txt"));
        int n = main.nextInt();
        String [] files = new String[n];
        for (int l = 0; l < n; l++) {
            files[l] = main.next();
            print("processing: " + files[l]);
        }
        for(int d = 0; d < n; d++) {

            Scanner inFile = new Scanner(new FileReader("twalok/bounce_bounce/" + files[d] + ".in"));
            BufferedWriter out = new BufferedWriter(new FileWriter("twalok/bounce_bounce_out/" + files[d] + ".out"));

            int num;

            ChristofidesThreeHalvesApproxMetricTSP a = new ChristofidesThreeHalvesApproxMetricTSP();
            Graph<String, DefaultEdge> g = new SimpleWeightedGraph<>(DefaultEdge.class);

            num = inFile.nextInt();
            String[] names = new String[num];
            //print(num);
            for (int i = 0; i < num; i++) {
                names[i] = inFile.next();
                g.addVertex(names[i]);
            }

            print("added vertices for " + files[d]);

            for (int i = 0; i < num; i++) {
                for (int j = 0; j < num; j++) {
                    double db = inFile.nextDouble();
                    //printl(db + " ");
                    if (i != j) {
                        addEdge(g, names[i], names[j], db);
                    }
                }
                //print("");
            }

            print("added edges for " + files[d]);

            GraphPath s = a.getTour(g);
            List vs = s.getVertexList();
            for (int i = 0; i < vs.size(); i++) {
                String r = (String) vs.get(i);
                out.write(r);
                if (i != vs.size() - 1) {
                    out.write(" ");
                }
            }


            out.close();
        }
    }

    public static void run_double()  throws IOException  {
        Scanner main = new Scanner(new FileReader("twalok/christofides_r2_inputs/main.txt"));
        int n = main.nextInt();
        String [] files = new String[n];
        for (int l = 0; l < n; l++) {
            files[l] = main.next();
            print("processing: " + files[l]);
        }
        for(int d = 0; d < n; d++) {

            Scanner inFile = new Scanner(new FileReader("twalok/christofides_r2_inputs/" + files[d] + ".in"));
            BufferedWriter out = new BufferedWriter(new FileWriter("twalok/two_r2_outputs/" + files[d] + ".out"));

            int num;

            TwoApproxMetricTSP a = new TwoApproxMetricTSP();
            Graph<String, DefaultEdge> g = new SimpleWeightedGraph<>(DefaultEdge.class);

            num = inFile.nextInt();
            String[] names = new String[num];
            //print(num);
            for (int i = 0; i < num; i++) {
                names[i] = inFile.next();
            }
            for (int i = num-1; i >= 0; i--) {
                g.addVertex(names[i]);
            }

            print("added vertices for " + files[d]);

            for (int i = 0; i < num; i++) {
                for (int j = 0; j < num; j++) {
                    double db = inFile.nextDouble();
                    //printl(db + " ");
                    if (i != j) {
                        addEdge(g, names[i], names[j], db);
                    }
                }
                //print("");
            }

            print("added edges for " + files[d]);

            GraphPath s = a.getTour(g);
            List vs = s.getVertexList();
            for (int i = 0; i < vs.size(); i++) {
                String r = (String) vs.get(i);
                out.write(r);
                if (i != vs.size() - 1) {
                    out.write(" ");
                }
            }


            out.close();
        }
    }

    //("twalok/opt_outputs/" + files[d] + ".out")

    public static void run_karp()  throws IOException  {
        Scanner main = new Scanner(new FileReader("twalok/christofides_inputs/main.txt"));
        int n = main.nextInt();
        String [] files = new String[n];
        for (int l = 0; l < n; l++) {
            files[l] = main.next();
            print("processing: " + files[l]);
        }
        for(int d = 0; d < n; d++) {

            Scanner inFile = new Scanner(new FileReader("twalok/christofides_inputs/" + files[d] + ".in"));
            BufferedWriter out = new BufferedWriter(new FileWriter("twalok/karp_outputs/" + files[d] + ".out"));

            int num;

            HeldKarpTSP a = new HeldKarpTSP();
            Graph<String, DefaultEdge> g = new SimpleWeightedGraph<>(DefaultEdge.class);

            num = inFile.nextInt();
            String[] names = new String[num];
            //print(num);

            int v = 0;
            for (int i = 0; i < num; i++) {
                names[i] = inFile.next();
//                g.addVertex(names[i]);
                v++;
            }
            print(v);

            if (v < 24) {
                for (int i = (num-1); i > -1; i--) {
//                    names[i] = inFile.next();
                    g.addVertex(names[i]);
//                    v++;
                }
                print("added vertices for " + files[d]);

                for (int i = 0; i < num; i++) {
                    for (int j = 0; j < num; j++) {
                        double db = inFile.nextDouble();
                        //printl(db + " ");
                        if (i != j) {
                            addEdge(g, names[i], names[j], db);
                        }
                    }
                    //print("");
                }

                print("added edges for " + files[d]);

                GraphPath s = a.getTour(g);
                List vs = s.getVertexList();
                for (int i = 0; i < vs.size(); i++) {
                    String r = (String) vs.get(i);
                    out.write(r);
                    if (i != vs.size() - 1) {
                        out.write(" ");
                    }
                }
            } else {
                print("TOO MANY VERTICES");
            }


            out.close();
        }
    }

    public static void print(Object s){
        System.out.println(s);
    }

    public static void printl(Object s){
        System.out.print(s);
    }

    public static void addEdge(Graph g, String a, String b, double d){
        g.addEdge(a, b);
        g.setEdgeWeight(a, b, d);
    }

}