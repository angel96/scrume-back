package com.spring.Scrume;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.server.ResponseStatusException;

import com.spring.CustomObject.NotificationSaveDto;
import com.spring.CustomObject.NotificationUpdateDto;
import com.spring.Service.NotificationService;

public class NotificationServiceTest extends AbstractTest {

	@Autowired
	private NotificationService notificationService;
	

	@Test
	public void NotificationServiceSaveTest() throws Exception {
		LocalDateTime localDateTime1 =  LocalDateTime.of(2020, 9, 03, 10, 15);
		Date date1 = Date.from(localDateTime1.atZone(ZoneId.systemDefault()).toInstant());
		NotificationSaveDto notificationSaveDto1 = new NotificationSaveDto("test1", date1, super.entities().get("sprint5"));
		
		LocalDateTime localDateTime2 =  LocalDateTime.of(2020, 2, 03, 10, 15);
		Date date2 = Date.from(localDateTime2.atZone(ZoneId.systemDefault()).toInstant());
		NotificationSaveDto notificationSaveDto2 = new NotificationSaveDto("test1", date2, super.entities().get("sprint5"));
		
		NotificationSaveDto notificationSaveDto3 = new NotificationSaveDto("test1", null, super.entities().get("sprint5"));

		LocalDateTime localDateTime3 =  LocalDateTime.of(2023, 2, 03, 10, 15);
		Date date3 = Date.from(localDateTime3.atZone(ZoneId.systemDefault()).toInstant());
		NotificationSaveDto notificationSaveDto4 = new NotificationSaveDto("test1", date3, super.entities().get("sprint5"));
		
		Object[][] objects = {
				
				{"testuser1@gmail.com", notificationSaveDto2, ResponseStatusException.class},
				{"testuser1@gmail.com", notificationSaveDto4, ResponseStatusException.class},
				{"testuser1@gmail.com", notificationSaveDto3, ResponseStatusException.class},
				{"testuser2@gmail.com", notificationSaveDto1, ResponseStatusException.class},
				{"testuser4@gmail.com", notificationSaveDto1, ResponseStatusException.class},
				{"testuser1@gmail.com", notificationSaveDto1, null}};

		Stream.of(objects).forEach(x -> driverNotificationServiceSaveTest((String) x[0], (NotificationSaveDto) x[1], (Class<?>) x[2]));
	}

	protected void driverNotificationServiceSaveTest(String user, NotificationSaveDto notificationSaveDto, Class<?> expected) {
		Class<?> caught = null;
		try {
			super.authenticateOrUnauthenticate(user);
			this.notificationService.save(notificationSaveDto);
			this.notificationService.flush();
			super.authenticateOrUnauthenticate(null);

		} catch (Exception oops) {
			caught = oops.getClass();
		}
		super.checkExceptions(expected, caught);
	}
	
	@Test
	public void NotificationServiceUpdateTest() throws Exception {
		LocalDateTime localDateTime1 =  LocalDateTime.of(2020, 9, 03, 10, 15);
		Date date1 = Date.from(localDateTime1.atZone(ZoneId.systemDefault()).toInstant());
		NotificationUpdateDto notificationUpdateDto1 = new NotificationUpdateDto("test1", date1);
		
		Object[][] objects = {

				{"testuser4@gmail.com", super.entities().get("notification1"), notificationUpdateDto1, ResponseStatusException.class},
				{"testuser1@gmail.com", super.entities().get("notification1"), notificationUpdateDto1, null}};

		Stream.of(objects).forEach(x -> driverNotificationServiceUpdateTest((String) x[0], (Integer) x[1], (NotificationUpdateDto) x[2], (Class<?>) x[3]));
	}

	protected void driverNotificationServiceUpdateTest(String user, Integer idNotification, NotificationUpdateDto notificationUpdateDto, Class<?> expected) {
		Class<?> caught = null;
		try {
			super.authenticateOrUnauthenticate(user);
			this.notificationService.update(idNotification, notificationUpdateDto);
			this.notificationService.flush();
			super.authenticateOrUnauthenticate(null);

		} catch (Exception oops) {
			caught = oops.getClass();
		}
		super.checkExceptions(expected, caught);
	}
	
