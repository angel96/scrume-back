package com.spring.Service;

import java.util.Calendar;
import java.util.Date;

import javax.transaction.Transactional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spring.CustomObject.InvitationRecipientDto;
import com.spring.CustomObject.InvitationSenderDto;
import com.spring.Model.Invitation;
import com.spring.Model.Team;
import com.spring.Model.User;
import com.spring.Repository.InvitationRepository;

@Service
@Transactional
public class InvitationService extends AbstractService{

	@Autowired
	private InvitationRepository invitationRepository;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private TeamService teamService;
	
	public InvitationSenderDto save(InvitationSenderDto invitationSenderDto) throws Exception {
		try {
			User principal = this.userService.getUserByPrincipal();
			Invitation invitationEntity = new Invitation();
			invitationEntity.setMessage(invitationSenderDto.getMessage());
			User recipient = this.userService.findOne(invitationSenderDto.getRecipient().getId());
			invitationEntity.setRecipient(recipient);
			Team team = this.teamService.findOne(invitationSenderDto.getTeam().getId());
			invitationEntity.setTeam(team);
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(new Date()); 
			calendar.add(Calendar.DAY_OF_YEAR, 30);
			invitationEntity.setValidDate(calendar.getTime());
			invitationEntity.setSender(principal);
			Invitation invitationDB = this.invitationRepository.save(invitationEntity);
			return new ModelMapper().map(invitationDB, InvitationSenderDto.class);
		}catch(Exception e) {
			throw new Exception("Error when saving the invitation");
		}
	}

	public InvitationRecipientDto answerInvitation(InvitationRecipientDto invitationRecipientDto) throws Exception {
		try {
			Invitation invitationEntity = this.invitationRepository.getOne(invitationRecipientDto.getId());
			invitationEntity.setIsAccepted(invitationRecipientDto.getIsAccepted());
			Invitation invitationDB = this.invitationRepository.save(invitationEntity);
			return new ModelMapper().map(invitationDB, InvitationRecipientDto.class);
		}catch(Exception e) {
			throw new Exception("Error when saving the invitation");
		}
	}
	
}
