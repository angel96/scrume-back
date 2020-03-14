package com.spring.CustomObject;

import java.util.Date;

import com.spring.Model.Project;

import lombok.Data;

@Data
public class SprintDatesDto {
	
	private Date startDate;
	
	private Date endDate;
	
	private Integer idProject;
}
