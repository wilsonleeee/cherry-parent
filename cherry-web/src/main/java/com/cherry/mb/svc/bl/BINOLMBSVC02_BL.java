package com.cherry.mb.svc.bl;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.ws.rs.core.MultivaluedMap;

import com.cherry.cm.cmbussiness.bl.BINOLCM27_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM37_BL;
import com.cherry.cm.core.CherryAESCoder;
import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CodeTable;
import com.cherry.cm.core.PropertiesUtil;
import com.cherry.cm.core.ThirdPartyConfig;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.mb.svc.interfaces.BINOLMBSVC02_1_IF;
import com.cherry.mb.svc.interfaces.BINOLMBSVC02_IF;
import com.cherry.mb.svc.service.BINOLMBSVC02_Service;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.core.util.MultivaluedMapImpl;


public class BINOLMBSVC02_BL implements BINOLMBSVC02_IF {
	
	static{
		SavingscardWebServiceUrl = PropertiesUtil.pps.getProperty("SavingscardWebServiceUrl");
		SavingscardAppID =PropertiesUtil.pps.getProperty("SavingscardAppID");
	}
	@Resource
	private BINOLMBSVC02_Service binOLMBSVC02_Service;
	
	/** 导出共通BL **/
	@Resource
	private BINOLCM37_BL binOLCM37_BL;
	@Resource(name="thirdPartyConfig")
	private ThirdPartyConfig thirdPartyConfig;
	
	@Resource(name ="binOLMBSVC02_1_IF")  
	private BINOLMBSVC02_1_IF binOLMBSVC02_1_IF;
	
	@Resource
	private CodeTable code;
	
	private static String SavingscardWebServiceUrl;
	private static String SavingscardAppID;
	@Resource
	private BINOLCM27_BL bINOLCM27_BL;
	
	@Override
	public Map<String, Object> getCardCountInfo(Map<String, Object> map) {
		return binOLMBSVC02_Service.getCardCountInfo(map);
	}
	
	@Override
	public List<Map<String, Object>> getCardList(Map<String, Object> map) {
		List<Map<String, Object>> cardList = binOLMBSVC02_Service.getCardList(map);
		for(Map<String,Object> m:cardList){
			m.put("cardType", code.getVal("1336", m.get("cardType")));
			m.put("discountType", code.getVal("1337", m.get("discountType")));
			m.put("serviceType", code.getVal("1338", m.get("serviceType")));
			m.put("state", code.getVal("1339", m.get("state")));
			m.put("transactionType", code.getVal("1340", m.get("transactionType")));
		}
		return cardList;
	}
	
	@Override
	public int stopCard(Map<String, Object> map) {
		return binOLMBSVC02_Service.updateCardVaild(map);
	}
	
	@Override
	public List<Map<String, Object>> getDataList(Map<String, Object> map)
			throws Exception {
		return binOLMBSVC02_Service.getCardDetailList(map);
	}
	
	@Override
	public Map<String, Object> getCardExportParam(Map<String, Object> map) {
		Map<String, Object> exportMap = new HashMap<String, Object>();
		exportMap.put(CherryConstants.ORGANIZATIONINFOID, map.get(CherryConstants.ORGANIZATIONINFOID));
		exportMap.put(CherryConstants.BRANDINFOID, map.get(CherryConstants.BRANDINFOID));
		String language = (String)map.get(CherryConstants.SESSION_CHERRY_LANGUAGE);
		
		exportMap.put("conditionMap", map);
		
	    exportMap.put("sheetName", CherryUtil.getResourceValue("mb", "BINOLMBSVC02", language, "sheetName"));
	    
	    String conditionContent = this.getCardConditionStr(map);
		if(conditionContent != null && !"".equals(conditionContent)) {
			exportMap.put("header", conditionContent);
		}
	    
	    List<String[]> titleRowList = new ArrayList<String[]>();
	    titleRowList.add(new String[]{"cardCode", CherryUtil.getResourceValue("mb", "BINOLMBSVC02", language, "SVC02_cardNo"), "30", "", ""});
	    titleRowList.add(new String[]{"mobilePhone", CherryUtil.getResourceValue("mb", "BINOLMBSVC02", language, "SVC02_obligateMobile"), "20", "", ""});
	    titleRowList.add(new String[]{"amount", CherryUtil.getResourceValue("mb", "BINOLMBSVC02", language, "SVC02_amount"), "20", "float", ""});
	    titleRowList.add(new String[]{"depositAmount", CherryUtil.getResourceValue("mb", "BINOLMBSVC02", language, "SVC02_depositAmount"), "20", "float", ""});
	    titleRowList.add(new String[]{"totalAmount", CherryUtil.getResourceValue("mb", "BINOLMBSVC02", language, "SVC02_totalAmount"), "20", "float", ""});
	    titleRowList.add(new String[]{"lastDepositAmount", CherryUtil.getResourceValue("mb", "BINOLMBSVC02", language, "SVC02_lastDepositAmount"), "20", "float", ""});
	    titleRowList.add(new String[]{"lastDepositTime", CherryUtil.getResourceValue("mb", "BINOLMBSVC02", language, "SVC02_lastDepositTime"), "20", "", ""});
	    titleRowList.add(new String[]{"state", CherryUtil.getResourceValue("mb", "BINOLMBSVC02", language, "SVC02_cardState"), "20", "", "1339"});
	    titleRowList.add(new String[]{"departName", CherryUtil.getResourceValue("mb", "BINOLMBSVC02", language, "SVC02_departName"), "50", "", ""});
	    exportMap.put("titleRows", titleRowList.toArray(new String[][]{}));
	    
	    return exportMap;
	}
	
