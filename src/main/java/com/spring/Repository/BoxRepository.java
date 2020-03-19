package com.spring.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.spring.model.Box;
import com.spring.model.Team;

@Repository
public interface BoxRepository extends AbstractRepository<Box> {

	@Query("select b from UserRol ur join ur.user u join u.box b where ur.team = ?1 order by b.price asc")
	List<Box> getMinimumBoxOfATeam(Team team);

}
