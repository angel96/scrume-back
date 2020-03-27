package com.spring.Scrume;

import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import com.spring.CustomObject.UserDto;
import com.spring.Model.User;
import com.spring.Service.UserService;


public class UserServiceTest extends AbstractTest {
	
	@Autowired
	private UserService userService;
	
	@Test
	public void userGet() throws Exception {
		Object[][] objects = {
				{ super.entities().get("user1"), null},
				{ null, NullPointerException.class}};
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
		Object[][] objects = {
			{ "prueba", "Prueba", "pruebatestuser", "fotografía", "Prueba Surname", super.entities().get("account5"), null},
			{ "prueba", "Prueba", "pruebatestuser", "fotografía", "Prueba Surname", super.entities().get("account5"), DataIntegrityViolationException.class},
			{ "prueba", "Prueba", null, "fotografía", "Prueba Surname", super.entities().get("account5"), DataIntegrityViolationException.class}};
		
		Stream.of(objects).forEach(x -> driverTestSave((String) x[0], (String) x[1], (String) x[2], (String) x[3], (String) x[4], (Integer) x[5], (Class<?>) x[6]));
	
	}
	
	protected void driverTestSave(String gitName, String name, String nick, String photo, String surname, Integer idUserAccount, Class<?> expected) {
		Class<?> caught = null;
		try {
			UserDto userDto = new UserDto();
			userDto.setGitUser(gitName);
			userDto.setName(name);
			userDto.setNick(nick);
			userDto.setPhoto(photo);
			userDto.setSurnames(surname);
			userDto.setIdUserAccount(idUserAccount);
			this.userService.save(userDto);
		} catch (Exception oops) {
			caught = oops.getClass();
		}
		super.checkExceptions(expected, caught);
	}
	
	@Test
	public void userTestUpdate() throws Exception {
		Object[][] objects = {
			{ super.entities().get("user2"), "Prueba Surname", null}};
		
		Stream.of(objects).forEach(x -> driverTestUpdate((Integer) x[0], (String) x[1], (Class<?>) x[2]));
	
	}
	
	protected void driverTestUpdate(Integer idUser, String surname, Class<?> expected) {
		Class<?> caught = null;
		try {
			super.authenticateOrUnauthenticate("testuser2@gmail.com");
			User userDB = this.userService.findOne(idUser);
			ModelMapper mapper = new ModelMapper();
			UserDto userDto = mapper.map(userDB, UserDto.class);
			userDto.setSurnames(surname);
			this.userService.update(userDto, idUser);
			super.authenticateOrUnauthenticate(null);
		} catch (Exception oops) {
			caught = oops.getClass();
		}
		super.checkExceptions(expected, caught);
	}

}
