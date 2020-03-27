package com.spring.Scrume;

import java.util.HashSet;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.modelmapper.internal.util.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.server.ResponseStatusException;

import com.spring.CustomObject.TaskDto;
import com.spring.CustomObject.TaskEditDto;
import com.spring.Model.Sprint;
import com.spring.Model.Task;
import com.spring.Model.Workspace;
import com.spring.Service.SprintService;
import com.spring.Service.TaskService;
import com.spring.Service.WorkspaceService;

public class TaskServiceTest extends AbstractTest {

	@Autowired
	private TaskService taskService;
	@Autowired
	SprintService sprintService;
	@Autowired
	WorkspaceService workService;

	@Test
	public void findTest() throws Exception {
		// Param 0 -> User to make the call
		// Param 1 -> Entity
		// Param 2 -> Value to modify
		// Param 3 -> Expected Exception;
		Object[][] objectsFind = {
				// Caso negativo(el usuario no pertenece al equipo de la Task)
				{ "testuser3@gmail.com", super.entities().get("task1"), ResponseStatusException.class },
				// Caso positivo
				{ "testuser1@gmail.com", super.entities().get("task1"), null }, };
		Stream.of(objectsFind).forEach(x -> driverFindTest((String) x[0], (Integer) x[1], (Class<?>) x[2]));
	}

	@Test
	public void findOneTest() throws Exception {
		Object[][] objectsFindOne = {
				// Caso positivo
				{ "testuser1@gmail.com", super.entities().get("task1"), null },
				// Caso negativo(usuario no autentificado)
				{ "", super.entities().get("task1"), UsernameNotFoundException.class },
				// Caso negativo(no existe Task con dicho id)
				{ "testuser1@gmail.com", Integer.MAX_VALUE, ResponseStatusException.class }, };
		Stream.of(objectsFindOne).forEach(x -> driverFindOneTest((String) x[0], (Integer) x[1], (Class<?>) x[2]));
	}

	@Test
	public void findBySprintTest() throws Exception {

		Object[][] objectsFindBySprint = {
				// Caso positivo
				{ "testuser1@gmail.com", super.entities().get("sprint1"), null },
				// Caso negativo(no existe Sprint con dicho id)
				{ "testuser1@gmail.com", Integer.MAX_VALUE, ResponseStatusException.class } };
		Stream.of(objectsFindBySprint)
				.forEach(x -> driverFindBySprintTest((String) x[0], (Integer) x[1], (Class<?>) x[2]));
	}

	@Test
	public void saveTest() throws Exception {

		Object[][] objectsSave = {
				// Caso positivo
				{ "testuser1@gmail.com", super.entities().get("project1"), null },
				// Caso negativo(usuario no logueado)
				{ "", super.entities().get("project1"), UsernameNotFoundException.class },
				// Caso negativo(el proyecto no existe)
				{ "testuser1@gmail.com", Integer.MAX_VALUE, ResponseStatusException.class },
				// Caso negativo(el usuario no pertenece al equipo)
				{ "testuser3@gmail.com", super.entities().get("project1"), ResponseStatusException.class } };
		Stream.of(objectsSave).forEach(x -> driverSave((String) x[0], (Integer) x[1], (Class<?>) x[2]));
	}

	@Test
	public void updateTest() throws Exception {
		Object[][] objectsUpdate = {
				// Caso positivo
				{ "testuser1@gmail.com", super.entities().get("task1"), null },
				// Caso negativo(usuario no logueado)
				{ "", super.entities().get("task1"), UsernameNotFoundException.class },
				// Caso negativo(el proyecto no existe)
				{ "testuser1@gmail.com", Integer.MAX_VALUE, ResponseStatusException.class },
				// Caso negativo(el usuario no pertenece al equipo)
				{ "testuser3@gmail.com", super.entities().get("task1"), ResponseStatusException.class } };
		Stream.of(objectsUpdate).forEach(x -> driverUpdate((String) x[0], (Integer) x[1], (Class<?>) x[2]));
	}

	@Test
	public void deleteTest() throws Exception {
		Object[][] objectsDelete = {
				// Caso positivo
				{ "testuser1@gmail.com", super.entities().get("task1"), null },
				// Caso negativo(usuario no logueado)
				{ "", super.entities().get("task1"), UsernameNotFoundException.class },
				// Caso negativo(la tarea no existe)
				{ "testuser1@gmail.com", Integer.MAX_VALUE, ResponseStatusException.class },
				// Caso negativo(el usuario no pertenece al equipo)
				{ "testuser3@gmail.com", super.entities().get("task1"), ResponseStatusException.class },
				// Caso negativo(el usuario no es el administrador del equipo)
				{ "testuser3@gmail.com", super.entities().get("task5"), ResponseStatusException.class } };
		Stream.of(objectsDelete).forEach(x -> driverDelete((String) x[0], (Integer) x[1], (Class<?>) x[2]));
	}

	@Test
	public void listAllTaskByProjectTest() throws Exception {
		Object[][] objectsListAllTaskByProject = {
				// Caso positivo
				{ "testuser1@gmail.com", super.entities().get("project1"), null },
				// Caso negativo(el usuario no pertenece al equipo de la Task)
				{ "testuser3@gmail.com", super.entities().get("project1"), ResponseStatusException.class } };
		Stream.of(objectsListAllTaskByProject)
				.forEach(x -> driverListAllTaskByProject((String) x[0], (Integer) x[1], (Class<?>) x[2]));
	}

