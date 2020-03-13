package com.spring.Service;

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
	
	public SprintCreateDto save(SprintCreateDto sprintCreateDto) throws Exception {
		ModelMapper modelMapper = new ModelMapper();
		User principal = this.userService.getUserByPrincipal();
		this.validateUserPrincipal(principal);
		Sprint sprintEntity = modelMapper.map(sprintCreateDto, Sprint.class);
		Project project = this.projectService.findOne(sprintCreateDto.getProject().getId());
		sprintEntity.setProject(project);
		Sprint sprintDB = this.sprintRepository.save(sprintEntity);
		return modelMapper.map(sprintDB, SprintCreateDto.class);
	}

	public SprintEditDto update(SprintEditDto sprintEditDto) throws Exception {
		ModelMapper modelMapper = new ModelMapper();
		User principal = this.userService.getUserByPrincipal();
		this.validateUserPrincipal(principal);
		Sprint sprintEntity = this.sprintRepository.findById(sprintEditDto.getId()).orElse(null);
		sprintEntity.setStartDate(sprintEditDto.getStartDate());
		sprintEntity.setEndDate(sprintEditDto.getEndDate());
		Sprint sprintDB = this.sprintRepository.save(sprintEntity);
		return modelMapper.map(sprintDB, SprintEditDto.class);
	}

	private void validateUserPrincipal(User principal) throws Exception{
		if(principal == null) {
			throw new HttpClientErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "The user must be logged in");
		}
	}
}
