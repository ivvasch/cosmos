package ru.cosmotask.security.authcontroller;

import io.micrometer.core.annotation.Timed;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.cosmotask.security.model.AuthenticationRequest;
import ru.cosmotask.security.model.AuthenticationResponse;
import ru.cosmotask.security.model.RegisterRequest;
import ru.cosmotask.security.service.AuthenticateService;

@Data
@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/auth")
public class AuthController {

    private final AuthenticateService service;


    @Operation(summary = "Register admin.")
    @SecurityRequirement(name = "Bearer Authentication")
    @ApiResponse(responseCode = "200", description = "Admin was register successfully.")
    @ApiResponse(responseCode = "400", description = "Bad Request")
    @Timed(value = "auth-controller-register")
    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(
            @RequestBody RegisterRequest request) {
        return ResponseEntity.ok(service.register(request));
    }

    @Operation(summary = "Authenticate.")
    @ApiResponse(responseCode = "200", description = "Admin was authenticated successfully.")
    @ApiResponse(responseCode = "400", description = "Bad Request")
    @Timed(value = "auth-controller-authenticate")
    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody AuthenticationRequest request) {
        return ResponseEntity.ok(service.authenticate(request));
    }
}
