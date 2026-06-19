package com.url_shortener.controller;

import com.url_shortener.dto.UrlRequest;
import com.url_shortener.dto.UrlResponse;
import com.url_shortener.model.UrlMapping;
import com.url_shortener.service.UrlShortenerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RequiredArgsConstructor
@RestController
@RequestMapping
@Tag(name = "URL Shortener", description = "Endpoints para encurtamento de URLs")
public class UrlShortenerController {

    private final UrlShortenerService service;

    @Operation(summary = "Cria uma URL encurtada")
    @PostMapping("/urls")
    public ResponseEntity<UrlResponse> create(@RequestBody @Valid UrlRequest request) {
        UrlMapping urlMapping = service.create(request.getOriginalUrl(), request.getExpiresAt());
        return ResponseEntity.status(HttpStatus.CREATED).body(toResponse(urlMapping));
    }

    @Operation(summary = "Redireciona para a URL original")
    @GetMapping("/{code}")
    public ResponseEntity<Void> redirect(@PathVariable String code) {
        UrlMapping urlMapping = service.registerAccess(code);
        return ResponseEntity.status(HttpStatus.FOUND)
                .location(URI.create(urlMapping.getOriginalUrl()))
                .build();
    }

    @Operation(summary = "Retorna estatísticas do link")
    @GetMapping("/urls/{code}/stats")
    public ResponseEntity<UrlResponse> stats(@PathVariable String code) {
        UrlMapping urlMapping = service.findByCode(code);
        return ResponseEntity.ok(toResponse(urlMapping));
    }

    @Operation(summary = "Deleta um link encurtado")
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
                .expiresAt(urlMapping.getExpiresAt())
                .build();
    }
}
