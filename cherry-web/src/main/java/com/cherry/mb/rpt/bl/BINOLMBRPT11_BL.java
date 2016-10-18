package com.cherry.mb.rpt.bl;

import java.io.File;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.cmbussiness.bl.BINOLCM37_BL;
import com.cherry.cm.cmbussiness.interfaces.BINOLCM37_IF;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.PropertiesUtil;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.mb.rpt.service.BINOLMBRPT11_Service;

/**
 * 扫粉关注报表
 * 
 * @author Hujh
 * @version 1.0 2015/11/11
 */
public class BINOLMBRPT11_BL implements BINOLCM37_IF{
	
	@Resource(name="binOLMBRPT11_Service")
	private BINOLMBRPT11_Service binOLMBRPT11_Service;
	
	@Resource(name="binOLCM37_BL")
	private BINOLCM37_BL binOLCM37_BL;
	
	public int getSubscribeCount(Map<String, Object> map) {
		
		return binOLMBRPT11_Service.getSubscribeCount(map);
	}


	public List<Map<String, Object>> getSubscribeList(Map<String, Object> map) {
		
		return binOLMBRPT11_Service.getSubscribeList(map);
	}

	/**
	 * 导出Excel
	 * @param searchMap
	 * @return
	 */
	public Map<String, Object> getExportMap(Map<String, Object> map) {
		String language = ConvertUtil.getString(map.get(CherryConstants.SESSION_LANGUAGE));
		String[][] array = {				
				{ "OpenID", binOLCM37_BL.getResourceValue("BINOLMBRPT11", language, "MBRPT11_openID"), "30", "", "" },
				{ "CounterCode", binOLCM37_BL.getResourceValue("BINOLMBRPT11", language, "MBRPT11_counterCode"), "15", "", "" },
				{ "CounterName", binOLCM37_BL.getResourceValue("BINOLMBRPT11", language, "MBRPT11_counterName"), "15", "", "" },
				{ "EmployeeCode", binOLCM37_BL.getResourceValue("BINOLMBRPT11", language, "MBRPT11_counterBASCode"), "15", "", "" },
				{ "EmployeeName", binOLCM37_BL.getResourceValue("BINOLMBRPT11", language, "MBRPT11_counterBASName"), "15", "", "" },
				{ "AgencyName", binOLCM37_BL.getResourceValue("BINOLMBRPT11", language, "MBRPT11_agency"), "15", "", "" },
				{ "SubscribeEventKey", binOLCM37_BL.getResourceValue("BINOLMBRPT11", language, "MBRPT11_subscribeEventKey"), "15", "", "" },
				{ "SubscribeTime", binOLCM37_BL.getResourceValue("BINOLMBRPT11", language, "MBRPT11_subscribeTime"), "25", "", "" },
				{ "QRCodeName", binOLCM37_BL.getResourceValue("BINOLMBRPT11", language, "MBRPT11_QRCodeName"), "20", "", "" },
				{ "QRCodeImageUrl", binOLCM37_BL.getResourceValue("BINOLMBRPT11", language, "MBRPT11_QRCodeImageUrl"), "30", "", "" },				
		};
		int dataLen = binOLMBRPT11_Service.getSubscribeCount(map);
		map.put("dataLen", dataLen);
		map.put("sheetName", binOLCM37_BL.getResourceValue("BINOLMBRPT11", language, "MBRPT11_sheetName"));
		map.put("downloadFileName", binOLCM37_BL.getResourceValue("BINOLMBRPT11", language, "MBRPT11_downloadFileName"));
		map.put("titleRows", array);
		map.put(CherryConstants.SORT_ID, "SubscribeTime desc");
		String conditionContent = this.getConditionStr(map);
		if(conditionContent != null && !"".equals(conditionContent)) {
			map.put("header", conditionContent);
		}
		return map;		
	}
	
