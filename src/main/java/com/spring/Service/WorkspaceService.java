package com.spring.service;

import java.util.Collection;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.spring.customobject.WorkspaceDto;
import com.spring.customobject.WorkspaceEditDto;
import com.spring.model.Sprint;
import com.spring.model.Team;
import com.spring.model.User;
import com.spring.model.UserAccount;
import com.spring.model.UserRol;
import com.spring.model.Workspace;
import com.spring.repository.WorkspaceRepository;
import com.spring.security.UserAccountService;

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
	private UserService serviceUser;

	public UserRol findUserRoleByUserAccountAndTeam(int userAccount, int team) {
		return this.repository.findUserRoleByUserAccountAndTeam(userAccount, team);
	}

	public WorkspaceDto findWorkspaceWithColumns(int id) {
		Workspace w = this.findOne(id);
		return new WorkspaceDto(id, w, serviceColumns.findColumnsTasksByWorkspace(id));
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
}
