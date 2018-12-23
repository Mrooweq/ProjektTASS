package com.tass;

import com.tass.service.AirportViewsService;
import com.tass.service.GraphService;
import edu.uci.ics.jung.graph.DirectedGraph;
import org.json.JSONObject;

import java.util.*;

public class Main {
    private static final boolean GENERATE_FROM_SCRATCH = false;

    public static void main(String[] args) {
        GraphService graphService = GraphService.getInstance();
        AirportViewsService airportViewsService = AirportViewsService.getInstance();

        String from = "20181001";
        String to = "20181101";
        DirectedGraph graph = graphService.generateGraph(GENERATE_FROM_SCRATCH);
        List<String> airports = new ArrayList<>(graph.getVertices());
        Collection<String> partOfAirports = airports.subList(0,10);
        JSONObject airportViewsJson = airportViewsService.getAirportViews(partOfAirports, from, to);
        airportViewsService.print(airportViewsJson);
    }
}
