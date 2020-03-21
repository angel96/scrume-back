package com.spring.Service;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spring.CustomObject.EstimationDto;
import com.spring.Model.Estimation;
import com.spring.Model.Task;
import com.spring.Model.User;
import com.spring.Repository.EstimationRepository;


@Service
@Transactional
public class EstimationService extends AbstractService {

	@Autowired
	private EstimationRepository estimationRepository;

	@Autowired
	private UserService userService;

	@Autowired
	private TaskService taskService;
	
	public Estimation save(EstimationDto estimationDto) {
		User principal = this.userService.getUserByPrincipal();
		Task task = this.taskService.findOne(estimationDto.getTask());
		Estimation estimationEntity = new Estimation(estimationDto.getPoints(), principal, task);
		return this.estimationRepository.save(estimationEntity);
	}
	
	public void flush() {
		estimationRepository.flush();
	}
}