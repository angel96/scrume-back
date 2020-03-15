package com.spring.CustomObject;

import com.spring.Model.Team;
import com.spring.Model.User;

import lombok.Data;

@Data
public class InvitationSenderDto {
	
    private String message;
 		
    private User recipient;
	
    private Team team;
}