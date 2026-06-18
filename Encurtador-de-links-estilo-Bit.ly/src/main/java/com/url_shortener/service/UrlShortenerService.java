package com.url_shortener.service;

import com.url_shortener.model.UrlMapping;
import com.url_shortener.repository.UrlMappingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UrlShortenerService {

    private final UrlMappingRepository repository;

    public UrlMapping create(String originalUrl) {
        String code = generateUniqueCode();

        UrlMapping urlMapping = UrlMapping.builder()
                .originalUrl(originalUrl)
                .code(code)
                .accessCount(0L)
                .build();

        return repository.save(urlMapping);
    }

    public UrlMapping findByCode(String code) {
        return repository.findByCode(code)
                .orElseThrow(() -> new RuntimeException("URL não encontrada"));
    }

    public UrlMapping registerAccess(String code) {
        UrlMapping urlMapping = findByCode(code);
        urlMapping.setAccessCount(urlMapping.getAccessCount() + 1);
        return repository.save(urlMapping);
    }

    public void delete(String code) {
        UrlMapping urlMapping = findByCode(code);
        repository.delete(urlMapping);
    }

    private String generateUniqueCode() {
        String code;
        do {
            code = UUID.randomUUID().toString().substring(0, 6);
        } while (repository.existsByCode(code));
        return code;
    }
}
