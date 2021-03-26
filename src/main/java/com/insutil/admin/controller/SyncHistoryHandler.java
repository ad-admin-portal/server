package com.insutil.admin.controller;


import com.insutil.admin.model.SyncHistory;
import com.insutil.admin.repository.SyncHistoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@RequiredArgsConstructor
public class SyncHistoryHandler {

    private  final SyncHistoryRepository syncHistoryRepository;

    public Mono<ServerResponse> getSyncHistory(ServerRequest request){

        return request.bodyToMono(SyncHistory.class)
                .flatMap(syncHistory -> {
                    System.out.println("syncHistory.getSearchStDt()=== " + syncHistory.getSearchStDt());
                    System.out.println("syncHistory.getSearchEndDt()=== " + syncHistory.getSearchEndDt());
                    return syncHistoryRepository.findBySyncHistory(syncHistory.getSearchStDt(), syncHistory.getSearchEndDt())
                        .collectList()
                        .flatMap(ServerResponse.ok()::bodyValue)
                        .onErrorResume(error -> {
                            error.printStackTrace();
                            return ServerResponse.badRequest().bodyValue(error.getMessage());
                        })
                        .log();
                });
    }
}
