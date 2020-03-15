package com.spring.Service;

import java.lang.reflect.Type;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import com.spring.CustomObject.SprintCreateDto;
import com.spring.CustomObject.SprintDatesDto;
import com.spring.CustomObject.SprintDto;
import com.spring.CustomObject.SprintEditDto;
import com.spring.Model.Project;
import com.spring.Model.Sprint;
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
	
	public SprintCreateDto save(SprintCreateDto sprintCreateDto) throws Exception {
		ModelMapper modelMapper = new ModelMapper();
		User principal = this.userService.getUserByPrincipal();
		Sprint sprintEntity = modelMapper.map(sprintCreateDto, Sprint.class);
		Project project = this.projectService.findOne(sprintEntity.getProject().getId());
		this.validateProject(project);
		sprintEntity.setProject(project);
		this.validateDates(sprintEntity);
		this.validateUserPrincipal(principal, sprintEntity.getProject());
		Sprint sprintDB = this.sprintRepository.save(sprintEntity);
		return modelMapper.map(sprintDB, SprintCreateDto.class);
	}

	public SprintEditDto update(SprintEditDto sprintEditDto) throws Exception {
		ModelMapper modelMapper = new ModelMapper();
		User principal = this.userService.getUserByPrincipal();
		Sprint sprintEntity = this.sprintRepository.findById(sprintEditDto.getId()).orElse(null);
		this.validateSprint(sprintEntity);
		this.validateUserPrincipal(principal, sprintEntity.getProject());
		sprintEntity.setStartDate(sprintEditDto.getStartDate());
		sprintEntity.setEndDate(sprintEditDto.getEndDate());
		this.validateDates(sprintEntity);
		Sprint sprintDB = this.sprintRepository.save(sprintEntity);
		return modelMapper.map(sprintDB, SprintEditDto.class);
	}
	
	public List<SprintDto> listByProject(Integer idProject) {
		ModelMapper modelMapper = new ModelMapper();
		User principal = this.userService.getUserByPrincipal();
		Project project = this.projectService.findOne(idProject);
		this.validateProject(project);
		this.validateUserToList(principal, project);
		List<Sprint> sprints = this.sprintRepository.findBySprintsOrdered(project);
		Type listType = new TypeToken<List<SprintDto>>(){}.getType();
		return modelMapper.map(sprints,listType);
	}
	
	private void validateUserToList(User principal, Project project) {
		if(principal == null) {
			throw new HttpClientErrorException(HttpStatus.UNAUTHORIZED, "The user must be logged in");
		}

		if(!this.userRolService.isUserOnTeam(principal, project.getTeam())) {
			throw new HttpClientErrorException(HttpStatus.UNAUTHORIZED, "The user must belong to the team of the project");
		}
	}

	private void validateUserPrincipal(User principal, Project project){
		if(principal == null) {
			throw new HttpClientErrorException(HttpStatus.UNAUTHORIZED, "The user must be logged in");
		}

		if(!this.userRolService.isUserOnTeam(principal, project.getTeam())) {
			throw new HttpClientErrorException(HttpStatus.UNAUTHORIZED, "The user must belong to the team of the project");
		}
		if(!this.userRolService.isAdminOnTeam(principal, project.getTeam())) {
			throw new HttpClientErrorException(HttpStatus.UNAUTHORIZED, "The user must be an admin of the team");
		}
		
	}
	
	private void validateProject(Project project){
		if(project == null) {
			throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "The project is not in the database");
		}
	}
	
	private void validateSprint(Sprint sprint){
		if(sprint == null) {
			throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "The sprint is not in the database");
		}
	}
	
	private void validateDates(Sprint sprint){
		if(sprint.getStartDate() == null) {
			throw new HttpClientErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "the start date cannot be null");
		}
		if(sprint.getEndDate() == null) {
			throw new HttpClientErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "the end date cannot be null");
		}
		if(sprint.getEndDate().before(sprint.getStartDate())) {
			throw new HttpClientErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "The end date of the sprint must be later than the start date");
		}
		if(this.areValidDates(sprint.getStartDate(), sprint.getEndDate(), sprint.getProject())) {
			throw new HttpClientErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "Sprint dates overlap with an existing sprint");
		}
		
	}

	public boolean areValidDates(Date startDate, Date endDate, Project project) {
		boolean res;
		if(this.sprintRepository.areValidDates(startDate, endDate, project) == 0) {
			res = true;
		}else {
			res = false;
		}
		return res;
	}

	public boolean areValidDates(SprintDatesDto sprintDatesDto) {
		Date startDate = sprintDatesDto.getStartDate();
		Date endDate = sprintDatesDto.getEndDate();
		Project project = this.projectService.findOne(sprintDatesDto.getIdProject());
		return areValidDates(startDate, endDate, project);
	}

}
