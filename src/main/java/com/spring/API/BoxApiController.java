package com.spring.API;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.spring.Model.Box;
import com.spring.Service.BoxService;

@RestController
@RequestMapping("/api/box")
public class BoxApiController extends AbstractApiController {

	@Autowired
	private BoxService service;
	
	
	@GetMapping("/all")
	public Collection<Box> findAllBox(){
		return this.service.allBoxForRegistration();
	}
	
}
