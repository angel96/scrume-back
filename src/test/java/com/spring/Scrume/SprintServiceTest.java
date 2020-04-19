package com.spring.Scrume;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.server.ResponseStatusException;

import com.spring.CustomObject.SprintDto;
import com.spring.CustomObject.SprintEditDto;
import com.spring.Model.Project;
import com.spring.Service.ProjectService;
import com.spring.Service.SprintService;

public class SprintServiceTest extends AbstractTest {

	@Autowired
	private SprintService sprintService;
	
	@Autowired
	private ProjectService projectService;
	
	
	@Test
	public void SprintServiceGetStatisticsTest() throws Exception {
		Object[][] objects = {
				{"testuser1@gmail.com", super.entities().get("sprint1"), null}, {"testuser2@gmail.com", super.entities().get("sprint1"), ResponseStatusException.class}, {"testuser3@gmail.com", super.entities().get("sprint1"), ResponseStatusException.class}};

		Stream.of(objects).forEach(x -> driverSprintServiceGetStatisticsTest((String) x[0], (Integer) x[1], (Class<?>) x[2]));
	}

	protected void driverSprintServiceGetStatisticsTest(String user, Integer idSprint, Class<?> expected) {
		Class<?> caught = null;
		try {
			super.authenticateOrUnauthenticate(user);
			this.sprintService.getStatistics(idSprint);
			this.sprintService.flush();
			super.authenticateOrUnauthenticate(null);

		} catch (Exception oops) {
			caught = oops.getClass();
		}
		super.checkExceptions(expected, caught);
	}
	
	@Test
	public void SprintServiceListByProjectTest() throws Exception {
		Object[][] objects = {
				{"testuser1@gmail.com", super.entities().get("project1"), null}, {"testuser2@gmail.com", super.entities().get("project1"), ResponseStatusException.class}, {"testuser1@gmail.com", 123456765, ResponseStatusException.class}};

		Stream.of(objects).forEach(x -> driverSprintServiceListByProjectTest((String) x[0], (Integer) x[1], (Class<?>) x[2]));
	}

	protected void driverSprintServiceListByProjectTest(String user, Integer idProject, Class<?> expected) {
		Class<?> caught = null;
		try {
			super.authenticateOrUnauthenticate(user);
			this.sprintService.listByProject(idProject);
			this.sprintService.flush();
			super.authenticateOrUnauthenticate(null);

		} catch (Exception oops) {
			caught = oops.getClass();
		}
		super.checkExceptions(expected, caught);
	}
	
	@Test
	public void SprintServiceSaveTest() throws Exception {
		SprintDto sprintDto1 = new SprintDto();
		sprintDto1.setProject(this.projectService.findOne(super.entities().get("project1")));
		LocalDateTime localDateTime =  LocalDateTime.of(2022, 7, 03, 10, 15);
		Date startDate1 = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
		LocalDateTime localDateTime1 = LocalDateTime.of(2022, 8, 03, 10, 15);
		Date endDate1 = Date.from(localDateTime1.atZone(ZoneId.systemDefault()).toInstant());
		
		LocalDateTime localDateTime2 =  LocalDateTime.of(2020, 6, 03, 10, 15);
		Date startDate2 = Date.from(localDateTime2.atZone(ZoneId.systemDefault()).toInstant());
		LocalDateTime localDateTime3 = LocalDateTime.of(2020, 9, 03, 10, 15);
		Date endDate2 = Date.from(localDateTime3.atZone(ZoneId.systemDefault()).toInstant());
		
		sprintDto1.setStartDate(startDate1);
		sprintDto1.setEndDate(endDate1);
		Project badProject = new Project("new", "description", null);
		badProject.setId(123456789);
		SprintDto sprintDto2 = new SprintDto();
		sprintDto2.setProject(badProject);
		sprintDto2.setStartDate(startDate1);
		sprintDto2.setEndDate(endDate1);
		
		SprintDto sprintDto3 = new SprintDto();
		sprintDto3.setProject(this.projectService.findOne(super.entities().get("project1")));
		sprintDto3.setStartDate(null);
		sprintDto3.setEndDate(endDate1);
		
		SprintDto sprintDto4 = new SprintDto();
		sprintDto4.setProject(this.projectService.findOne(super.entities().get("project1")));
		sprintDto4.setStartDate(startDate1);
		sprintDto4.setEndDate(null);
		
		SprintDto sprintDto5 = new SprintDto();
		sprintDto5.setProject(this.projectService.findOne(super.entities().get("project1")));
		sprintDto5.setStartDate(endDate1);
		sprintDto5.setEndDate(startDate1);
		
		SprintDto sprintDto6 = new SprintDto();
		sprintDto6.setProject(this.projectService.findOne(super.entities().get("project1")));
		sprintDto6.setStartDate(startDate2);
		sprintDto6.setEndDate(endDate2);

		Object[][] objects = {
				{"testuser1@gmail.com", sprintDto1, null}, {"testuser4@gmail.com", sprintDto1, ResponseStatusException.class}, {"testuser1@gmail.com", sprintDto2, ResponseStatusException.class}, {"testuser1@gmail.com", sprintDto3, ResponseStatusException.class}, {"testuser1@gmail.com", sprintDto4, ResponseStatusException.class}, {"testuser1@gmail.com", sprintDto5, ResponseStatusException.class}, {"testuser1@gmail.com", sprintDto6, ResponseStatusException.class}};

		Stream.of(objects).forEach(x -> driverSprintServiceSaveTest((String) x[0], (SprintDto) x[1], (Class<?>) x[2]));
	}

