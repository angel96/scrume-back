package com.spring.Scrume;

import java.util.stream.Stream;

import javax.persistence.EntityNotFoundException;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.server.ResponseStatusException;

import com.spring.CustomObject.ProjectDto;
import com.spring.Service.ProjectService;
import com.spring.Service.TeamService;

public class ProjectServiceTest extends AbstractTest {
	
	@Autowired
	private ProjectService projectService;
	
	@Autowired
	private TeamService teamService;
	
	@Test
	public void findProjectsByTeamIdTest() throws Exception {
		Object[][] objects = {
				{ "testuser@gmail.com", super.entities().get("team1"), null }, //Caso positivo
				{ "hola", super.entities().get("team1"), UsernameNotFoundException.class }}; //Caso negativo
		
		Stream.of(objects).forEach(x -> driverFindProjects((String) x[0], (Integer) x[1], (Class<?>) x[2]));
		}
	
	protected void driverFindProjects(String user, Integer teamId, Class<?> expected) {
		Class<?> caught = null;
		try {
			super.authenticateOrUnauthenticate(user);
			this.projectService.findProjectByTeamId(teamId);
			this.projectService.flush();
			super.authenticateOrUnauthenticate(null);
			
		} catch (Exception oops) {
			caught = oops.getClass();
		}
		super.checkExceptions(expected, caught);
	}
	
	@Test
	public void projectTestSave() throws Exception {
		ProjectDto projectDto = new ProjectDto();
		projectDto.setName("Name 1");
		projectDto.setDescription("Description 1");
		Object[][] objects = {
				{ "testuser@gmail.com", projectDto, super.entities().get("team1"), null }, //Caso positivo
				{ "hola", projectDto, super.entities().get("team1"), UsernameNotFoundException.class }}; //Caso negativo
		
		Stream.of(objects).forEach(x -> driverTestSave((String) x[0], (ProjectDto) x[1], (Integer) x[2], (Class<?>) x[3]));
		}

	protected void driverTestSave(String user, ProjectDto projectDto, Integer teamId, Class<?> expected) {
		Class<?> caught = null;
		try {
			super.authenticateOrUnauthenticate(user);
			projectDto.setTeam(this.teamService.findOne(teamId));
			this.projectService.save(projectDto);
			this.projectService.flush();
			super.authenticateOrUnauthenticate(null);
			
		} catch (Exception oops) {
			caught = oops.getClass();
		}
		super.checkExceptions(expected, caught);
	}
	
	@Test
	public void projectTestUpdate() throws Exception {
		ProjectDto projectDto = new ProjectDto();
		projectDto.setId(super.entities().get("project1"));
		projectDto.setName("Name 1");
		projectDto.setDescription("Description 1");
		Object[][] objects = {
				{ "testuser@gmail.com", projectDto, super.entities().get("team1"), super.entities().get("project1"), "NombreActualizado", null }, //Caso positivo
				{ "hola", projectDto, super.entities().get("team65"), super.entities().get("project1"), "NombreActualizado", UsernameNotFoundException.class }}; //Caso negativo
		
		Stream.of(objects).forEach(x -> driverTestUpdate((String) x[0], (ProjectDto) x[1], (Integer) x[2], (Integer) x[3], (String) x[4], (Class<?>) x[5]));
		}
	
	protected void driverTestUpdate(String user, ProjectDto projectDto, Integer teamId, Integer projectId, String nameUpdated, Class<?> expected) {
		Class<?> caught = null;
		try {
			super.authenticateOrUnauthenticate(user);
			projectDto.setTeam(this.teamService.findOne(teamId));
			this.projectService.save(projectDto);
			projectDto.setName(nameUpdated);
			this.projectService.update(projectDto, projectId);
			this.projectService.flush();
			super.authenticateOrUnauthenticate(null);
			
		} catch (Exception oops) {
			caught = oops.getClass();
		}
		super.checkExceptions(expected, caught);
	}

	@Test
	public void projectTestDelete() throws Exception {

		Object[][] objects = {
				{ "testuser@gmail.com", super.entities().get("project1") , null}, //Caso positivo
				{ "hola", super.entities().get("project1"), UsernameNotFoundException.class }, //Caso negativo
				{ "testuser@gmail.com", super.entities().get("project1") , EntityNotFoundException.class}, //Caso positivo
				{ "angdellun@gmail.com", super.entities().get("project2"), ResponseStatusException.class }}; //Caso negativo
			
		Stream.of(objects).forEach(x -> driverTestDelete((String) x[0], (Integer) x[1], (Class<?>) x[2]));
	}
	
		protected void driverTestDelete(String user, Integer projectId, Class<?> expected) {
			Class<?> caught = null;
			try {
				super.authenticateOrUnauthenticate(user);
				this.projectService.getOne(projectId);
				this.projectService.delete(projectId);
				this.projectService.flush();
				super.authenticateOrUnauthenticate(null);
				
			} catch (Exception oops) {
				caught = oops.getClass();
			}
			super.checkExceptions(expected, caught);
		}
}
