package com.spring.Scrume;

import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.server.ResponseStatusException;

import com.spring.CustomObject.WorkspaceEditDto;
import com.spring.Model.Workspace;
import com.spring.Repository.WorkspaceRepository;
import com.spring.Service.SprintService;
import com.spring.Service.WorkspaceService;

public class WorkspaceServiceTest extends AbstractTest {
	@Autowired
	private WorkspaceService service;

	@Autowired
	private SprintService serviceSprint;
	
	@Autowired
	private WorkspaceRepository workspaceRepository;

	@Test
	public void testListTodoColumnsOfAProject() {
		Object[][] objects = { 
				{ "testuser1@gmail.com", super.entities().get("project1"), null },
				{ "testuser10@gmail.com", super.entities().get("project6"), null },
				{ "testuser12@gmail.com", super.entities().get("project7"), null },
				{ "testuser3@gmail.com", super.entities().get("project1"), ResponseStatusException.class } };
		Stream.of(objects)
				.forEach(x -> driverListTodoColumnsOfAProject((String) x[0], (Integer) x[1], (Class<?>) x[2]));
	}

	protected void driverListTodoColumnsOfAProject(String user, Integer entity, Class<?> expected) {
		Class<?> caught = null;

		try {
			super.authenticateOrUnauthenticate(user);
			this.service.listTodoColumnsOfAProject(entity);
			service.flush();
			super.authenticateOrUnauthenticate(null);
		} catch (Throwable oops) {
			caught = oops.getClass();
		}
		super.checkExceptions(expected, caught);
	}

	@Test
	public void testFindWorkspacesByTeam() {
		this.workspaceRepository.save(new Workspace("Test", this.serviceSprint.getOne(super.entities().get("sprint7"))));
		this.workspaceRepository.save(new Workspace("Test", this.serviceSprint.getOne(super.entities().get("sprint7"))));
		Object[][] objects = { 
				{ "testuser1@gmail.com", super.entities().get("team1"), null },
				{ "testuser10@gmail.com", super.entities().get("team5"), null },
				{ "testuser12@gmail.com", super.entities().get("team6"), null },
				{ "testuser3@gmail.com", super.entities().get("team1"), ResponseStatusException.class } };

		Stream.of(objects).forEach(x -> driverFindWorkspacesByTeam((String) x[0], (Integer) x[1], (Class<?>) x[2]));
	}

	protected void driverFindWorkspacesByTeam(String user, Integer entity, Class<?> expected) {

		Class<?> caught = null;

		try {
			super.authenticateOrUnauthenticate(user);
			this.service.findWorkspacesByTeam(entity);
			service.flush();
			super.authenticateOrUnauthenticate(null);
		} catch (Throwable oops) {
			caught = oops.getClass();
		}
		super.checkExceptions(expected, caught);
	}

	@Test
	public void testFindWorkspaceWithColumns() {
		Object[][] objects = { { "testuser1@gmail.com", super.entities().get("workspace1"), null },
				{ "testuser3@gmail.com", super.entities().get("workspace1"), ResponseStatusException.class } };

		Stream.of(objects)
				.forEach(x -> driverFindWorkspacesWithColumns((String) x[0], (Integer) x[1], (Class<?>) x[2]));
	}

	protected void driverFindWorkspacesWithColumns(String user, Integer entity, Class<?> expected) {

		Class<?> caught = null;

		try {
			super.authenticateOrUnauthenticate(user);
			this.service.findWorkspaceWithColumns(entity);
			service.flush();
			super.authenticateOrUnauthenticate(null);
		} catch (Throwable oops) {
			caught = oops.getClass();
		}

		super.checkExceptions(expected, caught);
	}

	@Test
	public void testFindWorkspacesBySprint() {
		Object[][] objects = { 
				{ "testuser1@gmail.com", super.entities().get("sprint1"), null },
				{ "testuser10@gmail.com", super.entities().get("sprint6"), null },
				{ "testuser12@gmail.com", super.entities().get("sprint7"), null },
				{ "testuser3@gmail.com", super.entities().get("sprint1"), ResponseStatusException.class } };

		Stream.of(objects).forEach(x -> driverFindWorkspacesBySprint((String) x[0], (Integer) x[1], (Class<?>) x[2]));
	}

