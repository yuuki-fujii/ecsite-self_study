package com.example.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.example.service.UserDetailService;

/**
 * Spring Securityの制御を行うファイル.
 * 
 * @author yuuki
 *
 */
@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
	
	@Autowired
	private UserDetailService userDetailService;
	
	@Override
	public void configure(WebSecurity web) throws Exception {
		// セキュリティ設定を無視するリクエスト設定
		// 静的リソースに対するアクセスはセキュリティ設定を無視する
		web.ignoring()
			.antMatchers("/css/**","/img/**","/js/**","/fonts/**");
	}
	
	/**
	 * このメソッドをオーバーライドすることで、認可の設定やログイン/ログアウトに関する設定ができる.
	 * 
	 * @see org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter#configure(org.springframework.security.config.annotation.web.builders.HttpSecurity)
	 */
	@Override
	public void configure (HttpSecurity http) throws Exception {
		
		http
			.authorizeRequests()
				.anyRequest().permitAll(); //とりあえず全部許可
				
		http.formLogin() // ログインに関する設定
			.loginPage("/login") 
			.loginProcessingUrl("/login_input")
			.failureUrl("/login?error=true")
			.defaultSuccessUrl("/login/after_login", true)
			.usernameParameter("email")
			.passwordParameter("password");
			
		http.logout() // ログアウトに関する処理
			.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
			.logoutSuccessUrl("/")
			.invalidateHttpSession(true).permitAll();
		
		//　デフォルトの設定ではログイン前後でセッションIDが変わってしまうので、それを無効にする
		http.sessionManagement().sessionFixation().none();
	}
	
	/**
	 * 「認証」に関する設定.<br>
	 * 認証ユーザを取得する「UserDetailsService」の設定や<br>
	 * パスワード照合時に使う「PasswordEncoder」の設定
	 * 
	 * @see org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter#configure(org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder)
	 */
	@Override
	public void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailService)
			.passwordEncoder(new BCryptPasswordEncoder());
	}
	
	
	 /**
     * <pre>
     * bcryptアルゴリズムでハッシュ化する実装を返します.
     * これを指定することでパスワードハッシュ化やマッチ確認する際に
     * @Autowired
	 * private PasswordEncoder passwordEncoder;
	 * と記載するとDIされるようになります。
     * </pre>
     * @return bcryptアルゴリズムでハッシュ化する実装オブジェクト
     */
	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
