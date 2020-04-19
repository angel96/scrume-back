package com.spring.Service;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.spring.CustomObject.EstimationDto;
import com.spring.Model.Estimation;
import com.spring.Model.Task;
import com.spring.Model.Team;
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

	@Autowired
	private UserRolService userRolService;

	public EstimationDto save(EstimationDto estimationDto) {
		User principal = this.userService.getUserByPrincipal();
		Task task = this.taskService.findOne(estimationDto.getTask());
		Team team = task.getProject().getTeam();
		this.validateTask(task);
		this.validatePrincipal(principal);
		this.validatePrincipalTeam(principal, team);
		this.validateJustAnEstimation(principal, task);
		Estimation estimationEntity = new Estimation(estimationDto.getPoints(), principal, task);
		Estimation estimationBD = this.estimationRepository.save(estimationEntity);
		if (this.userRolService.getNumberOfUsersOfTeam(team)
				.equals(this.estimationRepository.getNumberOfEstimatesOfATask(task))) {
			Integer points = this.estimationRepository.getFinalEstimationOfATask(task);
			this.taskService.saveEstimation(task, points);
		}
		return new EstimationDto(estimationBD.getId(), estimationBD.getPoints(), estimationBD.getTask().getId());
	}

	private void validateTask(Task task) {
		if (task.getPoints() != 0) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The task is already estimated");
		}
	}

	private void validateJustAnEstimation(User principal, Task task) {
		if (this.estimationRepository.getNumberOfEstimatesOfAnUser(principal, task) > 0) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "The user can only estimate the task once");
		}
	}

	private void validatePrincipal(User principal) {
		if (principal == null) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "The user must be logged in");
		}
	}

	private void validatePrincipalTeam(User principal, Team team) {
		if (!this.userRolService.isUserOnTeam(principal, team)) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "The user must belong to the team");
		}
	}

	public void flush() {
		estimationRepository.flush();
	}

	public Estimation findByTaskAndUser(Task task, User user) {
		return this.estimationRepository.findByTask(task, user).orElse(null);
	}
}