package com.spring.Repository;

import org.springframework.stereotype.Repository;

import com.spring.Model.Box;

@Repository
public interface BoxRepository extends AbstractRepository<Box> {

	Box save(Box box);

}
