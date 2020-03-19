package com.spring.CustomObject;

import java.util.Date;

import com.spring.Model.Project;

import lombok.Data;

@Data
public class SprintDto {
	
	private Integer id;
	
	private Date startDate;
	
	private Date endDate;
	
	private Project project;
}
