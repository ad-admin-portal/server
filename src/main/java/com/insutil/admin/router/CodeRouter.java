package com.insutil.admin.router;

import com.insutil.admin.controller.CodeHandler;
import com.insutil.admin.filter.HandlerFunctionFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
@RequiredArgsConstructor
public class CodeRouter {
	private final HandlerFunctionFilter handlerFunctionFilter;

	@Bean
	public RouterFunction<ServerResponse> codeRoute(CodeHandler handler) {
		return RouterFunctions.route()
			.GET("/api/code", handler::getEnabledCodes)
			.GET("/api/system/code", handler::getCodes)
			.GET("/api/code/{id}", handler::getEnabledCodeById)
			.GET("/api/system/code/{id}", handler::getCodeById)
			.POST("/api/code", handler::saveCode)f
			.PUT("/api/code/{id}", handler::updateCode)
			.PUT("/api/code/{id}/enabled/{enabled}", handler::updateCodeEnabled)
			.DELETE("/api/code/{id}", handler::disableCode)
			.DELETE("/api/system/code/{id}", handler::deleteCode)
			.filter(handlerFunctionFilter)
			.build();
	}
}
