package com.cherry.mb.svc.bl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.ws.rs.core.MultivaluedMap;



import com.cherry.cm.cmbussiness.bl.BINOLCM27_BL;
import com.cherry.cm.core.CherryAESCoder;
import com.cherry.cm.core.PropertiesUtil;
import com.cherry.cm.core.ThirdPartyConfig;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.core.util.MultivaluedMapImpl;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.mb.svc.interfaces.BINOLMBSVC03_IF;

public class BINOLMBSVC03_BL implements BINOLMBSVC03_IF{
	static{
		SavingscardWebServiceUrl = PropertiesUtil.pps.getProperty("SavingscardWebServiceUrl");
		SavingscardAppID =PropertiesUtil.pps.getProperty("SavingscardAppID");
	}
	private static String SavingscardWebServiceUrl;
	private static String SavingscardAppID;
	@Resource
	private BINOLCM27_BL bINOLCM27_BL;
	@Resource(name="thirdPartyConfig")
	private ThirdPartyConfig thirdPartyConfig;
	
	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> getCard(Map<String, Object> map) throws Exception {
		String brandCode = ConvertUtil.getString(map.get("brandCode"));
		Map<String,Object> data=new HashMap<String, Object>();
		data.put("CardCode", ConvertUtil.getString(map.get("cardCode")));
		// 柜台号
		data.put("CounterCode", ConvertUtil.getString(map.get("counterCode")));
		data.put("TradeType", "GetSavingsCardInfo");
		MultivaluedMap<String, String> queryParams = new MultivaluedMapImpl();
		queryParams.add("brandCode", brandCode);
		queryParams.add("appID", SavingscardAppID + "_" + brandCode);
		queryParams.add("paramData", CherryAESCoder.encrypt(CherryUtil.map2Json(data), thirdPartyConfig.getDynamicAESKey(SavingscardAppID,brandCode)));
		WebResource wr= bINOLCM27_BL.getWebResource(SavingscardWebServiceUrl);
		String  result_card=wr.queryParams(queryParams).get(String.class);
		Map<String,Object> result_card1=ConvertUtil.json2Map(result_card);
		String errorCode=ConvertUtil.getString(result_card1.get("ERRORCODE"));
		Map<String,Object> cardInfo_map=new HashMap<String, Object>();
		if("0".equals(errorCode)){
			String resultContent=ConvertUtil.getString(result_card1.get("ResultContent"));
			List<Map<String,Object>> cardInfo_list=CherryUtil.json2ArryList(CherryAESCoder.decrypt(resultContent, thirdPartyConfig.getDynamicAESKey(SavingscardAppID, brandCode)));
			if(null != cardInfo_list && !cardInfo_list.isEmpty()) {
				cardInfo_map=(Map<String,Object>)cardInfo_list.get(0);
				result_card1.put("ResultContent", cardInfo_map);
			} else {
				result_card1.put("ResultContent", null);
			}
			
		}
		return result_card1;
	}


	@Override
	public Map<String, Object> getRechargeDiscountList(
			Map<String, Object> map) throws Exception {
		String brandCode = ConvertUtil.getString(map.get("brandCode"));
		Map<String,Object> data=new HashMap<String, Object>();
		data.put("CardType", ConvertUtil.getString(map.get("cardType")));
		data.put("TradeType", "GetSavingsCardDiscount");
		MultivaluedMap<String, String> queryParams = new MultivaluedMapImpl();
		queryParams.add("brandCode", brandCode);
		queryParams.add("appID", SavingscardAppID + "_" + brandCode);
		queryParams.add("paramData", CherryAESCoder.encrypt(CherryUtil.map2Json(data), thirdPartyConfig.getDynamicAESKey(SavingscardAppID,brandCode)));
		WebResource wr= bINOLCM27_BL.getWebResource(SavingscardWebServiceUrl);
		String  result_card=wr.queryParams(queryParams).get(String.class);
		Map<String,Object> result_card1=ConvertUtil.json2Map(result_card);
		String errorCode=ConvertUtil.getString(result_card1.get("ERRORCODE"));
		if("0".equals(errorCode)){
			String resultContent=ConvertUtil.getString(result_card1.get("ResultContent"));
			List<Map<String,Object>> discount_list=CherryUtil.json2ArryList(CherryAESCoder.decrypt(resultContent, thirdPartyConfig.getDynamicAESKey(SavingscardAppID,brandCode)));
			result_card1.put("ResultContent", discount_list);
		}
		return result_card1;
	}


	@Override
	public Map<String, Object> savingsCardRecharge(Map<String, Object> map)
			throws Exception {
		String brandCode = ConvertUtil.getString(map.get("brandCode"));
		String cardType=ConvertUtil.getString(map.get("cardType"));
		Map<String,Object> data=new HashMap<String, Object>();
		data.put("TradeType", "SavingsCardRecharge");
		data.put("CardCode", ConvertUtil.getString(map.get("cardCode")));
		data.put("BusinessType", ConvertUtil.getString(map.get("cardType")));
		data.put("RechargeValue", ConvertUtil.getString(map.get("camount")));
		data.put("GiveAmount", ConvertUtil.getString(map.get("giftAmount")));
		data.put("CounterCode", ConvertUtil.getString(map.get("counterCode")));
		data.put("EmployeeCode", ConvertUtil.getString(map.get("employeeCode")));
		data.put("Memo", ConvertUtil.getString(map.get("memo")));
		if("2".equals(cardType) || "3".equals(cardType)){
			data.put("ServiceDetail", map.get("service_list"));
		}
		MultivaluedMap<String, String> queryParams = new MultivaluedMapImpl();
		queryParams.add("brandCode",  brandCode);
		queryParams.add("appID", SavingscardAppID + "_" + brandCode);
		queryParams.add("paramData", CherryAESCoder.encrypt(CherryUtil.map2Json(data), thirdPartyConfig.getDynamicAESKey(SavingscardAppID,brandCode)));
		WebResource wr= bINOLCM27_BL.getWebResource(SavingscardWebServiceUrl);
		String  result_card=wr.queryParams(queryParams).get(String.class);
		Map<String,Object> result_card1=ConvertUtil.json2Map(result_card);
			if("0".equals(ConvertUtil.getString(result_card1.get("ERRORCODE")))){
				String resultContent=ConvertUtil.getString(result_card1.get("ResultContent"));
				String ERRORCODE=ConvertUtil.getString(result_card1.get("ERRORCODE"));
				Map<String,Object> result_list=CherryUtil.json2Map(CherryAESCoder.decrypt(resultContent, thirdPartyConfig.getDynamicAESKey(SavingscardAppID,brandCode)));
				result_list.put("ERRORCODE", ERRORCODE);
				return result_list;
			}
			return result_card1;
	}




}
