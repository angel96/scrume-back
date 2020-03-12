package com.spring.Service;

import java.util.Calendar;
import java.util.Date;

import javax.transaction.Transactional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

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

	public InvitationSenderDto save(InvitationSenderDto invitationSenderDto) throws Exception {
		User principal = this.userService.getUserByPrincipal();
		Assert.notNull(principal, "The user must be logged in");
		
		Integer recipientId = invitationSenderDto.getRecipient().getId();
		Assert.notNull(recipientId, "The id of the recipient cannot be null");
		User recipient = this.userService.findOne(recipientId);
		
		Integer teamId = invitationSenderDto.getTeam().getId();
		Assert.notNull(teamId, "The id of the team cannot be null");
		Team team = this.teamService.findOne(teamId);
		
		Assert.isTrue(this.userRolService.isUserOnTeam(principal, team), "The sender must belong to the team");
		
		Assert.isTrue(this.userRolService.isAdminOnTeam(principal), "The sender must be an administrator of the team");

		Assert.isTrue(!this.userRolService.isUserOnTeam(recipient, team), "The recipient must not belong to the team");

		Assert.isTrue(!this.existsActiveInvitation(recipient, team), "The recipient cannot have active invitations to the team");
		
		Invitation invitationEntity = new Invitation();
		invitationEntity.setMessage(invitationSenderDto.getMessage());
		invitationEntity.setRecipient(recipient);
		invitationEntity.setTeam(team);
		
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.add(Calendar.DAY_OF_YEAR, 30);
		invitationEntity.setValidDate(calendar.getTime());
		
		invitationEntity.setSender(principal);
		
		Invitation invitationDB = this.invitationRepository.save(invitationEntity);
		return new ModelMapper().map(invitationDB, InvitationSenderDto.class);
	}

	private boolean existsActiveInvitation(User recipient, Team team) {
		return this.invitationRepository.existsActiveInvitation(recipient, team) != 0;
	}

	public InvitationRecipientDto answerInvitation(InvitationRecipientDto invitationRecipientDto) throws Exception {

		Assert.notNull(invitationRecipientDto.getIsAccepted(), "The invitation must be accepted or rejected");

		Invitation invitationEntity = this.invitationRepository.getOne(invitationRecipientDto.getId());
		invitationEntity.setIsAccepted(invitationRecipientDto.getIsAccepted());
		Invitation invitationDB = this.invitationRepository.save(invitationEntity);
		if (invitationDB.getIsAccepted()) {
			UserRol userRol = new UserRol();
			userRol.setAdmin(false);
			userRol.setTeam(invitationDB.getTeam());
			userRol.setUser(invitationDB.getRecipient());
			this.userRolService.save(userRol);
		}
		return new ModelMapper().map(invitationDB, InvitationRecipientDto.class);
	}

}
