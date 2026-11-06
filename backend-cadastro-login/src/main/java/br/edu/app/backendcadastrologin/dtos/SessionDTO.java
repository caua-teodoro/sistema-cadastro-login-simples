package br.edu.app.backendcadastrologin.dtos;

import java.util.UUID;

public record SessionDTO(
        UUID userId,
        String email,
        String accessToken,
        String tokenType,
        long expiresInSeconds
) {
}
