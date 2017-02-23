package com.cherry.mb.svc.bl;

import com.cherry.cm.cmbussiness.bl.BINOLCM27_BL;
import com.cherry.cm.core.*;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.mb.svc.interfaces.BINOLMBSVC03_IF;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.core.util.MultivaluedMapImpl;

import javax.annotation.Resource;
import javax.ws.rs.core.MultivaluedMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BINOLMBSVC03_BL implements BINOLMBSVC03_IF{

	@Resource
	private BINOLCM27_BL bINOLCM27_BL;
	
	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> getCard(Map<String, Object> map) throws Exception {
		String brandCode = ConvertUtil.getString(map.get("brandCode"));
		Map<String,Object> data=new HashMap<String, Object>();
		data.put("CardCode", ConvertUtil.getString(map.get("cardCode")));
		// 柜台号
		data.put("CounterCode", ConvertUtil.getString(map.get("counterCode")));
		data.put("TradeType", "GetSavingsCardInfo");

		WebserviceConfigDTO wsconfigDTO = SystemConfigManager.getWebserviceConfigDTO(brandCode,"pekonws");
		String  result_card=accessSavingscardWS(brandCode,data,wsconfigDTO);

		Map<String,Object> result_card1=ConvertUtil.json2Map(result_card);
		String errorCode=ConvertUtil.getString(result_card1.get("ERRORCODE"));
		Map<String,Object> cardInfo_map=new HashMap<String, Object>();
		if("0".equals(errorCode)){
			String resultContent=ConvertUtil.getString(result_card1.get("ResultContent"));
			List<Map<String,Object>> cardInfo_list=CherryUtil.json2ArryList(CherryAESCoder.decrypt(resultContent, wsconfigDTO.getSecretKey()));
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

		WebserviceConfigDTO wsconfigDTO = SystemConfigManager.getWebserviceConfigDTO(brandCode,"pekonws");
		String  result_card=accessSavingscardWS(brandCode,data,wsconfigDTO);

		Map<String,Object> result_card1=ConvertUtil.json2Map(result_card);
		String errorCode=ConvertUtil.getString(result_card1.get("ERRORCODE"));
		if("0".equals(errorCode)){
			String resultContent=ConvertUtil.getString(result_card1.get("ResultContent"));
			List<Map<String,Object>> discount_list=CherryUtil.json2ArryList(CherryAESCoder.decrypt(resultContent, wsconfigDTO.getSecretKey()));
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

		WebserviceConfigDTO wsconfigDTO = SystemConfigManager.getWebserviceConfigDTO(brandCode,"pekonws");
		String  result_card=accessSavingscardWS(brandCode,data,wsconfigDTO);

		Map<String,Object> result_card1=ConvertUtil.json2Map(result_card);
			if("0".equals(ConvertUtil.getString(result_card1.get("ERRORCODE")))){
				String resultContent=ConvertUtil.getString(result_card1.get("ResultContent"));
				String ERRORCODE=ConvertUtil.getString(result_card1.get("ERRORCODE"));
				Map<String,Object> result_list=CherryUtil.json2Map(CherryAESCoder.decrypt(resultContent, wsconfigDTO.getSecretKey()));
				result_list.put("ERRORCODE", ERRORCODE);
				return result_list;
			}
			return result_card1;
	}


	private String accessSavingscardWS(String brandCode,Map<String,Object> data,WebserviceConfigDTO wsconfigDTO) throws Exception{
		MultivaluedMap<String, String> queryParams = new MultivaluedMapImpl();
		queryParams.add("brandCode", brandCode);
		queryParams.add("appID", wsconfigDTO.getAppID());
		queryParams.add("paramData", CherryAESCoder.encrypt(CherryUtil.map2Json(data), wsconfigDTO.getSecretKey()));
		WebResource wr= bINOLCM27_BL.getWebResource(wsconfigDTO.getWebserviceURL());
		String  result=wr.queryParams(queryParams).get(String.class);
		return result;
	}

}
