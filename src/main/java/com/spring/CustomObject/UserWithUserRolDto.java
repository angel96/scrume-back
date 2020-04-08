package com.spring.CustomObject;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserWithUserRolDto {

	private Integer id;
	
	private String nickname;
		
    private String email;
    
    private Boolean isAdmin;

}
