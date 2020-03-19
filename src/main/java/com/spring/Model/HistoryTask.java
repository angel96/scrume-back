package com.spring.model;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@Builder
@EqualsAndHashCode(callSuper = true)
public class HistoryTask extends BaseEntity {

	@Builder.Default
	private LocalDateTime date = LocalDateTime.now();

	@ManyToOne
	@JoinColumn(name = "origin", nullable = false)
	@OnDelete(action = OnDeleteAction.CASCADE)
	private Column origin;

	@ManyToOne
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JoinColumn(name = "destiny", nullable = false)
	private Column destiny;

	@ManyToOne
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JoinColumn(name = "task", nullable = false)
	private Task task;
}
