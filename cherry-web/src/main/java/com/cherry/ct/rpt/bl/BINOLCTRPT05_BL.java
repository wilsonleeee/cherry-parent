/*
 * @(#)BINOLCTRPT05_BL.java     1.0 2013/10/23
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
import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.PropertiesUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.cm.util.DateUtil;
import com.cherry.ct.rpt.interfaces.BINOLCTRPT05_IF;
import com.cherry.ct.rpt.service.BINOLCTRPT05_Service;

/**
 * 沟通效果统计BL
 * 
 * @author ZhangLe
 * @version 1.0 2013.10.23
 */
public class BINOLCTRPT05_BL implements BINOLCTRPT05_IF{

	@Resource(name="binOLCTRPT05_Service")
	private BINOLCTRPT05_Service binOLCTRPT05_Service;
	
	@Resource(name="binOLCM37_BL")
	private BINOLCM37_BL binOLCM37_BL;
	
	/**
	 * 获取沟通短信发送日期
	 * @throws Exception 
	 */
	@Override
	public String getSendTime(Map<String, Object> map) throws Exception {
		String sendTime = binOLCTRPT05_Service.getSendTime(map);	
		if(CherryChecker.isNullOrEmpty(sendTime, true)){
			//若没有取到则默认为系统日期
			sendTime = binOLCTRPT05_Service.getDateYMD();
		}else{
			sendTime = DateUtil.coverTime2YMD(sendTime, "yyyy-MM-dd HH:mm:ss");
		}
		return sendTime;
	}

	/**
	 * 获取统计信息
	 */
	@Override
	public Map<String, Object> getAnalysisTotal(Map<String, Object> map) {
		return binOLCTRPT05_Service.getAnalysisTotal(map);
	}
	
	/**
	 * 获取沟通效果统计List
	 */
	@Override
	public List<Map<String, Object>> getAnalysisList(Map<String, Object> map) {
		return binOLCTRPT05_Service.getAnalysisList(map);
	}

	/**
	 * 获取沟通效果统计count
	 */
	@Override
	public int getAnalysisCount(Map<String, Object> map) {
		return binOLCTRPT05_Service.getAnalysisCount(map);
	}

	/**
	 * 获取会员参与明细List
	 */
	@Override
	public List<Map<String, Object>> getJoinDetailList(Map<String, Object> map) {
		return binOLCTRPT05_Service.getJoinDetailList(map);
	}

	/**
	 * 获取会员参与明细count
	 */
	@Override
	public int getJoinDetailCount(Map<String, Object> map) {
		return binOLCTRPT05_Service.getJoinDetailCount(map);
	}
	
	/**
	 * 获取购买明细List
	 */
	@Override
	public List<Map<String, Object>> getSaleDetailList(Map<String, Object> map) {
		return binOLCTRPT05_Service.getSaleDetailList(map);
	}

	/**
	 * 获取购买明细Count
	 */
	@Override
	public int getSaleDetailCount(Map<String, Object> map) {
		return binOLCTRPT05_Service.getSaleDetailCount(map);
	}
	
	/**
	 * 获取沟通效果统计导出Map
	 */
	@Override
	public Map<String, Object> getExportMap(Map<String, Object> map){
		//Excel导出
		String language = ConvertUtil.getString(map.get(CherryConstants.SESSION_LANGUAGE));
		String[][] array = {
				{ "regionName", binOLCM37_BL.getResourceValue("BINOLCTRPT05", language, "ctrpt.region"), "20", "", "" },
				{ "departName", binOLCM37_BL.getResourceValue("BINOLCTRPT05", language, "ctrpt.agency"), "20", "", "" },
				{ "counterCode", binOLCM37_BL.getResourceValue("BINOLCTRPT05", language, "ctrpt.counterCode"), "20", "", "" },
				{ "counterName", binOLCM37_BL.getResourceValue("BINOLCTRPT05", language, "ctrpt.counter"), "20", "", "" },
				{ "sendNumber", binOLCM37_BL.getResourceValue("BINOLCTRPT05", language, "ctrpt.sendNumber"), "15", "", "" },
				{ "saleNumber", binOLCM37_BL.getResourceValue("BINOLCTRPT05", language, "ctrpt.joinNumber"), "15", "", "" },
				{ "rate", binOLCM37_BL.getResourceValue("BINOLCTRPT05", language, "ctrpt.joinRate"), "15", "", "" },
				{ "billQuantity", binOLCM37_BL.getResourceValue("BINOLCTRPT05", language, "ctrpt.billQuantity"), "15", "", "" },
				{ "amount", binOLCM37_BL.getResourceValue("BINOLCTRPT05", language, "ctrpt.amount"), "15", "", "" },
				{ "quantity", binOLCM37_BL.getResourceValue("BINOLCTRPT05", language, "ctrpt.quantity"), "15", "", "" },
				
		};
		int dataLen = binOLCTRPT05_Service.getAnalysisCount(map);
		map.put("dataLen", dataLen);
		map.put("sheetName", binOLCM37_BL.getResourceValue("BINOLCTRPT05", language, "sheetName"));
		map.put("downloadFileName", binOLCM37_BL.getResourceValue("BINOLCTRPT05", language, "downloadFileName"));
		map.put("titleRows", array);
		map.put(CherryConstants.SORT_ID, "sendNumber desc");
		return map;
	}
	
