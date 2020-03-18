package com.example.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.domain.Item;
import com.example.repository.ItemRepository;

/**
 * 商品一覧を表示するサービス.
 * 
 * @author yuuki
 *
 */
@Service
@Transactional
public class ShowItemListService {
	
	@Autowired
	private ItemRepository itemRepository;
	
	/**
	 * 商品一覧表示を行う.
	 * @return　商品一覧
	 */
	public List<Item>showList(){
		return itemRepository.findAll();
	}
}
