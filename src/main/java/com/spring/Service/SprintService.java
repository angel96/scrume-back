package com.spring.Service;

import java.util.Date;

import javax.transaction.Transactional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import com.spring.CustomObject.SprintCreateDto;
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
		this.validatePreviousSprintCreate(sprintEntity);
		this.validateUserPrincipal(principal, sprintEntity.getProject());
		Sprint sprintDB = this.sprintRepository.save(sprintEntity);
		return modelMapper.map(sprintDB, SprintCreateDto.class);
	}

	public SprintEditDto update(SprintEditDto sprintEditDto) throws Exception {
		ModelMapper modelMapper = new ModelMapper();
		User principal = this.userService.getUserByPrincipal();
		Sprint sprintEntity = this.sprintRepository.findById(sprintEditDto.getId()).orElse(null);
		this.validateSprint(sprintEntity);
		System.out.println(principal);
		System.out.println(sprintEntity);
		System.out.println(sprintEntity.getProject().getTeam());
		this.validateUserPrincipal(principal, sprintEntity.getProject());
		sprintEntity.setStartDate(sprintEditDto.getStartDate());
		sprintEntity.setEndDate(sprintEditDto.getEndDate());
		this.validateDates(sprintEntity);
		this.validatePreviousSprintUpdate(sprintEntity);
		Sprint sprintDB = this.sprintRepository.save(sprintEntity);
		return modelMapper.map(sprintDB, SprintEditDto.class);
	}

	public Sprint getPreviousSprintCreate(Project project) {
		return this.sprintRepository.getPreviousSprintCreate(project);
	}
	
	public Sprint getPreviousSprintUpdate(Sprint sprint) {
		return this.sprintRepository.getPreviousSprintUpdate(sprint);
	}
	
	private void validateUserPrincipal(User principal, Project project) throws Exception{
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
	
	private void validateProject(Project project) throws Exception{
		if(project == null) {
			throw new HttpClientErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "The project cannot be null");
		}
	}
	
	private void validateSprint(Sprint sprint) throws Exception{
		if(sprint == null) {
			throw new HttpClientErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "The sprint cannot be null");
		}
	}
	
	private void validateDates(Sprint sprint) throws Exception{
		if(sprint.getStartDate() == null) {
			throw new HttpClientErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "the start date cannot be null");
		}
		if(sprint.getEndDate() == null) {
			throw new HttpClientErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "the end date cannot be null");
		}
		if(sprint.getEndDate().before(sprint.getStartDate())) {
			throw new HttpClientErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "The end date of the sprint must be later than the start date");
		}
	}
	
	private void validatePreviousSprintCreate(Sprint sprint) throws Exception{
		Sprint previousSprint = this.getPreviousSprintCreate(sprint.getProject());
		if(previousSprint != null) {
			if(previousSprint.getEndDate().after(new Date())) {
				throw new HttpClientErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "Cannot start a sprint if the previous one has not finished");
			}
		}
	}
	
	private void validatePreviousSprintUpdate(Sprint sprint) throws Exception{
		Sprint previousSprint = this.getPreviousSprintUpdate(sprint);
		if(previousSprint != null) {
			if(previousSprint.getEndDate().after(new Date())) {
				throw new HttpClientErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "Cannot start a sprint if the previous one has not finished");
			}
		}
	}
}
