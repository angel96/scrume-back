package com.spring.Repository;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.spring.Model.Team;
import com.spring.Model.UserRol;
import com.spring.Model.Workspace;

@Repository
public interface WorkspaceRepository extends AbstractRepository<Workspace> {

	@Query("select w from Workspace w where w.sprint.project.team.id = ?1")
	Collection<Workspace> findWorkspacesByTeam(int id);

	@Query("select uRol.team from UserRol uRol where uRol.user.userAccount.id = ?1 and uRol.admin = true")
	Collection<Team> findAllTeamsByUserAccountAdmin(int userAccount);

	@Query("select uRol from UserRol uRol where uRol.user.userAccount.id = ?1 and uRol.team.id = ?1")
	UserRol findUserRoleByUserAccountAndTeam(int userAccount, int team);
}
