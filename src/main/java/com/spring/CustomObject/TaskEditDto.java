package com.spring.CustomObject;

import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskEditDto {
	
	private int id;

	private String title;

	private String description;
	
	private Set<Integer> users;
	
}
