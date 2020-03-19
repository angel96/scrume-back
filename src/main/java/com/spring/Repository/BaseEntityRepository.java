package com.spring.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.spring.model.BaseEntity;

@Repository
public interface BaseEntityRepository extends JpaRepository<BaseEntity, Integer> {

}
