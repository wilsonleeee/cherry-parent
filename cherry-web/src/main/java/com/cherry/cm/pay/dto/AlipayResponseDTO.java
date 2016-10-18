package com.cherry.cm.pay.dto;

import java.io.Serializable;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

/**
 * Alipay返回的XML转成entity
 * 
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class AlipayResponseDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5996858695388534809L;
	private String is_success;// 是否请求成功 T 或 F
	private String result_code;// 响应码 SUCCESS 或 FAIL 等
	private String detail_error_code;// 详细错误码
	private String detail_error_des;// 详细错误描述
	private String trade_status;// 交易状态
	private String refund_status;// 退款状态
	private String error;// 错误代码，请求失败的错误信息
	private String buyer_logon_id;// 买家支付宝账号
	private String buyer_user_id;// 买家支付宝用户号
	private String out_trade_no;// 商户网站唯一订单号，我们自己生成的billid
	private String trade_no;// 支付宝交易号，支付宝生成的

	public AlipayResponseDTO() {
		super();
	}

	public String getResult_code() {
		return result_code;
	}

	public void setResult_code(String result_code) {
		this.result_code = result_code;
	}

	public String getDetail_error_code() {
		return detail_error_code;
	}

	public void setDetail_error_code(String detail_error_code) {
		this.detail_error_code = detail_error_code;
	}

	public String getDetail_error_des() {
		return detail_error_des;
	}

	public void setDetail_error_des(String detail_error_des) {
		this.detail_error_des = detail_error_des;
	}

	public String getIs_success() {
		return is_success;
	}

	public void setIs_success(String is_success) {
		this.is_success = is_success;
	}

	public String getTrade_status() {
		return trade_status;
	}

	public void setTrade_status(String trade_status) {
		this.trade_status = trade_status;
	}

	public String getRefund_status() {
		return refund_status;
	}

	public void setRefund_status(String refund_status) {
		this.refund_status = refund_status;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public String getBuyer_logon_id() {
		return buyer_logon_id;
	}

	public void setBuyer_logon_id(String buyer_logon_id) {
		this.buyer_logon_id = buyer_logon_id;
	}

	public String getBuyer_user_id() {
		return buyer_user_id;
	}

	public void setBuyer_user_id(String buyer_user_id) {
		this.buyer_user_id = buyer_user_id;
	}

	public String getOut_trade_no() {
		return out_trade_no;
	}

	public void setOut_trade_no(String out_trade_no) {
		this.out_trade_no = out_trade_no;
	}

	public String getTrade_no() {
		return trade_no;
	}

	public void setTrade_no(String trade_no) {
		this.trade_no = trade_no;
	}
}
