package ru.cosmotask.security.service;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.cosmotask.security.model.AuthenticationRequest;
import ru.cosmotask.security.model.AuthenticationResponse;
import ru.cosmotask.security.model.RegisterRequest;
import ru.cosmotask.security.enums.Role;
import ru.cosmotask.security.model.User;
import ru.cosmotask.security.repository.UserRepository;

@Slf4j
@Data
@Service
@RequiredArgsConstructor
public class AuthenticateService {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse register(RegisterRequest request) {
        var user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole().equals(Role.STAR.name()) ? Role.STAR : Role.SUPER_STAR)
                .credentialNonExpired(true)
                .accountNonExpired(true)
                .accountNonLocked(true)
                .enabled(true)
                .build();
        repository.save(user);
        var jwt = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .jwt(jwt)
                .build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    request.getEmail(),
                    request.getPassword()
            ));
        } catch (AuthenticationException e) {
      log.error("Error occurred when we try to authenticate. Message is '{}'", e.getMessage());
            throw new RuntimeException(e);
        }
        var user = repository.findByEmail(request.getEmail()).orElseThrow();
        var jwt = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .jwt(jwt)
                .build();
    }
}
