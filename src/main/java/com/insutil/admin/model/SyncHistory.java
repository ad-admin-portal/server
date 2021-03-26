package com.insutil.admin.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@With
@NoArgsConstructor
@AllArgsConstructor
@Table(value = "t_ad_sync_his")
@Getter
@Setter
@Builder
@EqualsAndHashCode
public class SyncHistory {

    @Id
    private String syncDate;
    private String userId;
    private String userName;
    private String userInfo;
    private String successYn;

    @Transient
    private String searchStDt;
    @Transient
    private String searchEndDt;


}
