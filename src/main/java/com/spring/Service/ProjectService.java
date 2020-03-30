package com.spring.Service;

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.List;

import javax.transaction.Transactional;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
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
	private ProjectRepository projectRepository;

	@Autowired
	private TeamService teamService;

	@Autowired
	private UserService userService;

	@Autowired
	private UserRolService userRolService;


	public List<ProjectDto> findProjectByTeamId(Integer id) {
		User principal = this.userService.getUserByPrincipal();
		Team team = teamService.findOne(id);
		validateTeam(team);
		validateSeeProject(team, principal);
		List<Project> lista = projectRepository.findByTeam(team);

		ModelMapper mapper = new ModelMapper();

		Type listType = new TypeToken<List<ProjectDto>>() {
		}.getType();
		return mapper.map(lista, listType);
	}

	public Project findOne(Integer id) {
		return projectRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "The requested project doesn´t exists"));
	}

	public ProjectDto getOne(Integer idProject) {
		User principal = this.userService.getUserByPrincipal();
		ModelMapper mapper = new ModelMapper();
		Project projectEntity = this.projectRepository.getOne(idProject);
		validateProject(projectEntity);
		validateSeeProject(projectEntity.getTeam(), principal);
		return mapper.map(projectEntity, ProjectDto.class);
	}

	public ProjectDto update(ProjectDto projectDto, Integer idProject) {
		User principal = this.userService.getUserByPrincipal();
		ModelMapper mapper = new ModelMapper();
		Project projectEntity = mapper.map(projectDto, Project.class);
		validateProject(projectEntity);
		Project projectDB = this.projectRepository.getOne(idProject);
		validateProject(projectDB);
		Integer idTeam = projectEntity.getTeam().getId();
		Team team = teamService.findOne(idTeam);
		validateTeam(team);
		validateEditPermission(team, principal);
		projectDB.setTeam(team);
		projectDB.setName(projectEntity.getName());
		projectDB.setDescription(projectEntity.getDescription());
		projectRepository.save(projectDB);
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
		projectRepository.save(projectDB);
		return mapper.map(projectDB, ProjectDto.class);
	}

	public void delete(Integer id) {
		User principal = this.userService.getUserByPrincipal();
		boolean checkIfExists = this.projectRepository.existsById(id);
		Project projectDB = this.projectRepository.getOne(id);
		validateProject(projectDB);
		validateEditPermission(projectDB.getTeam(), principal);
		if (checkIfExists) {
			projectRepository.deleteById(id);
		}
	}
	
	public void flush() {
		projectRepository.flush();
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
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,
					"The user must belong to the team to see the project");

		}
	}

	private void validateEditPermission(Team team, User principal) {
		if (!this.userRolService.isUserOnTeam(principal, team)) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,
					"The user must belong to the team to edit the project");
		}
		Collection<Integer> membersTeam = this.userRolService.findIdUsersByTeam(team);
		if (!membersTeam.contains(principal.getId())) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,
					"The user must belong to the team to edit the project");
		}
//		if (!this.userRolService.isAdminOnTeam(principal, team)) {
//			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,
//					"The user must be an admin of the team to edit the project");
//		}
	}

}