package com.spring.Scrume;

import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.server.ResponseStatusException;

import com.spring.CustomObject.SecurityBreachDto;
import com.spring.Service.SecurityBreachService;

public class SecurityBreachServiceTest extends AbstractTest {

	@Autowired
	private SecurityBreachService securityBreachService;
	
	@Test
	public void SecurityBreachServiceGetTest() throws Exception {
		
		Object[][] objects = {
			
			{"testuser1@gmail.com", null}};

			Stream.of(objects).forEach(x -> driverSecurityBreachServiceGetTest((String) x[0], (Class<?>) x[1]));
		}

	protected void driverSecurityBreachServiceGetTest(String user, Class<?> expected) {
		Class<?> caught = null;
		try {
			super.authenticateOrUnauthenticate(user);
			this.securityBreachService.getSecurityBreach();
			this.securityBreachService.flush();
			super.authenticateOrUnauthenticate(null);

		} catch (Exception oops) {
			caught = oops.getClass();
		}
		super.checkExceptions(expected, caught);
	}
	
	@Test
	public void SecurityBreachServiceUpdateTest() throws Exception {
		SecurityBreachDto securityBreachDto = new SecurityBreachDto("cambiado", true);
		Object[][] objects = {
			{"testuser1@gmail.com", securityBreachDto, ResponseStatusException.class},
			{"administrator@gmail.com", securityBreachDto, null}};

			Stream.of(objects).forEach(x -> driverSecurityBreachServiceUpdateTest((String) x[0], (SecurityBreachDto)x[1], (Class<?>) x[2]));
		}

	protected void driverSecurityBreachServiceUpdateTest(String user, SecurityBreachDto securityBreachDto, Class<?> expected) {
		Class<?> caught = null;
		try {
			super.authenticateOrUnauthenticate(user);
			this.securityBreachService.updateSecurityBreach(securityBreachDto);
			this.securityBreachService.flush();
			super.authenticateOrUnauthenticate(null);

		} catch (Exception oops) {
			caught = oops.getClass();
		}
		super.checkExceptions(expected, caught);
	}
}
