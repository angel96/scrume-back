package com.spring.Scrume;

import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.server.ResponseStatusException;

import com.spring.CustomObject.DocumentDto;
import com.spring.Model.Document;
import com.spring.Model.Sprint;
import com.spring.Service.DocumentService;
import com.spring.Service.SprintService;

public class DocumentServiceTest extends AbstractTest {
	@Autowired
	private DocumentService documentService;
	@Autowired
	private SprintService sprintService;

	@Test
	public void showBySprintTest() {
		Object[][] objectsShow = {
				// Caso positivo
				{ "testuser1@gmail.com", super.entities().get("sprint1"), null },
				// Caso negativo (el usuario no pertenece al equipo)
				{ "testuser3@gmail.com", super.entities().get("sprint1"), ResponseStatusException.class } };
		Stream.of(objectsShow).forEach(x -> driverShow((String) x[0], (Integer) x[1], (Class<?>) x[2]));

	}

	@Test
	public void getDaily() {
		Object[][] objectsShow = {
		{ "testuser1@gmail.com", super.entities().get("sprint1"), null }};
		Stream.of(objectsShow).forEach(x -> driverGetDaily((String) x[0], (Integer) x[1], (Class<?>) x[2]));

	}
	
	@Test
	public void saveTest() {
		Object[][] objectsSave = {
				// Caso positivo
				{ "testuser1@gmail.com", super.entities().get("sprint1"), null, "DAILY" },
				// Caso negativo (el usuario no pertenece al equipo)
				{ "testuser3@gmail.com", super.entities().get("sprint1"), ResponseStatusException.class, "DAILY" },
				// Caso negativo(tipo incorrecto)
				{ "testuser1@gmail.com", super.entities().get("sprint1"), ResponseStatusException.class, "FAIL" },
				// Caso negativo(tipo nulo)
				{ "testuser1@gmail.com", super.entities().get("sprint1"), ResponseStatusException.class, null} };
		Stream.of(objectsSave).forEach(x -> driverSave((String) x[0], (Integer) x[1], (Class<?>) x[2], (String) x[3]));

	}
	
	@Test
	public void updateTest() {
		Object[][] objectsUpdate = {
				// Caso positivo
				{ "testuser1@gmail.com", super.entities().get("doc1"), null, "DAILY" },
				// Caso negativo (el usuario no pertenece al equipo)
				{ "testuser3@gmail.com", super.entities().get("doc1"), ResponseStatusException.class, "DAILY" },
				// Caso negativo(tipo incorrecto)
				{ "testuser1@gmail.com", super.entities().get("doc1"), ResponseStatusException.class, "FAIL" },
				// Caso negativo(tipo nulo)
				{ "testuser1@gmail.com", super.entities().get("doc1"), ResponseStatusException.class, null},
				// Caso negativo (id incorrecto)
				{ "testuser1@gmail.com", Integer.MAX_VALUE, ResponseStatusException.class,"DAILY" }};
		Stream.of(objectsUpdate).forEach(x -> driverUpdate((String) x[0], (Integer) x[1], (Class<?>) x[2], (String) x[3]));

	}
	
	@Test
	public void deleteTest() {
		Object[][] objectsDelete = {
				// Caso positivo
				{ "testuser1@gmail.com", super.entities().get("doc1"), null },
				// Caso negativo (el usuario no pertenece al equipo)
				{ "testuser3@gmail.com", super.entities().get("doc1"), ResponseStatusException.class }};
		Stream.of(objectsDelete).forEach(x -> driverDelete((String) x[0], (Integer) x[1], (Class<?>) x[2]));

	}
	
	//@Test
	public void showDtoTest() {
		Object[][] objectsShowDto = {
				// Caso positivo
				{ "testuser1@gmail.com", super.entities().get("doc1"), null },
				// Caso negativo (el usuario no pertenece al equipo)
				{ "testuser3@gmail.com", super.entities().get("doc1"), ResponseStatusException.class } };
		Stream.of(objectsShowDto).forEach(x -> driverShowDto((String) x[0], (Integer) x[1], (Class<?>) x[2]));

	}

	protected void driverShow(String user, Integer entity, Class<?> expected) {
		Class<?> caught = null;

		try {
			super.authenticateOrUnauthenticate(user);
			Sprint sprint = this.sprintService.getOne(entity);
			this.documentService.findAllBySprint(sprint.getId());
			super.authenticateOrUnauthenticate(null);
		} catch (Exception oops) {
			caught = oops.getClass();
		}
		super.checkExceptions(expected, caught);
	}

	protected void driverGetDaily(String user, Integer idSprint, Class<?> expected) {
		Class<?> caught = null;

		try {
			super.authenticateOrUnauthenticate(user);
			this.documentService.getDaily(idSprint);
			super.authenticateOrUnauthenticate(null);
		} catch (Exception oops) {
			caught = oops.getClass();
		}
		super.checkExceptions(expected, caught);
	}
	
	protected void driverSave(String user, Integer entity, Class<?> expected, String type) {
		Class<?> caught = null;

		try {
			super.authenticateOrUnauthenticate(user);
			int sprintId = this.sprintService.getOne(entity).getId();
			DocumentDto doc = new DocumentDto(0, "Test", type, "Test", sprintId);
			this.documentService.save(doc, sprintId);
			super.authenticateOrUnauthenticate(null);
		} catch (Exception oops) {
			caught = oops.getClass();
		}
		super.checkExceptions(expected, caught);
	}
	
	protected void driverUpdate(String user, Integer entity, Class<?> expected, String type) {
		Class<?> caught = null;

		try {
			super.authenticateOrUnauthenticate(user);
			Document doc = this.documentService.findOne(entity);
			DocumentDto docDto = new DocumentDto(doc.getId(), doc.getName(),
					String.valueOf(doc.getType()), doc.getContent(), doc.getSprint().getId());
			docDto.setType(type);
			this.documentService.update(docDto, doc.getId());
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
			this.documentService.delete(entity);
			this.documentService.flush();
			super.authenticateOrUnauthenticate(null);
		} catch (Exception oops) {
			caught = oops.getClass();

		}
		super.checkExceptions(expected, caught);
	}
	
	protected void driverShowDto(String user, Integer entity, Class<?> expected) {
		Class<?> caught = null;

		try {
			super.authenticateOrUnauthenticate(user);
			this.documentService.findOneDto(entity);
			super.authenticateOrUnauthenticate(null);
		} catch (Exception oops) {
			caught = oops.getClass();
		}
		super.checkExceptions(expected, caught);
	}


}
