package com.sparta.restructuring.security;

import com.sparta.restructuring.entity.User;
import com.sparta.restructuring.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

	private final UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String accountId) throws UsernameNotFoundException {
		User user = userRepository.findByAccountId(accountId);

		return new UserDetailsImpl(user);
	}

}
