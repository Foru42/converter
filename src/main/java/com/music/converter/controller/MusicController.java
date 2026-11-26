package com.music.converter.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.music.converter.service.Mp3Service;

@Controller
public class MusicController {

    @Autowired
    private Mp3Service mp3Service;

    @GetMapping("/")
    public String inicio() {
        return "music";
    }

    @PostMapping("/canciones/mp3")
    public Object convertirMp3(@RequestParam String url, RedirectAttributes redirect) {
        try {
            ResponseEntity<byte[]> respuesta = mp3Service.convertir(url);
            byte[] mp3 = respuesta.getBody();

            if (mp3 == null || mp3.length == 0) {
                System.out.println("VAcionn");
                redirect.addFlashAttribute("f_mensaje", "No se ha podido convertir el enlace");
                return "redirect:/";
            }

            String disposition = respuesta.getHeaders().getFirst(HttpHeaders.CONTENT_DISPOSITION);
            if (disposition == null) {
                disposition = ContentDisposition.attachment().filename("cancion.mp3").build().toString();
            }

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType("audio/mpeg"))
                    .header(HttpHeaders.CONTENT_DISPOSITION, disposition)
                    .contentLength(mp3.length)
                    .body(mp3);

        } catch (RestClientResponseException e) {
            System.out.println(e);
            redirect.addFlashAttribute("f_mensaje", "No se ha podido convertir el enlace");

        } catch (RestClientException e) {
            redirect.addFlashAttribute("f_mensaje", "No se puede conectar con el servidor");
        }

        return "redirect:/";
    }
}
