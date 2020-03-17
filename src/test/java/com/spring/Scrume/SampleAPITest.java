package com.spring.Scrume;

import javax.annotation.PostConstruct;

import org.junit.After;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;

import com.spring.CustomObject.TeamDto;
import com.spring.Model.Team;
import com.spring.Service.TeamService;

public class SampleAPITest extends AbstractTest {

	@Autowired
	private TeamService service;

	@PostConstruct
	public void initBefore() {
		super.authenticateOrUnauthenticate("angdellun@gmail.com");
	}

	@Test
	public void pruebaSave() throws Exception {
		try {
			Team team = this.service.findOne(super.entities().get("team2"));
			team.setName("Testing wrong");
			this.service.save(new ModelMapper().map(team, TeamDto.class));
		} catch (Throwable oops) {
			super.checkExceptions(HttpClientErrorException.class, oops.getClass());
		}
	}

	@After
	public void initFinish() {
		super.authenticateOrUnauthenticate(null);
	}
}
