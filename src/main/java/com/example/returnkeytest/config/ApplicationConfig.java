package com.example.returnkeytest.config;

import lombok.*;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.convert.DurationUnit;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

@Configuration
@ConfigurationProperties(prefix = "app")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ApplicationConfig {
    private Token token;

    @Setter
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Token {
        @DurationUnit(ChronoUnit.MINUTES)
        private Duration timeToLive = Duration.ofMinutes(5);
    }
}
