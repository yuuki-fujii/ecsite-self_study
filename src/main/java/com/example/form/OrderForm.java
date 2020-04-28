package com.example.form;

import java.time.LocalDateTime;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * 注文フォーム
 * 
 * @author yuuki
 *
 */
public class OrderForm {
	
	/** orderのid hiddenで埋めておく */
	private Integer id;
	/** 合計金額 hiddenで埋めておく */
	private Integer totalPrice;	
	/** 宛先氏名 */
	@NotBlank(message = "お名前を入力してください")
	private String destinationName;
	/** 宛先Eメール */
	@NotBlank(message = "メールアドレスを入力してください")
	@Email(message = "メールアドレスの形式が不正です")
	private String destinationEmail; 
	/** 宛先郵便番号 */
	@Pattern(regexp = "^[0-9]{7}$", message = "郵便番号はハイフン無の7桁で入力して下さい")
	private String destinationZipcode;
	/** 宛先住所 */
	@NotBlank(message = "住所を入力して下さい")
	private String destinationAddress;
	/** 宛先電話番号 */
	@Pattern(regexp = "^[0-9]+$", message = "電話番号は数値を入力して下さい")
	private String destinationTel;
	/** 配達日 */
	private String stringDeliveryDate;
	/** 配達時間 */
	private String stringDeliveryHour;
	/** 配達時間 */ 
	private LocalDateTime deliveryTime;
	/** 支払方法 */
	private Integer paymentMethod;
	/** カード番号 */
	private String card_number;
	/** 有効期限（年） */
	private String card_exp_year;
	/** 有効期限（月） */
	private String card_exp_month;
	/** カード名義人 */
	private String card_name;
	/** セキュリティコード */
	private String card_cvv;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getTotalPrice() {
		return totalPrice;
	}
	public void setTotalPrice(Integer totalPrice) {
		this.totalPrice = totalPrice;
	}
	public String getDestinationName() {
		return destinationName;
	}
	public void setDestinationName(String destinationName) {
		this.destinationName = destinationName;
	}
	public String getDestinationEmail() {
		return destinationEmail;
	}
	public void setDestinationEmail(String destinationEmail) {
		this.destinationEmail = destinationEmail;
	}
	public String getDestinationZipcode() {
		return destinationZipcode;
	}
	public void setDestinationZipcode(String destinationZipcode) {
		this.destinationZipcode = destinationZipcode;
	}
	public String getDestinationAddress() {
		return destinationAddress;
	}
	public void setDestinationAddress(String destinationAddress) {
		this.destinationAddress = destinationAddress;
	}
	public String getDestinationTel() {
		return destinationTel;
	}
	public void setDestinationTel(String destinationTel) {
		this.destinationTel = destinationTel;
	}
	public String getStringDeliveryDate() {
		return stringDeliveryDate;
	}
	public void setStringDeliveryDate(String stringDeliveryDate) {
		this.stringDeliveryDate = stringDeliveryDate;
	}
	public String getStringDeliveryHour() {
		return stringDeliveryHour;
	}
	public void setStringDeliveryHour(String stringDeliveryHour) {
		this.stringDeliveryHour = stringDeliveryHour;
	}
	public String getCard_number() {
		return card_number;
	}
	public void setCard_number(String card_number) {
		this.card_number = card_number;
	}
	public String getCard_exp_year() {
		return card_exp_year;
	}
	public void setCard_exp_year(String card_exp_year) {
		this.card_exp_year = card_exp_year;
	}
	public String getCard_exp_month() {
		return card_exp_month;
	}
	public void setCard_exp_month(String card_exp_month) {
		this.card_exp_month = card_exp_month;
	}
	public String getCard_name() {
		return card_name;
	}
	public void setCard_name(String card_name) {
		this.card_name = card_name;
	}
	public String getCard_cvv() {
		return card_cvv;
	}
	public void setCard_cvv(String card_cvv) {
		this.card_cvv = card_cvv;
	}
	// フォーム側でdelivaryTimeを加工する
	public LocalDateTime getDeliveryTime() {
		try {
			// stringDeliveryDate と stringDeliveryHourを用いてLocalDateTimeオブジェクトを作成する
			int year = Integer.parseInt(stringDeliveryDate.substring(0, 4));
			int month = Integer.parseInt(stringDeliveryDate.substring(5, 7));
			int date = Integer.parseInt(stringDeliveryDate.substring(8, 10));
			int hour = Integer.parseInt(stringDeliveryHour);
			LocalDateTime deliveryTime = LocalDateTime.of(year, month, date, hour, 0, 0);
			
			return deliveryTime;
		} catch (Exception e) {
			return null;
		}
	}
	public void setDeliveryTime(LocalDateTime deliveryTime) {
		this.deliveryTime = deliveryTime;
	}
	public Integer getPaymentMethod() {
		return paymentMethod;
	}
	public void setPaymentMethod(Integer paymentMethod) {
		this.paymentMethod = paymentMethod;
	}
	
	@Override
	public String toString() {
		return "OrderForm [id=" + id + ", totalPrice=" + totalPrice + ", destinationName=" + destinationName
				+ ", destinationEmail=" + destinationEmail + ", destinationZipcode=" + destinationZipcode
				+ ", destinationAddress=" + destinationAddress + ", destinationTel=" + destinationTel
				+ ", stringDeliveryDate=" + stringDeliveryDate + ", stringDeliveryHour=" + stringDeliveryHour
				+ ", deliveryTime=" + deliveryTime + ", paymentMethod=" + paymentMethod + ", card_number=" + card_number
				+ ", card_exp_year=" + card_exp_year + ", card_exp_month=" + card_exp_month + ", card_name=" + card_name
				+ ", card_cvv=" + card_cvv + "]";
	}
}