	private String getCardConditionStr(Map<String, Object> map) {
		StringBuffer condition = new StringBuffer();
		String language = (String)map.get(CherryConstants.SESSION_CHERRY_LANGUAGE);
		
		//储值卡号
		String cardCode = (String)map.get("cardCode");
		if(cardCode != null && !"".equals(cardCode)) {
			String paramName = CherryUtil.getResourceValue("mb", "BINOLMBSVC02", language, "SVC02_cardCode");
			String paramValue = cardCode;
			condition.append(paramName + "：" + paramValue + "\0\0\0\0\0");
		}
		
		// 预留手机号
		String mobilePhone = (String)map.get("mobilePhone");
		if(mobilePhone != null && !"".equals(mobilePhone)) {
			String paramName = CherryUtil.getResourceValue("mb", "BINOLMBSVC02", language, "SVC02_obligateMobile");
			String paramValue = mobilePhone;
			condition.append(paramName + "：" + paramValue + "\0\0\0\0\0");
		}
		
		// 储值卡状态
		String cardState = (String)map.get("cardState");
		if(cardState != null && !"".equals(cardState)) {
			String paramName = CherryUtil.getResourceValue("mb", "BINOLMBSVC02", language, "SVC02_cardState");
			String paramValue = cardState;
			condition.append(paramName + "：" + paramValue + "\0\0\0\0\0");
		}
	
		return condition.toString();
	}
	
	private String getSaleConditionStr(Map<String, Object> map) {
		StringBuffer condition = new StringBuffer();
		String language = (String)map.get(CherryConstants.SESSION_CHERRY_LANGUAGE);
		
		//储值卡号
		String cardCode = (String)map.get("cardCode");
		if(cardCode != null && !"".equals(cardCode)) {
			String paramName = CherryUtil.getResourceValue("mb", "BINOLMBSVC02", language, "SVC02_cardCode");
			String paramValue = cardCode;
			condition.append(paramName + "：" + paramValue + "\0\0\0\0\0");
		}
		
			
		//部门ID
		String departId = (String)map.get("departId");
		if(departId != null && !"".equals(departId)) {
			String paramName = CherryUtil.getResourceValue("mb", "BINOLMBSVC02", language, "SVC02_departId");
			String paramValue = departId;
			condition.append(paramName + "：" + paramValue + "\0\0\0\0\0");
		}
		
		
		//交易类型
		String transactionType=(String)map.get("transactionType");
		if(transactionType != null && !"".equals(transactionType)) {
			String paramName = CherryUtil.getResourceValue("mb", "BINOLMBSVC02", language, "SVC02_transactionType");
			String paramValue = transactionType;
			condition.append(paramName + "：" + paramValue + "\0\0\0\0\0");
		}
		
		//交易起始时间
		String fromDate=(String)map.get("fromDate");
		if(fromDate != null && !"".equals(fromDate)) {
			String paramName = CherryUtil.getResourceValue("mb", "BINOLMBSVC02", language, "SVC02_transactionFromDate");
			String paramValue = fromDate;
			condition.append(paramName + "：" + paramValue + "\0\0\0\0\0");
		}
			
		//交易截至时间
		String toDate=(String)map.get("toDate");
		if(toDate != null && !"".equals(toDate)) {
			String paramName = CherryUtil.getResourceValue("mb", "BINOLMBSVC02", language, "SVC02_transactionToDate");
			String paramValue = toDate;
			condition.append(paramName + "：" + paramValue + "\0\0\0\0\0");
		}
		
		return condition.toString();
	}
	
