package com.spring.Repository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.spring.Model.Document;
import com.spring.Model.DocumentType;
import com.spring.Model.Sprint;

@Repository
public interface DocumentRepository extends AbstractRepository<Document> {
	List<Document> findBySprint(Sprint sprint);

	@Query(value = "select d.id from Document d where d.sprint = ?1 and d.name like '%DAILY%' and d.date between ?2 and ?3")
	List<Integer> getDaily(Sprint sprint, LocalDateTime startDate, LocalDateTime endDate);

	@Query(value = "select d from Document d where d.sprint = ?3 and d.type = ?4 and d.date between ?1 and ?2")
	Collection<Document> findByDateAndSprintAndType(LocalDateTime startDate, LocalDateTime endDate, Sprint sprint,
			DocumentType type);
	
		

}
