package com.example.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.domain.Order;
import com.example.repository.OrderRepository;

@Service
@Transactional
public class OrderHistoryService {
	
	@Autowired
	private OrderRepository orderRepository;
	
	
	/**
	 * 特定のユーザの注文済のorder情報を検索する.
	 * 
	 * @param userId ユーザid
	 * @return 特定のユーザの注文済のorder情報
	 */
	public List<Order> getOrderHistory(Integer userId){
		return orderRepository.findByUserId(userId);
	}
}
