package com.spring.CustomObject;

import com.spring.Model.Column;

import lombok.Data;

@Data
public class TaskListDto {

	private Integer id;
	
	private String title;

	private String description;

	private Integer points;

	private Column column;

}