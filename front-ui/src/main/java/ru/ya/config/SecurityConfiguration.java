package ru.ya.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.AuthorizedClientServiceOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientProviderBuilder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.oidc.web.logout.OidcClientInitiatedLogoutSuccessHandler;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.web.client.RestClient;
import ru.ya.service.MyUserDetailsService;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {
    @Autowired
    private ClientRegistrationRepository clientRegistrationRepository;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                // Настройка авторизации запросов
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/get-register-form", "/register-user", "/actuator/health").permitAll() // Доступ к главной странице - всем,
                        .anyRequest().authenticated()  // остальное — только для аутентифицированных
                )
                .formLogin(Customizer.withDefaults())
/*                // Включаем форму логина
                .formLogin(form -> form
                        .loginPage("/login")       // Собственная страница логина
                        .permitAll()               // Разрешаем всем заходить на логин
                )*/
                // Конфигурация logout (разлогинивания)
                .logout(logout -> logout
                        .logoutUrl("/logout")               // URL для логаута (по умолчанию "/logout")
                        .logoutSuccessUrl("/")  // Куда перенаправить после выхода (после выхода вернёт на страницу с выбором входа или регистрации)
                        // Регистрируем OidcClientInitiatedLogoutSuccessHandler
                        .logoutSuccessHandler(oidcLogoutSuccessHandler())
                );
        return http.build();
    }

    @Bean
    @Primary
    UserDetailsService userDetailsService() {
        return new MyUserDetailsService();
    }

    @Bean
    @LoadBalanced
    RestClient restClient() {
        return RestClient.create();
    }

    @LoadBalanced
    @Bean
    RestClient.Builder restClientBuilder() {
        return RestClient.builder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider(UserDetailsService myUserService) {
        DaoAuthenticationProvider auth = new DaoAuthenticationProvider();
        auth.setUserDetailsService(myUserService);
        auth.setPasswordEncoder(passwordEncoder());
        return auth;
    }

    private LogoutSuccessHandler oidcLogoutSuccessHandler() {
        // Для работы необходимо указать ClientRegistrationRepository
        // Этот репозиторий содержит информацию обо всех зарегистрированных в приложении клиентах (и их провайдерах).
        OidcClientInitiatedLogoutSuccessHandler handler =
                new OidcClientInitiatedLogoutSuccessHandler(clientRegistrationRepository);

        // Можно указать адрес после успешного логаута
        handler.setPostLogoutRedirectUri("{baseUrl}");

        return handler;
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
}
