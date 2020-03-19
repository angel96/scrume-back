package com.spring.repository;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.spring.model.Project;
import com.spring.model.Team;

@Repository
public interface ProjectRepository extends AbstractRepository<Project>{

	@Query("select p from Project p where p.team.id = ?1")
	Collection<Project> findProjectsByTeamId(Integer id);
    
    List<Project> findByTeam(Team team);
}

	