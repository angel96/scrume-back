package com.spring.Model;

import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class Project extends BaseEntity{
	@NotBlank
	@NotNull
	@JsonProperty
	private String name;
	
	@NotBlank
	@NotNull
	@JsonProperty
	private String description;
	
	@JsonProperty
	@ManyToOne
	private Team team;
}
