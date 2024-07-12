package com.sparta.restructuring.security;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.restructuring.entity.UserRole;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JwtAuthorizationFilter extends OncePerRequestFilter {

	private final JwtProvider jwtProvider;
	private final UserDetailsServiceImpl userDetailsService;

	public JwtAuthorizationFilter(JwtProvider jwtProvider, UserDetailsServiceImpl userDetailsService) {
		this.jwtProvider = jwtProvider;
		this.userDetailsService = userDetailsService;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain filterChain) throws ServletException, IOException {
		String accessToken = jwtProvider.getAccessTokenFromHeader(req);
		String refreshToken = jwtProvider.getRefreshTokenFromRequest(req);

		if (StringUtils.hasText(accessToken)) {
			if (jwtProvider.validateAccessToken(accessToken)) {
				log.info("액세스 토큰 검증 성공");
				setAuthentication(jwtProvider.getAccountIdFromToken(accessToken));

			} else if (StringUtils.hasText(refreshToken)) {
				log.info("액세스 토큰 만료");

				if (jwtProvider.validateRefreshToken(refreshToken)) {
					log.info("리프레시 토큰 검증 성공 & 새로운 액세스 토큰 발급");
					String accountId = jwtProvider.getAccountIdFromToken(refreshToken);
					UserRole role = jwtProvider.getRoleFromToken(refreshToken);

					String newAccessToken = jwtProvider.createAccessToken(accountId, role);

					jwtProvider.setHeaderAccessToken(res, newAccessToken);
					setAuthentication(accountId);

				} else {
					log.info("리프레시 토큰 검증 실패");
					jwtExceptionHandler(res, HttpStatus.UNAUTHORIZED, "유효하지 않은 Refresh 토큰입니다.");
					return;
				}
			}
		}
		filterChain.doFilter(req, res);
	}

	/**
	 * 인증 처리
	 */
	public void setAuthentication(String accountId) {
		SecurityContext context = SecurityContextHolder.createEmptyContext();
		context.setAuthentication(createAuthentication(accountId));
		SecurityContextHolder.setContext(context);
	}

	/**
	 * 인증 객체 생성
	 */
	private Authentication createAuthentication(String accountId) {
		UserDetails userDetails = userDetailsService.loadUserByUsername(accountId);
		return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
	}

	/**
	 * JWT 예외 처리
	 */
	public void jwtExceptionHandler(HttpServletResponse res, HttpStatus status, String msg) {
		int statusCode = status.value();
		res.setStatus(statusCode);
		res.setContentType("application/json");
		try {
			String json = new ObjectMapper().writeValueAsString(new HttpResponse(statusCode, msg));
			res.getWriter().write(json);
		} catch (Exception e) {
			log.error(e.getMessage());
		}
	}

	@AllArgsConstructor
	public static class HttpResponse {
		private int statusCode;
		private String msg;
	}

}