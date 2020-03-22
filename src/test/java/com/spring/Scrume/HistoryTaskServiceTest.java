package com.spring.Scrume;

import java.util.Collection;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.server.ResponseStatusException;

import com.spring.CustomObject.HistoryTaskDto;
import com.spring.Model.HistoryTask;
import com.spring.Service.HistoryTaskService;

public class HistoryTaskServiceTest extends AbstractTest {

	@Autowired
	private HistoryTaskService service;

	@Test
	public void testFindHistoricalByWorkspace() throws Exception {

		/**
		 * Positivo
		 * 
		 * + Listar el historico de un workspace del usuario loggeado
		 * 
		 * Negativo
		 * 
		 * - Listar el workspace de un usuario no loggeado - Listar el workspace de un
		 * usuario que no es el loggeado.
		 * 
		 */

		Object[][] objects = { { "angdellun@gmail.com", super.entities().get(""), null },
				{ "angdellun@gmail.com", super.entities().get(""), ResponseStatusException.class } };

		Stream.of(objects)
				.forEach(x -> driverFindHistoricalByWorkspace((String) x[0], (Integer) x[1], (Class<?>) x[2]));
	}

	protected void driverFindHistoricalByWorkspace(String user, Integer entity, Class<?> expected) {
		Class<?> caught = null;

		try {
			super.authenticateOrUnauthenticate(user);
			Collection<HistoryTask> historical = this.service.findHistoricalByWorkspace(entity);
			this.service.flush();
			super.authenticateOrUnauthenticate(null);
		} catch (Exception oops) {
			caught = oops.getClass();
		}
		super.checkExceptions(expected, caught);
	}

	@Test
	public void testSave() throws Exception {

		/**
		 * Positivo
		 * 
		 * + Mover una tarea entre columnas del mismo workspace. 
		 * + Mover una tarea del backlog de un proyecto
		 * 
		 * Negativo
		 * 
		 * - Mover una tarea de un proyecto al que el usuario logegado no pertenece.
		 * - Mover una tarea de un equipo al que el usuario no pertenece.
		 */

		Object[][] objects = { { "angdellun@gmail.com", super.entities().get(""), null },
				{ "angdellun@gmail.com", super.entities().get(""), ResponseStatusException.class } };

		// Stream.of(objects)
		// .forEach(x -> driverFindHistoricalByWorkspace((String) x[0], (Integer) x[1],
		// (Class<?>) x[2]));
	}

	protected void driverSave(String user, Integer entity, Class<?> expected) {
		Class<?> caught = null;

		try {
			super.authenticateOrUnauthenticate(user);

			HistoryTaskDto saveTo = new HistoryTaskDto();

			this.service.flush();
			super.authenticateOrUnauthenticate(null);
		} catch (Exception oops) {
			caught = oops.getClass();
		}
		super.checkExceptions(expected, caught);
	}
}
