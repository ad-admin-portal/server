package com.insutil.admin.router;

import com.insutil.admin.controller.MenuHandler;
import com.insutil.admin.filter.HandlerFunctionFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
@RequiredArgsConstructor
public class MenuRouter {
	private final HandlerFunctionFilter handlerFunctionFilter;

	@Bean
	public RouterFunction<ServerResponse> menuRoute(MenuHandler handler) {
		return RouterFunctions.route()
			.GET("/api/menu", handler::getEnabledMenus)
			.GET("/api/menu/{id}", handler::getEnabledMenuById)
			.build();
	}

	@Bean
	public RouterFunction<ServerResponse> menuAuthorizedRoute(MenuHandler handler) {
		return RouterFunctions.route()
			.GET("/api/system/menu", handler::getMenus)
			.POST("/api/menu", handler::saveMenu)
			.PUT("/api/menu/{id}", handler::updateMenu)
			.DELETE("/api/menu/{id}", handler::disableMenu)
			.DELETE("/api/system/menu/{id}", handler::deleteMenu)
			.filter(handlerFunctionFilter)
			.build();
	}
}
