# URL Shortener API 🔗

API REST para encurtamento de URLs desenvolvida com Spring Boot.

## 🚀 Tecnologias

- Java 21
- Spring Boot 3
- Spring Data JPA
- PostgreSQL
- Docker
- Swagger/OpenAPI
- JUnit 5
- Mockito

## ⚙️ Como rodar o projeto

### Pré-requisitos
- Java 21
- Docker

### 1. Clone o repositório
```bash
git clone https://github.com/SEU_USUARIO/url-shortener-api.git
cd url-shortener-api
```

### 2. Suba o banco de dados
```bash
docker-compose up -d
```

### 3. Rode a aplicação
```bash
./mvnw spring-boot:run
```

### 4. Acesse o Swagger
http://localhost:8080/swagger-ui.html

## 📌 Endpoints

| Método | Endpoint | Descrição |
|---|---|---|
| POST | /urls | Cria uma URL encurtada |
| GET | /{code} | Redireciona para a URL original |
| GET | /urls/{code}/stats | Retorna estatísticas do link |
| DELETE | /urls/{code} | Deleta um link |

## 📝 Exemplos de uso

### Criar uma URL encurtada
```json
POST /urls
{
  "originalUrl": "https://www.google.com",
  "expiresAt": "2026-12-31T23:59:59"
}
```

### Resposta
```json
{
  "code": "abc123",
  "originalUrl": "https://www.google.com",
  "shortUrl": "http://localhost:8080/abc123",
  "accessCount": 0,
  "createdAt": "2026-06-19T10:00:00",
  "expiresAt": "2026-12-31T23:59:59"
}
```

## 🧪 Rodando os testes
```bash
./mvnw test
```

## 📄 Licença
MIT