	private String getConditionStr(Map<String, Object> map) {
		StringBuffer condition = new StringBuffer();
		String language = ConvertUtil.getString(map.get(CherryConstants.SESSION_LANGUAGE));
		String counterBAS = ConvertUtil.getString(map.get("counterBAS"));
		if(null != counterBAS && !"".equals(counterBAS)) {
			String paramName = CherryUtil.getResourceValue("mb", "BINOLMBRPT11", language, "MBRPT11_counterBAS");
			String paramValue = counterBAS;
			condition.append(paramName + "：" + paramValue + "\0\0\0\0\0");
		}
		String startDate = (String)map.get("StartDate");
		String endDate = (String)map.get("EndDate");
		String notLimit = CherryUtil.getResourceValue(null, language, "global.page.notLimit");
		StringBuffer saleDateCondition = new StringBuffer();
		if(startDate != null && !"".equals(startDate) && !"0000-00-00 00:00:00".equals(startDate)) {
			if(endDate != null && !"".equals(endDate) && !"9999-99-99 99:99:99".equals(endDate)) {
				saleDateCondition.append(startDate+"\0~\0"+endDate);
			} else {
				saleDateCondition.append(startDate+"\0~\0"+notLimit);
			}
		} else {
			if(endDate != null && !"".equals(endDate) && !"9999-99-99 99:99:99".equals(endDate)) {
				saleDateCondition.append(notLimit+"\0~\0"+endDate);
			} else {
				saleDateCondition.append(notLimit+"\0~\0"+notLimit);
			}
		}
		String paramValue = saleDateCondition.toString();
		if(paramValue != null && !"".equals(paramValue)) {
			String paramName = CherryUtil.getResourceValue("mb", "BINOLMBRPT11", language, "MBRPT11_dateRange");
			condition.append(paramName + "：" + paramValue + "\0\0\0\0\0");
		}
		return condition.toString();
	}


	/**
	 * 导出CSV
	 * 
	 */
	public String export(Map<String, Object> map) throws Exception {
		Map<String, Object> exportMap = map;
    	String tempFilePath = PropertiesUtil.pps.getProperty("tempFilePath");
        String sessionId = (String)map.get("sessionId");
        tempFilePath = tempFilePath + File.separator + sessionId;
        exportMap.put("tempFilePath", tempFilePath);
        String downloadFileName =  ConvertUtil.getString(exportMap.get("downloadFileName"));
        exportMap.put("tempFileName", downloadFileName);
        boolean result = binOLCM37_BL.exportCSV(exportMap, this);
        if(result) {
        	String zipName = downloadFileName+"Csv.zip";
        	result = binOLCM37_BL.fileCompression(new File(tempFilePath+File.separator + downloadFileName + ".csv"), zipName);
        	if(result) {
        		return tempFilePath+File.separator+zipName;
        	}
        }
        return null;
	}
	
	@Override
	public List<Map<String, Object>> getDataList(Map<String, Object> map) throws Exception {
		
		return binOLMBRPT11_Service.getSubscribeList(map);
	}

	/**
	 * 取得柜台主管名称
	 * @param map
	 * @return
	 */
	public String getCounterBAS(Map<String, Object> map) {
		List<Map<String, Object>> resultList = binOLMBRPT11_Service.getCounterBAS(map);
		return this.getString(map, resultList);
	}

	/**
	 * 将取得的柜台主管信息组装成指定格式用于显示
	 * @param map
	 * @param list
	 * @return
	 */
	private String getString(Map<String, Object> map, List<Map<String, Object>> list) {
		StringBuffer sb = new StringBuffer();
		for(int i = 0 ; i < list.size() ; i++){
			Map<String,Object> tempMap = list.get(i);
			sb.append((String)tempMap.get("name"));
			sb.append("|");
			sb.append((String)tempMap.get("code"));
			sb.append("|");
			sb.append(String.valueOf(tempMap.get("id")));
			if(i != list.size()){
				sb.append("\n");
			}
		}
		return sb.toString();
	}
	
}
