package com.sparta.restructuring.dto;

import lombok.Data;
import lombok.Getter;

@Data
public class LoginRequest {

	private final String accountId;
	private final String password;


}
