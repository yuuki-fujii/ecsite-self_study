package com.example.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.domain.Order;
import com.example.repository.OrderRepository;

/**
 * カート内を表示するサービス.
 * 
 * @author yuuki
 *
 */
@Service
@Transactional
public class ShowCartListService {
	
	@Autowired
	private OrderRepository orderRepository;
	
	/**
	 * カート内商品の表示.
	 * 
	 * @param userId ユーザーID
	 * @return 注文情報（該当情報なしの場合はnull）
	 */
	public List<Order> showCartList(Integer userId) {
		// カートの中身は必ず「注文前」なのでstatusは0で固定.
		List<Order> orderList = orderRepository.findByUserIdAndStatus(userId, 0);
		return orderList;
	}
}
