package com.spring.CustomObject;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserOfATeamByWorspaceDto {
	
	private Integer id;
	
    private String nick;
    
    private String photo;

}
