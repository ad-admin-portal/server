package com.insutil.admin.repository;

import com.insutil.admin.model.Mail;
import org.springframework.data.r2dbc.repository.R2dbcRepository;

public interface MailRepository  extends R2dbcRepository<Mail, Long> {

}
