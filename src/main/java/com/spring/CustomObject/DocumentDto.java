package com.spring.CustomObject;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DocumentDto {
	int id;
	private String name;
	private String type;
	private String content;
	private Integer sprint;

}
