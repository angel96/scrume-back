package com.spring.CustomObject;

import java.util.Collection;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InvitationSenderDto {
	

    private String message;
 		
    private Collection<Integer> recipients;
	
    private Integer team;
}