/*
 * @(#)BINOLCTRPT01_BL.java     1.0 2013/08/06
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
import com.cherry.ct.rpt.interfaces.BINOLCTRPT01_IF;
import com.cherry.ct.rpt.service.BINOLCTRPT01_Service;

/**
 * 历史沟通记录查询
 * 
 * @author ZhangGS
 * @version 1.0 2013.08.06
 */
public class BINOLCTRPT01_BL implements BINOLCTRPT01_IF{
	
	@Resource(name="binOLCTRPT01_Service")
	private BINOLCTRPT01_Service binOLCTRPT01_Service;
	
	@Resource(name="binOLCM37_BL")
	private BINOLCM37_BL binOLCM37_BL;
	
	@Override
	public int getPlanRunDetailCount(Map<String, Object> map) {
		// 获取沟通计划运行次数
		return binOLCTRPT01_Service.getPlanRunDetailCount(map);
	}
	
	@Override
	@SuppressWarnings("rawtypes")
	public List getPlanRunDetailList(Map<String, Object> map) throws Exception {
		// 获取沟通计划运行List
		List<Map<String, Object>> planRunList = binOLCTRPT01_Service.getPlanRunDetailList(map);
		return planRunList;		
	}

	/**
	 * 导出Map
	 */
	@Override
	public Map<String, Object> getExportMap(Map<String, Object> map){
		String language = ConvertUtil.getString(map.get(CherryConstants.SESSION_LANGUAGE));
		String[][] array = {
				{ "batchId", binOLCM37_BL.getResourceValue("BINOLCTRPT01", language, "ctrpt.batchId"), "20", "", "" },
				{ "planCode", binOLCM37_BL.getResourceValue("BINOLCTRPT01", language, "ctrpt.planCode"), "15", "", "" },
				{ "planName", binOLCM37_BL.getResourceValue("BINOLCTRPT01", language, "ctrpt.planName"), "25", "", "" },
				{ "activityCode", binOLCM37_BL.getResourceValue("BINOLCTRPT01", language, "ctrpt.activityCode"), "18", "", "" },
				{ "activityName", binOLCM37_BL.getResourceValue("BINOLCTRPT01", language, "ctrpt.activityName"), "20", "", "" },
				{ "communicationCode", binOLCM37_BL.getResourceValue("BINOLCTRPT01", language, "ctrpt.communicationCode"), "15", "", "" },
				{ "communicationName", binOLCM37_BL.getResourceValue("BINOLCTRPT01", language, "ctrpt.communicationName"), "15", "", "" },
				{ "commType", binOLCM37_BL.getResourceValue("BINOLCTRPT01", language, "ctrpt.commType"), "15", "", "1203" },
				{ "runType", binOLCM37_BL.getResourceValue("BINOLCTRPT01", language, "ctrpt.runType"), "15", "", "1262" },
				{ "runBeginTime", binOLCM37_BL.getResourceValue("BINOLCTRPT01", language, "ctrpt.runBeginTime"), "15", "", "" },
				{ "sendMsgNum", binOLCM37_BL.getResourceValue("BINOLCTRPT01", language, "ctrpt.sendMsgNum"), "15", "", "" },
				{ "sendErrorNum", binOLCM37_BL.getResourceValue("BINOLCTRPT01", language, "ctrpt.sendMsgErrorNum"), "15", "", "" },
				{ "runStatus", binOLCM37_BL.getResourceValue("BINOLCTRPT01", language, "ctrpt.runStatus"), "15", "", "" },
				{ "runError", binOLCM37_BL.getResourceValue("BINOLCTRPT01", language, "ctrpt.runError"), "15", "", "" },
		};
		int dataLen = binOLCTRPT01_Service.getPlanRunDetailCount(map);
		map.put("dataLen", dataLen);
		map.put("sheetName", binOLCM37_BL.getResourceValue("BINOLCTRPT01", language, "sheetName"));
		map.put("downloadFileName", binOLCM37_BL.getResourceValue("BINOLCTRPT01", language, "downloadFileName"));
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
        tempFilePath = tempFilePath + File.separator+ sessionId;
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
	public List<Map<String, Object>> getDataList(Map<String, Object> map) throws Exception {
		// 获取导出数据
		String language = ConvertUtil.getString(map.get(CherryConstants.SESSION_LANGUAGE));
		List<Map<String, Object>> planRunList = binOLCTRPT01_Service.getPlanRunDetailList(map);
		for(Map<String, Object> planRunMap : planRunList){
			String runStatus = ConvertUtil.getString(planRunMap.get("runStatus"));
			if("0".equals(runStatus)){
				planRunMap.put("runStatus", binOLCM37_BL.getResourceValue("BINOLCTRPT01", language, "ctrpt.runStatus.success"));
			}else{
				planRunMap.put("runStatus", binOLCM37_BL.getResourceValue("BINOLCTRPT01", language, "ctrpt.runStatus.fail"));
			}
		}
		return planRunList;
	}

	@Override
	public Map<String, Object> getPlanRunTotalInfo(Map<String, Object> map) {
		//获取沟通计划运行统计信息
		return binOLCTRPT01_Service.getPlanRunTotalInfo(map);
	}

}
