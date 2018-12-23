package com.tass.service;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

public class HTTPRequestService {
    private static HTTPRequestService httpRequestService;

    public static HTTPRequestService getInstance() {
        if (httpRequestService == null) {
            httpRequestService = new HTTPRequestService();
        }
        return httpRequestService;
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
}
