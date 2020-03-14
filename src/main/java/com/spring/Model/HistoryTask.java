package com.spring.Model;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@Builder
public class HistoryTask extends BaseEntity {

	@Builder.Default
	private LocalDateTime date = LocalDateTime.now();

	@ManyToOne
	@JoinColumn(name = "origin_id", nullable = false)
	private Column origin;

	@ManyToOne
	@JoinColumn(name = "destiny_id", nullable = false)
	private Column destiny;

	@ManyToOne
	@JoinColumn(name = "task_id", nullable = false)
	private Task task;
}
