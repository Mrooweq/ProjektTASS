package com.tass;


import com.tass.api.Plane;
import com.tass.service.JsonService;

import java.util.Set;

public class Main {

    public static void main(String[] args) {
        JsonService jsonService = JsonService.getInstance();
        Set<Plane> planes = jsonService.getPlanesFromFiles();
    }
}
