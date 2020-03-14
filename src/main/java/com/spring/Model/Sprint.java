package com.spring.Model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class Sprint extends BaseEntity {

	@NotNull
	@JsonProperty
	@Column(name = "startDate")
	private Date startDate;

	@NotNull
	@JsonProperty
	@Column(name = "endDate")
	private Date endDate;

	@JsonProperty
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "project")
	private Project project;
}
