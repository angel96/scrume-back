package com.spring.CustomObject;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PersonalTaskListDto {
	
	private int id;
	private int user;
	private String content;
}
