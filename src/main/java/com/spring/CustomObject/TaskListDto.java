package com.spring.CustomObject;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskListDto {

	private Integer id;
	
	private String title;

	private String description;

	private Integer finalPoints;
	
	private Integer estimatedPoints;

	private ColumnDto column;

}