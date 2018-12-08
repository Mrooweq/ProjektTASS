package com.tass.service;


import com.tass.api.Airport;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

public class WikiService {
    private static WikiService wikiService;


    private static final String ENG_WIKIPEDIA_HOSTNAME = "en.wikipedia.org";
    private static final String WIKIPEDIA = ".*https://.*[^en]\\.wikipedia\\.org.*";

    public static WikiService getInstance(){
        if(wikiService == null){
            wikiService = new WikiService();
        }
        return wikiService;
    }

    public Airport getAirportViews (String airportCode, String from, String to) {
        URLBuilder urlBuilder = URLBuilder.getInstance();
        URL googleURL = urlBuilder.buildGoogleSearching(airportCode);
        String googleResponse = doRequest(googleURL);

        HtmlService htmlService = HtmlService.getInstance();
        URL engWikiAirportURL = htmlService.findURL(googleResponse, ENG_WIKIPEDIA_HOSTNAME);
        String wikiResponse = doRequest(engWikiAirportURL);

        List<URL> allAvailableCountriesWikiAirportURLs = htmlService.findURLs(wikiResponse, WIKIPEDIA);
        allAvailableCountriesWikiAirportURLs.add(engWikiAirportURL);

        String polishAirportName = "";
        Long views = 0L;
        for (URL countryUrl : allAvailableCountriesWikiAirportURLs) {
            if (countryUrl.getHost().equals("pl.wikipedia.org"))
                polishAirportName = getNameFromURL(countryUrl);
            URL wikimediaUrl = urlBuilder.buildWikimedia(countryUrl);
            String jsonResponse = doRequest(wikimediaUrl);
            views += getViewsFromJson(jsonResponse);
        }

        return new Airport(polishAirportName,airportCode, views);
    }

    private String doRequest(URL url) {
        String htmlResponse;
        HttpClient client = HttpClientBuilder.create().build();
        HttpGet request = new HttpGet(url.toString());
        System.out.println(url.toString());
        HttpResponse response;

        try {
            response = client.execute(request);
            System.out.println(response.getEntity().getContent());

            BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

            StringBuffer result = new StringBuffer();
            String line;
            while ((line = rd.readLine()) != null) {
                result.append(line);
            }

            htmlResponse = result.toString();
        }
        catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Cos poszlo nie tak");
        }

        return htmlResponse;
    }

    private Long getViewsFromJson(String jsonResponse) {
        JSONObject json = new JSONObject(jsonResponse);
        JSONArray names = json.names();
        JSONArray values = json.toJSONArray(names);
        return values.getJSONArray(0).getJSONObject(0).getLong("views");
    }

    private String getNameFromURL(URL url) {
        List<String> params = Arrays.asList(url.getPath().split("/"));
        return params.get(2);
    }
}
