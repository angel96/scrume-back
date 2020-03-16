package com.spring.CustomObject;

import java.util.Collection;
import java.util.Map;

import com.spring.Model.Column;
import com.spring.Model.Task;
import com.spring.Model.Workspace;

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
