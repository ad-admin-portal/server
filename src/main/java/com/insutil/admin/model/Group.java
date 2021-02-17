package com.insutil.admin.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Table;
import reactor.core.publisher.Flux;

import java.time.LocalDateTime;
import java.util.List;


@Table(value = "t_group")
@ToString
@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class Group {
	@Id
	private Long id;
	private String name;
	private String description;
	private Long parentId;
	private Long registUser;
	private Long updateUser;
	private LocalDateTime registDate;
	private LocalDateTime updateDate;
	private Boolean enabled;

	@With
	@Transient
	private List<User> users;

	public Group update(Group group) {
		if (group.getName() != null) this.setName(group.getName());
		if (group.getDescription() != null) this.setDescription(group.getDescription());
		if (group.getParentId() != null) this.setParentId(group.getParentId());
		if (group.getUpdateUser() != null) this.setUpdateUser(group.getUpdateUser());
		if (group.getEnabled() != null) this.setEnabled(group.getEnabled());
		return this;
	}
}

/* DDL
CREATE TABLE `t_group` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL,
  `description` varchar(512) DEFAULT NULL,
  `parent_id` int(11) DEFAULT NULL,
  `regist_user` int(11) DEFAULT NULL,
  `update_user` int(11) DEFAULT NULL,
  `regist_date` datetime DEFAULT current_timestamp(),
  `update_date` datetime DEFAULT current_timestamp() ON UPDATE current_timestamp(),
  `enabled` tinyint(1) NOT NULL DEFAULT 1,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8

*/