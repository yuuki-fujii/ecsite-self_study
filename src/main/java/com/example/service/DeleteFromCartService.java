package com.example.service;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.domain.Order;
import com.example.repository.OrderItemRepository;
import com.example.repository.OrderRepository;

/**
 * カート内商品の削除処理を行うサービス.
 * 
 * @author yuuki
 *
 */
@Service
@Transactional
public class DeleteFromCartService {
	
	@Autowired
	private OrderRepository orderRepository;
	
	@Autowired
	private OrderItemRepository orderItemRepository;
	
	@Autowired
	private HttpSession session;
	
	/**
	 * 主キーを元にorderItemIdを削除する.
	 * 
	 * @param orderItemId 主キー
	 */
	public void cartDelete(Integer orderItemId) {
		// 削除前に判定
		Integer userId = (Integer) session.getAttribute("userId");
		// 非ログインユーザーがカートを表示する際の処理
		if (userId == null) {
			userId = session.getId().hashCode();
		}
		
		List<Order> orderList = orderRepository.findByUserIdAndStatus(userId, 0);
		Order order = orderList.get(0);
		
		// 削除処理
		orderItemRepository.deleteById(orderItemId);
		
		// もし最後の1つならOrderも削除
		if (order.getOrderItemList().size() == 1) {
			orderRepository.deleteById(order.getId());
		}
	}
}
