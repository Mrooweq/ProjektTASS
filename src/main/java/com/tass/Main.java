package com.tass;


import com.tass.api.Airport;
import com.tass.api.Plane;
import com.tass.service.JsonService;
import com.tass.service.WikiService;

import java.util.Set;

public class Main {

    public static void main(String[] args) {
        WikiService wikiService = WikiService.getInstance();
        Airport airport = wikiService.getAirportViews("WMI", "", "");
        System.out.println(airport.getName() + ": " + airport.getViews());
    }
}
