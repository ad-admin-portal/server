package com.insutil.admin.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;
import java.util.List;

@With
@NoArgsConstructor
@AllArgsConstructor
@Table(value = "t_menu")
@ToString
@Getter
@Setter
@Builder
@EqualsAndHashCode
public class Menu {
	@Id
	private Long id;
	private String name;
	private String path;
	private Long parentId;
	private String icon;
	private Integer sort;
	private Long registUser;
	private Long updateUser;
	private LocalDateTime registDate;
	private LocalDateTime updateDate;
	private Boolean enabled;

	@With
	@Transient
	private List<Role> roles;

	public Menu update(Menu menu) {
		if (menu.getName() != null) this.setName(menu.getName());
		if (menu.getParentId() != null) this.setParentId(menu.getParentId());
		if (menu.getPath() != null) this.setPath(menu.getPath());
		if (menu.getIcon() != null) this.setIcon(menu.getIcon());
		if (menu.getSort() != null) this.setSort(menu.getSort());
		if (menu.getEnabled() != null) this.setEnabled(menu.getEnabled());
		if (menu.getUpdateUser() != null) this.setUpdateUser(menu.getUpdateUser());
		return this;
	}
}


/*
CREATE TABLE `t_menu` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL,
  `path` varchar(100) NOT NULL,
  `parent_id` int(10) DEFAULT NULL,
  `icon` varchar(32) DEFAULT NULL,
  `sort` int(2) DEFAULT 0,
  `regist_user` int(11) NOT NULL,
  `update_user` int(11) DEFAULT NULL,
  `regist_date` datetime DEFAULT current_timestamp(),
  `update_date` datetime DEFAULT current_timestamp() ON UPDATE current_timestamp(),
  `enabled` tinyint(1) NOT NULL DEFAULT 1,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8
*/
