package com.spring.CustomObject;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EstimationDto {
	
	private Integer id;
		
	private Integer points;
	
	private Integer task;
}
