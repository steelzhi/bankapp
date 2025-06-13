package ru.ya.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtDecoders;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Map;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {
    @Value("${keycloak-location}")
    private String keycloakLocation;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity security) throws Exception {
        return security
                .authorizeHttpRequests(requests -> requests
                        //.requestMatchers("/*", "/register-user", "/actuator/health").permitAll()
                        .anyRequest().authenticated()
                )
                .oauth2ResourceServer(customizer -> {
                    customizer
                            .jwt(jwtCustomizer -> {
                                JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
                                jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwt -> {
                                    Map<String, Object> resourceAccess = jwt.getClaim("resource_access");
                                    Map<String, Object> account = (Map<String, Object>) resourceAccess.get("account");
                                    List<String> roles = (List<String>) account.get("roles");

                                    return roles.stream()
                                            .map(SimpleGrantedAuthority::new)
                                            .map(GrantedAuthority.class::cast)
                                            .toList();
                                });

                                jwtCustomizer.jwtAuthenticationConverter(jwtAuthenticationConverter);
                            });
                        }
                )
                .build();
    }

    @Bean
    public JwtDecoder jwtDecoder() {
        return JwtDecoders.fromIssuerLocation(keycloakLocation);
        //return JwtDecoders.fromIssuerLocation("http://localhost:8080/realms/master");
        //return JwtDecoders.fromIssuerLocation("http://keycloak:8080/realms/master");
    }
}
