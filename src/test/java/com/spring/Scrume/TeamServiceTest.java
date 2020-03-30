package com.spring.Scrume;

import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.server.ResponseStatusException;

import com.spring.CustomObject.TeamDto;
import com.spring.Service.TeamService;

public class TeamServiceTest extends AbstractTest {

	@Autowired
	private TeamService teamService;
	
	

	@Test
	public void teamServiceSaveTest() throws Exception {
		TeamDto teamDto1 = new TeamDto();
		teamDto1.setName("Save 1");
		Object[][] objects = {
				{"testuser1@gmail.com", teamDto1, null}};

		Stream.of(objects).forEach(x -> driverTeamServiceSaveTest((String) x[0], (TeamDto) x[1], (Class<?>) x[2]));
	}

	protected void driverTeamServiceSaveTest(String user, TeamDto teamDto, Class<?> expected) {
		Class<?> caught = null;
		try {
			super.authenticateOrUnauthenticate(user);
			this.teamService.save(teamDto);
			this.teamService.flush();
			super.authenticateOrUnauthenticate(null);

		} catch (Exception oops) {
			caught = oops.getClass();
		}
		super.checkExceptions(expected, caught);
	}
	
	@Test
	public void teamServiceUpdateTest() throws Exception {
		TeamDto teamDto1 = new TeamDto();
		teamDto1.setId(super.entities().get("team1"));
		teamDto1.setName("Update 1");
		TeamDto teamDto2 = new TeamDto();
		teamDto2.setId(1234567);
		teamDto1.setName("Update 2");
		
		Object[][] objects = {
				{"testuser1@gmail.com", teamDto1, null},
				{"testuser2@gmail.com", teamDto1, ResponseStatusException.class},
				//{"testuser4@gmail.com", teamDto1, ResponseStatusException.class},
				{"testuser1@gmail.com", teamDto2, ResponseStatusException.class}};

		Stream.of(objects).forEach(x -> driverTeamServiceUpdateTest((String) x[0], (TeamDto) x[1], (Class<?>) x[2]));
	}

	protected void driverTeamServiceUpdateTest(String user, TeamDto teamDto, Class<?> expected) {
		Class<?> caught = null;
		
		try {
			super.authenticateOrUnauthenticate(user);
			this.teamService.update(teamDto);
			this.teamService.flush();
			super.authenticateOrUnauthenticate(null);

		} catch (Exception oops) {
			caught = oops.getClass();
		}
		super.checkExceptions(expected, caught);

	}
	
	@Test
	public void teamServiceDeleteTest() throws Exception {
		
		Object[][] objects = {
				{"testuser2@gmail.com", super.entities().get("team2"), null}, {"testuser2@gmail.com", 123456, ResponseStatusException.class}, {"testuser4@gmail.com",  super.entities().get("team1"), ResponseStatusException.class},{"testuser1@gmail.com",  super.entities().get("team1"), ResponseStatusException.class}};

		Stream.of(objects).forEach(x -> driverTeamServiceDeleteTest((String) x[0], (Integer) x[1], (Class<?>) x[2]));
	}

	protected void driverTeamServiceDeleteTest(String user, Integer idTeam, Class<?> expected) {
		Class<?> caught = null;
		
		try {
			super.authenticateOrUnauthenticate(user);
			this.teamService.delete(idTeam);
			this.teamService.flush();
			super.authenticateOrUnauthenticate(null);

		} catch (Exception oops) {
			caught = oops.getClass();
		}
		super.checkExceptions(expected, caught);

	}
}