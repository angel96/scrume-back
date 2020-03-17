package com.spring.API;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
	
	@GetMapping("/list")
	public List<SprintStatisticsDto> list(@RequestParam(value="idProject") Integer idProject) throws Exception{
		return this.sprintService.listByProject(idProject);
	}
	
	@GetMapping("/statistics")
	public SprintStatisticsDto getStatistics(@RequestParam(value="idSprint") Integer idSprint) throws Exception{
		return this.sprintService.getStatistics(idSprint);
	}
	
	@GetMapping("/check-dates")
	public boolean areValidDates(@RequestParam(value="idProject") Integer idProject, @RequestBody SprintDto sprintDto) throws Exception{
		Project project = new Project();
		project.setId(idProject);
		sprintDto.setProject(project);
		return this.sprintService.areValidDates(sprintDto);
	}
	
	@PostMapping("/save")
	public SprintDto save(@RequestBody SprintDto sprintDto) throws Exception{
		return this.sprintService.save(sprintDto);
	}
	
	@PostMapping("/update")
	public SprintEditDto update(@RequestParam(value="idSprint") Integer idSprint, @RequestBody SprintEditDto sprintDto) throws Exception{
		sprintDto.setId(idSprint);
		return this.sprintService.update(sprintDto);
	}
	
}
