package com.spring.CustomObject;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificationUpdateDto {
		
	private String title;

    private Date date;
	
	
}