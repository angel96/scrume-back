package com.spring.Scrume;

import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.server.ResponseStatusException;

import com.spring.Service.ColumnService;

public class ColumnServiceTest extends AbstractTest {

	@Autowired
	private ColumnService service;

	@Test
	public void testFindColumnsTask() {
		Object[][] objects = { { super.entities().get("workspace1"), null }, };
		Stream.of(objects).forEach(x -> driverFindColumnsTask((Integer) x[0], (Class<?>) x[1]));
	}

	protected void driverFindColumnsTask(Integer workspace, Class<?> expected) {
		Class<?> caught = null;

		try {
			service.findColumnsTasksByWorkspace(workspace);
			service.flush();
		} catch (Exception oops) {
			caught = oops.getClass();
		}

		super.checkExceptions(expected, caught);
	}

	@Test
	public void testDelete() {
		Object[][] objects = { { "testuser1@gmail.com", super.entities().get("workspace1"), null },
				{ "testuser3@gmail.com", super.entities().get("workspace1"), ResponseStatusException.class } };
		Stream.of(objects).forEach(x -> driverDelete((String) x[0], (Integer) x[1], (Class<?>) x[2]));
	}

	protected void driverDelete(String username, Integer workspace, Class<?> expected) {
		Class<?> caught = null;

		try {
			super.authenticateOrUnauthenticate(username);
			service.deleteColumns(workspace);
			service.flush();
			super.authenticateOrUnauthenticate(null);
		} catch (Exception oops) {
			caught = oops.getClass();
		}

		super.checkExceptions(expected, caught);
	}
}
