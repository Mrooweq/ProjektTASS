package com.tass;


import com.tass.api.Plane;
import com.tass.api.Value;
import com.tass.service.GraphService;
import com.tass.service.JsonService;
import com.tass.service.WikiService;
import edu.uci.ics.jung.graph.DirectedGraph;

import java.util.Set;

public class Main {

    private static final boolean GENERATE_FROM_SCRATCH = false;

    public static void main(String[] args) {
        JsonService jsonService = JsonService.getInstance();
        GraphService graphService = GraphService.getInstance();
        WikiService wikiService = WikiService.getInstance();

//        String from = "20181001";
//        String to = "20181101";
//        Airport airport = wikiService.getAirportViews("WMI", from, to);
//        System.out.println(airport.getName() + ": " + airport.getViews());


        Set<Plane> planes = jsonService.getPlanesFromFiles();
        DirectedGraph<String, Value> graph = graphService.generateGraph(planes);

        System.out.println();
    }
}
