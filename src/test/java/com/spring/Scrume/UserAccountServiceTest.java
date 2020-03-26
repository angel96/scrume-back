package com.spring.Scrume;

import java.time.LocalDateTime;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.server.ResponseStatusException;

import com.spring.CustomObject.UserAccountDto;
import com.spring.Security.UserAccountService;

public class UserAccountServiceTest extends AbstractTest{
	
	@Autowired
	private UserAccountService	userAccountService;
	
	@Test
	public void userAccountTestSave() throws Exception {
		Object[][] objects = {
				{ "prueba@gmail.com", "Prueba12345", LocalDateTime.of(2020, 03, 26, 11, 00, 00), LocalDateTime.of(2020, 03, 26, 11, 01, 00), null }, //Caso positivo
				{ "prueba", "Prueba12345", LocalDateTime.of(2020, 03, 26, 11, 00, 00), LocalDateTime.of(2020, 03, 26, 11, 01, 00), ResponseStatusException.class}, //Caso negativo: username invalido
				{ "prueba@gmail.com", "prueba12345", LocalDateTime.of(2020, 03, 26, 11, 00, 00), LocalDateTime.of(2020, 03, 26, 11, 01, 00), ResponseStatusException.class}, //Caso negativo: password invalida
				{ "prueba@gmail.com", "prueba12345", null, LocalDateTime.of(2020, 03, 26, 11, 01, 00), ResponseStatusException.class}}; //Caso negativo: fecha de creacion nula
				 
		
		Stream.of(objects).forEach(x -> driverTestSave((String) x[0], (String) x[1], (LocalDateTime) x[2], (LocalDateTime) x[3], (Class<?>) x[4]));
	}
	
	protected void driverTestSave(String username, String password, LocalDateTime createdAt, LocalDateTime lastPasswordChangeAt, Class<?> expected) {
		Class<?> caught = null;
		try {
			UserAccountDto userAccountDto = new UserAccountDto();
			userAccountDto.setUsername(username);
			userAccountDto.setPassword(password);
			userAccountDto.setCreatedAt(createdAt);
			userAccountDto.setLastPasswordChangeAt(lastPasswordChangeAt);
			this.userAccountService.save(userAccountDto);
		} catch (Exception oops) {
			caught = oops.getClass();
		}
		super.checkExceptions(expected, caught);
	}
	
	@Test
	public void userAccountTestUpdate() throws Exception {
		UserAccountDto userAccountDto = new UserAccountDto();
		userAccountDto.setCreatedAt(LocalDateTime.of(2020, 03, 26, 11, 00, 00));
		userAccountDto.setLastPasswordChangeAt(LocalDateTime.of(2020, 03, 26, 11, 00, 00));
		userAccountDto.setPassword("Prueba112");
		userAccountDto.setUsername("prueba@gmail.com");
		Integer userAccountId = 1;
		Object[][] objects = {
				{ userAccountDto, userAccountId, "prueba22@gmail.com", null}}; //Caso positivo
				 
		
		Stream.of(objects).forEach(x -> driverTestUpdate((UserAccountDto) x[0], (Integer) x[1], (String) x[2], (Class<?>) x[3]));
	}
	
	protected void driverTestUpdate(UserAccountDto userAccountDto, Integer userAccountId, String usernameName, Class<?> expected) {
		Class<?> caught = null;
		try {
			this.userAccountService.save(userAccountDto);
			userAccountDto.setUsername(usernameName);
			this.userAccountService.update(userAccountId, userAccountDto);
		} catch (Exception oops) {
			caught = oops.getClass();
		}
		super.checkExceptions(expected, caught);
	}
}
