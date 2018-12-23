package com.tass.service;

import org.json.JSONArray;
import org.json.JSONObject;

public class WikiService {
    private static WikiService wikiService;

    public static WikiService getInstance(){
        if(wikiService == null){
            wikiService = new WikiService();
        }
        return wikiService;
    }

    public Long getViewsFromJson(String jsonResponse) {
        JSONObject json = new JSONObject(jsonResponse);
        JSONArray names = json.names();
        JSONArray values = json.toJSONArray(names);
        return values.getJSONArray(0).getJSONObject(0).getLong("views");
    }
}
