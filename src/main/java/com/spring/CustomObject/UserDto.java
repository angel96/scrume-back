package com.spring.customobject;

import java.sql.Blob;
import java.util.Date;

import com.spring.model.Box;

import lombok.Data;

@Data
public class UserDto {
	private Integer id;
	
	private String username;
	
	private String password;
	
	private String name;
	
	private String surnames;
	
    private String nick;

    private String gitUser;

	private Blob photo;

    private Date endingBoxDate;

    private Box box;


	
	
}
