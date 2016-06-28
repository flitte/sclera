package com.flitte.sclera;

import gaffer.store.schema.Schema;
import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.MultiGraph;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Map;

import static java.lang.Boolean.parseBoolean;

/**
 * @author flitte
 * @since 15/06/16.
 */
public class App {

    public static void main(final String args[]) throws FileNotFoundException {

        System.setProperty("org.graphstream.ui.renderer", "org.graphstream.ui.j2dviewer.J2DGraphRenderer");

        final InputStream dataSchema = new FileInputStream(args[0]);
        final InputStream dataTypes = App.class.getResourceAsStream("/dataTypes.json");
        final InputStream storeTypes = App.class.getResourceAsStream("/storeTypes.json");

        final Schema schema = Schema.fromJson(dataSchema);

        final Graph graph = new MultiGraph("Schema");

        graph.addAttribute("ui.quality");
        graph.addAttribute("ui.antialias");
        graph.addAttribute("ui.stylesheet", "url('file:///home/tom/projects/flitte/sclera/src/main/resources/gs.css')");

        schema.getEntities()
              .forEach((k, v) -> {
                  // Plot vertices (if not already added)...
                  if (graph.getNode(v.getVertex()) == null) {
                      graph.addNode(v.getVertex())
                           .setAttribute("ui.class", "vertex");
                  }

                  // Plot entities...
                  graph.addNode(k)
                       .setAttribute("ui.class", "entity");

                  // Plot entity properties...
                  graph.addNode(getPropertiesNodeText(v.getPropertyMap()))
                       .setAttribute("ui.class", "properties");

                  // Connect nodes to vertices...
                  graph.addEdge(k, v.getVertex(), k, false)
                       .setAttribute("ui.class", "entitytovertex");

                  // Connect entity nodes to entity property nodes...
                  graph.addEdge(k + "_properties", k, getPropertiesNodeText(v.getPropertyMap()))
                       .setAttribute("ui.class", "properties");
              });

        // Add edges between vertices...
        schema.getEdges()
              .forEach((k, v) -> {
                  graph.addEdge(k, v.getSource(), v.getDestination(), parseBoolean(v.getDirected()))
                       .setAttribute("ui.class", "vertextovertex");
              });

        graph.getNodeSet()
             .forEach(node -> node.setAttribute("ui.label", node.getId()));

        graph.getEdgeSet()
             .forEach(edge -> edge.setAttribute("ui.label", edge.getId()));

        graph.display();
    }

    private static String getPropertiesNodeText(final Map<String, String> map) {

        final StringBuilder sb = new StringBuilder();

        map.forEach((k, v) -> sb.append(k)
                                .append(": ")
                                .append(v)
                                .append(", "));

        return sb.toString();
//                 .substring(0, sb.length() - 2);
    }

}
