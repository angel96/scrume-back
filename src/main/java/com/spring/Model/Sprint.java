package com.spring.Model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)

public class Sprint extends BaseEntity {
	
	/**
	 * 
	 */
	
	@NotEmpty
	@JsonProperty
	private Date startDate;
	
	@NotEmpty
	@JsonProperty
	private Date endDate;
	
	@JsonProperty
	@ManyToOne
	private Project project;
	

}
