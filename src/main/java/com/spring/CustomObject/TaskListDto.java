package com.spring.CustomObject;

import java.util.List;

import com.spring.Model.Column;
import com.spring.Model.Project;
import com.spring.Model.User;

import lombok.Data;

@Data
public class TaskListDto {

	private Integer id;
	
	private String title;

	private String description;

	private Integer points;
	
	private Project project;

	private List<User> users;

	private Column column;

}
