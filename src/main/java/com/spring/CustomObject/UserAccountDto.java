package com.spring.CustomObject;

import java.time.LocalDate;
import java.util.Set;

import com.spring.Security.Role;

import lombok.Data;

@Data
public class UserAccountDto {

	private Integer id;

	private String username;

	private String password;

	private Set<Role> roles;

	private Boolean confirmation;

	private int box;

	private String orderId;

	private String payerId;

	private LocalDate expiredDate;

}
