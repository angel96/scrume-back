package com.spring.API;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.spring.CustomObject.NotificationSaveDto;
import com.spring.Service.NotificationService;

@RestController
@RequestMapping("/api/notification")
public class NotificationApiController extends AbstractApiController {

	@Autowired
	private NotificationService service;



	@PostMapping
	public NotificationSaveDto save(@RequestBody NotificationSaveDto notificationSaveDto) {
		return service.save(notificationSaveDto);
	}

}