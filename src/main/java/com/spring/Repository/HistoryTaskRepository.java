package com.spring.Repository;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.spring.Model.Column;
import com.spring.Model.HistoryTask;

@Repository
public interface HistoryTaskRepository extends AbstractRepository<HistoryTask> {

	@Query("select ht from HistoryTask ht where ht.origin.workspace.id = ?1 and ht.destiny.workspace.id = ?1")
	Collection<HistoryTask> findHistoricalByWorkspace(int workspace);

	@Query("select c from Column c where c.workspace.sprint.project.team.id = ?1")
	Collection<Column> findColumnsByTeamId(int team);
}
