package com.spring.CustomObject;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserLoginDto {
	
	private Integer idUser;
	
	private String username;
	
	private String password;
	
    private LocalDate endingBoxDate;

	
}
