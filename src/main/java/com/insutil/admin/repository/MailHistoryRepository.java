package com.insutil.admin.repository;

import com.insutil.admin.model.MailHistory;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MailHistoryRepository extends R2dbcRepository<MailHistory, Long> {

}
