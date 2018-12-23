package com.tass.service;


import com.tass.exceptions.EngWikiURLNotFoundException;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

public class WikiService {
    private static WikiService wikiService;

    private static final String POL_WIKIPEDIA_HOSTNAME = "pl.wikipedia.org";
    private static final String ENG_WIKIPEDIA_HOSTNAME = "en.wikipedia.org";
    private static final String WIKIPEDIA = ".*https://(?!en).{2,3}\\.wikipedia\\.org.*";

    public static WikiService getInstance(){
        if(wikiService == null){
            wikiService = new WikiService();
        }
        return wikiService;
    }

    public Long getAirportViews (String airportCode, String from, String to) {
        URLService urlService = URLService.getInstance();
        URL googleURL = urlService.buildGoogleSearching(airportCode);
        String googleResponse = doRequest(googleURL);
        HtmlService htmlService = HtmlService.getInstance();
        Long views = 0L;

        try {
            URL engWikiAirportURL = htmlService.findURL(googleResponse, ENG_WIKIPEDIA_HOSTNAME);
            String wikiResponse = doRequest(engWikiAirportURL);

            List<URL> allAvailableCountriesWikiAirportURLs = htmlService.findURLs(wikiResponse, WIKIPEDIA);
            allAvailableCountriesWikiAirportURLs.add(engWikiAirportURL);

            if (allAvailableCountriesWikiAirportURLs == null)
                views = 0L;

            for (URL countryUrl : allAvailableCountriesWikiAirportURLs) {
                URL wikimediaUrl = urlService.buildWikimedia(countryUrl, from, to);
                String jsonResponse = doRequest(wikimediaUrl);
                try {
                    views += getViewsFromJson(jsonResponse);
                } catch (JSONException e){
                    System.out.println("BAD: " + wikimediaUrl);
                }
                System.out.println("Good: " + wikimediaUrl);
            }

        } catch (EngWikiURLNotFoundException e) {
            System.out.println("Not found eng wikipedia for: " + airportCode);
            views = Long.MIN_VALUE;
        }

        return views;
    }

    public String doRequest(URL url) {
        String htmlResponse;

        RequestConfig requestConfig = RequestConfig.custom().setCookieSpec(CookieSpecs.STANDARD).build();
        HttpClient client = HttpClients.custom().setDefaultRequestConfig(requestConfig).build();

        HttpGet request = new HttpGet(url.toString());
        HttpResponse response;

        try {
            response = client.execute(request);

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
