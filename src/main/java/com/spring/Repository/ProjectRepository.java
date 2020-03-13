package com.spring.Repository;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.spring.Model.Project;

@Repository
public interface ProjectRepository extends AbstractRepository<Project>{

	@Query("select p from Project p where p.team.id = ?1")
	Collection<Project> findProjectsByTeamId(Integer id);
}
