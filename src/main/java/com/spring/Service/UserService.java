package com.spring.Service;

import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Base64;
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

import com.spring.CustomObject.FindByNickDto;
import com.spring.CustomObject.UserDto;
import com.spring.CustomObject.UserLoginDto;
import com.spring.CustomObject.UserOfATeamByWorspaceDto;
import com.spring.CustomObject.UserUpdateDto;
import com.spring.CustomObject.UserWithNickDto;
import com.spring.Model.Team;
import com.spring.Model.User;
import com.spring.Model.UserAccount;
import com.spring.Model.Workspace;
import com.spring.Repository.UserRepository;
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
	private TeamService teamService;

	@Autowired
	private WorkspaceService workspaceService;

	@Autowired
	private PaymentService paymentService;
	
	
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
		userDB.setNick(userDto.getNick());
		userDB.setPhoto(userDto.getPhoto());
		userDB.setSurnames(userDto.getSurnames());
		if(userDto.getPreviousPassword() != null && userDto.getPreviousPassword() != "") {
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
		return new UserDto(userDB.getId(), userDB.getName(), userDB.getSurnames(), userDB.getNick(), userDB.getGitUser(), userDB.getPhoto(), userDB.getUserAccount().getId());
	}



	public void flush() {
		userRepository.flush();
	}

	public Collection<UserWithNickDto> findByNickStartsWith(FindByNickDto findByNickDto) {
		List<User> users = this.userRepository.findByNickStartsWith(findByNickDto.getWord());
		Collection<Integer> idUsers = new ArrayList<>();
		if(findByNickDto.getUsers()!= null) {
			idUsers.addAll(findByNickDto.getUsers());
		}
		if(findByNickDto.getTeam() != null) {
			Team team = this.teamService.findOne(findByNickDto.getTeam());
			if(team != null) {
				idUsers.addAll(this.userRolService.findIdUsersByTeam(team));
			}
		}
		users = users.stream().filter(u -> !idUsers.contains(u.getId())).collect(Collectors.toList());
		if (users.size() > 5) {
			users = users.subList(0, 4);
		}
		ModelMapper mapper = new ModelMapper();
		Type listType = new TypeToken<List<UserWithNickDto>>() {
		}.getType();
		return mapper.map(users, listType);
	}

	private void validateUser(User user) {
		if (user == null) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "The user is null");
		}
	}
	
	private void validatePermission(User user) {
		User principal = this.getUserByPrincipal();
		if(!user.equals(principal)) {
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


	public UserLoginDto getByAuthorization(String string) {
		UserLoginDto res;
		Base64.Decoder dec = Base64.getDecoder();
		String auth;
		String decodedAuth;
		String username;
		try {
			auth = string.split(" ")[1];
			decodedAuth = new String(dec.decode(auth));
			username = decodedAuth.split(":")[0];
			User user = this.userRepository.findUserByUserName(username).orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not authorized"));
			LocalDate endingBoxDate = this.paymentService.findByUserAccount(user.getUserAccount()).getExpiredDate();
			res = new UserLoginDto(user.getId(), user.getUserAccount().getUsername(), endingBoxDate);
		}catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The user has not been found or does not have any box payment record");
		}
		return res;
	}

	private void validatePassword(UserAccount userAccountDB, String previousPassword, String newPassword) {
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		String pattern = "^.*(?=.{8,})(?=..*[0-9])(?=.*[a-z])(?=.*[A-Z]).*$";
		if (!encoder.matches(previousPassword, userAccountDB.getPassword())) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The current password does not match the one stored in the database");
		}
		if (!newPassword.matches(pattern)) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The new password must have an uppercase, a lowercase, a number and at least 8 characters");
		}
		
	}
}
