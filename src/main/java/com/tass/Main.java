package com.tass;


import com.tass.api.Airport;
import com.tass.api.Plane;
import com.tass.service.JsonService;
import com.tass.service.WikiService;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;

public class Main {

    public static void main(String[] args) {
        WikiService wikiService = WikiService.getInstance();
        String from = "20181001";
        String to = "20181101";
        Airport airport = wikiService.getAirportViews("WMI", from, to);
        System.out.println(airport.getName() + ": " + airport.getViews());
    }
}
