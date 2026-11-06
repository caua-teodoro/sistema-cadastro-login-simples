package br.edu.app.backendcadastrologin.security;

import java.util.List;
import java.util.UUID;

import br.edu.app.backendcadastrologin.exception.InvalidCredentialsException;
import org.springframework.security.oauth2.jwt.Jwt;

public final class AuthenticatedUser {
    private AuthenticatedUser() {
    }

    public static UUID extractUserId(Jwt jwt) {
        if (jwt == null || jwt.getSubject() == null) {
            throw new InvalidCredentialsException("Token de autenticação ausente ou inválido.");
        }
        try {
            return UUID.fromString(jwt.getSubject());
        } catch (IllegalArgumentException ex) {
            throw new InvalidCredentialsException("Identificador de usuário inválido no token.");
        }
    }

    public static boolean isAdmin(Jwt jwt) {
        if (jwt == null) {
            return false;
        }
        List<String> roles = jwt.getClaimAsStringList("roles");
        return roles != null && roles.contains("ADMIN");
    }
}
