package com.example.controller;

import java.util.List;

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
	public String toItemDetail(Integer id, Model model) {
		
		// Itemを1つ取得
		Item item = showItemDetailService.getAnItem(id);
		// 全てのトッピングを取得
		List<Topping> toppingList = showItemDetailService.getAllToppings();
		// ItemオブジェクトにtoppingListをセットする
		item.setToppingList(toppingList);
		
		model.addAttribute("item", item);
		return "item_detail";
	}
	
	
}
