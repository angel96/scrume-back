package com.spring.CustomObject;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserProjectWorkspaceFromTaskDto {
	private int taskId;
	private int projectId;
	private String projectName;
	private Integer workId;
	private String workName;
	

}
