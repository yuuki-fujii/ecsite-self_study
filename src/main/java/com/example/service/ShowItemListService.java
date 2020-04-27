package com.example.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.domain.Item;
import com.example.form.SearchForm;
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
	 * 全件検索を行う.(オートコンプリート用)
	 * 
	 * @return　全商品情報
	 */
	public List <Item> getAllItems(){
		return itemRepository.findAll();
	}
	
	/**
	 * 商品検索を行う.
	 * @return　検索結果
	 */
	public List<Item> search(SearchForm form){
		return itemRepository.search(form);
	}
	
	/**
	 * 検索にヒットした件数を取得する.
	 * 
	 * @param form 商品検索フォーム
	 * @return 検索ヒット数
	 */
	public Integer count(SearchForm form) {
		return itemRepository.count(form);
	}
	
	
	/**
	 * 商品を追加する（バッジ処理練習用）.
	 * 
	 * @param item 商品情報
	 */
	public void insert(Item item) {
		itemRepository.insert(item);
	}
}
