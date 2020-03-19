package com.spring.customobject;

import com.spring.model.Column;

import lombok.Data;

@Data
public class TaskListDto {

	private Integer id;
	
	private String title;

	private String description;

	private Integer points;

	private Column column;

}