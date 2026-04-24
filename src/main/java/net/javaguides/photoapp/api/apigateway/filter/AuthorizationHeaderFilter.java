package net.javaguides.photoapp.api.apigateway.filter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.util.Base64;
import javax.crypto.SecretKey;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * AuthorizationHeaderFilter
 * <p>
 * Created by IntelliJ, Spring Framework Guru.
 *
 * @author architecture - pvraul
 * @version 22/04/2026 - 17:39
 * @since 1.17
 */
@Component
@Slf4j
public class AuthorizationHeaderFilter extends AbstractGatewayFilterFactory<AuthorizationHeaderFilter.Config> {

    private final Environment env;

    public AuthorizationHeaderFilter(Environment env) {
        super(Config.class);
        this.env = env;
    }

    public static class Config {
        // Put configuration properties here
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();

            // Check if the Authorization header is present
            if (!request.getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                // If not present, return an error response
                return this.onError(exchange, "No Authorization header", HttpStatus.UNAUTHORIZED);
            }

            final String authorizationHeader = request.getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);
            final String token = authorizationHeader.replace("Bearer ", "");

            if (!this.isJwtValid(token)) {
                return this.onError(exchange, "Invalid token", HttpStatus.UNAUTHORIZED);
            }

            // continue processing the request
            return chain.filter(exchange);
        };
    }

    private Mono<Void> onError(ServerWebExchange exchange, String err, HttpStatus httpStatus) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(httpStatus);

        return response.setComplete();
    }

    private boolean isJwtValid(final String jwt) {
        boolean isValid = true;

        String subject = null;
        final String tokenSecret = env.getProperty("token.secret");
        final byte[] secretKeyBytes = Base64.getEncoder().encode(tokenSecret.getBytes());
        final SecretKey secretKey = Keys.hmacShaKeyFor(secretKeyBytes);

        JwtParser parser = Jwts.parser()
                .verifyWith(secretKey)
                .build();

        try {
            Claims claims = parser.parseSignedClaims(jwt).getPayload();
            subject = (String) claims.get("sub");

        } catch (Exception ex) {
            isValid = false;
        }

        if (subject == null || subject.isEmpty()) {
            isValid = false;
        }

        log.info("🔑 - isJwtValid: {}", isValid);
        return isValid;
    }
}
