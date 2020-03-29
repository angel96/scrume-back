package com.spring.CustomObject;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserForWorkspaceDto {
	
	private Integer id;
	
    private String nick;
    
    private String photo;

}
