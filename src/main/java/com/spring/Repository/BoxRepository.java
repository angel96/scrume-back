package com.spring.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.spring.Model.Box;

@Repository
public interface BoxRepository extends AbstractRepository<Box> {

	@Query("select (select p.box from Payment p where p.user = ur.user order by p.paymentDate DESC) from UserRol ur where ur.team.id = ?1")
	List<Box> getMinimumBoxOfATeam(int team);

}
