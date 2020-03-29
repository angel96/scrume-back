package com.spring.CustomObject;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserProjectWorkspaceFromTaskDto {

	private int id;
	
	private String title;

	private ProjectIdNameDto project;
	
	private WorkspaceSprintListDto workspace;
	

}
