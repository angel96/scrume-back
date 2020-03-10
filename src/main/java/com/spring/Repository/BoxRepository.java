package com.spring.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.spring.Model.Box;

public interface BoxRepository extends JpaRepository<Box, Integer> {

	Box save(Box box);

}
