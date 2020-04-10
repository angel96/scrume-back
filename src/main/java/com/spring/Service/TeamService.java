package com.spring.Service;

import javax.transaction.Transactional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.spring.CustomObject.MyTeamDto;
import com.spring.CustomObject.TeamDto;
import com.spring.Model.Team;
import com.spring.Model.User;
import com.spring.Model.UserRol;
import com.spring.Repository.TeamRepository;

@Service
@Transactional
public class TeamService extends AbstractService {

	@Autowired
	private TeamRepository teamRepository;

	@Autowired
	private UserService userService;

	@Autowired
	private UserRolService userRolService;

	public MyTeamDto getTeam(Integer idTeam) {
		User principal = this.userService.getUserByPrincipal();
		this.validateUserPrincipal(principal);
		Team teamDB = this.findOne(idTeam);
		this.validateTeam(teamDB);
		Boolean isAdmin = this.userRolService.isAdminOnTeam(principal, teamDB);
		return new MyTeamDto(teamDB.getId(), teamDB.getName(), isAdmin);
	}
	
	public TeamDto save(TeamDto teamDto) {
		ModelMapper modelMapper = new ModelMapper();
		User principal = this.userService.getUserByPrincipal();
		this.validateUserPrincipal(principal);
		Team teamEntity = modelMapper.map(teamDto, Team.class);
		Team teamDB = this.teamRepository.save(teamEntity);
		UserRol userRol = new UserRol();
		userRol.setAdmin(true);
		userRol.setTeam(teamDB);
		userRol.setUser(principal);
		this.userRolService.save(userRol);
		return modelMapper.map(teamDB, TeamDto.class);
	}

	public TeamDto update(TeamDto teamEditDto) {
		ModelMapper modelMapper = new ModelMapper();
		User principal = this.userService.getUserByPrincipal();
		Team teamEntity = this.teamRepository.findById(teamEditDto.getId()).orElse(null);
		this.validateTeam(teamEntity);
		this.validateEditPermission(principal, teamEntity);
		this.validateUserPrincipal(principal);
		teamEntity.setName(teamEditDto.getName());
		Team teamDB = this.teamRepository.saveAndFlush(teamEntity);
		return modelMapper.map(teamDB, TeamDto.class);
	}

	public void delete(Integer idTeam) {
		User principal = this.userService.getUserByPrincipal();
		Team team = this.teamRepository.findById(idTeam).orElse(null);
		this.validateUserPrincipal(principal);
		this.validateTeam(team);
		this.validateEditPermission(principal, team);
		this.validateDeletePermission(team);
		UserRol userRol = this.userRolService.findByUserAndTeam(principal, team);
		this.userRolService.delete(userRol);
		this.teamRepository.delete(team);
	}

	public void deleteVoid(Integer idTeam) {
		this.teamRepository.deleteById(idTeam);
	}

	public Team findOne(int teamId) {
		return this.teamRepository.findById(teamId).orElseThrow(
				() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "The requested team not exists"));
	}

	private void validateEditPermission(User principal, Team team) {
		if (!this.userRolService.isUserOnTeam(principal, team)) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "The user must belong to the team");
		}
		if (!this.userRolService.isAdminOnTeam(principal, team)) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "The user must be an admin of the team");
		}
	}

	private void validateTeam(Team team) {
		if (team == null) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "the team is not in the database");
		}
	}

	private void validateUserPrincipal(User principal) {
		if (principal == null) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "The user must be logged in");
		}
	}

	private void validateDeletePermission(Team team) {
		if (this.userRolService.getNumberOfUsersOfTeam(team) != 1) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,
					"To delete a team, the only member must be the administrator");
		}
	}

	public void flush() {
		teamRepository.flush();
	}

}
