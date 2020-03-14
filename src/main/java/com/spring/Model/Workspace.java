package com.spring.Model;

import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class Workspace extends BaseEntity {

	@NotBlank(message = "No puede ser vacio el nombre")
	private String name;
	@ManyToOne(optional = false)
	@JoinColumn(name = "sprint_id", nullable = false)
	private Sprint sprint;

}
