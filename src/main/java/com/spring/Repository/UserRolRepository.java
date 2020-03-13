package com.spring.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.spring.Model.Team;
import com.spring.Model.User;
import com.spring.Model.UserRol;

public interface UserRolRepository extends JpaRepository<UserRol, Long> {

	Boolean existsByUserAndTeam(User user, Team team);
	Boolean existsByUserAndAdmin(User user, Boolean admin);

}
