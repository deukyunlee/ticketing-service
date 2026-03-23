package com.ticketing.gateway.filter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Configuration
public class SimpleRateLimitFilter {

    private final Map<String, WindowCounter> counters = new ConcurrentHashMap<>();

    @Value("${app.rate-limit.max-requests:60}")
    private int maxRequests;

    @Value("${app.rate-limit.window-seconds:60}")
    private int windowSeconds;

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

            WindowCounter counter = counters.compute(key, (k, existing) -> {
                if (existing == null || nowMillis >= existing.windowStartMillis + windowMillis) {
                    return new WindowCounter(nowMillis, new AtomicInteger(1));
                }
                existing.count.incrementAndGet();
                return existing;
            });

            if (counter.count.get() > maxRequests) {
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

    private record WindowCounter(long windowStartMillis, AtomicInteger count) {
    }
}
