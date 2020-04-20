package com.spring.Service;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;

import javax.transaction.Transactional;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.spring.CustomObject.NotificationDto;
import com.spring.CustomObject.NotificationListDto;
import com.spring.CustomObject.NotificationSaveDto;
import com.spring.CustomObject.NotificationUpdateDto;
import com.spring.CustomObject.ProjectIdNameDto;
import com.spring.CustomObject.TeamDto;
import com.spring.Model.Notification;
import com.spring.Model.Sprint;
import com.spring.Model.Team;
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
	private DocumentService documentService;
	
	@Autowired
	private BoxService boxService;
	
	@Autowired
	private UserRolService userRolService;
	
	public Notification getOne(int id) {
		return this.notificationRepository.findById(id).orElseThrow(
				() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "The requested notification not exists"));
	}
	
	public NotificationSaveDto save(NotificationSaveDto notificationSaveDto) {
		User principal = this.userService.getUserByPrincipal();
		this.validateSprint(notificationSaveDto.getSprint());
		Sprint sprint = this.sprintService.getOne(notificationSaveDto.getSprint());
		this.validateBoxPrivileges(sprint.getProject().getTeam().getId());
		this.validatePrincipalIsLogged(principal);
		this.validatePrincipalPermission(principal, sprint);
		this.validateDate(notificationSaveDto.getDate(), sprint);
		Notification notificationEntity = new Notification(notificationSaveDto.getTitle(), notificationSaveDto.getDate(), sprint, null);
		Notification notificationBD = this.notificationRepository.save(notificationEntity);
		return new NotificationSaveDto(notificationBD.getTitle(), notificationBD.getDate(), notificationBD.getSprint().getId());
	}
	
	public NotificationDto update(Integer idNotification, NotificationUpdateDto notificationUpdateDto) {
		User principal = this.userService.getUserByPrincipal();
		Notification notificationEntity = this.getOne(idNotification);
		Sprint sprint = notificationEntity.getSprint();
		this.validatePrincipalIsLogged(principal);
		this.validatePrincipalPermission(principal, sprint);
		this.validateUpdatePermission(principal, notificationEntity);
		this.validateBoxPrivileges(sprint.getProject().getTeam().getId());

		notificationEntity.setTitle(notificationUpdateDto.getTitle());
		notificationEntity.setDate(notificationUpdateDto.getDate());
		this.validateDate(notificationUpdateDto.getDate(), sprint);
		Notification notificationBD = this.notificationRepository.save(notificationEntity);
		return new NotificationDto(notificationBD.getId(), notificationBD.getTitle(), notificationBD.getDate());
	}

	

	@Scheduled(cron = "0 0 0 ? * MON-FRI", zone = "GMT+2:00")
	public void createDailys() {
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		cal.set(Calendar.HOUR, cal.get(Calendar.HOUR)+ 2);
		Date actualDate = cal.getTime();
		String title = "Debes rellenar la daily de hoy (" + new SimpleDateFormat("dd/MM/yyyy").format(actualDate) + ")";
		Collection<Sprint> sprints = this.sprintService.getActivesSprints();
		for (Sprint sprint : sprints) {
			if(this.boxService.getMinimumBoxOfATeam(sprint.getProject().getTeam().getId()).getName().equals("PRO")) {
				this.documentService.saveDaily("Daily " + new SimpleDateFormat("dd/MM/yyyy").format(actualDate), sprint);
				Collection<User> users = this.userRolService.findUsersByTeam(sprint.getProject().getTeam());
				for (User user : users) {
					this.notificationRepository.saveAndFlush( new Notification(title, actualDate, sprint, user));
				}
			}
		}
	}
	
	public Collection<NotificationListDto> listByPrincipal() {
		User principal = this.userService.getUserByPrincipal();
		this.validatePrincipalIsLogged(principal);
		Collection<Team> teams = this.userRolService.findAllByUser(principal);
		Collection<NotificationListDto> res = new ArrayList<>();
		for (Team team : teams) {
			if(this.boxService.getMinimumBoxOfATeam(team.getId()).getName().equals("PRO")) {
				Collection<Notification> notifications = this.notificationRepository.listByUser(principal, team);
				for (Notification notification : notifications) {
					if(this.checkDocumentIsCreated(notification)) {
						this.notificationRepository.delete(notification);
					}else {
						res.add(new NotificationListDto(this.userRolService.isAdminOnTeam(principal, notification.getSprint().getProject().getTeam()), notification.getSprint().getId(), notification.getId(), notification.getTitle(), new TeamDto(team.getId(), team.getName()), new ProjectIdNameDto
								(notification.getSprint().getProject().getId(), notification.getSprint().getProject().getName()),
								notification.getDate()));
					}
				}
			}
		}
		return res;
	}

	public Collection<NotificationDto> listAllNotifications(Integer idSprint) {
		User principal = this.userService.getUserByPrincipal();
		this.validatePrincipalIsLogged(principal);
		Sprint sprint = this.sprintService.getOne(idSprint);
		this.validatePrincipalPermission(principal, sprint);
		this.validateBoxPrivileges(sprint.getProject().getTeam().getId());
		Collection<Notification> notifications = this.notificationRepository.listAllInactiveNotifications(sprint);
		ModelMapper mapper = new ModelMapper();
		Type listType = new TypeToken<Collection<NotificationDto>>() {
		}.getType();
		return mapper.map(notifications, listType);
	}
	
	public void delete(Integer idNotification) {
		User principal = this.userService.getUserByPrincipal();
		this.validatePrincipalIsLogged(principal);
		Notification notificationEntity = this.getOne(idNotification);
		this.validateDeletePermission(principal, notificationEntity);
		this.validateBoxPrivileges(notificationEntity.getSprint().getProject().getTeam().getId());

		this.notificationRepository.delete(notificationEntity);
	}
	
	public NotificationDto getNotification(Integer idNotification) {
		User principal = this.userService.getUserByPrincipal();
		this.validatePrincipalIsLogged(principal);
		Notification notificationDB = this.getOne(idNotification);
		this.validateBoxPrivileges(notificationDB.getSprint().getProject().getTeam().getId());
		this.validatePrincipalPermission(principal, notificationDB.getSprint());
		return new NotificationDto(notificationDB.getId(), notificationDB.getTitle(), notificationDB.getDate());
	}
	
	private boolean checkDocumentIsCreated(Notification notification) {
		return this.documentService.checkDocumentIsCreated(notification.getTitle(), notification.getSprint());
	}
	
	private void validateUpdatePermission(User principal, Notification notificationEntity) {
		if (!this.userRolService.isAdminOnTeam(principal, notificationEntity.getSprint().getProject().getTeam())) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,
					"The user must be a team administrator to perform this action");
		}
		if (notificationEntity.getUser() != null) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,
					"The user does not have permission to perform this action");
		}
		if (notificationEntity.getDate().before(new Date()) && notificationEntity.getUser() == null) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,
					"The notification has already been sent to the users, it is not possible to edit it");
		}			
	}
	
	private void validateDeletePermission(User principal, Notification notificationEntity) {
		String messageError = "The user does not have permission to delete the requested notification";
		
		if (!this.userRolService.isUserOnTeam(principal, notificationEntity.getSprint().getProject().getTeam())) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, 
					messageError);
		}
		if(notificationEntity.getUser() == null && notificationEntity.getDate().after(new Date()) && !this.userRolService.isAdminOnTeam(principal, notificationEntity.getSprint().getProject().getTeam())) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, 
					messageError);
		}
		if (!(notificationEntity.getUser() == null || notificationEntity.getUser() == principal)) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,
					messageError);
		}
	}
	

	private void validatePrincipalIsLogged(User principal) {
		if (principal == null) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "The user must be logged in");
		}
	}
	
	private void validatePrincipalPermission(User principal, Sprint sprint) {
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
		if (date == null) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "the date cannot be null");
		}
		if (date.before(Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant()))) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Date cannot be earlier than the current one");
		}
		if (date.before(sprint.getStartDate()) || date.after(sprint.getEndDate())) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
					"The date must be after the start date of the sprint and before the end date");
		}		
	}


	private void validateSprint(Integer idSprint) {
		if (idSprint == null) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "the sprint id cannot be null");
		}
	}
	
	private void validateBoxPrivileges(Integer idTeam) {
		if (!this.boxService.getMinimumBoxOfATeam(idTeam).getName().equals("PRO")) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "To use the notifications it is necessary to have the pro box");
		}
	}
	public void flush() {
		this.notificationRepository.flush();
	}






	
}
