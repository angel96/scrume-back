package com.spring.API;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.spring.CustomObject.ChangeRolDto;
import com.spring.CustomObject.InvitationRecipientDto;
import com.spring.CustomObject.InvitationSenderDto;
import com.spring.CustomObject.TeamDto;
import com.spring.CustomObject.TeamEditDto;
import com.spring.Service.InvitationService;
import com.spring.Service.TeamService;
import com.spring.Service.UserRolService;

@RestController
@RequestMapping("/api/team")
public class TeamApiController {

	@Autowired
	private TeamService teamService;
	
	@Autowired
	private InvitationService invitationService;
	
	@Autowired
	private UserRolService userRolService;
	
	@PostMapping("/save")
	public TeamDto save(@RequestBody TeamDto teamDto) throws Exception{
		return this.teamService.save(teamDto);
	}
	
	@PostMapping("/update")
	public TeamEditDto update(@RequestParam(value="idTeam") Integer idTeam, @RequestBody TeamEditDto teamEditDto) throws Exception{
		teamEditDto.setId(idTeam);
		return this.teamService.update(teamEditDto);
	}
	
	@PostMapping("/team-out")
	public void teamOut(@RequestParam(value="idTeam") Integer idTeam) throws Exception{
		this.userRolService.teamOut(idTeam);
	}
	
	@PostMapping("/delete")
	public void delete(@RequestParam(value="idTeam") Integer idTeam) throws Exception{
		this.teamService.delete(idTeam);
	}
	
	@PostMapping("/remove-from-team")
	public void removeFromTeam(@RequestParam(value="idUser") Integer idUser, @RequestParam(value="idTeam") Integer idTeam) throws Exception{
		this.userRolService.removeFromTeam(idUser, idTeam);
	}
	
	@PostMapping("/change-rol")
	public ChangeRolDto changeRol(@RequestParam(value="idUser") Integer idUser, @RequestParam(value="idTeam") Integer idTeam, @RequestBody ChangeRolDto changeRolDto) throws Exception{
		return this.userRolService.changeRol(idUser, idTeam, changeRolDto);
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
