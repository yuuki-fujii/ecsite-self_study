package com.example.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.domain.LoginUser;
import com.example.domain.User;
import com.example.repository.UserRepository;

@Service
public class UserDetailService implements UserDetailsService {
	
	@Autowired
	private UserRepository userRepository;
	
	
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		
		try {
			User user = userRepository.findByEmail(email);
			return new LoginUser(user);
		} catch (Exception e) {
			throw new UsernameNotFoundException("not found : " + email);	
		}
		
	}
}
