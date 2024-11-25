package com.app.controllers;

import java.io.IOException;
import java.nio.file.Files;

import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/custom-api-docs")
public class OpenApiDocsController {

    @GetMapping(value = "/openapi.json", produces = "application/json")
    public ResponseEntity<byte[]> getOpenApiJson() throws IOException {
        ClassPathResource resource = new ClassPathResource("openapi.json");
        byte[] data = Files.readAllBytes(resource.getFile().toPath());

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, "application/json");
        return new ResponseEntity<>(data, headers, HttpStatus.OK);
    }
}
