package com.spring.customobject;

import java.util.Collection;
import java.util.Map;

import com.spring.model.Column;
import com.spring.model.Task;
import com.spring.model.Workspace;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class WorkspaceDto {
	private Integer id;

	private Workspace workspace;

	private Map<Column, Collection<Task>> columns;
}
