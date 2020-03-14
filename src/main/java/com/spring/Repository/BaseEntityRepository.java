package com.spring.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.spring.Model.BaseEntity;

@Repository
public interface BaseEntityRepository extends JpaRepository<BaseEntity, Integer> {

}
