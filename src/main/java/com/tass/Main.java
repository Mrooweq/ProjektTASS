package com.tass;


import com.tass.api.Plane;
import com.tass.service.JsonService;
import com.tass.service.WikiService;

import java.util.Set;

public class Main {

    public static void main(String[] args) {
        JsonService jsonService = JsonService.getInstance();
        WikiService wikiService = WikiService.getInstance();

//        Set<Plane> planes = jsonService.getPlanesFromFiles();
        wikiService.doRequest();
    }
}
