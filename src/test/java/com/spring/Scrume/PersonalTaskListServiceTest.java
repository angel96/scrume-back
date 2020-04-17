package com.spring.Scrume;

import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.server.ResponseStatusException;

import com.spring.CustomObject.PersonalTaskListDto;
import com.spring.Service.PersonalTaskListService;

public class PersonalTaskListServiceTest extends AbstractTest {

	@Autowired
	private PersonalTaskListService personalTaskListService;
	
	@Test
	public void PersonalTaskListServiceFindAllByUserTest() throws Exception {
		
		Object[][] objects = {
			{"testuser1@gmail.com", null}};

			Stream.of(objects).forEach(x -> driverPersonalTaskListServiceFindAllByUserTest((String) x[0], (Class<?>) x[1]));
		}

	protected void driverPersonalTaskListServiceFindAllByUserTest(String user, Class<?> expected) {
		Class<?> caught = null;
		try {
			super.authenticateOrUnauthenticate(user);
			this.personalTaskListService.findAllByUser();
			this.personalTaskListService.flush();
			super.authenticateOrUnauthenticate(null);

		} catch (Exception oops) {
			caught = oops.getClass();
		}
		super.checkExceptions(expected, caught);
	}
	
	@Test
	public void personalTaskListServiceSaveTest() throws Exception {
		PersonalTaskListDto personalTaskListDto = new PersonalTaskListDto("nuevo");
		Object[][] objects = {
			{"testuser1@gmail.com", personalTaskListDto, null}};

			Stream.of(objects).forEach(x -> driverPersonalTaskListServiceSaveTest((String) x[0], (PersonalTaskListDto)x[1], (Class<?>) x[2]));
		}

	protected void driverPersonalTaskListServiceSaveTest(String user, PersonalTaskListDto personalTaskListDto, Class<?> expected) {
		Class<?> caught = null;
		try {
			super.authenticateOrUnauthenticate(user);
			this.personalTaskListService.save(personalTaskListDto);
			this.personalTaskListService.flush();
			super.authenticateOrUnauthenticate(null);

		} catch (Exception oops) {
			caught = oops.getClass();
		}
		super.checkExceptions(expected, caught);
	}
	
	@Test
	public void personalTaskListServiceUpdateTest() throws Exception {
		PersonalTaskListDto personalTaskListDto = new PersonalTaskListDto("cambiado");
		Object[][] objects = {
			{"testuser2@gmail.com", super.entities().get("personalList1"), personalTaskListDto, ResponseStatusException.class},
			{"testuser1@gmail.com", super.entities().get("personalList1"), personalTaskListDto, null}};

			Stream.of(objects).forEach(x -> driverPersonalTaskListServiceUpdateTest((String) x[0], (Integer) x[1], (PersonalTaskListDto)x[2], (Class<?>) x[3]));
		}

	protected void driverPersonalTaskListServiceUpdateTest(String user, Integer idPersonalTask, PersonalTaskListDto personalTaskListDto, Class<?> expected) {
		Class<?> caught = null;
		try {
			super.authenticateOrUnauthenticate(user);
			this.personalTaskListService.update(idPersonalTask, personalTaskListDto);
			this.personalTaskListService.flush();
			super.authenticateOrUnauthenticate(null);

		} catch (Exception oops) {
			caught = oops.getClass();
		}
		super.checkExceptions(expected, caught);
	}
	
	@Test
	public void personalTaskListServiceDeleteTest() throws Exception {
		Object[][] objects = {
			{"testuser2@gmail.com", super.entities().get("personalList1"), ResponseStatusException.class},
			{"testuser1@gmail.com", super.entities().get("personalList1"), null}};

			Stream.of(objects).forEach(x -> driverPersonalTaskListServiceDeleteTest((String) x[0], (Integer) x[1], (Class<?>) x[2]));
		}

	protected void driverPersonalTaskListServiceDeleteTest(String user, Integer idPersonalTask, Class<?> expected) {
		Class<?> caught = null;
		try {
			super.authenticateOrUnauthenticate(user);
			this.personalTaskListService.delete(idPersonalTask);
			this.personalTaskListService.flush();
			super.authenticateOrUnauthenticate(null);

		} catch (Exception oops) {
			caught = oops.getClass();
		}
		super.checkExceptions(expected, caught);
	}
}
