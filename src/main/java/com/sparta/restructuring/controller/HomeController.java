package com.sparta.restructuring.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

	@GetMapping("/")
	public String login() {
		return "login.html";
	}

	@GetMapping("/signup")
	public String signup() {
		return "signup.html";
	}

	@GetMapping("/home")
	public String home() {
		return "home.html";
	}

	@GetMapping("/index")
	public String index() {
		return "index.html";
	}
}
