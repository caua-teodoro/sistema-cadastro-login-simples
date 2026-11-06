package br.edu.app.backendcadastrologin.controller;

import br.edu.app.backendcadastrologin.dtos.UserRequestDTO;
import br.edu.app.backendcadastrologin.dtos.UserResponseDTO;
import br.edu.app.backendcadastrologin.model.User;
import br.edu.app.backendcadastrologin.security.AuthenticatedUser;
import br.edu.app.backendcadastrologin.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/users")
@Tag(name="Usuários", description="Cadastro e consulta de perfil")
public class UserController {

    public final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // Mapeia requisições HTTP do tipo POST para este metodo (usado para criar recursos)
    @PostMapping
    // Anotação do Swagger/OpenAPI para documentar o que este endpoint faz na interface visual da API
    @Operation(summary = "cadastra um novo usuário")
    // Declaração do metodo:
    // - ResponseEntity<UserResponseDTO>:Representa toda a resposta HTTP (status, cabeçalhos e corpo).
    // - @RequestBody: Pega o JSON enviado no corpo da requisição e converte para o objeto UserRequestDTO.
    // - @Valid:Aciona as validações definidas dentro do DTO (como @NotBlank, @Email, etc.) antes de executar o metodo. Se falhar, retorna erro 400 automaticamente
    public ResponseEntity<UserResponseDTO> cadastrar(@RequestBody @Valid UserRequestDTO dto) {

        // Chama a camada de serviço (regras de negócio) para salvar o usuário no banco.
        // Retorna um DTO de resposta (UserResponseDTO), que geralmente omite dados sensíveis (como a senha) e já traz o ID gerado.
        UserResponseDTO created = userService.createUser(dto);

        // Constrói a URI (URL) de onde o novo usuário pode ser acessado (Ex: http://localhost:8080/usuarios/15)
        URI location = ServletUriComponentsBuilder.fromCurrentRequest() // Pega a URL base que foi chamada nesta requisição (ex: http://localhost:8080/usuarios)
                .path("/{id}") // Adiciona um parâmetro dinâmico ao final da URL (ex: /usuarios/{id})
                .buildAndExpand(created.id()) // Substitui o "{id}" pelo ID real do usuário recém-criado no banco
                .toUri(); // Converte tudo isso para um objeto do tipo URI válido

        // Monta e retorna a resposta HTTP final:
        // - .created(location): Define o status HTTP como 201 (Created) e adiciona o cabeçalho "Location" com a URL gerada acima.
        // - .body(created): Coloca o JSON com os dados do usuário criado no corpo da resposta.
        return ResponseEntity.created(location).body(created);
    }

    @GetMapping("/me")
    @Operation(summary = "Retorna os dados do usuário atualmente autenticado")
    public ResponseEntity<UserResponseDTO> getCurrentUser(@AuthenticationPrincipal Jwt jwt) {
        UUID currentUserId = AuthenticatedUser.extractUserId(jwt);
        return ResponseEntity.ok(userService.findById(currentUserId));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Busca usuário por ID (permitido apenas ao próprio usuário ou ADMIN)")
    public ResponseEntity<UserResponseDTO> findById(
            @PathVariable UUID id,
            @AuthenticationPrincipal Jwt jwt
    ) {
        UUID currentUserId = AuthenticatedUser.extractUserId(jwt);
        if (!currentUserId.equals(id) && !AuthenticatedUser.isAdmin(jwt)) {
            throw new AccessDeniedException("Você não tem permissão para acessar os dados de outro usuário.");
        }
        return ResponseEntity.ok(userService.findById(id));
    }
}
