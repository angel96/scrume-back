package com.spring.Model;

import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;import javax.persistence.Column;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class Box extends BaseEntity{
	@NotBlank
	@NotNull
	@Pattern(regexp = "BASIC|STANDARD|PRO")
	@Column(name = "name", nullable = false)
    private String name;
 
	@Min(0)
	@NotNull
	@Column(name = "price", nullable = false)
    private Double price;
}
