package com.spring.Scrume;

import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.server.ResponseStatusException;

import com.spring.Model.Sprint;
import com.spring.Service.DocumentService;
import com.spring.Service.SprintService;

public class DocumentServiceTest extends AbstractTest {
/*	@Autowired
	private DocumentService documentService;
	@Autowired
	private SprintService sprintService;

	@Test
	public void showBySprintTest() {
		Object[][] objectsShow = {
				//Caso positivo
				{"testuser@gmail.com", super.entities().get("sprint1"), null},
				//Caso negativo (el usuario no pertenece al equipo)
				{"angdellun@gmail.com", super.entities().get("sprint1"), ResponseStatusException.class}
	};
		Stream.of(objectsShow).forEach(x -> driverShow((String) x[0], (Integer) x[1], (Class<?>) x[2]));

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
*/
	}
