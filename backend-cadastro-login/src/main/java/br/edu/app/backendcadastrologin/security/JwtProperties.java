package br.edu.app.backendcadastrologin.security;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "security.jwt")
public record JwtProperties(
        @NotBlank(message = "A chave secreta JWT não pode estar em branco.")
        @Size(min = 32, message = "A chave secreta JWT deve possuir pelo menos 32 caracteres (256 bits) para HMAC-SHA256.")
        String secretKey,

        @Positive(message = "O tempo de expiração do JWT deve ser positivo.")
        long expirationMillis
) {
}
