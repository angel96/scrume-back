package com.spring.Model;

import javax.persistence.MappedSuperclass;
import javax.persistence.OneToOne;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

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

	@OneToOne
	private UserAccount userAccount;

}
