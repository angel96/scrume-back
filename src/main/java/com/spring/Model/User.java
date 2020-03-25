package com.spring.Model;

import java.sql.Blob;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

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
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@EqualsAndHashCode(callSuper = true)
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
	
	@DateTimeFormat
	@NotNull
	@Column(name = "endingBoxDate", nullable = false)
    private Date endingBoxDate;
	
	@ManyToOne
	@NotNull
	@JoinColumn(name = "box", nullable = false)
    private Box box;
}
