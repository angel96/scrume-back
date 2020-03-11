package com.spring.API;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.spring.CustomObject.TeamDto;
import com.spring.Service.TeamService;

@RestController
@RequestMapping("/team")
public class TeamApiController {

	@Autowired
	private TeamService teamService;
	
	@PostMapping("/save")
	public TeamDto save(@RequestBody TeamDto teamDto) throws Exception{
		return this.teamService.save(teamDto);
	}
	
}
