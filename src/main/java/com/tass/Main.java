package com.tass;

import com.tass.api.graph.Value;
import com.tass.service.AirportViewsService;
import com.tass.service.GraphService;
import edu.uci.ics.jung.graph.UndirectedGraph;

public class Main {
    private static final boolean GENERATE_FROM_SCRATCH = false;

    public static void main(String[] args) {
        GraphService graphService = GraphService.getInstance();
        AirportViewsService airportViewsService = AirportViewsService.getInstance();
        UndirectedGraph<String, Value> graph = graphService.generateGraph(GENERATE_FROM_SCRATCH);

        String from = "20181001";
        String to = "20181101";

//        airportViewsService.getAirportViewsToJsonFile(airports, from, to);
    }
}