	@Override
	public String cardExportCSV(Map<String, Object> map) throws Exception {
	
		// 获取导出参数
		Map<String, Object> exportMap = this.getCardExportParam(map);
	    
		String language = (String)map.get(CherryConstants.SESSION_CHERRY_LANGUAGE);
		
		String tempFilePath = PropertiesUtil.pps.getProperty("tempFilePath");
	    String sessionId = (String)map.get("sessionId");
	    // 下载文件临时目录
	    tempFilePath = tempFilePath + File.separator + sessionId;
	    exportMap.put("tempFilePath", tempFilePath);
	    
	    // 下载文件名
	    String downloadFileName = CherryUtil.getResourceValue("mb", "BINOLMBSVC02", language, "downloadFileName");
	    exportMap.put("tempFileName", downloadFileName);
	    
	    exportMap.put("charset", map.get("charset"));
	    
	    // 导出CSV处理
	    boolean result = binOLCM37_BL.exportCSV(exportMap, this);
	    if(result) {
	    	// 压缩包名
	    	String zipName = downloadFileName+".zip";
	    	// 压缩文件处理
	    	result = binOLCM37_BL.fileCompression(new File(tempFilePath+File.separator+downloadFileName+".csv"), zipName);
	    	if(result) {
	    		return tempFilePath+File.separator+zipName;
	    	}
	    }
	    return null;
	}
	
	@Override
	public Map<String, Object> getSaleExportParam(Map<String, Object> map) {
		Map<String, Object> exportMap = new HashMap<String, Object>();
		exportMap.put(CherryConstants.ORGANIZATIONINFOID, map.get(CherryConstants.ORGANIZATIONINFOID));
		exportMap.put(CherryConstants.BRANDINFOID, map.get(CherryConstants.BRANDINFOID));
		String language = (String)map.get(CherryConstants.SESSION_CHERRY_LANGUAGE);
		
		exportMap.put("conditionMap", map);
		
	    exportMap.put("sheetName", CherryUtil.getResourceValue("mb", "BINOLMBSVC02", language, "saleSheetName"));
	    
	    String conditionContent = this.getSaleConditionStr(map);
		if(conditionContent != null && !"".equals(conditionContent)) {
			exportMap.put("header", conditionContent);
		}
	    
	    List<String[]> titleRowList = new ArrayList<String[]>();
	    titleRowList.add(new String[]{"cardCode", CherryUtil.getResourceValue("mb", "BINOLMBSVC02", language, "SVC02_cardNo"), "30", "", ""});
	    titleRowList.add(new String[]{"cardType", CherryUtil.getResourceValue("mb", "BINOLMBSVC02", language, "SVC02_cardType"), "20", "", "1350"});
	    titleRowList.add(new String[]{"departName", CherryUtil.getResourceValue("mb", "BINOLMBSVC02", language, "SVC02_departName"), "20", "", ""});
	    titleRowList.add(new String[]{"transactionTime", CherryUtil.getResourceValue("mb", "BINOLMBSVC02", language, "SVC02_transactionTime"), "20", "", ""});
	    titleRowList.add(new String[]{"billCode", CherryUtil.getResourceValue("mb", "BINOLMBSVC02", language, "SVC02_billCode"), "20", "", ""});
	    titleRowList.add(new String[]{"relevantCode", CherryUtil.getResourceValue("mb", "BINOLMBSVC02", language, "SVC02_relevantCode"), "20", "", ""});
	    titleRowList.add(new String[]{"transactionType", CherryUtil.getResourceValue("mb", "BINOLMBSVC02", language, "SVC02_transactionType"), "20", "", "1340"});
	    titleRowList.add(new String[]{"amount", CherryUtil.getResourceValue("mb", "BINOLMBSVC02", language, "SVC02_transactionAmount"), "20", "float", ""});
	    titleRowList.add(new String[]{"giftAmount", CherryUtil.getResourceValue("mb", "BINOLMBSVC02", language, "SVC02_giftAmount"), "20", "float", ""});
	    titleRowList.add(new String[]{"totalAmount", CherryUtil.getResourceValue("mb", "BINOLMBSVC02", language, "SVC02_transactionTotalAmount"), "20", "float", ""});
	    titleRowList.add(new String[]{"serviceType", CherryUtil.getResourceValue("mb", "BINOLMBSVC02", language, "SVC02_serviceType"), "20", "", "1338"});
	    titleRowList.add(new String[]{"serviceQuantity", CherryUtil.getResourceValue("mb", "BINOLMBSVC02", language, "SVC02_serviceQuantity"), "20", "", ""});
	    titleRowList.add(new String[]{"discount", CherryUtil.getResourceValue("mb", "BINOLMBSVC02", language, "SVC02_discount"), "20", "float", ""});
	    titleRowList.add(new String[]{"validFlag", CherryUtil.getResourceValue("mb", "BINOLMBSVC02", language, "SVC02_validFlag"), "20", "", "1137"});
	    exportMap.put("titleRows", titleRowList.toArray(new String[][]{}));
	    
	    return exportMap;
	}
	
