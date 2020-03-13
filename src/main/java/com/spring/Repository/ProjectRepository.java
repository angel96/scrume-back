package com.spring.Repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.spring.Model.Project;
import com.spring.Model.Team;

@Repository
public interface ProjectRepository extends AbstractRepository<Project>{

	List<Project> findByTeam(Team team);
	
	//Para queries nativas se pone nativeQuery=true
}
