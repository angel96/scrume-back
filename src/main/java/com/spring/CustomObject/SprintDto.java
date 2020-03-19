package com.spring.customobject;

import java.util.Date;

import com.spring.model.Project;

import lombok.Data;

@Data
public class SprintDto {
	
	private Integer id;
	
	private Date startDate;
	
	private Date endDate;
	
	private Project project;
}
