package com.spring.CustomObject;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WorkspaceAndColumnTodoDto {
	
	private Integer id;

	private String name;
	
	private Integer column;
}
