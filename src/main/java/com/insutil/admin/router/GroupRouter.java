package com.insutil.admin.router;

import com.insutil.admin.controller.GroupHandler;
import com.insutil.admin.filter.HandlerFunctionFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
@RequiredArgsConstructor
public class GroupRouter {
	private final HandlerFunctionFilter handlerFunctionFilter;

	@Bean
	public RouterFunction<ServerResponse> groupRoute(GroupHandler handler) {
		return RouterFunctions.route()
			.GET("/api/group", handler::getEnabledGroups)
			.GET("/api/system/group", handler::getGroups)
			.GET("/api/group/{id}", handler::getEnabledGroupById)
			.GET("/api/system/group/{id}", handler::getGroupById)
			.POST("/api/group", handler::saveGroup)
			.PUT("/api/group/{id}", handler::updateGroup)
			.DELETE("/api/group/{id}", handler::disableGroup)
			.DELETE("/api/system/group/{id}", handler::deleteGroup)
			.filter(handlerFunctionFilter)
			.build();
	}
}
