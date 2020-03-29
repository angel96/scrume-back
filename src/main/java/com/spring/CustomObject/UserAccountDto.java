package com.spring.CustomObject;

import java.time.LocalDate;

import lombok.Data;

@Data
public class UserAccountDto {

	private Integer id;

	private String username;

	private String password;

	private int box;

	private String orderId;

	private String payerId;

	private LocalDate expiredDate;

}
