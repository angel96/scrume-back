package com.spring.Service;

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.modelmapper.internal.util.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.spring.CustomObject.ListAllTaskByProjectDto;
import com.spring.CustomObject.TaskDto;
import com.spring.CustomObject.TaskEditDto;
import com.spring.CustomObject.TaskListDto;
import com.spring.Model.Project;
import com.spring.Model.Sprint;
import com.spring.Model.Task;
import com.spring.Model.Team;
import com.spring.Model.User;
import com.spring.Model.UserAccount;
import com.spring.Model.Workspace;
import com.spring.Repository.TaskRepository;
import com.spring.Security.Role;
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
	private UserService userService;

	public Task findOne(int id) {
		return this.taskRepository.findById(id).orElseThrow(
				() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "The requested task doesn´t exists"));
	}

	public TaskDto find(int id) {
		Task task = this.findOne(id);
		checkUserLogged(UserAccountService.getPrincipal());
		checkUserOnTeam(UserAccountService.getPrincipal(), task.getProject().getTeam());
		Set<Integer> users = task.getUsers().stream().map(User::getId).collect(Collectors.toSet());
		return new TaskDto(task.getTitle(), task.getDescription(), task.getPoints(), task.getProject().getId(), users,
				task.getColumn() == null ? null : task.getColumn().getId());
	}

	public List<Task> findAll() {
		return this.taskRepository.findAll();
	}

	public List<Task> findBySprint(Sprint sprint) {
		return this.taskRepository.findBySprint(sprint);
	}

	public List<Task> findCompleteTaskBySprint(Sprint sprint) {
		return this.taskRepository.findCompleteTaskBySprint(sprint);
	}

	public TaskDto save(TaskDto task, int projectId) {
		Project project = this.projectService.findOne(projectId);
		checkUserLogged(UserAccountService.getPrincipal());
		checkUserOnTeam(UserAccountService.getPrincipal(), project.getTeam());
		ModelMapper mapper = new ModelMapper();
		Task taskEntity = mapper.map(task, Task.class);
		Task taskDB = new Task();
		taskDB.setDescription(taskEntity.getDescription());
		taskDB.setPoints(taskEntity.getPoints());
		taskDB.setProject(project);
		taskDB.setTitle(taskEntity.getTitle());
		if (taskEntity.getUsers() == null || taskEntity.getUsers().isEmpty()) {
			Set<User> usuarios = new HashSet<>();
			taskDB.setUsers(usuarios);
		} else {
			Set<User> userAux = taskEntity.getUsers().stream().filter(x -> x.getId() > 0)
					.map(x -> this.userService.findOne(x.getId())).collect(Collectors.toSet());
			userAux.stream().forEach(x -> checkUserOnTeam(x.getUserAccount(), project.getTeam()));
			taskDB.setUsers(userAux);
		}

		taskDB = taskRepository.saveAndFlush(taskDB);
		Set<Integer> users = taskDB.getUsers().stream().map(User::getId).collect(Collectors.toSet());
		return new TaskDto(taskDB.getTitle(), taskDB.getDescription(), taskDB.getPoints(), taskDB.getProject().getId(),
				users, taskDB.getColumn() == null ? null : taskDB.getColumn().getId());
	}

	public TaskEditDto update(TaskEditDto task, int taskId) {
		ModelMapper mapper = new ModelMapper();
		Task taskEntity = mapper.map(task, Task.class);
		Task taskDB = findOne(taskId);
		Project project = this.projectService.findOne(taskDB.getProject().getId());
		checkUserLogged(UserAccountService.getPrincipal());
		checkUserOnTeam(UserAccountService.getPrincipal(), project.getTeam());

		if (taskEntity.getUsers() == null || taskEntity.getUsers().isEmpty()) {
			Set<User> usuarios = new HashSet<>();
			taskDB.setUsers(usuarios);
		} else {
			Set<User> usuarios = taskEntity.getUsers();
			Set<User> userAux = usuarios.stream().filter(x -> x.getId() > 0)
					.map(x -> this.userService.findOne(x.getId())).collect(Collectors.toSet());
			usuarios.stream().forEach(x -> checkUserOnTeam(x.getUserAccount(), project.getTeam()));
			taskDB.getUsers().retainAll(userAux);
		}
		taskDB.setDescription(taskEntity.getDescription());
		taskDB.setPoints(taskEntity.getPoints());
		taskDB.setProject(project);
		taskDB.setTitle(taskEntity.getTitle());

		/*
		 * if (taskEntity.getColumn() != null) { Workspace w =
		 * this.taskRepository.findWorkspaceByProject(project.getId());
		 * 
		 * Column c = this.taskRepository.findColumnToDoByWorkspace(w.getId());
		 * taskDB.setColumn(c); }
		 */
		taskDB = taskRepository.saveAndFlush(taskDB);

		return new TaskEditDto(taskDB.getId(), taskDB.getTitle(), taskDB.getDescription(), taskDB.getPoints());
	}

	public void delete(int taskId) {
		try {
//		if (this.taskRepository.existsById(taskId)) {
			Task task = this.findOne(taskId);
			checkUserLogged(UserAccountService.getPrincipal());
			Project project = task.getProject();
			checkUserOnTeam(UserAccountService.getPrincipal(), project.getTeam());
			Assert.isTrue(UserAccountService.getPrincipal().getRoles().contains(Role.ROLE_ADMIN));
			taskRepository.deleteById(taskId);
		} catch(Throwable t) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "The Task with id=" + taskId + " doesn´t exist");
		}
	}

	public ListAllTaskByProjectDto getAllTasksByProject(int idProject) {
		ModelMapper modelMapper = new ModelMapper();
		User principal = this.userService.getUserByPrincipal();
		Project project = this.projectService.findOne(idProject);
		this.validateProject(project);
		this.validateUserToList(principal, project);
		List<Task> tasks = this.taskRepository.findByProject(project);
		Type listType = new TypeToken<List<TaskListDto>>() {
		}.getType();
		List<TaskListDto> taskListDto = modelMapper.map(tasks, listType);
		ListAllTaskByProjectDto res = new ListAllTaskByProjectDto();
		res.setId(project.getId());
		res.setName(project.getName());
		res.setDescription(project.getDescription());
		res.setTasks(taskListDto);
		return res;
	}

	private void validateProject(Project project) {
		if (project == null) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "The project is not in the database");
		}
	}

	private void validateUserToList(User principal, Project project) {
		if (principal == null) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "The user must be logged in");
		}

		if (!this.userRolService.isUserOnTeam(principal, project.getTeam())) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,
					"The user must belong to the team of the project");
		}
	}

	private void checkUserOnTeam(UserAccount user, Team team) {
		User usuario = this.userService.getUserByPrincipal();
		if (!this.userRolService.isUserOnTeam(usuario, team))
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
					"The user " + user.getUsername() + " does not belong to the team: " + team.getName());
	}

	private void checkUserLogged(UserAccount user) {
		if (user == null)
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "You must be logged");
	}

	public void flush() {
		taskRepository.flush();
	}

	public Collection<Task> findByWorkspace(Workspace workspace) {
		return this.taskRepository.findByWorkspace(workspace);
	}
}