package com.tass.service;

public class AirportViewsService {
    private static AirportViewsService airportViewsService;

    public static AirportViewsService getInstance () {
        if (airportViewsService == null)
            airportViewsService = new AirportViewsService();
        return airportViewsService;
    }
}
