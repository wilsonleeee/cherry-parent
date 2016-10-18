package com.cherry.wp.wr.mrp.bl;

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
import com.cherry.wp.wr.mrp.interfaces.BINOLWRMRP06_IF;
import com.cherry.wp.wr.mrp.service.BINOLWRMRP99_Service;

/**
 * 会员积分兑换报表BL
 * 
 * @author WangCT
 * @version 1.0 2014/11/17
 */
public class BINOLWRMRP06_BL implements BINOLWRMRP06_IF {
	
	/** 会员报表Service **/
	@Resource
	private BINOLWRMRP99_Service binOLWRMRP99_Service;
	
	/** 导出共通BL **/
	@Resource
	private BINOLCM37_BL binOLCM37_BL;

	@Override
	public int getPxCount(Map<String, Object> map) {
		return binOLWRMRP99_Service.getPxCount(map);
	}

	@Override
	public Map<String, Object> getPxCountInfo(Map<String, Object> map) {
		return binOLWRMRP99_Service.getPxCountInfo(map);
	}

	@Override
	public List<Map<String, Object>> getPxInfoList(Map<String, Object> map) {
		return binOLWRMRP99_Service.getPxInfoList(map);
	}
	
	@Override
	public List<Map<String, Object>> getDataList(Map<String, Object> map)
			throws Exception {
		return this.getPxInfoList(map);
	}

	@Override
	public Map<String, Object> getExportParam(Map<String, Object> map) {
		Map<String, Object> exportMap = new HashMap<String, Object>();
		exportMap.put(CherryConstants.ORGANIZATIONINFOID, map.get(CherryConstants.ORGANIZATIONINFOID));
		exportMap.put(CherryConstants.BRANDINFOID, map.get(CherryConstants.BRANDINFOID));
		String language = (String)map.get(CherryConstants.SESSION_CHERRY_LANGUAGE);
		
		exportMap.put("conditionMap", map);
		
        exportMap.put("sheetName", CherryUtil.getResourceValue("wp", "BINOLWRMRP06", language, "sheetName"));
        
        String conditionContent = this.getConditionStr(map);
		if(conditionContent != null && !"".equals(conditionContent)) {
			exportMap.put("header", conditionContent);
		}
        
        List<String[]> titleRowList = new ArrayList<String[]>();
        titleRowList.add(new String[]{"billCode", CherryUtil.getResourceValue("wp", "BINOLWRMRP06", language, "WRMRP06_billCode"), "30", "", ""});
        titleRowList.add(new String[]{"saleTime", CherryUtil.getResourceValue("wp", "BINOLWRMRP06", language, "WRMRP06_saleTime"), "20", "", ""});
        titleRowList.add(new String[]{"memCode", CherryUtil.getResourceValue("wp", "BINOLWRMRP06", language, "WRMRP06_memCode"), "20", "", ""});
        titleRowList.add(new String[]{"memName", CherryUtil.getResourceValue("wp", "BINOLWRMRP06", language, "WRMRP06_memName"), "20", "", ""});
        titleRowList.add(new String[]{"employeeName", CherryUtil.getResourceValue("wp", "BINOLWRMRP06", language, "WRMRP06_employeeId"), "20", "", ""});
        titleRowList.add(new String[]{"quantity", CherryUtil.getResourceValue("wp", "BINOLWRMRP06", language, "WRMRP06_quantity"), "15", "int", ""});
        titleRowList.add(new String[]{"amount", CherryUtil.getResourceValue("wp", "BINOLWRMRP06", language, "WRMRP06_amount"), "15", "float", ""});
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
        String downloadFileName = CherryUtil.getResourceValue("wp", "BINOLWRMRP06", language, "downloadFileName");
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
		
		// 日期
		String startDate = (String)map.get("startDate");
		String endDate = (String)map.get("endDate");
		String notLimit = CherryUtil.getResourceValue(null, language, "global.page.notLimit");
		StringBuffer dateCondition = new StringBuffer();
		if(startDate != null && !"".equals(startDate)) {
			if(endDate != null && !"".equals(endDate)) {
				dateCondition.append(startDate+"\0~\0"+endDate);
			} else {
				dateCondition.append(startDate+"\0~\0"+notLimit);
			}
		} else {
			if(endDate != null && !"".equals(endDate)) {
				dateCondition.append(notLimit+"\0~\0"+endDate);
			}
		}
		String dateStr = dateCondition.toString();
		if(dateStr != null && !"".equals(dateStr)) {
			String paramName = CherryUtil.getResourceValue("wp", "BINOLWRMRP06", language, "WRMRP06_date");
			String paramValue = dateStr;
			condition.append(paramName + "：" + paramValue + "\0\0\0\0\0");
		}
		
		String memCode = (String)map.get("memCode");
		if(memCode != null && !"".equals(memCode)) {
			String paramName = CherryUtil.getResourceValue("wp", "BINOLWRMRP06", language, "WRMRP06_memCode");
			String paramValue = memCode;
			condition.append(paramName + "：" + paramValue + "\0\0\0\0\0");
		}
		
		String employeeName = (String)map.get("employeeName");
		if(employeeName != null && !"".equals(employeeName)) {
			String paramName = CherryUtil.getResourceValue("wp", "BINOLWRMRP06", language, "WRMRP06_employeeId");
			String paramValue = employeeName;
			condition.append(paramName + "：" + paramValue + "\0\0\0\0\0");
		}
		
		String mobilePhone = (String)map.get("mobilePhone");
		if(mobilePhone != null && !"".equals(mobilePhone)) {
			String paramName = CherryUtil.getResourceValue("wp", "BINOLWRMRP06", language, "WRMRP06_mobilePhone");
			String paramValue = mobilePhone;
			condition.append(paramName + "：" + paramValue + "\0\0\0\0\0");
		}
		
		return condition.toString();
	}

}
