package com.spring.Service;

import java.lang.reflect.Type;
import java.util.ArrayList;
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
	private BoxService boxService;

	@Autowired
	private UserRolService userRolService;

	public static final String BASIC_PLAN = "BASIC";
	public static final String STANDARD_PLAN = "STANDARD";

	public List<ProjectDto> findProjectByTeamId(Integer id) {
		User principal = this.userService.getUserByPrincipal();
		Team team = teamService.findOne(id);
		validateTeam(team);
		validateSeeProject(team, principal);

		List<Project> lista = projectRepository.findByTeam(team);

		if (this.boxService.getMinimumBoxOfATeam(team.getId()).getName() == null) {
			this.validateBoxPrivileges(team, null);
		} else if (this.boxService.getMinimumBoxOfATeam(team.getId()).getName().equals(BASIC_PLAN)) {
			lista = this.getFirstProjectsOfATeam(team, 1);
		} else if (this.boxService.getMinimumBoxOfATeam(team.getId()).getName().equals(STANDARD_PLAN)) {
			lista = this.getFirstProjectsOfATeam(team, 3);
		}
		ModelMapper mapper = new ModelMapper();

		Type listType = new TypeToken<List<ProjectDto>>() {
		}.getType();
		return mapper.map(lista, listType);
	}

	public Project findOne(Integer id) {
		return projectRepository.findById(id).orElseThrow(
				() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "The requested project doesn´t exists"));
	}

	public ProjectDto getOne(Integer idProject) {
		User principal = this.userService.getUserByPrincipal();
		ModelMapper mapper = new ModelMapper();
		Project projectEntity = this.projectRepository.getOne(idProject);
		validateProject(projectEntity);
		validateSeeProject(projectEntity.getTeam(), principal);
		this.validateBoxPrivileges(projectEntity.getTeam(), projectEntity);
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
		this.validateBoxPrivileges(projectDB.getTeam(), projectDB);
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
		this.validateBoxPrivilegesToSave(team);
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
		this.validateBoxPrivileges(projectDB.getTeam(), projectDB);
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
		if (!this.userRolService.isAdminOnTeam(principal, team)) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,
					"The user must be an admin of the team to edit the project");
		}
	}

	private List<Project> getFirstProjectsOfATeam(Team team, Integer number) {
		List<Project> res = new ArrayList<>();
		List<Project> projects = this.projectRepository.getFirstProjectOfATeam(team);
		if (projects.size() >= number) {
			Integer i = 0;
			while (i < number) {
				res.add(projects.get(i));
				i++;
			}
		}else {
			res = projects;
		}
		return res;
	}

	private void validateBoxPrivilegesToSave(Team team) {
		if (this.boxService.getMinimumBoxOfATeam(team.getId()).getName() == null) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,
					"There is no payment record in the database, so you cannot manage the project");
		}
		if (this.boxService.getMinimumBoxOfATeam(team.getId()).getName().equals(BASIC_PLAN)
				&& !this.getFirstProjectsOfATeam(team, 1).isEmpty()) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,
					"The minimum team box is basic, so you can only manage one project");
		}
		if (this.boxService.getMinimumBoxOfATeam(team.getId()).getName().equals(STANDARD_PLAN)
				&& this.getFirstProjectsOfATeam(team, 3).size() > 2) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,
					"The minimum team box is standard, so you can only manage three project");
		}
	}

	private void validateBoxPrivileges(Team team, Project project) {
		if (this.boxService.getMinimumBoxOfATeam(team.getId()).getName() == null) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,
					"There is no payment record in the database, so you cannot manage projects");
		}
		if (this.boxService.getMinimumBoxOfATeam(team.getId()).getName().equals(BASIC_PLAN)
				&& !this.getFirstProjectsOfATeam(team, 1).contains(project)) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,
					"The minimum team box is basic, so you can only manage the first of your projects");
		}
		if (this.boxService.getMinimumBoxOfATeam(team.getId()).getName().equals(STANDARD_PLAN)
				&& !this.getFirstProjectsOfATeam(team, 3).contains(project)) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,
					"The minimum team box is standard, so you are only allowed to manage your first three projects");
		}
	}
}