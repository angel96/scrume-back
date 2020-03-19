package com.spring.customobject;

import java.util.Set;

import com.spring.model.Column;
import com.spring.model.Project;
import com.spring.model.User;

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
