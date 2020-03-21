package com.spring.Scrume;

import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.server.ResponseStatusException;

import com.spring.CustomObject.TeamDto;
import com.spring.Model.Team;
import com.spring.Service.TeamService;

public class SampleAPITest extends AbstractTest {

	@Autowired
	private TeamService service;

	@Test
	public void sampleApiTest() throws Exception {
		// Param 0 -> User to make the call
		// Param 1 -> Entity
		// Param 2 -> Value to modify
		// Param 3 -> Expected Exception;
		Object[][] objects = {
				{ "angdellun@gmail.com", super.entities().get("team2"), ResponseStatusException.class } };

		Stream.of(objects).forEach(x -> driverApiTest((String) x[0], (Integer) x[1], (Class<?>) x[2]));
	}

	protected void driverApiTest(String user, Integer entity, Class<?> expected) {
		Class<?> caught = null;

		try {
			super.authenticateOrUnauthenticate(user);
			Team team = service.findOne(entity);
			team.setName("Changing name");
			this.service.save(new ModelMapper().map(team, TeamDto.class));
			this.service.flush();
			super.authenticateOrUnauthenticate(null);
		} catch (Exception oops) {
			caught = oops.getClass();
			super.checkExceptions(expected, caught);
		}

	}
}
