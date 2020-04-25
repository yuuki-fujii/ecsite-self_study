package com.example.domain;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * 注文を表すドメインクラス.
 * 
 * @author yuuki
 *
 */
@JsonPropertyOrder({"注文番号","注文日","利用者名","ステータス","合計金額"})
public class Order {
	/** 主キー */
	private Integer id;
	/** ユーザid */
	private Integer userId;
	/** 状態 */
	@JsonProperty("ステータス")
	private Integer status;
	/** 合計金額 */
	@JsonProperty("合計金額")
	private Integer totalPrice;
	/** 注文日 */
	@JsonProperty("注文日")
	private Date orderDate;
	/** 注文番号 */
	@JsonProperty("注文番号")
	private String orderNumber;
	/** 宛先氏名 */
	@JsonProperty("利用者名")
	private String destinationName;
	/** 宛先Eメール */
	private String destinationEmail; 
	/** 宛先郵便番号 */
	private String destinationZipcode;
	/** 宛先住所 */
	private String destinationAddress;
	/** 宛先電話番号 */
	private String destinationTel;
	/** 配達時間 */
	private Timestamp deliveryTime;
	/** 支払方法 */
	private Integer paymentMethod;
	/** ユーザ */
	private User user;
	/** 注文商品リスト */
	private List <OrderItem> orderItemList;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public Integer getTotalPrice() {
		return totalPrice;
	}
	public void setTotalPrice(Integer totalPrice) {
		this.totalPrice = totalPrice;
	}
	public Date getOrderDate() {
		return orderDate;
	}
	public void setOrderDate(Date orderDate) {
		this.orderDate = orderDate;
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
	public Timestamp getDeliveryTime() {
		return deliveryTime;
	}
	public void setDeliveryTime(Timestamp deliveryTime) {
		this.deliveryTime = deliveryTime;
	}
	public Integer getPaymentMethod() {
		return paymentMethod;
	}
	public void setPaymentMethod(Integer paymentMethod) {
		this.paymentMethod = paymentMethod;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public List<OrderItem> getOrderItemList() {
		return orderItemList;
	}
	public void setOrderItemList(List<OrderItem> orderItemList) {
		this.orderItemList = orderItemList;
	}
	
	public String getOrderNumber() {
		return orderNumber;
	}
	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}
	
	
	@Override
	public String toString() {
		return "Order [id=" + id + ", userId=" + userId + ", status=" + status + ", totalPrice=" + totalPrice
				+ ", orderDate=" + orderDate + ", orderNumber=" + orderNumber + ", destinationName=" + destinationName
				+ ", destinationEmail=" + destinationEmail + ", destinationZipcode=" + destinationZipcode
				+ ", destinationAddress=" + destinationAddress + ", destinationTel=" + destinationTel
				+ ", deliveryTime=" + deliveryTime + ", paymentMethod=" + paymentMethod + ", user=" + user
				+ ", orderItemList=" + orderItemList + "]";
	}
	/**
	 * 合計金額の税額を算出.
	 * @return 税額
	 */
	public int getTax() {
		int tax = getCalcTotalPrice() * 10 / 100;
		return tax;
	}
	
	
	/**
	 * ステータスを文字列で返す.
	 * @return 文字列ステータス
	 */
	public String getStringStatus() {
		String stringStatus = null;
		if (status == 1) {
			stringStatus = "未入金";
		} else if (status == 2) {
			stringStatus = "入金済";
		}
		return stringStatus;
	}

	
	/**
	 * 合計金額（税抜き）を算出.
	 * @return　合計金額（税抜き）
	 */
	public int getCalcTotalPrice() {
		int totalPrice = 0;
		for (OrderItem orderItem : orderItemList) {
			totalPrice += orderItem.getSubTotal();
		}
		return totalPrice;
	}
}
