package com.insutil.admin.controller;

import com.insutil.admin.model.User;
import com.insutil.admin.repository.UserRepository;
import com.insutil.admin.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;


@Slf4j
@Component
@RequiredArgsConstructor
public class UserHandler {
	private final UserRepository userRepository;
	private final BCryptPasswordEncoder passwordEncoder;
	private final JwtTokenProvider jwtTokenProvider;

	
	public Mono<ServerResponse> createUser(ServerRequest request) {
		return request.bodyToMono(User.class)
			.flatMap(userRepository::save)
			.flatMap(ServerResponse.status(HttpStatus.CREATED)::bodyValue)
			.onErrorResume(error -> ServerResponse.badRequest().bodyValue(error.getMessage()));
	}

	
	public Mono<ServerResponse> getUsers(ServerRequest request) {
		return userRepository.findAll()
			.map(user -> {
				user.setPassword("");
				return user;
			})
			.collectList()
			.flatMap(ServerResponse.ok()::bodyValue);
	}

	
	public Mono<ServerResponse> getEnabledUsers(ServerRequest request) {
		return userRepository.findAllByEnabled()
			.map(user -> {
				user.setPassword("");
				return user;
			})
			.collectList()
			.flatMap(ServerResponse.ok()::bodyValue);
	}

	
	public Mono<ServerResponse> getUserById(ServerRequest request) {
		String id = request.pathVariable("id");
		if (!NumberUtils.isDigits(id)) {
			return ServerResponse.badRequest().bodyValue(id);
		}
		return userRepository.findById(Long.valueOf(id))
			.map(user -> {
				user.setPassword("");
				return user;
			})
			.flatMap(ServerResponse.ok()::bodyValue)
			.switchIfEmpty(ServerResponse.notFound().build());

	}

	
	public Mono<ServerResponse> getEnabledUserById(ServerRequest request) {
		String id = request.pathVariable("id");
		if (!NumberUtils.isDigits(id)) {
			return ServerResponse.badRequest().bodyValue(id);
		}
		return userRepository.findEnabledUserById(Long.valueOf(id))
			.map(user -> {
				user.setPassword("");
				return user;
			})
			.flatMap(ServerResponse.ok()::bodyValue)
			.switchIfEmpty(ServerResponse.notFound().build());
	}

	
	public Mono<ServerResponse> getUserByUserId(ServerRequest request) {
		return userRepository.findByUserId(request.pathVariable("userId"))
			.map(user -> {
				user.setPassword("");
				return user;
			})
			.flatMap(ServerResponse.ok()::bodyValue)
			.switchIfEmpty(ServerResponse.notFound().build());
	}

	
	public Mono<ServerResponse> updateUser(ServerRequest request) {
		String id = request.pathVariable("id");
		if (!NumberUtils.isDigits(id)) {
			return ServerResponse.badRequest().bodyValue(id);
		}
		return userRepository.findById(Long.valueOf(id))
			.flatMap(origin ->
				request.bodyToMono(User.class)
				.map(user -> {
					String updateUser = request.attribute("id").orElseThrow().toString();
					user.setUpdateUser(Long.valueOf(updateUser));
					return origin.update(user);
				})
				.flatMap(userRepository::save)
			)
			.flatMap(ServerResponse.ok()::bodyValue)
			.switchIfEmpty(ServerResponse.notFound().build());
	}

	
	public Mono<ServerResponse> disableUser(ServerRequest request) {
		String id = request.pathVariable("id");
		if (!NumberUtils.isDigits(id)) {
			return ServerResponse.badRequest().bodyValue(id);
		}

		return userRepository.findById(Long.valueOf(id))
			.map(user -> {
				String updateUser = request.attribute("id)").orElseThrow().toString();
				user.setEnabled(false);
				user.setUpdateUser(Long.valueOf(updateUser));
				return user;
			})
			.flatMap(userRepository::save)
			.flatMap(ServerResponse.ok()::bodyValue)
			.switchIfEmpty(ServerResponse.notFound().build());
	}

	
	public Mono<ServerResponse> deleteUser(ServerRequest request) {
		String id = request.pathVariable("id");
		if (!NumberUtils.isDigits(id)) {
			return ServerResponse.badRequest().bodyValue(id);
		}

		return userRepository.deleteById(Long.valueOf(id))
			.flatMap(ServerResponse.ok()::bodyValue);
	}

	public Mono<ServerResponse> login(ServerRequest request) {
		return request.bodyToMono(User.class)
			.flatMap(user ->
					userRepository.findByUserId(user.getUserId())
						.flatMap(found -> {
							if (passwordEncoder.matches(user.getPassword(), found.getPassword())) {
								found.setPassword(null);
								found.setToken(jwtTokenProvider.createToken(found));
								return ServerResponse.ok().bodyValue(found);
							} else {
								return Mono.error(new Exception(HttpStatus.UNAUTHORIZED.toString()));
							}
						})
			);
	}
}

