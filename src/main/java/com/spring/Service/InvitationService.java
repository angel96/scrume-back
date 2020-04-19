package com.spring.Service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.spring.CustomObject.InvitationListDto;
import com.spring.CustomObject.InvitationRecipientDto;
import com.spring.CustomObject.InvitationSenderDto;
import com.spring.Model.Invitation;
import com.spring.Model.Team;
import com.spring.Model.User;
import com.spring.Model.UserRol;
import com.spring.Repository.InvitationRepository;

@Service
@Transactional
public class InvitationService extends AbstractService {

	@Autowired
	private InvitationRepository invitationRepository;

	@Autowired
	private UserService userService;

	@Autowired
	private UserRolService userRolService;

	@Autowired
	private TeamService teamService;

	public void save(InvitationSenderDto invitationSenderDto) {
		User principal = this.userService.getUserByPrincipal();
		this.validateUserPrincipal(principal);

		Team team = this.teamService.findOne(invitationSenderDto.getTeam());
		this.validateTeam(team);
				
		this.validateSender(principal, team);
		
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.add(Calendar.DAY_OF_YEAR, 30);
		this.validateRecipients(invitationSenderDto.getRecipients());
		for (Integer i : invitationSenderDto.getRecipients()) {
			User recipient = this.userService.findOne(i);
			this.validateRecipient(recipient, team);
			Invitation invitationEntity = new Invitation(invitationSenderDto.getMessage(),
					calendar.getTime(), null, principal, recipient, team);
			this.invitationRepository.saveAndFlush(invitationEntity);
		}
	}


	public boolean existsActiveInvitation(User recipient, Team team) {
		return this.invitationRepository.existsActiveInvitation(recipient, team) != 0;
	}

	public void answerInvitation(InvitationRecipientDto invitationRecipientDto) {
		User principal = this.userService.getUserByPrincipal();
		this.validateUserPrincipal(principal);
		this.validateIsAcceptedStatus(invitationRecipientDto.getIsAccepted());
		Invitation invitationEntity = this.invitationRepository.findById(invitationRecipientDto.getId()).orElse(null);
		this.validateInvitationEntityAnswer(invitationEntity);
		this.validatePrincipalCanAnswer(principal, invitationEntity);
		invitationEntity.setIsAccepted(invitationRecipientDto.getIsAccepted());
		Invitation invitationDB = this.invitationRepository.saveAndFlush(invitationEntity);
		if (invitationDB.getIsAccepted() != null && invitationDB.getIsAccepted()) {
			UserRol userRol = new UserRol();
			userRol.setAdmin(false);
			userRol.setTeam(invitationDB.getTeam());
			userRol.setUser(invitationDB.getRecipient());
			this.userRolService.save(userRol);
		}
	}
	
	public List<InvitationListDto> listAllByPrincipal() {
		User principal = this.userService.getUserByPrincipal();
		this.validateUserPrincipal(principal);
		List<InvitationListDto> res = new ArrayList<>();
		for (Invitation invitation : this.invitationRepository.findActiveByRecipient(principal)) {
			InvitationListDto invitationListDto = new InvitationListDto(invitation.getId(), invitation.getTeam().getName(), 
					invitation.getSender().getNick(), invitation.getSender().getPhoto(), invitation.getMessage());
			res.add(invitationListDto);
		}
		return res;
	}

	private void validatePrincipalCanAnswer(User principal, Invitation invitation) {
		if (invitation.getRecipient() != principal) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "The invitation is not addressed to the logged-in user");
		}		
	}

	private void validateInvitationEntityAnswer(Invitation invitationEntity) {
		if (invitationEntity == null) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "The invitation is not in the database");
		}
		if(invitationEntity.getIsAccepted() != null) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "The invitation has been accepted or rejected before");
		}
	}

	private void validateIsAcceptedStatus(Boolean isAccepted) {
		if (isAccepted == null) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The invitation must be accepted or rejected");
		}
	}

	private void validateUserPrincipal(User principal) {
		if (principal == null) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "The user must be logged in");
		}
	}

	private void validateTeam(Team team) {
		if (team == null) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "The team is not in the database");
		}
	}

	private void validateSender(User sender, Team team) {
		if (!this.userRolService.isUserOnTeam(sender, team)) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "The sender must belong to the team");
		}
		if (!this.userRolService.isAdminOnTeam(sender, team)) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,
					"The sender must be an administrator of the team");
		}
	}

	private void validateRecipient(User recipient, Team team) {
		if (recipient == null) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "The recipient is not in the database");
		}
		if (this.userRolService.isUserOnTeam(recipient, team)) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The recipient must not belong to the team");
		}
		if (this.existsActiveInvitation(recipient, team)) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
					"The recipient cannot have active invitations to the team");
		}
	}

	private void validateRecipients(Collection<Integer> recipients) {
		if(recipients == null || recipients.isEmpty()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
					"Recipient cannot be null");	
		}
	}
	
	public void flush() {
		invitationRepository.flush();
	}


}

