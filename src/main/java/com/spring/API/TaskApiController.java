package com.spring.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.spring.customobject.ListAllTaskByProjectDto;
import com.spring.customobject.TaskDto;
import com.spring.model.Task;
import com.spring.service.TaskService;

@RestController
@RequestMapping("api/task")
public class TaskApiController extends AbstractApiController {
	@Autowired
	private TaskService taskService;
	
	@GetMapping("/{idTask}")
	public Task show(@PathVariable int idTask) {
		super.logger.info("GET /api/task/" + idTask);
		return this.taskService.findOne(idTask);
	}
	
	@PostMapping("/{idProject}")
	public TaskDto save(@PathVariable int idProject, @RequestBody TaskDto task) {
		super.logger.info("POST /api/task/" + idProject);
		return this.taskService.save(task, idProject);
	}
	@PutMapping("/{idTask}")
	public TaskDto update(@PathVariable int idTask, @RequestBody TaskDto task) {
		super.logger.info("PUT /api/task/" + idTask);
		return this.taskService.update(task, idTask);
	}
	
	@DeleteMapping("/{idTask}")
	public void delete(@PathVariable int idTask) {
		super.logger.info("DELETE /api/task/" + idTask);
		this.taskService.delete(idTask);
	}
	
	@GetMapping("/list-by-project/{idProject}")
	public ListAllTaskByProjectDto getAllTasksByProject(@PathVariable int idProject) {
		super.logger.info("GET /api/task/list-by-project/" + idProject);
		return this.taskService.getAllTasksByProject(idProject);
	}

}
