package com.spring.CustomObject;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MyTeamDto {
	
	private Integer id;

	private String name;
	
	private Boolean isAdmin;

}
