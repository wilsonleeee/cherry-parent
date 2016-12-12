package com.cherry.cm.pay.bl;

import com.cherry.cm.core.CherryAESCoder;
import com.cherry.cm.pay.dto.AlipayResponseDTO;
import com.cherry.cm.pay.interfaces.AlipayIf;
import com.cherry.cm.pay.service.AliPayService;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.client.apache4.ApacheHttpClient4;
import com.sun.jersey.client.apache4.config.DefaultApacheHttpClient4Config;
import com.sun.jersey.core.util.MultivaluedMapImpl;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.SyncBasicHttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import javax.ws.rs.core.MultivaluedMap;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Map.Entry;


public class AliPayBL implements AlipayIf {

	private static Logger logger = LoggerFactory.getLogger(AliPayBL.class.getName());
	
	@Resource
	private AliPayService alipayService;
	
	private static Map<String, Object> webResourceMap = new HashMap<String, Object>();
	private static Map<String, Object> configMap = new HashMap<String, Object>();
	
	private static Client client;
	
	public boolean getAliPayConfig(Map<String, Object> map){
		map.put("type", "ALIPAY");
		List<Map<String, Object>> configList = alipayService.getAllConfig(map);
		if(configList != null && configList.size() > 0){
			for(int i = 0; i < configList.size(); i++){
				Map<String, Object> config = configList.get(i);
				configMap.put(config.get("paramKey").toString(), config.get("paramValue"));
			}
			return true;
		}
		return false;
	}
	
	
	/**
	 * 支付宝接口(收单查询接口)
	 */
	@Override
	public List<Map<String, Object>> getAlipayQuery(Map paramMap) {
		String p_strOutTradeNo = (String)paramMap.get("strOutTradeNo");//单据号
		
		
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		
		String partner = (String) paramMap.get("alipayPartnerId");
		String key = (String) paramMap.get("alipaySignKey");
		String _input_charset = (String) paramMap.get("aliPayInputCharSet");
		String sign_type = (String) paramMap.get("alipaySignType");
		
		String service = "alipay.acquire.query";// 接口名称
		String alipay_ca_request = "";
		String out_trade_no = p_strOutTradeNo;
		String trade_no = "";

		// 把请求参数打包成数组
		Map<String, String> map = new TreeMap<String, String>();

		if (!ConvertUtil.isBlank(_input_charset)) {
			map.put("_input_charset", _input_charset);
		}
		if (!ConvertUtil.isBlank(alipay_ca_request)) {
			map.put("alipay_ca_request", alipay_ca_request);
		}
		if (!ConvertUtil.isBlank(out_trade_no)) {
			map.put("out_trade_no", out_trade_no);
		}
		if (!ConvertUtil.isBlank(partner)) {
			map.put("partner", partner);
		}
		if (!ConvertUtil.isBlank(service)) {
			map.put("service", service);
		}
		if (!ConvertUtil.isBlank(trade_no)) {
			map.put("trade_no", trade_no);
		}

		Document doc = getAlipayDocument(map, key, _input_charset, sign_type);
		AlipayResponseDTO entity = docToEntity(doc);
		Map<String, Object> maps = new HashMap<String, Object>();
		maps.put("is_success", entity.getIs_success());
		maps.put("result_code", entity.getResult_code());
		maps.put("detail_error_code", entity.getDetail_error_code());
		maps.put("detail_error_des", entity.getDetail_error_des());
		maps.put("trade_status", entity.getTrade_status());
		maps.put("refund_status", entity.getRefund_status());
		maps.put("error", entity.getError());
		maps.put("buyer_logon_id", entity.getBuyer_logon_id());
		maps.put("buyer_user_id", entity.getBuyer_user_id());
		maps.put("out_trade_no", entity.getOut_trade_no());
		maps.put("trade_no", entity.getTrade_no());
//		retMap.put("ResultContent", maps);
		logger.info("支付宝1.0收单查询接口结果：单据号为==="+p_strOutTradeNo+"参数为==="+ConvertUtil.getString(maps));
		list.add(maps);
		return list;
	}

