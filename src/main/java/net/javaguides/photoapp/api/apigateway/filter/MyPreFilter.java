package net.javaguides.photoapp.api.apigateway.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * MyPreFilter
 * <p>
 * Created by IntelliJ, Spring Framework Guru.
 *
 * @author architecture - pvraul
 * @version 25/04/2026 - 08:41
 * @since 1.17
 */
@Component
@Slf4j
public class MyPreFilter implements GlobalFilter, Ordered {

     @Override
     public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
         log.info("📥 - My first Pre Filter executed...");

         String requestPath = exchange.getRequest().getPath().toString();
         log.info("Request path: {}", requestPath);

         HttpHeaders headers = exchange.getRequest().getHeaders();
         headers.forEach((key, value) -> log.info("Header: {} = {}", key, value));

         return chain.filter(exchange);
     }

    @Override
    public int getOrder() {
        return 0;
    }
}
