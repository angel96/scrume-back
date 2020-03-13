package com.spring.Repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.spring.Model.Project;
import com.spring.Model.Sprint;

@Repository
public interface SprintRepository extends AbstractRepository<Sprint> {

	@Query(value = "select * from Sprint s where s.project = ?1 order by s.end_date DESC limit 1", nativeQuery = true)
	Sprint getPreviousSprintCreate(Project project);
	
	@Query(value = "select * from Sprint s where s.project = (select sp.project from Sprint sp where sp.id = ?1) and s.id != ?1 order by s.end_date DESC limit 1", nativeQuery = true)
	Sprint getPreviousSprintUpdate(Sprint sprint);

}
