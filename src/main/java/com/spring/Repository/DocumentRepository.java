package com.spring.Repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.spring.Model.Document;
import com.spring.Model.Sprint;

@Repository
public interface DocumentRepository extends AbstractRepository<Document> {
	List<Document> findBySprint(Sprint sprint);
	
		

}
