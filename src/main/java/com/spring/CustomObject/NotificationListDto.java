package com.spring.CustomObject;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificationListDto {
	
	private boolean isAdmin;
	
	private Integer idSprint;
	
	private Integer id;
	
	private String title;

	private TeamDto team;
	
	private ProjectIdNameDto project;
	
    private Date date;
	
}