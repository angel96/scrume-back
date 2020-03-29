package com.spring.Service;

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.List;

import javax.transaction.Transactional;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.spring.CustomObject.ChangeRolDto;
import com.spring.CustomObject.TeamDto;
import com.spring.Model.Team;
import com.spring.Model.User;
import com.spring.Model.UserRol;
import com.spring.Repository.UserRolRepository;

@Service
@Transactional
public class UserRolService extends AbstractService {

	@Autowired
	private UserRolRepository userRolRepository;

	@Autowired
	private UserService userService;

	@Autowired
	private TeamService teamService;

	public UserRol save(UserRol userRol) {
		try {
			return this.userRolRepository.saveAndFlush(userRol);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, "Error when saving the user rol");
		}
	}

	public boolean isUserOnTeam(User principal, Team team) {
		return this.userRolRepository.existsByUserAndTeam(principal, team);
	}

	public boolean isAdminOnTeam(User principal, Team team) {
		return this.userRolRepository.existsByUserAndAdminAndTeam(principal, true, team);
	}

	public void teamOut(Integer idTeam) {
		User principal = this.userService.getUserByPrincipal();
		Team team = this.teamService.findOne(idTeam);
		this.validateTeam(team);
		this.validatePrincipal(principal);
		this.validatePrincipalTeam(principal, team);
		if (this.isTheOnlyAdminOnTeam(principal, team)) {
			User newAdmin = this.getAnotherUserNoAdmin(principal, team);
			this.changeRol(newAdmin, team, true);
		}
		this.userRolRepository.delete(this.findByUserAndTeam(principal, team));
		if (this.getNumberOfUsersOfTeam(team) == 0) {
			this.teamService.deleteVoid(team.getId());
		}
	}

	public void removeFromTeam(Integer idUser, Integer idTeam) {
		User principal = this.userService.getUserByPrincipal();
		User user = this.userService.findOne(idUser);
		if (principal.equals(user)) {
			this.teamOut(idTeam);
		} else {
			Team team = this.teamService.findOne(idTeam);
			this.validateTeam(team);
			this.validateUser(user, team);
			this.validatePrincipal(principal);
			this.validatePrincipalTeam(principal, team);
			this.validateIsAdmin(principal, team);
			this.userRolRepository.delete(this.findByUserAndTeam(principal, team));
		}
	}

	public UserRol findByUserAndTeam(User user, Team team) {
		return this.userRolRepository.findByUserAndTeam(user, team).orElse(null);
	}

	private void validateUser(User user, Team team) {
		if (user == null) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "The user is not in the database");
		}
		if (!this.isUserOnTeam(user, team)) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
					"The user you're throwing out doesn't belong to the team");
		}
	}

	private User getAnotherUserNoAdmin(User user, Team team) {
		return this.userRolRepository.getAnotherUserNoAdmin(user, team).get(0);
	}

	public ChangeRolDto changeRol(Integer idUser, Integer idTeam, ChangeRolDto changeRolDto) {
		ModelMapper modelMapper = new ModelMapper();
		User user = this.userService.findOne(idUser);
		Team team = this.teamService.findOne(idTeam);
		this.validateTeam(team);
		this.validateUser(user, team);
		Boolean admin = changeRolDto.getAdmin();
		UserRol userRolDB = this.changeRol(user, team, admin);
		return modelMapper.map(userRolDB, ChangeRolDto.class);
	}

	private UserRol changeRol(User user, Team team, Boolean admin) {
		User principal = this.userService.getUserByPrincipal();
		this.validatePrincipal(principal);
		this.validatePrincipalTeam(principal, team);
		this.validateIsAdmin(principal, team);
		UserRol userRol = this.findByUserAndTeam(user, team);
		this.validateUserRol(userRol);
		userRol.setAdmin(admin);
		return this.userRolRepository.saveAndFlush(userRol);
	}

	public void delete(UserRol userRol) {
		this.userRolRepository.delete(userRol);
	}
	
	public Collection<Integer> findIdUsersByTeam(Team team) {
		return this.userRolRepository.findIdUsersByTeam(team);
	}

	public List<TeamDto> listAllTeamsOfAnUser() {
		ModelMapper modelMapper = new ModelMapper();
		User principal = this.userService.getUserByPrincipal();
		this.validatePrincipal(principal);
		List<Team> teams = this.userRolRepository.findByUser(principal);
		Type listType = new TypeToken<List<TeamDto>>() {
		}.getType();
		return modelMapper.map(teams, listType);
	}

	private void validateUserRol(UserRol userRol) {
		if (userRol == null) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND,
					"there is no record in the database in which the user related to the team appears");
		}
	}

	private void validateIsAdmin(User user, Team team) {
		if (!this.isAdminOnTeam(user, team)) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,
					"The user who performs the action must be an admin of the team");
		}
	}
	
	public void leaveAllTeams(User principal) {
		Collection<UserRol> userRoles = this.userRolRepository.findAllUserRolesByUser(principal);
		this.userRolRepository.deleteAll(userRoles);
		this.userRolRepository.flush();
	}

	public boolean isTheOnlyAdminOnTeam(User user, Team team) {
		Integer usersOfTeam = this.getNumberOfUsersOfTeam(team);
		Integer adminsOfTeam = this.userRolRepository.getNumberOfAdminsOfTeam(team);
		return this.isAdminOnTeam(user, team) && usersOfTeam > 1 && adminsOfTeam == 1;
	}

	public Integer getNumberOfUsersOfTeam(Team team) {
		return this.userRolRepository.getNumberOfUsersOfTeam(team);
	}

	private void validateTeam(Team team) {
		if (team == null) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "The team is not in the database");
		}
	}

	private void validatePrincipal(User principal) {
		if (principal == null) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "The user must be logged in");
		}
	}

	private void validatePrincipalTeam(User principal, Team team) {
		if (!this.isUserOnTeam(principal, team)) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,
					"The user who performs the action must belong to the team");
		}
	}

	public void flush() {
		userRolRepository.flush();
	}

	public Collection<User> findUsersByTeam(Team team) {
		return this.userRolRepository.findUsersByTeam(team);
	}


}
