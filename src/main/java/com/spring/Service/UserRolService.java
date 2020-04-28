package com.spring.Service;

import java.util.ArrayList;
import java.util.Collection;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.spring.CustomObject.ChangeRolDto;
import com.spring.CustomObject.MyTeamDto;
import com.spring.CustomObject.UserWithUserRolDto;
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
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error when saving the user rol");
		}
	}

	public boolean isUserOnTeam(User principal, Team team) {
		return this.userRolRepository.existsByUserAndTeam(principal, team);
	}

	public boolean isAdminOnTeam(User principal, Team team) {
		return this.userRolRepository.existsByUserAndAdminAndTeam(principal, true, team);
	}

	public boolean isAdminOnTeam(Integer idTeam) {
		User principal = this.userService.getUserByPrincipal();
		Team team = this.teamService.findOne(idTeam);
		return this.userRolRepository.existsByUserAndAdminAndTeam(principal, true, team);
	}
	
	public void teamOut(Integer idTeam) {
		User principal = this.userService.getUserByPrincipal();
		this.validateTeam(idTeam);
		Team team = this.teamService.findOne(idTeam);
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
		this.validateUser(idUser);
		User user = this.userService.findOne(idUser);
		if (principal.equals(user)) {
			this.teamOut(idTeam);
		} else {
			this.validateTeam(idTeam);
			Team team = this.teamService.findOne(idTeam);
			this.validateUserTeam(user, team);
			this.validatePrincipal(principal);
			this.validatePrincipalTeam(principal, team);
			this.validateIsAdmin(principal, team);
			this.userRolRepository.delete(this.findByUserAndTeam(user, team));
		}
	}
	
	public void changeRol(ChangeRolDto changeRolDto) {
		User principal = this.userService.getUserByPrincipal();
		this.validateTeam(changeRolDto.getIdTeam());
		this.validateUser(changeRolDto.getIdUser());
		User user = this.userService.findOne(changeRolDto.getIdUser());
		Team team = this.teamService.findOne(changeRolDto.getIdTeam());
		this.validateIsTheOnlyAdmin(principal, user , team);
		this.validateUserTeam(user, team);
		Boolean admin = changeRolDto.getAdmin();
		this.changeRol(user, team, admin);
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
	
	public Collection<MyTeamDto> listAllTeamsOfAnUser() {
		User principal = this.userService.getUserByPrincipal();
		this.validatePrincipal(principal);
		Collection<MyTeamDto> res = new ArrayList<>();
		Collection<UserRol> userRoles = this.userRolRepository.findAllUserRolesByUser(principal);
		for (UserRol userRol : userRoles) {
			Team team = userRol.getTeam();
			res.add(new MyTeamDto(team.getId(), team.getName(), userRol.getAdmin()));
		}
		return res;
	}

	public UserRol findByUserAndTeam(User user, Team team) {
		return this.userRolRepository.findByUserAndTeam(user, team).orElse(null);
	}

	private void validateUserTeam(User user, Team team) {
		if (!this.isUserOnTeam(user, team)) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
					"The user on whom you perform the action does not belong to the team");
		}
	}
	
	private void validateIsTheOnlyAdmin(User principal, User user, Team team) {
		if (principal.getId().equals(user.getId()) && this.isTheOnlyAdminOnTeamValidation(principal, team)) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
					"You can't stop being the admin if you don't appoint someone else first");
		}		
	}

	private boolean isTheOnlyAdminOnTeamValidation(User user, Team team) {
		Integer adminsOfTeam = this.userRolRepository.getNumberOfAdminsOfTeam(team);
		return this.isAdminOnTeam(user, team) && adminsOfTeam == 1;
	}

	private void validateUser(Integer idUser) {
		if (idUser == null) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
					"The user id cannot be null");
		}		
	}
	
	private User getAnotherUserNoAdmin(User user, Team team) {
		return this.userRolRepository.getAnotherUserNoAdmin(user, team).get(0);
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

		for (UserRol userRol : userRoles) {

			if (this.isTheOnlyAdminOnTeam(userRol.getUser(), userRol.getTeam())) {
				User newAdmin = this.getAnotherUserNoAdmin(userRol.getUser(), userRol.getTeam());
				UserRol newAdminUserRol = this.findByUserAndTeam(newAdmin, userRol.getTeam());
				newAdminUserRol.setAdmin(true);
				this.userRolRepository.saveAndFlush(newAdminUserRol);
			}
			
			this.userRolRepository.delete(userRol);
			
			if (this.getNumberOfUsersOfTeam(userRol.getTeam()) == 0) {
				this.teamService.deleteVoid(userRol.getTeam().getId());
			}
		}
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
	
	public Collection<User> findUsersByTeam(Team team) {
		return this.userRolRepository.findUsersByTeam(team);
	}
	
	public Collection<Team> findAllByUser(User user) {
		return this.userRolRepository.findByUser(user);
	}
	
	public Collection<UserWithUserRolDto> listMembersOfATeam(Integer idTeam) {
		this.validateTeam(idTeam);
		Team teamDB = this.teamService.findOne(idTeam);
		Collection<UserRol> userRoles = this.userRolRepository.findUserRolesByTeam(teamDB);
		Collection<UserWithUserRolDto> res = new ArrayList<>();
		for (UserRol userRol : userRoles) {
			User user = userRol.getUser();
			res.add(new UserWithUserRolDto(user.getId(), user.getNick(), user.getPhoto(), user.getUserAccount().getUsername(), userRol.getAdmin()));
		}
		return res;
	}

	private void validateTeam(Integer teamId) {
		if (teamId == null) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The id of the equipment cannot be null");
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


}
