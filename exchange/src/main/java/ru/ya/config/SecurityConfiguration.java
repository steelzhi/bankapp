package ru.ya.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.AuthorizedClientServiceOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientProviderBuilder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
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
                        .requestMatchers("/actuator/health").permitAll()
                        .anyRequest().authenticated()
                )
                .oauth2ResourceServer(customizer -> {
                    customizer
                            .jwt(jwtCustomizer -> {
                                JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
                                jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwt -> {
                                    System.out.println("222");
                                    Map<String, Object> resourceAccess = jwt.getClaim("resource_access");
                                    System.out.println("1: " + resourceAccess);
                                    Map<String, Object> account = (Map<String, Object>) resourceAccess.get("account");
                                    System.out.println("2: " + account);
                                    List<String> roles = (List<String>) account.get("roles");
                                    System.out.println("3: " + roles);

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

    @Bean
    public OAuth2AuthorizedClientManager authorizedClientManager(
            ClientRegistrationRepository clientRegistrationRepository,
            OAuth2AuthorizedClientService authorizedClientService
    ) {
        AuthorizedClientServiceOAuth2AuthorizedClientManager manager =
                new AuthorizedClientServiceOAuth2AuthorizedClientManager(clientRegistrationRepository, authorizedClientService);

        manager.setAuthorizedClientProvider(OAuth2AuthorizedClientProviderBuilder.builder()
                .clientCredentials() // Включаем получение токена с помощью client_credentials
                .refreshToken() // Также включаем использование refresh_token
                .build());

        return manager;
    }

    @LoadBalanced
    @Bean
    RestClient.Builder restClientBuilder() {
        return RestClient.builder();
    }
}
