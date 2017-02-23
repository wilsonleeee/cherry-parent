package com.cherry.wp.sal.bl;

import com.cherry.cm.cmbussiness.bl.BINOLCM27_BL;
import com.cherry.cm.core.*;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.wp.sal.action.BINOLWPSAL13_Action;
import com.cherry.wp.sal.service.BINOLWPSAL13_Service;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.core.util.MultivaluedMapImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import javax.ws.rs.core.MultivaluedMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BINOLWPSAL13_BL {

	private Logger logger = LoggerFactory.getLogger(BINOLWPSAL13_Action.class);
	/** WebService共通BL */
	@Resource
	private BINOLCM27_BL binOLCM27_BL;
	
	@Resource(name="binOLWPSAL13_Service")
	private BINOLWPSAL13_Service binOLWPSAL13_Service;
	/**查询调用Penkonws接口的密钥信息*/
	@Resource(name="thirdPartyConfig")
	private ThirdPartyConfig thirdPartyConfig;
	
	public List<Map<String,Object>> getCard(Map<String, Object> map) throws Exception {
		String brandCode = ConvertUtil.getString(map.get("brandCode"));
		Map<String,Object> data=new HashMap<String, Object>();
		data.put("CardCode", ConvertUtil.getString(map.get("cardCode")));
		data.put("MemberCode", ConvertUtil.getString(map.get("memberCode")));
		data.put("CounterCode", ConvertUtil.getString(map.get("counterCode")));
		data.put("TradeType", "GetSavingsCardInfo");
		WebserviceConfigDTO wsconfigDTO = SystemConfigManager.getWebserviceConfigDTO(brandCode,"pekonws");
		String  result_card="";
		try {
			result_card=accessSavingscardWS(brandCode,data,wsconfigDTO);
		} catch (Exception e) {
			logger.info("储值卡接口连接失败！");
			logger.info(e.getMessage(),e);
			List<Map<String,Object>> cardInfo = new ArrayList<Map<String,Object>>();
			Map<String,Object> ermap = new HashMap<String, Object>();
			ermap.put("ERROR", "ERROR");
			cardInfo.add(ermap);
			return cardInfo;
		}
		Map<String,Object> result_card1=ConvertUtil.json2Map(result_card);
		String errorCode=ConvertUtil.getString(result_card1.get("ERRORCODE"));
		if("0".equals(errorCode)){
			String resultContent=ConvertUtil.getString(result_card1.get("ResultContent"));
			List<Map<String,Object>> cardInfo=CherryUtil.json2ArryList(CherryAESCoder.decrypt(resultContent, wsconfigDTO.getSecretKey()));
			return cardInfo;
		}else{
			logger.info("储值卡查询出错,ERRORCODE="+result_card1.get("ERRORCODE"));
			return null;
		}
	}


	public List<Map<String, Object>> getRechargeDiscountList(
			Map<String, Object> map) throws Exception {
		String brandCode = ConvertUtil.getString(map.get("brandCode"));
		Map<String,Object> data=new HashMap<String, Object>();
//		data.put("CardType", ConvertUtil.getString(map.get("cardType")));
		data.put("BusinessType", ConvertUtil.getString(map.get("businessType")));
		data.put("TradeType", "GetSavingsCardDiscount");
		WebserviceConfigDTO wsconfigDTO = SystemConfigManager.getWebserviceConfigDTO(brandCode,"pekonws");
		String  result_card=accessSavingscardWS(brandCode,data,wsconfigDTO);
		Map<String,Object> result_card1=ConvertUtil.json2Map(result_card);
		String errorCode=ConvertUtil.getString(result_card1.get("ERRORCODE"));
		if("0".equals(errorCode)){
			String resultContent=ConvertUtil.getString(result_card1.get("ResultContent"));
			List<Map<String,Object>> discount_list=CherryUtil.json2ArryList(CherryAESCoder.decrypt(resultContent, wsconfigDTO.getSecretKey()));
			return discount_list;
		}else{
			logger.info("储值卡查询出错,ERRORCODE="+result_card1.get("ERRORCODE"));
			return null;
		}
	}


	public Map<String, Object> savingsCardRecharge(Map<String, Object> map)
			throws Exception {
		String brandCode = ConvertUtil.getString(map.get("brandCode"));
		String businessType=ConvertUtil.getString(map.get("BusinessType"));
		Map<String,Object> data=new HashMap<String, Object>();
		data.put("TradeType", "SavingsCardRecharge");
		data.put("CardCode", ConvertUtil.getString(map.get("CardCode")));
		data.put("GiveAmount", ConvertUtil.getString(map.get("GiveAmount")));
		data.put("RechargeValue", ConvertUtil.getString(map.get("RechargeValue")));
		data.put("GiveAmount", ConvertUtil.getString(map.get("GiveAmount")));
		data.put("CounterCode", ConvertUtil.getString(map.get("CounterCode")));
		data.put("EmployeeCode", ConvertUtil.getString(map.get("EmployeeCode")));
		data.put("Memo", ConvertUtil.getString(map.get("Memo")));
		data.put("BusinessType", businessType);
		if(!"1".equals(businessType) && !"".equals(businessType)){
			data.put("ServiceDetail", map.get("ServiceDetail"));
		}
		WebserviceConfigDTO wsconfigDTO = SystemConfigManager.getWebserviceConfigDTO(brandCode,"pekonws");
		String  result_card=accessSavingscardWS(brandCode,data,wsconfigDTO);
		Map<String,Object> result1=ConvertUtil.json2Map(result_card);
		return result1;
	}


	public Map<String, Object> createCard(Map<String, Object> map) throws Exception {
		String brandCode = ConvertUtil.getString(map.get("brandCode"));
		Map<String,Object> data=new HashMap<String, Object>();
		data.put("TradeType", "SavingsCardActivation");
		data.put("CardCode", ConvertUtil.getString(map.get("cardCode")));
//		data.put("CardType", ConvertUtil.getString(map.get("cardType")));
		data.put("BusinessType", ConvertUtil.getString(map.get("businessType")));
		data.put("MemberCode", ConvertUtil.getString(map.get("memCode")));
		data.put("MobilePhone", ConvertUtil.getString(map.get("mobilePhone")));
		data.put("Password", ConvertUtil.getString(map.get("password")));
		data.put("CounterCode", ConvertUtil.getString(map.get("counterCode")));
		data.put("EmployeeCode", ConvertUtil.getString(map.get("employeeCode")));
		data.put("GiveAmount", ConvertUtil.getString(map.get("GiveAmount")));
		data.put("RechargeValue", ConvertUtil.getString(map.get("RechargeValue")));
		data.put("Memo", ConvertUtil.getString(map.get("memo")));
		data.put("brandCode", brandCode);
		if(null!=map.get("ServiceDetail") && !"".equals(map.get("ServiceDetail").toString())){
			data.put("ServiceDetail", map.get("ServiceDetail"));
		}
		WebserviceConfigDTO wsconfigDTO = SystemConfigManager.getWebserviceConfigDTO(brandCode,"pekonws");
		String  result_card=accessSavingscardWS(brandCode,data,wsconfigDTO);
		Map<String,Object> result1=ConvertUtil.json2Map(result_card);
		return result1;
	}


	public Map<String, Object> transactionDetail(Map<String, Object> map) throws Exception {
		String brandCode = ConvertUtil.getString(map.get("brandCode"));
		Map<String,Object> data=new HashMap<String, Object>();
		data.put("TradeType", "GetCardTransactionInfo");
		data.put("CardCode",  ConvertUtil.getString(map.get("cardCode")));
		data.put("BillCode",  ConvertUtil.getString(map.get("billCode")));
		WebserviceConfigDTO wsconfigDTO = SystemConfigManager.getWebserviceConfigDTO(brandCode,"pekonws");
		String  result_card=accessSavingscardWS(brandCode,data,wsconfigDTO);
		Map<String,Object> result=ConvertUtil.json2Map(result_card);
		return result;
	}
	
	public Map<String, Object> revoke(Map<String, Object> map) throws Exception{
		String brandCode = ConvertUtil.getString(map.get("brandCode"));
		Map<String,Object> data=new HashMap<String, Object>();
		data.put("TradeType", "SavingsCardRechargeRevoke");
		data.put("OperateType", "RR");
		data.put("EmployeeCode", ConvertUtil.getString(map.get("employeeCode")));
		data.put("CounterCode", ConvertUtil.getString(map.get("counterCode")));
		data.put("CardCode",  ConvertUtil.getString(map.get("cardCode")));
		data.put("BillCode",  ConvertUtil.getString(map.get("billCode")));
		data.put("VerificationType",  ConvertUtil.getString(map.get("verificationType")));
		if(map.get("verificationType")!=null){
			String v = map.get("verificationType").toString();
			if("1".equals(v)){
				data.put("Password", map.get("password"));
			}else if ("2".equals(v)) {
				data.put("VerificationCode", map.get("verificationCode"));
			}
		}
		WebserviceConfigDTO wsconfigDTO = SystemConfigManager.getWebserviceConfigDTO(brandCode,"pekonws");
		String  result_card=accessSavingscardWS(brandCode,data,wsconfigDTO);
		Map<String,Object> result=ConvertUtil.json2Map(result_card);
		return result;
	}

	public Integer getCardId(Map<String, Object> map) {
		return binOLWPSAL13_Service.getCardId(map);
	}
	
	public void relation(Map<String, Object> map) {
		binOLWPSAL13_Service.relation(map);
	}

	public Map<String, Object> getMemberIdByCode(Map<String, Object> map) {
		return binOLWPSAL13_Service.getMemberIdByCode(map);
	}

	private String accessSavingscardWS(String brandCode,Map<String,Object> data,WebserviceConfigDTO wsconfigDTO) throws Exception{
		MultivaluedMap<String, String> queryParams = new MultivaluedMapImpl();
		queryParams.add("brandCode", brandCode);
		queryParams.add("appID", wsconfigDTO.getAppID());
		queryParams.add("paramData", CherryAESCoder.encrypt(CherryUtil.map2Json(data), wsconfigDTO.getSecretKey()));
		WebResource wr= binOLCM27_BL.getWebResource(wsconfigDTO.getWebserviceURL());
		String  result=wr.queryParams(queryParams).get(String.class);
		return result;
	}
}
