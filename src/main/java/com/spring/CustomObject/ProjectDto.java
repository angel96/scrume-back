package com.spring.CustomObject;

import com.spring.Model.Team;

import lombok.Data;

@Data
public class ProjectDto {
	
	private Integer id;
	
	private String name;
	
	private String description;
	
	private Team team;
}
