package com.spring.repository;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.spring.model.Column;
import com.spring.model.Task;

@Repository
public interface ColumnRepository extends AbstractRepository<Column> {

	@Query("select c from Column c where c.workspace.id = ?1")
	Collection<Column> findColumnsByWorkspace(int id);

	@Query("select t from Task t where t.column.id = ?1")
	Collection<Task> findAllTasksByColumn(int column);
}
