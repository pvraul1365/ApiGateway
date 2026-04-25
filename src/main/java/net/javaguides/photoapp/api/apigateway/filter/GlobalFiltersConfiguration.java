package net.javaguides.photoapp.api.apigateway.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import reactor.core.publisher.Mono;

/**
 * GlobalFiltersConfiguration
 * <p>
 * Created by IntelliJ, Spring Framework Guru.
 *
 * @author architecture - pvraul
 * @version 25/04/2026 - 09:16
 * @since 1.17
 */
@Configuration
@Slf4j
public class GlobalFiltersConfiguration {

    @Order(1)
    @Bean
    public GlobalFilter secondPreFilter() {
        return (exchange, chain) -> {
            log.info("📥 - My second global Pre Filter executed...");
            return chain.filter(exchange).then(Mono.fromRunnable(() -> {
                log.info("📤 - My third global Post Filter executed...");
            }));
        };
    }

    @Order(2)
    @Bean
    public GlobalFilter thirdPreFilter() {
        return (exchange, chain) -> {
            log.info("📥 - My third global Pre Filter executed...");
            return chain.filter(exchange).then(Mono.fromRunnable(() -> {
                log.info("📤 - My second global Post Filter executed...");
            }));
        };
    }

    @Order(3)
    @Bean
    public GlobalFilter fourthPreFilter() {
        return (exchange, chain) -> {
            log.info("📥 - My fourth global Pre Filter executed...");
            return chain.filter(exchange).then(Mono.fromRunnable(() -> {
                log.info("📤 - My first global Post Filter executed...");
            }));
        };
    }

}
