package com.spring.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.spring.Model.Estimation;
import com.spring.Model.Task;
import com.spring.Model.User;

@Repository
public interface EstimationRepository extends AbstractRepository<Estimation> {

	@Query("select count(e) from Estimation e where e.task = ?1")
	Integer getNumberOfEstimatesOfATask(Task task);

	@Query("select avg(e.points) from Estimation e where e.task = ?1")
	Integer getFinalEstimationOfATask(Task task);
	
	@Query("select count(e) from Estimation e where e.task = ?2 and e.user = ?1")
	Integer getNumberOfEstimatesOfAnUser(User principal, Task task);

	List<Estimation> findByUser(User principal);

	@Query("select e from Estimation e where e.task = ?1 and e.user = ?2")
	Optional<Estimation> findByTask(Task task, User user);

}
