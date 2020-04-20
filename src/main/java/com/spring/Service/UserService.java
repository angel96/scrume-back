package com.spring.Service;

import java.lang.reflect.Type;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.spring.CustomObject.AllDataDto;
import com.spring.CustomObject.FindByNickDto;
import com.spring.CustomObject.TaskToAllDataDto;
import com.spring.CustomObject.UserDto;
import com.spring.CustomObject.UserOfATeamByWorspaceDto;
import com.spring.CustomObject.UserUpdateDto;
import com.spring.CustomObject.UserWithNickDto;
import com.spring.Model.Task;
import com.spring.Model.Team;
import com.spring.Model.User;
import com.spring.Model.UserAccount;
import com.spring.Model.Workspace;
import com.spring.Repository.UserRepository;
import com.spring.Security.Role;
import com.spring.Security.UserAccountService;
import com.spring.Utiles.Utiles;

@Service
@Transactional
public class UserService extends AbstractService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private UserAccountService userAccountService;

	@Autowired
	private UserRolService userRolService;
	
	@Autowired
	private InvitationService invitationService;


	@Autowired
	private TeamService teamService;

	@Autowired
	private TaskService taskService;

	@Autowired
	private WorkspaceService workspaceService;

	public Collection<UserOfATeamByWorspaceDto> listUsersOfATeamByWorkspace(Integer idWorkspace) {
		ModelMapper mapper = new ModelMapper();
		User principal = this.getUserByPrincipal();
		Workspace workspace = this.workspaceService.findOne(idWorkspace);
		this.validateWorkspace(workspace);
		Team team = workspace.getSprint().getProject().getTeam();
		this.validateUserPrincipalTeam(principal, team);
		Collection<User> users = this.userRolService.findUsersByTeam(team);

		Type listType = new TypeToken<Collection<UserOfATeamByWorspaceDto>>() {
		}.getType();
		return mapper.map(users, listType);
	}

	public User getUserByPrincipal() {
		UserAccount userAccount = UserAccountService.getPrincipal();
		return this.userRepository.findByUserAccount(userAccount.getUsername()).orElse(null);
	}

	public User findOne(int userId) {
		return this.userRepository.findById(userId).orElseThrow(
				() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "The requested user doesn´t exists"));
	}

	public UserDto get(Integer idUser) {
		UserDto userDto = new UserDto();
		User userDB = this.findOne(idUser);
		validatePermission(userDB);
		validateUser(userDB);
		userDto.setId(userDB.getId());
		userDto.setName(userDB.getName());
		userDto.setGitUser(userDB.getGitUser());
		userDto.setNick(userDB.getNick());
		userDto.setPhoto(userDB.getPhoto());
		userDto.setSurnames(userDB.getSurnames());
		userDto.setIdUserAccount(userDB.getUserAccount().getId());
		return userDto;
	}

	public UserDto save(UserDto userDto) {
		ModelMapper mapper = new ModelMapper();
		User userEntity = mapper.map(userDto, User.class);
		User userDB = new User();
		userDB.setGitUser(userEntity.getGitUser());
		userDB.setName(userEntity.getName());
		userDB.setNick(userEntity.getNick());
		userDB.setPhoto(userEntity.getPhoto());
		userDB.setSurnames(userEntity.getSurnames());
		UserAccount userAccountDB = this.userAccountService.findOne(userDto.getIdUserAccount());
		userDB.setUserAccount(userAccountDB);
		validateUser(userDB);
		this.userRepository.saveAndFlush(userDB);
		return userDto;
	}

	public UserDto update(UserUpdateDto userDto, Integer idUser) {
		User userDB = this.findOne(idUser);
		validatePermission(userDB);
		UserAccount userAccountDB = this.userAccountService.findOne(userDB.getUserAccount().getId());
		userDB.setGitUser(userDto.getGitUser());
		userDB.setName(userDto.getName());
		if(userDto.getNick() != null && !userDB.getNick().equals(userDto.getNick())) {
			this.validateNick(userDto.getNick());
		}
		userDB.setNick(userDto.getNick());
		userDB.setPhoto(userDto.getPhoto());
		userDB.setSurnames(userDto.getSurnames());
		if (userDto.getPreviousPassword() != null && !userDto.getPreviousPassword().equals("")) {
			this.validatePassword(userAccountDB, userDto.getPreviousPassword(), userDto.getNewPassword());
			String password = Utiles.encryptedPassword(userDto.getNewPassword());
			userAccountDB.setPassword(password);
			userAccountDB.setCreatedAt(LocalDateTime.now());
			userAccountDB.setLastPasswordChangeAt(LocalDateTime.now());
			userAccountDB.setRoles(userAccountDB.getRoles());
			userAccountDB = this.userAccountService.save(userAccountDB);
		}
		userDB.setUserAccount(userAccountDB);
		validateUser(userDB);
		this.userRepository.saveAndFlush(userDB);
		return new UserDto(userDB.getId(), userDB.getName(), userDB.getSurnames(), userDB.getNick(),
				userDB.getGitUser(), userDB.getPhoto(), userDB.getUserAccount().getId());
	}

	public void flush() {
		userRepository.flush();
	}

	public Collection<UserWithNickDto> findByNickStartsWith(FindByNickDto findByNickDto) {
		List<User> users = this.userRepository.findByNickStartsWith(findByNickDto.getWord());
		Collection<Integer> idUsers = new ArrayList<>();
		if (findByNickDto.getUsers() != null) {
			idUsers.addAll(findByNickDto.getUsers());
		}
		if (findByNickDto.getTeam() != null) {
			Team team = this.teamService.findOne(findByNickDto.getTeam());
			if (team != null) {
				users = users.stream().filter(u -> !this.invitationService.existsActiveInvitation(u, team)).collect(Collectors.toList());
				idUsers.addAll(this.userRolService.findIdUsersByTeam(team));
			}
		}
		users = users.stream().filter(u -> (!idUsers.contains(u.getId())) && (!u.getUserAccount().getRoles().contains(Role.ROLE_ADMIN))).collect(Collectors.toList());
		if (users.size() > 5) {
			users = users.subList(0, 5);
		}
		ModelMapper mapper = new ModelMapper();
		Type listType = new TypeToken<List<UserWithNickDto>>() {
		}.getType();
		return mapper.map(users, listType);
	}

	public void anonymize() {
		String anonymous = "anonymous";
		SecureRandom random = new SecureRandom();
		User principal = this.getUserByPrincipal();
		this.validateUser(principal);
		principal.setGitUser(anonymous);
		principal.setName(anonymous);
		String nickAnonymous = new BigInteger(130, random).toString(32);
		principal.setNick(nickAnonymous);
		principal.setPhoto(anonymous);
		principal.setSurnames(anonymous);
		UserAccount userAccountAnonymous = principal.getUserAccount();
		String usernameAnonymous = new BigInteger(130, random).toString(32) + "@anonymous.es";
		String passwordAnonymous = new BigInteger(130, random).toString(32) + "Aa";
		userAccountAnonymous.setUsername(usernameAnonymous);
		userAccountAnonymous.setPassword(passwordAnonymous);
		this.taskService.getOutAllTasks(principal);
		this.userRolService.leaveAllTeams(principal);
		this.userAccountService.save(userAccountAnonymous);
		principal.setUserAccount(userAccountAnonymous);
		this.userRepository.saveAndFlush(principal);
	}
	
	public Boolean getIsAdminOfSystem() {
		return this.getUserByPrincipal().getUserAccount().getRoles().contains(Role.ROLE_ADMIN);
	}
	
	public AllDataDto getAllMyData() {
		User principal = this.getUserByPrincipal();
		this.validateUser(principal);
		Collection<Task> tasks = this.taskService.findAllTaskByUser(principal);
		Collection<TaskToAllDataDto> tasksToAllDataDto = new ArrayList<>();
		for (Task task : tasks) {
			TaskToAllDataDto taskAllDataDto = new TaskToAllDataDto(task.getTitle(), task.getDescription(),
					task.getPoints());
			tasksToAllDataDto.add(taskAllDataDto);
		}
		
		Collection<Team> teams = this.userRolService.findAllByUser(principal);
		Collection<String> teamsName = teams.stream().map(Team::getName).collect(Collectors.toList());
		return new AllDataDto(principal.getUserAccount().getUsername(), principal.getName(), principal.getSurnames(),
				principal.getNick(), principal.getGitUser(), principal.getPhoto(), tasksToAllDataDto, teamsName);
	}

	private void validateUser(User user) {
		if (user == null) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "The user is null");
		}
	}

	private void validatePermission(User user) {
		User principal = this.getUserByPrincipal();
		if (!user.equals(principal)) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "The user does not match the logged in user");
		}
	}

	private void validateUserPrincipalTeam(User principal, Team team) {
		if (principal == null) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "The user must be logged in");
		}
		if (!this.userRolService.isUserOnTeam(principal, team)) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "The user must belong to the team");
		}
	}

	private void validateWorkspace(Workspace workspace) {
		if (workspace == null) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "The workspace is not in the database");
		}
	}

	private void validatePassword(UserAccount userAccountDB, String previousPassword, String newPassword) {
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		String pattern = "^.*(?=.{8,})(?=..*[0-9])(?=.*[a-z])(?=.*[A-Z]).*$";
		if (!encoder.matches(previousPassword, userAccountDB.getPassword())) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
					"The current password does not match the one stored in the database");
		}
		if (!newPassword.matches(pattern)) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
					"The new password must have an uppercase, a lowercase, a number and at least 8 characters");
		}

	}

	private void validateNick(String nick) {
		if (this.userRepository.existsByNick(nick)) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The nick is not unique");
		}
	}

}
