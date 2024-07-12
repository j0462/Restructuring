package com.sparta.restructuring.dto;

import com.sparta.restructuring.entity.User;
import com.sparta.restructuring.entity.UserRole;

import lombok.Data;

@Data
public class SignupResponse {

	private Long id;
	private String accountId;
	private String userName;
	private UserRole role;

	public SignupResponse(User user) {
		this.id = user.getId();
		this.accountId = user.getAccountId();
		this.userName = user.getUserName();
		this.role = user.getRole();
	}


}
