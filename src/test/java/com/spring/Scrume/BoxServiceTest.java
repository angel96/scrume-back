package com.spring.Scrume;

import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.server.ResponseStatusException;

import com.spring.Service.BoxService;

public class BoxServiceTest extends AbstractTest {

	@Autowired
	private BoxService service;

	@Test
	public void testMinimalBox() {
		Object[][] objects = { { "testuser1@gmail.com", null } };

		Stream.of(objects).forEach(x -> driverMinimalBox((String) x[0], (Class<?>) x[1]));
	}

	protected void driverMinimalBox(String user, Class<?> expected) {

		Class<?> caught = null;

		try {
			super.authenticateOrUnauthenticate(user);

			assert service.getMinimumBoxOfATeam(super.entities().get("team1")).getName() == "PRO";
			service.flush();
			super.authenticateOrUnauthenticate(null);
		} catch (Throwable oops) {
			caught = oops.getClass();
		}
		super.checkExceptions(expected, caught);
	}

	@Test
	public void testGetBox() {
		Object[][] objects = { { super.entities().get("proBox"), null }, { -1, ResponseStatusException.class } };

		Stream.of(objects).forEach(x -> driverGetBox((Integer) x[0], (Class<?>) x[1]));
	}

	protected void driverGetBox(Integer box, Class<?> expected) {

		Class<?> caught = null;

		try {
			service.findOne(box);
			service.flush();
		} catch (Throwable oops) {
			caught = oops.getClass();
		}
		super.checkExceptions(expected, caught);
	}
}
