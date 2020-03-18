package com.example.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.domain.Item;
import com.example.domain.Topping;
import com.example.repository.ItemRepository;
import com.example.repository.ToppingRepository;

@Service
public class ShowItemDetailService {
	
	@Autowired
	private ItemRepository itemRepository;
	
	@Autowired
	private ToppingRepository toppingRepository;
	
	
	/**
	 * 商品情報詳細を取得.
	 * @param id　商品ID
	 * @return　商品情報詳細
	 */
	public Item getAnItem(Integer id) {
		return itemRepository.findById(id);
	}
	
	/**
	 * トッピング情報を全件取得.
	 * 
	 * @return トッピング一覧
	 */
	public List<Topping> getAllToppings() {
		return toppingRepository.findAll();
	}

}
