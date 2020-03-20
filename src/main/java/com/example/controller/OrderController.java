package com.example.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.domain.Order;
import com.example.form.OrderForm;
import com.example.service.ShowCartListService;

/**
 * 注文確認を行うコントローラ.
 * 
 * @author yuuki
 *
 */
@Controller
@RequestMapping("/order")
public class OrderController {	
	
	@Autowired
	private HttpSession session;
	
	@Autowired
	private ShowCartListService showCartListService;
	
	@ModelAttribute
	public OrderForm setUpOrderForm() {
		return new OrderForm();
	}
	
	/**
	 * 注文確認画面に遷移する.
	 * 
	 * @return 注文確認画面
	 */
	@RequestMapping("/to_confirm")
	public String toOrderConfirm(Model model) {
		
		Integer userId = (Integer) session.getAttribute("userId");
		// ログインしていない場合、ログインを要求する.
		if (userId == null) {
			return "forward:/login";
		}
		
		List <Order> orderList = showCartListService.showCartList(userId);
		if (orderList == null) {
			model.addAttribute("noOrderItemMessage", "カートに商品がありません");
			return "cart_list";
		}
		
		// カートの中のOrderは1つだけなので、先頭のものを取り出す
		Order order = orderList.get(0);
		// DB上ではtotal_priceは0なので、計算してセットする
		order.setTotalPrice(order.getCalcTotalPrice() + order.getTax());
		model.addAttribute("order", order);
		
		return "order_confirm";
	}
	
	
	@RequestMapping("/do_order")
	public String doOrder() {
		
		return "redirect:/order/to_finished";
	}
	
	
	/**
	 * 決済完了画面を表示する.
	 * 
	 * @return 決済完了画面
	 */
	@RequestMapping("/to_finished")
	public String toFinished() {
		return "order_finished";
	}
	
	
}
