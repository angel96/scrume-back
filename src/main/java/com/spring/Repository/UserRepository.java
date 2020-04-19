package com.spring.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.spring.Model.User;

@Repository
public interface UserRepository extends AbstractRepository<User> {

	@Query("select u from User u where u.userAccount.username = ?1")
	Optional<User> findByUserAccount(String username);

	@Query("select u from User u where u.nick like ?1%")
	List<User> findByNickStartsWith(String string);

	@Query("select u from User u join u.userAccount ua where ua.username = ?1")
	Optional<User> findUserByUserName(String username);
	
	boolean existsByNick(String nick);
}
