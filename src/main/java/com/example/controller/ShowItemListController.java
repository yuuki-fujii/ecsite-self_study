package com.example.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.domain.Item;
import com.example.service.ShowItemListService;

/**
 * 商品の一覧表示を行うコントローラー.
 * 
 * @author yuuki
 *
 */
@Controller
@RequestMapping("/")
public class ShowItemListController {
	
	@Autowired
	private ShowItemListService showItemListService;
	
	// とりあえず商品表示まで
	@RequestMapping("")
	public String showList(Model model) {
		
		List<Item> itemList = showItemListService.showList();
		List<List<Item>> itemListList = getThreeItemsList(itemList);
		model.addAttribute("itemListList", itemListList);
		
		return "item_list_curry";
	}
	
	
	
	/**
	 * 3個のItemオブジェクトを1セットにしてリストで返す.
	 * 
	 * @param itemList 商品リスト
	 * @return 3個1セットの商品リスト
	 */
	private List<List<Item>> getThreeItemsList(List<Item> itemList) {
		List<List<Item>> itemListList = new ArrayList<>();
		List<Item> threeItemsList = new ArrayList<>();

		for (int i = 1; i <= itemList.size(); i++) {
			threeItemsList.add(itemList.get(i - 1));

			if (i % 3 == 0 || i == itemList.size()) {
				itemListList.add(threeItemsList);
				threeItemsList = new ArrayList<>();
			}
		}
		return itemListList;
	}
	
}
