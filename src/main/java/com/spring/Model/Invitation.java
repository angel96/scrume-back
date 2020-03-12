package com.spring.Model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity

public class Invitation extends BaseEntity{

	@NotBlank
	@NotNull
	@Column(name = "message", nullable = false)
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
	@JoinColumn(name = "team", nullable = false)
    private Team team;
}
