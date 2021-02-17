package com.insutil.admin.controller;

import com.insutil.admin.model.Code;
import com.insutil.admin.repository.CodeRepository;
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
public class CodeHandler {
	private final CodeRepository codeRepository;

	@Autowired
	public CodeHandler(CodeRepository codeRepository) {
		this.codeRepository = codeRepository;
	}

	public Mono<ServerResponse> saveCode(ServerRequest request) {
		return request.bodyToMono(Code.class)
			.map(code -> {
				String id = request.attribute("id").orElseThrow().toString();
				code.setRegistUser(Long.valueOf(id));
				return code;
			})
			.flatMap(codeRepository::save)
			.flatMap(ServerResponse.status(HttpStatus.CREATED)::bodyValue)
			.onErrorResume(error -> ServerResponse.badRequest().bodyValue(error.getMessage()));
	}

	public Mono<ServerResponse> getCodes(ServerRequest request) {
		return codeRepository.findAll()
			.collectList()
			.flatMap(ServerResponse.ok()::bodyValue);
	}

	public Mono<ServerResponse> getEnabledCodes(ServerRequest request) {
		return codeRepository.findEnabledCodes()
			.collectList()
			.flatMap(ServerResponse.ok()::bodyValue);
	}

	public Mono<ServerResponse> getCodeById(ServerRequest request) {
		String id = request.pathVariable("id");
		if (NumberUtils.isDigits(id)) {
			return codeRepository.findById(Long.valueOf(id))
				.flatMap(ServerResponse.ok()::bodyValue)
				.switchIfEmpty(ServerResponse.notFound().build());
		}

		return ServerResponse.badRequest().bodyValue(id);
	}

	public Mono<ServerResponse> getEnabledCodeById(ServerRequest request) {
		String id = request.pathVariable("id");
		if (NumberUtils.isDigits(id)) {
			return codeRepository.findEnabledCodeById(Long.valueOf(id))
				.flatMap(ServerResponse.ok()::bodyValue)
				.switchIfEmpty(ServerResponse.notFound().build());
		}

		return ServerResponse.badRequest().bodyValue(id);
	}

	public Mono<ServerResponse> updateCode(ServerRequest request) {
		String id = request.pathVariable("id");
		if (NumberUtils.isDigits(id)) {
			return codeRepository.findById(Long.valueOf(id))
				.flatMap(origin ->
					request.bodyToMono(Code.class)
					.map(code -> {
						String updateUser = request.attribute("id").orElseThrow().toString();
						code.setUpdateUser(Long.valueOf(updateUser));
						return origin.update(code);
					})
					.flatMap(codeRepository::save)
				)
				.flatMap(ServerResponse.ok()::bodyValue)
				.switchIfEmpty(ServerResponse.notFound().build());
		}

		return ServerResponse.badRequest().bodyValue(id);
	}

	public Mono<ServerResponse> updateCodeEnabled(ServerRequest request) {
		String id = request.pathVariable("id");
		String enabled = request.pathVariable("enabled");
		if (!NumberUtils.isDigits(id) && ("true".equals(enabled) || "false".equals(enabled))) {
			return ServerResponse.badRequest().bodyValue("id: " + id + ", enabled: " + enabled);
		}

		return codeRepository.findById(Long.valueOf(id))
			.flatMap(found ->
				codeRepository.enableCode(Long.valueOf(id), Boolean.parseBoolean(enabled))
			)
			.flatMap(ServerResponse.ok()::bodyValue)
			.switchIfEmpty(ServerResponse.notFound().build());
	}

	public Mono<ServerResponse> disableCode(ServerRequest request) {
		String id = request.pathVariable("id");
		if (!NumberUtils.isDigits(id)) {
			return ServerResponse.badRequest().bodyValue(id);
		}
		return codeRepository.findById(Long.valueOf(id))
			.map(origin -> {
				String updateUser = request.attribute("id").orElseThrow().toString();
				origin.setEnabled(false);
				origin.setUpdateUser(Long.valueOf(updateUser));
				return origin;
			})
			.flatMap(codeRepository::save)
			.flatMap(ServerResponse.ok()::bodyValue)
			.switchIfEmpty(ServerResponse.notFound().build());
	}

	public Mono<ServerResponse> deleteCode(ServerRequest request) {
		String id = request.pathVariable("id");
		if (NumberUtils.isDigits(id)) {
			return codeRepository.deleteById(Long.valueOf(id))
				.flatMap(ServerResponse.ok()::bodyValue);
		}

		return ServerResponse.badRequest().bodyValue(id);
	}
}
