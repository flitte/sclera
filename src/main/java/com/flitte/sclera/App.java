package com.flitte.sclera;

import gaffer.store.schema.Schema;
import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.SingleGraph;

import java.io.InputStream;

import static java.lang.Boolean.parseBoolean;

/**
 * @author flitte
 * @since 15/06/16.
 */
public class App {

    public static void main(final String args[]) {

        final InputStream dataSchema = App.class.getResourceAsStream("/dataSchema.json");
        final InputStream dataTypes = App.class.getResourceAsStream("/dataTypes.json");
        final InputStream storeTypes = App.class.getResourceAsStream("/storeTypes.json");

        final Schema schema = Schema.fromJson(dataSchema);

        System.out.println(schema.toString());

        System.out.println(schema.getEdges()
                                 .toString());

        System.out.println(schema.getEntities()
                                 .toString());

        final Graph graph = new SingleGraph("Test");

        schema.getEntities()
              .forEach((k, v) -> {
                  // Plot vertices (if not already added)...
                  if (graph.getNode(v.getVertex()) == null) {
                      System.out.println("Added node: " + v.getVertex());
                      graph.addNode(v.getVertex());
                  }

                  // Plot entities...
                  graph.addNode(k);
                  System.out.println("Added node: " + k);

                  // Connect nodes to vertices...
                  graph.addEdge(k, v.getVertex(), k, false);
              });

        schema.getEdges()
              .forEach((k, v) -> {
                  graph.addEdge(k, v.getSource(), v.getDestination(), Boolean.parseBoolean(v.getDirected()));
              });

        graph.getNodeSet()
             .forEach(node -> node.setAttribute("ui.label", node.getId()));

        graph.getEdgeSet()
             .forEach(edge -> edge.setAttribute("ui.label", edge.getId()));

//        graph.addNode("A");
//        graph.addNode("B");
//        graph.addNode("C");
//        graph.addEdge("AB", "A", "B");
//        graph.addEdge("BC", "B", "C");
//        graph.addEdge("CA", "C", "A");

        graph.display();
    }

}
