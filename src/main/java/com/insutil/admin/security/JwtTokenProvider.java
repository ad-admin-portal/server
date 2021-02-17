package com.insutil.admin.security;

import com.insutil.admin.model.User;
import com.insutil.admin.repository.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.server.ServerWebExchange;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;
import java.util.*;

@Component
public
class JwtTokenProvider {
    private final byte[] key = "insutil".getBytes(StandardCharsets.UTF_8);
    private final UserRepository userRepository;

    @Autowired
    public JwtTokenProvider(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public String createToken(User user) {
        Claims claims = Jwts.claims().setSubject(user.getUserId());
        claims.put("id", user.getId());
        return Jwts.builder()
            .setClaims(claims)
            .setIssuer("AdAdminPortal")
            .setIssuedAt(new Date())
            .setExpiration(Date.from(Instant.now().plus(Duration.ofMinutes(3000))))
            .signWith(SignatureAlgorithm.HS256, key)
            .compact();
    }

    public boolean validateToken(String token) {
        return !Jwts.parser()
            .setSigningKey(key)
            .parseClaimsJws(token)
            .getBody()
            .getExpiration()
            .before(new Date());
    }

    public String getUserId(String token) {
        return Jwts.parser()
            .setSigningKey(key)
            .parseClaimsJws(token)
            .getBody()
            .getSubject();
    }

    public Long getId(ServerRequest req) {
        return Long.valueOf(
            Jwts.parser()
                .setSigningKey(key)
                .parseClaimsJws(req.headers().header("X-AUTH-TOKEN").get(0))
                .getBody().get("id")
                .toString()
        );
    }

    public Long getId(ServerWebExchange exchange) {
        return Long.valueOf(
            Jwts.parser()
                .setSigningKey(key)
                .parseClaimsJws(Objects.requireNonNull(exchange.getRequest().getHeaders().get("X-AUTH-TOKEN")).get(0))
                .getBody().get("id")
                .toString()
        );
    }

    @Bean
    public ReactiveUserDetailsService userDetailsService() {
        return username -> userRepository.findByUserId(username)
            .map(user ->
                org.springframework.security.core.userdetails.User.builder()
                    .username(user.getUserId())
                    .password(user.getPassword())
                    .authorities(user.getRoleName())
                    .build()
            );
    }

    @Bean
    public BCryptPasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}