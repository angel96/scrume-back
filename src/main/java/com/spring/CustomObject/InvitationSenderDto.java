package com.spring.CustomObject;

import java.util.Collection;


import lombok.Data;

@Data
public class InvitationSenderDto {
	

    private String message;
 		
    private Collection<Integer> recipients;
	
    private Integer team;
}