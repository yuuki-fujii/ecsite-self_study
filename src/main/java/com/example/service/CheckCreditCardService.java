package com.example.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.domain.CheckedCreditCard;
import com.example.form.OrderForm;

/**
 * クレジットカード情報の確認をするサービス.
 * 
 * @author yuuki
 *
 */
@Service
public class CheckCreditCardService {
	
	@Autowired
	RestTemplate restTemplate; // WebAPIを利用するためのクラス
	
	/** WebApiのwarファイルデプロイ先URL */
	private static final String URL = "http://192.168.1.3:8080/sample-credit-card-web-api/credit-card/payment";
	
	/**
	 * クレジットカード情報の確認.
	 * 
	 * @param form クレジットカード情報
	 * @return 確認情報
	 */
	public CheckedCreditCard checkCardInfo(OrderForm form) {
		return restTemplate.postForObject(URL, form, CheckedCreditCard.class);
	}
	
}
