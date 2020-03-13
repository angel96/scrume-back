package com.spring.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.spring.Model.Box;

@Repository
public interface BoxRepository extends JpaRepository<Box, Integer> {

	Box save(Box box);

}
