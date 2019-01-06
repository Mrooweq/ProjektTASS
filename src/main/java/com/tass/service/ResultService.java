package com.tass.service;


import com.tass.api.AirportViews;
import com.tass.api.graph.Value;
import edu.uci.ics.jung.graph.UndirectedGraph;
import edu.uci.ics.jung.graph.util.Pair;

import java.util.*;
import java.util.stream.Collectors;

public class ResultService {
    private static ResultService resultService;

    public static ResultService getInstance(){
        if(resultService == null){
            resultService = new ResultService();
        }
        return resultService;
    }

    public static void main(String [] args){
        GraphService graphService = GraphService.getInstance();
        AirportViewsService airportViewsService = AirportViewsService.getInstance();

        UndirectedGraph<String, Value> graph = graphService.generateGraph(false);

        Set<AirportViews> airportViewsFromJson = airportViewsService.getAirportViewsFromJson();

        Map<String, Long> mapOfCentrality = new HashMap<>();
        Map<String, Long> mapOfViews = new HashMap<>();

        airportViewsFromJson.forEach(x -> mapOfViews.put(x.getCode(), x.getViews()));
        graph.getVertices().forEach(x -> mapOfCentrality.put(x, 0L));

        for (Value value : graph.getEdges()) {
            Pair<String> endpoints = graph.getEndpoints(value);
            String first = endpoints.getFirst();
            String second = endpoints.getSecond();
            long val = value.getValue();

            mapOfCentrality.put(first, mapOfCentrality.get(first) + val);
            mapOfCentrality.put(second, mapOfCentrality.get(second) + val);
        }

        List<Map.Entry<String, Long>> collect = mapOfCentrality.entrySet().stream().sorted(Comparator.comparing(Map.Entry::getValue)).collect(Collectors.toList());

        for (Map.Entry<String, Long> entry : collect) {
            String code = entry.getKey();
            System.out.println(code + ";" + entry.getValue() + ";" + mapOfViews.get(code));
        }
    }
}
