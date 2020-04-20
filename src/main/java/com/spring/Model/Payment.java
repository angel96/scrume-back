package com.spring.Model;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.format.annotation.DateTimeFormat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@EqualsAndHashCode(callSuper=true)
public class Payment extends BaseEntity {

	@Builder.Default
	private LocalDate paymentDate = LocalDate.now();

	@ManyToOne(optional = false)
	@NotNull
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JoinColumn(name = "box", nullable = false)
	private Box box;

	@ManyToOne(optional = false)
	@NotNull
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JoinColumn(name = "user", nullable = false)
	private UserAccount userAccount;

	@DateTimeFormat(pattern = "yyyy/MM/dd")
	private LocalDate expiredDate;

	
	private String orderId;
	
	
	private String payerId;
}
