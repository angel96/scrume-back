package com.spring.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collection;
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
import com.spring.Model.Team;
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
	private BoxService boxService;
	
	@Autowired
	private ProjectService projectService;

	@Autowired
	private UserRolService userRolService;

	@Autowired
	private WorkspaceService workspaceService;

	@Autowired
	private TaskService taskService;

	public Sprint getOne(int id) {
		return this.sprintRepository.findById(id).orElseThrow(
				() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "The requested sprint not exists"));
	}

	public Collection<Sprint> getActivesSprints(){
		return this.sprintRepository.getActivesSprints();
	}
	public SprintStatisticsDto getStatistics(Integer idSprint) {
		User principal = this.userService.getUserByPrincipal();
		SprintStatisticsDto res = new SprintStatisticsDto();
		Sprint sprint = this.getOne(idSprint);
		LocalDateTime validDate = sprint.getStartDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
		validDate = validDate.plusDays(30);
		this.validateBoxPrivileges(sprint.getProject().getTeam(), sprint, validDate);
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
		this.validateBoxPrivilegesToSave(project.getTeam());
		sprintEntity.setProject(project);
		this.validateDates(sprintEntity);
		this.validateUserPrincipal(principal, sprintEntity.getProject());
		this.validateUserPrincipalIsAdmin(principal, sprintEntity.getProject());
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
		this.validateUserPrincipalIsAdmin(principal, sprintEntity.getProject());

		sprintEntity.setStartDate(sprintEditDto.getStartDate());
		sprintEntity.setEndDate(sprintEditDto.getEndDate());
		this.validateDates(sprintEntity);
		LocalDateTime validDate = sprintEntity.getStartDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
		validDate = validDate.plusDays(30);
		this.validateBoxPrivileges(sprintEntity.getProject().getTeam(), sprintEntity, validDate);
		Sprint sprintDB = this.sprintRepository.saveAndFlush(sprintEntity);
		return modelMapper.map(sprintDB, SprintEditDto.class);
	}

	public List<SprintStatisticsDto> listByProject(Integer idProject) {
		User principal = this.userService.getUserByPrincipal();
		Project project = this.projectService.findOne(idProject);
		this.validateProject(project);
		this.validateUserPrincipal(principal, project);
		List<Integer> sprints = this.sprintRepository.findBySprintsOrdered(project);
		if (this.boxService.getMinimumBoxOfATeam(project.getTeam().getId()).getName() == null) {
			sprints = new ArrayList<>();
		}
		else if(this.boxService.getMinimumBoxOfATeam(project.getTeam().getId()).getName() == "BASIC") {
			sprints = this.getFirstSprintsOfATeam(project.getTeam(), 1).stream().map(x -> x.getId()).collect(Collectors.toList());
		}
		return sprints.stream().map(x -> this.getStatistics(x))
				.collect(Collectors.toList());
	}

	private void validateUserPrincipal(User principal, Project project) {
		if (principal == null) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "The user must be logged in");
		}

		if (!this.userRolService.isUserOnTeam(principal, project.getTeam())) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,
					"The user must belong to the team of the project");
		}
	}
	
	private void validateUserPrincipalIsAdmin(User principal, Project project) {
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
		if (sprint.getStartDate().before(Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant()))) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Dates cannot be earlier than the current one");
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
		if(idSprint == null) {
			idSprint = 0;
		}
		if (this.sprintRepository.areValidDates(startDate, endDate, project, idSprint) == 0) {
			res = true;
		} else {
			res = false;
		}
		return res;
	}

	public boolean areValidDates(SprintDto sprintDto) {
		User principal = this.userService.getUserByPrincipal();
		Date startDate = sprintDto.getStartDate();
		Date endDate = sprintDto.getEndDate();
		Project project = this.projectService.findOne(sprintDto.getProject().getId());
		this.validateProject(project);
		this.validateUserPrincipal(principal, project);
		return areValidDates(startDate, endDate, project, 0);
	}

	private List<Sprint> getFirstSprintsOfATeam(Team team, Integer number) {
		List<Sprint> res = new ArrayList<>();
		List<Sprint> sprints = this.sprintRepository.getFirstSprintsOfATeam(team);
		if(sprints.size() >= number) {
			Integer i = 0;
			while (i < number) {
				res.add(sprints.get(i));
				i++;
			}
		}
		return res;
	}
	
	private void validateBoxPrivilegesToSave(Team team) {
		if (this.boxService.getMinimumBoxOfATeam(team.getId()).getName() == null) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,
					"There is no payment record in the database, so you cannot manage sprints");
		}
		if (this.boxService.getMinimumBoxOfATeam(team.getId()).getName() == "BASIC" 
				&& this.getFirstSprintsOfATeam(team, 1).size() > 0) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,
					"The minimum team box is basic, so you can only manage a 30-day sprint");
		}
	}
	
	private void validateBoxPrivileges(Team team, Sprint sprint, LocalDateTime validDate) {
		if (this.boxService.getMinimumBoxOfATeam(team.getId()).getName() == null) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,
					"There is no payment record in the database, so you cannot manage sprints");
		}
		
		if (this.boxService.getMinimumBoxOfATeam(team.getId()).getName() == "BASIC" 
				&& (!this.getFirstSprintsOfATeam(team, 1).contains(sprint) || validDate.isBefore(LocalDateTime.now(ZoneId.systemDefault())))) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,
					"The minimum team box is basic, so you can only manage a 30-day sprint");
		}
	}
	

	public void flush() {
		sprintRepository.flush();
	}
}
