package com.spring.Scrume;

import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.server.ResponseStatusException;

import com.spring.CustomObject.ChangeRolDto;
import com.spring.Service.UserRolService;

public class UserRolServiceTest extends AbstractTest {

	@Autowired
	private UserRolService userRolService;
	
	

	@Test
	public void userRolServiceTeamOutTest() throws Exception {
		
		Object[][] objects = {
				{"testuser1@gmail.com", super.entities().get("team1"), null},
				{"testuser4@gmail.com", super.entities().get("team4"), null},
				{"testuser3@gmail.com", super.entities().get("team1"), ResponseStatusException.class},
				{"testuser1@gmail.com", 123456765, ResponseStatusException.class}};
		Stream.of(objects).forEach(x -> driverUserRolServiceTeamOutTest((String) x[0], (Integer) x[1], (Class<?>) x[2]));
	}

	protected void driverUserRolServiceTeamOutTest(String user, Integer idTeam, Class<?> expected) {
		Class<?> caught = null;
		try {
			super.authenticateOrUnauthenticate(user);
			this.userRolService.teamOut(idTeam);
			this.userRolService.flush();
			super.authenticateOrUnauthenticate(null);

		} catch (Exception oops) {
			caught = oops.getClass();
		}
		super.checkExceptions(expected, caught);
	}
	
	@Test
	public void userRolServiceRemoveFromTeamTest() throws Exception {
		
		Object[][] objects = {
				{"testuser1@gmail.com",super.entities().get("user4"), super.entities().get("team1"), null},
				{"testuser4@gmail.com",super.entities().get("user4"), super.entities().get("team4"), null},
				{"testuser1@gmail.com",1234543, super.entities().get("team1"), ResponseStatusException.class},
				{"testuser2@gmail.com", super.entities().get("user4"), super.entities().get("team1"), ResponseStatusException.class},
				{"testuser1@gmail.com", super.entities().get("user4"), 123456765, ResponseStatusException.class},
				{"testuser4@gmail.com", super.entities().get("user1"), super.entities().get("team1"), ResponseStatusException.class}};

		Stream.of(objects).forEach(x -> driverUserRolServiceRemoveFromTeamTest((String) x[0], (Integer) x[1], (Integer) x[2], (Class<?>) x[3]));
	}

	protected void driverUserRolServiceRemoveFromTeamTest(String user, Integer idUser, Integer idTeam, Class<?> expected) {
		Class<?> caught = null;
		try {
			super.authenticateOrUnauthenticate(user);
			this.userRolService.removeFromTeam(idUser, idTeam);
			this.userRolService.flush();
			super.authenticateOrUnauthenticate(null);

		} catch (Exception oops) {
			caught = oops.getClass();
		}
		super.checkExceptions(expected, caught);
	}
	
	@Test
	public void userRolServiceChangeRolTest() throws Exception {
		ChangeRolDto changeRol1 = new ChangeRolDto();
		changeRol1.setAdmin(false);
		Object[][] objects = {
				{"testuser2@gmail.com",super.entities().get("user1"), super.entities().get("team1"), changeRol1, ResponseStatusException.class},
				//{"testuser4@gmail.com",super.entities().get("user4"), super.entities().get("team1"), changeRol1, ResponseStatusException.class},
				{"testuser1@gmail.com",super.entities().get("user4"), super.entities().get("team1"), changeRol1, null}};

		Stream.of(objects).forEach(x -> driverUserRolServiceChangeRolTest((String) x[0], (Integer) x[1], (Integer) x[2], (ChangeRolDto) x[3], (Class<?>) x[4]));
	}

	protected void driverUserRolServiceChangeRolTest(String user, Integer idUser, Integer idTeam, ChangeRolDto changeRolDto, Class<?> expected) {
		Class<?> caught = null;
		try {
			super.authenticateOrUnauthenticate(user);
			this.userRolService.changeRol(idUser, idTeam, changeRolDto);
			this.userRolService.flush();
			super.authenticateOrUnauthenticate(null);

		} catch (Exception oops) {
			caught = oops.getClass();
		}
		super.checkExceptions(expected, caught);
	}
	
	@Test
	public void userRolServiceListAllTeamsOfAnUserTest() throws Exception {
		ChangeRolDto changeRol1 = new ChangeRolDto();
		changeRol1.setAdmin(false);
		Object[][] objects = {
				{"testuser1@gmail.com", null}};

		Stream.of(objects).forEach(x -> driverUserRolServiceListAllTeamsOfAnUserTest((String) x[0], (Class<?>) x[1]));
	}

	protected void driverUserRolServiceListAllTeamsOfAnUserTest(String user, Class<?> expected) {
		Class<?> caught = null;
		try {
			super.authenticateOrUnauthenticate(user);
			this.userRolService.listAllTeamsOfAnUser();
			this.userRolService.flush();
			super.authenticateOrUnauthenticate(null);

		} catch (Exception oops) {
			caught = oops.getClass();
		}
		super.checkExceptions(expected, caught);
	}

}