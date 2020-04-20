package com.spring.Scrume;

import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.server.ResponseStatusException;

import com.spring.CustomObject.NoteDto;
import com.spring.Service.NoteService;

public class NoteServiceTest extends AbstractTest {

	@Autowired
	private NoteService noteService;
	
	@Test
	public void NoteServiceFindAllByUserTest() throws Exception {
		
		Object[][] objects = {
			{"testuser1@gmail.com", null}};

			Stream.of(objects).forEach(x -> driverNoteServiceFindAllByUserTest((String) x[0], (Class<?>) x[1]));
		}

	protected void driverNoteServiceFindAllByUserTest(String user, Class<?> expected) {
		Class<?> caught = null;
		try {
			super.authenticateOrUnauthenticate(user);
			this.noteService.findAllByUser();
			this.noteService.flush();
			super.authenticateOrUnauthenticate(null);

		} catch (Exception oops) {
			caught = oops.getClass();
		}
		super.checkExceptions(expected, caught);
	}
	
	@Test
	public void NoteServiceGetTest() throws Exception {
		
		Object[][] objects = {
			{"testuser3@gmail.com", super.entities().get("note1"), ResponseStatusException.class},
			{"testuser1@gmail.com", super.entities().get("note1"), null}};

			Stream.of(objects).forEach(x -> driverNoteServiceGetTest((String) x[0], (Integer) x[1], (Class<?>) x[2]));
		}

	protected void driverNoteServiceGetTest(String user, Integer idNote, Class<?> expected) {
		Class<?> caught = null;
		try {
			super.authenticateOrUnauthenticate(user);
			this.noteService.getOne(idNote);
			this.noteService.flush();
			super.authenticateOrUnauthenticate(null);

		} catch (Exception oops) {
			caught = oops.getClass();
		}
		super.checkExceptions(expected, caught);
	}
	
	@Test
	public void NoteServiceSaveTest() throws Exception {
		NoteDto NoteDto = new NoteDto("nuevo");
		Object[][] objects = {
			{"testuser1@gmail.com", NoteDto, null}};

			Stream.of(objects).forEach(x -> driverNoteServiceSaveTest((String) x[0], (NoteDto)x[1], (Class<?>) x[2]));
		}

	protected void driverNoteServiceSaveTest(String user, NoteDto NoteDto, Class<?> expected) {
		Class<?> caught = null;
		try {
			super.authenticateOrUnauthenticate(user);
			this.noteService.save(NoteDto);
			this.noteService.flush();
			super.authenticateOrUnauthenticate(null);

		} catch (Exception oops) {
			caught = oops.getClass();
		}
		super.checkExceptions(expected, caught);
	}
	
	@Test
	public void NoteServiceUpdateTest() throws Exception {
		NoteDto NoteDto = new NoteDto("cambiado");
		Object[][] objects = {
			{"testuser2@gmail.com", super.entities().get("note1"), NoteDto, ResponseStatusException.class},
			{"testuser1@gmail.com", super.entities().get("note1"), NoteDto, null}};

			Stream.of(objects).forEach(x -> driverNoteServiceUpdateTest((String) x[0], (Integer) x[1], (NoteDto)x[2], (Class<?>) x[3]));
		}

	protected void driverNoteServiceUpdateTest(String user, Integer idNote, NoteDto NoteDto, Class<?> expected) {
		Class<?> caught = null;
		try {
			super.authenticateOrUnauthenticate(user);
			this.noteService.update(idNote, NoteDto);
			this.noteService.flush();
			super.authenticateOrUnauthenticate(null);

		} catch (Exception oops) {
			caught = oops.getClass();
		}
		super.checkExceptions(expected, caught);
	}
	
	@Test
	public void NoteServiceDeleteTest() throws Exception {
		Object[][] objects = {
			{"testuser2@gmail.com", super.entities().get("note1"), ResponseStatusException.class},
			{"testuser1@gmail.com", super.entities().get("note1"), null}};

			Stream.of(objects).forEach(x -> driverNoteServiceDeleteTest((String) x[0], (Integer) x[1], (Class<?>) x[2]));
		}

	protected void driverNoteServiceDeleteTest(String user, Integer idNote, Class<?> expected) {
		Class<?> caught = null;
		try {
			super.authenticateOrUnauthenticate(user);
			this.noteService.delete(idNote);
			this.noteService.flush();
			super.authenticateOrUnauthenticate(null);

		} catch (Exception oops) {
			caught = oops.getClass();
		}
		super.checkExceptions(expected, caught);
	}
}
