package com.sparta.restructuring.dto;

import lombok.Data;
import lombok.Getter;

@Data
public class LoginRequest {

	private String accountId;
	private String password;


}
