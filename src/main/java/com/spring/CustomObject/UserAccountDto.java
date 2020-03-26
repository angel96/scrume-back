package com.spring.CustomObject;

import java.time.LocalDateTime;
import java.util.Set;

import com.spring.Security.Role;

import lombok.Data;

@Data
public class UserAccountDto {
	
	private String username;
	
	private String password;
	
	private LocalDateTime createdAt;
	
	private LocalDateTime lastPasswordChangeAt = LocalDateTime.now();
	
	private Set<Role> roles;
}
