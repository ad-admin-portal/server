package com.insutil.admin.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Table("t_ad_mail")
@ToString
@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Mail {

    @Id
    private Long id;
    private Integer type;
    private String smsContent;
    private String mailContent;
    private LocalDateTime sendTime;
    private LocalDateTime sendDueDate;
    private Long sendType;
    private Long sendUser;
    private Long registUser;
    private Long updateUser;
    private LocalDateTime registDate;
    private LocalDateTime updateDate;
    private Boolean enabled;


}
