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

import com.spring.CustomObject.DocumentDto;
import com.spring.CustomObject.PersonalTaskListDto;
import com.spring.Service.PersonalTaskListService;

@Controller
@RequestMapping("/api/personalList")
public class PersonalTaskListApiController extends AbstractApiController {
	
	@Autowired
	private PersonalTaskListService service;
	
	@GetMapping("{idUser}")
	public List<PersonalTaskListDto> showAll(@PathVariable int idUser){
		super.logger.info("GET /api/personalList/" + idUser);
		return this.service.findAllByUser(idUser);
	}
	
	@PostMapping("{idUser}")
	public PersonalTaskListDto save(@PathVariable int idUser, @RequestBody PersonalTaskListDto dto) {
		super.logger.info("POST /api/personalList/" + idUser);
		return this.service.save(idUser, dto);
	}
	
	@PutMapping("{idList}")
	public PersonalTaskListDto update(@PathVariable int idList, @RequestBody PersonalTaskListDto dto) {
		super.logger.info("PUT /api/personalList/" + idList);
		return this.service.update(idList, dto);
		
	}
	
	@DeleteMapping("{idList}")
	public void delete(@PathVariable int idList) {
		super.logger.info("DELETE /api/personalList/" + idList);
		this.service.delete(idList);
	}

}
