package com.insutil.admin.router;

import com.insutil.admin.controller.MailHandler;
import com.insutil.admin.controller.MailHistoryHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class MailRouter {
    @Bean
    public RouterFunction<ServerResponse> mailRoute(MailHandler mailHandler) {
        return RouterFunctions.route()
                .GET("/api/management/mail", mailHandler::getMailInfo)
                .POST("/api/management/mail" , mailHandler::saveMailInfo)
                .build();
    }

}
