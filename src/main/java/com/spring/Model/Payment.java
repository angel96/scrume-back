package com.spring.Model;

import java.time.LocalDateTime;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Payment extends BaseEntity{
	
	@Builder.Default
	private LocalDateTime paymentDate = LocalDateTime.now();
	
	@ManyToOne
	@NotNull
	@JoinColumn(name = "box", nullable = false)
    private Box box;
	
	@ManyToOne
	@NotNull
	@JoinColumn(name = "user", nullable = false)
    private User user;

}
