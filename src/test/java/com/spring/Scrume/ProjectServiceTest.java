package com.spring.Scrume;

import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.server.ResponseStatusException;

import com.spring.CustomObject.ProjectDto;
import com.spring.Model.Project;
import com.spring.Repository.ProjectRepository;
import com.spring.Service.ProjectService;
import com.spring.Service.TeamService;

public class ProjectServiceTest extends AbstractTest {
	
	@Autowired
	private ProjectService projectService;
	
	@Autowired
	private TeamService teamService;
	

	@Autowired
	private ProjectRepository projectRepository;
	
	@Test
	public void findProjectsByTeamIdTest() throws Exception {
		Object[][] objects = {
				{ "testuser10@gmail.com", super.entities().get("team5"), null }, 
				{ "testuser12@gmail.com", super.entities().get("team6"), null }, 
				{ "testuser1@gmail.com", super.entities().get("team1"), null }, //Caso positivo
				{ "hola", super.entities().get("team1"), ResponseStatusException.class }}; //Caso negativo
		
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
		this.projectRepository.save(new Project("Acme-Standard",
				"Proyecto de nuestro equipo standard",
				this.teamService.findOne(super.entities().get("team6"))));
		this.projectRepository.save(new Project("Acme-Standard",
				"Proyecto de nuestro equipo standard",
				this.teamService.findOne(super.entities().get("team6"))));
		ProjectDto projectDto = new ProjectDto();
		projectDto.setName("Name 1");
		projectDto.setDescription("Description 1");
		Object[][] objects = {
				{ "testuser1@gmail.com", projectDto, super.entities().get("team1"), null }, 
				{ "testuser10@gmail.com", projectDto, super.entities().get("team5"), ResponseStatusException.class }, 
				{ "testuser12@gmail.com", projectDto, super.entities().get("team6"), ResponseStatusException.class }, 
				{ "hola", projectDto, super.entities().get("team1"), ResponseStatusException.class }}; //Caso negativo
		
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
				{ "testuser1@gmail.com", projectDto, super.entities().get("team1"), super.entities().get("project1"), "NombreActualizado", null }, //Caso positivo
				{ "hola", projectDto, super.entities().get("team65"), super.entities().get("project1"), "NombreActualizado", ResponseStatusException.class }}; //Caso negativo
		
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
				{ "testuser4@gmail.com", super.entities().get("project1") , ResponseStatusException.class}, 
				{ "testuser3@gmail.com", super.entities().get("project1") , ResponseStatusException.class}, 
				{ "hola", super.entities().get("project1"), ResponseStatusException.class }, //Caso negativo
				{ "testuser1@gmail.com", super.entities().get("project1") , null}, //Caso positivo
				{ "testuser3@gmail.com", super.entities().get("project2"), ResponseStatusException.class }}; //Caso negativo
			
		Stream.of(objects).forEach(x -> driverTestDelete((String) x[0], (Integer) x[1], (Class<?>) x[2]));
	}
	
		protected void driverTestDelete(String user, Integer projectId, Class<?> expected) {
			Class<?> caught = null;
			try {
				super.authenticateOrUnauthenticate(user);
				this.projectService.delete(projectId);
				this.projectService.flush();
				super.authenticateOrUnauthenticate(null);
				
			} catch (Exception oops) {
				caught = oops.getClass();
			}
			super.checkExceptions(expected, caught);
		}
		
		@Test
		public void projectTestGet() throws Exception {
			Project project1 = projectRepository.save(new Project("Acme-Basic",
					"Proyecto de nuestro equipo basico",
					this.teamService.findOne(super.entities().get("team5"))));
			this.projectRepository.save(new Project("Acme-Standard",
					"Proyecto de nuestro equipo standard",
					this.teamService.findOne(super.entities().get("team6"))));
			this.projectRepository.save(new Project("Acme-Standard",
					"Proyecto de nuestro equipo standard",
					this.teamService.findOne(super.entities().get("team6"))));
			Project project2 = projectRepository.save(new Project("Acme-Standard",
					"Proyecto de nuestro equipo standard",
					this.teamService.findOne(super.entities().get("team6"))));
			Object[][] objects = {
					{ "testuser3@gmail.com", super.entities().get("project1") , ResponseStatusException.class}, 
					{ "testuser1@gmail.com", super.entities().get("project1") , null}, 
					{ "testuser12@gmail.com", project2.getId(), ResponseStatusException.class}, 
					{ "testuser10@gmail.com", project1.getId(), ResponseStatusException.class }}; 
				
			Stream.of(objects).forEach(x -> driverTestGet((String) x[0], (Integer) x[1], (Class<?>) x[2]));
		}
		
			protected void driverTestGet(String user, Integer projectId, Class<?> expected) {
				Class<?> caught = null;
				try {
					super.authenticateOrUnauthenticate(user);
					this.projectService.getOne(projectId);
					this.projectService.flush();
					super.authenticateOrUnauthenticate(null);
					
				} catch (Exception oops) {
					caught = oops.getClass();
				}
				super.checkExceptions(expected, caught);
			}
}
