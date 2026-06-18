package com.url_shortener.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UrlResponse {

    private String code;
    private String originalUrl;
    private String shortUrl;
    private Long accessCount;
    private LocalDateTime createdAt;
}
