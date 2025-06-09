package config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.client.RestClient;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;
import ru.ya.dto.UserDto;
import ru.ya.enums.Roles;
import ru.ya.model.UserPrincipal;

import java.time.LocalDate;

@TestConfiguration
@EnableWebSecurity
public class ExchangeGeneratorConfigTest {
    @Bean
    @Primary
    UserDetailsService userDetailsService() {
        return username -> new UserPrincipal(new UserDto(1, "1", "1", "1", "1", LocalDate.now(), Roles.USER));
    }

    @Bean
    @Primary
    public SecurityFilterChain securityWebFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/get-register-form", "/register-user", "/actuator/health").permitAll()
                        .anyRequest().authenticated()
                )
                .csrf().disable();

                return http.build();
    }

    @Bean
    RestClient restClient() {
        return RestClient.create();
    }

    @Bean
    Logger logger() {
        return LoggerFactory.getLogger("logger-for-accounts");
    }

    @Bean(name = "mvcHandlerMappingIntrospector")
    public HandlerMappingIntrospector mvcHandlerMappingIntrospector() {
        return new HandlerMappingIntrospector();
    }
}


