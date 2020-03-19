package com.spring.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.spring.model.Column;
import com.spring.model.Project;
import com.spring.model.Sprint;
import com.spring.model.Task;
import com.spring.model.User;
import com.spring.model.Workspace;

@Repository
public interface TaskRepository extends AbstractRepository<Task> {

	@Query("select u from User u where u.userAccount.id = ?1")
	User findUserByUserAccount(int id);

	//@Query("select w from Workspace w where w.sprint.project.id = ?1")
	@Query("select w from Workspace w join w.sprint s join s.project p where p.id = ?1")
	Workspace findWorkspaceByProject(int project);
	
	@Query("select c from Column c where c.workspace.id = ?1 and c.name = 'To do'")
	Column findColumnToDoByWorkspace(int id);
	
	@Query("select c from Column c where c.workspace.id = ?1 and c.name = 'In progress'")
	Column findColumnInprogressByWorkspace(int id);
	
	@Query("select c from Column c where c.workspace.id = ?1 and c.name = 'Done'")
	Column findColumnDoneByWorkspace(int id);
	
	@Query("select t from Task t join t.column c join c.workspace w join w.sprint s where s = ?1")
	List<Task> findBySprint(Sprint sprint);
	
	@Query("select t from Task t join t.column c join c.workspace w join w.sprint s where s = ?1 and c.name = 'Done'")
	List<Task> findCompleteTaskBySprint(Sprint sprint);
	
	List<Task> findByProject(Project project);
}
