package com.tass.service;

import com.tass.api.Plane;
import com.tass.api.Value;
import com.tass.utils.PlaneTypeEnum;
import edu.uci.ics.jung.graph.DirectedGraph;
import edu.uci.ics.jung.graph.DirectedSparseGraph;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;


public class GraphService {
    private static GraphService graphService;

    public static GraphService getInstance(){
        if(graphService == null){
            graphService = new GraphService();
        }
        return graphService;
    }

    public DirectedGraph<String, Value> generateGraph(Set<Plane> planes){
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
}
