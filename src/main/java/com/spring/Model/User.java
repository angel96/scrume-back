package com.spring.Model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.SafeHtml;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@EqualsAndHashCode(callSuper = true)
@ToString
public class User extends Actor{
 
	@NotBlank
	@NotNull
	@Column(name = "name", nullable = false)
	@SafeHtml
    private String name;
 
	@NotBlank
	@NotNull
	@Column(name = "surnames", nullable = false)
	@SafeHtml
    private String surnames;
	
	@NotBlank
	@NotNull
    @Column(name = "nick", nullable = false, unique = true)
	@SafeHtml
    private String nick;
	
	@Column(name = "gitUser")
	@SafeHtml
    private String gitUser;
	
	@Column(name = "photo")
	@SafeHtml
	private String photo;
	
}
