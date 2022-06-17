package com.github.TheTrueHooha.cryptohistorydashboard.utils;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.util.Collections;

public class HttpUtils {

    private static  String apiHost = "coinranking1.p.rapidapi.com";
    private static  String apiKey = "bddd8711c8msh172c4aba06bbf60p1a8473jsne113e831e770";

    public static HttpEntity<String> getHttpEntity(){
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        httpHeaders.set("X-RapidAPI-Key", apiKey);
        httpHeaders.set("X-RapidAPI-Host", apiHost);
        return new HttpEntity<>(null, httpHeaders);
    }


}