	/**
	 * 沟通效果统计参与明细导出Map
	 */
	@Override
	public Map<String, Object> getJoinExportMap(Map<String, Object> map){
		String language = ConvertUtil.getString(map.get(CherryConstants.SESSION_LANGUAGE));
		String[][] array = {
				{ "counterCode", binOLCM37_BL.getResourceValue("BINOLCTRPT05", language, "ctrpt.counterCode"), "20", "", "" },
				{ "counterName", binOLCM37_BL.getResourceValue("BINOLCTRPT05", language, "ctrpt.counter"), "20", "", "" },
				{ "memCode", binOLCM37_BL.getResourceValue("BINOLCTRPT05", language, "ctrpt.memCode"), "15", "", "" },
				{ "memberName", binOLCM37_BL.getResourceValue("BINOLCTRPT05", language, "ctrpt.memName"), "15", "", "" },
				{ "mobilephone", binOLCM37_BL.getResourceValue("BINOLCTRPT05", language, "ctrpt.mobilePhone"), "15", "", "" },
				{ "billQuantity", binOLCM37_BL.getResourceValue("BINOLCTRPT05", language, "ctrpt.billQuantity"), "15", "", "" },
				{ "amount", binOLCM37_BL.getResourceValue("BINOLCTRPT05", language, "ctrpt.amount"), "15", "", "" },
				{ "quantity", binOLCM37_BL.getResourceValue("BINOLCTRPT05", language, "ctrpt.quantity"), "15", "", "" }	
		};
		int dataLen = binOLCTRPT05_Service.getJoinDetailCount(map);
		map.put("dataLen", dataLen);
		map.put("sheetName", binOLCM37_BL.getResourceValue("BINOLCTRPT05", language, "joinSheetName"));
		map.put("downloadFileName", binOLCM37_BL.getResourceValue("BINOLCTRPT05", language, "joinDownloadFileName"));
		map.put("titleRows", array);
		map.put(CherryConstants.SORT_ID, "counterCode desc");
		return map;
	}	
	
	/**
	 * 获取购买明细导出map
	 */
	@Override
	public Map<String, Object> getSaleExportMap(Map<String, Object> map) {
		String language = ConvertUtil.getString(map.get(CherryConstants.SESSION_LANGUAGE));
		String[][] array = {
				{ "counterCode", binOLCM37_BL.getResourceValue("BINOLCTRPT05", language, "ctrpt.counterCode"), "20", "", "" },
				{ "counterName", binOLCM37_BL.getResourceValue("BINOLCTRPT05", language, "ctrpt.counter"), "20", "", "" },
				{ "memCode", binOLCM37_BL.getResourceValue("BINOLCTRPT05", language, "ctrpt.memCode"), "15", "", "" },
				{ "memberName", binOLCM37_BL.getResourceValue("BINOLCTRPT05", language, "ctrpt.memName"), "15", "", "" },
				{ "mobilephone", binOLCM37_BL.getResourceValue("BINOLCTRPT05", language, "ctrpt.mobilePhone"), "15", "", "" },
				{ "billCode", binOLCM37_BL.getResourceValue("BINOLCTRPT05", language, "ctrpt.billCode"), "40", "", "" },
				{ "amount", binOLCM37_BL.getResourceValue("BINOLCTRPT05", language, "ctrpt.amount"), "15", "", "" },
				{ "quantity", binOLCM37_BL.getResourceValue("BINOLCTRPT05", language, "ctrpt.quantity"), "15", "", "" },	
				{ "saleDate", binOLCM37_BL.getResourceValue("BINOLCTRPT05", language, "ctrpt.saleDate"), "15", "", "" }
		};
		int dataLen = binOLCTRPT05_Service.getJoinDetailCount(map);
		map.put("dataLen", dataLen);
		map.put("sheetName", binOLCM37_BL.getResourceValue("BINOLCTRPT05", language, "saleSheetName"));
		map.put("downloadFileName", binOLCM37_BL.getResourceValue("BINOLCTRPT05", language, "saleDownloadFileName"));
		map.put("titleRows", array);
		map.put(CherryConstants.SORT_ID, "counterCode desc");
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

	/**
	 * 沟通统计导出数据
	 */
	@Override
	public List<Map<String, Object>> getDataList(Map<String, Object> map)
			throws Exception {
		String exportFormat = ConvertUtil.getString(map.get("exportFormat"));
		if("1".endsWith(exportFormat) || "2".equals(exportFormat)){
			//效果统计
			List<Map<String, Object>> analysisList = binOLCTRPT05_Service.getAnalysisList(map);
			for(Map<String, Object> fm : analysisList){
				String rate = ConvertUtil.obj2Price(fm.get("rate"))+"%";
				fm.put("rate", rate);
			}
			return analysisList;
		}else if("3".endsWith(exportFormat) || "4".equals(exportFormat)){
			//参与明细
			return binOLCTRPT05_Service.getJoinDetailList(map);
		}else{
			//沟通明细
			return binOLCTRPT05_Service.getSaleDetailList(map);
		}
	}
}