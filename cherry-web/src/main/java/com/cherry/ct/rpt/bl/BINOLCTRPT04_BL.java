/*
 * @(#)BINOLCTRPT03_BL.java     1.0 2013/08/06
 * 
 * Copyright (c) 2010 SHANGHAI BINGKUN DIGITAL TECHNOLOGY CO.,LTD
 * All rights reserved
 * 
 * This software is the confidential and proprietary information of 
 * SHANGHAI BINGKUN.("Confidential Information").  You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with SHANGHAI BINGKUN.
 */
package com.cherry.ct.rpt.bl;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import com.cherry.cm.cmbussiness.bl.BINOLCM37_BL;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.PropertiesUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.ct.rpt.interfaces.BINOLCTRPT04_IF;
import com.cherry.ct.rpt.service.BINOLCTRPT04_Service;

/**
 * 历史事件触发记录查询
 * 
 * @author ZhangGS
 * @version 1.0 2013.08.06
 */
public class BINOLCTRPT04_BL implements BINOLCTRPT04_IF{

	@Resource(name="binOLCTRPT04_Service")
	private BINOLCTRPT04_Service binOLCTRPT04_Service;
	
	@Resource(name="binOLCM37_BL")
	private BINOLCM37_BL binOLCM37_BL;
	
	
	@Override
	public int getErrorMsgDetailCount(Map<String, Object> map) {
		return binOLCTRPT04_Service.getErrorMsgDetailCount(map);
	}
	
	@Override
	@SuppressWarnings("rawtypes")
	public List getErrorMsgDetailList(Map<String, Object> map) throws Exception {
		// 获取沟通事件List
		List<Map<String, Object>> msgDetailList = binOLCTRPT04_Service.getErrorMsgDetailList(map);
		List<Map<String, Object>> newmsgDetailList = new ArrayList<Map<String, Object>>();
		for(Map<String,Object> msgDetailMap : msgDetailList){
			Map<String,Object> newmsgDetailMap = new HashMap<String, Object>();
			String message = ConvertUtil.getString(msgDetailMap.get("message"));
			String errorText = ConvertUtil.getString(msgDetailMap.get("errorText"));
			if(message.length() > 20){
				newmsgDetailMap.put("messageCut", message.substring(0, 20)+" ...");
			}else{
				newmsgDetailMap.put("messageCut", message);
			}
			if(errorText.length() > 20){
				newmsgDetailMap.put("errorTextCut", errorText.substring(0, 20)+" ...");
			}else{
				newmsgDetailMap.put("errorTextCut", errorText);
			}
			newmsgDetailMap.putAll(msgDetailMap);
			newmsgDetailList.add(newmsgDetailMap);
		}
		return newmsgDetailList;
	}

	@Override
	public List<Map<String, Object>> getDataList(Map<String, Object> map) throws Exception {
		// 获取导出数据List
		return binOLCTRPT04_Service.getErrorMsgDetailList(map);
	}

	@Override
	public Map<String, Object> getExportMap(Map<String, Object> map){
		//Excel导出
		String language = ConvertUtil.getString(map.get(CherryConstants.SESSION_LANGUAGE));
		String[][] array = {
				{ "memCode", binOLCM37_BL.getResourceValue("BINOLCTRPT04", language, "ctrpt.memCode"), "20", "", "" },
				{ "memName", binOLCM37_BL.getResourceValue("BINOLCTRPT04", language, "ctrpt.memName"), "20", "", "" },
				{ "mobilephone", binOLCM37_BL.getResourceValue("BINOLCTRPT04", language, "ctrpt.mobilePhone"), "15", "", "" },
				{ "message", binOLCM37_BL.getResourceValue("BINOLCTRPT04", language, "ctrpt.message"), "50", "", "" },
				{ "sendTime", binOLCM37_BL.getResourceValue("BINOLCTRPT04", language, "ctrpt.sendTime"), "25", "", "" },
				{ "commType", binOLCM37_BL.getResourceValue("BINOLCTRPT04", language, "ctrpt.messageType"), "10", "", "1203" },
				{ "errorType", binOLCM37_BL.getResourceValue("BINOLCTRPT04", language, "ctrpt.errorType"), "20", "", "1273" },
				{ "errorText", binOLCM37_BL.getResourceValue("BINOLCTRPT04", language, "ctrpt.errorMsg"), "50", "", "" },
				
		};
		int dataLen = binOLCTRPT04_Service.getErrorMsgDetailCount(map);
		map.put("dataLen", dataLen);
		map.put("sheetName", binOLCM37_BL.getResourceValue("BINOLCTRPT04", language, "sheetName"));
		map.put("downloadFileName", binOLCM37_BL.getResourceValue("BINOLCTRPT04", language, "downloadFileName"));
		map.put("titleRows", array);
		map.put(CherryConstants.SORT_ID, "sendTime desc");
		return map;
	}

	/**
     * 导出CSV处理
     */
	@Override
	public String export(Map<String, Object> map) throws Exception {
		
		// 获取导出参数
		Map<String, Object> exportMap = map;
    	String tempFilePath = PropertiesUtil.pps.getProperty("tempFilePath");
        String sessionId = (String)map.get("sessionId");
        // 下载文件临时目录
        tempFilePath = tempFilePath + File.separator + sessionId;
        exportMap.put("tempFilePath", tempFilePath);
        // 下载文件名
        String downloadFileName =  ConvertUtil.getString(exportMap.get("downloadFileName"));
        exportMap.put("tempFileName", downloadFileName);
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
}