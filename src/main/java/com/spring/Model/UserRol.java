package com.spring.Model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

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
@EqualsAndHashCode(callSuper = true)
public class UserRol extends BaseEntity{

	@NotNull
	@Column(name = "admin", nullable = false)
    private Boolean admin;
	
	@ManyToOne
	@NotNull
	@JoinColumn(name = "user", nullable = false)
    private User user;
	
	@ManyToOne
	@NotNull
	@JoinColumn(name = "team", nullable = false)
    private Team team;
}
