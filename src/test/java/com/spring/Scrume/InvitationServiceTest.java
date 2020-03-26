package com.spring.Scrume;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.server.ResponseStatusException;

import com.spring.CustomObject.InvitationRecipientDto;
import com.spring.CustomObject.InvitationSenderDto;
import com.spring.Service.InvitationService;

public class InvitationServiceTest extends AbstractTest {

	@Autowired
	private InvitationService invitationService;
	

	@Test
	public void invitationServiceSaveTest() throws Exception {
		List<Integer> recipients1 = new ArrayList<>();
		recipients1.add(super.entities().get("user1"));
		InvitationSenderDto invitationSenderDto1 = new InvitationSenderDto("message1", recipients1, super.entities().get("team2"));
	
		List<Integer> recipients2 = new ArrayList<>();
		recipients2.add(super.entities().get("user2"));
		InvitationSenderDto invitationSenderDto2 = new InvitationSenderDto("message1", recipients2, super.entities().get("team1"));
		
		List<Integer> recipients3 = new ArrayList<>();
		InvitationSenderDto invitationSenderDto3 = new InvitationSenderDto("message1", recipients3, super.entities().get("team2"));
	
		List<Integer> recipients4 = new ArrayList<>();
		recipients4.add(super.entities().get("user1"));
		InvitationSenderDto invitationSenderDto4 = new InvitationSenderDto("message1", recipients4,23454321);
		
		Object[][] objects = {
				{"testuser2@gmail.com", invitationSenderDto4, ResponseStatusException.class},
				{"testuser2@gmail.com", invitationSenderDto3, ResponseStatusException.class},
				{"testuser1@gmail.com", invitationSenderDto2, ResponseStatusException.class},
				{"testuser4@gmail.com", invitationSenderDto1, ResponseStatusException.class},
				{"testuser4@gmail.com", invitationSenderDto2, ResponseStatusException.class},
				{"testuser2@gmail.com", invitationSenderDto1, null}};

		Stream.of(objects).forEach(x -> driverInvitationServiceSaveTest((String) x[0], (InvitationSenderDto) x[1], (Class<?>) x[2]));
	}

	protected void driverInvitationServiceSaveTest(String user, InvitationSenderDto invitationSenderDto, Class<?> expected) {
		Class<?> caught = null;
		try {
			super.authenticateOrUnauthenticate(user);
			this.invitationService.save(invitationSenderDto);
			this.invitationService.flush();
			super.authenticateOrUnauthenticate(null);

		} catch (Exception oops) {
			caught = oops.getClass();
		}
		super.checkExceptions(expected, caught);
	}
	
	@Test
	public void invitationServiceListTest() throws Exception {
		Object[][] objects = {
				{"testuser2@gmail.com", null}};

		Stream.of(objects).forEach(x -> driverInvitationServiceListTest((String) x[0], (Class<?>) x[1]));
	}

	protected void driverInvitationServiceListTest(String user, Class<?> expected) {
		Class<?> caught = null;
		try {
			super.authenticateOrUnauthenticate(user);
			this.invitationService.listAllByPrincipal();
			this.invitationService.flush();
			super.authenticateOrUnauthenticate(null);

		} catch (Exception oops) {
			caught = oops.getClass();
		}
		super.checkExceptions(expected, caught);
	}
	
	@Test
	public void invitationServiceAnswerTest() throws Exception {
		
		InvitationRecipientDto invitationRecipientDto1 = new InvitationRecipientDto(super.entities().get("invitation3"), true);
		InvitationRecipientDto invitationRecipientDto2 = new InvitationRecipientDto(super.entities().get("invitation2"), true);

		Object[][] objects = {
				{"testuser3@gmail.com", invitationRecipientDto1, ResponseStatusException.class},
				{"testuser3@gmail.com", invitationRecipientDto2, ResponseStatusException.class},
				{"testuser2@gmail.com", invitationRecipientDto1, null}};

		Stream.of(objects).forEach(x -> driverInvitationServiceAnswerTest((String) x[0], (InvitationRecipientDto) x[1], (Class<?>) x[2]));
	}

	protected void driverInvitationServiceAnswerTest(String user, InvitationRecipientDto invitationRecipientDto, Class<?> expected) {
		Class<?> caught = null;
		try {
			super.authenticateOrUnauthenticate(user);
			this.invitationService.answerInvitation(invitationRecipientDto);
			this.invitationService.flush();
			super.authenticateOrUnauthenticate(null);

		} catch (Exception oops) {
			caught = oops.getClass();
		}
		super.checkExceptions(expected, caught);
	}
	
}