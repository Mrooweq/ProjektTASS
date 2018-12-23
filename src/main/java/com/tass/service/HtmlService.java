package com.tass.service;

import com.tass.exceptions.EngWikiURLNotFoundException;
import com.tass.exceptions.WikiURLsNotFoundException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class HtmlService {
    private static HtmlService htmlService;

    public static HtmlService getInstance(){
        if(htmlService == null){
            htmlService = new HtmlService();
        }
        return htmlService;
    }

    public URL findURL (String htmlText, String hostname) throws EngWikiURLNotFoundException {
        Document document = Jsoup.parse(htmlText);
        Elements elements = document.select("a");

        for (Element element : elements) {
            String link = element.attr("href");
            if (link.contains(hostname)) {
                return getUrlFromHref(link);
            }
        }

        throw new EngWikiURLNotFoundException("Nie znaleziono zadnego linka z podana zawartoscia: " + hostname);
    }

    public List<URL> findURLs (String htmlText, String urlRegex) throws WikiURLsNotFoundException {
        List<URL> URLs = new ArrayList<>();
        Document document = Jsoup.parse(htmlText);
        Elements elements = document.select("a");

        Pattern pattern = Pattern.compile(urlRegex);

        for (Element element : elements) {
            String href = element.attr("href");
            if (pattern.matcher(href).matches()) {
                URLs.add(getUrlFromHref(href));
            }
        }

        if (URLs.isEmpty())
            throw new WikiURLsNotFoundException("Nie znaleziono zadnego linka z podana zawartoscia: " + urlRegex);

        return URLs;
    }

    private URL getUrlFromHref(String href) {
        URLService urlService = URLService.getInstance();
        String urlString = href.substring(href.indexOf("https"));

        if (urlString.contains("&")) {
            urlString = urlString.substring(0, urlString.indexOf("&"));
        }

        //urlString = urlService.correctForWikimedia(urlString);

        return urlService.create(urlString);
    }
}
