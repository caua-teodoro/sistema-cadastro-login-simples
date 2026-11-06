package br.edu.app.backendcadastrologin.dtos;

import br.edu.app.backendcadastrologin.model.User;

import java.time.LocalDateTime;
import java.util.UUID;

public record UserResponseDTO (
        UUID id,
        String name,
        String email,
        LocalDateTime createdAt
) {
    public static UserResponseDTO from(User user) {
        return new UserResponseDTO(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getCreatedAt()
        );
    }
}
