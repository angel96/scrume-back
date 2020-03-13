package com.spring.Service;

import java.util.Calendar;
import java.util.Date;

import javax.transaction.Transactional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.web.client.HttpClientErrorException;

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
		ModelMapper modelMapper = new ModelMapper();
		User principal = this.userService.getUserByPrincipal();
		this.validateUserPrincipal(principal);
		
		Integer recipientId = invitationSenderDto.getRecipient().getId();
		this.validateRecipientId(recipientId);
		
		Integer teamId = invitationSenderDto.getTeam().getId();
		this.validateTeamId(recipientId);
		
		User recipient = this.userService.findOne(recipientId);

		Team team = this.teamService.findOne(teamId);
		
		this.validateSender(principal, team);

		this.validateRecipient(recipient, team);
		
		Invitation invitationEntity = modelMapper.map(invitationSenderDto, Invitation.class);
		invitationEntity.setRecipient(recipient);
		invitationEntity.setTeam(team);
		
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.add(Calendar.DAY_OF_YEAR, 30);
		invitationEntity.setValidDate(calendar.getTime());
		
		invitationEntity.setSender(principal);
		
		Invitation invitationDB = this.invitationRepository.save(invitationEntity);
		return modelMapper.map(invitationDB, InvitationSenderDto.class);
	}

	private boolean existsActiveInvitation(User recipient, Team team) {
		return this.invitationRepository.existsActiveInvitation(recipient, team) != 0;
	}

	//TODO Añadir validaciones a este metodo
	public InvitationRecipientDto answerInvitation(InvitationRecipientDto invitationRecipientDto) throws Exception {

		Assert.notNull(invitationRecipientDto.getIsAccepted(), "The invitation must be accepted or rejected");

		Invitation invitationEntity = this.invitationRepository.findById(invitationRecipientDto.getId()).orElse(null);
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
	
	private void validateUserPrincipal(User principal) throws Exception{
		if(principal == null) {
			throw new HttpClientErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "The user must be logged in");
		}
	}
	
	private void validateRecipientId(Integer recipientId) throws Exception{
		if(recipientId == null) {
			throw new HttpClientErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "The id of the recipient cannot be null");
		}
	}
	
	private void validateTeamId(Integer teamId) throws Exception{
		if(teamId == null) {
			throw new HttpClientErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "The id of the team cannot be null");
		}
	}
	
	private void validateSender(User sender, Team team) throws Exception{
		if(!this.userRolService.isUserOnTeam(sender, team)) {
			throw new HttpClientErrorException(HttpStatus.UNAUTHORIZED, "The sender must belong to the team");
		}if(!this.userRolService.isAdminOnTeam(sender)) {
			throw new HttpClientErrorException(HttpStatus.UNAUTHORIZED, "The sender must be an administrator of the team");
		}
	}
	
	private void validateRecipient(User recipient, Team team) throws Exception{
		if(this.userRolService.isUserOnTeam(recipient, team)) {
			throw new HttpClientErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "The recipient must not belong to the team");
		}if(this.existsActiveInvitation(recipient, team)) {
			throw new HttpClientErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "The recipient cannot have active invitations to the team");
		}
	}

}
