package com.insutil.admin.controller;

import com.insutil.admin.model.MailHistory;
import com.insutil.admin.repository.MailHistoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import reactor.util.annotation.NonNull;

@Slf4j
@Component
@RequiredArgsConstructor
public class MailHistoryHandler {

    private final MailHistoryRepository mailHistoryRepository;

    @NonNull
    public Mono<ServerResponse> getMailHistory(ServerRequest serverRequest) {
        return mailHistoryRepository.findAll()
                .collectList()
                .flatMap(ServerResponse.ok()::bodyValue);
    }

}
