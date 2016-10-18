package com.cherry.cm.pay.bl;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.security.KeyStore;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import javax.net.ssl.SSLContext;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.SyncBasicHttpParams;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.cm.pay.dto.WeChatpayResponseDTO;
import com.cherry.cm.pay.interfaces.WeChatPayIf;
import com.cherry.cm.util.ConvertUtil;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.client.apache4.ApacheHttpClient4;
import com.sun.jersey.client.apache4.config.DefaultApacheHttpClient4Config;


public class WeChatPayBL implements WeChatPayIf{
	
	private static Logger logger = LoggerFactory.getLogger(WeChatPayBL.class.getName());
	

//	private static String appid = "wxa571e8dc7778540e";// 微信分配的公众账号ID
//	private static String mch_id = "10010716";// 微信支付分配的商户号
//	private static String sub_mch_id = "";// 微信支付分配的子商户号，受理模式下必填
//	private static String paternerKey = "linqingxuanforestcabin2164067506";// 财付通商户权限密钥Key
	
	private static String nonce_str = "witposmobilewitposmobile";// 随机字符串，不长于32位
	private static String _input_charset = "UTF-8";// 字符集类型
	private static Map<String, Object> webResourceMap = new HashMap<String, Object>();
	private static Client client;
	
	
	@Override
	public boolean getWeChatConfig(Map map) {
		
		return true;
	}
	
