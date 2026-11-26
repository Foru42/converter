package com.music.converter.service;

import org.springframework.http.ResponseEntity;

public interface Mp3Service {
    ResponseEntity<byte[]> convertir(String url);
}
