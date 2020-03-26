package com.spring.Scrume;

import java.time.LocalDateTime;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.server.ResponseStatusException;

import com.spring.CustomObject.UserAccountDto;
import com.spring.Model.UserAccount;
import com.spring.Security.UserAccountService;

public class UserAccountServiceTest extends AbstractTest{
	
	@Autowired
	private UserAccountService	userAccountService;
	
	@Test
	public void userAccountTestSave() throws Exception {
		Object[][] objects = {
				{ "prueba@gmail.com", "Prueba12345", null }, //Caso positivo
				{ "prueba", "Prueba12345", ResponseStatusException.class}, //Caso negativo: username invalido
				{ "prueba@gmail.com", "prueba12345", ResponseStatusException.class}}; //Caso negativo: password invalida
				 
		
		Stream.of(objects).forEach(x -> driverTestSave((String) x[0], (String) x[1], (Class<?>) x[2]));
	}
	
	protected void driverTestSave(String username, String password, Class<?> expected) {
		Class<?> caught = null;
		try {
			UserAccountDto userAccountDto = new UserAccountDto();
			userAccountDto.setUsername(username);
			userAccountDto.setPassword(password);
			this.userAccountService.save(userAccountDto);
		} catch (Exception oops) {
			caught = oops.getClass();
		}
		super.checkExceptions(expected, caught);
	}
	
	@Test
	public void userAccountTestUpdate() throws Exception {
		Object[][] objects = {
				{ super.entities().get("user1"), super.entities().get("account1"), "prueba22@gmail.com", null}}; //Caso positivo
				 
		
		Stream.of(objects).forEach(x -> driverTestUpdate((Integer) x[0], (Integer) x[1], (String) x[2], (Class<?>) x[3]));
	}
	
	protected void driverTestUpdate(Integer userId, Integer userAccountId, String username, Class<?> expected) {
		Class<?> caught = null;
		try {
			System.out.println(userId);
			System.out.println(userAccountId);
			UserAccount userAccountDB = this.userAccountService.findOne(userAccountId);
			System.out.println(userAccountDB);
			UserAccountDto userAccountDto = new UserAccountDto();
			userAccountDto.setPassword(userAccountDB.getPassword());
			userAccountDto.setRoles(null);
			userAccountDto.setUsername(userAccountDB.getUsername());
			userAccountDto.setUsername(username);
			this.userAccountService.update(userAccountId, userAccountDto);
		} catch (Exception oops) {
			caught = oops.getClass();
		}
		super.checkExceptions(expected, caught);
	}
}
