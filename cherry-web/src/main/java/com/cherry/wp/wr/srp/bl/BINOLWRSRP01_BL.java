package com.cherry.wp.wr.srp.bl;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.cmbussiness.bl.BINOLCM37_BL;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.PropertiesUtil;
import com.cherry.cm.util.CherryUtil;
import com.cherry.wp.wr.srp.interfaces.BINOLWRSRP01_IF;
import com.cherry.wp.wr.srp.service.BINOLWRSRP99_Service;

/**
 * 业务小结BL
 * 
 * @author WangCT
 * @version 1.0 2014/09/10
 */
public class BINOLWRSRP01_BL implements BINOLWRSRP01_IF {
	
	@Resource
	private BINOLWRSRP99_Service binOLWRSRP99_Service;
	
	/** 导出共通BL **/
	@Resource
	private BINOLCM37_BL binOLCM37_BL;

	@Override
	public int getBASaleCount(Map<String, Object> map) {
		return binOLWRSRP99_Service.getBASaleCount(map);
	}
	
	@Override
	public Map<String, Object> getBASaleCountInfo(Map<String, Object> map) {
		return binOLWRSRP99_Service.getBASaleCountInfo(map);
	}

	@Override
	public List<Map<String, Object>> getBASaleList(Map<String, Object> map) {
		
		List<Map<String, Object>> baSaleList = binOLWRSRP99_Service.getBASaleList(map);
		if(baSaleList != null && !baSaleList.isEmpty()) {
			for(int i = 0; i < baSaleList.size(); i++) {
				Map<String, Object> baSaleMap = baSaleList.get(i);
				BigDecimal amount = (BigDecimal)baSaleMap.get("amount");
				BigDecimal memAmount = (BigDecimal)baSaleMap.get("memAmount");
				BigDecimal quantity = (BigDecimal)baSaleMap.get("quantity");
				Integer saleCount = (Integer)baSaleMap.get("saleCount");
				if(saleCount != 0) {
					baSaleMap.put("joinRate", CherryUtil.div(quantity.doubleValue(), saleCount, 2));
				} else {
					baSaleMap.put("joinRate", "");
				}
				if(amount.doubleValue() != 0) {
					baSaleMap.put("memSaleRate", CherryUtil.percent(memAmount.doubleValue(), amount.doubleValue(), 2));
				} else {
					baSaleMap.put("memSaleRate", "");
				}
			}
		}
		return baSaleList;
	}

	@Override
	public List<Map<String, Object>> getDataList(Map<String, Object> map)
			throws Exception {
		return this.getBASaleList(map);
	}

	@Override
	public Map<String, Object> getExportParam(Map<String, Object> map) {
		
		Map<String, Object> exportMap = new HashMap<String, Object>();
		exportMap.put(CherryConstants.ORGANIZATIONINFOID, map.get(CherryConstants.ORGANIZATIONINFOID));
		exportMap.put(CherryConstants.BRANDINFOID, map.get(CherryConstants.BRANDINFOID));
		String language = (String)map.get(CherryConstants.SESSION_CHERRY_LANGUAGE);
		
		exportMap.put("conditionMap", map);
		
        exportMap.put("sheetName", CherryUtil.getResourceValue("wp", "BINOLWRSRP01", language, "sheetName"));
        
        String conditionContent = this.getConditionStr(map);
		if(conditionContent != null && !"".equals(conditionContent)) {
			exportMap.put("header", conditionContent);
		}
        
        List<String[]> titleRowList = new ArrayList<String[]>();
        titleRowList.add(new String[]{"employeeName", CherryUtil.getResourceValue("wp", "BINOLWRSRP01", language, "WRSRP01_employeeId"), "20", "", ""});
        titleRowList.add(new String[]{"quantity", CherryUtil.getResourceValue("wp", "BINOLWRSRP01", language, "WRSRP01_quantity"), "15", "int", ""});
        titleRowList.add(new String[]{"amount", CherryUtil.getResourceValue("wp", "BINOLWRSRP01", language, "WRSRP01_amount"), "15", "float", ""});
        titleRowList.add(new String[]{"joinRate", CherryUtil.getResourceValue("wp", "BINOLWRSRP01", language, "WRSRP01_joinRate"), "15", "", ""});
        titleRowList.add(new String[]{"memCount", CherryUtil.getResourceValue("wp", "BINOLWRSRP01", language, "WRSRP01_memCount"), "15", "int", ""});
        titleRowList.add(new String[]{"memSaleRate", CherryUtil.getResourceValue("wp", "BINOLWRSRP01", language, "WRSRP01_memSaleRate"), "15", "", ""});
        exportMap.put("titleRows", titleRowList.toArray(new String[][]{}));
        
        return exportMap;
	}

	@Override
	public String exportCSV(Map<String, Object> map) throws Exception {
		
		// 获取导出参数
		Map<String, Object> exportMap = this.getExportParam(map);
        
		String language = (String)map.get(CherryConstants.SESSION_CHERRY_LANGUAGE);
		
    	String tempFilePath = PropertiesUtil.pps.getProperty("tempFilePath");
        String sessionId = (String)map.get("sessionId");
        // 下载文件临时目录
        tempFilePath = tempFilePath + File.separator + sessionId;
        exportMap.put("tempFilePath", tempFilePath);
        
        // 下载文件名
        String downloadFileName = CherryUtil.getResourceValue("wp", "BINOLWRSRP01", language, "downloadFileName");
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
	
	private String getConditionStr(Map<String, Object> map) {
		StringBuffer condition = new StringBuffer();
		String language = (String)map.get(CherryConstants.SESSION_CHERRY_LANGUAGE);
		
		String saleDateStart = (String)map.get("saleDateStart");
		String saleDateEnd = (String)map.get("saleDateEnd");
		String notLimit = CherryUtil.getResourceValue(null, language, "global.page.notLimit");
		StringBuffer saleDateCondition = new StringBuffer();
		if(saleDateStart != null && !"".equals(saleDateStart)) {
			if(saleDateEnd != null && !"".equals(saleDateEnd)) {
				saleDateCondition.append(saleDateStart+"\0~\0"+saleDateEnd);
			} else {
				saleDateCondition.append(saleDateStart+"\0~\0"+notLimit);
			}
		} else {
			if(saleDateEnd != null && !"".equals(saleDateEnd)) {
				saleDateCondition.append(notLimit+"\0~\0"+saleDateEnd);
			}
		}
		String paramValue = saleDateCondition.toString();
		if(paramValue != null && !"".equals(paramValue)) {
			String paramName = CherryUtil.getResourceValue("wp", "BINOLWRSRP01", language, "WRSRP01_joinRate");
			condition.append(paramName + "：" + paramValue + "\0\0\0\0\0");
		}
		
		String employeeName = (String)map.get("employeeName");
		if(employeeName != null && !"".equals(employeeName)) {
			String paramName = CherryUtil.getResourceValue("wp", "BINOLWRSRP01", language, "WRSRP01_employeeId");
			paramValue = employeeName;
			condition.append(paramName + "：" + paramValue + "\0\0\0\0\0");
		}
		
		return condition.toString();
	}

}
