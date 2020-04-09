package com.spring.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.spring.Model.Document;
import com.spring.Model.Sprint;

@Repository
public interface DocumentRepository extends AbstractRepository<Document> {
	List<Document> findBySprint(Sprint sprint);

	@Query(value = "select d.id from Document d where d.sprint_id = ?1 and d.type = 'DAILY' order by d.id desc LIMIT 1", nativeQuery = true)
	Optional<Integer> getDaily(Sprint sprint);
	
		

}
