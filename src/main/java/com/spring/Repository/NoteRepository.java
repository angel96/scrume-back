package com.spring.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.spring.Model.Note;
import com.spring.Model.User;

@Repository
public interface NoteRepository extends AbstractRepository<Note>{
	
	@Query("select n from Note n where n.user = ?1")
	List<Note> findByUser(User user);
	

}
