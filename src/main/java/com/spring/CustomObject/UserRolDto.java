package com.spring.CustomObject;

import com.spring.Model.Team;
import com.spring.Model.User;

import lombok.Data;

@Data
public class UserRolDto {
	
		private Team team;
		private User user;
		private Boolean admin;

}
