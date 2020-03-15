package com.spring.CustomObject;

import java.util.List;

import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.spring.Model.Column;
import com.spring.Model.Project;
import com.spring.Model.User;
import com.spring.Model.UserAccount;

public class TaskDto {

	private String title;

	private String description;

	private Integer points;
	private Project project;

	private List<UserAccount> users;

	private Column column;

}
