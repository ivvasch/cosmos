package ru.cosmotask.security.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import ru.cosmotask.security.enums.Role;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final AuthenticationProvider authenticationProvider;
    private final JWTAuthenticationFilter jwtAuthFilter;
    private static final String[] AUTH_WHITELIST = {
            // -- Swagger UI v2
            "/v2/api-docs",
            "/swagger-resources",
            "/swagger-resources/**",
            "/configuration/ui",
            "/configuration/security",
            "/swagger-ui.html",
            "/webjars/**",
            // -- Swagger UI v3 (OpenAPI)
            "/v3/api-docs/**",
            "/swagger-ui/**"
            // other public endpoints of your API may be appended to this array
    };

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http.
                csrf(AbstractHttpConfigurer::disable)
//                .formLogin(form -> form.loginPage("/login").permitAll())
                .authorizeHttpRequests(auth ->
                        auth
                                .requestMatchers("api/v1/auth/authenticate", "/login", "api/v1/auth/register")
                                .permitAll()
                                .requestMatchers(AUTH_WHITELIST)
                                .permitAll()
                                .requestMatchers(HttpMethod.POST).hasAnyRole(Role.SUPER_STAR.name(), Role.STAR.name())
                                .requestMatchers(HttpMethod.PUT).hasAnyRole(Role.SUPER_STAR.name(), Role.STAR.name())
                                .requestMatchers(HttpMethod.PATCH).hasAnyRole(Role.SUPER_STAR.name(), Role.STAR.name())
                                .requestMatchers(HttpMethod.DELETE).hasRole(Role.SUPER_STAR.name())
                                .anyRequest()
                                .authenticated()

                )
                .sessionManagement(ssmg -> ssmg.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
