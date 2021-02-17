package com.insutil.admin.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
class JwtAuthenticationConverter implements ServerAuthenticationConverter {
	private final JwtTokenProvider jwtTokenProvider;
	private final ReactiveUserDetailsService reactiveUserDetailsService;

	@Autowired
	public JwtAuthenticationConverter(JwtTokenProvider jwtTokenProvider, ReactiveUserDetailsService reactiveUserDetailsService) {
		this.jwtTokenProvider = jwtTokenProvider;
		this.reactiveUserDetailsService = reactiveUserDetailsService;
	}
	@Override
    public Mono<Authentication> convert(ServerWebExchange exchange) {
        return Mono.justOrEmpty(exchange)
            .flatMap (it -> Mono.justOrEmpty(it.getRequest().getHeaders().get("X-AUTH-TOKEN")))
            .filter (it -> it != null && jwtTokenProvider.validateToken(it.get(0)) )
            .onErrorResume (error -> Mono.empty())
            .flatMap (it -> reactiveUserDetailsService.findByUsername(jwtTokenProvider.getUserId(it.get(0))) )
            .flatMap (it -> Mono.just(new UsernamePasswordAuthenticationToken(it.getUsername(), it.getPassword(), it.getAuthorities())));
    }
}