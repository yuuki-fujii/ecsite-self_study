package com.example.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.domain.LoginUser;
import com.example.form.AdminRegisterForm;

@Controller
@RequestMapping("/admin")
public class AdminRegisterController {
	
	@ModelAttribute
	public AdminRegisterForm setUpAdminRegisterForm() {
		return new AdminRegisterForm();
	}
	
	@RequestMapping("")
	public String toAdminResister() {
		return "register_admin";
	}
	
	@RequestMapping("/register")
	public String adminRegister(@Validated AdminRegisterForm form,BindingResult result, @AuthenticationPrincipal LoginUser loginUser) {
		// パスワードが一致しない場合、エラーメッセージを表示し返す
		if (!"1234".equals(form.getPassword())) {
			result.rejectValue("password", null, "パスワードが違います");
		}
		
		if (result.hasErrors()) {
			return "register_admin";
		}
		// パスワードが一致する際の処理
		System.out.println("パスワード一致");
		
		
		return "forward:/";
	}
}
