package com.spring.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.validator.constraints.SafeHtml;
import org.springframework.format.annotation.DateTimeFormat;

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
public class Invitation extends BaseEntity{

	@NotBlank
	@NotNull
	@Column(name = "message", nullable = false)
	@SafeHtml
    private String message;
 
	@DateTimeFormat
	@NotNull
	@Column(name = "validDate", nullable = false)
    private Date validDate;
	
	@Column(name = "isAccepted", nullable = true)
    private Boolean isAccepted;
	
	@ManyToOne
	@NotNull
	@JoinColumn(name = "sender", nullable = false)
    private User sender;
	
	@ManyToOne
	@NotNull
	@JoinColumn(name = "recipient", nullable = false)
    private User recipient;
	
	@ManyToOne
	@NotNull
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JoinColumn(name = "team", nullable = false)
    private Team team;
}
