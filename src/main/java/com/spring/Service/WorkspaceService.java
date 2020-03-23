package com.spring.Service;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.spring.CustomObject.ColumnDto;
import com.spring.CustomObject.SprintWithWorkspacesDto;
import com.spring.CustomObject.TaskForWorkspaceDto;
import com.spring.CustomObject.UserForWorkspaceDto;
import com.spring.CustomObject.WorkspaceAndColumnTodoDto;
import com.spring.CustomObject.WorkspaceEditDto;
import com.spring.CustomObject.WorkspaceSprintListDto;
import com.spring.CustomObject.WorkspaceWithColumnsDto;
import com.spring.Model.Column;
import com.spring.Model.HistoryTask;
import com.spring.Model.Project;
import com.spring.Model.Sprint;
import com.spring.Model.Task;
import com.spring.Model.Team;
import com.spring.Model.User;
import com.spring.Model.Workspace;
import com.spring.Repository.WorkspaceRepository;

@Service
@Transactional
public class WorkspaceService extends AbstractService {

	@Autowired
	private WorkspaceRepository repository;

	@Autowired
	private ColumnService serviceColumns;

	@Autowired
	private SprintService serviceSprint;

	@Autowired
	private UserRolService serviceUserRol;

	@Autowired
	private TeamService serviceTeam;

	@Autowired
	private TaskService taskService;

	@Autowired
	private UserService serviceUser;

	@Autowired
	private ProjectService projectService;

	public boolean checksIfExists(int workspace) {
		return this.repository.existsById(workspace);
	}

	public WorkspaceWithColumnsDto findWorkspaceWithColumns(int id) {
		ModelMapper modelMapper = new ModelMapper();
		Workspace workspace = this.findOne(id);
		Collection<Task> tasks = this.taskService.findByWorkspace(workspace);

		Collection<TaskForWorkspaceDto> tasksDtoTodo = new ArrayList<>();
		Collection<TaskForWorkspaceDto> tasksDtoInProgress = new ArrayList<>();
		Collection<TaskForWorkspaceDto> tasksDtoDone = new ArrayList<>();

		for (Task task : tasks) {
			Collection<User> users = task.getUsers();
			Type collectionUsersDto = new TypeToken<Collection<UserForWorkspaceDto>>() {
			}.getType();
			Collection<UserForWorkspaceDto> usersDto = modelMapper.map(users, collectionUsersDto);
			TaskForWorkspaceDto taskDto = new TaskForWorkspaceDto(task.getId(), task.getTitle(), task.getDescription(),
					task.getPoints(), usersDto);
			if (task.getColumn().getName().compareTo("To Do") == 0) {
				tasksDtoTodo.add(taskDto);
			} else if (task.getColumn().getName().compareTo("In progress") == 0) {
				tasksDtoInProgress.add(taskDto);
			} else {
				tasksDtoDone.add(taskDto);
			}
		}
		Column columnTodo = this.serviceColumns.findColumnTodoByWorkspace(workspace);
		ColumnDto columnTodoDto = new ColumnDto(columnTodo.getId(), columnTodo.getName(), tasksDtoTodo);

		Column columnInProgress = this.serviceColumns.findColumnInprogressByWorkspace(workspace);
		ColumnDto columnInProgressDto = new ColumnDto(columnInProgress.getId(), columnInProgress.getName(),
				tasksDtoInProgress);

		Column columnDone = this.serviceColumns.findColumnDoneByWorkspace(workspace);
		ColumnDto columnDoneDto = new ColumnDto(columnDone.getId(), columnDone.getName(), tasksDtoDone);

		Collection<ColumnDto> columnsDto = new ArrayList<>();

		columnsDto.add(columnTodoDto);
		columnsDto.add(columnInProgressDto);
		columnsDto.add(columnDoneDto);
		return new WorkspaceWithColumnsDto(workspace.getId(), workspace.getName(), columnsDto);
	}

	public Collection<Workspace> findWorkspacesByTeam(int team) {
		checkMembers(team);
		return this.repository.findWorkspacesByTeam(team);
	}

