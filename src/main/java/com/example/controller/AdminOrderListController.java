package com.example.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.domain.Order;
import com.example.service.AdminOrderListService;


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
	
	
	@RequestMapping("")
	public String toOrderList(Model model) {
		List<Order> orderList = adminOrderListService.getOrderListForAdmin();
		model.addAttribute("orderList", orderList);
		return "admin_order_list";
	}
	
	@RequestMapping("/download_csv")
	public void downloadCsv(HttpServletResponse response) {
		String encodedFileName = null;
		try {
			encodedFileName = URLEncoder.encode("ラクラクカリー.csv", "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		response.setContentType(MimeTypeUtils.APPLICATION_OCTET_STREAM_VALUE + ";charset=utf-8");
		response.setHeader("Content-Disposition", "attachment; filename=\"" + encodedFileName + "\"" );
		
		try (PrintWriter pw = response.getWriter()) {
			List<Order> orderList = adminOrderListService.getOrderListForAdmin();
			StringBuilder outputStringBuilder = new StringBuilder();
			outputStringBuilder.append("注文番号,日付,利用者名,ステータス,総計（税込）\r\n");
			
			for (Order order : orderList) {
				String orderNumber = order.getOrderNumber();
				String Date = String.valueOf(order.getOrderDate());
				String name = order.getDestinationName();
				String status = String.valueOf(order.getStatus());
				String totalPrice = String.valueOf(order.getTotalPrice());
				// CSVファイル内部に記載する形式で文字列を設定
				outputStringBuilder.append(orderNumber + "," + Date + "," + name + "," + status + "," + totalPrice + ",\r\n");
			}
			// CSVファイルに書き込み
			pw.print(outputStringBuilder.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
