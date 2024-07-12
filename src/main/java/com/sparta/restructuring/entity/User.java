package com.sparta.restructuring.entity;

import java.util.ArrayList;
import java.util.List;

import com.sparta.restructuring.dto.SignupRequest;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
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

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private UserRole role;

	private String refreshToken;

	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<UserBoard> userBoardList = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<Card> cards;

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
