package com.spring.CustomObject;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BurndownDto {
	String date;
	long pointsBurnDown;
	long totalDates;

}
