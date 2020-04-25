package com.example.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.domain.Order;
import com.example.repository.OrderRepository;

@Service
@Transactional
public class AdminOrderListService {
	
	@Autowired
	private OrderRepository orderRepository;
	
	
	/**
	 * 全ての注文済Orderを取得する（管理者用）.
	 * 
	 * @return 全ての注文済Order
	 */
	public List<Order> getOrderListForAdmin(){
		return orderRepository.findAllOrder();
	}
}
