package com.tass.service;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

public class URLBuilder {
    private static URLBuilder urlBuilder;

    private static final String URL_TO_SEARCH_IN_GOOGLE = "https://www.google.com/search?q={airport_code}+airport";
    private static final String URL_TO_WIKIMEDIA = "https://wikimedia.org/api/rest_v1/metrics/pageviews/per-article/{host}/all-access/user/{airport_name}/monthly/20181001/20181101";

    public static URLBuilder getInstance(){
        if(urlBuilder == null){
            urlBuilder = new URLBuilder();
        }
        return urlBuilder;
    }

    public URL buildGoogleSearching(String airportCode) {
        String urlString = URL_TO_SEARCH_IN_GOOGLE.replace("{airport_code}", airportCode);

        try {
            return new URL(urlString);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    public URL buildWikimedia(URL wikipediaUrl) {
        List<String> params = Arrays.asList(wikipediaUrl.getPath().split("/"));
        String urlString = URL_TO_WIKIMEDIA.replace("{host}", wikipediaUrl.getHost()).replace("{airport_name}", params.get(2));

        try {
            return new URL(urlString);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }
}
