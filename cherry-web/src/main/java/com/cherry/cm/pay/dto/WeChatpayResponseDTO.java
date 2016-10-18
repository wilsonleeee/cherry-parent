package com.cherry.cm.pay.dto;

import java.io.Serializable;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

/**
 * 微信支付返回结构体
 * 
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class WeChatpayResponseDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1019712057763824845L;

	private String return_code;// 返回状态码
	private String return_msg;// 返回信息
	// 以下字段在return_code 为SUCCESS 的时候有返回
	private String appid;// 公众账号ID
	private String mch_id;// 商户号
	private String sub_mch_id;// 子商户号
	private String device_info;// 设备号
	private String nonce_str;// 随机字符串
	private String sign;// 签名
	private String result_code;// 业务结果
	private String err_code;// 错误代码
	private String err_code_des;// 错误代码描述
	private String recall;// 是否重调
	// 以下字段在return_code 和 result_code 都为SUCCESS 的时候有返回
	private String openid;// 用户标识
	private String is_subscribe;// 是否关注公众账号
	private String trade_type;// 交易类型
	private String bank_type;// 付款银行
	private Integer total_fee;// 总金额
	private Integer coupon_fee;// 企业红包金额
	private String fee_type;// 货币种类
	private String transaction_id;// 微信支付订单号
	private String out_trade_no;// 商户订单号
	private String out_refund_no;// 商户订单号
	private String refund_id;// 微信退款单号
	private Integer refund_fee;// 退款金额
	private Integer refund_count;// 退款笔数
	private String refund_status;// 退款状态
	private String refund_channel;// 退款渠道
	private Integer coupon_refund_fee;// 企业红包退款金额=订单金额-现金退款金额
	private String attach;// 商家数据包
	private String time_end;// 支付完成时间
	private String trade_state;// 交易状态

	public String getReturn_code() {
		return return_code;
	}

	public void setReturn_code(String return_code) {
		this.return_code = return_code;
	}

	public String getReturn_msg() {
		return return_msg;
	}

	public void setReturn_msg(String return_msg) {
		this.return_msg = return_msg;
	}

	public String getAppid() {
		return appid;
	}

	public void setAppid(String appid) {
		this.appid = appid;
	}

	public String getMch_id() {
		return mch_id;
	}

	public void setMch_id(String mch_id) {
		this.mch_id = mch_id;
	}

	public String getSub_mch_id() {
		return sub_mch_id;
	}

	public void setSub_mch_id(String sub_mch_id) {
		this.sub_mch_id = sub_mch_id;
	}

	public String getDevice_info() {
		return device_info;
	}

	public void setDevice_info(String device_info) {
		this.device_info = device_info;
	}

	public String getNonce_str() {
		return nonce_str;
	}

	public void setNonce_str(String nonce_str) {
		this.nonce_str = nonce_str;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public String getResult_code() {
		return result_code;
	}

	public void setResult_code(String result_code) {
		this.result_code = result_code;
	}

	public String getErr_code() {
		return err_code;
	}

	public void setErr_code(String err_code) {
		this.err_code = err_code;
	}

	public String getErr_code_des() {
		return err_code_des;
	}

	public void setErr_code_des(String err_code_des) {
		this.err_code_des = err_code_des;
	}

	public String getOpenid() {
		return openid;
	}

	public void setOpenid(String openid) {
		this.openid = openid;
	}

	public String getIs_subscribe() {
		return is_subscribe;
	}

	public void setIs_subscribe(String is_subscribe) {
		this.is_subscribe = is_subscribe;
	}

	public String getTrade_type() {
		return trade_type;
	}

	public void setTrade_type(String trade_type) {
		this.trade_type = trade_type;
	}

	public String getBank_type() {
		return bank_type;
	}

	public void setBank_type(String bank_type) {
		this.bank_type = bank_type;
	}

	public Integer getTotal_fee() {
		return total_fee;
	}

	public void setTotal_fee(Integer total_fee) {
		this.total_fee = total_fee;
	}

	public Integer getCoupon_fee() {
		return coupon_fee;
	}

	public void setCoupon_fee(Integer coupon_fee) {
		this.coupon_fee = coupon_fee;
	}

	public String getFee_type() {
		return fee_type;
	}

	public void setFee_type(String fee_type) {
		this.fee_type = fee_type;
	}

	public String getTransaction_id() {
		return transaction_id;
	}

	public void setTransaction_id(String transaction_id) {
		this.transaction_id = transaction_id;
	}

	public String getOut_trade_no() {
		return out_trade_no;
	}

	public void setOut_trade_no(String out_trade_no) {
		this.out_trade_no = out_trade_no;
	}

	public String getAttach() {
		return attach;
	}

	public void setAttach(String attach) {
		this.attach = attach;
	}

	public String getTime_end() {
		return time_end;
	}

	public void setTime_end(String time_end) {
		this.time_end = time_end;
	}

	public String getTrade_state() {
		return trade_state;
	}

	public void setTrade_state(String trade_state) {
		this.trade_state = trade_state;
	}

	public String getOut_refund_no() {
		return out_refund_no;
	}

	public void setOut_refund_no(String out_refund_no) {
		this.out_refund_no = out_refund_no;
	}

	public Integer getRefund_fee() {
		return refund_fee;
	}

	public void setRefund_fee(Integer refund_fee) {
		this.refund_fee = refund_fee;
	}

	public Integer getCoupon_refund_fee() {
		return coupon_refund_fee;
	}

	public void setCoupon_refund_fee(Integer coupon_refund_fee) {
		this.coupon_refund_fee = coupon_refund_fee;
	}

	public String getRefund_id() {
		return refund_id;
	}

	public void setRefund_id(String refund_id) {
		this.refund_id = refund_id;
	}

	public String getRefund_status() {
		return refund_status;
	}

	public void setRefund_status(String refund_status) {
		this.refund_status = refund_status;
	}

	public String getRefund_channel() {
		return refund_channel;
	}

	public void setRefund_channel(String refund_channel) {
		this.refund_channel = refund_channel;
	}

	public Integer getRefund_count() {
		return refund_count;
	}

	public void setRefund_count(Integer refund_count) {
		this.refund_count = refund_count;
	}

	public String getRecall() {
		return recall;
	}

	public void setRecall(String recall) {
		this.recall = recall;
	}
}
