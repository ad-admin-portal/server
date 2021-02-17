package com.insutil.admin.repository;

import com.insutil.admin.model.Menu;
import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface MenuRepository extends R2dbcRepository<Menu, Long> {
	@Query("select * from t_menu where enabled = 1 and id = :id")
	Mono<Menu> findEnabledMenuById(Long id);

	@Query("select * from t_menu where enabled = 1 order by parent_id, sort")
	Flux<Menu> findEnabledMenus();

	@Modifying
	@Query("update t_menu set enabled = 0 where id = :id")
	Mono<Integer> disableMenu(Long id);
}
