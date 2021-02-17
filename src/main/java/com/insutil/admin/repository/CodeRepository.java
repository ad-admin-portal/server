package com.insutil.admin.repository;

import com.insutil.admin.model.Code;
import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface CodeRepository extends R2dbcRepository<Code, Long> {

	@Query("select * from t_code where enabled = 1")
	Flux<Code> findEnabledCodes();

	@Query("select * from t_code where enabled = 1 and id = :id")
	Mono<Code> findEnabledCodeById(Long id);

	@Modifying
	@Query("update t_code set enabled = 0 where id = :id")
	Mono<Integer> disableCode(Long id);

	@Modifying
	@Query("update t_code set enabled = :enabled where id in (" +
		"with recursive code as (" +
		"select id from t_code where id = :id " +
		"union all " +
		"select child.id from t_code as child, code as parent " +
		"where parent.id = child.parent_id) " +
		"select id from code)")
	Mono<Integer> enableCode(Long id, boolean enabled);
}
