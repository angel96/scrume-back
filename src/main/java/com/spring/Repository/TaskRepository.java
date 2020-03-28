package com.spring.Repository;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.spring.Model.Column;
import com.spring.Model.Project;
import com.spring.Model.Sprint;
import com.spring.Model.Task;
import com.spring.Model.User;
import com.spring.Model.Workspace;

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
	
	@Query("select t from Task t where t.project = ?1")
	List<Task> findByProject(Project project);

	@Query("select t from Task t join t.column c join c.workspace w where w = ?1")
	Collection<Task> findByWorkspace(Workspace workspace);
	
	@Query("select t from Task t where ?1 member of t.users")
	List<Task> findAllByUser(User user);
}
