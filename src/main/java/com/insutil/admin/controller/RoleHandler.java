package com.insutil.admin.controller;

import com.insutil.admin.model.Role;
import com.insutil.admin.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;


@Slf4j
@Component
@RequiredArgsConstructor
public class RoleHandler{
    private final RoleRepository roleRepository;

    
    public Mono<ServerResponse> getRoles(ServerRequest request) {
        return roleRepository.findAll()
            .collectList()
            .flatMap(ServerResponse.ok()::bodyValue);
    }

    
    public Mono<ServerResponse> getEnabledRoles(ServerRequest request) {
        return roleRepository.findAllByEnabled()
            .collectList()
            .flatMap(ServerResponse.ok()::bodyValue);
    }

    
    public Mono<ServerResponse> getRoleById(ServerRequest request) {
        String id = request.pathVariable("id");
        if (!NumberUtils.isDigits(id)) {
            return ServerResponse.badRequest().bodyValue(id);
        }
        return roleRepository.findById(Long.valueOf(id))
            .flatMap(ServerResponse.ok()::bodyValue);
    }

    
    public Mono<ServerResponse> addRole(ServerRequest request) {
        return request.bodyToMono(Role.class)
            .flatMap(roleRepository::save)
            .flatMap(ServerResponse.status(HttpStatus.CREATED)::bodyValue)
            .onErrorResume(error -> ServerResponse.badRequest().bodyValue(error.getMessage()));
    }

    
    public Mono<ServerResponse> updateRole(ServerRequest request) {
        String id = request.pathVariable("id");
        if (!NumberUtils.isDigits(id)) {
            return ServerResponse.badRequest().bodyValue(id);
        }
        return  roleRepository.findById(Long.valueOf(id))
            .flatMap(origin ->
                request.bodyToMono(Role.class)
                    .map(role -> {
                        String updateUser = request.attribute("id").orElseThrow().toString();
                        role.setUpdateUser(Long.valueOf(updateUser));
                        return origin.update(role);
                    })
                .flatMap(roleRepository::save)
            )
            .flatMap(ServerResponse.ok()::bodyValue)
            .switchIfEmpty(ServerResponse.notFound().build());
    }

    
    public Mono<ServerResponse> disableRole(ServerRequest request) {
        String id = request.pathVariable("id");
        if (!NumberUtils.isDigits(id)) {
            return ServerResponse.badRequest().bodyValue(id);
        }

        return roleRepository.findById(Long.valueOf(id))
            .map(role -> {
                String updateUser = request.attribute("id").orElseThrow().toString();
                role.setEnabled(false);
                role.setUpdateUser(Long.valueOf(updateUser));
                return role;
            })
            .flatMap(roleRepository::save)
            .flatMap(ServerResponse.ok()::bodyValue)
            .switchIfEmpty(ServerResponse.notFound().build());
    }

}
