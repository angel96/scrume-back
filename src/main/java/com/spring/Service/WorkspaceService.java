package com.spring.Service;

import java.util.Arrays;
import java.util.Collection;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import com.spring.CustomObject.WorkspaceDto;
import com.spring.Model.Team;
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

	public UserRol findUserRoleByUserAccountAndTeam(int userAccount, int team) {
		return this.repository.findUserRoleByUserAccountAndTeam(userAccount, team);
	}

	public WorkspaceDto findWorkspaceWithColumns(int id) {
		Workspace w = this.findOne(id);
		return new WorkspaceDto(w, serviceColumns.findColumnsTasksByWorkspace(id));
	}

	public Collection<Workspace> findWorkspacesByTeam(int team) {
		return this.repository.findWorkspacesByTeam(team);
	}

	public Workspace findOne(int id) {

		Workspace w = this.repository.findById(id).orElseThrow(
				() -> new HttpClientErrorException(HttpStatus.BAD_REQUEST, "The workspace requested does not exists"));
		checkMembers(w.getSprint().getProject().getTeam().getId());
		return w;
	}

	public Workspace save(Workspace workspace) {

		Workspace saveTo = null;

		if (workspace.getId() != 0) {
			checkAuthorityAdmin(workspace.getId());
			saveTo = this.repository.save(workspace);
		} else {
			saveTo = this.repository.save(workspace);
			this.serviceColumns.saveDefaultColumns(saveTo);
		}

		return saveTo;
	}

	public boolean delete(int workspace) {

		Boolean check = this.repository.existsById(workspace);

		if (check) {
			checkAuthorityAdmin(workspace);
			this.serviceColumns.deleteColumns(workspace);
			this.repository.deleteById(workspace);
		}

		return check;
	}

	public void checkMembers(int team) {
		UserAccount userAccount = UserAccountService.getPrincipal();
		UserRol uRol = this.repository.findUserRoleByUserAccountAndTeam(userAccount.getId(), team);
		// Param 0 -> Object to check
		// Param 1 -> HttpStatus status
		// Param 2 -> String message
		super.assertValues(
				Arrays.asList(new Object[] { userAccount, HttpStatus.FORBIDDEN, "The authentication is not correct" },
						new Object[] { uRol, HttpStatus.FORBIDDEN, "You are not member of this team" }));
	}

	private void checkAuthorityAdmin(int workspace) {
		UserAccount userAccount = UserAccountService.getPrincipal();

		Collection<Team> teams = this.repository.findAllTeamsByUserAccountAdmin(userAccount.getId());
		Workspace w = this.repository.findById(workspace).orElseThrow(
				() -> new HttpClientErrorException(HttpStatus.BAD_REQUEST, "The workspace requested does not exists"));
		if (!teams.contains(w.getSprint().getProject().getTeam())) {
			throw new HttpClientErrorException(HttpStatus.FORBIDDEN, "You do not own to the team of this workspace.");
		}
	}

}