	/**
	 * 微信接口(被扫订单查询)
	 */
	@Override
	public List<Map<String, Object>> getOrderQuery(Map paramMap) {
		String appid = (String) paramMap.get("appid");
		String mch_id = (String) paramMap.get("mch_id");
		String sub_mch_id = (String) paramMap.get("sub_mch_id");
		String paternerKey = (String) paramMap.get("paternerKey");
		
		String out_trade_no = (String)paramMap.get("out_trade_no");
		
		if(out_trade_no.length() > 32){
			out_trade_no = out_trade_no.substring(2, out_trade_no.length());
		}
		String url = "https://api.mch.weixin.qq.com/pay/orderquery";// 接口名称
		String transaction_id = "";
		
		StringBuffer xml = new StringBuffer();
		Map<String, String> map = new TreeMap<String, String>();
		xml.append("<xml>");

		map.put("appid", appid);
		xml.append("<appid>" + appid + "</appid>");

		map.put("mch_id", mch_id);
		xml.append("<mch_id>" + mch_id + "</mch_id>");

		map.put("nonce_str", nonce_str);
		xml.append("<nonce_str>" + nonce_str + "</nonce_str>");

		map.put("out_trade_no", out_trade_no);
		xml.append("<out_trade_no>" + out_trade_no + "</out_trade_no>");

		if (!ConvertUtil.isBlank(sub_mch_id)) {
			map.put("sub_mch_id", sub_mch_id);
			xml.append("<sub_mch_id>" + sub_mch_id + "</sub_mch_id>");
		}
		if (!ConvertUtil.isBlank(transaction_id)) {
			map.put("transaction_id", transaction_id);
			xml.append("<transaction_id>" + transaction_id + "</transaction_id>");
		}
		xml.append("<sign><![CDATA[" + generateSign(map, paternerKey) + "]]></sign>");
		xml.append("</xml>");

		WeChatpayResponseDTO entity = null;
		try {
			Document doc = sendXMLData(url, xml.toString().getBytes());
			entity = docToEntity(doc);
		} catch (Exception e) {
			logger.error("", e.toString(), e);
		}
		logger.info("微信被扫订单查询接口订单号:"+out_trade_no+" 结果为"+ConvertUtil.getString(EntityToList(entity)));
		return EntityToList(entity);
	}
	/**
	 * 微信接口(被扫支付)
	 */
	@Override
	public List<Map<String, Object>> getMicropay(Map paramMap) {
		String appid = (String) paramMap.get("appid");
		String mch_id = (String) paramMap.get("mch_id");
		String sub_mch_id = (String) paramMap.get("sub_mch_id");
		String paternerKey = (String) paramMap.get("paternerKey");
		
		String out_trade_no = (String)paramMap.get("out_trade_no");
		Integer total_fee = null;
		if (paramMap.get("total_fee") != null && !"".equals(paramMap.get("total_fee"))) {
			total_fee = Integer.parseInt(paramMap.get("total_fee").toString());
		}
		String device_info = (String)paramMap.get("device_info");
		String auth_code = (String)paramMap.get("auth_code");
		
		InetAddress addr;
		String spbill_create_ip = null;
		try {
			addr = InetAddress.getLocalHost();
			spbill_create_ip = addr.getHostAddress().toString();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		
		if(out_trade_no.length() > 32){
			out_trade_no = out_trade_no.substring(2, out_trade_no.length());
		}
		String url = "https://api.mch.weixin.qq.com/pay/micropay";// 接口名称
		String transaction_id = "";
		String body = out_trade_no;
//		String body_after_encode = body;
//		try {
//			body_after_encode = URLEncoder.encode(body, _input_charset);
//		} catch (UnsupportedEncodingException e) {
//			e.printStackTrace();
//		}
		String attach = "";
		String time_start = "";
		String time_expire = "";
		String goods_tag = "";
		
		StringBuffer xml = new StringBuffer();
		Map<String, String> map = new TreeMap<String, String>();

		xml.append("<xml>");

		map.put("appid", appid);
		xml.append("<appid>" + appid + "</appid>");

		if (!ConvertUtil.isBlank(attach)) {
			map.put("attach", attach);
			xml.append("<attach>" + attach + "</attach>");
		}

		map.put("auth_code", auth_code);
		xml.append("<auth_code>" + auth_code + "</auth_code>");

		map.put("body", body);
		xml.append("<body>" + body + "</body>");
		
		if (!ConvertUtil.isBlank(device_info)) {
			map.put("device_info", device_info);
			xml.append("<device_info>" + device_info + "</device_info>");
		}
		
		if (!ConvertUtil.isBlank(goods_tag)) {
			map.put("goods_tag", goods_tag);
			xml.append("<goods_tag>" + goods_tag + "</goods_tag>");
		}

		map.put("mch_id", mch_id);
		xml.append("<mch_id>" + mch_id + "</mch_id>");

		map.put("nonce_str", nonce_str);
		xml.append("<nonce_str>" + nonce_str + "</nonce_str>");

		map.put("out_trade_no", out_trade_no);
		xml.append("<out_trade_no>" + out_trade_no + "</out_trade_no>");

		map.put("spbill_create_ip", spbill_create_ip);
		xml.append("<spbill_create_ip>" + spbill_create_ip + "</spbill_create_ip>");

		if (!ConvertUtil.isBlank(sub_mch_id)) {
			map.put("sub_mch_id", sub_mch_id);
			xml.append("<sub_mch_id>" + sub_mch_id + "</sub_mch_id>");
		}

		if (!ConvertUtil.isBlank(time_expire)) {
			map.put("time_expire", time_expire);
			xml.append("<time_expire>" + time_expire + "</time_expire>");
		}

		if (!ConvertUtil.isBlank(time_start)) {
			map.put("time_start", time_start);
			xml.append("<time_start>" + time_start + "</time_start>");
		}

		map.put("total_fee", String.valueOf(total_fee));
		xml.append("<total_fee>" + total_fee + "</total_fee>");

		if (!ConvertUtil.isBlank(transaction_id)) {
			map.put("transaction_id", transaction_id);
			xml.append("<transaction_id>" + transaction_id + "</transaction_id>");
		}
		xml.append("<sign><![CDATA[" + generateSign(map, paternerKey) + "]]></sign>");
		xml.append("</xml>");
		
		WeChatpayResponseDTO entity = null;
		logger.info("调用微信当面付订单号:"+out_trade_no+"=====交易起始时间:"+time_start+"=====交易截至时间:"+time_expire+"=====总金额:"+total_fee);
		try {
			Document doc = sendXMLData(url, xml.toString().getBytes());
//			Document doc = sendXMLData(url, xml.toString());
			entity = docToEntity(doc);
		} catch (Exception e) {
			logger.error("", e.toString(), e);
		}
		
		return EntityToList(entity);
	}

	/**
	 * 微信接口(退款申请)
	 */
	@Override
	public List<Map<String, Object>> getWEpayRefund(Map paramMap) {
		// 品牌代码
		String brandCode = ConvertUtil.getString(paramMap.get("brandCode"));
		String appid = (String) paramMap.get("appid");
		String mch_id = (String) paramMap.get("mch_id");
		String sub_mch_id = (String) paramMap.get("sub_mch_id");
		String paternerKey = (String) paramMap.get("paternerKey");
		
		String out_trade_no = (String)paramMap.get("out_trade_no");
		String out_refund_no = (String)paramMap.get("out_refund_no");
		
		Map<String, Object> mapQuery = new HashMap<String, Object>();
		mapQuery.put("out_trade_no", out_trade_no);
		mapQuery.put("appid", appid);
		mapQuery.put("mch_id", mch_id);
		mapQuery.put("sub_mch_id", sub_mch_id);
		mapQuery.put("paternerKey", paternerKey);
		
		List<Map<String, Object>> orderQueryList = getOrderQuery(mapQuery);
		if (orderQueryList.size() > 0) {
			if("SUCCESS".equals(orderQueryList.get(0).get("return_code"))){
				if("SUCCESS".equals(orderQueryList.get(0).get("result_code"))){
					if("REFUND".equals(orderQueryList.get(0).get("trade_state"))){
						return orderQueryList;
					}
				} else {
					return orderQueryList;
				}
			} else {
				return orderQueryList;
			}
		} else {
			return orderQueryList;
		}
		

		
		
		Integer total_fee = null;
		if (paramMap.get("total_fee") != null && !"".equals(paramMap.get("total_fee"))) {
			total_fee = Integer.parseInt(paramMap.get("total_fee").toString());
		}
		Integer refund_fee = null;
		if (paramMap.get("refund_fee") != null && !"".equals(paramMap.get("refund_fee"))) {
			refund_fee = Integer.parseInt(paramMap.get("refund_fee").toString());
		}
		String device_info = (String)paramMap.get("device_info");
		
		if(out_trade_no.length() > 32){
			out_trade_no = out_trade_no.substring(2, out_trade_no.length());
		}
		if(out_refund_no.length() > 32){
			out_refund_no = out_refund_no.substring(2, out_refund_no.length());
		}
		String url = "https://api.mch.weixin.qq.com/secapi/pay/refund";// 接口名称
		String transaction_id = "";
		String op_user_id = mch_id;
				
		StringBuffer xml = new StringBuffer();
		Map<String, String> map = new TreeMap<String, String>();

		xml.append("<xml>");

		map.put("appid", appid);
		xml.append("<appid>" + appid + "</appid>");

		if (!ConvertUtil.isBlank(device_info)) {
			map.put("device_info", device_info);
			xml.append("<device_info>" + device_info + "</device_info>");
		}

		map.put("mch_id", mch_id);
		xml.append("<mch_id>" + mch_id + "</mch_id>");

		map.put("nonce_str", nonce_str);
		xml.append("<nonce_str>" + nonce_str + "</nonce_str>");

		map.put("op_user_id", String.valueOf(op_user_id));
		xml.append("<op_user_id>" + op_user_id + "</op_user_id>");
		
		map.put("out_refund_no", out_refund_no);
		xml.append("<out_refund_no>" + out_refund_no + "</out_refund_no>");
		
		map.put("out_trade_no", out_trade_no);
		xml.append("<out_trade_no>" + out_trade_no + "</out_trade_no>");

		map.put("refund_fee", String.valueOf(refund_fee));
		xml.append("<refund_fee>" + refund_fee + "</refund_fee>");
		
		if (!ConvertUtil.isBlank(sub_mch_id)) {
			map.put("sub_mch_id", sub_mch_id);
			xml.append("<sub_mch_id>" + sub_mch_id + "</sub_mch_id>");
		}
		
		map.put("total_fee", String.valueOf(total_fee));
		xml.append("<total_fee>" + total_fee + "</total_fee>");

		if (!ConvertUtil.isBlank(transaction_id)) {
			map.put("transaction_id", transaction_id);
			xml.append("<transaction_id>" + transaction_id + "</transaction_id>");
		}
		xml.append("<sign><![CDATA[" + generateSign(map, paternerKey) + "]]></sign>");
		xml.append("</xml>");
		
		WeChatpayResponseDTO entity = null;
		try {
			Document doc = sendXMLData(url, xml.toString(), mch_id, brandCode);
			entity = docToEntity(doc);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("", e.toString(), e);
		}
		
		return EntityToList(entity);
	}
	
	/**
	 * 微信接口(退款查询)
	 */
	@Override
	public List<Map<String, Object>> getWEpayRefundQuery(Map paramMap) {
		String appid = (String) paramMap.get("appid");
		String mch_id = (String) paramMap.get("mch_id");
		String sub_mch_id = (String) paramMap.get("sub_mch_id");
		String paternerKey = (String) paramMap.get("paternerKey");
		
		String out_trade_no = (String)paramMap.get("out_trade_no");
		if(out_trade_no.length() > 32){
			out_trade_no = out_trade_no.substring(2, out_trade_no.length());
		}
		String url = "https://api.mch.weixin.qq.com/pay/refundquery";// 接口名称
		String transaction_id = "";
		String device_info = "";
		
		StringBuffer xml = new StringBuffer();
		Map<String, String> map = new TreeMap<String, String>();

		xml.append("<xml>");

		map.put("appid", appid);
		xml.append("<appid>" + appid + "</appid>");

		if (!ConvertUtil.isBlank(device_info)) {
			map.put("device_info", device_info);
			xml.append("<device_info>" + device_info + "</device_info>");
		}

		map.put("mch_id", mch_id);
		xml.append("<mch_id>" + mch_id + "</mch_id>");

		map.put("nonce_str", nonce_str);
		xml.append("<nonce_str>" + nonce_str + "</nonce_str>");
		
		map.put("out_trade_no", out_trade_no);
		xml.append("<out_trade_no>" + out_trade_no + "</out_trade_no>");

		if (!ConvertUtil.isBlank(sub_mch_id)) {
			map.put("sub_mch_id", sub_mch_id);
			xml.append("<sub_mch_id>" + sub_mch_id + "</sub_mch_id>");
		}
		
		if (!ConvertUtil.isBlank(transaction_id)) {
			map.put("transaction_id", transaction_id);
			xml.append("<transaction_id>" + transaction_id + "</transaction_id>");
		}
		xml.append("<sign><![CDATA[" + generateSign(map, paternerKey) + "]]></sign>");
		xml.append("</xml>");
		
		WeChatpayResponseDTO entity = null;
		try {
			Document doc = sendXMLData(url, xml.toString().getBytes());
			entity = docToEntity(doc);
		} catch (Exception e) {
			logger.error("", e.toString(), e);
		}
		
		return EntityToList(entity);
	}
	
	/**
	 * 微信接口(撤销接口)
	 */
	@Override
	public List<Map<String, Object>> getWEpayReverse(Map paramMap) {
		// 品牌代码
		String brandCode = ConvertUtil.getString(paramMap.get("brandCode"));
		String appid = (String) paramMap.get("appid");
		String mch_id = (String) paramMap.get("mch_id");
		String sub_mch_id = (String) paramMap.get("sub_mch_id");
		String paternerKey = (String) paramMap.get("paternerKey");
		
		String out_trade_no = (String)paramMap.get("out_trade_no");
		if(out_trade_no.length() > 32){
			out_trade_no = out_trade_no.substring(2, out_trade_no.length());
		}
		String url = "https://api.mch.weixin.qq.com/secapi/pay/reverse";// 接口名称
		String transaction_id = "";
		
		StringBuffer xml = new StringBuffer();
		Map<String, String> map = new TreeMap<String, String>();

		xml.append("<xml>");

		map.put("appid", appid);
		xml.append("<appid>" + appid + "</appid>");

		map.put("mch_id", mch_id);
		xml.append("<mch_id>" + mch_id + "</mch_id>");

		map.put("nonce_str", nonce_str);
		xml.append("<nonce_str>" + nonce_str + "</nonce_str>");

		map.put("out_trade_no", out_trade_no);
		xml.append("<out_trade_no>" + out_trade_no + "</out_trade_no>");

		if (!ConvertUtil.isBlank(sub_mch_id)) {
			map.put("sub_mch_id", sub_mch_id);
			xml.append("<sub_mch_id>" + sub_mch_id + "</sub_mch_id>");
		}
		if (!ConvertUtil.isBlank(transaction_id)) {
			map.put("transaction_id", transaction_id);
			xml.append("<transaction_id>" + transaction_id + "</transaction_id>");
		}
		xml.append("<sign><![CDATA[" + generateSign(map, paternerKey) + "]]></sign>");
		xml.append("</xml>");
		
		WeChatpayResponseDTO entity = null;
		try {
			Document doc = sendXMLData(url, xml.toString(), mch_id, brandCode);
			entity = docToEntity(doc);
		} catch (Exception e) {
			logger.error("", e.toString(), e);
		}
		
		return EntityToList(entity);
	}
	
	/**
	 * 微信接口(对账单接口)
	 */
	@Override
	public List<Map<String, Object>> getWEpayDownloadbill(Map paramMap) {
		String appid = (String) paramMap.get("appid");
		String mch_id = (String) paramMap.get("mch_id");
		String sub_mch_id = (String) paramMap.get("sub_mch_id");
		String paternerKey = (String) paramMap.get("paternerKey");
		
		String bill_date = (String)paramMap.get("bill_date");
		String bill_type = (String)paramMap.get("bill_type");
		String device_info = (String)paramMap.get("device_info");
		
		String url = "https://api.mch.weixin.qq.com/pay/downloadbill";// 接口名称
		
		StringBuffer xml = new StringBuffer();
		Map<String, String> map = new TreeMap<String, String>();

		xml.append("<xml>");

		map.put("appid", appid);
		xml.append("<appid>" + appid + "</appid>");

		map.put("bill_date", bill_date);
		xml.append("<bill_date>" + bill_date + "</bill_date>");

		if (!ConvertUtil.isBlank(bill_type)) {
			map.put("bill_type", bill_type);
			xml.append("<bill_type>" + bill_type + "</bill_type>");
		}
		
		if (!ConvertUtil.isBlank(device_info)) {
			map.put("device_info", device_info);
			xml.append("<device_info>" + device_info + "</device_info>");
		}
		
		map.put("mch_id", mch_id);
		xml.append("<mch_id>" + mch_id + "</mch_id>");

		map.put("nonce_str", nonce_str);
		xml.append("<nonce_str>" + nonce_str + "</nonce_str>");

		
		if (!ConvertUtil.isBlank(sub_mch_id)) {
			map.put("sub_mch_id", sub_mch_id);
			xml.append("<sub_mch_id>" + sub_mch_id + "</sub_mch_id>");
		}
		
		xml.append("<sign><![CDATA[" + generateSign(map, paternerKey) + "]]></sign>");
		xml.append("</xml>");
		
		WeChatpayResponseDTO entity = null;
		try {
			Document doc = sendXMLData(url, xml.toString().getBytes());
			entity = docToEntity(doc);
		} catch (Exception e) {
			logger.error("", e.toString(), e);
		}
		
		return EntityToList(entity);
	}
	
	public static Document sendXMLData(String urlPath, String data, String mch_id, String brandCode) throws Exception {
		// 品牌代号
		String brandCodeArr = "";
		if(brandCode!=null && !"".equals(brandCode)){
			brandCodeArr =  brandCode.toLowerCase();
		}else {
			logger.info("品牌代号为空");
		}
		InputStream instream = null;
		KeyStore keyStore = null;
		try {
			keyStore = KeyStore.getInstance("PKCS12");
			instream = WeChatPayBL.class.getResourceAsStream("/conf/sslcertificate/"+ brandCodeArr +"/apiclient_cert.p12");
			keyStore.load(instream, mch_id.toCharArray());
		} catch (Exception e){
			e.printStackTrace();
		} finally {
			try {
				instream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		// Trust own CA and all self-signed certs
		SSLContext sslcontext = null;
		try {
			sslcontext = SSLContexts.custom().loadKeyMaterial(keyStore, mch_id.toCharArray()).build();
		} catch (Exception e) {
			e.printStackTrace();
		}
		// Allow TLSv1 protocol only
		SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslcontext, new String[] { "TLSv1" }, null,
				SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
		CloseableHttpClient httpclient = HttpClients.custom().setSSLSocketFactory(sslsf).build();
		try {
			HttpPost httppost = new HttpPost(urlPath);
			StringEntity strEntity = new StringEntity(data, _input_charset); 
		    httppost.addHeader("Content-Type", "text/xml");
			httppost.setEntity(strEntity);

			CloseableHttpResponse response = httpclient.execute(httppost);
			
			HttpEntity entity = response.getEntity();
			InputStream is = entity.getContent();
			Document doc = new SAXReader().read(is);
			return doc;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				httpclient.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	
	// 以流方式向服务器端发送xml文件数据，并获得服务器端输出流
//	public Document sendXMLData(String urlPath, String data) throws Exception {
//		
//		WebResource webResource = getWebResource(urlPath);
//		String entity = webResource.type(MediaType.TEXT_XML).entity(data.getBytes()).post(String.class);
//		InputStream is = new ByteArrayInputStream(entity.getBytes());
//		Document doc = new SAXReader().read(is);
//		return doc;
//	}
		
	public Document sendXMLData(String urlPath, byte[] data) throws Exception {
		int timeout = 3000;// 超时时间
		URL url = new URL(urlPath);
		// 打开连接
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		// 设置提交方式
		conn.setDoOutput(true);
		conn.setDoInput(true);
		conn.setRequestMethod("POST");
		// post方式不能使用缓存
		conn.setUseCaches(false);
		conn.setInstanceFollowRedirects(true);
		// 设置连接超时时间
		conn.setConnectTimeout(timeout);
		// 配置本次连接的Content-Type
		conn.setRequestProperty("Content-Type", "text/xml;charset=" + _input_charset);
		// 维持长连接
		conn.setRequestProperty("Connection", "Keep-Alive");
		// 设置浏览器编码
		conn.setRequestProperty("Charset", _input_charset);
		// 将请求参数数据向服务器端发送
		DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
		dos.write(data);
		dos.flush();
		dos.close();
		if (conn.getResponseCode() == 200) {
			// 获得服务器端输出流
			InputStream is = conn.getInputStream();
			Document doc = new SAXReader().read(is);
			return doc;
		}
		
		return null;
	}
	
	/**
     * 创建Client对象的配置信息对象
     * 
     * @return 配置信息对象
     */
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
	 * 获取sign
	 * 
	 * @param map
	 * @return
	 */
	public static String generateSign(Map<String, String> map, String paternerKey) {
		StringBuilder string1 = new StringBuilder();
		// 把数组所有元素，按照“参数=参数值”的模式用“&”字符拼接成字符串，并对参数值做URLEncode
		Iterator<Entry<String, String>> it = map.entrySet().iterator();
		while (it.hasNext()) {
			Entry<String, String> entry = (Entry<String, String>) it.next();
			try {
				string1.append(entry.getKey() + "=" + URLEncoder.encode((String) entry.getValue(), _input_charset)
						+ "&");

			} catch (UnsupportedEncodingException e) {
				logger.error("", e.toString(), e);
			}
		}
		String stringSignTemp = string1.toString() + "key=" + paternerKey;
		// 签名结果
		String sign = ConvertUtil.createSign(stringSignTemp).toUpperCase();

		return sign;
	}

	/**
	 * document转成entity
	 * 
	 */
	public static WeChatpayResponseDTO docToEntity(Document doc) {
		WeChatpayResponseDTO entity = new WeChatpayResponseDTO();
		if (doc != null) {
			// 取得根节点
			Element root = doc.getRootElement();
			if (root.element("return_code") != null) {
				String return_code = root.element("return_code").getText();
				entity.setReturn_code(return_code);
			}
			if (root.element("return_msg") != null) {
				String return_msg = root.element("return_msg").getText();
				entity.setReturn_msg(return_msg);
			}
			if (root.element("appid") != null) {
				String appid = root.element("appid").getText();
				entity.setAppid(appid);
			}
			if (root.element("mch_id") != null) {
				String mch_id = root.element("mch_id").getText();
				entity.setMch_id(mch_id);
			}
			if (root.element("sub_mch_id") != null) {
				String sub_mch_id = root.element("sub_mch_id").getText();
				entity.setSub_mch_id(sub_mch_id);
			}
			if (root.element("device_info") != null) {
				String device_info = root.element("device_info").getText();
				entity.setDevice_info(device_info);
			}
			if (root.element("nonce_str") != null) {
				String nonce_str = root.element("nonce_str").getText();
				entity.setNonce_str(nonce_str);
			}
			if (root.element("sign") != null) {
				String sign = root.element("sign").getText();
				entity.setSign(sign);
			}
			if (root.element("result_code") != null) {
				String result_code = root.element("result_code").getText();
				entity.setResult_code(result_code);
			}
			if (root.element("err_code") != null) {
				String err_code = root.element("err_code").getText();
				entity.setErr_code(err_code);
			}
			if (root.element("err_code_des") != null) {
				String err_code_des = root.element("err_code_des").getText();
				entity.setErr_code_des(err_code_des);
			}
			if (root.element("recall") != null) {
				String recall = root.element("recall").getText();
				entity.setRecall(recall);
			}
			if (root.element("openid") != null) {
				String openid = root.element("openid").getText();
				entity.setOpenid(openid);
			}
			if (root.element("is_subscribe") != null) {
				String is_subscribe = root.element("is_subscribe").getText();
				entity.setIs_subscribe(is_subscribe);
			}
			if (root.element("trade_type") != null) {
				String trade_type = root.element("trade_type").getText();
				entity.setTrade_type(trade_type);
			}
			if (root.element("bank_type") != null) {
				String bank_type = root.element("bank_type").getText();
				entity.setBank_type(bank_type);
			}
			if (root.element("total_fee") != null) {
				String total_fee = root.element("total_fee").getText();
				entity.setTotal_fee(Integer.parseInt(total_fee));
			}
			if (root.element("coupon_fee") != null) {
				String coupon_fee = root.element("coupon_fee").getText();
				entity.setCoupon_fee(Integer.parseInt(coupon_fee));
			}
			if (root.element("fee_type") != null) {
				String fee_type = root.element("fee_type").getText();
				entity.setFee_type(fee_type);
			}
			if (root.element("transaction_id") != null) {
				String transaction_id = root.element("transaction_id").getText();
				entity.setTransaction_id(transaction_id);
			}
			if (root.element("out_trade_no") != null) {
				String out_trade_no = root.element("out_trade_no").getText();
				entity.setOut_trade_no(out_trade_no);
			}
			if (root.element("out_refund_no") != null) {
				String out_refund_no = root.element("out_refund_no").getText();
				entity.setOut_refund_no(out_refund_no);
			}
			if (root.element("refund_id") != null) {
				String refund_id = root.element("refund_id").getText();
				entity.setRefund_id(refund_id);
			}
			if (root.element("refund_fee") != null) {
				String refund_fee = root.element("refund_fee").getText();
				entity.setRefund_fee(Integer.parseInt(refund_fee));
			}
			if (root.element("refund_count") != null) {
				String refund_count = root.element("refund_count").getText();
				entity.setRefund_count(Integer.parseInt(refund_count));
			}
			if (root.element("refund_status") != null) {
				String refund_status = root.element("refund_status").getText();
				entity.setRefund_status(refund_status);
			}
			if (root.element("refund_channel") != null) {
				String refund_channel = root.element("refund_channel").getText();
				entity.setRefund_channel(refund_channel);
			}
			if (root.element("coupon_refund_fee") != null) {
				String coupon_refund_fee = root.element("coupon_refund_fee").getText();
				entity.setCoupon_refund_fee(Integer.parseInt(coupon_refund_fee));
			}
			if (root.element("attach") != null) {
				String attach = root.element("attach").getText();
				entity.setAttach(attach);
			}
			if (root.element("time_end") != null) {
				String time_end = root.element("time_end").getText();
				entity.setTime_end(time_end);
			}
			if (root.element("trade_state") != null) {
				String trade_state = root.element("trade_state").getText();
				entity.setTrade_state(trade_state);
			}
		}
		return entity;
	}
	
	/**
	 * 将Entity转换成List
	 * @param entity
	 * @return
	 */
	public static List<Map<String, Object>> EntityToList(WeChatpayResponseDTO entity) {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		Map<String, Object> maps = new HashMap<String, Object>();
		if (entity != null) {
			maps.put("return_code", entity.getReturn_code());
			maps.put("return_msg", entity.getReturn_msg());
			maps.put("appid", entity.getAppid());
			maps.put("mch_id", entity.getMch_id());
			maps.put("sub_mch_id", entity.getSub_mch_id());
			maps.put("device_info", entity.getDevice_info());
			maps.put("nonce_str", entity.getNonce_str());
			maps.put("sign", entity.getSign());
			maps.put("result_code", entity.getResult_code());
			maps.put("err_code", entity.getErr_code());
			maps.put("err_code_des", entity.getErr_code_des());
			maps.put("recall", entity.getRecall());
			maps.put("openid", entity.getOpenid());
			maps.put("is_subscribe", entity.getIs_subscribe());
			maps.put("trade_type", entity.getTrade_type());
			maps.put("bank_type", entity.getBank_type());
			maps.put("total_fee", entity.getTotal_fee());
			maps.put("coupon_fee", entity.getCoupon_fee());
			maps.put("fee_type", entity.getFee_type());
			maps.put("transaction_id", entity.getTransaction_id());
			maps.put("out_trade_no", entity.getOut_trade_no());
			maps.put("out_refund_no", entity.getOut_refund_no());
			maps.put("refund_id", entity.getRefund_id());
			maps.put("refund_fee", entity.getRefund_fee());
			maps.put("refund_count", entity.getRefund_count());
			maps.put("refund_status", entity.getRefund_status());
			maps.put("refund_channel", entity.getRefund_channel());
			maps.put("coupon_refund_fee", entity.getCoupon_refund_fee());
			maps.put("attach", entity.getAttach());
			maps.put("time_end", entity.getTime_end());
			maps.put("trade_state", entity.getTrade_state());
			list.add(maps);
		}
		
		return list;
	}


}
