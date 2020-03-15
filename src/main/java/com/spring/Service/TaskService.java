package com.spring.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.transaction.Transactional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spring.CustomObject.TaskDto;
import com.spring.Model.Project;
import com.spring.Model.Task;
import com.spring.Model.Team;
import com.spring.Model.UserAccount;
import com.spring.Model.UserRol;
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
	private UserAccountService UAService;
	@Autowired
	private WorkspaceService workspaceService;

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
		taskDB.setUsers(Arrays.asList(UserAccountService.getPrincipal()));
//		taskDB.setColumn(new Column();
		taskRepository.save(taskDB);
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
		if (checkDistinctUser(taskDB.getUsers())) { //Preguntar a Front como van a hacer esto, porque si es, por ejemplo
			List<UserAccount> temp = new ArrayList<>(); //mostrando una lista con todos los usuarios del equipo, esto no sirve
			taskEntity.getUsers().add(UserAccountService.getPrincipal());
			taskDB.setUsers(temp);
		}
//		taskDB.setColumn();
		taskRepository.save(taskDB);
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

	private boolean checkDistinctUser(List<UserAccount> account) {
		UserAccount principal = UserAccountService.getPrincipal();
		return account.stream().noneMatch(x -> x.getUsername().equals(principal.getUsername()));

	}
}
