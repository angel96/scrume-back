package com.spring.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.spring.CustomObject.ColumnDto;
import com.spring.CustomObject.ListAllTaskByProjectDto;
import com.spring.CustomObject.ProjectIdNameDto;
import com.spring.CustomObject.TaskDto;
import com.spring.CustomObject.TaskEditDto;
import com.spring.CustomObject.TaskListDto;
import com.spring.CustomObject.UserForWorkspaceDto;
import com.spring.CustomObject.UserProjectWorkspaceFromTaskDto;
import com.spring.CustomObject.WorkspaceSprintListDto;
import com.spring.Model.Estimation;
import com.spring.Model.Project;
import com.spring.Model.Sprint;
import com.spring.Model.Task;
import com.spring.Model.Team;
import com.spring.Model.User;
import com.spring.Model.UserAccount;
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
	private UserService userService;
	@Autowired
	private EstimationService estimationService;

	public Task findOne(int id) {
		return this.taskRepository.findById(id).orElseThrow(
				() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "The requested task doesn´t exists"));
	}

	public TaskDto find(int id) {
		Task task = this.findOne(id);
		checkUserLogged(UserAccountService.getPrincipal());
		checkUserOnTeam(UserAccountService.getPrincipal(), task.getProject().getTeam());
		Set<UserForWorkspaceDto> users = task.getUsers().stream()
				.map(x -> new UserForWorkspaceDto(x.getId(), x.getNick(), x.getPhoto())).collect(Collectors.toSet());
//		Set<Integer> users = task.getUsers().stream().map(User::getId).collect(Collectors.toSet());
		return new TaskDto(task.getTitle(), task.getDescription(), task.getPoints(), task.getProject().getId(), users,
				task.getColumn() == null ? null : task.getColumn().getId());
	}

	public List<Task> findBySprint(Sprint sprint) {
		return this.taskRepository.findBySprint(sprint);
	}

	public List<Task> findCompleteTaskBySprint(Sprint sprint) {
		List<Task> tasks = this.taskRepository.findCompleteTaskBySprint(sprint);
		return tasks;
	}
	
	public List<UserProjectWorkspaceFromTaskDto> findTaskByUser() {
		ModelMapper mapper = new ModelMapper();
		User principal = this.userService.getUserByPrincipal();
		this.checkUserLogged(principal.getUserAccount());
		List<Task> userTask = this.taskRepository.findAllByUser(principal);
		List<UserProjectWorkspaceFromTaskDto> res = new ArrayList<>();
		for (Task task : userTask) {
			WorkspaceSprintListDto workspace = null;
			Integer idSprint = null;
			if (task.getColumn() != null) {
				idSprint = task.getColumn().getWorkspace().getSprint().getId();
				workspace = mapper.map(task.getColumn().getWorkspace(), WorkspaceSprintListDto.class);
			}
			UserProjectWorkspaceFromTaskDto userProjectWorkspaceFromTaskDto = 
			new UserProjectWorkspaceFromTaskDto(task.getId(), task.getTitle(),
			new ProjectIdNameDto(task.getProject().getId(),
			task.getProject().getName()), workspace, idSprint);	
			res.add(userProjectWorkspaceFromTaskDto);
		}
		return res;
	}
	
	public Collection<Task> findByWorkspace(Workspace workspace) {
		return this.taskRepository.findByWorkspace(workspace);
	}
	
	public ListAllTaskByProjectDto getAllTasksByProject(int idProject) {
		User principal = this.userService.getUserByPrincipal();
		Project project = this.projectService.findOne(idProject);
		this.validateProject(project);
		checkUserLogged(UserAccountService.getPrincipal());
		checkUserOnTeam(UserAccountService.getPrincipal(), project.getTeam());
		List<Task> tasks = this.taskRepository.findByProject(project);
		List<TaskListDto> taskListDto = new ArrayList<>();
		for (Task task : tasks) {
			Integer finalPoints = task.getPoints();
			if (finalPoints == null) {
				finalPoints = 0;
			}
			Estimation estimation = this.estimationService.findByTaskAndUser(task, principal);
			Integer estimatedPoints;
			if (estimation == null) {
				estimatedPoints = 0;
			} else {
				estimatedPoints = estimation.getPoints();
			}
			if(task.getColumn() != null) {
				taskListDto.add(new TaskListDto(task.getId(), task.getTitle(), task.getDescription(), finalPoints, estimatedPoints, new ColumnDto(task.getColumn().getId(), null, null)));		
			}else {
				taskListDto.add(new TaskListDto(task.getId(), task.getTitle(), task.getDescription(), finalPoints, estimatedPoints, null));		
			}
			}
		return new ListAllTaskByProjectDto(project.getId(), project.getName(), project.getTeam(),
				project.getDescription(), taskListDto);
	}
	
	public void saveEstimation(Task task, Integer points) {
		task.setPoints(points);
		this.taskRepository.save(task);
	}

	public TaskDto save(TaskDto task, int projectId) {
		Project project = this.projectService.findOne(projectId);
		validateProject(project);
		checkUserLogged(UserAccountService.getPrincipal());
		checkUserOnTeam(UserAccountService.getPrincipal(), project.getTeam());
		ModelMapper mapper = new ModelMapper();
		Task taskEntity = mapper.map(task, Task.class);
		Task taskDB = new Task(taskEntity.getTitle(), taskEntity.getDescription(), 0, project, new HashSet<>(), null);
		taskDB = taskRepository.saveAndFlush(taskDB);
		return new TaskDto(taskDB.getTitle(), taskDB.getDescription(), taskDB.getPoints(), projectId, new HashSet<>(),null);
	}

	public TaskEditDto update(TaskEditDto taskDto, int taskId) {
		Task taskEntity = this.findOne(taskId);
		checkUserLogged(UserAccountService.getPrincipal());
		checkUserOnTeam(UserAccountService.getPrincipal(), taskEntity.getProject().getTeam());
		
		taskEntity.setDescription(taskDto.getDescription());
		taskEntity.setTitle(taskDto.getTitle());
		if(taskDto.getUsers() != null) {
			taskEntity.setUsers(taskDto.getUsers().stream().map(x -> this.userService.findOne(x)).collect(Collectors.toSet()));
		}else {
			taskEntity.setUsers(new HashSet<>());
		}
		Task taskDB = taskRepository.saveAndFlush(taskEntity);

		return new TaskEditDto(taskDB.getId(), taskDB.getTitle(), taskDB.getDescription(), taskDto.getUsers());
	}

	public void delete(int taskId) {
		boolean check = this.taskRepository.existsById(taskId);

		if (!check) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Task not found");
		}

		checkUserLogged(UserAccountService.getPrincipal());
		Task task = this.findOne(taskId);
		Project project = task.getProject();
		checkUserOnTeam(UserAccountService.getPrincipal(), project.getTeam());
		boolean user = this.userRolService.isAdminOnTeam(this.userService.getUserByPrincipal(), project.getTeam());

		if (user) {
			taskRepository.deleteById(taskId);
		} else {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,
					"You are not admin of this team. You are not allowed to remove this task");
		}

	}

	public Collection<Task> findAllTaskByUser(User user) {
		return this.taskRepository.findAllByUser(user);
	}


	private void validateProject(Project project) {
		if (project == null) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "The project is not in the database");
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

	public void getOutAllTasks(User user) {
		Collection<Task> tasks = this.taskRepository.findAllByUser(user);
		for (Task task : tasks) {
			Set<User> users = task.getUsers();
			users.remove(user);
			task.setUsers(users);
			this.taskRepository.saveAndFlush(task);
		}
	}

	public void removeFromWorkspace(Workspace workspace) {
		Collection<Task> tasks = this.taskRepository.findByWorkspace(workspace);
		for (Task task : tasks) {
			task.setColumn(null);
			task.setUsers(new HashSet<>());
			this.taskRepository.saveAndFlush(task);
		}
	}

	
	
	
	
}