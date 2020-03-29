package com.spring.CustomObject;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserWithNickDto {
	
	private Integer id;
	
    private String nick;
   
}
