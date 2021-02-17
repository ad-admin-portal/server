package com.insutil.admin.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Table(value = "t_code")
@ToString
@Getter
@Setter
@Builder
@EqualsAndHashCode
public class Code {
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

	public Code update(Code code) {
		if (code.getName() != null) this.setName(code.getName());
		if (code.getDescription() != null) this.setDescription(code.getDescription());
		if (code.getParentId() != null) this.setParentId(code.getParentId());
		if (code.getEnabled() != null) this.setEnabled(code.getEnabled());
		if (code.getUpdateUser() != null) this.setUpdateUser(code.getUpdateUser());

		return this;
	}
}

/* DDL
CREATE TABLE `t_code` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL,
  `description` varchar(512) DEFAULT NULL,
  `parent_id` int(11) DEFAULT NULL,
  `regist_user` int(11) NOT NULL,
  `update_user` int(11) DEFAULT NULL,
  `regist_date` datetime DEFAULT current_timestamp(),
  `update_date` datetime DEFAULT current_timestamp() ON UPDATE current_timestamp(),
  `enabled` tinyint(1) NOT NULL DEFAULT 1,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8
*/