package com.tass.service;


import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class WikiService {
    private static WikiService wikiService;
    private static final String URL = "https://wikimedia.org/api/rest_v1/metrics/pageviews/per-article/pl.wikipedia.org/all-access/user/Port_lotniczy_Warszawa-Modlin/monthly/20181001/20181101";

    public static WikiService getInstance(){
        if(wikiService == null){
            wikiService = new WikiService();
        }
        return wikiService;
    }

    public void doRequest(){
        String jsonResponse;

        HttpClient client = HttpClientBuilder.create().build();
        HttpGet request = new HttpGet(URL);

        HttpResponse response;
        try {
            response = client.execute(request);

            BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

            StringBuffer result = new StringBuffer();
            String line;
            while ((line = rd.readLine()) != null) {
                result.append(line);
            }

            jsonResponse = result.toString();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Cos poszlo nie tak");
        }

        System.out.println("response: " + jsonResponse);
        Long views = getViewsFromJson(jsonResponse);
        System.out.println("views: " + views);
    }

    private Long getViewsFromJson(String jsonResponse) {
        JSONObject json = new JSONObject(jsonResponse);
        JSONArray names = json.names();
        JSONArray values = json.toJSONArray(names);
        return values.getJSONArray(0).getJSONObject(0).getLong("views");
    }

}
