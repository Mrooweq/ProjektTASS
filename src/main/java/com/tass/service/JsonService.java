package com.tass.service;

import com.tass.api.Plane;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;


public class JsonService {
    private static final int MODEL_POSITION_ID = 8;
    private static final int FROM_POSITION_ID = 11;
    private static final int TO_POSITION_ID = 12;
    private static JsonService jsonService;

    public static JsonService getInstance(){
        if(jsonService == null){
            jsonService = new JsonService();
        }
        return jsonService;
    }

    public Set<Plane> getPlanesFromFiles(){
        Set<Plane> planes = new HashSet<>();
        Set<String> filePathSet;

        try {
            filePathSet = Files.walk(Paths.get("files/"))
                    .filter(Files::isRegularFile)
                    .map(Path::toString)
                    .collect(Collectors.toSet());
        }catch (Exception e) {
            throw new RuntimeException("Nie udalo sie odczytac sciezek do plikow", e);
        }

        for (String filePath : filePathSet) {
            String contentOfFile = readFromFile(filePath);
            Set<Plane> setOfPlanes = getPlanes(contentOfFile);
            planes.addAll(setOfPlanes);
        }

        return planes;
    }

    private String readFromFile(String fileName) {
        StringBuilder contentBuilder = new StringBuilder();

        try {
            Files
                    .lines(Paths.get(fileName), StandardCharsets.UTF_8)
                    .forEach(s -> contentBuilder.append(s).append("\n"));
        } catch (Exception e){
            throw new RuntimeException("Nie udalo sie odczytac JSONa", e);
        }
        return contentBuilder.toString();
    }

    private Set<Plane> getPlanes(String responseBody){
        Set<Plane> planes;
        try{
            planes = parseJsonToPlanes(responseBody);
        } catch (JSONException e) {
            throw new RuntimeException("Nie udalo sie rozparsowac JSONa", e);
        }

        return refinePlanes(planes);
    }

    private Set<Plane> refinePlanes(Set<Plane> planes) {
        return planes.stream()
                .filter(this::isPlaneValed)
                .collect(Collectors.toSet());
    }

    private boolean isPlaneValed(Plane plane){
        return StringUtils.isNotBlank(plane.getFrom())
                && StringUtils.isNotBlank(plane.getTo())
                && StringUtils.isNotBlank(plane.getModel());
    }

    private Set<Plane> parseJsonToPlanes(String responseBody) {
        Set<Plane> setOfPlanes = new HashSet<>();

        JSONArray names, values;
        JSONObject json = new JSONObject(responseBody);

        json.remove("full_count");
        json.remove("version");
        json.remove("stats");

        names = json.names();
        values = json.toJSONArray(names);

        for (int i = 0; i < names.length(); i++) {
            String model = values.getJSONArray(i).getString(MODEL_POSITION_ID);
            String from = values.getJSONArray(i).getString(FROM_POSITION_ID);
            String to = values.getJSONArray(i).getString(TO_POSITION_ID);

            Plane plane = new Plane(model, from, to);
            setOfPlanes.add(plane);
        }

        return setOfPlanes;
    }
}