	@Override
	public String saleExportCSV(Map<String, Object> map) throws Exception {
	
		// 获取导出参数
		Map<String, Object> exportMap = this.getSaleExportParam(map);
	    
		String language = (String)map.get(CherryConstants.SESSION_CHERRY_LANGUAGE);
		
		String tempFilePath = PropertiesUtil.pps.getProperty("tempFilePath");
	    String sessionId = (String)map.get("sessionId");
	    // 下载文件临时目录
	    tempFilePath = tempFilePath + File.separator + sessionId;
	    exportMap.put("tempFilePath", tempFilePath);
	    
	    // 下载文件名
	    String downloadFileName = CherryUtil.getResourceValue("mb", "BINOLMBSVC02", language, "saleDownloadFileName");
	    exportMap.put("tempFileName", downloadFileName);
	    
	    exportMap.put("charset", map.get("charset"));
	    
	    // 导出CSV处理
	    boolean result = binOLCM37_BL.exportCSV(exportMap, binOLMBSVC02_1_IF);
	    if(result) {
	    	// 压缩包名
	    	String zipName = downloadFileName+".zip";
	    	// 压缩文件处理
	    	result = binOLCM37_BL.fileCompression(new File(tempFilePath+File.separator+downloadFileName+".csv"), zipName);
	    	if(result) {
	    		return tempFilePath+File.separator+zipName;
	    	}
	    }
	    return null;
	}
	
	@Override
	public Map getSaleCountInfo(Map<String, Object> map) {
		return binOLMBSVC02_Service.getSaleCountInfo(map);
	}
	
	@Override
	public List<Map<String, Object>> getSaleList(Map<String, Object> map) {
		List<Map<String, Object>> saleList = binOLMBSVC02_Service.getSaleList(map);
		for(Map<String,Object> m:saleList){
			m.put("cardType", code.getVal("1350", m.get("cardType")));
			m.put("discountType", code.getVal("1337", m.get("discountType")));
			m.put("serviceType", code.getVal("1338", m.get("serviceType")));
			m.put("transactionType", code.getVal("1340", m.get("transactionType")));
		}
		return saleList;
	}
	
	@Override
	public Map<String, Object> SavingsCardAddCard(Map<String,Object> card_map) throws Exception {
		String brandCode = ConvertUtil.getString(card_map.get("brandCode"));
		Map<String,Object> data=new HashMap<String, Object>();
		data.put("TradeType", "SavingsCardAddCard");
		data.put("CardList", card_map.get("CardList"));
		MultivaluedMap<String, String> queryParams = new MultivaluedMapImpl();
		queryParams.add("brandCode", brandCode);
		queryParams.add("appID", SavingscardAppID + "_" + brandCode);
		queryParams.add("paramData", CherryAESCoder.encrypt(CherryUtil.map2Json(data), thirdPartyConfig.getDynamicAESKey(SavingscardAppID,brandCode)));
		WebResource wr= bINOLCM27_BL.getWebResource(SavingscardWebServiceUrl);
		String  result_card=wr.queryParams(queryParams).get(String.class);
		Map<String,Object> result_card1=ConvertUtil.json2Map(result_card);
		String resultContent=ConvertUtil.getString(result_card1.get("ResultContent"));
		if(!"".equals(resultContent)){
			List<Map<String,Object>> ResultContent_list=CherryUtil.json2ArryList(CherryAESCoder.decrypt(resultContent, thirdPartyConfig.getDynamicAESKey(SavingscardAppID,brandCode)));
			result_card1.put("resultContent_list", ResultContent_list);
		}
		return result_card1;
	}
	
	@Override
	public List<Map<String, Object>> getServiceList(Map<String, Object> map) {
		List<Map<String,Object>> serviceList=binOLMBSVC02_Service.getServiceList(map);
		for(Map<String,Object> m:serviceList){
			m.put("cardType", code.getVal("1350", m.get("cardType")));
			m.put("discountType", code.getVal("1337", m.get("discountType")));
			m.put("serviceType", code.getVal("1338", m.get("serviceType")));
			m.put("transactionType", code.getVal("1340", m.get("transactionType")));
		}
		return serviceList;
	}
	
