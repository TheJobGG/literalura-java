package com.aluralatam.literalura.service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class ConsumoAPI {
    public String buscarLibroEnApi(String url){
        System.out.println("url a hacer petición: " + url);

//        Variable que llevará a cabo la petición
        HttpClient client = HttpClient.newHttpClient();

//        Variable que construye la petición que se realizará
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).build();

//        Variable donde guardar el resultado de la petición
        HttpResponse<String> response = null;

        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e){
            throw new RuntimeException(e);
        }

        return response.body();
    }
}
