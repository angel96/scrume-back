package com.spring.customobject;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WorkspaceSprintEditDto {

	private Integer id;

	private String name;
	
	private Date startDate;
	
	private Date endDate;

	private Integer project;
	
}
