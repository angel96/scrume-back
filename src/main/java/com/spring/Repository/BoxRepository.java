package com.spring.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.spring.Model.Box;

@Repository
public interface BoxRepository extends AbstractRepository<Box> {

	@Query("select p.box from Payment p, UserRol ur where ur.team.id = ?1 and p.userAccount = ur.user.userAccount and p.expiredDate >= CURRENT_DATE")
	List<Box> getBoxMoreRecently(int team);

}
