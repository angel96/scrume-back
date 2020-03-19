package com.spring.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.spring.CustomObject.SprintDto;
import com.spring.CustomObject.SprintEditDto;
import com.spring.CustomObject.SprintStatisticsDto;
import com.spring.Model.Project;
import com.spring.Model.Sprint;
import com.spring.Model.Task;
import com.spring.Model.User;
import com.spring.Repository.SprintRepository;

@Service
@Transactional
public class SprintService extends AbstractService {

	@Autowired
	private SprintRepository sprintRepository;

	@Autowired
	private UserService userService;

	@Autowired
	private ProjectService projectService;

	@Autowired
	private UserRolService userRolService;

	@Autowired
	private WorkspaceService workspaceService;

	@Autowired
	private TaskService taskService;

	public Sprint getOne(int id) {
		return this.sprintRepository.findById(id).orElse(null);
	}

	public SprintStatisticsDto getStatistics(Integer idSprint) {
		User principal = this.userService.getUserByPrincipal();
		SprintStatisticsDto res = new SprintStatisticsDto();
		Sprint sprint = this.getOne(idSprint);
		this.validateSprint(sprint);
		this.validateUserPrincipal(principal, sprint.getProject());
		List<Task> tasksOfSprint = this.taskService.findBySprint(sprint);
		List<Task> completeTasksOfSprint = this.taskService.findCompleteTaskBySprint(sprint);
		Integer totalHP = tasksOfSprint.stream().collect(Collectors.summingInt(Task::getPoints));
		Integer completedHP = completeTasksOfSprint.stream().collect(Collectors.summingInt(Task::getPoints));
		res.setId(sprint.getId());
		res.setStartDate(sprint.getStartDate());
		res.setEndDate(sprint.getEndDate());
		res.setProject(sprint.getProject());
		res.setTotalTasks(tasksOfSprint.size());
		res.setCompletedTasks(completeTasksOfSprint.size());
		res.setTotalHP(totalHP);
		res.setCompletedHP(completedHP);
		return res;
	}

	public SprintDto save(SprintDto sprintCreateDto) {
		ModelMapper modelMapper = new ModelMapper();
		User principal = this.userService.getUserByPrincipal();
		Sprint sprintEntity = modelMapper.map(sprintCreateDto, Sprint.class);
		Project project = this.projectService.findOne(sprintEntity.getProject().getId());
		this.validateProject(project);
		sprintEntity.setProject(project);
		this.validateDates(sprintEntity);
		this.validateUserPrincipal(principal, sprintEntity.getProject());
		Sprint sprintDB = this.sprintRepository.saveAndFlush(sprintEntity);
		this.workspaceService.saveDefaultWorkspace(sprintDB);
		return modelMapper.map(sprintDB, SprintDto.class);
	}

	public SprintEditDto update(SprintEditDto sprintEditDto) {
		ModelMapper modelMapper = new ModelMapper();
		User principal = this.userService.getUserByPrincipal();
		Sprint sprintEntity = this.sprintRepository.findById(sprintEditDto.getId()).orElse(null);
		this.validateSprint(sprintEntity);
		this.validateUserPrincipal(principal, sprintEntity.getProject());
		sprintEntity.setStartDate(sprintEditDto.getStartDate());
		sprintEntity.setEndDate(sprintEditDto.getEndDate());
		this.validateDates(sprintEntity);
		Sprint sprintDB = this.sprintRepository.saveAndFlush(sprintEntity);
		return modelMapper.map(sprintDB, SprintEditDto.class);
	}

	public List<SprintStatisticsDto> listByProject(Integer idProject) {
		User principal = this.userService.getUserByPrincipal();
		Project project = this.projectService.findOne(idProject);
		this.validateProject(project);
		this.validateUserToList(principal, project);
		return this.sprintRepository.findBySprintsOrdered(project).stream().map(x -> this.getStatistics(x))
				.collect(Collectors.toList());
	}

	private void validateUserToList(User principal, Project project) {
		if (principal == null) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "The user must be logged in");
		}

		if (!this.userRolService.isUserOnTeam(principal, project.getTeam())) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,
					"The user must belong to the team of the project");
		}
	}

	private void validateUserPrincipal(User principal, Project project) {
		if (principal == null) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "The user must be logged in");
		}

		if (!this.userRolService.isUserOnTeam(principal, project.getTeam())) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,
					"The user must belong to the team of the project");
		}
		if (!this.userRolService.isAdminOnTeam(principal, project.getTeam())) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "The user must be an admin of the team");
		}

	}

	private void validateProject(Project project) {
		if (project == null) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "The project is not in the database");
		}
	}

	private void validateSprint(Sprint sprint) {
		if (sprint == null) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "The sprint is not in the database");
		}
	}

	private void validateDates(Sprint sprint) {
		if (sprint.getStartDate() == null) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "the start date cannot be null");
		}
		if (sprint.getEndDate() == null) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "the end date cannot be null");
		}
		if (sprint.getEndDate().before(sprint.getStartDate())) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
					"The end date of the sprint must be later than the start date");
		}
		if (!this.areValidDates(sprint.getStartDate(), sprint.getEndDate(), sprint.getProject(), sprint.getId())) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Sprint dates overlap with an existing sprint");
		}

	}

	public boolean areValidDates(Date startDate, Date endDate, Project project, Integer idSprint) {
		boolean res;
		if (this.sprintRepository.areValidDates(startDate, endDate, project, idSprint) == 0) {
			res = true;
		} else {
			res = false;
		}
		return res;
	}

	public boolean areValidDates(SprintDto sprintDto) {
		Date startDate = sprintDto.getStartDate();
		Date endDate = sprintDto.getEndDate();
		Project project = this.projectService.findOne(sprintDto.getProject().getId());
		return areValidDates(startDate, endDate, project, 0);
	}

	public void flush() {
		sprintRepository.flush();
	}
}
