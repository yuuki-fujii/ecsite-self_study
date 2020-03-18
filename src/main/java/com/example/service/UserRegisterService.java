package com.example.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.domain.User;
import com.example.repository.UserRepository;

/**
 * ユーザ登録の処理を行うサービス.
 * 
 * @author yuuki
 *
 */
@Service
@Transactional
public class UserRegisterService {
	
	@Autowired
	private UserRepository userRepository;
	
	/**
	 * ユーザ情報を登録する.
	 * 
	 * @param user ユーザ情報
	 */
	public void insert(User user) {
		userRepository.insert(user);
	}
	
	/**
	 * メールアドレスでユーザ検索する.
	 * @param email　メールアドレス
	 * @return ユーザ情報
	 */
	public User findByEmail(String email) {
		User user = userRepository.findByEmail(email);
		return user;
	}
}
