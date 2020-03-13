package com.spring.Service;

import java.util.Collection;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spring.Model.Project;
import com.spring.Repository.ProjectRepository;

@Service
@Transactional
public class ProjectService extends AbstractService{
	
	@Autowired
	private ProjectRepository repository;
	
	public List<Project> findAll() {
		return repository.findAll();
	}
	
	public Collection<Project> findProjectByTeamId(Integer id) {
		return repository.findProjectsByTeamId(id);
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
