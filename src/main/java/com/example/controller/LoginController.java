package com.example.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.domain.LoginUser;
import com.example.service.AddToCartService;



@Controller
@RequestMapping("/login")
public class LoginController {
	
	
	@Autowired
	private HttpSession session;
	
	@Autowired
	private AddToCartService addToCartService;
	
	/**
	 * ログインページを表示する.
	 * 
	 * @param model リクエストスコープ
	 * @param error ログイン失敗の場合true 
	 * @return ログイン画面
	 */
	@RequestMapping("")
	public String toLoginPage(Model model, @RequestParam(required = false) String error) {
		if (error != null) {
			// ログイン失敗の場合はエラーメッセージを表示する
			model.addAttribute("inputError", "メールアドレスまたはパスワードが不正です");
		}
		return "login";
	}
	
	/**
	 * ショッピングカートの「注文へ進む」ボタンが押された時にのみ使用するメソッド.
	 * 
	 * @param request クライアントからのリクエスト情報
	 * @return ログイン画面
	 */
	@RequestMapping("/referer")
	public String makeReferer(HttpServletRequest request) {
		// 「注文へ進む」ボタン→ログイン画面→ログイン成功という順でアクセスした場合
		// 次に商品一覧ではなく、注文確認画面へ遷移させたいため、リファラ情報を格納しておく
		session.setAttribute("referer", request.getHeader("REFERER"));
		return "login";
	}
	
	/**
	 * ログイン成功時に必要な処理を記述. ログイン処理成功後に必要な処理を行い、任意のページに遷移する.
	 * 
	 * @param loginUser ログインユーザー情報
	 * @param model     リクエストスコープ
	 * @return 商品一覧画面（「注文へ進む」ボタンから当パスに遷移してきた場合は、return:注文確認画面）
	 */
	@RequestMapping("/after_login")
	public String afterLogin(@AuthenticationPrincipal LoginUser loginUser,Model model) {
				
		// ログイン前のカートの中身をログイン後のカートに反映する
		addToCartService.addToCartAfterLogin(loginUser);
		
		// ログイン成功後、リファラ情報を取り出す
		String url = (String) session.getAttribute("referer");
		// 「注文へ進む」ボタンからのリクエストがきた場合のみ、ログイン後に注文確認画面へ遷移する.
		if ("http://localhost:8080/show_cart_list".equals(url)) {
			return "forward:/order/to_confirm";
		}
		// 通常は商品一覧画面に遷移する.
		return "forward:/";
	}
	
}
