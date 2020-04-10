package com.spring.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.spring.Model.Invitation;
import com.spring.Model.Team;
import com.spring.Model.User;

@Repository
public interface InvitationRepository extends AbstractRepository<Invitation> {

	@Query("select Count(i) from Invitation i where i.recipient = ?1 and i.team = ?2 and i.isAccepted is null and i.validDate > CURRENT_DATE")
	Integer existsActiveInvitation(User recipient, Team team);

	@Query("select i from Invitation i where i.recipient = ?1 and i.validDate > CURRENT_DATE and i.isAccepted = null")
	List<Invitation> findActiveByRecipient(User principal);

}
