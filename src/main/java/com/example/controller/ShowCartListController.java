package com.example.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.domain.Order;
import com.example.service.ShowCartListService;

@Controller
@RequestMapping("/show_cart_list")
public class ShowCartListController {
	
	@Autowired
	private ShowCartListService showCartListService;
	
	@Autowired
	private HttpSession session;
	
	@RequestMapping("")
	public String toCartList(Model model) {
		
		Integer userId = (Integer) session.getAttribute("userId");
		// 非ログインユーザーがカートを表示する際の処理
		if (userId == null) {
			userId = session.getId().hashCode();
		}
		
		List <Order> orderList = showCartListService.showCartList(userId);
		
		if (orderList == null) {
			model.addAttribute("noOrderItemMessage", "カートに商品がありません");
			return "cart_list";
		}
		// カートの中のOrderは1つだけなので、先頭のものを取り出す
		Order order = orderList.get(0);
		// DB上ではtotal_priceは0なので、計算してセットする
		order.setTotalPrice(order.getCalcTotalPrice() + order.getTax());
		model.addAttribute("order", order);
		
		return "cart_list";
	}
}