	/**
	 * 支付宝接口(收单撤销接口,撤销先调查询接口)
	 * 不需要调用
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public List<Map<String, Object>> getAlipayQueryAndCancel(Map paramMap) {
		String p_strOutTradeNo = (String)paramMap.get("strOutTradeNo");
		String p_strBaCode = (String)paramMap.get("strBaCode");
		List<Map<String, Object>> list = getAlipayQuery(paramMap);

		if ("SUCCESS".equals(list.get(0).get("result_code"))) {
			if ("TRADE_SUCCESS".equals(list.get(0).get("trade_status")) || "TRADE_PENDING".equals(list.get(0).get("trade_status"))) {// 已付款成功,不允许撤销
				list.get(0).put("result_code", "ACQUIRE_FAIL_TRADE_SUCCESS");
				return list;
			} else {// 付款没成功，撤销
				return getAlipayCancel(paramMap);
			}
		} else if ("FAIL".equals(list.get(0).get("result_code"))) {
			list.get(0).put("result_code", "ORDER_PAY_NOT_EXISTS");
			return list;
		} else {
			logger.error("alipayQueryAndCancel", "单据查询异常！ 传递参数：p_strOutTradeNo=" + p_strOutTradeNo
					+ " p_strBaCode=" + p_strBaCode);
			return list;
		}
	}
	
	/**
	 * 支付宝接口(收单退款接口，先调查询接口)
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public List<Map<String, Object>> getAlipayQueryAndRefund(Map paramMap) {
		String p_strOutTradeNo = (String)paramMap.get("strOutTradeNo");
		String p_strRefundAmount = (String)paramMap.get("strRefundAmount");
		String p_strBaCode = (String)paramMap.get("strBaCode");
		String p_strOutRequestNo = (String)paramMap.get("strOutRequestNo");
		List<Map<String, Object>> list = getAlipayQuery(paramMap);
		logger.info("调用支付宝1.0退款查询接口，销售单据号为："+ConvertUtil.getString(paramMap.get("strOutTradeNo"))+" 退货单据号为："+ConvertUtil.getString(paramMap.get("strOutRequestNo"))
				+"返回值:"+ConvertUtil.getString(list));
		if ("SUCCESS".equals(list.get(0).get("result_code"))) {
			if ("WAIT_BUYER_PAY".equals(list.get(0).get("trade_status"))) {// 已付款成功，不允许退款
				list.get(0).put("result_code", "REFUND_FAIL_WAIT_BUYER_PAY");
				return list;
			} else if ("TRADE_CLOSED".equals(list.get(0).get("trade_status"))) {// 单据已关闭，不允许退款
				list.get(0).put("result_code", "REFUND_FAIL_TRADE_CLOSED");
				return list;
			} else if ("TRADE_PENDING".equals(list.get(0).get("trade_status"))) {// 卖家账户冻结，不允许退款
				list.get(0).put("result_code", "REFUND_FAIL_TRADE_PENDING");
				return list;
			} else if ("TRADE_FINISHED".equals(list.get(0).get("trade_status"))) {// 交易已完成，不允许退款
				list.get(0).put("result_code", "REFUND_FAIL_TRADE_FINISHED");
				return list;
			} else if ("TRADE_SUCCESS".equals(list.get(0).get("trade_status"))) {
				List<Map<String, Object>> list2 = getAlipayRefund(paramMap);
				if ("SUCCESS".equals(list2.get(0).get("result_code")) || "FAIL".equals(list2.get(0).get("result_code"))) {
					return list2;
				} else if ("UNKNOWN".equals(list2.get(0).get("result_code"))) {
					// 出现未知的情况，需要掉用单笔查询接口
					List<Map<String, Object>> list3 = getAlipaySingleTradeQuery(paramMap);
			
					if ("REFUND_SUCCESS".equals(list3.get(0).get("refund_status"))) {
						list3.get(0).put("result_code", "SUCCESS");
						return list3;
					} else {
						list3.get(0).put("result_code", "FAIL");
						return list3;
					}
				} else {
					list2.get(0).put("result_code", "FAIL");
					return list2;
				}
			} else {
				logger.error("alipayQueryAndRefund", "单据查询异常！ 传递参数：p_strOutTradeNo=" + p_strOutTradeNo
						+ " p_strRefundAmount=" + p_strRefundAmount + " p_strBaCode=" + p_strBaCode
						+ " p_strOutRequestNo=" + p_strOutRequestNo);
				return list;
			}

		} else if ("FAIL".equals(list.get(0).get("result_code"))) {
			list.get(0).put("result_code", "REFUND_FAIL_ORDER_NOT_EXISTS");
			return list;
		} else {
			logger.error("alipayQueryAndRefund", "单据查询异常！ 传递参数：p_strOutTradeNo=" + p_strOutTradeNo
					+ " p_strRefundAmount=" + p_strRefundAmount + " p_strBaCode=" + p_strBaCode + " p_strOutRequestNo="
					+ p_strOutRequestNo);
			return list;
		}
	}

	/*
	 * 支付宝接口(单笔查询接口)
	 * 不需要action主动调用
	 * @see com.witposmobile.business.interfaces.AlipayInfo_IF#getAlipaySingleTradeQuery(java.util.Map)
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public List<Map<String, Object>> getAlipaySingleTradeQuery(Map paramMap) {
		String service = "single_trade_query";
		String out_trade_no = (String)paramMap.get("strOutTradeNo");
		String partner = (String) paramMap.get("alipayPartnerId");
		String key = (String) paramMap.get("alipaySignKey");
		String _input_charset = (String) paramMap.get("aliPayInputCharSet");
		String sign_type = (String) paramMap.get("alipaySignType");
		String trade_no = "";

		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		// 把请求参数打包成数组
		Map<String, String> map = new TreeMap<String, String>();
		if (!ConvertUtil.isBlank(_input_charset)) {
			map.put("_input_charset", _input_charset);
		}
		if (!ConvertUtil.isBlank(out_trade_no)) {
			map.put("out_trade_no", out_trade_no);
		}
		if (!ConvertUtil.isBlank(partner)) {
			map.put("partner", partner);
		}
		if (!ConvertUtil.isBlank(service)) {
			map.put("service", service);
		}
		if (!ConvertUtil.isBlank(trade_no)) {
			map.put("trade_no", trade_no);
		}

		Document doc = getAlipayDocument(map, key, _input_charset, sign_type);
		AlipayResponseDTO entity = docToEntity(doc);
		Map<String, Object> maps = new HashMap<String, Object>();
		maps.put("is_success", entity.getIs_success());
		maps.put("result_code", entity.getResult_code());
		maps.put("detail_error_code", entity.getDetail_error_code());
		maps.put("detail_error_des", entity.getDetail_error_des());
		maps.put("trade_status", entity.getTrade_status());
		maps.put("refund_status", entity.getRefund_status());
		maps.put("error", entity.getError());
		maps.put("buyer_logon_id", entity.getBuyer_logon_id());
		maps.put("buyer_user_id", entity.getBuyer_user_id());
		maps.put("out_trade_no", entity.getOut_trade_no());
		maps.put("trade_no", entity.getTrade_no());
		list.add(maps);
		return list;
	}
	
	/**
	 * 支付宝接口(收单关闭接口)
	 * 不需要调用
	 * 
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public List<Map<String, Object>> getAlipayClose(Map paramMap) {
		String service = "alipay.acquire.close";
		String alipay_ca_request = "";
		String out_trade_no = (String)paramMap.get("strOutTradeNo");
		String partner = (String) paramMap.get("alipayPartnerId");
		String key = (String) paramMap.get("alipaySignKey");
		String _input_charset = (String) paramMap.get("aliPayInputCharSet");
		String sign_type = (String) paramMap.get("alipaySignType");
		String trade_no = "";
		String operator_id = (String)paramMap.get("strBaCode");

		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		// 把请求参数打包成数组
		Map<String, String> map = new TreeMap<String, String>();
		if (!ConvertUtil.isBlank(_input_charset)) {
			map.put("_input_charset", _input_charset);
		}
		if (!ConvertUtil.isBlank(alipay_ca_request)) {
			map.put("alipay_ca_request", alipay_ca_request);
		}
		if (!ConvertUtil.isBlank(out_trade_no)) {
			map.put("out_trade_no", out_trade_no);
		}
		if (!ConvertUtil.isBlank(partner)) {
			map.put("partner", partner);
		}
		if (!ConvertUtil.isBlank(service)) {
			map.put("service", service);
		}
		if (!ConvertUtil.isBlank(trade_no)) {
			map.put("trade_no", trade_no);
		}
		if (!ConvertUtil.isBlank(operator_id)) {
			map.put("operator_id", operator_id);
		}

		Document doc = getAlipayDocument(map, key, _input_charset, sign_type);
		AlipayResponseDTO entity = docToEntity(doc);
		Map<String, Object> maps = new HashMap<String, Object>();
		maps.put("is_success", entity.getIs_success());
		maps.put("result_code", entity.getResult_code());
		maps.put("detail_error_code", entity.getDetail_error_code());
		maps.put("detail_error_des", entity.getDetail_error_des());
		maps.put("trade_status", entity.getTrade_status());
		maps.put("refund_status", entity.getRefund_status());
		maps.put("error", entity.getError());
		maps.put("buyer_logon_id", entity.getBuyer_logon_id());
		maps.put("buyer_user_id", entity.getBuyer_user_id());
		maps.put("out_trade_no", entity.getOut_trade_no());
		maps.put("trade_no", entity.getTrade_no());
		list.add(maps);
		return list;
	}
	
	/**
	 * 支付宝接口(收单撤销接口)
	 * 不需要action主动调用
	 * 
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public List<Map<String, Object>> getAlipayCancel(Map paramMap) {
		String service = "alipay.acquire.cancel";
		String alipay_ca_request = "";
		String out_trade_no = (String)paramMap.get("strOutTradeNo");
		String partner = (String) paramMap.get("alipayPartnerId");
		String key = (String) paramMap.get("alipaySignKey");
		String _input_charset = (String) paramMap.get("aliPayInputCharSet");
		String sign_type = (String) paramMap.get("alipaySignType");
		String trade_no = "";
//		String operator_type = "1";
		String operator_type = configMap.get("operator_type").toString();
		String operator_id = (String)paramMap.get("strBaCode");

		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		// 把请求参数打包成数组
		Map<String, String> map = new TreeMap<String, String>();
		if (!ConvertUtil.isBlank(_input_charset)) {
			map.put("_input_charset", _input_charset);
		}
		if (!ConvertUtil.isBlank(alipay_ca_request)) {
			map.put("alipay_ca_request", alipay_ca_request);
		}
		if (!ConvertUtil.isBlank(out_trade_no)) {
			map.put("out_trade_no", out_trade_no);
		}
		if (!ConvertUtil.isBlank(partner)) {
			map.put("partner", partner);
		}
		if (!ConvertUtil.isBlank(service)) {
			map.put("service", service);
		}
		if (!ConvertUtil.isBlank(trade_no)) {
			map.put("trade_no", trade_no);
		}
		if (!ConvertUtil.isBlank(operator_id)) {
			map.put("operator_id", operator_id);
		}
		if (!ConvertUtil.isBlank(operator_type)) {
			map.put("operator_type", operator_type);
		}

		Document doc = getAlipayDocument(map, key, _input_charset, sign_type);
		AlipayResponseDTO entity = docToEntity(doc);
		Map<String, Object> maps = new HashMap<String, Object>();
		maps.put("is_success", entity.getIs_success());
		maps.put("result_code", entity.getResult_code());
		maps.put("detail_error_code", entity.getDetail_error_code());
		maps.put("detail_error_des", entity.getDetail_error_des());
		maps.put("trade_status", entity.getTrade_status());
		maps.put("refund_status", entity.getRefund_status());
		maps.put("error", entity.getError());
		maps.put("buyer_logon_id", entity.getBuyer_logon_id());
		maps.put("buyer_user_id", entity.getBuyer_user_id());
		maps.put("out_trade_no", entity.getOut_trade_no());
		maps.put("trade_no", entity.getTrade_no());
		list.add(maps);
		return list;
	}
	
	/**
	 * 支付宝接口(收单退款接口)
	 * 不需要action主动调用
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public List<Map<String, Object>> getAlipayRefund(Map paramMap) {
		String service = "alipay.acquire.refund";
		String alipay_ca_request = "";
		String out_trade_no = (String)paramMap.get("strOutTradeNo");
		String refund_amount = (String)paramMap.get("strRefundAmount");
		String trade_no = "";
		String out_request_no = (String)paramMap.get("strOutRequestNo");
		String partner = (String) paramMap.get("alipayPartnerId");
		String key = (String) paramMap.get("alipaySignKey");
		String _input_charset = (String) paramMap.get("aliPayInputCharSet");
		String sign_type = (String) paramMap.get("alipaySignType");
//		String operator_type = "1";
		String operator_type = configMap.get("operator_type").toString();
		String operator_id = (String)paramMap.get("strBaCode");
		String refund_reason = "";

		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		// 把请求参数打包成数组
		Map<String, String> map = new TreeMap<String, String>();
		if (!ConvertUtil.isBlank(_input_charset)) {
			map.put("_input_charset", _input_charset);
		}
		if (!ConvertUtil.isBlank(alipay_ca_request)) {
			map.put("alipay_ca_request", alipay_ca_request);
		}
		if (!ConvertUtil.isBlank(out_trade_no)) {
			map.put("out_trade_no", out_trade_no);
		}
		if (!ConvertUtil.isBlank(out_request_no)) {
			map.put("out_request_no", out_request_no);
		}
		if (!ConvertUtil.isBlank(partner)) {
			map.put("partner", partner);
		}
		if (!ConvertUtil.isBlank(service)) {
			map.put("service", service);
		}
		if (!ConvertUtil.isBlank(trade_no)) {
			map.put("trade_no", trade_no);
		}
		if (!ConvertUtil.isBlank(operator_id)) {
			map.put("operator_id", operator_id);
		}
		if (!ConvertUtil.isBlank(operator_type)) {
			map.put("operator_type", operator_type);
		}
		if (!ConvertUtil.isBlank(refund_amount)) {
			map.put("refund_amount", refund_amount);
		}
		if (!ConvertUtil.isBlank(refund_reason)) {
			map.put("refund_reason", refund_reason);
		}

		Document doc = getAlipayDocument(map, key, _input_charset, sign_type);
		AlipayResponseDTO entity = docToEntity(doc);
		Map<String, Object> maps = new HashMap<String, Object>();
		maps.put("is_success", entity.getIs_success());
		maps.put("result_code", entity.getResult_code());
		maps.put("detail_error_code", entity.getDetail_error_code());
		maps.put("detail_error_des", entity.getDetail_error_des());
		maps.put("trade_status", entity.getTrade_status());
		maps.put("refund_status", entity.getRefund_status());
		maps.put("error", entity.getError());
		maps.put("buyer_logon_id", entity.getBuyer_logon_id());
		maps.put("buyer_user_id", entity.getBuyer_user_id());
		maps.put("out_trade_no", entity.getOut_trade_no());
		maps.put("trade_no", entity.getTrade_no());
		list.add(maps);
		return list;
	}

	/**
	 * 支付宝接口(统一下单并支付接口)
	 * 不需要action主动调用
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public List<Map<String, Object>> getAlipayCreateAndPay2(Map paramMap) {
		String service = "alipay.acquire.createandpay";
		String alipay_ca_request = "";
		String out_trade_no = (String)paramMap.get("strOutTradeNo");
//		String subject = "sale";
		String subject = configMap.get("subject").toString();
		String product_code = "BARCODE_PAY_OFFLINE";
		String total_fee = (String)paramMap.get("strTotalFee");
//		String operator_type = "1";
		String operator_type = configMap.get("operator_type").toString();
		String operator_id = (String)paramMap.get("strBaCode");
		String body = "";
//		String extend_params = (String)paramMap.get("strExtendParams");
		String extend_params = "";
//		String it_b_pay = "5m";
		String it_b_pay = configMap.get("it_b_pay").toString();
//		String royalty_type = "ROYALTY";
		String royalty_type = configMap.get("royalty_type").toString();
		String dynamic_id_type = (String)paramMap.get("strDynamicIdType");
		String dynamic_id = (String)paramMap.get("strDynamicId");
		String partner = (String) paramMap.get("alipayPartnerId");
		String key = (String) paramMap.get("alipaySignKey");
		String _input_charset = (String) paramMap.get("aliPayInputCharSet");
		String sign_type = (String) paramMap.get("alipaySignType");

		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		// 把请求参数打包成数组
		Map<String, String> map = new TreeMap<String, String>();
		if (!ConvertUtil.isBlank(_input_charset)) {
			map.put("_input_charset", _input_charset);
		}
		if (!ConvertUtil.isBlank(alipay_ca_request)) {
			map.put("alipay_ca_request", alipay_ca_request);
		}
		if (!ConvertUtil.isBlank(out_trade_no)) {
			map.put("out_trade_no", out_trade_no);
		}
		if (!ConvertUtil.isBlank(partner)) {
			map.put("partner", partner);
		}
		if (!ConvertUtil.isBlank(service)) {
			map.put("service", service);
		}
		if (!ConvertUtil.isBlank(operator_id)) {
			map.put("operator_id", operator_id);
		}
		if (!ConvertUtil.isBlank(operator_type)) {
			map.put("operator_type", operator_type);
		}
		if (!ConvertUtil.isBlank(extend_params)) {
			map.put("extend_params", extend_params);
		}
		if (!ConvertUtil.isBlank(subject)) {
			map.put("subject", subject);
		}
		if (!ConvertUtil.isBlank(product_code)) {
			map.put("product_code", product_code);
		}
		if (!ConvertUtil.isBlank(total_fee)) {
			map.put("total_fee", total_fee);
		}
		if (!ConvertUtil.isBlank(body)) {
			map.put("body", body);
		}
		if (!ConvertUtil.isBlank(it_b_pay)) {
			map.put("it_b_pay", it_b_pay);
		}
		if (!ConvertUtil.isBlank(royalty_type)) {
			map.put("royalty_type", royalty_type);
		}
		if (!ConvertUtil.isBlank(dynamic_id_type)) {
			map.put("dynamic_id_type", dynamic_id_type);
		}
		if (!ConvertUtil.isBlank(dynamic_id)) {
			map.put("dynamic_id", dynamic_id);
		}

		Document doc = getAlipayDocument(map, key, _input_charset, sign_type);
		AlipayResponseDTO entity = docToEntity(doc);
		Map<String, Object> maps = new HashMap<String, Object>();
		maps.put("is_success", entity.getIs_success());
		maps.put("result_code", entity.getResult_code());
		maps.put("detail_error_code", entity.getDetail_error_code());
		maps.put("detail_error_des", entity.getDetail_error_des());
		maps.put("trade_status", entity.getTrade_status());
		maps.put("refund_status", entity.getRefund_status());
		maps.put("error", entity.getError());
		maps.put("buyer_logon_id", entity.getBuyer_logon_id());
		maps.put("buyer_user_id", entity.getBuyer_user_id());
		maps.put("out_trade_no", entity.getOut_trade_no());
		maps.put("trade_no", entity.getTrade_no());
		list.add(maps);
		return list;
	}
	
	/**
	 * 支付宝接口(统一下单并支付接口,下单前先调查询接口)
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public List<Map<String, Object>> getAlipayQueryAndCreateAndPay2(Map paramMap) {
		String p_strOutTradeNo = (String)paramMap.get("strOutTradeNo");//订单号
		String p_strTotalFee = (String)paramMap.get("strTotalFee");//订单金额
		String p_strBaCode = (String)paramMap.get("strBaCode");//营业员
		String p_strGoodsDetail = (String)paramMap.get("strGoodsDetail");//商品明细
		String p_strDynamicIdType = (String)paramMap.get("strDynamicIdType");//qr_code
		String p_strDynamicId = (String)paramMap.get("strDynamicId");//条码
//		String p_strExtendParams = (String)paramMap.get("strExtendParams");//空
		String p_strExtendParams = "";//空
		List<Map<String, Object>> list = getAlipayQuery(paramMap);
		
		if ("SUCCESS".equals(list.get(0).get("result_code"))) {
			if ("WAIT_BUYER_PAY".equals(list.get(0).get("trade_status"))) {// 单据已存在，等待付款
				list.get(0).put("result_code", "ORDER_EXIST_WAIT_BUYER_PAY");
				return list;
			} else if ("TRADE_CLOSED".equals(list.get(0).get("trade_status"))) {// 单据已存在，已关闭
				list.get(0).put("result_code", "ORDER_EXIST_TRADE_CLOSED");
				return list;
			} else if ("TRADE_SUCCESS".equals(list.get(0).get("trade_status"))) {// 单据已存在，已付款成功
				list.get(0).put("result_code", "ORDER_EXIST_TRADE_SUCCESS");
				return list;
			} else if ("TRADE_PENDING".equals(list.get(0).get("trade_status"))) {// 单据已存在，卖家账户冻结
				list.get(0).put("result_code", "ORDER_EXIST_TRADE_PENDING");
				return list;
			} else if ("TRADE_FINISHED".equals(list.get(0).get("trade_status"))) {// 单据已存在，已完成
				list.get(0).put("result_code", "ORDER_EXIST_TRADE_FINISHED");
				return list;
			} else {
				logger.error("alipayQueryAndCreateAndPay2", "查询单据状态异常！ 传递参数：p_strOutTradeNo="
						+ p_strOutTradeNo + " p_strTotalFee=" + p_strTotalFee + " p_strBaCode=" + p_strBaCode
						+ " p_strGoodsDetail=" + p_strGoodsDetail + " p_strDynamicIdType=" + p_strDynamicIdType
						+ " p_strDynamicId=" + p_strDynamicId + " p_strExtendParams=" + p_strExtendParams);
				return list;
			}
		} else if ("FAIL".equals(list.get(0).get("result_code"))) {
			// 没有查询到已存在的单据号，可以正常下单
			List<Map<String, Object>> list2 = getAlipayCreateAndPay2(paramMap);

			// 下单成功支付失败，需要撤单
			if ("ORDER_SUCCESS_PAY_FAIL".equals(list2.get(0).get("result_code"))) {
				List<Map<String, Object>> list3 = getAlipayCancel(paramMap);

				if ("SUCCESS".equals(list3.get(0).get("result_code"))) {// 支付失败，撤单成功
					list3.get(0).put("result_code", "ORDER_PAY_FAIL_ACQUIRE_SUCCESS");
					return list3;
				} else if ("FAIL".equals(list3.get(0).get("result_code"))) {// 支付失败，撤单失败
					list3.get(0).put("result_code", "ORDER_PAY_FAIL_ACQUIRE_FAIL");
					return list3;
				} else if ("UNKNOWN".equals(list3.get(0).get("result_code"))) {// 支付失败，撤单未知
					list3.get(0).put("result_code", "ORDER_PAY_FAIL_ACQUIRE_UNKNOWN");
					return list3;
				} else {
					logger.error("alipayQueryAndCreateAndPay2", "撤消异常！ 传递参数：p_strOutTradeNo="
							+ p_strOutTradeNo + " p_strTotalFee=" + p_strTotalFee + " p_strBaCode=" + p_strBaCode
							+ " p_strGoodsDetail=" + p_strGoodsDetail + " p_strDynamicIdType=" + p_strDynamicIdType
							+ " p_strDynamicId=" + p_strDynamicId + " p_strExtendParams=" + p_strExtendParams);
					return list3;
				}
			} else if ("UNKNOWN".equals(list2.get(0).get("result_code"))) {
				List<Map<String, Object>> list4 = getAlipayQuery(paramMap);

				if ("SUCCESS".equals(list4.get(0).get("result_code"))) {
					if ("WAIT_BUYER_PAY".equals(list4.get(0).get("trade_status"))) {// 单据已存在，等待付款
						list4.get(0).put("result_code", "ORDER_SUCCESS_PAY_INPROCESS");
						return list4;
					} else if ("TRADE_SUCCESS".equals(list4.get(0).get("trade_status"))) {// 单据已存在，已付款成功
						list4.get(0).put("result_code", "ORDER_SUCCESS_PAY_SUCCESS");
						return list4;
					} else if ("TRADE_PENDING".equals(list4.get(0).get("trade_status"))) {// 单据已存在，卖家账户冻结
						list4.get(0).put("result_code", "ORDER_SUCCESS_TRADE_PENDING");
						return list4;
					}
				} else if ("FAIL".equals(list4.get(0).get("result_code"))) {// 单据不存在，下单失败
					list4.get(0).put("result_code", "ORDER_FAIL_ORDER_NOT_EXIST");
					return list4;
				} else if ("PROCESS_EXCEPTION".equals(list4.get(0).get("result_code"))) {// 查询异常，下单失败
					list4.get(0).put("result_code", "PROCESS_EXCEPTION");
					return list4;
				}
			}
			return list2;
		} else if ("PROCESS_EXCEPTION".equals(list.get(0).get("result_code"))) {// 查询异常，下单失败
			list.get(0).put("result_code", "ORDER_FAIL_PROCESS_EXCEPTION");
			return list;
		} else {
			logger.error("alipayQueryAndCreateAndPay2", "支付宝下单异常！ 传递参数：p_strOutTradeNo="
					+ p_strOutTradeNo + " p_strTotalFee=" + p_strTotalFee + " p_strBaCode=" + p_strBaCode
					+ " p_strGoodsDetail=" + p_strGoodsDetail + " p_strDynamicIdType=" + p_strDynamicIdType
					+ " p_strDynamicId=" + p_strDynamicId + " p_strExtendParams=" + p_strExtendParams);
			return list;
		}
	}
	
	/**
	 * 支付宝接口(退款查询接口)
	 * 不需要调用
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public List<Map<String, Object>> getAlipayRefundQuery(Map paramMap) {
		String service = "refund_fastpay_query";
		String out_trade_no = (String)paramMap.get("strOutTradeNo");
		String batch_no = (String)paramMap.get("strBatchNo");
		String partner = (String) paramMap.get("alipayPartnerId");
		String key = (String) paramMap.get("alipaySignKey");
		String _input_charset = (String) paramMap.get("aliPayInputCharSet");
		String sign_type = (String) paramMap.get("alipaySignType");
		String trade_no = "";

		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		// 把请求参数打包成数组
		Map<String, String> map = new TreeMap<String, String>();
		if (!ConvertUtil.isBlank(_input_charset)) {
			map.put("_input_charset", _input_charset);
		}
		if (!ConvertUtil.isBlank(out_trade_no)) {
			map.put("out_trade_no", out_trade_no);
		}
		if (!ConvertUtil.isBlank(partner)) {
			map.put("partner", partner);
		}
		if (!ConvertUtil.isBlank(service)) {
			map.put("service", service);
		}
		if (!ConvertUtil.isBlank(trade_no)) {
			map.put("trade_no", trade_no);
		}
		if (!ConvertUtil.isBlank(batch_no)) {
			map.put("batch_no", batch_no);
		}

		Document doc = getAlipayDocument(map, key, _input_charset, sign_type);
		AlipayResponseDTO entity = docToEntity(doc);
		Map<String, Object> maps = new HashMap<String, Object>();
		maps.put("is_success", entity.getIs_success());
		maps.put("result_code", entity.getResult_code());
		maps.put("detail_error_code", entity.getDetail_error_code());
		maps.put("detail_error_des", entity.getDetail_error_des());
		maps.put("trade_status", entity.getTrade_status());
		maps.put("refund_status", entity.getRefund_status());
		maps.put("error", entity.getError());
		maps.put("buyer_logon_id", entity.getBuyer_logon_id());
		maps.put("buyer_user_id", entity.getBuyer_user_id());
		maps.put("out_trade_no", entity.getOut_trade_no());
		maps.put("trade_no", entity.getTrade_no());
		list.add(maps);
		return list;
	}
	
	/**
	 * 向支付宝发送http请求获取流数据后，转换为document对象
	 * 
	 */
	public Document getAlipayDocument(Map<String, String> map, String key, String _input_charset, String sign_type) {
		try {
			String url = "https://mapi.alipay.com/gateway.do";
			WebResource webResource = getWebResource(url);
			MultivaluedMap<String, String> queryParams = new MultivaluedMapImpl();
			StringBuilder strTmp = new StringBuilder();
			Iterator<Entry<String, String>> it = map.entrySet().iterator();
			while (it.hasNext()) {
				Entry<String, String> entry = (Entry<String, String>) it.next();
				queryParams.add(entry.getKey(), entry.getValue());
				try {
					if ("extend_params".equals(entry.getKey())) {
						strTmp.append(entry.getKey() + "=" + entry.getValue() + "&");
					} else {
						strTmp.append(entry.getKey() + "=" + URLEncoder.encode((String) entry.getValue(), _input_charset)
								+ "&");
					}
				} catch (UnsupportedEncodingException e) {
					logger.error("", e.toString(), e);
				}
			}
			// 去掉最後一個&字符
			strTmp.deleteCharAt(strTmp.length() - 1);
			// 签名结果
			String sign = ConvertUtil.createSign(strTmp.toString() + key);
			queryParams.add("sign_type", sign_type);
			queryParams.add("sign", sign);
			String result = webResource.queryParams(queryParams).get(String.class);
			Document doc = DocumentHelper.parseText(result);
			return doc;
		} catch (DocumentException e) {
			logger.error("", "支付宝请求异常，传递参数：" + mapToString(map), e);
			return null;
		}
		
	}
	
