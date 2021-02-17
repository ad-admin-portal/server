package com.insutil.admin.repository;

import com.insutil.admin.model.Holiday;
import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface HolidayRepository extends R2dbcRepository<Holiday, Long> {

	@Query("select * from t_holiday where enabled = 1")
	Flux<Holiday> findByEnabled();

	@Query("select * from t_holiday where enabled = 1 and id = :id")
	Mono<Holiday> getHolidayById(Long id);

	@Query("select * from t_holiday where enabled = 1 and holiday_date = :holidayDate")
	Mono<Holiday> getHolidayByDate(String holidayDate);

	@Query("select * from t_holiday where enabled = 1 and after_working_date = :afterWorkingDate")
	Flux<Holiday> getHolidaysByAfterWorkingDate(String afterWorkingDate);

	@Modifying
	@Query("update t_holiday set enabled = 0 and update_user = :loginUserId where id = :id")
	Mono<Integer> disableHoliday(Long id, Long loginUserId);
}
