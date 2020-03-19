package com.spring.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.spring.customobject.SprintDto;
import com.spring.customobject.SprintEditDto;
import com.spring.customobject.SprintStatisticsDto;
import com.spring.model.Project;
import com.spring.service.SprintService;

@RestController
@RequestMapping("/api/sprint")
public class SprintApiController extends AbstractApiController{

	@Autowired
	private SprintService sprintService;
	
	@GetMapping("/list/{idProject}")
	public List<SprintStatisticsDto> list(@PathVariable Integer idProject){
		super.logger.info("GET /api/sprint/list" + idProject);
		return this.sprintService.listByProject(idProject);
	}
	
	@GetMapping("/{idSprint}")
	public SprintStatisticsDto getStatistics(@PathVariable Integer idSprint) {
		super.logger.info("GET /api/sprint/" + idSprint);
		return this.sprintService.getStatistics(idSprint);
	}
	
	@GetMapping("/check-dates/{idProject}")
	public boolean areValidDates(@PathVariable Integer idProject, @RequestBody SprintDto sprintDto) {
		super.logger.info("GET /api/sprint/check-dates/" + idProject);
		Project project = new Project();
		project.setId(idProject);
		sprintDto.setProject(project);
		return this.sprintService.areValidDates(sprintDto);
	}
	
	@PostMapping
	public SprintDto save(@RequestBody SprintDto sprintDto){
		super.logger.info("POST /api/sprint");
		return this.sprintService.save(sprintDto);
	}
	
	@PutMapping("/{idSprint}")
	public SprintEditDto update(@PathVariable Integer idSprint, @RequestBody SprintEditDto sprintDto) {
		sprintDto.setId(idSprint);
		super.logger.info("PUT /api/sprint/" + idSprint);
		return this.sprintService.update(sprintDto);
	}
	
}
