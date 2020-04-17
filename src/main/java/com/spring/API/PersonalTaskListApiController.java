package com.spring.API;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.spring.CustomObject.PersonalTaskListDto;
import com.spring.Model.PersonalTaskList;
import com.spring.Service.PersonalTaskListService;

@Controller
@RequestMapping("/api/personalList")
public class PersonalTaskListApiController extends AbstractApiController {
	
	@Autowired
	private PersonalTaskListService service;
	
	@GetMapping
	public List<PersonalTaskList> showAll(){
		super.logger.info("GET /api/personalList");
		return this.service.findAllByUser();
	}
	
	@PostMapping
	public PersonalTaskList save(@RequestBody PersonalTaskListDto dto) {
		super.logger.info("POST /api/personalList");
		return this.service.save(dto);
	}
	
	@PutMapping("{idPersonalTask}")
	public PersonalTaskList update(@PathVariable int idPersonalTask, @RequestBody PersonalTaskListDto dto) {
		super.logger.info("PUT /api/personalList/" + idPersonalTask);
		return this.service.update(idPersonalTask, dto);
		
	}
	
	@DeleteMapping("{idPersonalTask}")
	public void delete(@PathVariable int idPersonalTask) {
		super.logger.info("DELETE /api/personalList/" + idPersonalTask);
		this.service.delete(idPersonalTask);
	}

}
