package com.spring.API;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.spring.CustomObject.ProjectDto;
import com.spring.CustomObject.TeamDto;
import com.spring.Service.ProjectService;

@RestController
@RequestMapping("/api/project")
public class ProjectApiController extends AbstractApiController {

	@Autowired
	private ProjectService projectService;
	
	@GetMapping("/list")
	public List<ProjectDto> list(@RequestParam(value="id") Integer idProject) throws Exception {
		return this.projectService.findProjectByTeamId(idProject);
	}
	

	
	
}
