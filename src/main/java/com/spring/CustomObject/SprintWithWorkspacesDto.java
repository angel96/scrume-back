package com.spring.CustomObject;

import java.util.Collection;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SprintWithWorkspacesDto {
	
	private Integer id;

	private Collection<WorkspaceAndColumnTodoDto> workspaces;
	
	
}
