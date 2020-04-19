package com.spring.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.spring.Model.Team;
import com.spring.Model.User;
import com.spring.Model.UserRol;

@Repository
public interface UserRolRepository extends AbstractRepository<UserRol> {
			
	Boolean existsByUserAndTeam(User user, Team team);
	Boolean existsByUserAndAdminAndTeam(User user, Boolean admin, Team team);
	Optional<UserRol> findByUserAndTeam(User user, Team team);
	
	@Query("select count(u) from UserRol u where u.team = ?1")
	Integer getNumberOfUsersOfTeam(Team team);
	
	@Query("select count(u) from UserRol u where u.team = ?1 and u.admin = 1")
	Integer getNumberOfAdminsOfTeam(Team team);
	
	@Query("select ur.user from UserRol ur where ur.user != ?1 and ur.team = ?2")
	List<User> getAnotherUserNoAdmin(User user, Team team);
	
	@Query("select ur.team from UserRol ur where ur.user = ?1")
	List<Team> findByUser(User principal);
	
	@Query("select ur.user.id from UserRol ur where ur.team = ?1")
	Collection<Integer> findIdUsersByTeam(Team team);
	
	@Query("select ur.user from UserRol ur where ur.team = ?1")
	Collection<User> findUsersByTeam(Team team);
	
	@Query("select ur from UserRol ur where ur.user = ?1")
	Collection<UserRol> findAllUserRolesByUser(User user);
	
	@Query("select ur from UserRol ur where ur.team = ?1")
	Collection<UserRol> findUserRolesByTeam(Team team);
	
	@Query("select ur from UserRol ur where ur.user = ?1")
	Collection<UserRol> findUserRolesByUser(User user);
	
	
}