	/**
	 * Map转String
	 * 
	 */
	public String mapToString(Map<String, String> map) {
		StringBuilder strTmp = new StringBuilder();
		Iterator<Entry<String, String>> it = map.entrySet().iterator();
		while (it.hasNext()) {
			Entry<String, String> entry = (Entry<String, String>) it.next();
			strTmp.append(entry.getKey() + "=" + entry.getValue() + " ");
		}
		return strTmp.toString();
	}
	
	/**
	 * document转成entity
	 * 
	 */
	public AlipayResponseDTO docToEntity(Document doc) {
		AlipayResponseDTO entity = new AlipayResponseDTO();
		if (doc != null) {
			// 取得根节点
			Element root = doc.getRootElement();
			// 是否请求成功
			String is_success = root.element("is_success").getText();
			entity.setIs_success(is_success);
			// 判断支付宝请求是否成功
			if ("T".equals(is_success)) {
				// response元素节点
				Element elementResponse = root.element("response");
				// alipay元素节点，通常都有的，除了单笔交易查询接口alipaySingleTradeQuery，它的节点是trade
				Element elementAlipay = elementResponse.element("alipay");
				// trade元素节点
				Element elementTrade = elementResponse.element("trade");
				if(elementAlipay != null){
					// 执行结果
					String result_code = elementAlipay.element("result_code").getText();
					entity.setResult_code(result_code);
					if ("SUCCESS".equals(result_code) || "ORDER_SUCCESS_PAY_SUCCESS".equals(result_code)) {// 成功
						// 交易状态
						if(elementAlipay.element("trade_status") != null){
							String trade_status = elementAlipay.element("trade_status").getText();
							entity.setTrade_status(trade_status);
						}
						// 买家支付宝账号
						if(elementAlipay.element("buyer_logon_id") != null){
							String buyer_logon_id = elementAlipay.element("buyer_logon_id").getText();
							entity.setBuyer_logon_id(buyer_logon_id);
						} 
						// 买家支付宝用户号
						if(elementAlipay.element("buyer_user_id") != null){
							String buyer_user_id = elementAlipay.element("buyer_user_id").getText();
							entity.setBuyer_user_id(buyer_user_id);
						}
						// 商户网站唯一订单号
						if(elementAlipay.element("out_trade_no") != null){
							String out_trade_no = elementAlipay.element("out_trade_no").getText();
							entity.setOut_trade_no(out_trade_no);
						}
						// 支付宝交易号
						if(elementAlipay.element("trade_no") != null){
							String trade_no = elementAlipay.element("trade_no").getText();
							entity.setTrade_no(trade_no);
						}
					} else if ("FAIL".equals(result_code) || "ORDER_FAIL".equals(result_code)){// 失败
						// 错误代码
						if(elementAlipay.element("detail_error_code") != null){
							String detail_error_code = elementAlipay.element("detail_error_code").getText();
							entity.setDetail_error_code(detail_error_code);
						}
						// 错误描述
						if(elementAlipay.element("detail_error_des") != null){
							String detail_error_des = elementAlipay.element("detail_error_des").getText();
							entity.setDetail_error_des(detail_error_des);
						}
					}
				} else if(elementTrade != null){
					// 退款状态
					if(elementTrade.element("refund_status") != null){
						String refund_status = elementTrade.element("refund_status").getText();
						entity.setRefund_status(refund_status);
					}
				}
			} else {
				if(root.element("error") != null){
					String error = root.element("error").getText();
					entity.setError(error);
				}
				logger.error("", "支付宝请求失败");
			}
		}
		return entity;
	}

