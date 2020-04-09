package com.spring.Model;

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
public class Notification extends BaseEntity{
	
	@NotBlank
	@NotNull
	@Column(name = "title", nullable = false)
	@SafeHtml
	private String title;

	@DateTimeFormat
	@NotNull
	@Column(name = "date", nullable = false)
    private Date date;
	
	@ManyToOne
	@NotNull
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JoinColumn(name = "sprint", nullable = false)
    private Sprint sprint;
	
	@ManyToOne
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JoinColumn(name = "user", nullable = true)
	private User user;
	
}
