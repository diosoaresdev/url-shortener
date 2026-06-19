package com.url_shortener;

import com.url_shortener.model.UrlMapping;
import com.url_shortener.repository.UrlMappingRepository;
import com.url_shortener.service.UrlShortenerService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UrlShortenerServiceTest {

    @Mock
    private UrlMappingRepository repository;

    @InjectMocks
    private UrlShortenerService service;

    @Test
    @DisplayName("Deve criar uma URL encurtada com sucesso")
    void deveCriarUrlComSucesso() {

        String originalUrl = "https://www.google.com";
        LocalDateTime expiresAt = LocalDateTime.now().plusDays(7);

        UrlMapping urlMappingSalvo = UrlMapping.builder()
                .id(1L)
                .originalUrl(originalUrl)
                .code("abc123")
                .accessCount(0L)
                .expiresAt(expiresAt)
                .build();

        when(repository.existsByCode(anyString())).thenReturn(false);
        when(repository.save(any(UrlMapping.class))).thenReturn(urlMappingSalvo);

        UrlMapping resultado = service.create(originalUrl, expiresAt);

        assertNotNull(resultado);
        assertEquals(originalUrl, resultado.getOriginalUrl());
        assertEquals("abc123", resultado.getCode());
        assertEquals(0L, resultado.getAccessCount());
    }

    @Test
    @DisplayName("Deve retornar URL quando código existe")
    void deveRetornarUrlQuandoCodigoExiste() {
        // Arrange
        UrlMapping urlMapping = UrlMapping.builder()
                .code("abc123")
                .originalUrl("https://www.google.com")
                .accessCount(0L)
                .expiresAt(LocalDateTime.now().plusDays(7))
                .build();

        when(repository.findByCode("abc123")).thenReturn(Optional.of(urlMapping));

        // Act
        UrlMapping resultado = service.findByCode("abc123");

        // Assert
        assertNotNull(resultado);
        assertEquals("abc123", resultado.getCode());
    }

    @Test
    @DisplayName("Deve lançar exceção quando código não existe")
    void deveLancarExcecaoQuandoCodigoNaoExiste() {
        // Arrange
        when(repository.findByCode("xyz999")).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            service.findByCode("xyz999");
        });

        assertEquals("URL não encontrada", exception.getMessage());
    }

    @Test
    @DisplayName("Deve lançar exceção quando link expirou")
    void deveLancarExcecaoQuandoLinkExpirou() {
        // Arrange
        UrlMapping urlMapping = UrlMapping.builder()
                .code("abc123")
                .originalUrl("https://www.google.com")
                .accessCount(0L)
                .expiresAt(LocalDateTime.now().minusDays(1))
                .build();

        when(repository.findByCode("abc123")).thenReturn(Optional.of(urlMapping));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            service.findByCode("abc123");
        });

        assertEquals("Este link expirou", exception.getMessage());
    }

    @Test
    @DisplayName("Deve incrementar o contador de acessos")
    void deveIncrementarContadorDeAcessos() {
        // Arrange
        UrlMapping urlMapping = UrlMapping.builder()
                .code("abc123")
                .originalUrl("https://www.google.com")
                .accessCount(0L)
                .expiresAt(LocalDateTime.now().plusDays(7))
                .build();

        when(repository.findByCode("abc123")).thenReturn(Optional.of(urlMapping));
        when(repository.save(any(UrlMapping.class))).thenReturn(urlMapping);

        // Act
        UrlMapping resultado = service.registerAccess("abc123");

        // Assert
        assertEquals(1L, resultado.getAccessCount());
    }

    @Test
    @DisplayName("Deve deletar URL com sucesso")
    void deveDeletarUrlComSucesso() {
        // Arrange
        UrlMapping urlMapping = UrlMapping.builder()
                .code("abc123")
                .originalUrl("https://www.google.com")
                .accessCount(0L)
                .expiresAt(LocalDateTime.now().plusDays(7))
                .build();

        when(repository.findByCode("abc123")).thenReturn(Optional.of(urlMapping));

        // Act
        service.delete("abc123");

        // Assert
        verify(repository, times(1)).delete(urlMapping);
    }
}