	/**
     * 创建一个Client类的实例，该实例作为静态变量只初始化一次
     * 
     * @return 返回一个Client类的实例
     */
	public Client getClient() {
		if(client == null) {
			client = ApacheHttpClient4.create(getClientConfig());
		}
		return client;
	}
	
	/**
     * 创建Client对象的配置信息对象
     * 
     * @return 配置信息对象
     */
	@SuppressWarnings("deprecation")
	private ClientConfig getClientConfig() {
		
	   ClientConfig config = new DefaultApacheHttpClient4Config();
	   // 使用HttpParams对象设定jersey客户端访问WebService的ConnectionTimeout和ReadTimeout
	   HttpParams httpParams = new SyncBasicHttpParams();
	   HttpConnectionParams.setConnectionTimeout(httpParams, 60000);
	   HttpConnectionParams.setSoTimeout(httpParams, 180000);
	   
	   config.getProperties().put(DefaultApacheHttpClient4Config.PROPERTY_CHUNKED_ENCODING_SIZE, 0);
	   config.getProperties().put(DefaultApacheHttpClient4Config.PROPERTY_THREADPOOL_SIZE, 10);
	   config.getProperties().put(DefaultApacheHttpClient4Config.PROPERTY_CONNECT_TIMEOUT, 60000);
	   config.getProperties().put(DefaultApacheHttpClient4Config.PROPERTY_READ_TIMEOUT, 180000);
	   config.getProperties().put(DefaultApacheHttpClient4Config.PROPERTY_HTTP_PARAMS, httpParams);
	   config.getProperties().put(DefaultApacheHttpClient4Config.PROPERTY_CONNECTION_MANAGER, new ThreadSafeClientConnManager());
	   return config;
	}
	
