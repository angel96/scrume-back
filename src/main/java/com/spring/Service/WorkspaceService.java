package com.spring.Service;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;

import javax.transaction.Transactional;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.spring.CustomObject.ColumnDto;
import com.spring.CustomObject.TaskForWorkspaceDto;
import com.spring.CustomObject.UserForWorkspaceDto;
import com.spring.CustomObject.WorkspaceEditDto;
import com.spring.CustomObject.WorkspaceWithColumnsDto;
import com.spring.Model.Column;
import com.spring.Model.Sprint;
import com.spring.Model.Task;
import com.spring.Model.Team;
import com.spring.Model.User;
import com.spring.Model.UserAccount;
import com.spring.Model.UserRol;
import com.spring.Model.Workspace;
import com.spring.Repository.WorkspaceRepository;
import com.spring.Security.UserAccountService;

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

	public UserRol findUserRoleByUserAccountAndTeam(int userAccount, int team) {
		return this.repository.findUserRoleByUserAccountAndTeam(userAccount, team);
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
			Type collectionUsersDto = new TypeToken<Collection<UserForWorkspaceDto>>() {}.getType();
			Collection<UserForWorkspaceDto> usersDto = modelMapper.map(users, collectionUsersDto);
			TaskForWorkspaceDto taskDto = new TaskForWorkspaceDto(task.getId(), task.getTitle(), task.getDescription(), task.getPoints(), usersDto);
			if(task.getColumn().getName() == "To do") {
				tasksDtoTodo.add(taskDto);
			}
			else if(task.getColumn().getName() == "In progress") {
				tasksDtoInProgress.add(taskDto);
			}
			else {
				tasksDtoDone.add(taskDto);
			}
		}
		
		Column columnTodo = this.serviceColumns.findColumnTodoByWorkspace(workspace);
		ColumnDto columnTodoDto = new ColumnDto(columnTodo.getId(), columnTodo.getName(), tasksDtoTodo);
		
		Column columnInProgress = this.serviceColumns.findColumnInprogressByWorkspace(workspace);
		ColumnDto columnInProgressDto = new ColumnDto(columnInProgress.getId(), columnInProgress.getName(), tasksDtoInProgress);

		Column columnDone = this.serviceColumns.findColumnDoneByWorkspace(workspace);
		ColumnDto columnDoneDto = new ColumnDto(columnDone.getId(), columnDone.getName(), tasksDtoDone);

		Collection<ColumnDto> columnsDto = new ArrayList<>();
		
		columnsDto.add(columnTodoDto);
		columnsDto.add(columnInProgressDto);
		columnsDto.add(columnDoneDto);
		return new WorkspaceWithColumnsDto(workspace.getId(), workspace.getName(), columnsDto);
	}

	public Collection<Workspace> findWorkspacesByTeam(int team) {
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

	public Workspace save(int idWorkspace, WorkspaceEditDto workspaceDto) {

		Workspace saveTo = null;

		Workspace workspace = null;

		if (idWorkspace != 0) {
			checkAuthorityAdmin(idWorkspace);
			workspace = this.repository.getOne(idWorkspace);
			workspace.setName(workspaceDto.getName());
			if (workspaceDto.getSprint() != 0) {
				workspace.setSprint(this.serviceSprint.getOne(workspaceDto.getSprint()));
			}
			saveTo = this.repository.save(workspace);
		} else {
			workspace = new Workspace(workspaceDto.getName(), this.serviceSprint.getOne(workspaceDto.getSprint()));
			saveTo = this.repository.saveAndFlush(workspace);
			this.serviceColumns.saveDefaultColumns(saveTo);
		}

		return saveTo;
	}

	public void delete(int workspace) {

		boolean check = this.repository.existsById(workspace);
		if (check) {
//			checkAuthorityAdmin(workspace);
//			this.serviceColumns.deleteColumns(workspace);
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
		UserAccount userAccount = UserAccountService.getPrincipal();

		Collection<Team> teams = this.repository.findAllTeamsByUserAccountAdmin(userAccount.getId());
		Workspace w = this.repository.findById(workspace).orElseThrow(
				() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "The workspace requested does not exists"));
		if (!teams.contains(w.getSprint().getProject().getTeam())) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You do not own to the team of this workspace.");
		}
	}
	public void flush() {
		repository.flush();
	}

	public Collection<Workspace> findWorkspacesBySprint(int sprint) {
		return this.repository.findWorkspacesBySprint(sprint);
	}
}