	@Test
	public void NotificationServiceCreateDailysTest() throws Exception {
		driverNotificationServiceCreateDailysTest(null);
	}

	protected void driverNotificationServiceCreateDailysTest(Class<?> expected) {
		Class<?> caught = null;
		try {
			this.notificationService.createDailys();
			this.notificationService.flush();

		} catch (Exception oops) {
			caught = oops.getClass();
		}
		super.checkExceptions(expected, caught);
	}
	
	@Test
	public void NotificationServiceDeleteTest() throws Exception {
		
		Object[][] objects = {
				
				{"testuser4@gmail.com", super.entities().get("notification2"), ResponseStatusException.class},
				{"testuser2@gmail.com", super.entities().get("notification2"), ResponseStatusException.class},
				{"testuser1@gmail.com", super.entities().get("notification2"), null}};

		Stream.of(objects).forEach(x -> driverNotificationServiceDeleteTest((String) x[0], (Integer) x[1], (Class<?>) x[2]));
	}

	protected void driverNotificationServiceDeleteTest(String user, Integer notificationId, Class<?> expected) {
		Class<?> caught = null;
		try {
			super.authenticateOrUnauthenticate(user);
			this.notificationService.delete(notificationId);
			this.notificationService.flush();
			super.authenticateOrUnauthenticate(null);

		} catch (Exception oops) {
			caught = oops.getClass();
		}
		super.checkExceptions(expected, caught);
	}
	
	@Test
	public void NotificationServiceListByPrincipalTest() throws Exception {
		
		Object[][] objects = {
			
			{"testuser1@gmail.com", null}};

			Stream.of(objects).forEach(x -> driverNotificationServiceListByPrincipalTest((String) x[0], (Class<?>) x[1]));
		}

	protected void driverNotificationServiceListByPrincipalTest(String user, Class<?> expected) {
		Class<?> caught = null;
		try {
			super.authenticateOrUnauthenticate(user);
			this.notificationService.listByPrincipal();
			this.notificationService.flush();
			super.authenticateOrUnauthenticate(null);

		} catch (Exception oops) {
			caught = oops.getClass();
		}
		super.checkExceptions(expected, caught);
	}
	
	@Test
	public void NotificationServiceListAllNotificationsTest() throws Exception {
		
		Object[][] objects = {
			{"testuser2@gmail.com", super.entities().get("sprint1"), ResponseStatusException.class},
			{"testuser1@gmail.com", super.entities().get("sprint1"), null}};

			Stream.of(objects).forEach(x -> driverNotificationServiceListAllNotificationsTest((String) x[0], (Integer) x[1], (Class<?>) x[2]));
		}

	protected void driverNotificationServiceListAllNotificationsTest(String user, Integer idSprint, Class<?> expected) {
		Class<?> caught = null;
		try {
			super.authenticateOrUnauthenticate(user);
			this.notificationService.listAllNotifications(idSprint);
			this.notificationService.flush();
			super.authenticateOrUnauthenticate(null);

		} catch (Exception oops) {
			caught = oops.getClass();
		}
		super.checkExceptions(expected, caught);
	}
	
	@Test
	public void NotificationServiceListGetTest() throws Exception {
		
		Object[][] objects = {
			{"testuser2@gmail.com", super.entities().get("notification1"), ResponseStatusException.class},
			{"testuser1@gmail.com", super.entities().get("notification1"), null}};

			Stream.of(objects).forEach(x -> driverNotificationServiceGetTest((String) x[0], (Integer) x[1], (Class<?>) x[2]));
		}

	protected void driverNotificationServiceGetTest(String user, Integer idNotification, Class<?> expected) {
		Class<?> caught = null;
		try {
			super.authenticateOrUnauthenticate(user);
			this.notificationService.getNotification(idNotification);
			this.notificationService.flush();
			super.authenticateOrUnauthenticate(null);

		} catch (Exception oops) {
			caught = oops.getClass();
		}
		super.checkExceptions(expected, caught);
	}
	
}