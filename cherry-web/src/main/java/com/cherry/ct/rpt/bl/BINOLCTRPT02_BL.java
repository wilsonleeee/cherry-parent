/*
 * @(#)BINOLCTRPT02_BL.java     1.0 2013/08/06
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
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import com.cherry.cm.cmbussiness.bl.BINOLCM37_BL;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.PropertiesUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.ct.rpt.interfaces.BINOLCTRPT02_IF;
import com.cherry.ct.rpt.service.BINOLCTRPT02_Service;

/**
 * 历史事件触发记录查询
 * 
 * @author ZhangGS
 * @version 1.0 2013.08.06
 */
public class BINOLCTRPT02_BL implements BINOLCTRPT02_IF{
	
	@Resource(name="binOLCTRPT02_Service")
	private BINOLCTRPT02_Service binOLCTRPT02_Service;
	
	@Resource(name="binOLCM37_BL")
	private BINOLCM37_BL binOLCM37_BL;
	
	@Override
	public int getEventRunDetailCount(Map<String, Object> map) {
		return binOLCTRPT02_Service.getEventRunDetailCount(map);
	}
	
	@Override
	@SuppressWarnings("rawtypes")
	public List getEventRunDetailList(Map<String, Object> map) throws Exception {
		// 获取沟通事件List
		List<Map<String, Object>> eventRunList = binOLCTRPT02_Service.getEventRunDetailList(map);
		return eventRunList;
	}

	@Override
	public List<Map<String, Object>> getDataList(Map<String, Object> map) throws Exception {
		// 获取导出数据
		String language = ConvertUtil.getString(map.get(CherryConstants.SESSION_LANGUAGE));
		List<Map<String, Object>> eventRunList = binOLCTRPT02_Service.getEventRunDetailList(map);
		for(Map<String, Object> eventRunMap : eventRunList){
			String runStatus = ConvertUtil.getString(eventRunMap.get("runStatus"));
			if("0".equals(runStatus)){
				eventRunMap.put("runStatus", binOLCM37_BL.getResourceValue("BINOLCTRPT01", language, "ctrpt.runStatus.success"));
			}else{
				eventRunMap.put("runStatus", binOLCM37_BL.getResourceValue("BINOLCTRPT01", language, "ctrpt.runStatus.fail"));
			}
		}
		return eventRunList;
	}

	@Override
	public Map<String, Object> getExportMap(Map<String, Object> map) {
		//Excel导出
		String language = ConvertUtil.getString(map.get(CherryConstants.SESSION_LANGUAGE));
		String[][] array = {
				{ "batchId", binOLCM37_BL.getResourceValue("BINOLCTRPT02", language, "ctrpt.batchId"), "20", "", "" },
				{ "eventType", binOLCM37_BL.getResourceValue("BINOLCTRPT02", language, "ctrpt.eventName"), "15", "", "1219" },
				{ "commType", binOLCM37_BL.getResourceValue("BINOLCTRPT02", language, "ctrpt.commType"), "18", "", "1203" },
				{ "runType", binOLCM37_BL.getResourceValue("BINOLCTRPT02", language, "ctrpt.runType"), "20", "", "1262" },
				{ "runBeginTime", binOLCM37_BL.getResourceValue("BINOLCTRPT02", language, "ctrpt.runBeginTime"), "15", "", "" },
				{ "sendMsgNum", binOLCM37_BL.getResourceValue("BINOLCTRPT02", language, "ctrpt.sendMsgNum"), "15", "", "" },
				{ "sendErrorNum", binOLCM37_BL.getResourceValue("BINOLCTRPT02", language, "ctrpt.sendMsgErrorNum"), "15", "", "" },
				{ "runStatus", binOLCM37_BL.getResourceValue("BINOLCTRPT02", language, "ctrpt.runStatus"), "15", "", "" },
				{ "runError", binOLCM37_BL.getResourceValue("BINOLCTRPT02", language, "ctrpt.runError"), "15", "", "" },
		};
		int dataLen = binOLCTRPT02_Service.getEventRunDetailCount(map);
		map.put("dataLen", dataLen);
		map.put("sheetName", binOLCM37_BL.getResourceValue("BINOLCTRPT02", language, "sheetName"));
		map.put("downloadFileName", binOLCM37_BL.getResourceValue("BINOLCTRPT02", language, "downloadFileName"));
		map.put("titleRows", array);
		map.put(CherryConstants.SORT_ID, "runBeginTime desc");
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
	
	@Override
	public Map<String, Object> getEventRunTotalInfo(Map<String, Object> map) {
		return binOLCTRPT02_Service.getEventRunTotalInfo(map);
	}
	
}
