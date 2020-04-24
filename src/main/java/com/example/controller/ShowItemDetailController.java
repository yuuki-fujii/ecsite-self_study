package com.example.controller;

import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.domain.Item;
import com.example.domain.Topping;
import com.example.service.ShowItemDetailService;

@Controller
@RequestMapping("/item-detail")
public class ShowItemDetailController {
	
	@Autowired
	private ShowItemDetailService showItemDetailService;
	
	@RequestMapping("")
	public String toItemDetail(HttpServletRequest request,HttpServletResponse response,Integer id, Model model) {
		
		// Itemを1つ取得
		Item item = showItemDetailService.getAnItem(id);
		
		// クッキーをセット (最大5つまで)
		Cookie [] cookies = request.getCookies();
		
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if (!cookie.getName().equals("item")) { // Cookieにname="item"が1つもない時 = 初回
					Cookie newCookie = new Cookie("item", item.getName());
					newCookie.setMaxAge(60*60*1);
					newCookie.setPath("/"); // 有効なパスを指定しないとcookieを共有できない
					response.addCookie(newCookie);
				} else if (cookie.getName().equals("item")) {
					String itemName = cookie.getValue() + "/" + item.getName();
					cookie.setValue(itemName);
					cookie.setPath("/");
					response.addCookie(cookie);
				}
			}
		}
		
		
		
		
		
		
		
		// 全てのトッピングを取得
		List<Topping> toppingList = showItemDetailService.getAllToppings();
		// ItemオブジェクトにtoppingListをセットする
		item.setToppingList(toppingList);
		
		model.addAttribute("item", item);
		return "item_detail";
	}
	
	
}