	@Override
	public Map<String, Object> getServiceCountInfo(Map<String, Object> map) {
		return binOLMBSVC02_Service.getServiceCountInfo(map);
	}
	
	public Map<String, Object> getSaleByBillCode(Map<String, Object> map) {
		Map<String, Object> sale = binOLMBSVC02_Service.getSaleByBillCode(map);
		return sale;
	}
	
	@Override
	public Map<String, Object> getSaleDetailCountInfo(Map<String, Object> map) {
		Map<String, Object> saleListCount = binOLMBSVC02_Service.getSaleDetailCountInfo(map);
		return saleListCount;
	}
	
	@Override
	public List<Map<String, Object>> getSaleByCardCode(Map<String, Object> map) {
		List<Map<String, Object>> saleList = binOLMBSVC02_Service.getSaleByCardCode(map);
		return saleList;
	}

	@Override
	public String getCouponCode(Map<String, Object> map) throws Exception{
		String brandCode = ConvertUtil.getString(map.get("brandCode"));
		Map<String,Object> data=new HashMap<String, Object>();
		data.put("TradeType", "SavingsCardValidate");
		data.put("CardCode", map.get("cardCode"));
		data.put("CounterCode", map.get("counterCode"));
		data.put("UsesType", 5);
		MultivaluedMap<String, String> queryParams = new MultivaluedMapImpl();
		queryParams.add("brandCode", brandCode);
		queryParams.add("appID", SavingscardAppID + "_" + brandCode);
		queryParams.add("paramData", CherryAESCoder.encrypt(CherryUtil.map2Json(data), thirdPartyConfig.getDynamicAESKey(SavingscardAppID,brandCode)));
		WebResource wr= bINOLCM27_BL.getWebResource(SavingscardWebServiceUrl);
		String  result_json = wr.queryParams(queryParams).get(String.class);
		Map<String,Object> result_map = ConvertUtil.json2Map(result_json);
		return getResultMapMessage(result_map);
	}

	@Override
	public Map<String,Object> getNewPassword(Map<String, Object> map) throws Exception {
		boolean retPassword = false;
		String brandCode = ConvertUtil.getString(map.get("brandCode"));
		Map<String,Object> data=new HashMap<String, Object>();
		data.put("TradeType", "SavingsCardPasswordChange");
		data.put("CardCode", map.get("cardCode"));
		//如果有验证码则为验证码+卡号验证
		if(!CherryChecker.isNull(map.get("verificationCode"))){
			data.put("VerificationType", 2);
			data.put("VerificationCode", map.get("verificationCode"));
		}
		//如果是密码验证则为旧密码+卡号
		else if(!CherryChecker.isNull(map.get("oldPassword"))){
			data.put("VerificationType", 1);
			data.put("Password", map.get("oldPassword"));
		}
		//判断是否有新密码
		if(CherryChecker.isNull(map.get("newPassword"))){
			//设置一个新密码
			String newPassword = CherryUtil.generateSalt(8);
			data.put("NewPassword", newPassword);
			retPassword =true;
		}else{
			data.put("NewPassword", map.get("newPassword"));
		}
		MultivaluedMap<String, String> queryParams = new MultivaluedMapImpl();
		queryParams.add("brandCode", brandCode);
		queryParams.add("appID", SavingscardAppID + "_" + brandCode);
		queryParams.add("paramData", CherryAESCoder.encrypt(CherryUtil.map2Json(data), thirdPartyConfig.getDynamicAESKey(SavingscardAppID,brandCode)));
		WebResource wr= bINOLCM27_BL.getWebResource(SavingscardWebServiceUrl);
		String  result_json=wr.queryParams(queryParams).get(String.class);
		Map<String,Object> result_map=ConvertUtil.json2Map(result_json);
		String resultStr = getResultMapMessage(result_map);
		if(retPassword){
			if("0".equals(resultStr)){
				String message = ConvertUtil.getString(data.get("NewPassword"));
				result_map.put("ERRORMSG",message);
			}
			
		}
		return result_map;
	}
	
	/**
	 * WebService 返回Map处理
	 * 返回0或者错误代码
	 * @param map
	 * @return
	 */
	private String getResultMapMessage(Map<String,Object> map){
		String errorCode = ConvertUtil.getString(map.get("ERRORCODE"));
		//0为发送成功，否则为失败
		if("0".equals(errorCode)){
			return "0";
		}else{
			String errorMessage = ConvertUtil.getString(map.get("ERRORMSG"));
			return errorMessage;
		}
	}
	
	@Override
	public int abandonCard(Map<String, Object> map) {
		return binOLMBSVC02_Service.updateCardStateAndVaild(map);
	}

}
