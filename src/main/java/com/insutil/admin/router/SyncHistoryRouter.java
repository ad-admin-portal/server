package com.insutil.admin.router;

import com.insutil.admin.controller.SyncHistoryHandler;
import com.insutil.admin.filter.HandlerFunctionFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
@RequiredArgsConstructor
public class SyncHistoryRouter {

    private final HandlerFunctionFilter handlerFunctionFilter;

    @Bean
    public RouterFunction<ServerResponse> syncHistoryRoute(SyncHistoryHandler handler){

        return RouterFunctions.route()
                .POST("/api/management/syncHistory", handler::getSyncHistory)
                .filter(handlerFunctionFilter)
                .build();
    }
}
