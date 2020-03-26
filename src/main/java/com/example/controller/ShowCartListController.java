package com.example.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.domain.LoginUser;
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
	public String toCartList(@AuthenticationPrincipal LoginUser loginUser, Model model) {
		
		Integer userId = null;
		// 非ログインユーザーがカートを表示する際の処理
		if (loginUser == null) {
			userId = session.getId().hashCode();
		} else {
			userId = loginUser.getUser().getId();
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
