package com.url_shortener.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.URL;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UrlRequest {

    @NotBlank(message = "A URl não pode ser vazia")
    @URL(message = "Formato de URL inválido")
    private String originalUrl;

    @Future(message = "A data de expiração deve ser no futuro")
    private LocalDateTime expiresAt;
}
