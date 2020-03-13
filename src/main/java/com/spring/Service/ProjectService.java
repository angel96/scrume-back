package com.spring.Service;

import java.util.List;
import java.lang.reflect.Type;


import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.jsoup.HttpStatusException;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;

import com.spring.CustomObject.ProjectDto;
import com.spring.Model.Project;
import com.spring.Model.Team;
import com.spring.Repository.ProjectRepository;

@Service
@Transactional
public class ProjectService extends AbstractService{
	
	@Autowired
	private ProjectRepository repository;
	
	@Autowired
	private TeamService teamService;
	
	public List<Project> findAll() {
		return repository.findAll();
	}
	
	public List<ProjectDto> findProjectByTeamId(Integer id) {
		Team team = teamService.findOne(id);
		List<Project> lista = repository.findByTeam(team);
		
		ModelMapper mapper = new ModelMapper();
		
		Type listType = new TypeToken<List<ProjectDto>>() {}.getType();
		return mapper.map(lista, listType);
	}
	
	public Project findOne(Integer id) {
		return repository.findById(id).orElse(null);
	}
	
	public Project save(Project p) {
		return repository.save(p);
	}
	
	public boolean delete(Integer id) {
		boolean checkIfExists = this.repository.existsById(id);
		if (checkIfExists) {
			repository.deleteById(id);
		}
		return checkIfExists;
	}
}
