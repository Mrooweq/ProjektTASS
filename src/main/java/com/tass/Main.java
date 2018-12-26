package com.tass;


import com.tass.api.Airport;
import com.tass.api.graph.Value;
import com.tass.service.GraphService;
import com.tass.service.WikiService;
import edu.uci.ics.jung.graph.UndirectedGraph;

public class Main {

    private static final boolean GENERATE_FROM_SCRATCH = false;

    public static void main(String[] args) {
        GraphService graphService = GraphService.getInstance();
        WikiService wikiService = WikiService.getInstance();

        UndirectedGraph<String, Value> graph = graphService.generateGraph(GENERATE_FROM_SCRATCH);

        String from = "20181001";
        String to = "20181101";
        Airport airport = wikiService.getAirportViews("WMI", from, to);
        System.out.println(airport.getName() + ": " + airport.getViews());
    }
}
