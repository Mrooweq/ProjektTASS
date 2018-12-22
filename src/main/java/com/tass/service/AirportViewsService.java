package com.tass.service;

import com.tass.api.graph.Value;
import edu.uci.ics.jung.graph.DirectedGraph;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class AirportViewsService {
    private static AirportViewsService airportViewsService;

    private static final boolean GENERATE_FROM_SCRATCH = false;

    private GraphService graphService;
    private WikiService wikiService;

    public AirportViewsService() {
        graphService = GraphService.getInstance();
        wikiService = WikiService.getInstance();
    }

    public static AirportViewsService getInstance () {
        if (airportViewsService == null)
            airportViewsService = new AirportViewsService();
        return airportViewsService;
    }

    public Map<String, Long> getAirportViews (String from, String to) {
        Map<String, Long> airportViews = new HashMap<>();
        DirectedGraph<String, Value> graph = graphService.generateGraph(GENERATE_FROM_SCRATCH);
        Collection<String> airports = graph.getVertices();

        for (String airport : airports) {
            Long views = wikiService.getAirportViews(airport, from, to);
            airportViews.put(airport, views);
        }

        return airportViews;
    }

    public void print (Map<String, Long> airportViews) {
        for (Map.Entry entry : airportViews.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
    }
 }
