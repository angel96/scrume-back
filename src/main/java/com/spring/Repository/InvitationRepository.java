package com.spring.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.spring.Model.Invitation;

public interface InvitationRepository extends JpaRepository<Invitation, Integer> {

}
