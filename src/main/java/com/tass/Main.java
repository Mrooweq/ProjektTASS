package com.tass;

import com.tass.service.AirportViewsService;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        AirportViewsService airportViewsService = AirportViewsService.getInstance();

        String from = "20181001";
        String to = "20181101";
        Map<String,Long> airportViews = airportViewsService.getAirportViews(from, to);
        airportViewsService.print(airportViews);
    }
}
