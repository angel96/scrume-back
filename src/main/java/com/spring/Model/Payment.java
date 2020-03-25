package com.spring.Model;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class Payment extends BaseEntity {

	@Builder.Default
	private LocalDate paymentDate = LocalDate.now();

	@ManyToOne
	@NotNull
	@JoinColumn(name = "box", nullable = false)
	private Box box;

	@ManyToOne
	@NotNull
	@JoinColumn(name = "user", nullable = false)
	private UserAccount userAccount;

	@DateTimeFormat(pattern = "yyyy/MM/dd")
	private LocalDate expiredDate;

}
