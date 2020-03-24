package com.spring.Scrume;

import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.spring.Service.BoxService;

public class BoxServiceTest extends AbstractTest {

	@Autowired
	private BoxService service;

	@Test
	public void testMinimalBox() {
		Object[][] objects = { { "testuser@gmail.com", null } };

		Stream.of(objects).forEach(x -> driverMinimalBox((String) x[0], (Class<?>) x[1]));
	}

	protected void driverMinimalBox(String user, Class<?> expected) {

		Class<?> caught = null;

		try {
			super.authenticateOrUnauthenticate(user);
			
			assert service.getMinimumBoxOfATeam(super.entities().get("team1")).getName() == "BASIC";
			
			super.authenticateOrUnauthenticate(null);
		} catch (Throwable oops) {
			caught = oops.getClass();
		}
		super.checkExceptions(expected, caught);
	}
}
