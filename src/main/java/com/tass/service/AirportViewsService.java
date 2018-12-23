package com.tass.service;

import com.tass.api.graph.Value;
import com.tass.exceptions.EngWikiURLNotFoundException;
import com.tass.exceptions.WikiURLsNotFoundException;
import edu.uci.ics.jung.graph.DirectedGraph;
import org.json.JSONException;

import java.net.URL;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AirportViewsService {
    private static final String POL_WIKIPEDIA_HOSTNAME = "pl.wikipedia.org";
    private static final String ENG_WIKIPEDIA_HOSTNAME = "en.wikipedia.org";
    private static final String WIKIPEDIA = ".*https://(?!en).{2,3}\\.wikipedia\\.org.*";

    private static AirportViewsService airportViewsService;

    private WikiService wikiService;
    private URLService urlService;
    private HTTPRequestService httpRequestService;
    private HtmlService htmlService;

    public AirportViewsService() {
        wikiService = WikiService.getInstance();
        urlService = URLService.getInstance();
        httpRequestService = HTTPRequestService.getInstance();
        htmlService = HtmlService.getInstance();
    }

    public static AirportViewsService getInstance () {
        if (airportViewsService == null)
            airportViewsService = new AirportViewsService();
        return airportViewsService;
    }

    public Map<String, Long> getAirportViews(Collection<String> airports, String from, String to) {
        Map<String, Long> airportViews = new HashMap<>();
        Long views = 0L;

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
                        //System.out.println("Good: " + wikimediaUrl);
                    } catch (JSONException e) {
                        System.out.println("BAD: " + wikimediaUrl);
                    }

                }

                airportViews.put(airport, views);
            } catch (EngWikiURLNotFoundException e) {
                System.out.println("Not found eng wikipedia for: " + airports);
                views = Long.MIN_VALUE;
                airportViews.put(airport, views);
            }
            catch (WikiURLsNotFoundException e) {
                System.out.println("Not found any Wiki for: " + airport);
                views = 0L;
                airportViews.put(airport, views);
            }
        }

        return airportViews;
    }

    public void print (Map<String, Long> airportViews) {
        for (Map.Entry entry : airportViews.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
    }
 }
