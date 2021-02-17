package com.insutil.admin.repository;

import com.insutil.admin.model.Role;
import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface RoleRepository extends R2dbcRepository<Role, Long> {

    @Query("select * from t_role where enabled = 1")
    Flux<Role> findAllByEnabled();

    @Modifying
    @Query("update t_role set enabled = 0 where id = :id")
    Mono<Integer> disableRole(Long id);
}
