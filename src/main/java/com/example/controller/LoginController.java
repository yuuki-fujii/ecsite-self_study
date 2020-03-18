package com.example.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.domain.LoginUser;
import com.example.domain.User;



@Controller
@RequestMapping("/login")
public class LoginController {
	
	
	@Autowired
	private HttpSession session;
	
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
	 * ログイン成功時に必要な処理を記述. ログイン処理成功後に必要な処理を行い、任意のページに遷移する.
	 * 
	 * @param loginUser ログインユーザー情報
	 * @param model     リクエストスコープ
	 * @return 商品一覧画面（「注文へ進む」ボタンから当パスに遷移してきた場合は、return:注文確認画面）
	 */
	@RequestMapping("/after_login")
	public String afterLogin(@AuthenticationPrincipal LoginUser loginUser, Model model) {
		
		User user = loginUser.getUser();
		session.setAttribute("user", user);
		
		// 通常は商品一覧画面に遷移する.
		return "forward:/";
	}
	
}
