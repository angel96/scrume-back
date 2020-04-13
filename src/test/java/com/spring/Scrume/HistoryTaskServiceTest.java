package com.spring.Scrume;

import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.server.ResponseStatusException;

import com.spring.CustomObject.HistoryTaskDto;
import com.spring.Model.Task;
import com.spring.Service.HistoryTaskService;
import com.spring.Service.TaskService;

public class HistoryTaskServiceTest extends AbstractTest {

	@Autowired
	private HistoryTaskService serviceHistoryTask;
	@Autowired
	private TaskService serviceTask;

	@Test
	public void testFindHistoricalByWorkspace() throws Exception {
		Object[][] objects = { { "testuser1@gmail.com", super.entities().get("workspace1"), null },
				{ "testuser1@gmail.com", 0, null },
				{ "testuser2@gmail.com", super.entities().get("workspace1"), ResponseStatusException.class } };

		Stream.of(objects)
				.forEach(x -> driverFindHistoricalByWorkspace((String) x[0], (Integer) x[1], (Class<?>) x[2]));
	}

	protected void driverFindHistoricalByWorkspace(String user, Integer entity, Class<?> expected) {
		Class<?> caught = null;

		try {
			super.authenticateOrUnauthenticate(user);
			this.serviceHistoryTask.findHistoricalByWorkspace(entity);
			this.serviceHistoryTask.flush();
			super.authenticateOrUnauthenticate(null);
		} catch (Exception oops) {
			caught = oops.getClass();

		}
		super.checkExceptions(expected, caught);
	}

	@Test
	public void testSaveBacklog() {
		Task task = this.serviceTask.findOne(super.entities().get("task1"));
		task.setColumn(null);

		Object[][] objects = {
				{ "testuser1@gmail.com", super.entities().get("task3"), super.entities().get("toDo5"), null },
				{ "testuser1@gmail.com", super.entities().get("task1"), super.entities().get("toDo4"),
						ResponseStatusException.class } 
				};

		Stream.of(objects).forEach(x ->

		driverSave((String) x[0], (Integer) x[1], (Integer) x[2], (Class<?>) x[3]));
	}

	@Test
	public void testSave() throws Exception {

		Object[][] objects = {
				{ "testuser1@gmail.com", super.entities().get("task1"), super.entities().get("inProgress"), null },
				{ "testuser2@gmail.com", super.entities().get("task1"), super.entities().get("inProgress"),
						ResponseStatusException.class },
				{ "testuser1@gmail.com", super.entities().get("task1"), super.entities().get("inProgress4"),
						ResponseStatusException.class },
				{ "testuser1@gmail.com", super.entities().get("task1"), super.entities().get("inProgress4"),
						ResponseStatusException.class } };

		Stream.of(objects).forEach(x ->

		driverSave((String) x[0], (Integer) x[1], (Integer) x[2], (Class<?>) x[3]));
	}

	protected void driverSave(String user, Integer entity, Integer destiny, Class<?> expected) {
		Class<?> caught = null;

		try {
			super.authenticateOrUnauthenticate(user);
			this.serviceHistoryTask.save(new HistoryTaskDto(destiny, entity));
			this.serviceHistoryTask.flush();
			super.authenticateOrUnauthenticate(null);
		} catch (Exception oops) {
			caught = oops.getClass();
			System.out.println(caught);
		}
		super.checkExceptions(expected, caught);
	}
}
