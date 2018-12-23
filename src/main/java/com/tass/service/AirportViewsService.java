package com.tass.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tass.api.AirportViews;
import com.tass.exceptions.EngWikiURLNotFoundException;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.*;

public class AirportViewsService {
    private static final String POL_WIKIPEDIA_HOSTNAME = "pl.wikipedia.org";
    private static final String ENG_WIKIPEDIA_HOSTNAME = "en.wikipedia.org";
    private static final String WIKIPEDIA = ".*https://(?!en).{2,3}\\.wikipedia\\.org.*";

    private static AirportViewsService airportViewsService;

    private WikiService wikiService;
    private URLService urlService;
    private HTTPRequestService httpRequestService;
    private HtmlService htmlService;
    private ObjectMapper objectMapper;

    public AirportViewsService() {
        wikiService = WikiService.getInstance();
        urlService = URLService.getInstance();
        httpRequestService = HTTPRequestService.getInstance();
        htmlService = HtmlService.getInstance();
        objectMapper = new ObjectMapper();
    }

    public static AirportViewsService getInstance () {
        if (airportViewsService == null)
            airportViewsService = new AirportViewsService();
        return airportViewsService;
    }

    public void getAirportViewsToJsonFile(Collection<String> airports, String from, String to) {
        Set<AirportViews> airportViews = getAirportViews(airports, from, to);
        parseAirportViewsToJsonFile(airportViews);
    }

    public String getAirportViewsToJson (Collection<String> airports, String from, String to) {
        Set<AirportViews> airportViews = getAirportViews(airports, from, to);
        return parseAirportViewToJson(airportViews);
    }

    private Set<AirportViews> getAirportViews (Collection<String> airports, String from, String to) {
        Set<AirportViews> airportViews = new HashSet<>();
        Long views = 0L;
        Long badURLs = 0L;
        Long goodURLs = 0L;

        for (String airport : airports) {
            URL googleURL = urlService.buildGoogleSearching(airport);
            String googleResponse = httpRequestService.doRequest(googleURL);

            try {
                URL engWikiAirportURL = htmlService.findURL(googleResponse, ENG_WIKIPEDIA_HOSTNAME);
                String wikiResponse = httpRequestService.doRequest(engWikiAirportURL);
                List<URL> allAvailableCountriesWikiAirportURLs = htmlService.findURLs(wikiResponse, WIKIPEDIA);
                allAvailableCountriesWikiAirportURLs.add(engWikiAirportURL);

                for (URL countryUrl : allAvailableCountriesWikiAirportURLs) {
                    URL wikimediaUrl = urlService.buildWikimedia(countryUrl, from, to);

                    try {
                        String jsonResponse = httpRequestService.doRequest(wikimediaUrl);
                        views += wikiService.getViewsFromJson(jsonResponse);
                    } catch (JSONException e) {
                        System.out.println("BAD: " + wikimediaUrl);
                        badURLs++;
                    }
                    goodURLs++;
                }

                airportViews.add(new AirportViews(airport, views));
            } catch (EngWikiURLNotFoundException e) {
                System.out.println("Not found eng wikipedia for: " + airports);
                views = Long.MIN_VALUE;
                airportViews.add(new AirportViews(airport, views));
            }
        }
        System.out.println("Bad URLs: " + badURLs);
        System.out.println("Good URLs: " + goodURLs);
        return airportViews;
    }

    private void parseAirportViewsToJsonFile (Set<AirportViews> airportViews) {
        try {
            objectMapper.writeValue(new File("airportViews.json"), airportViews);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String parseAirportViewToJson (Set<AirportViews> airportViews) {
        try {
            return objectMapper.writeValueAsString(airportViews);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Parsowanie jsona do Stringa nie wyszlo.");
        }
    }
 }
