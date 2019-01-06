package com.tass.service;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.List;

public class URLService {
    private static URLService urlService;

    private static final String URL_TO_SEARCH_IN_GOOGLE = "https://www.google.com/search?q={airport_code}+airport+iata";
    private static final String URL_TO_WIKIMEDIA = "https://wikimedia.org/api/rest_v1/metrics/pageviews/per-article/{host}/all-access/user/{airport_name}/monthly/{timestamp_from}/{timestamp_to}";

    public static URLService getInstance(){
        if(urlService == null){
            urlService = new URLService();
        }
        return urlService;
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

    public URL buildWikimedia(URL wikipediaUrl, String from, String to) {
        String article = wikipediaUrl.getPath().replace("/wiki/", "");
        String urlString = URL_TO_WIKIMEDIA.
                replace("{host}", wikipediaUrl.getHost()).
                replace("{airport_name}", decodeArticle(article)).
                replace("{timestamp_from}", from).
                replace("{timestamp_to}", to);
        //System.out.println(decodeArticle(article));

        try {
            return new URL(urlString);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    public URL create (String urlString) {
        try {
            return new URL(urlString);
        } catch (MalformedURLException e) {
            e.getStackTrace();
            throw new RuntimeException("");
        }
    }

    private String decodeArticle (String article) {
        try {
            return URLDecoder.decode(article, "UTF-8").
                    replace("/", "%2F").
                    replace("\"","%22");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Decoding problem");
        }
    }
}
