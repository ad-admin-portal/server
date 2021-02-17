package com.insutil.admin.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Table;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;

@With
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString
@EqualsAndHashCode
@Table(value = "t_role")
public class Role {
    @Id
    private Long id;
    private String name;
    private String description;
    private Long registUser;
    private Long updateUser;
    private LocalDateTime registDate;
    private LocalDateTime updateDate;
    private Boolean enabled;

    public Role update(Role target) {
        if (target.name != null) this.name = target.name;
        if (target.description != null) this.description = target.description;
        if (target.enabled != null) this.enabled = target.enabled;
        if (target.updateUser != null) this.updateUser = target.updateUser;
        return this;
    }
}

/*
CREATE TABLE `t_role` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL,
  `description` varchar(256) DEFAULT '',
  `regist_user` int(11) NOT NULL,
  `update_user` int(11) DEFAULT NULL,
  `regist_date` datetime DEFAULT current_timestamp(),
  `update_date` datetime DEFAULT current_timestamp() ON UPDATE current_timestamp(),
  `enabled` tinyint(1) NOT NULL DEFAULT 1,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8
*/