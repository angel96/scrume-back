package com.spring.Repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.spring.Model.PersonalTaskList;
import com.spring.Model.User;

@Repository
public interface PersonalTaskListRepository extends AbstractRepository<PersonalTaskList>{
	
	List<PersonalTaskList> findByUser(User user);
	

}
