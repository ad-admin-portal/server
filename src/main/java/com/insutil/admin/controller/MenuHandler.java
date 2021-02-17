package com.insutil.admin.controller;

import com.insutil.admin.model.Menu;
import com.insutil.admin.repository.MenuRepository;
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
public class MenuHandler {
	private final MenuRepository menuRepository;

	@Autowired
	public MenuHandler(MenuRepository menuRepository) {
		this.menuRepository = menuRepository;
	}

	
	public Mono<ServerResponse> saveMenu(ServerRequest request) {
		return request.bodyToMono(Menu.class)
			.flatMap(menuRepository::save)
			.flatMap(ServerResponse.status(HttpStatus.CREATED)::bodyValue)
			.onErrorResume(error -> ServerResponse.badRequest().bodyValue(error.getMessage()));
	}

	
	public Mono<ServerResponse> getMenus(ServerRequest request) {
		return menuRepository.findAll()
			.collectList()
			.flatMap(ServerResponse.ok()::bodyValue);
	}

	
	public Mono<ServerResponse> getEnabledMenus(ServerRequest request) {
		return menuRepository.findEnabledMenus()
			.collectList()
			.flatMap(ServerResponse.ok()::bodyValue);
	}

	
	public Mono<ServerResponse> getMenuById(ServerRequest request) {
		String id = request.pathVariable("id");
		if (NumberUtils.isDigits(id)) {
			return menuRepository.findById(Long.valueOf(id))
				.flatMap(ServerResponse.ok()::bodyValue)
				.switchIfEmpty(ServerResponse.notFound().build());
		}

		return ServerResponse.badRequest().bodyValue(id);
	}

	
	public Mono<ServerResponse> getEnabledMenuById(ServerRequest request) {
		String id = request.pathVariable("id");
		if (NumberUtils.isDigits(id)) {
			return menuRepository.findEnabledMenuById(Long.valueOf(id))
				.flatMap(ServerResponse.ok()::bodyValue)
				.switchIfEmpty(ServerResponse.notFound().build());
		}

		return ServerResponse.badRequest().bodyValue(id);
	}

	
	public Mono<ServerResponse> updateMenu(ServerRequest request) {
		String id = request.pathVariable("id");
		if (NumberUtils.isDigits(id)) {
			return menuRepository.findById(Long.valueOf(id))
				.flatMap(origin ->
					request.bodyToMono(Menu.class)
					.map(menu -> {
						String updateUser = request.attribute("id").orElseThrow().toString();
						menu.setUpdateUser(Long.valueOf(updateUser));
						return origin.update(menu);
					})
					.flatMap(menuRepository::save)
				)
				.flatMap(ServerResponse.ok()::bodyValue)
				.switchIfEmpty(ServerResponse.notFound().build())
				.onErrorResume(error -> ServerResponse.badRequest().bodyValue(error.getMessage()));
		}

		return ServerResponse.badRequest().bodyValue(id);
	}

	
	public Mono<ServerResponse> disableMenu(ServerRequest request) {
		String id = request.pathVariable("id");
		if (!NumberUtils.isDigits(id)) {
			return ServerResponse.badRequest().bodyValue(id);
		}

		return menuRepository.findById(Long.valueOf(id))
			.map(origin -> {
				String updateUser = request.attribute("id").orElseThrow().toString();
				origin.setEnabled(false);
				origin.setUpdateUser(Long.valueOf(updateUser));
				return origin;
			})
			.flatMap(menuRepository::save)
			.flatMap(ServerResponse.ok()::bodyValue)
			.switchIfEmpty(ServerResponse.notFound().build());
	}

	
	public Mono<ServerResponse> deleteMenu(ServerRequest request) {
		String id = request.pathVariable("id");
		if (NumberUtils.isDigits(id)) {
			return menuRepository.deleteById(Long.valueOf(id))
				.flatMap(ServerResponse.ok()::bodyValue);
		}

		return ServerResponse.badRequest().bodyValue(id);
	}
}
