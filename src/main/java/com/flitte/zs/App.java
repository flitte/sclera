package com.flitte.zs;

import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.SingleGraph;

/**
 * @author flitte
 * @since 15/06/16.
 */
public class App {
    public static void main(final String args[]) {
        final Graph graph = new SingleGraph("Test");

        graph.addNode("A");
        graph.addNode("B");
        graph.addNode("C");
        graph.addEdge("AB", "A", "B");
        graph.addEdge("BC", "B", "C");
        graph.addEdge("CA", "C", "A");

        graph.display();
    }

}
