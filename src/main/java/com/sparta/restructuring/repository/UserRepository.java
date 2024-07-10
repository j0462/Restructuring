package com.sparta.restructuring.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sparta.restructuring.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
	boolean existsByUserName(String userName);

	User findByUserName(String userName);
}
