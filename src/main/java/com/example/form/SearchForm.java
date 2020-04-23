package com.example.form;

/**
 * 検索用フォーム.
 * 
 * @author yuuki
 *
 */
public class SearchForm {

	/** 商品名 */
	private String name;
	/** ページ数 */
	private Integer page;
	/** ソート順 */
	private String sort;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getPage() {
		return page;
	}
	public void setPage(Integer page) {
		this.page = page;
	}
	public String getSort() {
		return sort;
	}
	public void setSort(String sort) {
		this.sort = sort;
	}
	@Override
	public String toString() {
		return "SearchForm [name=" + name + ", page=" + page + ", sort=" + sort + "]";
	}
}
