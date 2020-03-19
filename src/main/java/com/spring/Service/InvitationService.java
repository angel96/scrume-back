package com.spring.Service;

import java.util.Calendar;
import java.util.Date;

import javax.transaction.Transactional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

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
		
		User recipient = this.userService.findOne(invitationSenderDto.getRecipient().getId());

		Team team = this.teamService.findOne(invitationSenderDto.getTeam().getId());
		this.validateTeam(team);
		
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
		
		Invitation invitationDB = this.invitationRepository.saveAndFlush(invitationEntity);
		return modelMapper.map(invitationDB, InvitationSenderDto.class);
	}

	private boolean existsActiveInvitation(User recipient, Team team) {
		return this.invitationRepository.existsActiveInvitation(recipient, team) != 0;
	}

	public InvitationRecipientDto answerInvitation(InvitationRecipientDto invitationRecipientDto) throws Exception {

		this.validateIsAcceptedStatus(invitationRecipientDto.getIsAccepted());
		Invitation invitationEntity = this.invitationRepository.findById(invitationRecipientDto.getId()).orElse(null);
		this.validateInvitationEntityAnswer(invitationEntity);
		invitationEntity.setIsAccepted(invitationRecipientDto.getIsAccepted());
		Invitation invitationDB = this.invitationRepository.saveAndFlush(invitationEntity);
		if (invitationDB.getIsAccepted() != null && invitationDB.getIsAccepted()) {
			UserRol userRol = new UserRol();
			userRol.setAdmin(false);
			userRol.setTeam(invitationDB.getTeam());
			userRol.setUser(invitationDB.getRecipient());
			this.userRolService.save(userRol);
		}
		return new ModelMapper().map(invitationDB, InvitationRecipientDto.class);
	}
	
	private void validateInvitationEntityAnswer(Invitation invitationEntity) {
		if(invitationEntity == null) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "The invitation is not in the database");
		}
	}

	private void validateIsAcceptedStatus(Boolean isAccepted) {
		if(isAccepted == null) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The invitation must be accepted or rejected");
		}
	}

	private void validateUserPrincipal(User principal){
		if(principal == null) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "The user must be logged in");
		}
	}

	
	private void validateTeam(Team team){
		if(team == null) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "The team is not in the database");
		}
	}
	
	private void validateSender(User sender, Team team){
		if(!this.userRolService.isUserOnTeam(sender, team)) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "The sender must belong to the team");
		}
		if(!this.userRolService.isAdminOnTeam(sender, team)) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "The sender must be an administrator of the team");
		}
	}
	
	private void validateRecipient(User recipient, Team team){
		if(recipient == null) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "The recipient is not in the database");
		}
		if(this.userRolService.isUserOnTeam(recipient, team)) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The recipient must not belong to the team");
		}
		if(this.existsActiveInvitation(recipient, team)) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The recipient cannot have active invitations to the team");
		}
	}

}
