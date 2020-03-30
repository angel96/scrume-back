package com.spring.Service;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spring.Model.Column;
import com.spring.Model.Project;
import com.spring.Model.Task;
import com.spring.Model.Workspace;
import com.spring.Repository.ColumnRepository;

@Service
@Transactional
public class ColumnService extends AbstractService {

	@Autowired
	private ColumnRepository repository;

	@Autowired
	private WorkspaceService serviceWorkspace;

	public Column findOne(int id) {
		return this.repository.findById(id).orElse(null);
	}

	public Collection<Column> saveDefaultColumns(Workspace workspace) {

		Column toDo = new Column("To do", workspace);
		Column inProgress = new Column("In progress", workspace);
		Column done = new Column("Done", workspace);

		return repository.saveAll(Arrays.asList(toDo, inProgress, done));
	}

	public Map<Column, Collection<Task>> findColumnsTasksByWorkspace(int workspace) {
		return this.repository.findColumnsByWorkspace(workspace).stream()
				.collect(Collectors.toMap(x -> x, x -> repository.findAllTasksByColumn(x.getId())));
	}

	public void deleteColumns(int workspace) {
		this.serviceWorkspace.findOne(workspace);
		Collection<Column> columns = this.repository.findColumnsByWorkspace(workspace);

		if (!columns.isEmpty()) {
			this.repository.deleteAll(columns);
		}
	}
	
	public void flush() {
		repository.flush();
	}

	public Column findColumnTodoByWorkspace(Workspace workspace) {
		return this.repository.findColumnToDoByWorkspace(workspace);
	}
	
	public Column findColumnInprogressByWorkspace(Workspace workspace) {
		return this.repository.findColumnInprogressByWorkspace(workspace);
	}
	
	public Column findColumnDoneByWorkspace(Workspace workspace) {
		return this.repository.findColumnDoneByWorkspace(workspace);
	}
	
	public Collection<Column> findColumnTodoByProject(Project project) {
		return this.repository.findColumnToDoByProject(project);
	}

}
