package com.example.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.domain.LoginUser;
import com.example.form.AddToCartForm;
import com.example.service.AddToCartService;

/**
 * ショッピングカートに商品を追加するコントローラ.
 * 
 * @author yuuki
 *
 */
@Controller
@RequestMapping("/add_to_cart")
public class AddToCartController {

	@Autowired
	private AddToCartService addToCartService;
	
	/**
	 * カートに商品を追加する.
	 * 
	 * @param form 追加する商品情報
	 * @return カート内一覧画面
	 */
	@RequestMapping("")
	public String cartAdd(@AuthenticationPrincipal LoginUser loginUser,AddToCartForm form) {

		// cartAddサービスにformを渡す
		addToCartService.addToCart(loginUser,form);

		return "redirect:/show_cart_list";
	}
}
