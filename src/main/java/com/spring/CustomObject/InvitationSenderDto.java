package com.spring.customobject;

import com.spring.model.Team;
import com.spring.model.User;

import lombok.Data;

@Data
public class InvitationSenderDto {
	
	private Integer id;

    private String message;
 		
    private User recipient;
	
    private Team team;
}