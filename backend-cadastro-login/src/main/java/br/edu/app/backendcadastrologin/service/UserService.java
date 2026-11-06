package br.edu.app.backendcadastrologin.service;

import br.edu.app.backendcadastrologin.dtos.UserRequestDTO;
import br.edu.app.backendcadastrologin.dtos.UserResponseDTO;
import br.edu.app.backendcadastrologin.exception.BusinessException;
import br.edu.app.backendcadastrologin.exception.ResourceNotFoundException;
import br.edu.app.backendcadastrologin.model.User;
import br.edu.app.backendcadastrologin.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserService {
    private final UserRepository userRepository;

    private final PasswordEncoder encoder;

    public UserService(UserRepository userRepository, PasswordEncoder encoder) {
        this.userRepository = userRepository;
        this.encoder = encoder;
    }

    @Transactional
    public UserResponseDTO createUser(UserRequestDTO dto) {
        String normalizedEmail = dto.email().trim().toLowerCase();

        if (userRepository.existsByEmailIgnoreCase(normalizedEmail)) {
            throw new BusinessException("Este endereço de e-mail já está em uso.", HttpStatus.CONFLICT);
        }

        String encodedPassword = encoder.encode(dto.password());
        User user = new User(dto.name(), normalizedEmail, encodedPassword);

        User savedUser = userRepository.save(user);
        return UserResponseDTO.from(savedUser);
    }

    @Transactional(readOnly = true)
    public UserResponseDTO findById(UUID id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado."));
        return UserResponseDTO.from(user);
    }

}
