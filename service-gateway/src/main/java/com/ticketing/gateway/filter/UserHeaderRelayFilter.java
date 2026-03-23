package com.ticketing.gateway.filter;

import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import reactor.core.publisher.Mono;

@Configuration
public class UserHeaderRelayFilter {

    @Bean
    public GlobalFilter userIdRelayGlobalFilter() {
        return (exchange, chain) -> ReactiveSecurityContextHolder.getContext()
                .map(securityContext -> securityContext.getAuthentication())
                .filter(authentication -> authentication instanceof JwtAuthenticationToken)
                .cast(JwtAuthenticationToken.class)
                .flatMap(authentication -> {
                    String userId = authentication.getToken().getSubject();
                    ServerHttpRequest requestWithUserId = exchange.getRequest().mutate()
                            .headers(headers -> headers.set("X-User-Id", userId))
                            .build();
                    return chain.filter(exchange.mutate().request(requestWithUserId).build());
                })
                .switchIfEmpty(Mono.defer(() -> chain.filter(exchange)));
    }
}
