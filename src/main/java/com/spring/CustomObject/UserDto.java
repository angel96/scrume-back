package com.spring.CustomObject;



import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
	private Integer id;
	
	private String name;
	
	private String surnames;
	
    private String nick;

    private String gitUser;

	private String photo;
	
	private Integer idUserAccount;
	
}
