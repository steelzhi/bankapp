package ru.ya.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LoggerConfiguration {

    @Bean
    Logger logger() {
        return LoggerFactory.getLogger("logger-for-front-ui");
    }

}
