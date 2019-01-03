package com.tass;

import com.tass.api.AirportViews;
import com.tass.service.AirportViewsService;
import com.tass.service.GraphService;
import com.tass.service.WikiService;
import edu.uci.ics.jung.graph.UndirectedGraph;
import edu.uci.ics.jung.graph.DirectedGraph;
import com.tass.api.graph.Value;

import java.util.*;

public class Main {
    private static final boolean GENERATE_FROM_SCRATCH = false;

    public static void main(String[] args) {
        GraphService graphService = GraphService.getInstance();
        AirportViewsService airportViewsService = AirportViewsService.getInstance();
        UndirectedGraph<String, Value> graph = graphService.generateGraph(GENERATE_FROM_SCRATCH);

        String from = "20181001";
        String to = "20181101";
//        Collection<String> airports = graph.getVertices();
        //List<String> airports = new ArrayList<>(graph.getVertices());
        //Collection<String> partOfAirports = airports.subList(0,10);
//        airportViewsService.getAirportViewsToJsonFile(airports, from, to);
    }
}
