package com.insutil.admin.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@With
@NoArgsConstructor
@AllArgsConstructor
@Table(value = "t_holiday")
@Getter
@Setter
@Builder
@EqualsAndHashCode
public class Holiday {
    @Id
    private Long id;
    @JsonAlias({"holidayDate", "startDate"})
    private String holidayDate;
    private String afterWorkingDate;
    @JsonAlias({"title", "holidayDescription"})
    private String holidayDescription;
    private Long registUser;
    private Long updateUser;
    private LocalDateTime registDate;
    private LocalDateTime updateDate;
    private Boolean enabled;

    public Holiday update(Holiday holiday) {
        if (holiday.holidayDate != null) this.holidayDate = holiday.holidayDate;
        if (holiday.afterWorkingDate != null) this.afterWorkingDate = holiday.afterWorkingDate;
        if (holiday.holidayDescription != null) this.holidayDescription = holiday.holidayDescription;
        if (holiday.updateUser != null) this.updateUser = holiday.updateUser;
        if (holiday.enabled != null) this.enabled = holiday.enabled;
        return this;
    }
}

/* DDL

CREATE TABLE `t_holiday` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `holiday_date` varchar(8) NOT NULL,
  `after_working_date` varchar(8) NOT NULL,
  `holiday_description` varchar(128) NOT NULL,
  `regist_user` int(11) NOT NULL,
  `update_user` int(11) DEFAULT NULL,
  `regist_date` datetime DEFAULT current_timestamp(),
  `update_date` datetime DEFAULT current_timestamp() ON UPDATE current_timestamp(),
  `enabled` tinyint(1) NOT NULL DEFAULT 1,
  PRIMARY KEY (`id`),
  UNIQUE KEY `t_holiday_holiday_date_uindex` (`holiday_date`),
  UNIQUE KEY `t_holiday_id_uindex` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8
*/