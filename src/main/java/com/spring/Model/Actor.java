package com.spring.Model;

import javax.persistence.JoinColumn;
import javax.persistence.MappedSuperclass;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@MappedSuperclass
@NoArgsConstructor
@AllArgsConstructor
public abstract class Actor extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7616122021941518198L;

	@NotNull
	@OneToOne(optional = false)
	@JoinColumn(name = "user_account_id", nullable = false)
	private UserAccount userAccount;

}
