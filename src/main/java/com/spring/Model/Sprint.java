package com.spring.Model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)

public class Sprint extends BaseEntity {
	
	@NotNull
	@JsonProperty
	private Date startDate;
	
	@NotNull
	@JsonProperty
	private Date endDate;
	
	@JsonProperty
	@ManyToOne
	private Project project;
	

}