package com.spring.CustomObject;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TaskEditDto {
	
	private int id;

	private String title;

	private String description;

	private int points;
}
