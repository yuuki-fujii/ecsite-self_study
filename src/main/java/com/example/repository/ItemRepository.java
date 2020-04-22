package com.example.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import com.example.domain.Item;

/**
 * itemsテーブルに接続するためのリポジトリ.
 * 
 * @author yuuki
 *
 */
@Repository
public class ItemRepository {

	@Autowired
	private NamedParameterJdbcTemplate template;
	
	// テーブル名を定数
	private final String TABLE_NAME = "items";
	
	/** itemドメインを生成するRowMapper. */
	public static final RowMapper<Item> ITEM_ROW_MAPPER = (rs, i) -> {
		Item item = new Item();
		item.setId(rs.getInt("id"));
		item.setName(rs.getString("name"));
		item.setDescription(rs.getString("description"));
		item.setPriceM(rs.getInt("price_m"));
		item.setPriceL(rs.getInt("price_l"));
		item.setImagePath(rs.getString("image_path"));
		item.setDeleted(rs.getBoolean("deleted"));
		return item;
	};
	
	/**
	 * 全件検索を行う.（Mサイズの金額昇順）
	 * 
	 * @return Itemリスト
	 */
	public List<Item> findAll(){
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT id,name,description,price_m,price_l,image_path,deleted ");
		sql.append("FROM " + TABLE_NAME + " ORDER BY price_m");
		return template.query(sql.toString(), ITEM_ROW_MAPPER);
	}
	
	
	public List <Item> findByLikeName(String name){
		StringBuilder sql = new StringBuilder();
		sql.append("");
		
		return null;
	}
	
	
	
	/**
	 * 引数の商品IDで商品情報を検索します.
	 * 
	 * @param id 商品ID
	 * @return 商品情報詳細
	 */
	public Item findById(Integer id) {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT id,name,description,price_m,price_l,image_path,deleted ");
		sql.append("FROM " + TABLE_NAME + " WHERE id=:id ");
		SqlParameterSource param = new MapSqlParameterSource().addValue("id", id);
		Item item = template.queryForObject(sql.toString(), param, ITEM_ROW_MAPPER);
		return item;
	}
}
