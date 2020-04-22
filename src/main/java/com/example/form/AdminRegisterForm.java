package com.example.form;

import javax.validation.constraints.NotBlank;

/**
 * 管理者登録用フォーム.
 * 
 * @author yuuki
 *
 */
public class AdminRegisterForm {
	@NotBlank(message = "パスワードを入力してください")
	private String password;

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public String toString() {
		return "AdminRegisterForm [password=" + password + "]";
	}
	
}
