package com.spring.CustomObject;

import java.util.Collection;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ColumnDto {
	
	private Integer id;

	private String name;
	
	private Collection<TaskForWorkspaceDto> tasks;
}
