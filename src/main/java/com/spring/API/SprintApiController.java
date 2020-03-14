package com.spring.API;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.spring.CustomObject.SprintCreateDto;
import com.spring.CustomObject.SprintDatesDto;
import com.spring.CustomObject.SprintDto;
import com.spring.CustomObject.SprintEditDto;
import com.spring.Service.SprintService;

@RestController
@RequestMapping("/api/sprint")
public class SprintApiController {

	@Autowired
	private SprintService sprintService;
	
	@GetMapping("/list")
	public List<SprintDto> save(@RequestParam(value="idProject") Integer idProject) throws Exception{
		return this.sprintService.listByProject(idProject);
	}
	
	@GetMapping("/check-dates")
	public boolean areValidDates(@RequestParam(value="idProject") Integer idProject, @RequestBody SprintDatesDto sprintDatesDto) throws Exception{
		sprintDatesDto.setIdProject(idProject);
		return this.sprintService.areValidDates(sprintDatesDto);
	}
	
	@PostMapping("/save")
	public SprintCreateDto save(@RequestBody SprintCreateDto sprintDto) throws Exception{
		return this.sprintService.save(sprintDto);
	}
	
	@PostMapping("/update")
	public SprintEditDto update(@RequestParam(value="idSprint") Integer idSprint, @RequestBody SprintEditDto sprintDto) throws Exception{
		sprintDto.setId(idSprint);
		return this.sprintService.update(sprintDto);
	}
	
}
