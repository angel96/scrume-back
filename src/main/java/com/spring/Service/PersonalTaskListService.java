package com.spring.Service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.spring.CustomObject.PersonalTaskListDto;
import com.spring.Model.PersonalTaskList;
import com.spring.Model.User;
import com.spring.Repository.PersonalTaskListRepository;

@Service
@Transactional
public class PersonalTaskListService extends AbstractService {
	@Autowired
	private PersonalTaskListRepository repo;
	@Autowired 
	private UserService userService;
	
	private PersonalTaskList findOne(int id) {
		return this.repo.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
		
	}
	
	public List<PersonalTaskList> findAllByUser(){
		User user = this.userService.getUserByPrincipal();
		this.validateUserIsLogged(user);
		return this.repo.findByUser(user);
	}
	

	public PersonalTaskList save(PersonalTaskListDto dto) {
		User user = this.userService.getUserByPrincipal();
		this.validateUserIsLogged(user);
		PersonalTaskList entity = new PersonalTaskList(user, dto.getContent());
		entity = this.repo.saveAndFlush(entity);
		return entity;
	}
	
	public PersonalTaskList update(int idPersonalTask, PersonalTaskListDto dto) {
		User user = this.userService.getUserByPrincipal();
		PersonalTaskList db = this.findOne(idPersonalTask);
		this.validateUserIsLogged(user);
		this.checkUser(user, db.getUser());
		db.setContent(dto.getContent());
		db = this.repo.saveAndFlush(db);
		return db;
	}
	
	public void delete(int listId) {
		User principal = this.userService.getUserByPrincipal();
		PersonalTaskList personalTask = this.findOne(listId);
		this.checkUser(principal, personalTask.getUser());
		this.repo.delete(personalTask);
	}
	
	private void checkUser(User principal, User user) {
		if(principal.getId() != user.getId())
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only the owner can do this task");
	}
	
	private void validateUserIsLogged(User user) {
		if (user == null) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "The user must be logged in");
		}
		
	}
	
	public void flush() {
		repo.flush();
	}

}
