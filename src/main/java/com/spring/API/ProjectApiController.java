package com.spring.API;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.spring.Service.ProjectService;

@RestController
@RequestMapping("/api/project")
public class ProjectApiController extends AbstractApiController {

	@Autowired
	private ProjectService projectService;
	
	@GetMapping("/listProjects")
	@ResponseBody
	public ResponseEntity<?> findByTeamId(@PathVariable("id") Integer idProject) {
		ResponseEntity<?> result = null;
		
		try {
			result = new ResponseEntity<>(projectService.findProjectByTeamId(idProject), HttpStatus.OK);
			super.logger.info("/api/project/listProjects" + HttpStatus.OK);
		} catch (final Throwable oops) {
			super.logger.error(oops);
			result = new ResponseEntity<>("No está autorizado", HttpStatus.UNAUTHORIZED);
			super.logger.error("GET /api/project/listProjects" + HttpStatus.UNAUTHORIZED);
		}
		return result;
	}
	
	@GetMapping(value = "/get/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<?> getProject(@PathVariable final int idProject) {
		ResponseEntity<?> result = null;
		
		try {
			result = new ResponseEntity<>(projectService.findOne(idProject), HttpStatus.OK);
			super.logger.info("/api/project/get/" + idProject + HttpStatus.OK);
		} catch (final Throwable oops) {
			super.logger.error(oops);
			result = new ResponseEntity<>("No está autorizado", HttpStatus.UNAUTHORIZED);
			super.logger.error("GET /api/project/get/" + idProject + HttpStatus.UNAUTHORIZED);
		}
		return result;
	}
	
	
}
