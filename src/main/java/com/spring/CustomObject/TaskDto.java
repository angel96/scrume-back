package com.spring.CustomObject;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.spring.Model.Column;
import com.spring.Model.Project;
import com.spring.Model.UserAccount;

import lombok.Data;

@Data
public class TaskDto {

	private String title;

	private String description;

	private Integer points;
	private Project project;

	@JsonIgnore
	private List<UserAccount> users;

	private Column column;

}
