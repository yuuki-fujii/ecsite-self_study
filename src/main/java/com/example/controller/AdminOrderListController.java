package com.example.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.example.domain.Order;
import com.example.helper.DownloadHelper;
import com.example.service.AdminOrderListService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.dataformat.csv.CsvGenerator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;


/**
 * 注文一覧画面を表示するコントローラー（管理者用）.
 * 
 * @author yuuki
 *
 */
@Controller
@RequestMapping("/admin_order_list")
public class AdminOrderListController {
	
	@Autowired
	private AdminOrderListService adminOrderListService;
	
	@Autowired
	private DownloadHelper downloadHelper;
	
	@RequestMapping("")
	public String toOrderList(Model model) {
		List<Order> orderList = adminOrderListService.getOrderListForAdmin();
		model.addAttribute("orderList", orderList);
		return "admin_order_list";
	}
	
	/**
     * csvをダウンロードする。
     * @param response
     * @return
     * @throws IOException
     */
	@RequestMapping(value = "/download/csv", method = RequestMethod.GET)
	public ResponseEntity<byte[]> download() throws IOException {
		HttpHeaders headers = new HttpHeaders();
        downloadHelper.addContentDisposition(headers, "ラクラクカリー.csv");
        return new ResponseEntity<>(getCsvText().getBytes("MS932"), headers, HttpStatus.OK);
	}
	
	
	/**
     * CsvMapperで、csvを作成する。
     * @return csv(String)
     * @throws JsonProcessingException
     */
	private String getCsvText() throws JsonProcessingException {
		CsvMapper mapper = new CsvMapper();
		//文字列にダブルクオートをつける
		mapper.configure(CsvGenerator.Feature.ALWAYS_QUOTE_STRINGS, true);
		//ヘッダをつける
		CsvSchema schema = mapper.schemaFor(Order.class).withHeader();
		// 商品情報をDBから取得
		List<Order> orderList = adminOrderListService.getOrderListForAdmin();
		
		return mapper.writer(schema).writeValueAsString(orderList);
	}
	
}
