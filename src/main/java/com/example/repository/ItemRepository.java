package com.example.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;
import org.thymeleaf.util.StringUtils;

import com.example.domain.Item;
import com.example.form.SearchForm;

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
	 * 全件検索を行う.（オートコンプリート用）
	 * 
	 * @return 全商品情報
	 */
	public List<Item> findAll (){
		String sql = "SELECT id,name,description,price_m,price_l,image_path,deleted FROM " + TABLE_NAME;
		return template.query(sql, ITEM_ROW_MAPPER);
	}
	
	
	
	/**
	 * 商品検索を行う
	 * 
	 * @param form　商品検索フォーム
	 * @return 検索結果
	 */
	public List<Item> search(SearchForm form){
		MapSqlParameterSource param = new MapSqlParameterSource();
		StringBuilder sql = createSql(form, param, null);
		return template.query(sql.toString(),param, ITEM_ROW_MAPPER);
	}
	
	/**
	 * 検索にヒットした件数を取得する.
	 * 
	 * @param form 商品検索フォーム
	 * @return 検索ヒット数
	 */
	public Integer count(SearchForm form) {
		MapSqlParameterSource param = new MapSqlParameterSource();
		StringBuilder sql = createSql(form, param, "count");
		return template.queryForObject(sql.toString(),param ,Integer.class);
	}
	
	
	public StringBuilder createSql(SearchForm form, MapSqlParameterSource param, String mode) {
		StringBuilder sql = new StringBuilder();
		
		if ("count".equals(mode)) {
			sql.append("SELECT count(*) ");
		} else {
			sql.append("SELECT id,name,description,price_m,price_l,image_path,deleted ");
		}
		
		sql.append("FROM " + TABLE_NAME + " WHERE 1 = 1 ");
		
		// 商品名の曖昧検索
		if (!StringUtils.isEmpty(form.getName())) {
			sql.append("AND name ILIKE :name ");
			param.addValue("name", "%" + form.getName() + "%");
		}
		
		if (!"count".equals(mode)) {
			Integer startNumber = calcStartNumber(form);
			sql.append("ORDER BY price_m, id LIMIT 6 OFFSET " + startNumber);
		}
		
		return sql;
	}
	
	/**
	 * 現在のページでの開始番号 - 1 を求める.
	 * 
	 * @param form 商品検索フォーム
	 * @return 現在のページでの開始番号 - 1 (OFFSETで使う数字)
	 */
	private Integer calcStartNumber(SearchForm form) {
		Integer pageNumber = form.getPage();
		Integer startNumber = 6 * (pageNumber - 1);
		return startNumber;
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
