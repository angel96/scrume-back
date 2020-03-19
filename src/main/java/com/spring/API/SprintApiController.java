package com.spring.API;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.spring.CustomObject.SprintDto;
import com.spring.CustomObject.SprintEditDto;
import com.spring.CustomObject.SprintStatisticsDto;
import com.spring.Model.Project;
import com.spring.Service.SprintService;

@RestController
@RequestMapping("/api/sprint")
public class SprintApiController extends AbstractApiController{

	@Autowired
	private SprintService sprintService;
	
	@GetMapping("/list/{idProject}")
	public List<SprintStatisticsDto> list(@PathVariable Integer idProject) throws Exception{
		super.logger.info("GET /api/sprint/list" + String.valueOf(idProject));
		return this.sprintService.listByProject(idProject);
	}
	
	@GetMapping("/{idSprint}")
	public SprintStatisticsDto getStatistics(@PathVariable Integer idSprint) throws Exception{
		super.logger.info("GET /api/sprint/" + String.valueOf(idSprint));
		return this.sprintService.getStatistics(idSprint);
	}
	
	@GetMapping("/check-dates/{idProject}")
	public boolean areValidDates(@PathVariable Integer idProject, @RequestBody SprintDto sprintDto) throws Exception{
		super.logger.info("GET /api/sprint/check-dates/" + String.valueOf(idProject));
		Project project = new Project();
		project.setId(idProject);
		sprintDto.setProject(project);
		return this.sprintService.areValidDates(sprintDto);
	}
	
	@PostMapping
	public SprintDto save(@RequestBody SprintDto sprintDto) throws Exception{
		super.logger.info("POST /api/sprint");
		return this.sprintService.save(sprintDto);
	}
	
	@PutMapping("/{idSprint}")
	public SprintEditDto update(@PathVariable Integer idSprint, @RequestBody SprintEditDto sprintDto) throws Exception{
		sprintDto.setId(idSprint);
		super.logger.info("PUT /api/sprint/" + String.valueOf(idSprint));
		return this.sprintService.update(sprintDto);
	}
	
}
