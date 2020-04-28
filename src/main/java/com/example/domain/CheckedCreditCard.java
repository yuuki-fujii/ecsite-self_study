package com.example.domain;

/**
 * クレジットカード確認情報を表すドメイン.
 * 
 * @author yuuki
 *
 */
public class CheckedCreditCard {
	/** ステータス(successまたはerror) */
	private String status;
	/** メッセージ */
	private String message;
	/** エラーコード */
	private String error_code;
	
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getError_code() {
		return error_code;
	}
	public void setError_code(String error_code) {
		this.error_code = error_code;
	}
	
	@Override
	public String toString() {
		return "CheckedCreditCard [status=" + status + ", message=" + message + ", error_code=" + error_code + "]";
	}
}
