package com.spring.customobject;

import java.util.Date;

import com.spring.model.Project;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SprintStatisticsDto {

	private Integer id;
	
	private Date startDate;
	
	private Date endDate;
	
	private Project project;
	
	private Integer totalTasks;
	
	private Integer completedTasks;
	
	private Integer totalHP;
	
	private Integer completedHP;
}
