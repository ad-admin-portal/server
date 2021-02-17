package com.insutil.admin.repository;

import com.insutil.admin.model.Group;
import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface GroupRepository extends R2dbcRepository<Group, Long> {
	@Query("select * from t_group where enabled = 1")
	Flux<Group> findEnabledGroups();

	@Query("select * from t_group where enabled = 1 and id = :id")
	Mono<Group> findEnabledGroupById(Long id);

	@Modifying
	@Query("update t_group set enabled = 0 where id = :id")
	Mono<Integer> disableGroup(Long id);
}