	/**
     * 使用Client对象创建一个WebResource类的实例，可根据不同的URL创建不同的WebResponse对象
     * 
     * @param webServiceUrl 访问WebService的URL，为空的场合使用系统设定的WebServiceURL
     * @return 返回WebResource对象
     */
	public WebResource getWebResource(String webServiceUrl) {
		Client c = getClient();
		if(webResourceMap.containsKey(webServiceUrl)) {
			return (WebResource)webResourceMap.get(webServiceUrl);
		} else {
			WebResource webResource = c.resource(webServiceUrl);
			webResourceMap.put(webServiceUrl, webResource);
			return webResource;
		}
	}

	/**
	 * 支付宝接口(统一下单并支付接口,下单前先调查询接口)
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public List<Map<String, Object>> getAlipayQueryAndCreateAndPayTwo(Map map) {
		//查询订单
		List<Map<String, Object>> list = getAlipayQueryTwo(map);
		if ("10000".equals(list.get(0).get("result_code"))) {// 订单支付成功
			if ("WAIT_BUYER_PAY".equals(list.get(0).get("trade_status"))) {// 单据已存在，等待付款
				list.get(0).put("result_code", "ORDER_EXIST_WAIT_BUYER_PAY");
				return list;
			} else if ("TRADE_CLOSED".equals(list.get(0).get("trade_status"))) {// 单据已存在，已关闭
				list.get(0).put("result_code", "ORDER_EXIST_TRADE_CLOSED");
				return list;
			} else if ("TRADE_SUCCESS".equals(list.get(0).get("trade_status"))) {// 单据已存在，已付款成功
				list.get(0).put("result_code", "ORDER_EXIST_TRADE_SUCCESS");
				return list;
			} else if ("TRADE_FINISHED".equals(list.get(0).get("trade_status"))) {// 单据已存在，已完成
				list.get(0).put("result_code", "ORDER_EXIST_TRADE_FINISHED");
				return list;
			} else {
				logger.error("getAlipayQueryTwo", "查询单据状态异常！ 传递参数：" + map.toString());
				return list;
			}
		} else if ("40004".equals(list.get(0).get("result_code"))) {// 交易创建失败
			// 没有查询到已存在的单据号，可以正常下单
			List<Map<String, Object>> list2 = getAlipayCreateAndPayTwo(map);
			if ("10003".equals(list2.get(0).get("result_code"))) {// 订单创建成功支付处理中
				List<Map<String, Object>> list3 = getAlipayQueryTwo(map);
				if ("10000".equals(list3.get(0).get("result_code"))) {
					if ("WAIT_BUYER_PAY".equals(list3.get(0).get("trade_status"))) {// 单据已存在，等待付款
						list3.get(0).put("result_code", "ORDER_SUCCESS_PAY_INPROCESS");
						return list3;
					} else if ("TRADE_SUCCESS".equals(list3.get(0).get("trade_status"))) {// 单据已存在，已付款成功
						list3.get(0).put("result_code", "ORDER_SUCCESS_PAY_SUCCESS");
						return list3;
					}
				} else if ("40004".equals(list3.get(0).get("result_code"))) {// 单据不存在，下单失败
					list3.get(0).put("result_code", "ORDER_FAIL_ORDER_NOT_EXIST");
					return list3;
				}else {//下单成功支付失败，需要撤单
					List<Map<String, Object>> list4 = getAlipayQueryAndCancelTwo(map);
					if("ACQUIRE_FAIL_TRADE_SUCCESS".equals(list4.get(0).get("result_code"))){// 已付款成功,不允许撤销
						list4.get(0).put("result_code", "SUCCESS");
						return list4;
					}else if("ORDER_PAY_NOT_EXISTS".equals(list4.get(0).get("result_code"))){// 单据不存在
						return list4;
					}else {
						logger.error("getAlipayQueryAndCancelTwo", "撤单异常！ 传递参数：" + map.toString());
						return list4;
					}
				}
			}
			return list2;
		} else {
			logger.error("getAlipayQueryTwo", "支付宝下单异常！ 传递参数：" + map.toString());
			return list;
		}
	}
	/**
	 * 支付宝接口(收单查询接口2.0)
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public List<Map<String, Object>> getAlipayQueryTwo(Map map) {
		
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		String version = "1.0";
		String app_id = ConvertUtil.getString(map.get("alipayPartnerId"));
		String charset = ConvertUtil.getString(map.get("aliPayInputCharSet"));
		String sign = ConvertUtil.getString(map.get("alipaySignKey"));
		String sign_type = ConvertUtil.getString(map.get("alipaySignType"));
		String timestamp = CherryUtil.getSysDateTime("yyyy-MM-dd HH:mm:ss");
		String out_trade_no = ConvertUtil.getString(map.get("strOutTradeNo"));
		String biz_content = "{\"out_trade_no\":\""+out_trade_no+"\"}";
		// 把请求参数打包成数组
		Map<String, String> pmap = new TreeMap<String, String>();
		if (!ConvertUtil.isBlank(version)) {
			pmap.put("version", version);
		}
		if (!ConvertUtil.isBlank(app_id)) {
			pmap.put("app_id", app_id);
		}
		if (!ConvertUtil.isBlank(charset)) {
			pmap.put("charset", charset);
		}
		if (!ConvertUtil.isBlank(timestamp)) {
			pmap.put("timestamp", timestamp);
		}
		if (!ConvertUtil.isBlank(sign_type)) {
			pmap.put("sign_type", sign_type);
		}
		if (!ConvertUtil.isBlank(biz_content)) {
			pmap.put("biz_content", biz_content);
		}
		// 接口
		pmap.put("method", "alipay.trade.query");
		logger.info("调用支付宝“收单查询”接口alipay.trade.query，参数：" + pmap.toString());
		JSONObject json =  getAlipayDocumentTwo(pmap,sign);
		AlipayResponseDTO entity = jsonToEntity(json);
		Map<String, Object> maps = new HashMap<String, Object>();
		if("10000".equals(entity.getResult_code())){
			maps.put("is_success", "T");
		}else{
			maps.put("is_success", "F");
		}
		maps.put("result_code", entity.getResult_code());
		maps.put("detail_error_code", entity.getDetail_error_code());
		maps.put("detail_error_des", entity.getDetail_error_des());
		maps.put("trade_status", entity.getTrade_status());
		maps.put("refund_status", "");
		maps.put("error", "");
		maps.put("buyer_logon_id", entity.getBuyer_logon_id());
		maps.put("buyer_user_id", entity.getBuyer_user_id());
		maps.put("out_trade_no", entity.getOut_trade_no());
		maps.put("trade_no", entity.getTrade_no());
		logger.info("调用支付宝“收单查询”接口alipay.acquire.query，返回值：" + maps.toString());
		list.add(maps);
		return list;
	}
	/**
	 * 向支付宝发送http请求获取流数据后，转换为document对象
	 * @param key 
	 * 
	 */
	public static JSONObject getAlipayDocumentTwo(Map<String, String> map, String key) {
		try {

			List<NameValuePair> params = getAlipayUrlRSA(map, key);
			HttpPost httpRequest = new HttpPost("https://openapi.alipay.com/gateway.do");

			httpRequest.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
			
			// 取得HttpClient对象
			HttpClient httpClient = new DefaultHttpClient();
			
			
			/* 发送请求并等待响应 */
			HttpResponse httpResponse = httpClient.execute(httpRequest);

			// 获取entity
			HttpEntity resEntity = httpResponse.getEntity();
			
			String out = EntityUtils.toString(resEntity , "UTF-8").trim();

			JSONObject jsonObject = new JSONObject(out.toString());
			return jsonObject;
		} catch (Exception ex) {
			logger.error("", "支付宝请求异常，传递参数：" + map.toString(), ex);
		}
		return null;
	}
	/**
	 * 拼接URL
	 * @param key 
	 * 
	 */
	public static List<NameValuePair> getAlipayUrlRSA(Map<String, String> map, String key) {
		StringBuilder strTmp = new StringBuilder();
		// 把数组所有元素，按照“参数=参数值”的模式用“&”字符拼接成字符串，并对参数值做URLEncode
		Iterator<Entry<String, String>> it = map.entrySet().iterator();
		while (it.hasNext()) {
			Entry<String, String> entry = it.next();
			strTmp.append(entry.getKey() + "=" + entry.getValue() + "&");// 生成签名时字符串不转码
		}
		// 去掉最後一個&字符
		strTmp.deleteCharAt(strTmp.length() - 1);
		// 签名结果
		String sign="";
		try {
			sign = CherryAESCoder.sign(strTmp.toString(), key);
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("拼接签名失败，参数="+map.toString());
		}
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("app_id", map.get("app_id")));
		params.add(new BasicNameValuePair("biz_content", map.get("biz_content")));
		params.add(new BasicNameValuePair("charset", map.get("charset")));
		params.add(new BasicNameValuePair("method", map.get("method")));
		params.add(new BasicNameValuePair("sign", sign));
		params.add(new BasicNameValuePair("sign_type", map.get("sign_type")));
		params.add(new BasicNameValuePair("timestamp", map.get("timestamp")));
		params.add(new BasicNameValuePair("version", map.get("version")));
		
