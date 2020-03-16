package com.spring.Repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.spring.Model.Column;
import com.spring.Model.Task;
import com.spring.Model.User;
import com.spring.Model.Workspace;

@Repository
public interface TaskRepository extends AbstractRepository<Task> {

	@Query("select u from User u where u.userAccount.id = ?1")
	User findUserByUserAccount(int id);

	@Query("select w from Workspace w where w.sprint.project.id = ?1")
	Workspace findWorkspaceByProject(int project);
	
	@Query("select c from Column c where c.workspace.id = ?1 and c.name = 'To do'")
	Column findColumnToDoByWorkspace(int id);
	
	@Query("select c from Column c where c.workspace.id = ?1 and c.name = 'In progress'")
	Column findColumnInprogressByWorkspace(int id);
	
	@Query("select c from Column c where c.workspace.id = ?1 and c.name = 'Done'")
	Column findColumnDoneByWorkspace(int id);
}