	protected void driverSprintServiceSaveTest(String user, SprintDto sprintDto, Class<?> expected) {
		Class<?> caught = null;
		try {
			super.authenticateOrUnauthenticate(user);
			this.sprintService.save(sprintDto);
			this.sprintService.flush();
			super.authenticateOrUnauthenticate(null);

		} catch (Exception oops) {
			caught = oops.getClass();
		}
		super.checkExceptions(expected, caught);
	}
	
	@Test
	public void SprintServiceUpdateTest() throws Exception {
		LocalDateTime localDateTime =  LocalDateTime.of(2022, 7, 03, 10, 15);
		Date startDate1 = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
		LocalDateTime localDateTime1 = LocalDateTime.of(2022, 8, 03, 10, 15);
		Date endDate1 = Date.from(localDateTime1.atZone(ZoneId.systemDefault()).toInstant());
		
		LocalDateTime localDateTime2 =  LocalDateTime.of(2020, 6, 03, 10, 15);
		Date startDate2 = Date.from(localDateTime2.atZone(ZoneId.systemDefault()).toInstant());
		LocalDateTime localDateTime3 = LocalDateTime.of(2020, 9, 03, 10, 15);
		Date endDate2 = Date.from(localDateTime3.atZone(ZoneId.systemDefault()).toInstant());
		
		SprintEditDto sprintDto1 = new SprintEditDto();
		sprintDto1.setId(super.entities().get("sprint1"));
		sprintDto1.setStartDate(startDate1);
		sprintDto1.setEndDate(endDate1);
		
		SprintEditDto sprintDto2 = new SprintEditDto();
		sprintDto2.setId(super.entities().get("sprint1"));

		sprintDto2.setStartDate(null);
		sprintDto2.setEndDate(endDate1);
		
		SprintEditDto sprintDto3 = new SprintEditDto();
		sprintDto3.setId(super.entities().get("sprint1"));

		sprintDto3.setStartDate(startDate1);
		sprintDto3.setEndDate(null);
		
		SprintEditDto sprintDto4 = new SprintEditDto();
		sprintDto4.setId(super.entities().get("sprint1"));

		sprintDto4.setStartDate(endDate1);
		sprintDto4.setEndDate(startDate1);
		
		SprintEditDto sprintDto5 = new SprintEditDto();
		sprintDto5.setId(super.entities().get("sprint1"));

		sprintDto5.setStartDate(startDate2);
		sprintDto5.setEndDate(endDate2);

		SprintEditDto sprintDto6 = new SprintEditDto();
		sprintDto6.setId(1234565432);
		sprintDto6.setStartDate(startDate1);
		sprintDto6.setEndDate(endDate1);
		
		Object[][] objects = {
				{"testuser1@gmail.com", sprintDto1, null},
				//{"testuser4@gmail.com", sprintDto1, ResponseStatusException.class},
				{"testuser1@gmail.com", sprintDto2, ResponseStatusException.class},
				{"testuser1@gmail.com", sprintDto3, ResponseStatusException.class},
				{"testuser1@gmail.com", sprintDto4, ResponseStatusException.class},
				{"testuser1@gmail.com", sprintDto5, ResponseStatusException.class},
				{"testuser1@gmail.com", sprintDto6, ResponseStatusException.class}};

		Stream.of(objects).forEach(x -> driverSprintServiceUpdateTest((String) x[0], (SprintEditDto) x[1], (Class<?>) x[2]));
	}