		return params;
	}
	/**
	 * json转成entity
	 * 
	 */
	@SuppressWarnings("unchecked")
	public static AlipayResponseDTO jsonToEntity(JSONObject json) {
		AlipayResponseDTO entity = new AlipayResponseDTO();
		if (json != null) {
			try {
				Map<String, Object> map1 = ConvertUtil.json2Map(json.toString());
				Iterator<Entry<String, Object>> it = map1.entrySet().iterator();
				Map<String, Object> map = new HashMap<String, Object>();
				while (it.hasNext()) {
					Entry<String, Object> entry = it.next();
					if (!"sign".equals(entry.getKey())) {
						map = (Map<String, Object>) entry.getValue();
					}
				}

				String result_code = map.get("code").toString();
				entity.setResult_code(map.get("msg").toString());
				entity.setResult_code(result_code);
				if ("10000".equals(result_code)) {
					// 交易状态
					if (map.get("trade_status") != null) {
						String trade_status = map.get("trade_status").toString();
						entity.setTrade_status(trade_status);
					}
					// 买家支付宝账号
					if (map.get("buyer_logon_id") != null) {
						String buyer_logon_id = map.get("buyer_logon_id").toString();
						entity.setBuyer_logon_id(buyer_logon_id);
					}
					// 买家支付宝用户号
					if (map.get("buyer_user_id") != null) {
						String buyer_user_id = map.get("buyer_user_id").toString();
						entity.setBuyer_user_id(buyer_user_id);
					}
					// 商户网站唯一订单号
					if (map.get("out_trade_no") != null) {
						String out_trade_no = map.get("out_trade_no").toString();
						entity.setOut_trade_no(out_trade_no);
					}
					// 支付宝交易号
					if (map.get("trade_no") != null) {
						String trade_no = map.get("trade_no").toString();
						entity.setTrade_no(trade_no);
					}
				} else if ("40004".equals(result_code)) {// 失败
					// 错误代码
					if (map.get("sub_code") != null) {
						String detail_error_code = map.get("sub_code").toString();
						entity.setDetail_error_code(detail_error_code);
					}
					// 错误描述
					if (map.get("sub_msg") != null) {
						String detail_error_des = map.get("sub_msg").toString();
						entity.setDetail_error_des(detail_error_des);
					}
				}
			} catch (Exception e) {
				logger.info(e.getMessage(),e);
				e.printStackTrace();
			}
		}
		return entity;
	}
	/**
	 * 支付宝接口(统一下单并支付接口)
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public List<Map<String, Object>> getAlipayCreateAndPayTwo(Map map) {
		String out_trade_no = ConvertUtil.getString(map.get("strOutTradeNo"));
		String app_id = ConvertUtil.getString(map.get("alipayPartnerId"));
		String method = "alipay.trade.pay";
		String charset = ConvertUtil.getString(map.get("charset"));
		String sign_type = ConvertUtil.getString(map.get("alipaySignType"));
		String timestamp = CherryUtil.getSysDateTime("yyyy-MM-dd HH:mm:ss");
		String version = "1.0";
		String biz_content = "";
		
		String total_fee = (String) map.get("strTotalFee");
		// 没有订单标题，所以使用商户订单号做标题
		String subject = out_trade_no;
		String key = ConvertUtil.getString(map.get("alipaySignKey"));
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String time_expire = sdf.format(System.currentTimeMillis() + 24 * 60 * 60 * 1000);

		StringBuilder sb = new StringBuilder();
		sb.append("{\"out_trade_no\":\"" + out_trade_no + "\",");
		sb.append("\"scene\":\"bar_code\",");
		sb.append("\"auth_code\":\"" + map.get("strDynamicId") + "\",");
		sb.append("\"total_amount\":\"" + total_fee// 交易金额
				+ "\",\"discountable_amount\":\"0.00\",");
		sb.append("\"subject\":\"" + subject + "\",\"body\":\"test\",");
		sb.append("\"time_expire\":\"" + time_expire + "\"}");
		biz_content = sb.toString();

		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		Map<String, String> map1 = new TreeMap<String, String>();
		if (!ConvertUtil.isBlank(app_id)) {
			map1.put("app_id", app_id);
		}
		if (!ConvertUtil.isBlank(biz_content)) {
			map1.put("biz_content", biz_content);
		}
		if (!ConvertUtil.isBlank(charset)) {
			map1.put("charset", charset);
		}
		if (!ConvertUtil.isBlank(method)) {
			map1.put("method", method);
		}
		if (!ConvertUtil.isBlank(sign_type)) {
			map1.put("sign_type", sign_type);
		}
		if (!ConvertUtil.isBlank(timestamp)) {
			map1.put("timestamp", timestamp);
		}
		if (!ConvertUtil.isBlank(version)) {
			map1.put("version", version);
		}
		// 把请求参数打包成数组
		logger.info("调用支付宝“统一下单并支付”接口alipay.trade.pay，参数：" + map1.toString());
		JSONObject json = getAlipayDocumentTwo(map1, key);

		AlipayResponseDTO entity = jsonToEntity(json);
		Map<String, Object> maps = new HashMap<String, Object>();
		
		maps.put("result_code", entity.getResult_code());
		if("10000".equals(entity.getResult_code())){
			maps.put("is_success", "T");
			maps.put("result_code", "ORDER_SUCCESS_PAY_SUCCESS");
		}else{
			maps.put("is_success", "F");
		}
		maps.put("detail_error_code", entity.getDetail_error_code());
		maps.put("detail_error_des", entity.getDetail_error_des());
		maps.put("trade_status", entity.getTrade_status());
		maps.put("refund_status", entity.getRefund_status());
		maps.put("error", entity.getError());
		maps.put("buyer_logon_id", entity.getBuyer_logon_id());
		maps.put("buyer_user_id", entity.getBuyer_user_id());
		maps.put("out_trade_no", entity.getOut_trade_no());
		maps.put("trade_no", entity.getTrade_no());
		logger.info("调用支付宝“统一下单并支付”接口alipay.trade.pay，返回值：" + maps.toString());
		list.add(maps);
		return list;
	}
	/**
	 * 支付宝接口(收单撤销接口)
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public List<Map<String, Object>> getAlipayCancelTwo(Map paramMap) {

		String key = ConvertUtil.getString(paramMap.get("alipaySignKey"));
		String out_trade_no = ConvertUtil.getString(paramMap.get("strOutTradeNo"));
		String app_id = ConvertUtil.getString(paramMap.get("alipayPartnerId"));
		String method = "alipay.trade.cancel";
		String charset = ConvertUtil.getString(paramMap.get("charset"));
		String sign_type = ConvertUtil.getString(paramMap.get("alipaySignType"));
		String timestamp = CherryUtil.getSysDateTime("yyyy-MM-dd HH:mm:ss");
		String version = "1.0";
		String biz_content = "";
		
		StringBuilder sb = new StringBuilder();
		sb.append("{\"out_trade_no\":\"" + out_trade_no + "\"}");
		biz_content = sb.toString();

		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		Map<String, String> map = new TreeMap<String, String>();
		if (!ConvertUtil.isBlank(app_id)) {
			map.put("app_id", app_id);
		}
		if (!ConvertUtil.isBlank(biz_content)) {
			map.put("biz_content", biz_content);
		}
		if (!ConvertUtil.isBlank(charset)) {
			map.put("charset", charset);
		}
		if (!ConvertUtil.isBlank(method)) {
			map.put("method", method);
		}
		if (!ConvertUtil.isBlank(sign_type)) {
			map.put("sign_type", sign_type);
		}
		if (!ConvertUtil.isBlank(timestamp)) {
			map.put("timestamp", timestamp);
		}
		if (!ConvertUtil.isBlank(version)) {
			map.put("version", version);
		}
		logger.info("调用支付宝“收单撤销”接口alipay.trade.cancel，参数：" + map.toString());

		JSONObject json = getAlipayDocumentTwo(map, key);
		AlipayResponseDTO entity = jsonToEntity(json);

		Map<String, Object> maps = new HashMap<String, Object>();
		maps.put("result_code", entity.getResult_code());
		if("10000".equals(entity.getResult_code())){
			maps.put("is_success", "T");
		}else{
			maps.put("is_success", "F");
		}
		maps.put("detail_error_code", entity.getDetail_error_code());
		maps.put("detail_error_des", entity.getDetail_error_des());
		maps.put("trade_status", entity.getTrade_status());
		maps.put("refund_status", entity.getRefund_status());
		maps.put("error", entity.getError());
		maps.put("buyer_logon_id", entity.getBuyer_logon_id());
		maps.put("buyer_user_id", entity.getBuyer_user_id());
		maps.put("out_trade_no", entity.getOut_trade_no());
		maps.put("trade_no", entity.getTrade_no());
		logger.info("调用支付宝“收单撤销”接口alipay.trade.cancel，返回值：" + maps.toString());
		list.add(maps);
		return list;
	}
	/**
	 * 支付宝接口(收单退款接口)
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public List<Map<String, Object>> getAlipayRefundTwo(Map paramMap) {

		String key = ConvertUtil.getString(paramMap.get("alipaySignKey"));
		String out_trade_no = ConvertUtil.getString(paramMap.get("strOutTradeNo"));
		String app_id = ConvertUtil.getString(paramMap.get("alipayPartnerId"));
		String method = "alipay.trade.refund";
		String charset = ConvertUtil.getString(paramMap.get("aliPayInputCharSet"));
		String sign_type = ConvertUtil.getString(paramMap.get("alipaySignType"));
		String timestamp = CherryUtil.getSysDateTime("yyyy-MM-dd HH:mm:ss");
		String strRefundAmount = ConvertUtil.getString(paramMap.get("strRefundAmount"));
		String version = "1.0";
		String biz_content = "";
		
		StringBuilder sb = new StringBuilder();
		sb.append("{\"out_trade_no\":\"" + out_trade_no + "\",");
		sb.append("\"refund_amount\":\"" + strRefundAmount + "\"}");// 交易金额
		biz_content = sb.toString();

		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		Map<String, String> map = new TreeMap<String, String>();
		if (!ConvertUtil.isBlank(app_id)) {
			map.put("app_id", app_id);
		}
		if (!ConvertUtil.isBlank(biz_content)) {
			map.put("biz_content", biz_content);
		}
		if (!ConvertUtil.isBlank(charset)) {
			map.put("charset", charset);
		}
		if (!ConvertUtil.isBlank(method)) {
			map.put("method", method);
		}
		if (!ConvertUtil.isBlank(sign_type)) {
			map.put("sign_type", sign_type);
		}
		if (!ConvertUtil.isBlank(timestamp)) {
			map.put("timestamp", timestamp);
		}
		if (!ConvertUtil.isBlank(version)) {
			map.put("version", version);
		}
		logger.info("调用支付宝“收单退款”接口alipay.trade.refund，参数：" + map.toString());
		JSONObject json = getAlipayDocumentTwo(map, key);
		AlipayResponseDTO entity = jsonToEntity(json);
		Map<String, Object> maps = new HashMap<String, Object>();
		maps.put("result_code", entity.getResult_code());
		if("10000".equals(entity.getResult_code())){
			maps.put("is_success", "T");
			maps.put("result_code", "SUCCESS");
		}else{
			maps.put("is_success", "F");
			maps.put("result_code", "FALE");
		}
		maps.put("detail_error_code", entity.getDetail_error_code());
		maps.put("detail_error_des", entity.getDetail_error_des());
		maps.put("trade_status", entity.getTrade_status());
		maps.put("refund_status", entity.getRefund_status());
		maps.put("error", entity.getError());
		maps.put("buyer_logon_id", entity.getBuyer_logon_id());
		maps.put("buyer_user_id", entity.getBuyer_user_id());
		maps.put("out_trade_no", entity.getOut_trade_no());
		maps.put("trade_no", entity.getTrade_no());
		logger.info("调用支付宝“收单退款”接口alipay.trade.refund，返回值：" + maps.toString());
		list.add(maps);
		return list;
	}
	
	/**支付宝接口(收单退款接口,先调查询接口)*/
	@Override
	public List<Map<String, Object>> getAlipayQueryAndRefundTwo(Map paramMap) {
		List<Map<String, Object>> list = getAlipayQueryTwo(paramMap);
		logger.info("调用支付宝2.0退款查询接口，销售单据号为："+ConvertUtil.getString(paramMap.get("strOutTradeNo"))+" 退货单据号为："+ConvertUtil.getString(paramMap.get("strOutRequestNo"))
				+"返回值:"+ConvertUtil.getString(list));
		if ("10000".equals(list.get(0).get("result_code"))) {
			if ("WAIT_BUYER_PAY".equals(list.get(0).get("trade_status"))) {// 已付款成功，不允许退款
				list.get(0).put("result_code", "REFUND_FAIL_WAIT_BUYER_PAY");
				return list;
			} else if ("TRADE_CLOSED".equals(list.get(0).get("trade_status"))) {// 单据已关闭，不允许退款
				list.get(0).put("result_code", "REFUND_FAIL_TRADE_CLOSED");
				return list;
			} else if ("TRADE_FINISHED".equals(list.get(0).get("trade_status"))) {// 交易已完成，不允许退款
				list.get(0).put("result_code", "REFUND_FAIL_TRADE_FINISHED");
				return list;
			} else if ("TRADE_SUCCESS".equals(list.get(0).get("trade_status"))) {
				return getAlipayRefundTwo(paramMap);
			} else {
				logger.error("getAlipayQueryTwo",  "单据查询异常！ 传递参数：p_strOutTradeNo=" + paramMap.toString());
				return list;
			}
		} else if ("40004".equals(list.get(0).get("result_code"))) {
			list.get(0).put("result_code", "REFUND_FAIL_ORDER_NOT_EXISTS");
			return list;
		} else {
			logger.error("alipayQueryAndRefund_V2", "单据查询异常！ 传递参数：p_strOutTradeNo=" + paramMap.toString());
			return list;
		}
	}
	@Override
	public List<Map<String, Object>> getAlipayQueryAndCancelTwo(Map paramMap) {
		List<Map<String, Object>> list = getAlipayQueryTwo(paramMap);
		if ("10000".equals(list.get(0).get("result_code"))) {
			if ("TRADE_SUCCESS".equals(list.get(0).get("trade_status"))) {// 已付款成功,不允许撤销
				list.get(0).put("result_code", "ACQUIRE_FAIL_TRADE_SUCCESS");
				return list;
			} else {// 付款没成功，撤销
				return getAlipayCancelTwo(paramMap);
			}
		} else if ("40004".equals(list.get(0).get("result_code"))) {
			list.get(0).put("result_code", "ORDER_PAY_NOT_EXISTS");
			return list;
		} else {
			logger.error("getAlipayQueryAndCancelTwo","单据查询异常！ 传递参数：" + paramMap.toString());
			return list;
		}
	}
	
}
