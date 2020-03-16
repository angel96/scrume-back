package com.spring.API;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.spring.CustomObject.TaskDto;
import com.spring.Model.Task;
import com.spring.Service.TaskService;

@RestController
@RequestMapping("api/task")
public class TaskApiController extends AbstractApiController {
	@Autowired
	private TaskService taskService;
	
	@GetMapping("/show")
	public Task show(@RequestParam(value = "id") int id) {
		return this.taskService.findOne(id);
	}
	
	@PostMapping("/save")
	public TaskDto save(@RequestParam(value = "id") int projectId, @RequestBody TaskDto task) {
		return this.taskService.save(task, projectId);
	}
	@PutMapping("/update")
	public TaskDto update(@RequestParam(value = "id") int taskId, @RequestBody TaskDto task) {
		return this.taskService.update(task, taskId);
	}
	
	@PostMapping("/delete")
	public void delete(@RequestParam(value = "id") int taskId) {
		this.taskService.delete(taskId);
	}

}
