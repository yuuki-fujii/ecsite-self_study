package com.example.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.domain.Item;
import com.example.form.SearchForm;
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
	
	@ModelAttribute
	public SearchForm setUpSearchForm () {
		return new SearchForm();
	}
	
	// 1ページに表示する商品は6品
	private static final int VIEW_SIZE = 6;
	
	// とりあえず商品表示まで
	@RequestMapping("")
	public String showList(SearchForm form ,Model model) {
			
		// オートコンプリート用の記述
		List<Item> fullItemList = showItemListService.getAllItems();
		StringBuilder itemListForAutocomplete = getItemListForAutocomplete(fullItemList);
		model.addAttribute("itemListForAutocomplete", itemListForAutocomplete);
		
		if (form.getPage() == null) {
			form.setPage(1);
		}
		
		Integer count = showItemListService.count(form);
		Integer maxPage = 0;
		
		if (count % VIEW_SIZE == 0) {
			maxPage = count / VIEW_SIZE;
		} else {
			maxPage = count / VIEW_SIZE + 1;
		}
			
		List <Integer> pageNumberList = new ArrayList<>();
		for (int i = 1; i <= maxPage; i++) {
			pageNumberList.add(i);
		}
		
		List<Item> itemList = showItemListService.search(form);
		List<List<Item>> itemListList = getThreeItemsList(itemList);
		model.addAttribute("nowPageNumber", form.getPage());
		model.addAttribute("pageNumberList", pageNumberList);
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
	
	/**
	 * オートコンプリート用にJavaScriptの配列の中身を文字列で作る.
	 * 
	 * @param itemList 商品一覧
	 * @return　オートコンプリート用JavaScriptの配列の文字列
	 */
	private StringBuilder getItemListForAutocomplete(List <Item> itemList) {
		StringBuilder itemListForAutocomplete = new StringBuilder();
		for (int i = 0; i < itemList.size(); i++) {
			if (i != 0) {
				itemListForAutocomplete.append(",");
			}
			Item item = itemList.get(i);
			itemListForAutocomplete.append("\"");
			itemListForAutocomplete.append(item.getName());
			itemListForAutocomplete.append("\"");		
		}
		return itemListForAutocomplete;
	}
	
}
