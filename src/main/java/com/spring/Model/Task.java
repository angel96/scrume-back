package com.spring.Model;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.validator.constraints.SafeHtml;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@EqualsAndHashCode(callSuper = true)
public class Task extends BaseEntity {

	@JsonProperty
	@NotBlank
	@SafeHtml
	private String title;
	
	@JsonProperty
	@NotBlank
	@SafeHtml
	private String description;
	
	@JsonProperty
	@Min(value = 0)
	private Integer points;
	
	@ManyToOne
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JsonProperty
	@NotNull
	private Project project;

	@ManyToMany
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JsonProperty
	private Set<User> users;

	@ManyToOne
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JoinColumn(name = "`column`")
	@JsonProperty
	private Column column;
}
