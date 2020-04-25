package com.example.controller;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.domain.User;
import com.example.form.UserRegisterForm;
import com.example.service.UserRegisterService;

@Controller
@RequestMapping("/user_register")
public class UserResisterController {
	
	@Autowired
	private UserRegisterService userRegisterService;

	@ModelAttribute
	public UserRegisterForm setUpUserRegisterForm() {
		return new UserRegisterForm();
	}
	
	/**
	 * ユーザ登録のページを表示.
	 * 
	 * @return ユーザ登録画面
	 */
	@RequestMapping("")
	public String toRegister() {
		return "register_user";
	}
	
	@RequestMapping("/admin")
	public String toRegisterAdmin() {
		return "register_admin";
	}
	
	/**
	 * ユーザ情報を登録する.
	 * 
	 * @param form ユーザ情報フォーム
	 * @param result　入力値チェック
	 * @return　ログイン画面へリダイレクト
	 */
	@RequestMapping("/insert")
	public String insert(@Validated UserRegisterForm form, BindingResult result) {
		
		User registeredUser = userRegisterService.findByEmail(form.getEmail());
		// メールアドレスが重複している場合登録できない
		if (registeredUser != null) {
			result.rejectValue("email", null, "そのメールアドレスはすでに使われています");
		} 
		// パスワードと確認用パスワードが一致するかどうか
		if (!form.getConfirmationPassword().equals(form.getPassword())) {
			result.rejectValue("password", null, "パスワードと確認用パスワードが一致しません");
		}
		// 入力段階で問題があれば登録画面に戻す
		if (result.hasErrors()) {
			return "register_user";
		}
		
		// パスワードを暗号化
		BCryptPasswordEncoder bcrypt = new BCryptPasswordEncoder();
		String encode = bcrypt.encode(form.getPassword());
		
		// フォームからドメインにプロパティ値をコピー
		User user = new User();
		BeanUtils.copyProperties(form, user);
		// ハッシュ化したパスワードをドメインにセット
		user.setPassword(encode);
		// 管理者権限の付与
		if ("1".equals(form.getIsAdminNumber())) {
			user.setIsAdmin(true);
		}
		userRegisterService.insert(user);
		return "redirect:/login";
	}
	
	
	
	
	
}
