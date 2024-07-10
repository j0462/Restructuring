package com.sparta.restructuring.controller;

import static com.sparta.restructuring.base.ControllerUtil.getFieldErrorResponseEntity;
import static com.sparta.restructuring.base.ControllerUtil.getResponseEntity;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import jakarta.validation.Valid;
import com.sparta.restructuring.base.CommonResponse;
import com.sparta.restructuring.dto.SignupRequest;
import com.sparta.restructuring.dto.SignupResponse;
import com.sparta.restructuring.entity.User;
import com.sparta.restructuring.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class UserController {

	private final UserService userService;


	/**
	 * 회원 가입
	 */
	@PostMapping("/user/signup")
	public ResponseEntity<CommonResponse> signup(
		@Valid @RequestBody SignupRequest request,
		BindingResult bindingResult
	) {
		if (bindingResult.hasErrors()) {
			return getFieldErrorResponseEntity(bindingResult, "회원가입 실패");
		}

		User user = userService.signup(request);
		SignupResponse response = new SignupResponse(user);

		return getResponseEntity(response, "회원 가입 성공");
	}
}
