package com.spring.CustomObject;

import java.util.Collection;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskForWorkspaceDto {

	private Integer id;
	
	private String title;

	private String description;

	private Integer points;

	private Collection<UserForWorkspaceDto> users;
}
