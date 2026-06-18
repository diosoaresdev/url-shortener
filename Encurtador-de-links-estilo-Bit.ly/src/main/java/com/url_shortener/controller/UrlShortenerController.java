package com.url_shortener.controller;

import com.url_shortener.dto.UrlRequest;
import com.url_shortener.dto.UrlResponse;
import com.url_shortener.model.UrlMapping;
import com.url_shortener.service.UrlShortenerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RequiredArgsConstructor
@RestController
@RequestMapping
public class UrlShortenerController {

    private final UrlShortenerService service;

    @PostMapping("/urls")
    public ResponseEntity<UrlResponse> create(@RequestBody @Valid UrlRequest request) {
        UrlMapping urlMapping = service.create(request.getOriginalUrl());
        return ResponseEntity.status(HttpStatus.CREATED).body(toResponse(urlMapping));
    }

    @GetMapping("/{code}")
    public ResponseEntity<Void> redirect(@PathVariable String code) {
        UrlMapping urlMapping = service.registerAccess(code);
        return ResponseEntity.status(HttpStatus.FOUND)
                .location(URI.create(urlMapping.getOriginalUrl()))
                .build();
    }

    @GetMapping("/urls/{code}/stats")
    public ResponseEntity<UrlResponse> stats(@PathVariable String code) {
        UrlMapping urlMapping = service.findByCode(code);
        return ResponseEntity.ok(toResponse(urlMapping));
    }

    @DeleteMapping("/urls/{code}")
    public ResponseEntity<Void> delete(@PathVariable String code) {
        service.delete(code);
        return ResponseEntity.noContent().build();
    }

    private UrlResponse toResponse(UrlMapping urlMapping) {
        return UrlResponse.builder()
                .code(urlMapping.getCode())
                .originalUrl(urlMapping.getOriginalUrl())
                .shortUrl("http://localhost:8080/" + urlMapping.getCode())
                .accessCount(urlMapping.getAccessCount())
                .createdAt(urlMapping.getCreatedAt())
                .build();
    }
}
