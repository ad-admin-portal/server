package com.insutil.admin.router;

import com.insutil.admin.controller.UserHandler;
import com.insutil.admin.filter.HandlerFunctionFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
@RequiredArgsConstructor
public class SystemRouter {
	private final HandlerFunctionFilter handlerFunctionFilter;

	@Bean
	public RouterFunction<ServerResponse> systemRoute(UserHandler handler) {
		return RouterFunctions.route()
			.POST("/api/login", handler::login)
			.build();
	}
}
