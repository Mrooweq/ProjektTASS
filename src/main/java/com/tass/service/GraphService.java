package com.tass.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tass.api.Plane;
import com.tass.api.graph.Tuple;
import com.tass.api.graph.Value;
import com.tass.utils.PlaneTypeEnum;
import edu.uci.ics.jung.graph.DirectedGraph;
import edu.uci.ics.jung.graph.DirectedSparseGraph;
import edu.uci.ics.jung.graph.util.Pair;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;


public class GraphService {
    private static GraphService graphService;
    private JsonService jsonService;

    public GraphService() {
        jsonService = JsonService.getInstance();
    }

    public static GraphService getInstance(){
        if(graphService == null){
            graphService = new GraphService();
        }
        return graphService;
    }

    private DirectedGraph<String, Value> generateGraph(Set<Plane> planes){
        DirectedGraph<String, Value> graph = new DirectedSparseGraph<>();

        List<Plane> copyOfPlanes = new ArrayList<>(planes);

        while(!copyOfPlanes.isEmpty()){
            Plane plane = copyOfPlanes.get(0);
            String from = plane.getFrom();
            String to = plane.getTo();

            Predicate<Plane> predicate = p -> p.getFrom().equals(from) && p.getTo().equals(to)
                    || p.getFrom().equals(to) && p.getTo().equals(from);

            Set<Plane> planesToCountValue = copyOfPlanes.stream().filter(predicate).collect(Collectors.toSet());

            long value = planesToCountValue.stream()
                    .mapToLong(x -> PlaneTypeEnum.valueOf(x.getPlaneType()).getNumberOfPassengers())
                    .sum();

            graph.addEdge(new Value(value), from, to);

            copyOfPlanes.removeAll(planesToCountValue);
        }

        return graph;
    }

    public DirectedGraph<String, Value> generateGraph(boolean generateFromScratch){
        ObjectMapper objectMapper = new ObjectMapper();
        DirectedGraph<String, Value> graph;

        if(generateFromScratch){
            Set<Plane> planes = jsonService.getPlanesFromFiles();
            graph = generateGraph(planes);

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
            }
            catch (FileNotFoundException e) {
                throw new RuntimeException("JSON file with graph cannot be found");
            }
            catch (IOException e) {
                e.printStackTrace();
            }

            graph = new DirectedSparseGraph<>();

            for (Tuple tuple : set) {
                graph.addEdge(tuple.getValue(), tuple.getFrom(), tuple.getTo());
            }
        }

        return graph;
    }
}
