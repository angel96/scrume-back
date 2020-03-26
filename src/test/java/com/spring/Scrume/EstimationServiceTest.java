package com.spring.Scrume;

import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.server.ResponseStatusException;

import com.spring.CustomObject.EstimationDto;
import com.spring.Service.EstimationService;

public class EstimationServiceTest extends AbstractTest {

	@Autowired
	private EstimationService estimationService;
	

	@Test
	public void estimationServiceSaveTest() throws Exception {
		EstimationDto estimationDto1 = new EstimationDto(null, 12, super.entities().get("task4"));
		EstimationDto estimationDto2 = new EstimationDto(null, 12, super.entities().get("task1"));

		Object[][] objects = {
				
				{"testuser4@gmail.com", estimationDto1, ResponseStatusException.class},
				{"testuser3@gmail.com", estimationDto1, ResponseStatusException.class},
				{"testuser1@gmail.com", estimationDto2, ResponseStatusException.class},
				{"testuser1@gmail.com", estimationDto1, null}};

		Stream.of(objects).forEach(x -> driverEstimationServiceSaveTest((String) x[0], (EstimationDto) x[1], (Class<?>) x[2]));
	}

	protected void driverEstimationServiceSaveTest(String user, EstimationDto estimationDto, Class<?> expected) {
		Class<?> caught = null;
		try {
			super.authenticateOrUnauthenticate(user);
			this.estimationService.save(estimationDto);
			this.estimationService.flush();
			super.authenticateOrUnauthenticate(null);

		} catch (Exception oops) {
			caught = oops.getClass();
		}
		super.checkExceptions(expected, caught);
	}
	
}