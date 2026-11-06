package br.edu.app.backendcadastrologin.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record LoginDTO (
        @NotBlank(message = "O email é obrigatório.")
        @Email(message = "Formato de email inválido")
        @Size(max=150, message = "O email deve ter no máximo 150 caracteres.")
        String email,

        @NotBlank(message = "A senha é obrigatória")
        @Size(min=6, max = 100, message = "A senha deve ter entre 6 a 100 caracteres")
        String password
) {
}
