package com.insutil.admin.repository;

import com.insutil.admin.model.User;
import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface UserRepository extends R2dbcRepository<User, Long> {
	@Query("select * from t_user where enabled = 1")
	Flux<User> findAllByEnabled();

	@Query("select * from t_user where enabled = 1 and id = :id")
	Mono<User> findEnabledUserById(Long id);

	@Query("select user.*, role.name as role_name " +
		"from t_user user " +
		"left join t_role role on role.id = user.role_id " +
		"where user.enabled = 1 and user.user_id = :userId")
	Mono<User> findByUserId(String userId);

	@Modifying
	@Query("update t_user set enabled = 0 where id = :id")
	Mono<Integer> disableUser(Long id);
}
