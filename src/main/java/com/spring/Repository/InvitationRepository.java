package com.spring.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.spring.model.Invitation;
import com.spring.model.Team;
import com.spring.model.User;

@Repository
public interface InvitationRepository extends AbstractRepository<Invitation> {

	@Query("select Count(i) from Invitation i where i.recipient = ?1 and i.team = ?2 and i.isAccepted is null and i.validDate > CURRENT_DATE")
	Integer existsActiveInvitation(User recipient, Team team);

}
