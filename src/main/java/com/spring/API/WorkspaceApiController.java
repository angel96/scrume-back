package com.spring.API;

import java.util.Collection;
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

import com.spring.CustomObject.LastWorkspaceDto;
import com.spring.CustomObject.SprintWithWorkspacesDto;
import com.spring.CustomObject.WorkspaceEditDto;
import com.spring.CustomObject.WorkspaceSprintListDto;
import com.spring.CustomObject.WorkspaceWithColumnsDto;
import com.spring.Model.Workspace;
import com.spring.Service.WorkspaceService;

@RestController
@RequestMapping("/api/workspace")
public class WorkspaceApiController extends AbstractApiController {

	@Autowired
	private WorkspaceService serviceWorkspace;

	@GetMapping("/list/sprint/{sprint}")
	public List<WorkspaceSprintListDto> listBySprint(@PathVariable int sprint) {
		super.logger.info("GET /api/workspace/list/" + sprint);
		return serviceWorkspace.findWorkspacesBySprint(sprint);
	}

	@GetMapping("/list/{team}")
	public Collection<Workspace> list(@PathVariable int team) {
		super.logger.info("GET /api/workspace/list/" + team);
		return serviceWorkspace.findWorkspacesByTeam(team);
	}

	@GetMapping("/{workspace}")
	public WorkspaceWithColumnsDto get(@PathVariable int workspace) {
		super.logger.info("GET /api/workspace/" + workspace);
		return this.serviceWorkspace.findWorkspaceWithColumns(workspace);
	}

	@PostMapping
	public Workspace save(@RequestBody WorkspaceEditDto workspace) {
		super.logger.info("POST /api/workspace");
		return this.serviceWorkspace.save(0, workspace);
	}

	@PutMapping("/{workspace}")
	public Workspace save(@PathVariable int workspace, @RequestBody WorkspaceEditDto workspaceDto) {
		super.logger.info("PUT /api/workspace/" + workspace);
		return this.serviceWorkspace.save(workspace, workspaceDto);
	}

	@DeleteMapping("/{workspace}")
	public void delete(@PathVariable int workspace) {
		super.logger.info("DELETE /api/workspace/" + workspace);
		this.serviceWorkspace.delete(workspace);
	}

	@GetMapping("/list-todo-columns/{idProject}")
	public Collection<SprintWithWorkspacesDto> listTodoColumnsOfAProject(@PathVariable Integer idProject) {
		super.logger.info("GET /api/list-todo-columns/" + idProject);
		return this.serviceWorkspace.listTodoColumnsOfAProject(idProject);
	}

	@GetMapping("/last-by-project/{project}")
	public LastWorkspaceDto lastWorkspaceModified(@PathVariable Integer project) {
		super.logger.info("GET /api/last-by-project/" + project);
		return this.serviceWorkspace.findWorkspaceLastModifiedByProject(project);
	}

}
