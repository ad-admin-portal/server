package com.insutil.admin.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Table;

import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@With
@NoArgsConstructor
@AllArgsConstructor
@Table(value = "t_user")
@ToString(exclude = "password")
@Getter
@Setter
@Builder
@EqualsAndHashCode
public class User {
	@Id
	private Long id;
	private String userId;
	private String userName;
	private String password;
	private Long groupId;
	private Long roleId;
	@Transient
	private String roleName;
	@Transient
	private String token;
	private Long registUser;
	private Long updateUser;
	private LocalDateTime registDate;
	private LocalDateTime updateDate;
	private Boolean enabled;

	public User update(User user) {
		if (user.userName != null) this.userName = user.userName;
		if (user.password != null) this.password = user.password;
		if (user.groupId != null) this.groupId = user.groupId;
		if (user.roleId != null) this.roleId = user.roleId;
		if (user.enabled != null) this.enabled = user.enabled;
		if (user.updateUser != null) this.updateUser = user.updateUser;
		return this;
	}
}

/* DDL
CREATE TABLE `t_user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` varchar(100) NOT NULL,
  `user_name` varchar(100) NOT NULL,
  `password` varchar(100) NOT NULL,
  `group_id` int(11) DEFAULT NULL,
  `role_id` int(11) NOT NULL,
  `regist_user` int(11) DEFAULT NULL,
  `update_user` int(11) DEFAULT NULL,
  `regist_date` datetime DEFAULT current_timestamp(),
  `update_date` datetime DEFAULT current_timestamp() ON UPDATE current_timestamp(),
  `enabled` tinyint(1) NOT NULL DEFAULT 1,
  PRIMARY KEY (`id`),
  UNIQUE KEY `t_user_user_id_UN` (`user_id`),
  KEY `t_user_group_id_FK` (`group_id`),
  KEY `t_user_role_id_FK` (`role_id`),
  CONSTRAINT `t_user_group_id_FK` FOREIGN KEY (`group_id`) REFERENCES `t_group` (`id`),
  CONSTRAINT `t_user_role_id_FK` FOREIGN KEY (`role_id`) REFERENCES `t_role` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8
*/