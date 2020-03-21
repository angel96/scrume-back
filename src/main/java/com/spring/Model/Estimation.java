package com.spring.Model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

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
public class Estimation extends BaseEntity{

	
	@NotNull
	@Column(name = "points", nullable = false)
    private Integer points;
	
	@ManyToOne
	@NotNull
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JoinColumn(name = "user", nullable = false)
    private User user;
	
	@ManyToOne
	@NotNull
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JoinColumn(name = "task", nullable = false)
    private Task task;
	
}
