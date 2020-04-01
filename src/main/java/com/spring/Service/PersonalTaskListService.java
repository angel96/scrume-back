package com.spring.Service;

import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.spring.CustomObject.PersonalTaskListDto;
import com.spring.Model.PersonalTaskList;
import com.spring.Model.User;
import com.spring.Repository.PersonalTaskListRepository;
import com.spring.Security.UserAccountService;

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
	
	public List<PersonalTaskListDto> findAllByUser(int userId){
		User user = this.userService.findOne(userId);
		checkUser(user);
		return this.repo.findByUser(user).stream().map(x->new PersonalTaskListDto(x.getId(), x.getUser().getId(), x.getContent())).collect(Collectors.toList());
	}
	
	public PersonalTaskListDto save(int userId, PersonalTaskListDto dto) {
		User user = this.userService.findOne(userId);
		checkUser(user);
		PersonalTaskList entity = new PersonalTaskList(user, dto.getContent());
		entity = this.repo.saveAndFlush(entity);
		return new PersonalTaskListDto(entity.getId(),entity.getUser().getId(), entity.getContent());
	}
	
	public PersonalTaskListDto update(int listId, PersonalTaskListDto dto) {
		PersonalTaskList db = this.findOne(listId);
		checkUser(db.getUser());
		db.setContent(dto.getContent());
		db = this.repo.saveAndFlush(db);
		return new PersonalTaskListDto(db.getId(), db.getUser().getId(), db.getContent());
	}
	
	public void delete(int listId) {
		
		if(!this.repo.existsById(listId)) 
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		
		this.repo.deleteById(listId);
	}
	
	private void checkUser(User user) {
		if(UserAccountService.getPrincipal().equals(user.getUserAccount()))
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only the owner can do this task");
	}
	
	

}
