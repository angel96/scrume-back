package com.spring.CustomObject;

import java.util.Set;

import com.spring.Security.Role;

import lombok.Data;

@Data
public class UserAccountDto {
	
	private String id;
	
	private String username;
	
	private String password;
	
	private Set<Role> roles;
}
