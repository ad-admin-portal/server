package com.insutil.admin.router;

import com.insutil.admin.controller.MailHistoryHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class MailHistoryRouter {

    @Bean
    public RouterFunction<ServerResponse> mailHistoryRoute(MailHistoryHandler mailHistoryHandler) {
        return RouterFunctions.route()
                .GET("/api/history/mail", mailHistoryHandler::getMailHistory)
                .build();
    }

}
