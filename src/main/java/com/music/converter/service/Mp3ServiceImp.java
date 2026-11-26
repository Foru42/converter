package com.music.converter.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class Mp3ServiceImp implements Mp3Service {

    @Value("${url.api.mp3}")
    private String URL;

    private final RestClient restClient = RestClient.create();

    @Override
    public ResponseEntity<byte[]> convertir(String url) {
        return restClient.post()
                .uri(URL)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Map.of("url", url))
                .retrieve()
                .toEntity(byte[].class);
    }
}
