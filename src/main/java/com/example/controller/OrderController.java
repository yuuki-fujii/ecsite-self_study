package com.example.controller;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.domain.LoginUser;
import com.example.domain.Order;
import com.example.domain.User;
import com.example.form.OrderForm;
import com.example.service.OrderService;
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
	private OrderService orderService;
	
	@Autowired
	private ShowCartListService showCartListService;
	
	@ModelAttribute
	public OrderForm setUpOrderForm(@AuthenticationPrincipal LoginUser loginUser) {
		OrderForm form = new OrderForm();
		if (loginUser ==null) {
			return form;
		}
		
		User user = loginUser.getUser();
		form.setDestinationName(user.getName());
		form.setDestinationEmail(user.getEmail());
		form.setDestinationZipcode(user.getZipcode());
		form.setDestinationAddress(user.getAddress());
		form.setDestinationTel(user.getTelephone());
		form.setStringDeliveryHour("10");
		form.setPaymentMethod(1);
		return form;
	}
	
	/**
	 * 注文確認画面に遷移する.
	 * 
	 * @return 注文確認画面
	 */
	@RequestMapping("/to_confirm")
	public String toOrderConfirm(@AuthenticationPrincipal LoginUser loginUser, Model model) {
		
		Integer userId = null;
		// 非ログインユーザーの場合、ログイン画面を要求
		if (loginUser == null) {
			return "forward:/login/referer";
		} else {
			userId = loginUser.getUser().getId();
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
	public String doOrder(@AuthenticationPrincipal LoginUser loginUser,@Validated OrderForm form, BindingResult result, Model model) {
		// 入力段階でエラーがある場合、注文確認画面を返す
		if (result.hasErrors()) {
			return toOrderConfirm(loginUser,model);
		}
		
		// 配達希望時刻を取得
		LocalDateTime localDeliveryTime = form.getDeliveryTime();
		//　現在時刻を取得
		LocalDateTime nowPlus2hour = LocalDateTime.now().plusHours(2);
		//　もし過去の時刻が選択されたらエラーを返す
		if (nowPlus2hour.isAfter(localDeliveryTime)) {
			result.rejectValue("stringDeliveryDate",  null, "配達希望日時は現在時刻の2時間以上後を選択して下さい");
		}
		
		// TimestampクラスのvalueOfメソッドを使って変換しておく
		Timestamp deliveryTime = Timestamp.valueOf(localDeliveryTime);
		
		// ユーザIDを元に注文前Orderを検索
		Integer userId = loginUser.getUser().getId();
		List <Order> orderList = showCartListService.showCartList(userId);
		Order order = orderList.get(0);
		
		// formの中身をorderドメインにコピー
		BeanUtils.copyProperties(form, order);
		// deliveryTimeは手動でセットする
		order.setDeliveryTime(deliveryTime);
		// 注文日を取得する
		LocalDate now = LocalDate.now();
		// 注文番号に日付をセットする（連番はシーケンスでSQL側でセット）
		DateTimeFormatter formatterforOrderNumber = DateTimeFormatter.ofPattern("yyyyMMdd");
		String date_for_order_number = now.format(formatterforOrderNumber);
		order.setOrderNumber(date_for_order_number);
		// 注文日も手動でセットする
		Date orderDate = java.sql.Date.valueOf(now);	
		order.setOrderDate(orderDate);
		// 支払い状況に応じてstatusの値を更新する
		order.setStatus(form.getPaymentMethod());
		// シーケンス判定
		DateTimeFormatter formatterforSequence = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		String date_for_sequence_judge = now.format(formatterforSequence);
		
		orderService.resetSequence(date_for_sequence_judge);
		orderService.updateOrder(order);
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
