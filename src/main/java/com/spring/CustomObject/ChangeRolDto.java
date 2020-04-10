package com.spring.CustomObject;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChangeRolDto {
		
	private Integer idUser;
	
	private Integer idTeam;
	
	private Boolean admin;

}
