package com.example.service;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.domain.Item;
import com.example.domain.LoginUser;
import com.example.domain.Order;
import com.example.domain.OrderItem;
import com.example.domain.OrderTopping;
import com.example.form.AddToCartForm;
import com.example.repository.ItemRepository;
import com.example.repository.OrderItemRepository;
import com.example.repository.OrderRepository;
import com.example.repository.OrderToppingRepository;

/**
 * ショッピングカートに商品を追加する業務処理.
 * 
 * @author yuuki
 *
 */
@Service
@Transactional
public class AddToCartService {

	@Autowired
	private ItemRepository itemRepository;
	
	@Autowired
	private OrderToppingRepository orderToppingRepository;
	
	@Autowired
	private OrderItemRepository orderItemRepository;
	
	@Autowired
	private OrderRepository orderRepository;
	
	@Autowired
	private HttpSession session;
	
	/**
	 * ショッピングカートの内容をDBに追加する
	 * 
	 * @param form ショッピングカート用フォーム
	 */
	public void addToCart(@AuthenticationPrincipal LoginUser loginUser,AddToCartForm form) {
		
		Integer userId = null;
		// 非ログインユーザーがカートを表示する際の処理
		if (loginUser == null) {
			userId = session.getId().hashCode();
		} else {
			userId = loginUser.getUser().getId();
		}
		
		// userIdで注文前のorderを検索
		List<Order> orderList = orderRepository.findByUserIdAndStatus(userId, 0);
		
		// OrderItemクラスを作成
		OrderItem orderItem = new OrderItem();

		// DBのordersテーブルにuserId & status = 0　のデータがない時の処理
		// 例：初めてカートに追加する or 注文後初めてカートに追加するケース
		if (orderList == null) {
			// Orderオブジェクトを作成
			Order order = new Order();
			// userIdをセット userIdがnullだとインサートできない
			order.setUserId(userId);
			// カートに入れるだけなのでステータスは「0」= 注文前で固定
			order.setStatus(0);
			// nullだとインサートできないので0をセットしておく
			order.setTotalPrice(0);
			// OrderをDBにインサートし、自動採番されたIDを取得
			order = orderRepository.insertOrder(order);
			orderItem.setOrderId(order.getId());
		} else {
			orderItem.setOrderId(orderList.get(0).getId());
		}

		// フォームから送られてきた商品idを元に商品オブジェクトを検索
		Item item = itemRepository.findById(form.getItemId());
		BeanUtils.copyProperties(form, orderItem);
		orderItem.setItem(item);

		// order_itemsテーブルにインサートし、自動採番されたIDを取得
		orderItem = orderItemRepository.insertOrderItem(orderItem);
		
		if (form.getToppingId() == null) {
			// トッピングが1つも選択されない場合 NullPointerException回避のため
		} else {
			for (String strToppingId : form.getToppingId()) {
				
				// フォームから送られてくるトッピングidはString型なのでIntegerに変換
				Integer intToppingId = Integer.parseInt(strToppingId);

				OrderTopping orderTopping = new OrderTopping();
				orderTopping.setToppingId(intToppingId);
				orderTopping.setOrderItemId(orderItem.getId());

				// DBのorder_toppingsテーブルに保存
				orderToppingRepository.insertOrderTopping(orderTopping);
			}
		}
	}
	
	
	/**
	 * ログイン前にカートに追加した内容をログイン後のカートに反映させる.
	 */
	public void addToCartAfterLogin(@AuthenticationPrincipal LoginUser loginUser) {
		
		// ログイン前のカートに中身がなければ何もせずreturn 
		List <Order> beforeLoginOrderList = orderRepository.findByUserIdAndStatus(session.getId().hashCode(), 0);
		
		if (beforeLoginOrderList == null) {
			return ;
		}
		
		// useIdを取得
		Integer userId = loginUser.getUser().getId();
		
		// ログイン後のカートを検索
		List <Order> afterLoginOrderList = orderRepository.findByUserIdAndStatus(userId, 0);
		if (afterLoginOrderList == null) {
			// ログイン後のカートが空の場合OrderドメインのuserIdを更新するだけ
			orderRepository.updateUserId(userId);
		} else {
			// ordersテーブルを削除し、order_itemsテーブルのorder_idをカートに入っているものに更新する
			Order beforeLoginOrder = beforeLoginOrderList.get(0);
			orderRepository.deleteOrderAndUpdateOrderItem(beforeLoginOrder.getId(),userId);
		}
	}
	
	
	
}
