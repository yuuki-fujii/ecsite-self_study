package com.example.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.domain.Order;
import com.example.repository.OrderRepository;

/**
 * 注文を行うサービス.
 * 
 * @author yuuki
 *
 */
@Service
@Transactional
public class OrderService {
	
	@Autowired
	private OrderRepository orderRepository;
	
	
	/**
	 * Orderを更新する.
	 * 
	 * @param order orderオブジェクト
	 */
	public void updateOrder(Order order) {
		orderRepository.updateOrder(order);
	}
}
