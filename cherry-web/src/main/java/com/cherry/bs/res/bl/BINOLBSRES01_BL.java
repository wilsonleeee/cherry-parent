/*
 * @(#)BINOLBSRES01_BL.java     1.0 2014/10/29
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
package com.cherry.bs.res.bl;

import java.io.File;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.bs.res.interfaces.BINOLBSRES01_IF;
import com.cherry.bs.res.service.BINOLBSRES01_Service;
import com.cherry.cm.cmbussiness.bl.BINOLCM37_BL;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.cm.core.PropertiesUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.mo.common.interfaces.BINOLMOCOM01_IF;


/**
 * 经销商一览
 * @author hujh
 * @version 1.0 2014/11/11
 */
public class BINOLBSRES01_BL implements BINOLBSRES01_IF{
	
	@Resource(name="binOLMOCOM01_BL")
	private BINOLMOCOM01_IF binOLMOCOM01_BL;
	
	@Resource(name="binOLCM37_BL")
	private BINOLCM37_BL binOLCM37_BL;
	
	@Resource(name="binOLBSRES01_Service")
	private BINOLBSRES01_Service binOLBSRES01_Service;

	@Override
	public int getResCount(Map<String, Object> map) {
		return binOLBSRES01_Service.getResCount(map);
	}

	@Override
	public List<Map<String, Object>> getResList(Map<String, Object> map) {
		return binOLBSRES01_Service.getResList(map);
	}
	@Override
	public int tran_enableRes(Map<String, Object> map) throws CherryException {
		return binOLBSRES01_Service.enableRes(map);
	}

	@Override
	public int tran_disableRes(Map<String, Object> map) throws CherryException {
		return binOLBSRES01_Service.disableRes(map);
	}
	/**
	 * 导出Excel
	 */
	@Override
	public Map<String, Object> getExportMap(Map<String, Object> map){
		String language = ConvertUtil.getString(map.get(CherryConstants.SESSION_LANGUAGE));
		String[][] array = {				
				{ "resellerCode", binOLCM37_BL.getResourceValue("BINOLBSRES01", language, "RES01_resellerCode"), "20", "", "" },
				{ "resellerName", binOLCM37_BL.getResourceValue("BINOLBSRES01", language, "RES01_resellerName"), "20", "", "" },
				{ "resellerNameShort", binOLCM37_BL.getResourceValue("BINOLBSRES01", language, "RES01_resellerNameShort"), "20", "", "" },
				{ "type", binOLCM37_BL.getResourceValue("BINOLBSRES01", language, "RES01_type"), "20", "", "1314" },
				{ "levelCode", binOLCM37_BL.getResourceValue("BINOLBSRES01", language, "RES01_levelCode"), "20", "", "1315" },
				{ "parentResellerCode", binOLCM37_BL.getResourceValue("BINOLBSRES01", language, "RES01_parentResellerCode"), "20", "", "" },
				{ "parentResellerName", binOLCM37_BL.getResourceValue("BINOLBSRES01", language, "RES01_parentResellerName"), "20", "", "" },
				{ "regionName", binOLCM37_BL.getResourceValue("BINOLBSRES01", language, "RES01_regionName"), "20", "", "" },
				{ "provinceName", binOLCM37_BL.getResourceValue("BINOLBSRES01", language, "RES01_provinceName"), "20", "", "" },
				{ "cityName", binOLCM37_BL.getResourceValue("BINOLBSRES01", language, "RES01_cityName"), "20", "", "" },
				{ "legalPerson", binOLCM37_BL.getResourceValue("BINOLBSRES01", language, "RES01_legalPerson"), "20", "", "" },
				{ "telePhone", binOLCM37_BL.getResourceValue("BINOLBSRES01", language, "RES01_telephone"), "20", "", "" },
				{ "mobile", binOLCM37_BL.getResourceValue("BINOLBSRES01", language, "RES01_mobile"), "20", "", "" },				
		};
		int dataLen = binOLBSRES01_Service.getResCount(map);
		map.put("dataLen", dataLen);
		map.put("sheetName", binOLCM37_BL.getResourceValue("BINOLBSRES01", language, "sheetName"));
		map.put("downloadFileName", binOLCM37_BL.getResourceValue("BINOLBSRES01", language, "downloadFileName"));
		map.put("titleRows", array);
		map.put(CherryConstants.SORT_ID, "resellerCode desc");
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
	public List<Map<String, Object>> getDataList(Map<String, Object> map) throws Exception {
		List<Map<String, Object>> resInfoList = binOLBSRES01_Service.getResList(map);
		return resInfoList;
	}
	public BINOLCM37_BL getBinOLCM37_BL() {
		return binOLCM37_BL;
	}

	public void setBinOLCM37_BL(BINOLCM37_BL binOLCM37_BL) {
		this.binOLCM37_BL = binOLCM37_BL;
	}

	public BINOLMOCOM01_IF getBinOLMOCOM01_BL() {
		return binOLMOCOM01_BL;
	}

	public void setBinOLMOCOM01_BL(BINOLMOCOM01_IF binOLMOCOM01_BL) {
		this.binOLMOCOM01_BL = binOLMOCOM01_BL;
	}
	
	
}