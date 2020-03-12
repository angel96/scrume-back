package com.spring.API;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.spring.CustomObject.InvitationRecipientDto;
import com.spring.CustomObject.InvitationSenderDto;
import com.spring.CustomObject.TeamDto;
import com.spring.Service.InvitationService;
import com.spring.Service.TeamService;

@RestController
@RequestMapping("/team")
public class TeamApiController {

	@Autowired
	private TeamService teamService;
	
	@Autowired
	private InvitationService invitationService;
	
	@PostMapping("/save")
	public TeamDto save(@RequestBody TeamDto teamDto) throws Exception{
		return this.teamService.save(teamDto);
	}
	
	@PostMapping("/invite")
	public InvitationSenderDto invite(@RequestBody InvitationSenderDto invitationSenderDto) throws Exception{
		return this.invitationService.save(invitationSenderDto);
	}
	
	@PostMapping("/answer-invitation")
	public InvitationRecipientDto answerInvitation(@RequestBody InvitationRecipientDto invitationRecipientDto) throws Exception{
		return this.invitationService.answerInvitation(invitationRecipientDto);
	}
	
}
