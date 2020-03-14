package com.spring.API;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.spring.CustomObject.InvitationRecipientDto;
import com.spring.CustomObject.InvitationSenderDto;
import com.spring.CustomObject.TeamDto;
import com.spring.CustomObject.TeamEditDto;
import com.spring.Service.InvitationService;
import com.spring.Service.TeamService;

@RestController
@RequestMapping("/api/team")
public class TeamApiController {

	@Autowired
	private TeamService teamService;
	
	@Autowired
	private InvitationService invitationService;
	
	@PostMapping("/save")
	public TeamDto save(@RequestBody TeamDto teamDto) throws Exception{
		return this.teamService.save(teamDto);
	}
	
	@PostMapping("/update")
	public TeamEditDto update(@RequestParam(value="idTeam") Integer idTeam, @RequestBody TeamEditDto teamEditDto) throws Exception{
		teamEditDto.setId(idTeam);
		return this.teamService.update(teamEditDto);
	}
	
	@PostMapping("/invite")
	public InvitationSenderDto invite(@RequestBody InvitationSenderDto invitationSenderDto) throws Exception{
		return this.invitationService.save(invitationSenderDto);
	}
	
	@PostMapping("/answer-invitation")
	public InvitationRecipientDto answerInvitation(@RequestParam(value="idInvitation") Integer idInvitation, @RequestBody InvitationRecipientDto invitationRecipientDto) throws Exception{
		invitationRecipientDto.setId(idInvitation);
		return this.invitationService.answerInvitation(invitationRecipientDto);
	}
	
}
