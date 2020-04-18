package com.spring.Service;

import java.lang.reflect.Type;
import java.util.List;

import javax.transaction.Transactional;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.spring.CustomObject.NoteDto;
import com.spring.CustomObject.NoteIdDto;
import com.spring.Model.Note;
import com.spring.Model.User;
import com.spring.Repository.NoteRepository;

@Service
@Transactional
public class NoteService extends AbstractService {
	@Autowired
	private NoteRepository noteRepository;
	@Autowired 
	private UserService userService;
	
	private Note findOne(int id) {
		return this.noteRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,  "The requested note not exists"));
	}
	
	public NoteIdDto getOne(int idNote) {
		User user = this.userService.getUserByPrincipal();
		this.validateUserIsLogged(user);
		Note entityDB = this.findOne(idNote);
		this.validateUserPermission(user, entityDB);
		return new NoteIdDto(entityDB.getId(), entityDB.getContent());
	}
	
	public List<NoteIdDto> findAllByUser(){
		User user = this.userService.getUserByPrincipal();
		this.validateUserIsLogged(user);
		List<Note> notes = this.noteRepository.findByUser(user);
		ModelMapper mapper = new ModelMapper();

		Type listType = new TypeToken<List<NoteIdDto>>() {
		}.getType();
		return mapper.map(notes, listType);
	}
	

	public NoteIdDto save(NoteDto dto) {
		User user = this.userService.getUserByPrincipal();
		this.validateUserIsLogged(user);
		Note entity = new Note(user, dto.getContent());
		entity = this.noteRepository.saveAndFlush(entity);
		return new NoteIdDto(entity.getId(), entity.getContent());
	}
	
	public NoteIdDto update(int idNote, NoteDto dto) {
		User user = this.userService.getUserByPrincipal();
		Note db = this.findOne(idNote);
		this.validateUserIsLogged(user);
		this.checkUser(user, db.getUser());
		db.setContent(dto.getContent());
		db = this.noteRepository.saveAndFlush(db);
		return new NoteIdDto(db.getId(), db.getContent());
	}
	
	public void delete(int listId) {
		User principal = this.userService.getUserByPrincipal();
		Note note = this.findOne(listId);
		this.checkUser(principal, note.getUser());
		this.noteRepository.delete(note);
	}
	
	private void checkUser(User principal, User user) {
		if(!principal.getId().equals(user.getId()))
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only the owner can do this note");
	}
	
	private void validateUserPermission(User user, Note note) {
		if(!(note.getUser().equals(user)))
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "The requested note does not belong to the user");	
	}
	private void validateUserIsLogged(User user) {
		if (user == null) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "The user must be logged in");
		}
		
	}
	
	public void flush() {
		noteRepository.flush();
	}


}
