package com.spring.Model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Box extends BaseEntity{
	
	@NotBlank
	@NotNull
	@Column(name = "name", nullable = false)
    private String name;
 
	@Min(0)
	@NotNull
	@Column(name = "price", nullable = false)
    private Double price;
	

}
