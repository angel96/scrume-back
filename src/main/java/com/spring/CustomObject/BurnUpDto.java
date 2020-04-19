package com.spring.CustomObject;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BurnUpDto {
	String date;
	long pointsBurnUp;
	long totalHistoryTask;

}
