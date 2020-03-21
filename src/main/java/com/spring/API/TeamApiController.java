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
public class TeamApiController extends AbstractApiController {

	@Autowired
	private TeamService teamService;

	@Autowired
	private InvitationService invitationService;

	@Autowired
	private UserRolService userRolService;

	@PostMapping
	public TeamDto save(@RequestBody TeamDto teamDto) throws Exception {
		super.logger.info("POST /api/team");
		return this.teamService.save(teamDto);
	}

	@PutMapping("/{idTeam}")
	public TeamDto update(@PathVariable Integer idTeam, @RequestBody TeamDto teamEditDto) {
		teamEditDto.setId(idTeam);
		super.logger.info("PUT /api/team/" + idTeam);
		return this.teamService.update(teamEditDto);
	}

	@PostMapping("/team-out/{idTeam}")
	public void teamOut(@PathVariable Integer idTeam) {
		super.logger.info("POST /api/team/team-out/" + idTeam);
		this.userRolService.teamOut(idTeam);
	}

	@DeleteMapping("/{idTeam}")
	public void delete(@PathVariable Integer idTeam) {
		super.logger.info("DELETE /api/team/" + idTeam);
		this.teamService.delete(idTeam);
	}

	@GetMapping("/remove-from-team/{idUser}/{idTeam}")
	public void removeFromTeam(@PathVariable Integer idUser, @PathVariable Integer idTeam) {
		super.logger.info("GET /api/team/remove-from-team/" + idUser + "/" + idTeam);
		this.userRolService.removeFromTeam(idUser, idTeam);
	}

	@GetMapping("/list")
	public List<TeamDto> list() {
		super.logger.info("GET /api/team/list/");
		return this.userRolService.listAllTeamsOfAnUser();
	}

	@PostMapping("/change-rol/{idUser}/{idTeam}")
	public ChangeRolDto changeRol(@PathVariable Integer idUser, @PathVariable Integer idTeam,
			@RequestBody ChangeRolDto changeRolDto) {
		super.logger.info("GET /api/team/change-rol/" + idUser + "/" + idTeam);
		return this.userRolService.changeRol(idUser, idTeam, changeRolDto);
	}

	@PostMapping("/invite")
	public InvitationSenderDto invite(@RequestBody InvitationSenderDto invitationSenderDto) {
		super.logger.info("POST /api/team/invite");
		return this.invitationService.save(invitationSenderDto);
	}

	@PutMapping("/answer-invitation/{idInvitation}")
	public InvitationRecipientDto answerInvitation(@PathVariable Integer idInvitation,
			@RequestBody InvitationRecipientDto invitationRecipientDto) throws Exception {
		invitationRecipientDto.setId(idInvitation);
		super.logger.info("PUT /api/team/answer-invitation/" + idInvitation);
		return this.invitationService.answerInvitation(invitationRecipientDto);
	}

}
