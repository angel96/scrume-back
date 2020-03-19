package com.spring.API;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.spring.CustomObject.ChangeRolDto;
import com.spring.CustomObject.InvitationRecipientDto;
import com.spring.CustomObject.InvitationSenderDto;
import com.spring.CustomObject.TeamDto;
import com.spring.Service.InvitationService;
import com.spring.Service.TeamService;
import com.spring.Service.UserRolService;

@RestController
@RequestMapping("/api/team")
public class TeamApiController extends AbstractApiController{

	@Autowired
	private TeamService teamService;
	
	@Autowired
	private InvitationService invitationService;
	
	@Autowired
	private UserRolService userRolService;
	
	@PostMapping
	public TeamDto save(@RequestBody TeamDto teamDto) throws Exception{
		super.logger.info("POST /api/team");
		return this.teamService.save(teamDto);
	}
	
	@PutMapping("/{idTeam}")
	public TeamDto update(@PathVariable Integer idTeam, @RequestBody TeamDto teamEditDto) throws Exception{
		teamEditDto.setId(idTeam);
		super.logger.info("PUT /api/team/" + String.valueOf(idTeam));
		return this.teamService.update(teamEditDto);
	}
	
	@PostMapping("/team-out/{idTeam}")
	public void teamOut(@PathVariable Integer idTeam) throws Exception{
		super.logger.info("POST /api/team/team-out/" + String.valueOf(idTeam));
		this.userRolService.teamOut(idTeam);
	}
	
	@DeleteMapping("/{idTeam}")
	public void delete(@PathVariable Integer idTeam) throws Exception{
		super.logger.info("DELETE /api/team/" + String.valueOf(idTeam));
		this.teamService.delete(idTeam);
	}
	
	@GetMapping("/remove-from-team/{idUser}/{idTeam}")
	public void removeFromTeam(@PathVariable Integer idUser, @PathVariable Integer idTeam) throws Exception{
		super.logger.info("GET /api/team/remove-from-team/" + String.valueOf(idUser) + "/" + String.valueOf(idTeam));
		this.userRolService.removeFromTeam(idUser, idTeam);
	}
	
	@GetMapping("/list/{idUser}")
	public List<TeamDto> list(@PathVariable Integer idUser) throws Exception{
		super.logger.info("GET /api/team/list/" + String.valueOf(idUser));
		return this.userRolService.listAllTeamsOfAnUser(idUser);
	}
	
	@PostMapping("/change-rol/{idUser}/{idTeam}")
	public ChangeRolDto changeRol(@PathVariable Integer idUser, @PathVariable Integer idTeam, @RequestBody ChangeRolDto changeRolDto) throws Exception{
		super.logger.info("GET /api/team/change-rol/" + String.valueOf(idUser) + "/" + String.valueOf(idTeam));
		return this.userRolService.changeRol(idUser, idTeam, changeRolDto);
	}
	
	@PostMapping("/invite")
	public InvitationSenderDto invite(@RequestBody InvitationSenderDto invitationSenderDto) throws Exception{
		super.logger.info("POST /api/team/invite");
		return this.invitationService.save(invitationSenderDto);
	}
	
	@PutMapping("/answer-invitation/{idInvitation}")
	public InvitationRecipientDto answerInvitation(@PathVariable Integer idInvitation, @RequestBody InvitationRecipientDto invitationRecipientDto) throws Exception{
		invitationRecipientDto.setId(idInvitation);
		super.logger.info("PUT /api/team/answer-invitation/" + String.valueOf(idInvitation));
		return this.invitationService.answerInvitation(invitationRecipientDto);
	}
	
}
