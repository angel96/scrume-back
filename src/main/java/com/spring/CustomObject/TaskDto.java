package com.spring.CustomObject;

import java.util.Set;

import com.spring.Model.Column;
import com.spring.Model.Project;
import com.spring.Model.User;

import lombok.Data;

@Data
public class TaskDto {

	private String title;

	private String description;

	private int points;
	private Project project;

	private Set<User> users;

	private Column column;

}
