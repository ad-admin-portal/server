package com.insutil.admin.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDate;

@Table(value = "t_ad_mail_his")
@ToString
@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class MailHistory {

    @Id
    private Long id;
    private String target;
    private String title;
    private String content;
    private String sendType;
    private LocalDate sendDate;
    private Boolean sendStatus;

}
