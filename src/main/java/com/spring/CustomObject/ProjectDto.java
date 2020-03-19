package com.spring.customobject;

import com.spring.model.Team;

import lombok.Data;

@Data
public class ProjectDto {
	
	private Integer id;
	
	private String name;
	
	private String description;
	
	private Team team;
}
