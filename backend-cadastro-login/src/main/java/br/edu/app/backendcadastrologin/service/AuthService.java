package br.edu.app.backendcadastrologin.service;

import br.edu.app.backendcadastrologin.dtos.LoginDTO;
import br.edu.app.backendcadastrologin.dtos.SessionDTO;
import br.edu.app.backendcadastrologin.exception.InvalidCredentialsException;
import br.edu.app.backendcadastrologin.model.User;
import br.edu.app.backendcadastrologin.repository.UserRepository;
import br.edu.app.backendcadastrologin.security.TokenService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Locale;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, TokenService tokenService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenService = tokenService;
    }

    @Transactional(readOnly = true)
    public SessionDTO login(LoginDTO loginDTO) {
        String normalizedEmail = loginDTO.email().trim().toLowerCase(Locale.ROOT);
        User user = userRepository.findByEmailIgnoreCase(normalizedEmail)
                .orElseThrow(() -> new InvalidCredentialsException("E-mail ou senha inválidos."));
        if (!passwordEncoder.matches(loginDTO.password(), user.getPassword())) {
            throw new InvalidCredentialsException("E-mail ou senha inválidos.");
        }
        String accessToken = tokenService.generateToken(user);
        return new SessionDTO(
                user.getId(),
                user.getEmail(),
                accessToken,
                "Bearer",
                tokenService.getExpirationSeconds()
        );
    }
}
