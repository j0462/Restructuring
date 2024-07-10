package com.sparta.restructuring.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.restructuring.dto.LoginRequest;
import com.sparta.restructuring.entity.User;
import com.sparta.restructuring.security.JwtProvider;


import com.sparta.restructuring.entity.UserRole;
import com.sparta.restructuring.repository.UserRepository;
import com.sparta.restructuring.security.UserDetailsImpl;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;

import static jakarta.servlet.http.HttpServletResponse.SC_OK;
import static jakarta.servlet.http.HttpServletResponse.SC_UNAUTHORIZED;

@Slf4j

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

	private final JwtProvider jwtProvider;
	private final UserRepository userRepository;

	public JwtAuthenticationFilter(JwtProvider jwtProvider, UserRepository userRepository) {
		this.jwtProvider = jwtProvider;
		this.userRepository = userRepository;
		setFilterProcessesUrl("/api/user/login");
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest req, HttpServletResponse res) throws AuthenticationException {
		log.info("인증 시도");
		try {
			// json to object
			LoginRequest requestDto = new ObjectMapper()
				.readValue(req.getInputStream(), LoginRequest.class);

			return getAuthenticationManager().authenticate(
				new UsernamePasswordAuthenticationToken(
					requestDto.getAccountId(),
					requestDto.getPassword(),
					null
				)
			);
		} catch (IOException e) {
			log.error(e.getMessage());
			throw new RuntimeException(e.getMessage());
		}
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest req, HttpServletResponse res, FilterChain chain, Authentication authResult) throws IOException {
		log.info("인증 성공 및 JWT 생성");
		String username = ((UserDetailsImpl) authResult.getPrincipal()).getUsername();
		UserRole role = ((UserDetailsImpl) authResult.getPrincipal()).getUser().getRole();

		String accessToken = jwtProvider.createAccessToken(username, role);
		String refreshToken = jwtProvider.createRefreshToken(username, role);

		// 헤더에 액세스 토큰 추가
		res.addHeader(JwtProvider.AUTHORIZATION_HEADER, accessToken);

		// 쿠키에 리프레시 토큰 추가
		jwtProvider.addJwtToCookie(refreshToken, res);

		// DB에 리프레시 토큰이 이미 있으면 수정, 없으면 저장
		User user = ((UserDetailsImpl) authResult.getPrincipal()).getUser();
		user.updateRefreshToken(refreshToken);
		userRepository.save(user);

		log.info("로그인 성공 : {}", username);

		// 응답 메시지 작성
		res.setStatus(SC_OK);
		res.setCharacterEncoding("UTF-8");
		res.setContentType("application/json");

		// JSON 응답 생성
		String jsonResponse = new ObjectMapper().writeValueAsString(
			new ApiResponse(SC_OK, "로그인 성공", accessToken, refreshToken)
		);

		res.getWriter().write(jsonResponse);
	}

	/**
	 * 로그인 실패
	 */
	protected void unsuccessfulAuthentication(HttpServletRequest req, HttpServletResponse res, AuthenticationException failed) throws IOException {
		log.error("로그인 실패 : {}", failed.getMessage());

		// 응답 메시지 작성
		res.setStatus(SC_UNAUTHORIZED);
		res.setCharacterEncoding("UTF-8");
		res.setContentType("application/json");

		// JSON 응답 생성
		String jsonResponse = new ObjectMapper().writeValueAsString(
			new ApiResponse(SC_UNAUTHORIZED, "로그인 실패: " + failed.getMessage(), null, null)
		);

		res.getWriter().write(jsonResponse);
	}

	/**
	 * 로그인 응답 데이터
	 */
	@Data
	@AllArgsConstructor
	private static class ApiResponse {

		private int statusCode;
		private String msg;
		private String accessToken;
		private String refreshToken;

	}

}
