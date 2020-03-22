package com.spring.CustomObject;

import java.util.Collection;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FindByNickDto {

	private String word;
	private Integer team;
	private Collection<Integer> users;
}
