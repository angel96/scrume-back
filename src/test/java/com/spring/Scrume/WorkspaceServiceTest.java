package com.spring.Scrume;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.server.ResponseStatusException;

import com.spring.CustomObject.SprintDto;
import com.spring.CustomObject.WorkspaceEditDto;
import com.spring.Service.ProjectService;
import com.spring.Service.SprintService;
import com.spring.Service.WorkspaceService;

public class WorkspaceServiceTest extends AbstractTest {
	@Autowired
	private WorkspaceService service;

	@Autowired
	private SprintService serviceSprint;

	@Autowired
	private ProjectService serviceProject;

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

		Object[][] objects = { { "testuser1@gmail.com", super.entities().get("team1"), null },
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
		Object[][] objects = { { "testuser1@gmail.com", super.entities().get("sprint1"), null },
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
	public void testSave() {
		ZoneId defaultZoneId = ZoneId.systemDefault();
		Date startDate1 = Date.from(LocalDate.of(2040, 04, 15).atStartOfDay(defaultZoneId).toInstant());
		Date startDate2 = Date.from(LocalDate.of(2050, 04, 15).atStartOfDay(defaultZoneId).toInstant());
		Date startDate3 = Date.from(LocalDate.of(2060, 04, 15).atStartOfDay(defaultZoneId).toInstant());

		Date endDate1 = Date.from(LocalDate.of(2040, 04, 15).atStartOfDay(defaultZoneId).toInstant());
		Date endDate2 = Date.from(LocalDate.of(2050, 04, 15).atStartOfDay(defaultZoneId).toInstant());
		Date endDate3 = Date.from(LocalDate.of(2060, 04, 15).atStartOfDay(defaultZoneId).toInstant());

		
		Object[][] objects = { { "testuser1@gmail.com", 0, startDate1, endDate1, null },
				{ "testuser2@gmail.com", super.entities().get("workspace1"), startDate2, endDate2, ResponseStatusException.class },
				{ "testuser1@gmail.com", super.entities().get("workspace1"), startDate3, endDate3, null } };

		Stream.of(objects).forEach(x -> driverSave((String) x[0], (Integer) x[1], (Date) x[2], (Date) x[3],(Class<?>) x[4]));
	}

	protected void driverSave(String user, Integer entity, Date startDate,  Date endDate, Class<?> expected) {
		Class<?> caught = null;

		try {

			super.authenticateOrUnauthenticate(user);

			WorkspaceEditDto dto = entity == 0 ? new WorkspaceEditDto()
					: new WorkspaceEditDto(service.findOne(entity).getId(), service.findOne(entity).getName(),
							service.findOne(entity).getSprint().getId());

			dto.setName("Changing name");
			dto.setSprint(serviceSprint.save(
					new SprintDto(0, startDate,
							endDate,
							serviceProject.findOne(super.entities().get("project1"))))
					.getId());

			this.service.save(entity, dto);

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
