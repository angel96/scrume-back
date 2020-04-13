package com.spring.Repository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.spring.Model.Column;
import com.spring.Model.HistoryTask;
import com.spring.Model.Sprint;
import com.spring.Model.Task;

@Repository
public interface HistoryTaskRepository extends AbstractRepository<HistoryTask> {

	@Query("select ht from HistoryTask ht where ht.origin.workspace.id = ?1 and ht.destiny.workspace.id = ?1")
	Collection<HistoryTask> findHistoricalByWorkspace(int workspace);

	@Query("select c from Column c where c.workspace.sprint.project.team.id = ?1")
	Collection<Column> findColumnsByTeamId(int team);
	
	@Query("select h.task.points from HistoryTask h where h.destiny.workspace.sprint = ?1 and h.date between ?2 and ?3 and h.destiny.name = 'Done'")
	List<Integer> findBySprintAndDay(Sprint sprint, LocalDateTime startDateTime, LocalDateTime endDateTime);

	@Query("select h from HistoryTask h where h.task = ?1 and h.destiny = ?2")
	HistoryTask findByTaskAndDestiny(Task task, Column origin);
	
	
}
