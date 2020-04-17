package com.spring.Service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.spring.CustomObject.NoteDto;
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
		return this.noteRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
		
	}
	
	public List<Note> findAllByUser(){
		User user = this.userService.getUserByPrincipal();
		this.validateUserIsLogged(user);
		return this.noteRepository.findByUser(user);
	}
	

	public Note save(NoteDto dto) {
		User user = this.userService.getUserByPrincipal();
		this.validateUserIsLogged(user);
		Note entity = new Note(user, dto.getContent());
		entity = this.noteRepository.saveAndFlush(entity);
		return entity;
	}
	
	public Note update(int idNote, NoteDto dto) {
		User user = this.userService.getUserByPrincipal();
		Note db = this.findOne(idNote);
		this.validateUserIsLogged(user);
		this.checkUser(user, db.getUser());
		db.setContent(dto.getContent());
		db = this.noteRepository.saveAndFlush(db);
		return db;
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
	
	private void validateUserIsLogged(User user) {
		if (user == null) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "The user must be logged in");
		}
		
	}
	
	public void flush() {
		noteRepository.flush();
	}

}
