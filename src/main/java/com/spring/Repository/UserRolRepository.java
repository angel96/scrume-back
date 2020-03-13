package com.spring.Repository;

import org.springframework.stereotype.Repository;

import com.spring.Model.Team;
import com.spring.Model.User;
import com.spring.Model.UserRol;

@Repository
public interface UserRolRepository extends AbstractRepository<UserRol> {
			
	Boolean existsByUserAndTeam(User user, Team team);
	Boolean existsByUserAndAdminAndTeam(User user, Boolean admin, Team team);

}
