package com.spring.Repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.spring.Model.Project;
import com.spring.Model.Sprint;

@Repository
public interface SprintRepository extends AbstractRepository<Sprint> {

	@Query("select s.id from Sprint s where s.project = ?1 order by s.startDate asc")
	List<Integer> findBySprintsOrdered(Project idProject);

	@Query("select count(s) from Sprint s where s.id != ?4 and s.project = ?3 and ((s.startDate <= ?1 and s.endDate >= ?1) or (s.startDate <= ?2 and s.endDate >= ?2) or (s.startDate >= ?1 and s.endDate <= ?2))")
	Integer areValidDates(Date startDate, Date endDate, Project project, Integer idSprint);
}
