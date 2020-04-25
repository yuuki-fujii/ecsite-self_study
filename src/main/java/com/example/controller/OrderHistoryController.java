package com.example.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.domain.LoginUser;
import com.example.domain.Order;
import com.example.domain.User;
import com.example.service.OrderHistoryService;

@Controller
@RequestMapping("/order_history")
public class OrderHistoryController {
	
	@Autowired
	private OrderHistoryService orderHistoryService;
	
	@RequestMapping("")
	public String toOrderHistory(@AuthenticationPrincipal LoginUser loginUser,Model model) {
		
		User user = loginUser.getUser();
		List<Order> orderHistoryList = orderHistoryService.getOrderHistory(user.getId());
		
		model.addAttribute("orderHistoryList", orderHistoryList);
		return "order_history";
	}
}
