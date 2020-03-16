package com.spring.Service;

import java.util.Arrays;
import java.util.List;

import javax.transaction.Transactional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spring.CustomObject.TaskDto;
import com.spring.Model.Column;
import com.spring.Model.Project;
import com.spring.Model.Sprint;
import com.spring.Model.Task;
import com.spring.Model.Team;
import com.spring.Model.User;
import com.spring.Model.UserAccount;
import com.spring.Model.UserRol;
import com.spring.Model.Workspace;
import com.spring.Repository.TaskRepository;
import com.spring.Security.UserAccountService;

@Service
@Transactional
public class TaskService extends AbstractService {

	@Autowired
	private TaskRepository taskRepository;
	@Autowired
	private ProjectService projectService;
	@Autowired
	private UserRolService userRolService;
	@Autowired
	private WorkspaceService workspaceService;

	public List<Task> findBySprint(Sprint sprint){
		return this.taskRepository.findBySprint(sprint);
	}
	
	public List<Task> findCompleteTaskBySprint(Sprint sprint){
		return this.taskRepository.findCompleteTaskBySprint(sprint);
	}
	
	public Task findOne(int id) {
		return this.taskRepository.findById(id).orElse(null);
	}

	public List<Task> findAll() {
		return this.taskRepository.findAll();
	}

	public TaskDto save(TaskDto task, int projectId) {
		Project project = this.projectService.findOne(projectId);
		checkUserOnTeam(UserAccountService.getPrincipal(), project.getTeam());
		ModelMapper mapper = new ModelMapper();
		Task taskEntity = mapper.map(task, Task.class);
		Task taskDB = new Task();
		taskDB.setDescription(taskEntity.getDescription());
		taskDB.setPoints(taskEntity.getPoints());
		taskDB.setProject(project);
		taskDB.setTitle(taskEntity.getTitle());
		User user = this.taskRepository.findUserByUserAccount(UserAccountService.getPrincipal().getId());
		taskDB.setUsers(Arrays.asList(user));

		Workspace w = this.taskRepository.findWorkspaceByProject(projectId);

		Column c = this.taskRepository.findColumnToDoByWorkspace(w.getId());

		taskDB.setColumn(c);

		taskRepository.saveAndFlush(taskDB);
		return mapper.map(taskDB, TaskDto.class);
	}

	public TaskDto update(TaskDto task, int taskId) {
		ModelMapper mapper = new ModelMapper();
		Task taskEntity = mapper.map(task, Task.class);
		Task taskDB = findOne(taskId);
		taskDB.setDescription(taskEntity.getDescription());
		taskDB.setPoints(taskEntity.getPoints());
		taskDB.setProject(taskEntity.getProject());
		taskDB.setTitle(taskEntity.getTitle());
		if (checkDistinctUser(taskDB.getUsers())) { // Preguntar a Front como van a hacer esto, porque si es, por
													// ejemplo
			// mostrando una lista con todos los usuarios del equipo, esto
			// no sirve
			User user = this.taskRepository.findUserByUserAccount(UserAccountService.getPrincipal().getId());
			taskDB.setUsers(Arrays.asList(user));
		}

		taskDB.setColumn(task.getColumn());

		taskRepository.saveAndFlush(taskDB);

		return mapper.map(taskDB, TaskDto.class);
	}

	public boolean delete(int taskId) {
		boolean checkIfExists = this.taskRepository.existsById(taskId);
		if (checkIfExists) {
			taskRepository.deleteById(taskId);
		}
		return checkIfExists;
	}

	private boolean checkUserOnTeam(UserAccount user, Team team) {
		UserRol role = this.workspaceService.findUserRoleByUserAccountAndTeam(user.getId(), team.getId());
		return this.userRolService.isUserOnTeam(role.getUser(), team) ? true : false;
	}

	private boolean checkDistinctUser(List<User> account) {
		UserAccount principal = UserAccountService.getPrincipal();
		return account.stream().noneMatch(x -> x.getUserAccount().getUsername().equals(principal.getUsername()));
	}
}
