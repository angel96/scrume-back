package com.spring.customobject;

import com.spring.model.Team;
import com.spring.model.User;

import lombok.Data;

@Data
public class UserRolDto {
	private Integer id;

		private Team team;
		private User user;
		private Boolean admin;

}
