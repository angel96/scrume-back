package com.spring.CustomObject;

import java.util.Collection;
import java.util.Map;

import com.spring.Model.Column;
import com.spring.Model.Task;
import com.spring.Model.Workspace;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class WorkspaceDto {
	private Integer id;

	private Workspace workspace;

	private Map<Column, Collection<Task>> columns;
}
