package com.insutil.admin.filter;

import com.insutil.admin.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.HandlerFilterFunction;
import org.springframework.web.reactive.function.server.HandlerFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Configuration
@RequiredArgsConstructor
public
class HandlerFunctionFilter implements HandlerFilterFunction<ServerResponse, ServerResponse> {
	private final JwtTokenProvider jwtTokenProvider;

    @Override
	public Mono<ServerResponse> filter(ServerRequest request, HandlerFunction<ServerResponse> next) {
        request.attributes().put("id", jwtTokenProvider.getId(request));
        return next.handle(request);
    }
}