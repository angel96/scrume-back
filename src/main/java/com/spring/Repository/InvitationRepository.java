package com.spring.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.spring.Model.Invitation;
import com.spring.Model.Team;
import com.spring.Model.User;

public interface InvitationRepository extends JpaRepository<Invitation, Integer> {

	@Query("select Count(*) from Invitation i where i.recipient = ?1 and i.team = ?2 and i.isAccepted is null and i.validDate < CURRENT_DATE")
	Integer existsActiveInvitation(User recipient, Team team);

}
