package com.spring.customobject;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WorkspaceEditDto {

	private int id;
	
	private String name;

	private int sprint;
}
