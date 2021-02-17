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
public class UserRouter {
	private final HandlerFunctionFilter handlerFunctionFilter;

	@Bean
	public RouterFunction<ServerResponse> userRoute(UserHandler handler) {
		return RouterFunctions.route()
			.GET("/api/user", handler::getEnabledUsers)
			.GET("/api/system/user", handler::getUsers)
			.GET("/api/user/{id}", handler::getEnabledUserById)
			.GET("/api/system/user/{id}", handler::getUserById)
			.GET("/api/user/auth/{userId}", handler::getUserByUserId)
			.POST("/api/user", handler::createUser)
			.PUT("/api/user/{id}", handler::updateUser)
			.DELETE("/api/user/{id}", handler::disableUser)
			.DELETE("/api/system/user/{id}", handler::deleteUser)
			.filter(handlerFunctionFilter)
			.build();
	}
}
