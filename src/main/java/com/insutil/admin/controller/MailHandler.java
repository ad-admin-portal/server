package com.insutil.admin.controller;

import com.insutil.admin.model.Mail;
import com.insutil.admin.repository.MailRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@RequiredArgsConstructor
public class MailHandler {

    private final MailRepository mailRepository;

    @NonNull
    public Mono<ServerResponse> getMailInfo(ServerRequest serverRequest) {
        return mailRepository.findAll()
                .collectList()
                .flatMap(ServerResponse.ok()::bodyValue);
    }

    @NonNull
    public Mono<ServerResponse> saveMailInfo(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(Mail.class)
                .flatMap(mailRepository::save)
                .flatMap(ServerResponse.status(HttpStatus.CREATED)::bodyValue)
                .onErrorResume(error -> ServerResponse.badRequest().bodyValue(error.getMessage()));
    }

    @NonNull
    public Mono<ServerResponse> updateMailInfo(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(Mail.class)
                .flatMap(mailRepository::save)
                .flatMap(ServerResponse.status(HttpStatus.CREATED)::bodyValue)
                .onErrorResume(error -> ServerResponse.badRequest().bodyValue(error.getMessage()));
    }

}