	protected void driverFindWorkspacesBySprint(String user, Integer entity, Class<?> expected) {

		Class<?> caught = null;

		try {
			super.authenticateOrUnauthenticate(user);
			this.service.findWorkspacesBySprint(entity);
			service.flush();
			super.authenticateOrUnauthenticate(null);
		} catch (Throwable oops) {
			caught = oops.getClass();
		}

		super.checkExceptions(expected, caught);
	}
	
	@Test
	public void testFindWorkspaceLastModifiedByProject() {
		Object[][] objects = { 
				{ "testuser1@gmail.com", super.entities().get("project1"), null },
				{ "testuser10@gmail.com", super.entities().get("project6"), null },
				{ "testuser12@gmail.com", super.entities().get("project7"), null },
				{ "testuser3@gmail.com", super.entities().get("project1"), ResponseStatusException.class } };

		Stream.of(objects).forEach(x -> driverFindWorkspaceLastModifiedByProject((String) x[0], (Integer) x[1], (Class<?>) x[2]));
	}

	protected void driverFindWorkspaceLastModifiedByProject(String user, Integer idProject, Class<?> expected) {

		Class<?> caught = null;

		try {
			super.authenticateOrUnauthenticate(user);
			this.service.findWorkspaceLastModifiedByProject(idProject);
			service.flush();
			super.authenticateOrUnauthenticate(null);
		} catch (Throwable oops) {
			caught = oops.getClass();
		}

		super.checkExceptions(expected, caught);
	}

	@Test
	public void testSave() {
		this.workspaceRepository.save(new Workspace("Test", this.serviceSprint.getOne(super.entities().get("sprint7"))));
		Workspace workspace = this.workspaceRepository.save(new Workspace("Test", this.serviceSprint.getOne(super.entities().get("sprint7"))));
		WorkspaceEditDto dto1 = new WorkspaceEditDto(0, "test1", super.entities().get("sprint1"));
		WorkspaceEditDto dto2 = new WorkspaceEditDto(0, "test1", super.entities().get("sprint6"));
		WorkspaceEditDto dto3 = new WorkspaceEditDto(0, "test1", super.entities().get("sprint7"));
		WorkspaceEditDto dto4 = new WorkspaceEditDto(super.entities().get("workspace6"), "test1", super.entities().get("sprint6"));
		WorkspaceEditDto dto5 = new WorkspaceEditDto(workspace.getId(), "test1", super.entities().get("sprint7"));
		WorkspaceEditDto dto6 = new WorkspaceEditDto(super.entities().get("workspace1"), "test1", super.entities().get("sprint1"));
		Object[][] objects = { 
				{ "testuser1@gmail.com", dto1, null },
				{ "testuser4@gmail.com", dto6, ResponseStatusException.class },
				{ "testuser10@gmail.com", dto2, ResponseStatusException.class },
				{ "testuser12@gmail.com", dto3, ResponseStatusException.class },
				{ "testuser10@gmail.com", dto4, ResponseStatusException.class },
				{ "testuser12@gmail.com", dto5, ResponseStatusException.class },
				{ "testuser2@gmail.com", dto6, ResponseStatusException.class },
				{ "testuser1@gmail.com", dto6, null } };

		Stream.of(objects).forEach(x -> driverSave((String) x[0], (WorkspaceEditDto) x[1],(Class<?>) x[2]));
	}

	protected void driverSave(String user, WorkspaceEditDto dto, Class<?> expected) {
		Class<?> caught = null;

		try {

			super.authenticateOrUnauthenticate(user);
			this.service.save(dto.getId(), dto);
			service.flush();
			super.authenticateOrUnauthenticate(null);
		} catch (Throwable oops) {
			caught = oops.getClass();
		}
		super.checkExceptions(expected, caught);
	}

	@Test
	public void testDelete() {
		Object[][] objects = {
				{ "testuser3@gmail.com", super.entities().get("workspace1"), ResponseStatusException.class },
				{ "testuser2@gmail.com", super.entities().get("workspace1"), ResponseStatusException.class },
				{ "testuser1@gmail.com", super.entities().get("workspace1"), null } };

		Stream.of(objects).forEach(x -> driverDelete((String) x[0], (Integer) x[1], (Class<?>) x[2]));
	}

	protected void driverDelete(String user, Integer entity, Class<?> expected) {
		Class<?> caught = null;

		try {
			super.authenticateOrUnauthenticate(user);
			this.service.delete(entity);
			service.flush();
			super.authenticateOrUnauthenticate(null);
		} catch (Throwable oops) {
			caught = oops.getClass();
		}
		super.checkExceptions(expected, caught);
	}
}
