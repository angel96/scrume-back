package com.spring.Service;

import java.lang.reflect.Type;
import java.util.List;

import javax.transaction.Transactional;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.server.ResponseStatusException;

import com.spring.CustomObject.ProjectDto;
import com.spring.Model.Project;
import com.spring.Model.Team;
import com.spring.Model.User;
import com.spring.Repository.ProjectRepository;

@Service
@Transactional
public class ProjectService extends AbstractService {

	@Autowired
	private ProjectRepository repository;

	@Autowired
	private TeamService teamService;

	@Autowired
	private UserService userService;

	@Autowired
	private UserRolService userRolService;


	public List<Project> findAll() {
		return repository.findAll();
	}

	public List<ProjectDto> findProjectByTeamId(Integer id) {
		User principal = this.userService.getUserByPrincipal();
		Team team = teamService.findOne(id);
		validateTeam(team);
		validateSeeProject(team, principal);
		List<Project> lista = repository.findByTeam(team);

		ModelMapper mapper = new ModelMapper();

		Type listType = new TypeToken<List<ProjectDto>>() {
		}.getType();
		return mapper.map(lista, listType);
	}

	public Project findOne(Integer id) {
		return repository.findById(id).orElse(null);
	}

	public ProjectDto getOne(Integer idProject) {
		User principal = this.userService.getUserByPrincipal();
		ModelMapper mapper = new ModelMapper();
		Project projectEntity = this.repository.getOne(idProject);
		validateProject(projectEntity);
		validateSeeProject(projectEntity.getTeam(), principal);
		return mapper.map(projectEntity, ProjectDto.class);
	}

	public ProjectDto update(ProjectDto projectDto, Integer idProject) {
		User principal = this.userService.getUserByPrincipal();
		ModelMapper mapper = new ModelMapper();
		Project projectEntity = mapper.map(projectDto, Project.class);
		validateProject(projectEntity);
		Project projectDB = this.repository.getOne(idProject);
		Integer idTeam = projectEntity.getTeam().getId();
		Team team = teamService.findOne(idTeam);
		validateTeam(team);
		validateEditPermission(team, principal);
		projectDB.setTeam(team);
		projectDB.setName(projectEntity.getName());
		projectDB.setDescription(projectEntity.getDescription());
		repository.save(projectDB);
		return mapper.map(projectDB, ProjectDto.class);
	}

	public ProjectDto save(ProjectDto projectDto) {
		ModelMapper mapper = new ModelMapper();
		User principal = this.userService.getUserByPrincipal();
		Project projectEntity = mapper.map(projectDto, Project.class);
		Integer idTeam = projectEntity.getTeam().getId();
		Team team = teamService.findOne(idTeam);
		validateEditPermission(team, principal);
		Project projectDB = new Project();
		projectDB.setTeam(team);
		projectDB.setName(projectEntity.getName());
		projectDB.setDescription(projectEntity.getDescription());
		repository.save(projectDB);
		return mapper.map(projectDB, ProjectDto.class);
	}

	public boolean delete(Integer id) {
		User principal = this.userService.getUserByPrincipal();
		boolean checkIfExists = this.repository.existsById(id);
		Project projectDB = this.repository.getOne(id);
		validateEditPermission(projectDB.getTeam(), principal);
		if (checkIfExists) {
			repository.deleteById(id);
		}
		return checkIfExists;
	}

	private void validateProject(Project project) {
		if (project == null) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "the project is not in the database");
		}
	}

	private void validateTeam(Team team) {
		if (team == null) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "the team is not in the database");
		}
	}

	private void validateSeeProject(Team team, User principal) {
		if (!this.userRolService.isUserOnTeam(principal, team)) {
			throw new HttpClientErrorException(HttpStatus.UNAUTHORIZED,
					"The user must belong to the team to see the project");

		}
	}

	private void validateEditPermission(Team team, User principal) {
		if (!this.userRolService.isUserOnTeam(principal, team)) {
			throw new HttpClientErrorException(HttpStatus.UNAUTHORIZED,
					"The user must belong to the team to edit the project");
		}
		if (!this.userRolService.isAdminOnTeam(principal, team)) {
			throw new HttpClientErrorException(HttpStatus.UNAUTHORIZED,
					"The user must be an admin of the team to edit the project");
		}
	}

}