	@Test
	public void findByWorkspaceTest() throws Exception {
		Object[][] objectsFindByWorkspace = {
				// Caso positivo
				{ "testuser1@gmail.com", super.entities().get("workspace1"), null },
				// Caso negativo(el workspace no existe)
				{ "testuser1@gmail.com", Integer.MAX_VALUE, ResponseStatusException.class },
				{ "testuser1@gmail.com", null, NullPointerException.class }, };

		Stream.of(objectsFindByWorkspace)
				.forEach(x -> driverFindByWorkspace((String) x[0], (Integer) x[1], (Class<?>) x[2]));

	}

	@Test
	public void saveEstimation() throws Exception {

		Object[][] objectsSaveEstimation = {
				// Caso positivo
				{ "testuser1@gmail.com", super.entities().get("task1"), null },
				// Caso negativo(la tarea no existe)
				{ "testuser1@gmail.com", Integer.MAX_VALUE, ResponseStatusException.class } };

		Stream.of(objectsSaveEstimation)
				.forEach(x -> driverSaveEstimationTest((String) x[0], (Integer) x[1], (Class<?>) x[2]));
	}

	protected void driverFindTest(String user, Integer entity, Class<?> expected) {
		Class<?> caught = null;

		try {
			super.authenticateOrUnauthenticate(user);
			this.taskService.find(entity);
			super.authenticateOrUnauthenticate(null);
		} catch (Exception oops) {
			caught = oops.getClass();

		}
		super.checkExceptions(expected, caught);
	}

	protected void driverFindOneTest(String user, Integer entity, Class<?> expected) {
		Class<?> caught = null;

		try {
			super.authenticateOrUnauthenticate(user);
			this.taskService.findOne(entity);
			super.authenticateOrUnauthenticate(null);
		} catch (Exception oops) {
			caught = oops.getClass();

		}
		super.checkExceptions(expected, caught);
	}

	protected void driverFindBySprintTest(String user, Integer entity, Class<?> expected) {
		Class<?> caught = null;
		try {
			super.authenticateOrUnauthenticate(user);
			Sprint sprint = this.sprintService.getOne(entity);
			List<Task> tasks = this.taskService.findBySprint(sprint);
			Assert.isTrue(!tasks.isEmpty());
			super.authenticateOrUnauthenticate(null);
		} catch (Exception oops) {
			caught = oops.getClass();

		}
		super.checkExceptions(expected, caught);
	}

	protected void driverSave(String user, Integer entity, Class<?> expected) {
		Class<?> caught = null;
		try {
			super.authenticateOrUnauthenticate(user);
			TaskDto dto = new TaskDto("test", "test", 0, entity, new HashSet<>(), 0);
			this.taskService.save(dto, entity);
			this.taskService.flush();
			super.authenticateOrUnauthenticate(null);
		} catch (Exception oops) {
			caught = oops.getClass();

		}
		super.checkExceptions(expected, caught);
	}

	protected void driverUpdate(String user, Integer entity, Class<?> expected) {
		Class<?> caught = null;
		try {
			super.authenticateOrUnauthenticate(user);
			Task task = this.taskService.findOne(entity);
			TaskEditDto dto = new TaskEditDto();
			dto.setDescription(task.getDescription());
			dto.setId(task.getId());
			dto.setTitle("Test");
			TaskEditDto updated = this.taskService.update(dto, entity);
			Assert.isTrue(!(updated.getTitle() != task.getTitle()));
			this.taskService.flush();
			super.authenticateOrUnauthenticate(null);
		} catch (Exception oops) {
			caught = oops.getClass();

		}
		super.checkExceptions(expected, caught);
	}

	protected void driverDelete(String user, Integer entity, Class<?> expected) {
		Class<?> caught = null;

		try {
			super.authenticateOrUnauthenticate(user);
			System.out.println(user);
			this.taskService.delete(entity);
			this.taskService.flush();
			super.authenticateOrUnauthenticate(null);
		} catch (Exception oops) {
			caught = oops.getClass();

		}
		super.checkExceptions(expected, caught);
	}

	protected void driverListAllTaskByProject(String user, Integer entity, Class<?> expected) {
		Class<?> caught = null;

		try {
			super.authenticateOrUnauthenticate(user);
			System.out.println(user);
			this.taskService.getAllTasksByProject(entity);
			super.authenticateOrUnauthenticate(null);
		} catch (Exception oops) {
			caught = oops.getClass();
		}
		super.checkExceptions(expected, caught);
	}

	protected void driverFindByWorkspace(String user, Integer entity, Class<?> expected) {
		Class<?> caught = null;

		try {
			super.authenticateOrUnauthenticate(user);
			Workspace w = this.workService.findOne(entity);
			this.taskService.findByWorkspace(w);
			super.authenticateOrUnauthenticate(null);
		} catch (Exception oops) {
			caught = oops.getClass();

		}
		super.checkExceptions(expected, caught);
	}

	protected void driverSaveEstimationTest(String user, Integer entity, Class<?> expected) {
		Class<?> caught = null;

		try {
			super.authenticateOrUnauthenticate(user);
			Task task = this.taskService.findOne(entity);
			this.taskService.saveEstimation(task, Integer.MAX_VALUE);
			super.authenticateOrUnauthenticate(null);
		} catch (Exception oops) {
			caught = oops.getClass();

		}
		super.checkExceptions(expected, caught);
	}

}
