package com.spring.CustomObject;

import java.util.Collection;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AllDataDto {

	private String email;
	
	private String name;
	
	private String surnames;
	
    private String nick;

    private String gitUser;

	private String photo;
	
	private Collection<TaskToAllDataDto> tasks;
	
	private Collection<String> teams;
		
}
