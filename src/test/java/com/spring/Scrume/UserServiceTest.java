package com.spring.Scrume;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.server.ResponseStatusException;

import com.spring.CustomObject.FindByNickDto;
import com.spring.CustomObject.UserDto;
import com.spring.CustomObject.UserUpdateDto;
import com.spring.Model.User;
import com.spring.Model.UserAccount;
import com.spring.Security.Role;
import com.spring.Security.UserAccountRepository;
import com.spring.Service.UserService;
import com.spring.Utiles.Utiles;

public class UserServiceTest extends AbstractTest {

	@Autowired
	private UserService userService;
	
	@Autowired
	private UserAccountRepository userAccountRepository;

	@Test
	public void userGet() throws Exception {
		Object[][] objects = { { super.entities().get("user1"), null }, { null, NullPointerException.class } };
		Stream.of(objects).forEach(x -> driverTestGet((Integer) x[0], (Class<?>) x[1]));
	}

	protected void driverTestGet(Integer idUser, Class<?> expected) {
		Class<?> caught = null;
		try {
			super.authenticateOrUnauthenticate("testuser1@gmail.com");
			this.userService.get(idUser);
			super.authenticateOrUnauthenticate(null);
		} catch (Exception oops) {
			caught = oops.getClass();
		}
		super.checkExceptions(expected, caught);
	}

	@Test
	public void userTestSave() throws Exception {
		UserAccount account500 = this.userAccountRepository
				.save(new UserAccount("testuser50000@gmail.com", Utiles.encryptedPassword("1234565"), LocalDateTime.now(),
						LocalDateTime.now(), new HashSet<Role>()));
		Object[][] objects = {
				{ "prueba", "Prueba", "pruebatestuser", "fotografía", "Prueba Surname",
						account500.getId(), null },
				{ "prueba", "Prueba", "pruebatestuser", "fotografía", "Prueba Surname",
							account500.getId(), DataIntegrityViolationException.class },
				{ "prueba", "Prueba", null, "fotografía", "Prueba Surname", account500.getId(),
						DataIntegrityViolationException.class } };

		Stream.of(objects).forEach(x -> driverTestSave((String) x[0], (String) x[1], (String) x[2], (String) x[3],
				(String) x[4], (Integer) x[5], (Class<?>) x[6]));

	}

	protected void driverTestSave(String gitName, String name, String nick, String photo, String surname,
			Integer idUserAccount, Class<?> expected) {
		Class<?> caught = null;
		try {
			UserDto userDto = new UserDto();
			userDto.setGitUser(gitName);
			userDto.setName(name);
			userDto.setNick(nick);
			userDto.setPhoto(photo);
			userDto.setSurnames(surname);
			userDto.setIdUserAccount(idUserAccount);
			userService.flush();
			this.userService.save(userDto);
		} catch (Exception oops) {
			caught = oops.getClass();
		}
		super.checkExceptions(expected, caught);
	}

	@Test
	public void userTestUpdate() throws Exception {
		Object[][] objects = { { super.entities().get("user2"), null, null, null},
				{ super.entities().get("user2"), "1234561", "aCaCa123", ResponseStatusException.class },
				{ super.entities().get("user2"), "1234562", " 52", ResponseStatusException.class } };

		Stream.of(objects)
				.forEach(x -> driverTestUpdate((Integer) x[0], (String) x[1], (String) x[2], (Class<?>) x[3]));

	}

	protected void driverTestUpdate(Integer idUser, String beforePassword, String afterPassword, Class<?> expected) {
		Class<?> caught = null;
		try {
			super.authenticateOrUnauthenticate("testuser2@gmail.com");
			System.out.println(afterPassword);
			User userDB = this.userService.findOne(idUser);
			UserUpdateDto userDto = new UserUpdateDto(userDB.getId(), userDB.getName(), userDB.getSurnames(),
					userDB.getNick(), userDB.getGitUser(), userDB.getPhoto(), null, null);
			if (beforePassword != null && afterPassword != null) {
				userDto.setPreviousPassword(beforePassword);
				userDto.setNewPassword(afterPassword);
			}
			userDto.setSurnames("Prueba Surname");
			this.userService.update(userDto, idUser);
			userService.flush();
			super.authenticateOrUnauthenticate(null);
		} catch (Exception oops) {
			caught = oops.getClass();
		}
		super.checkExceptions(expected, caught);
	}

	@Test
	public void testAnonymize() {
		Object[][] objects = { { "testuser1@gmail.com", null }, { "", ResponseStatusException.class } };

		Stream.of(objects).forEach(x -> driverAnonymize((String) x[0], (Class<?>) x[1]));
	}

	protected void driverAnonymize(String user, Class<?> expected) {
		Class<?> caught = null;
		try {
			super.authenticateOrUnauthenticate(user);
			this.userService.anonymize();
			userService.flush();
			super.authenticateOrUnauthenticate(null);
		} catch (Exception oops) {
			caught = oops.getClass();
		}
		super.checkExceptions(expected, caught);
	}

	@Test
	public void testlistUsersOfATeamByWorkspace() {
		Object[][] objects = { { "testuser1@gmail.com", super.entities().get("workspace1"), null },
				{ "testuser1@gmail.com", super.entities().get("workspace4"), ResponseStatusException.class } };

		Stream.of(objects)
				.forEach(x -> driverlistUsersOfATeamByWorkspace((String) x[0], (Integer) x[1], (Class<?>) x[2]));
	}

	protected void driverlistUsersOfATeamByWorkspace(String username, Integer workspace, Class<?> expected) {
		Class<?> caught = null;

		try {
			super.authenticateOrUnauthenticate(username);
			this.userService.listUsersOfATeamByWorkspace(workspace);
			userService.flush();
			super.authenticateOrUnauthenticate(null);
		} catch (Exception oops) {
			caught = oops.getClass();
		}
		super.checkExceptions(expected, caught);
	}

	@Test
	public void testAllMyData() {
		Object[][] objects = { { "testuser1@gmail.com", null }, { "", ResponseStatusException.class } };

		Stream.of(objects).forEach(x -> driverAllMyData((String) x[0], (Class<?>) x[1]));
	}

	protected void driverAllMyData(String username, Class<?> expected) {
		Class<?> caught = null;

		try {
			super.authenticateOrUnauthenticate(username);
			this.userService.getAllMyData();
			userService.flush();
			super.authenticateOrUnauthenticate(null);
		} catch (Exception oops) {
			caught = oops.getClass();
		}
		super.checkExceptions(expected, caught);
	}

	@Test
	public void testFindByNickStartsWith() {
		Object[][] objects = { { "angdellun", null }, { "jualorper", null }, { "ezeporjur", null } };

		Stream.of(objects).forEach(x -> driverFindByNickStartsWith((String) x[0], (Class<?>) x[1]));
	}

	protected void driverFindByNickStartsWith(String nick, Class<?> expected) {
		Class<?> caught = null;
		try {
			List<Integer> ids = new ArrayList<>();
			ids.add(super.entities().get("user1"));
			ids.add(super.entities().get("user2"));
			ids.add(super.entities().get("user3"));
			ids.add(super.entities().get("user4"));

			this.userService.findByNickStartsWith(new FindByNickDto(nick, super.entities().get("team1"), ids));
		} catch (Exception oops) {
			caught = oops.getClass();
		}
		super.checkExceptions(expected, caught);
	}
}
