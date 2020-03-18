package com.spring.API;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.spring.CustomObject.WorkspaceEditDto;
import com.spring.Model.Workspace;
import com.spring.Service.WorkspaceService;

@RestController
@RequestMapping("/api/workspace")
public class WorkspaceApiController extends AbstractApiController {

	@Autowired
	private WorkspaceService serviceWorkspace;

	@GetMapping("/list/{team}")
	public Collection<Workspace> list(@PathVariable int team) {
		super.logger.info("GET /api/workspace/list/" + String.valueOf(team));
		return serviceWorkspace.findWorkspacesByTeam(team);
	}

	@GetMapping("/{workspace}")
	public Workspace get(@PathVariable int workspace) {
		super.logger.info("GET /api/workspace/" + String.valueOf(workspace));
		return this.serviceWorkspace.findOne(workspace);
	}

	@PostMapping
	public Workspace save(@RequestBody WorkspaceEditDto workspace) {
		super.logger.info("POST /api/workspace");
		return this.serviceWorkspace.save(0, workspace);
	}

	@PutMapping("/{workspace}")
	public Workspace save(@PathVariable int workspace, @RequestBody WorkspaceEditDto workspaceDto) {
		super.logger.info("PUT /api/workspace/" + String.valueOf(workspace));
		return this.serviceWorkspace.save(workspace, workspaceDto);
	}

	@DeleteMapping("/{workspace}")
	public ResponseEntity<?> delete(@PathVariable int workspace) {
		super.logger.info("DELETE /api/workspace/" + String.valueOf(workspace));
		return this.serviceWorkspace.delete(workspace)
				? new ResponseEntity<>("It has been delete properly", HttpStatus.OK)
				: new ResponseEntity<>("There was a problem!", HttpStatus.OK);
	}

}
