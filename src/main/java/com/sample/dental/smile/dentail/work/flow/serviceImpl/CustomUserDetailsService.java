package com.sample.dental.smile.dentail.work.flow.serviceImpl;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.sample.dental.smile.dentail.work.flow.repo.userRepository;
import com.sample.dental.smile.dentail.work.flow.entity.User;

@Service
public class CustomUserDetailsService implements UserDetailsService {

	@Autowired
	private userRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepository.findByUserName(username);
		if (user == null) {
			throw new UsernameNotFoundException("User not found");
		}
		// Convert User entity to UserDetails
		return new org.springframework.security.core.userdetails.User(user.getUserName(), user.getPassword(),
				new ArrayList<>()); // Replace with authorities if any
	}
}
