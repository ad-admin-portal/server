package com.insutil.admin.repository;

import com.insutil.admin.model.SyncHistory;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

import java.time.LocalDateTime;

@Repository
public interface SyncHistoryRepository  extends R2dbcRepository<SyncHistory, Long> {
    
     @Query("SELECT " +
             "DATE_FORMAT(STR_TO_DATE(ash.sync_date, '%Y%m%d'), '%Y-%m-%d') AS sync_date" +
             ",ash.user_id " +
             ",ash.user_name" +
             ",CASE ash.user_info " +
             "WHEN 'A' THEN '입사' " +
             "WHEN 'B' THEN '퇴사' " +
             "WHEN 'C' THEN '부서이동' " +
             "ELSE '조직개편' END AS user_info " +
             ",CASE ash.success_yn " +
             "WHEN 'Y' THEN '성공' ELSE '실패' END AS success_yn " +
             "FROM " +
             "t_ad_sync_his ash " +
             "WHERE " +
             "ash.enabled = 1 " +
             "AND ash.sync_date BETWEEN REPLACE(:searchStDt, '-', '') AND REPLACE(:searchEndDt, '-', '') " +
             "ORDER BY " +
             "ash.sync_date DESC")
    Flux<SyncHistory> findBySyncHistory(String searchStDt, String searchEndDt);
    
}
