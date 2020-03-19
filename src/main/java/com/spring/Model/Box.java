package com.spring.Model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.SafeHtml;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@EqualsAndHashCode(callSuper = true)
public class Box extends BaseEntity{
	
	@NotBlank
	@NotNull
	@Pattern(regexp = "BASIC|STANDARD|PRO")
	@Column(name = "name", nullable = false)
	@SafeHtml
    private String name;
 
	@Min(0)
	@NotNull
	@Column(name = "price", nullable = false)
    private Double price;
	

}
