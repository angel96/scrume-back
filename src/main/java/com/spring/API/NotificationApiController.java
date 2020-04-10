package com.spring.API;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.spring.CustomObject.NotificationDto;
import com.spring.CustomObject.NotificationListDto;
import com.spring.CustomObject.NotificationSaveDto;
import com.spring.CustomObject.NotificationUpdateDto;
import com.spring.Service.NotificationService;

@RestController
@RequestMapping("/api/notification")
public class NotificationApiController extends AbstractApiController {

	@Autowired
	private NotificationService notificationService;

	@PostMapping
	public NotificationSaveDto save(@RequestBody NotificationSaveDto notificationSaveDto) {
		super.logger.info("POST /api/notification");
		return notificationService.save(notificationSaveDto);
	}
	
	@PutMapping("/{idNotification}")
	public NotificationDto update(@PathVariable Integer idNotification, @RequestBody NotificationUpdateDto notificationUpdateDto) {
		super.logger.info("PUT /api/notification/" + idNotification);
		return notificationService.update(idNotification, notificationUpdateDto);
	}
	
	@GetMapping("/{idNotification}")
	public NotificationDto getNotification(@PathVariable Integer idNotification) {
		super.logger.info("GET /api/notification/" + idNotification);
		return notificationService.getNotification(idNotification);
	}
	
	@GetMapping("/list-all-notifications/{idSprint}")
	public Collection<NotificationDto> listAllNotifications(@PathVariable Integer idSprint) {
		super.logger.info("GET /api/notification/list-all-notifications/" + idSprint);
		return notificationService.listAllNotifications(idSprint);
	}
	
	@GetMapping("/list-my-notifications")
	public Collection<NotificationListDto> listByPrincipal() {
		super.logger.info("GET /api/notification/list-my-notifications");
		return notificationService.listByPrincipal();
	}
	
	
	@DeleteMapping("/{idNotification}")
	public void delete(@PathVariable Integer idNotification) {
		super.logger.info("DELETE /api/notification/" + idNotification);
		notificationService.delete(idNotification);
	}
	
	
	
	

}