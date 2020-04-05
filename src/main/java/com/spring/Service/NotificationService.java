package com.spring.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.spring.CustomObject.NotificationSaveDto;
import com.spring.Model.Notification;
import com.spring.Model.Sprint;
import com.spring.Model.User;
import com.spring.Repository.NotificationRepository;

@Service
@Transactional
public class NotificationService extends AbstractService {

	@Autowired
	private NotificationRepository notificationRepository;

	@Autowired
	private SprintService sprintService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private UserRolService userRolService;
	
	public NotificationSaveDto save(NotificationSaveDto notificationSaveDto) {
		User principal = this.userService.getUserByPrincipal();
		Sprint sprint = this.sprintService.getOne(notificationSaveDto.getSprint());
		this.validatePrincipalPermission(principal, sprint);
		this.validateDate(notificationSaveDto.getDate(), sprint);
		Notification notificationEntity = new Notification(notificationSaveDto.getTitle(), notificationSaveDto.getDate(), sprint, null);
		Notification notificationBD = this.notificationRepository.save(notificationEntity);
		return new NotificationSaveDto(notificationBD.getTitle(), notificationBD.getDate(), notificationBD.getSprint().getId());
	}

	private void validatePrincipalPermission(User principal, Sprint sprint) {
		if (principal == null) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "The user must be logged in");
		}
		if (!this.userRolService.isUserOnTeam(principal, sprint.getProject().getTeam())) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,
					"The user who performs the action must belong to the team");
		}
		if (!this.userRolService.isAdminOnTeam(principal, sprint.getProject().getTeam())) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,
					"The user who performs the action must be an admin of the team");
		}
	}
	
	private void validateDate(Date date, Sprint sprint) {
		if (sprint.getStartDate() == null) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "the date cannot be null");
		}
		if (sprint.getStartDate().before(Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant()))) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Date cannot be earlier than the current one");
		}
		if (date.before(sprint.getStartDate()) || date.after(sprint.getEndDate())) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
					"The date must be after the start date of the sprint and before the end date");
		}		
	}

	
}
