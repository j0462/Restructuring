package com.sparta.restructuring.service;

import org.springframework.stereotype.Service;

import com.sparta.restructuring.repository.UserBoardRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class UserBoardService {

	private UserBoardRepository userBoardRepository;
}
