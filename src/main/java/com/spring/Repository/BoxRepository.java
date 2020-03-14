package com.spring.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.spring.Model.Box;
import com.spring.Model.Team;

@Repository
public interface BoxRepository extends AbstractRepository<Box> {

	Box save(Box box);

	@Query("select b from UserRol ur join ur.user u join u.box b where ur.team = ?1 order by b.price asc")
	List<Box> getMinimumBoxOfATeam(Team team);
	
}
