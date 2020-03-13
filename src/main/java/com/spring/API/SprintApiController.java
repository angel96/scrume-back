package com.spring.API;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.spring.CustomObject.SprintCreateDto;
import com.spring.CustomObject.SprintEditDto;
import com.spring.Service.InvitationService;
import com.spring.Service.SprintService;

@RestController
@RequestMapping("/api/sprint")
public class SprintApiController {

	@Autowired
	private SprintService sprintService;
	
	@Autowired
	private InvitationService invitationService;
	
	@PostMapping("/save")
	public SprintCreateDto save(@RequestBody SprintCreateDto sprintDto) throws Exception{
		return this.sprintService.save(sprintDto);
	}
	
	@PostMapping("/update")
	public SprintEditDto update(@RequestBody SprintEditDto sprintDto) throws Exception{
		return this.sprintService.update(sprintDto);
	}
	
}
