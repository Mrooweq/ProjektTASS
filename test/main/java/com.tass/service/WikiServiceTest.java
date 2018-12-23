package com.tass.service;

import org.junit.Test;

import java.net.MalformedURLException;
import java.net.URL;


public class WikiServiceTest {
    private WikiService wikiService = WikiService.getInstance();

    @Test(expected = IllegalArgumentException.class)
    public void doRequest() throws MalformedURLException {
        //given
        URL url = new URL
                ("https://wikimedia.org/api/rest_v1/metrics/pageviews/per-article/" +
                        "pms.wikipedia.org/" +
                        "all-access/user/Winnipeg_%22James_Armstrong_Richardson%22_International_Airport/" +
                        "monthly/20181001/20181101");

        //when
        wikiService.doRequest(url);

        //then
    }
}
