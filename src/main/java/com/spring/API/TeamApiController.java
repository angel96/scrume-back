package com.spring.API;

import java.util.Collection;
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
import com.spring.CustomObject.FindByNickDto;
import com.spring.CustomObject.InvitationListDto;
import com.spring.CustomObject.InvitationRecipientDto;
import com.spring.CustomObject.InvitationSenderDto;
import com.spring.CustomObject.MyTeamDto;
import com.spring.CustomObject.TeamDto;
import com.spring.CustomObject.UserWithNickDto;
import com.spring.CustomObject.UserWithUserRolDto;
import com.spring.Service.InvitationService;
import com.spring.Service.TeamService;
import com.spring.Service.UserRolService;
import com.spring.Service.UserService;

@RestController
@RequestMapping("/api/team")
public class TeamApiController extends AbstractApiController {

	@Autowired
	private TeamService teamService;

	@Autowired
	private InvitationService invitationService;

	@Autowired
	private UserRolService userRolService;

	@Autowired
	private UserService userService;
	
	@PostMapping
	public TeamDto save(@RequestBody TeamDto teamDto) {
		super.logger.info("POST /api/team");
		return this.teamService.save(teamDto);
	}

	@PutMapping("/{idTeam}")
	public TeamDto update(@PathVariable Integer idTeam, @RequestBody TeamDto teamEditDto) {
		teamEditDto.setId(idTeam);
		super.logger.info("PUT /api/team/" + idTeam);
		return this.teamService.update(teamEditDto);
	}

	@GetMapping("/team-out/{idTeam}")
	public void teamOut(@PathVariable Integer idTeam) {
		super.logger.info("GET /api/team/team-out/" + idTeam);
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
	public Collection<MyTeamDto> list() {
		super.logger.info("GET /api/team/list/");
		return this.userRolService.listAllTeamsOfAnUser();
	}

	@PostMapping("/change-rol")
	public void changeRol(@RequestBody ChangeRolDto changeRolDto) {
		super.logger.info("POST /api/team/change-rol");
		this.userRolService.changeRol(changeRolDto);
	}

	@PostMapping("/invite")
	public void invite(@RequestBody InvitationSenderDto invitationSenderDto) {
		super.logger.info("POST /api/team/invite");
		this.invitationService.save(invitationSenderDto);
	}

	@PutMapping("/answer-invitation/{idInvitation}")
	public void answerInvitation(@PathVariable Integer idInvitation,
			@RequestBody InvitationRecipientDto invitationRecipientDto) {
		invitationRecipientDto.setId(idInvitation);
		super.logger.info("PUT /api/team/answer-invitation/" + idInvitation);
		this.invitationService.answerInvitation(invitationRecipientDto);
	}
	
	@GetMapping("/list-invitations")
	public List<InvitationListDto> listInvitations() {
		super.logger.info("GET /api/team/list-invitations");
		return this.invitationService.listAllByPrincipal();
	}
	
	@GetMapping("/{idTeam}")
	public MyTeamDto getTeam(@PathVariable Integer idTeam) {
		super.logger.info("GET /api/team/" + idTeam);
		return this.teamService.getTeam(idTeam);
	}
	
	@GetMapping("/list-members/{idTeam}")
	public Collection<UserWithUserRolDto> listMembersOfATeam(@PathVariable Integer idTeam) {
		super.logger.info("GET /api/team/list-members/" + idTeam);
		return this.userRolService.listMembersOfATeam(idTeam);
	}
	
	@GetMapping("/isAdmin/{idTeam}")
	public boolean isAdmin(@PathVariable Integer idTeam) {
		super.logger.info("GET /api/team/isAdmin/" + idTeam);
		return this.userRolService.isAdminOnTeam(idTeam);
	}
	
	@PostMapping("/findByNick")
	public Collection<UserWithNickDto> findByNickStartsWith(@RequestBody FindByNickDto findByNickDto) {
		super.logger.info("POST /api/team/findByNick");
		return this.userService.findByNickStartsWith(findByNickDto);
	}

}
