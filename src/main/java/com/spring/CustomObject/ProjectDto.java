package com.spring.CustomObject;

import com.spring.Model.Team;

import lombok.Data;

@Data
public class ProjectDto {
	
	private String name;
	
	private String description;
	
	private Team team;
}
