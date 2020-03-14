package com.spring.Repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.spring.CustomObject.SprintDto;
import com.spring.Model.Project;
import com.spring.Model.Sprint;

@Repository
public interface SprintRepository extends AbstractRepository<Sprint> {

	@Query("select s from Sprint s where s.project = ?1 order by s.startDate asc")
	List<Sprint> findBySprintsOrdered(Project idProject);

	@Query("select count(s) from Sprint s where s.project = ?3 and s.endDate > CURRENT_DATE and ((s.startDate < ?1 and s.endDate < ?1) or (s.startDate > ?2 and s.endDate > ?2))")
	Integer areValidDates(Date startDate, Date endDate, Project project);
}
