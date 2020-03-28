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

import com.spring.CustomObject.EstimationDto;
import com.spring.CustomObject.ListAllTaskByProjectDto;
import com.spring.CustomObject.TaskDto;
import com.spring.CustomObject.TaskEditDto;
import com.spring.CustomObject.UserProjectWorkspaceFromTaskDto;
import com.spring.Model.Task;
import com.spring.Service.EstimationService;
import com.spring.Service.TaskService;

@RestController
@RequestMapping("api/task")
public class TaskApiController extends AbstractApiController {
	@Autowired
	private TaskService taskService;
	
	@Autowired
	private EstimationService estimationService;
	
	@GetMapping("user/{id}")
	public List<UserProjectWorkspaceFromTaskDto> findTaskByUser(@PathVariable int id){
		super.logger.info("GET /api/task/user/" + id);
		return this.taskService.findTaskByUser(id);
	}
	
	@GetMapping("/{idTask}")
	public TaskDto show(@PathVariable int idTask) {
		super.logger.info("GET /api/task/" + idTask);
		return this.taskService.find(idTask);
	}
	
	@PostMapping("/{idProject}")
	public TaskDto save(@PathVariable int idProject, @RequestBody TaskDto task) {
		super.logger.info("POST /api/task/" + idProject);
		return this.taskService.save(task, idProject);
	}
	@PutMapping("/{idTask}")
	public TaskEditDto update(@PathVariable int idTask, @RequestBody TaskEditDto task) {
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

	@PostMapping("/estimate/{idTask}")
	public EstimationDto estimateTask(@PathVariable int idTask, @RequestBody EstimationDto estimationDto) {
		estimationDto.setTask(idTask);
		super.logger.info("POST /api/task/estimate/" + idTask);
		return this.estimationService.save(estimationDto);
	}
	
}
