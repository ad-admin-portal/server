package com.insutil.admin.router;

import com.insutil.admin.controller.RoleHandler;
import com.insutil.admin.filter.HandlerFunctionFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
@RequiredArgsConstructor
public class RoleRouter {
    private final HandlerFunctionFilter handlerFunctionFilter;

    @Bean
    public RouterFunction<ServerResponse> roleRoute(RoleHandler roleHandler) {
        return RouterFunctions.route()
            .GET("/api/role", roleHandler::getEnabledRoles)
            .GET("/api/system/role", roleHandler::getRoles)
            .GET("/api/system/role/{id}", roleHandler::getRoleById)
            .POST("/api/system/role", roleHandler::addRole)
            .PUT("/api/system/role/{id}", roleHandler::updateRole)
            .DELETE("/api/system/role/{id}", roleHandler::disableRole)
            .filter(handlerFunctionFilter)
            .build();
    }
}
