package com.spring.Repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.spring.Model.Note;
import com.spring.Model.User;

@Repository
public interface NoteRepository extends AbstractRepository<Note>{
	
	List<Note> findByUser(User user);
	

}
