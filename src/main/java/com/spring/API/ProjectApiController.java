package com.spring.API;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.spring.CustomObject.ProjectDto;
import com.spring.Service.ProjectService;

@RestController
@RequestMapping("/api/project")
public class ProjectApiController extends AbstractApiController {

	@Autowired
	private ProjectService projectService;

	@GetMapping("/{idProject}")
	public ProjectDto get(@PathVariable Integer idProject) {
		super.logger.info("GET /api/project/" + idProject);
		return this.projectService.getOne(idProject);
	}

	@GetMapping("/list/{idTeam}")
	public List<ProjectDto> list(@PathVariable Integer idTeam) {
		super.logger.info("GET /api/project/list/" + idTeam);
		return this.projectService.findProjectByTeamId(idTeam);
	}

	@PostMapping
	public ProjectDto save(@RequestBody ProjectDto project) {
		super.logger.info("POST /api/project");
		return this.projectService.save(project);
	}

	@PutMapping("/{idProject}")
	public ProjectDto update(@PathVariable Integer idProject, @RequestBody ProjectDto project) {
		super.logger.info("PUT /api/project/" + idProject);
		return this.projectService.update(project, idProject);
	}

	@DeleteMapping("/{idProject}")
	public void delete(@PathVariable Integer idProject) {
		super.logger.info("DELETE /api/project/" + idProject);
		this.projectService.delete(idProject);
	}

}
