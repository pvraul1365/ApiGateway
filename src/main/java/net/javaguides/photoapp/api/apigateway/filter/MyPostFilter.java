package net.javaguides.photoapp.api.apigateway.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * MyPostFilter
 * <p>
 * Created by IntelliJ, Spring Framework Guru.
 *
 * @author architecture - pvraul
 * @version 25/04/2026 - 09:09
 * @since 1.17
 */
@Component
@Slf4j
public class MyPostFilter implements GlobalFilter, Ordered {

     @Override
     public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
         return chain.filter(exchange).then(Mono.fromRunnable(() -> {
             log.info("📤 - My last Post Filter executed...");
         }));
     }

    @Override
    public int getOrder() {
        return 0;
    }
}
