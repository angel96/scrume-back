package com.spring.Scrume;

import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.server.ResponseStatusException;

import com.spring.CustomObject.ProjectDto;
import com.spring.Model.Project;
import com.spring.Repository.ProjectRepository;
import com.spring.Service.ProjectService;

public class ProjectAPITest extends AbstractTest {
	
	@Autowired
	private ProjectService projectService;
	
	@Autowired
	private ProjectRepository projectRepository;
	
	
	@Test
	public void projectApiTestUpdate() throws Exception {
		
		Object[][] objects1 = {
				{ "angdellun@gmail.com", super.entities().get("project1"), "holis", ResponseStatusException.class }};
		Object[][] objects2 = {
				{ "hola", super.entities().get("project1"), "holis", UsernameNotFoundException.class }};
		Object[][] objects3 = {
				{ "hola", super.entities().get("project1"), null, UsernameNotFoundException.class }};
		
		//Caso positivo
		Stream.of(objects1).forEach(x -> driverApiTest((String) x[0], (Integer) x[1], (String) x[2], (Class<?>) x[3]));
		//Caso negativo
		Stream.of(objects2).forEach(x -> driverApiTest((String) x[0], (Integer) x[1], (String) x[2], (Class<?>) x[3]));
		//Caso negativo
		Stream.of(objects2).forEach(x -> driverApiTest((String) x[0], (Integer) x[1], (String) x[2], (Class<?>) x[3]));
		}

	protected void driverApiTest(String user, Integer entity, String name, Class<?> expected) {
		Class<?> caught = null;
		try {
			super.authenticateOrUnauthenticate(user);
			Project project = projectService.findOne(entity);
			project.setName(name);
			this.projectService.save(new ModelMapper().map(project, ProjectDto.class));
			this.projectRepository.flush();
			super.authenticateOrUnauthenticate(null);
			
		} catch (Exception oops) {
			caught = oops.getClass();
		}
		super.checkExceptions(expected, caught);
	}
		
		@Test
		public void projectApiTestDelete() throws Exception {
			
			Object[][] objects1 = {
					{ "angdellun@gmail.com", super.entities().get("project1"), ResponseStatusException.class }};
			Object[][] objects2 = {
					{ "hola", super.entities().get("project1"), UsernameNotFoundException.class }};
			Object[][] objects3 = {
					{ "testuser@gmail.com", super.entities().get("project1"), ResponseStatusException.class }};
			
			//Caso positivo
			Stream.of(objects1).forEach(x -> driverApiDeleteTest((String) x[0], (Integer) x[1], (Class<?>) x[2]));
			//Caso negativo
			Stream.of(objects2).forEach(x -> driverApiDeleteTest((String) x[0], (Integer) x[1], (Class<?>) x[2]));
			//Caso negativo
			Stream.of(objects2).forEach(x -> driverApiDeleteTest((String) x[0], (Integer) x[1], (Class<?>) x[2]));
			}
	
		protected void driverApiDeleteTest(String user, Integer entity, Class<?> expected) {
			Class<?> caught = null;
			try {
				super.authenticateOrUnauthenticate(user);
				this.projectService.delete(entity);
				this.projectRepository.flush();
				super.authenticateOrUnauthenticate(null);
				
			} catch (Exception oops) {
				caught = oops.getClass();
			}
			super.checkExceptions(expected, caught);
		}

}
