package com.sparta.restructuring.entity;

import com.sparta.restructuring.dto.SignupRequest;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;

@Entity
@Getter
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "user_id")
	private Long id;

	@Column(nullable = false, unique = true)
	private String accountId;

	@Column(nullable = false)
	private String password;

	@Column(nullable = false)
	private String userName;

	@Column(nullable = false)
	private UserRole role;

	private String refreshToken;

	public User(SignupRequest request, UserRole roleEnum) {
		accountId = request.getAccountId();
		password = request.getPassword();
		userName = request.getUserName();
		role = roleEnum;
	}

	public void updateRefreshToken(String newRefreshToken) {
		refreshToken = newRefreshToken;
	}
}
