package front_ui.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // Настройка авторизации запросов
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/signup", "/account").permitAll() // Доступ к главной странице - всем,
                        .anyRequest().authenticated()  // остальное — только для аутентифицированных
                )
                // Включаем форму логина (для полноты примера)
                .formLogin(form -> form
                        .loginPage("/login")       // Собственная страница логина
                        .permitAll()               // Разрешаем всем заходить на логин
                )
                // Конфигурация logout (разлогинивания)
                .logout(logout -> logout
                        .logoutUrl("/logout")               // URL для логаута (по умолчанию "/logout")
                        .logoutSuccessUrl("/login?logout")  // Куда перенаправить после выхода (после выхода вернёт на логин)
                        .invalidateHttpSession(true)        // Аннулировать сессию (по умолчанию true)
                        .clearAuthentication(true)          // Очистить аутентификацию (по умолчанию true)
                        .permitAll()                        // Разрешить вызывать logout всем (даже неавторизованным)
                );
        return http.build();
    }

/*    @Bean
    public ServerLogoutSuccessHandler logoutSuccessHandler(String uri) {
        RedirectServerLogoutSuccessHandler successHandler = new RedirectServerLogoutSuccessHandler();
        successHandler.setLogoutSuccessUrl(URI.create(uri));
        return successHandler;
    }*/

/*
    @Bean
    public ReactiveUserDetailsService r2dbcUserDetailsService(UserRepository userRepository) {
        return new R2dbcUserDetailsService(userRepository);
    }
*/

/*    @Bean
    ReactiveOAuth2AuthorizedClientManager auth2AuthorizedClientManager(
            ReactiveClientRegistrationRepository clientRegistrationRepository,
            ReactiveOAuth2AuthorizedClientService authorizedClientService
    ) {
        AuthorizedClientServiceReactiveOAuth2AuthorizedClientManager manager =
                new AuthorizedClientServiceReactiveOAuth2AuthorizedClientManager(clientRegistrationRepository, authorizedClientService);
        manager.setAuthorizedClientProvider(ReactiveOAuth2AuthorizedClientProviderBuilder.builder()
                .clientCredentials() // Включаем получение токена с помощью client_credentials
                .refreshToken() // Также включаем использование refresh_token
                .build()
        );

        return manager;
    }*/
}
