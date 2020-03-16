package com.spring.Model;

import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotEmpty;

import org.hibernate.validator.constraints.SafeHtml;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class Profile extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6048703424455966606L;

	@NotEmpty
	@JsonProperty
	@SafeHtml
	private String name;
	
	@JsonProperty
	@ManyToOne
	private Administrator administrator;
}
