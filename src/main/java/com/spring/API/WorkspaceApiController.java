package com.spring.API;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.spring.Model.Sprint;
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

	@GetMapping("/create/")
	public Workspace create() {
		return new Workspace("", new Sprint());
	}

	@PostMapping("/save")
	public Workspace save(@RequestBody Workspace workspace) {
		return this.serviceWorkspace.save(workspace);
	}

	@GetMapping("/delete/{workspace}")
	public ResponseEntity<?> delete(@PathVariable int workspace) {
		return this.serviceWorkspace.delete(workspace)
				? new ResponseEntity<>("It has been delete properly", HttpStatus.OK)
				: new ResponseEntity<>("There was a problem!", HttpStatus.OK);
	}

}
