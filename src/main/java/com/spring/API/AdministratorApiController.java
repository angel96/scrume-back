package com.spring.API;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.spring.CustomObject.SecurityBreachDto;
import com.spring.Model.SecurityBreach;
import com.spring.Service.SecurityBreachService;

@RestController
@RequestMapping("/api/administrator")
public class AdministratorApiController extends AbstractApiController {

	@Autowired
	private SecurityBreachService securityBreachService;
	
	@PutMapping
	public SecurityBreach updateSecurityBreach(@RequestBody SecurityBreachDto securityBreachDto){
		return this.securityBreachService.updateSecurityBreach(securityBreachDto);
	}
	
}
