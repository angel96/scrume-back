package com.spring.Service;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spring.Model.Task;
import com.spring.Repository.TaskRepository;

@Service
@Transactional
public class TaskService extends AbstractService {

	@Autowired
	private TaskRepository repository;

	public Task findOne(int task) {
		return this.repository.findById(task).orElse(null);
	}

}