	public Workspace findOne(int id) {

		Workspace w = this.repository.findById(id).orElseThrow(
				() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "The workspace requested does not exists"));
		checkMembers(w.getSprint().getProject().getTeam().getId());
		return w;
	}

	public void saveDefaultWorkspace(Sprint sprint) {
		Workspace workspace = new Workspace();
		workspace.setName("Default");
		workspace.setSprint(sprint);
		Workspace saveTo = this.repository.saveAndFlush(workspace);
		this.serviceColumns.saveDefaultColumns(saveTo);
	}

	public Collection<SprintWithWorkspacesDto> listTodoColumnsOfAProject(Integer idProject) {
		Project project = this.projectService.findOne(idProject);
		checkMembers(project.getTeam().getId());
		Collection<Column> columns = this.serviceColumns.findColumnTodoByProject(project);
		Collection<SprintWithWorkspacesDto> res = new ArrayList<>();
		Map<Integer, Collection<WorkspaceAndColumnTodoDto>> sprints = new HashMap<>();
		for (Column column : columns) {
			Workspace workspace = column.getWorkspace();
			WorkspaceAndColumnTodoDto workspaceAndColumnTodoDto = new WorkspaceAndColumnTodoDto(workspace.getId(),
					workspace.getName(), column.getId());
			if (sprints.containsKey(workspace.getSprint().getId())) {
				Collection<WorkspaceAndColumnTodoDto> aux = sprints.get(workspace.getSprint().getId());
				aux.add(workspaceAndColumnTodoDto);
				sprints.put(workspace.getSprint().getId(), aux);
			} else {
				Collection<WorkspaceAndColumnTodoDto> aux = new ArrayList<>();
				aux.add(workspaceAndColumnTodoDto);
				sprints.put(workspace.getSprint().getId(), aux);
			}
		}
		for (Entry<Integer, Collection<WorkspaceAndColumnTodoDto>> entry : sprints.entrySet()) {
			res.add(new SprintWithWorkspacesDto(entry.getKey(), entry.getValue()));
		}
		return res;
	}

	public Workspace save(int idWorkspace, WorkspaceEditDto workspaceDto) {

		Workspace saveTo = null;

		Workspace workspace = null;

		if (idWorkspace != 0) {
			checkAuthorityAdmin(idWorkspace);
			workspace = this.findOne(idWorkspace);
			workspace.setName(workspaceDto.getName());
			if (workspaceDto.getSprint() != 0) {
				workspace.setSprint(this.serviceSprint.getOne(workspaceDto.getSprint()));
			}
			saveTo = this.repository.saveAndFlush(workspace);
		} else {
			workspace = new Workspace(workspaceDto.getName(), this.serviceSprint.getOne(workspaceDto.getSprint()));
			saveTo = this.repository.saveAndFlush(workspace);
			this.serviceColumns.saveDefaultColumns(saveTo);
		}

		return saveTo;
	}

	public void delete(int workspace) {

		// 1. Existe
		boolean check = this.checksIfExists(workspace);

		if (check) {
			// 2. Es administrador y pertenece
			this.findOne(workspace);
			checkAuthorityAdmin(workspace);
			this.repository.deleteById(workspace);
		}
	}

	public void checkMembers(int teamId) {
		User user = this.serviceUser.getUserByPrincipal();

		Team team = this.serviceTeam.findOne(teamId);
		if (user == null) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "The user must be logged in");
		}
		if (!this.serviceUserRol.isUserOnTeam(user, team)) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "The user must belong to the team");
		}

	}

	private void checkAuthorityAdmin(int workspace) {

		User u = this.serviceUser.getUserByPrincipal();

		boolean uRol = this.serviceUserRol.isAdminOnTeam(u, this.findOne(workspace).getSprint().getProject().getTeam());

		if (!uRol) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,
					"The user must be an administrator for this action");
		}

	}

	public List<WorkspaceSprintListDto> findWorkspacesBySprint(int sprint) {
		Team team = this.serviceSprint.getOne(sprint).getProject().getTeam();
		checkMembers(team.getId());
		return this.repository.findWorkspacesBySprint(sprint).stream()
				.map(x -> new WorkspaceSprintListDto(x.getId(), x.getName())).collect(Collectors.toList());
	}

	public WorkspaceSprintListDto findWorkspaceLastModifiedByProject(int project) {

		Project proj = this.projectService.findOne(project);

		checkMembers(proj.getTeam().getId());

		Collection<HistoryTask> historyTasksByProject = this.repository.findAllHistoryTasksByProject(project);

		WorkspaceSprintListDto result = null;

		if (historyTasksByProject.isEmpty() || historyTasksByProject == null) {
			result = new WorkspaceSprintListDto(0, "");
		} else {
			List<HistoryTask> historyTasks = new ArrayList<>(historyTasksByProject);
			Workspace ht = historyTasks.get(0).getDestiny().getWorkspace();
			result = new WorkspaceSprintListDto(ht.getId(), ht.getName());
		}

		return result;

	}

	public void flush() {
		repository.flush();
	}
}
