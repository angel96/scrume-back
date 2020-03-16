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
import com.spring.CustomObject.WorkspaceSprintEditDto;
import com.spring.Model.Workspace;
import com.spring.Service.WorkspaceService;

@RestController
@RequestMapping("/api/workspace")
public class WorkspaceApiController extends AbstractApiController {

	@Autowired
	private WorkspaceService serviceWorkspace;

	@GetMapping("/list/{team}")
	public Collection<Workspace> list(@PathVariable int team) {
		return serviceWorkspace.findWorkspacesByTeam(team);
	}

	@GetMapping("/get/{workspace}")
	public Workspace get(@PathVariable int workspace) {
		return this.serviceWorkspace.findOne(workspace);
	}

	@PostMapping("/save")
	public Workspace save(@RequestBody WorkspaceEditDto workspace) {
		return this.serviceWorkspace.save(0, workspace);
	}

	@PutMapping("/save/{workspace}")
	public Workspace save(@PathVariable int workspace, @RequestBody WorkspaceEditDto workspaceDto) {
		return this.serviceWorkspace.save(workspace, workspaceDto);
	}

	@DeleteMapping("/delete/{workspace}")
	public ResponseEntity<?> delete(@PathVariable int workspace) {
		return this.serviceWorkspace.delete(workspace)
				? new ResponseEntity<>("It has been delete properly", HttpStatus.OK)
				: new ResponseEntity<>("There was a problem!", HttpStatus.OK);
	}

}
