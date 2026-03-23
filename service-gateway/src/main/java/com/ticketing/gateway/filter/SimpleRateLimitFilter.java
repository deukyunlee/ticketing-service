package com.ticketing.gateway.filter;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;

import java.time.Duration;

@Configuration
public class SimpleRateLimitFilter {

    private Cache<String, WindowCounter> counters;

    @Value("${app.rate-limit.max-requests:60}")
    private int maxRequests;

    @Value("${app.rate-limit.window-seconds:60}")
    private int windowSeconds;

    @Value("${app.rate-limit.max-keys:10000}")
    private long maxKeys;

    @PostConstruct
    void initCache() {
        this.counters = Caffeine.newBuilder()
                .maximumSize(maxKeys)
                .expireAfterAccess(Duration.ofSeconds(windowSeconds * 2L))
                .build();
    }

    @Bean
    public GlobalFilter rateLimitGlobalFilter() {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            String path = request.getPath().value();

            if (!path.startsWith("/api/")) {
                return chain.filter(exchange);
            }

            String key = resolveKey(request);
            long nowMillis = System.currentTimeMillis();
            long windowMillis = Duration.ofSeconds(windowSeconds).toMillis();

            WindowCounter counter = counters.asMap().compute(key, (k, existing) -> {
                if (existing == null || nowMillis >= existing.windowStartMillis + windowMillis) {
                    return new WindowCounter(nowMillis, 1);
                }
                existing.count++;
                return existing;
            });

            if (counter.count > maxRequests) {
                exchange.getResponse().setStatusCode(HttpStatus.TOO_MANY_REQUESTS);
                exchange.getResponse().getHeaders().set("Retry-After", String.valueOf(windowSeconds));
                return exchange.getResponse().setComplete();
            }

            return chain.filter(exchange);
        };
    }

    private String resolveKey(ServerHttpRequest request) {
        String userId = request.getHeaders().getFirst("X-User-Id");
        if (userId != null && !userId.isBlank()) {
            return "user:" + userId;
        }
        if (request.getRemoteAddress() != null && request.getRemoteAddress().getAddress() != null) {
            return "ip:" + request.getRemoteAddress().getAddress().getHostAddress();
        }
        return "anonymous";
    }

    private static class WindowCounter {
        private long windowStartMillis;
        private int count;

        private WindowCounter(long windowStartMillis, int count) {
            this.windowStartMillis = windowStartMillis;
            this.count = count;
        }
    }
}
