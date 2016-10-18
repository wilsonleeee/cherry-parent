package com.cherry.wp.wr.srp.bl;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.cmbussiness.bl.BINOLCM37_BL;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.PropertiesUtil;
import com.cherry.cm.util.CherryUtil;
import com.cherry.wp.wr.srp.interfaces.BINOLWRSRP08_IF;
import com.cherry.wp.wr.srp.service.BINOLWRSRP99_Service;

/**
 * 商品销售排行BL
 * 
 * @author songka
 * @version 1.0 2015/09/07
 */
public class BINOLWRSRP08_BL implements BINOLWRSRP08_IF {
	
	@Resource
	private BINOLWRSRP99_Service binOLWRSRP99_Service;
	
	/** 导出共通BL **/
	@Resource
	private BINOLCM37_BL binOLCM37_BL;

	@Override
	public Map<String, Object> getHeroCountInfo(Map<String, Object> map) {
		return binOLWRSRP99_Service.getHeroCountInfo(map);
	}

	@Override
	public List<Map<String, Object>> getDataList(Map<String, Object> map)
			throws Exception {
		return this.getHeroList(map);
	}
	
	@Override
	public List<Map<String, Object>> getHeroList(Map<String, Object> map) {
		List<Map<String, Object>> heroList = binOLWRSRP99_Service.getHeroList(map);
		return heroList;
	}
	
	@Override
	public Map<String, Object> getExportParam(Map<String, Object> map) {
		
		Map<String, Object> exportMap = new HashMap<String, Object>();
		exportMap.put(CherryConstants.ORGANIZATIONINFOID, map.get(CherryConstants.ORGANIZATIONINFOID));
		exportMap.put(CherryConstants.BRANDINFOID, map.get(CherryConstants.BRANDINFOID));
		String language = (String)map.get(CherryConstants.SESSION_CHERRY_LANGUAGE);
		
		exportMap.put("conditionMap", map);
		
        exportMap.put("sheetName", CherryUtil.getResourceValue("wp", "BINOLWRSRP08", language, "sheetName"));
        
        String conditionContent = this.getConditionStr(map);
		if(conditionContent != null && !"".equals(conditionContent)) {
			exportMap.put("header", conditionContent);
		}
        
        List<String[]> titleRowList = new ArrayList<String[]>();
        titleRowList.add(new String[]{"employeeName", CherryUtil.getResourceValue("wp", "BINOLWRSRP08", language, "WRSRP08_employeeName"), "20", "", ""});
        titleRowList.add(new String[]{"employeeID", CherryUtil.getResourceValue("wp", "BINOLWRSRP08", language, "WRSRP08_employeeID"), "20", "", ""});
        titleRowList.add(new String[]{"memCount", CherryUtil.getResourceValue("wp", "BINOLWRSRP08", language, "WRSRP08_memCount"), "20", "int", ""});
        titleRowList.add(new String[]{"quantity", CherryUtil.getResourceValue("wp", "BINOLWRSRP08", language, "WRSRP08_quantity"), "15", "int", ""});
        titleRowList.add(new String[]{"amount", CherryUtil.getResourceValue("wp", "BINOLWRSRP08", language, "WRSRP08_amount"), "15", "float", ""});
        titleRowList.add(new String[]{"ranking", CherryUtil.getResourceValue("wp", "BINOLWRSRP08", language, "WRSRP08_ranking"), "20", "", ""});
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
        String downloadFileName = CherryUtil.getResourceValue("wp", "BINOLWRSRP08", language, "downloadFileName");
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
		
		// 销售日期
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
			String paramName = CherryUtil.getResourceValue("wp", "BINOLWRSRP04", language, "WRSRP04_saleDate");
			condition.append(paramName + "：" + paramValue + "\0\0\0\0\0");
		}
		
		// 营业员
		String employeeName = (String)map.get("employeeName");
		if(employeeName != null && !"".equals(employeeName)) {
			String paramName = CherryUtil.getResourceValue("wp", "BINOLWRSRP04", language, "WRSRP04_employeeId");
			paramValue = employeeName;
			condition.append(paramName + "：" + paramValue + "\0\0\0\0\0");
		}
		
		return condition.toString();
	}

}
