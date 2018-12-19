package com.tass;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tass.api.Plane;
import com.tass.api.graph.Tuple;
import com.tass.api.graph.Value;
import com.tass.service.GraphService;
import com.tass.service.JsonService;
import com.tass.service.WikiService;
import edu.uci.ics.jung.graph.DirectedGraph;
import edu.uci.ics.jung.graph.DirectedSparseGraph;
import edu.uci.ics.jung.graph.util.Pair;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class Main {

    private static final boolean GENERATE_FROM_SCRATCH = false;

    public static void main(String[] args) {
        JsonService jsonService = JsonService.getInstance();
        GraphService graphService = GraphService.getInstance();
        WikiService wikiService = WikiService.getInstance();
        ObjectMapper objectMapper = new ObjectMapper();
        DirectedGraph<String, Value> graph;

//        String from = "20181001";
//        String to = "20181101";
//        Airport airport = wikiService.getAirportViews("WMI", from, to);
//        System.out.println(airport.getName() + ": " + airport.getViews());


        if(GENERATE_FROM_SCRATCH){
            Set<Plane> planes = jsonService.getPlanesFromFiles();
            graph = graphService.generateGraph(planes);

            Set<Tuple> set = new HashSet<>();

            for (Value value : graph.getEdges()) {
                Pair<String> endpoints = graph.getEndpoints(value);
                Tuple tuple = new Tuple(endpoints.getFirst(), endpoints.getSecond(), value);
                set.add(tuple);
            }

            try {
                objectMapper.writeValue(new File("graph.json"), set);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else {
            Set<Tuple> set = new HashSet<>();
            try {
                set = objectMapper.readValue(new File("graph.json"), new TypeReference<Set<Tuple>>(){});
            } catch (IOException e) {
                e.printStackTrace();
            }

            graph = new DirectedSparseGraph<>();

            for (Tuple tuple : set) {
                graph.addEdge(tuple.getValue(), tuple.getFrom(), tuple.getTo());
            }
        }
    }
}
