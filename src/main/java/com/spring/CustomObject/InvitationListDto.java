package com.spring.CustomObject;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InvitationListDto {
	
	private Integer id;
	
	private String team;

	private String from;
	
	private String photo;

	private String message;
 	
	
}