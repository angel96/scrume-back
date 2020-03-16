package com.spring.Model;

import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotBlank;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

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
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JoinColumn(name = "sprint", nullable = false)
	private Sprint sprint;

}
