package com.insutil.admin.controller;

import com.insutil.admin.model.Group;
import com.insutil.admin.repository.GroupRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class GroupHandler {
	private final GroupRepository groupRepository;

	@Autowired
	public GroupHandler(GroupRepository groupRepository) {
		this.groupRepository = groupRepository;
	}

	
	public Mono<ServerResponse> saveGroup(ServerRequest request) {
		return request.bodyToMono(Group.class)
			.map(group -> {
				String id = request.attribute("id").orElseThrow().toString();
				group.setRegistUser(Long.valueOf(id));
				return group;
			})
			.flatMap(groupRepository::save)
			.flatMap(ServerResponse.status(HttpStatus.CREATED)::bodyValue)
			.onErrorResume(error -> ServerResponse.badRequest().bodyValue(error.getMessage()));
	}

	
	public Mono<ServerResponse> getGroups(ServerRequest request) {
		return groupRepository.findAll()
			.collectList()
			.flatMap(ServerResponse.ok()::bodyValue);
	}

	
	public Mono<ServerResponse> getEnabledGroups(ServerRequest request) {
		return groupRepository.findEnabledGroups()
			.collectList()
			.flatMap(ServerResponse.ok()::bodyValue);
	}

	
	public Mono<ServerResponse> getGroupById(ServerRequest request) {
		String id = request.pathVariable("id");
		if (NumberUtils.isDigits(id)) {
			return groupRepository.findById(Long.valueOf(id))
				.flatMap(ServerResponse.ok()::bodyValue)
				.switchIfEmpty(ServerResponse.notFound().build());
		}

		return ServerResponse.badRequest().bodyValue(id);
	}

	
	public Mono<ServerResponse> getEnabledGroupById(ServerRequest request) {
		String id = request.pathVariable("id");
		if (NumberUtils.isDigits(id)) {
			return groupRepository.findEnabledGroupById(Long.valueOf(id))
				.flatMap(ServerResponse.ok()::bodyValue)
				.switchIfEmpty(ServerResponse.notFound().build());
		}

		return ServerResponse.badRequest().bodyValue(id);
	}

	
	public Mono<ServerResponse> updateGroup(ServerRequest request) {
		String id = request.pathVariable("id");
		if (!NumberUtils.isDigits(id)) {
			return ServerResponse.badRequest().bodyValue(id);
		}

		return groupRepository.findById(Long.valueOf(id))
			.flatMap(origin ->
				request.bodyToMono(Group.class)
					.map(group -> {
						String updateUser = request.attribute("id").orElseThrow().toString();
						group.setUpdateUser(Long.valueOf(updateUser));
						return origin.update(group);
					})
				.flatMap(groupRepository::save)
			)
			.flatMap(ServerResponse.ok()::bodyValue)
			.switchIfEmpty(ServerResponse.notFound().build())
			.onErrorResume(error -> ServerResponse.badRequest().bodyValue(error.getMessage()));
	}

	
	public Mono<ServerResponse> disableGroup(ServerRequest request) {
		String id = request.pathVariable("id");
		if (!NumberUtils.isDigits(id)) {
			return ServerResponse.badRequest().bodyValue(id);
		}

		return groupRepository.findById(Long.valueOf(id))
			.map(origin -> {
				String updateUser = request.attribute("id").orElseThrow().toString();
				origin.setEnabled(false);
				origin.setUpdateUser(Long.valueOf(updateUser));
				return origin;
			})
			.flatMap(groupRepository::save)
			.flatMap(ServerResponse.ok()::bodyValue)
			.switchIfEmpty(ServerResponse.notFound().build());
	}

	/*
		DB 에서 record 를 삭제
		성공 시 status  200 반환
		pathVariable 값이 long 값이 아닐 경우 status 400 반환
	 */
	
	public Mono<ServerResponse> deleteGroup(ServerRequest request) {
		String id = request.pathVariable("id");
		if (!NumberUtils.isDigits(id)) {
			return ServerResponse.badRequest().bodyValue(id);
		}

		return groupRepository.deleteById(Long.valueOf(id))
			.flatMap(ServerResponse.ok()::bodyValue);
	}
}
