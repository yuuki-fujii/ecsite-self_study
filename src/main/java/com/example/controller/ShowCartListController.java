package com.example.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/show_cart_list")
public class ShowCartListController {
	
	@RequestMapping("")
	public String toCartList() {
		return "cart_list";
	}
}
