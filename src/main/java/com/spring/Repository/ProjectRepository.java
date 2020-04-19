package com.spring.Repository;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.spring.Model.Project;
import com.spring.Model.Team;

@Repository
public interface ProjectRepository extends AbstractRepository<Project>{

	@Query("select p from Project p where p.team.id = ?1")
	Collection<Project> findProjectsByTeamId(Integer id);
    
    List<Project> findByTeam(Team team);

	@Query("select p from Project p where p.team = ?1 order by p.id asc")
	List<Project> getFirstProjectOfATeam(Team team);
}

	