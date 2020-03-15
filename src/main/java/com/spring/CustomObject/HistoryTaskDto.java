package com.spring.CustomObject;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class HistoryTaskDto {

	public int origin;
	public int destiny;
	public int task;

}
