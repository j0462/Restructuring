package com.sparta.restructuring.service;

import static com.sparta.restructuring.security.JwtProvider.AUTHORIZATION_HEADER;

import com.sparta.restructuring.dto.SignupResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.sparta.restructuring.dto.LoginRequest;
import com.sparta.restructuring.dto.SignupRequest;
import com.sparta.restructuring.entity.User;
import com.sparta.restructuring.entity.UserRole;
import com.sparta.restructuring.repository.UserRepository;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	@Value("${admin.token}")
	private String adminToken;

	public SignupResponse signup(SignupRequest request) {

		if (userRepository.existsByUserName(request.getUserName())) {
			throw new IllegalArgumentException("이미 존재하는 아이디입니다.");
		}

		UserRole role = UserRole.USER;

		if (request.isAdmin()) {
			if (!request.getAdminToken().equals(adminToken)) {
				throw new IllegalArgumentException("어드민 토큰이 일치하지 않습니다.");
			}
			role = UserRole.MANAGER;
		}

		String encodedPassword = passwordEncoder.encode(request.getPassword());

		request.setPassword(encodedPassword);
		User user = new User(request, role);
		userRepository.save(user);
		return new SignupResponse(user);
	}

	public Long logout(User user, HttpServletResponse response) {
		response.setHeader(AUTHORIZATION_HEADER, "");
		Long userId = user.getId();
		user.updateRefreshToken(null);
		userRepository.save(user);

		return userId;
	}


}
