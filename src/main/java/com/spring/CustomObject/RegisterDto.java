package com.spring.CustomObject;

import java.time.LocalDateTime;
import java.util.Date;

import lombok.Data;

@Data
public class RegisterDto {
	private Integer id;
	
	private String username;
	
	private String password;
	
	private String name;
	
	private String surnames;
	
    private String nick;

    private String gitUser;

	private String photo;
    
    private LocalDateTime createdAt;
    
    private LocalDateTime lastPasswordChangeAt = LocalDateTime.now();
    
    
}
