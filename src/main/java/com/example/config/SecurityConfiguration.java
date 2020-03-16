package com.example.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * Spring Securityの制御を行うファイル.
 * 
 * @author yuuki
 *
 */
@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter{
	
	@Override
	public void configure(WebSecurity web) throws Exception {
		// セキュリティ設定を無視するリクエスト設定
		// 静的リソースに対するアクセスはセキュリティ設定を無視する
		web.ignoring().antMatchers("/css/**","/img/**","/js/**","/fonts/**");
	}
	
	@Override
	public void configure (HttpSecurity http) throws Exception {
		http
			.authorizeRequests()
				.anyRequest().permitAll();
	}
}