	protected void driverSprintServiceUpdateTest(String user, SprintEditDto sprintDto, Class<?> expected) {
		Class<?> caught = null;
		try {
			super.authenticateOrUnauthenticate(user);
			this.sprintService.update(sprintDto);
			this.sprintService.flush();
			super.authenticateOrUnauthenticate(null);

		} catch (Exception oops) {
			caught = oops.getClass();
		}
		super.checkExceptions(expected, caught);
	}
	
	@Test
	public void SprintServiceAreValidDatesTest() throws Exception {
		LocalDateTime localDateTime =  LocalDateTime.of(2022, 7, 03, 10, 15);
		Date startDate1 = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
		LocalDateTime localDateTime1 = LocalDateTime.of(2022, 8, 03, 10, 15);
		Date endDate1 = Date.from(localDateTime1.atZone(ZoneId.systemDefault()).toInstant());
		
		SprintDto sprintDto1 = new SprintDto();
		sprintDto1.setId(super.entities().get("sprint1"));
		sprintDto1.setStartDate(startDate1);
		sprintDto1.setEndDate(endDate1);
		sprintDto1.setProject(this.projectService.findOne(super.entities().get("project1")));
		
		Object[][] objects = {
				{"testuser1@gmail.com", sprintDto1, null}};

		Stream.of(objects).forEach(x -> driverSprintServiceAreValidDatesTest((String) x[0], (SprintDto) x[1], (Class<?>) x[2]));
	}
	
	protected void driverSprintServiceAreValidDatesTest(String user, SprintDto sprintDto, Class<?> expected) {
		Class<?> caught = null;
		try {
			super.authenticateOrUnauthenticate(user);
			this.sprintService.areValidDates(sprintDto);
			this.sprintService.flush();
			super.authenticateOrUnauthenticate(null);

		} catch (Exception oops) {
			caught = oops.getClass();
		}
		super.checkExceptions(expected, caught);
	}
	
	@Test
	public void SprintServiceBurndownTest() throws Exception {
		Object[][] objects = {
				{"testuser1@gmail.com", super.entities().get("sprint1"), null},
				{"testuser4@gmail.com", super.entities().get("sprint4"),null},
				{"testuser1@gmail.com", super.entities().get("sprint4"), ResponseStatusException.class},
				{"testuser3@gmail.com", super.entities().get("sprint1"), ResponseStatusException.class}};
		//poner un id que no existe, deberia estar cubierto pero por si acaso

		Stream.of(objects).forEach(x -> driverSprintServiceBurndownTest((String) x[0], (Integer) x[1], (Class<?>) x[2]));
	}

	protected void driverSprintServiceBurndownTest(String user, Integer idSprint, Class<?> expected) {
		Class<?> caught = null;
		try {
			super.authenticateOrUnauthenticate(user);
			this.sprintService.getBurnDown(idSprint);
			this.sprintService.flush();
			super.authenticateOrUnauthenticate(null);

		} catch (Exception oops) {
			caught = oops.getClass();
		}
		super.checkExceptions(expected, caught);
	}

	
	@Test
	public void SprintServiceBurnupTest() throws Exception {
		Object[][] objects = {
				{"testuser1@gmail.com", super.entities().get("sprint1"), null},
				{"testuser4@gmail.com", super.entities().get("sprint4"),null},
				{"testuser1@gmail.com", super.entities().get("sprint4"), ResponseStatusException.class},
				{"testuser3@gmail.com", super.entities().get("sprint1"), ResponseStatusException.class}};
		//poner un id que no existe, deberia estar cubierto pero por si acaso

		Stream.of(objects).forEach(x -> driverSprintServiceBurnupTest((String) x[0], (Integer) x[1], (Class<?>) x[2]));
	}

	protected void driverSprintServiceBurnupTest(String user, Integer idSprint, Class<?> expected) {
		Class<?> caught = null;
		try {
			super.authenticateOrUnauthenticate(user);
			this.sprintService.getBurnUp(idSprint);
			this.sprintService.flush();
			super.authenticateOrUnauthenticate(null);

		} catch (Exception oops) {
			caught = oops.getClass();
		}
		super.checkExceptions(expected, caught);
	}


}