package com.spring.Scrume;

import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.server.ResponseStatusException;

import com.spring.CustomObject.TeamDto;
import com.spring.Model.UserRol;
import com.spring.Service.TeamService;
import com.spring.Service.UserRolService;

public class TeamAPITest extends AbstractTest {

	@Autowired
	private TeamService teamService;
	
	@Autowired
	private UserRolService userRolService;
	
	

	@Test
	public void teamApiSaveTest() throws Exception {
		TeamDto teamDto1 = new TeamDto();
		teamDto1.setName("Save 1");
		Object[][] objects = {
				{"testuser@gmail.com", teamDto1, null}};

		Stream.of(objects).forEach(x -> driverTeamApiSaveTest((String) x[0], (TeamDto) x[1], (Class<?>) x[2]));
	}

	protected void driverTeamApiSaveTest(String user, TeamDto teamDto, Class<?> expected) {
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
	public void teamApiUpdateTest() throws Exception {
		TeamDto teamDto1 = new TeamDto();
		teamDto1.setId(super.entities().get("team1"));
		teamDto1.setName("Update 1");
		UserRol userRol = new UserRol();
		userRol.setAdmin(false);
		userRol.setTeam(this.teamService.findOne(super.entities().get("team1")));
	//	userRol.setUser(user);
		this.userRolService.save(userRol);
		TeamDto teamDto2 = new TeamDto();
		teamDto2.setId(1234567);
		teamDto1.setName("Update 2");
		
		Object[][] objects = {
				{"testuser@gmail.com", teamDto1, null}, {"testuser@gmail.com", teamDto2, ResponseStatusException.class}, {"testuser4@gmail.com", teamDto1, ResponseStatusException.class}};

		Stream.of(objects).forEach(x -> driverTeamApiUpdateTest((String) x[0], (TeamDto) x[1], (Class<?>) x[2]));
	}

	protected void driverTeamApiUpdateTest(String user, TeamDto teamDto, Class<?> expected) {
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
}