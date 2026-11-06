# 🔐 Backend — Sistema de Cadastro e Login

![Java](https://img.shields.io/badge/Java-21-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.4.3-6DB33F?style=for-the-badge&logo=springboot&logoColor=white)
![Spring Security](https://img.shields.io/badge/Spring_Security-JWT_OAuth2-6DB33F?style=for-the-badge&logo=springsecurity&logoColor=white)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-16+-4169E1?style=for-the-badge&logo=postgresql&logoColor=white)
![Swagger](https://img.shields.io/badge/OpenAPI_3-Swagger_UI-85EA2D?style=for-the-badge&logo=swagger&logoColor=black)

API RESTful stateless para **cadastro, autenticação e controle de acesso de usuários**, construída com **Java 21** e **Spring Boot**, aplicando boas práticas de segurança (BCrypt, JWT HMAC-SHA256 via OAuth2 Resource Server, controle por roles e proteção contra IDOR).

---

## 🚀 Tecnologias e Arquitetura

- **Java 21** (uso de `records` imutáveis nos DTOs e configurações tipadas)
- **Spring Boot 3.4.3** (`Web`, `Data JPA`, `Validation`)
- **Spring Security + OAuth2 Resource Server** (emissão e validação nativa de JWT `HS256` e hash `BCrypt`)
- **PostgreSQL** (persistência relacional com chaves primárias `UUID`)
- **SpringDoc OpenAPI 3** (documentação interativa via Swagger UI)
- **Tratamento Global de Erros** (`@RestControllerAdvice` com respostas JSON padronizadas)

```text
src/main/java/br/edu/app/backendcadastrologin
├── config/       # Configuração OpenAPI / Swagger
├── controller/   # Endpoints REST (/auth, /users)
├── dtos/         # Contratos de entrada e saída (Records + Bean Validation)
├── exception/    # Handler global e exceções de domínio/autenticação
├── model/        # Entidade JPA User e enum Role (USER, ADMIN)
├── repository/   # Persistência Spring Data JPA
├── security/     # SecurityFilterChain, TokenService, CORS e JwtProperties
└── service/      # Regras de negócio (AuthService, UserService)
```

---

## 📌 Endpoints Principais

| Método | Rota | Acesso | Descrição |
| :--- | :--- | :--- | :--- |
| `POST` | `/users` | Público | Cadastra um novo usuário (`201 Created`) |
| `POST` | `/auth/login` | Público | Autentica credenciais e retorna o `accessToken` JWT |
| `GET` | `/users/me` | Autenticado (`Bearer`) | Retorna o perfil do usuário logado |
| `GET` | `/users/{id}` | Próprio usuário / `ADMIN` | Consulta usuário por `UUID` (protegido contra IDOR) |

---

## ⚙️ Como Executar Localmente

### Pré-requisitos
- **JDK 21+**
- **PostgreSQL** rodando localmente com o banco criado:
  ```sql
  CREATE DATABASE cadastro_login_db;
  ```

### Variáveis de Ambiente (Opcional — possui valores padrão para dev)

| Variável | Descrição | Valor Padrão (Dev) |
| :--- | :--- | :--- |
| `DB_URL` | URL JDBC do PostgreSQL | `jdbc:postgresql://localhost:5432/cadastro_login_db` |
| `DB_USERNAME` | Usuário do banco | `postgres` |
| `DB_PASSWORD` | Senha do banco | `postgres` |
| `JWT_SECRET` | Chave secreta HMAC-SHA256 (mín. 32 bytes) | *(chave local de desenvolvimento)* |
| `JWT_EXPIRATION_MILLIS` | Tempo de expiração do token (ms) | `3600000` (1 hora) |
| `CORS_ALLOWED_ORIGINS` | Origens liberadas no CORS | `http://localhost:3000,http://localhost:4200,http://localhost:5173` |

### Executando a aplicação

```bash
# Clonar o repositório
git clone <URL_DO_SEU_REPOSITORIO>
cd backend-cadastro-login

# Subir a API via Maven Wrapper
./mvnw spring-boot:run
```
*(No Windows PowerShell/CMD, utilize `.\mvnw.cmd spring-boot:run`.)*

---

## 📖 Documentação Interativa (Swagger UI)

Com a aplicação rodando, acesse no navegador:

- **Swagger UI:** http://localhost:8080/swagger-ui.html
- **OpenAPI JSON:** http://localhost:8080/v3/api-docs

> **Como testar rotas protegidas no Swagger:** execute `POST /auth/login`, copie o valor de `accessToken` e cole no botão **Authorize** (sem digitar `Bearer`).
