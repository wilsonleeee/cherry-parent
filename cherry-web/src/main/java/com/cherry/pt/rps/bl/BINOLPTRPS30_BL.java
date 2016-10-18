/*
 * @(#)BINOLPTRPS30_BL.java     1.0 2014/06/27
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
package com.cherry.pt.rps.bl;

import java.io.File;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.cmbussiness.bl.BINOLCM37_BL;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.PropertiesUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.pt.rps.interfaces.BINOLPTRPS30_IF;
import com.cherry.pt.rps.service.BINOLPTRPS30_Service;

/**
 * SPA报表BL
 * @author zhangle
 * @version 1.0 2014.06.27
 */
public class BINOLPTRPS30_BL implements BINOLPTRPS30_IF {
	
	@Resource(name="binOLPTRPS30_Service")
	private BINOLPTRPS30_Service binOLPTRPS30_Service;
	
	@Resource(name="binOLCM37_BL")
	private BINOLCM37_BL binOLCM37_BL;
	
	@Override
	public int getSPARptCount(Map<String, Object> map) {
		return binOLPTRPS30_Service.getSPARptCount(map);
	}

	@Override
	public List<Map<String, Object>> getSPARptList(Map<String, Object> map) {
		return binOLPTRPS30_Service.getSPARptList(map);
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
	 * 取得需要转成Excel文件的数据List
	 * @param map 查询条件
	 * @return 需要转成Excel文件的数据List
	 */
	@Override
	public List<Map<String, Object>> getDataList(Map<String, Object> map)
			throws Exception {
		return this.getSPARptList(map);
	}
	
	/**
	 * 导出Map
	 */
	@Override
	public Map<String, Object> getExportMap(Map<String, Object> map){
		String language = ConvertUtil.getString(map.get(CherryConstants.SESSION_LANGUAGE));
		String[][] array = {
				{ "resellerCode", binOLCM37_BL.getResourceValue("BINOLPTRPS30", language, "PTRPS.resellerCode"), "20", "", "" },
				{ "resellerName", binOLCM37_BL.getResourceValue("BINOLPTRPS30", language, "PTRPS.resellerName"), "20", "", "" },
				{ "basCode", binOLCM37_BL.getResourceValue("BINOLPTRPS30", language, "PTRPS.basCode"), "20", "", "" },
				{ "basName", binOLCM37_BL.getResourceValue("BINOLPTRPS30", language, "PTRPS.basName"), "20", "", "" },
				{ "counterCode", binOLCM37_BL.getResourceValue("BINOLPTRPS30", language, "PTRPS.counterCode"), "20", "", "" },
				{ "counterName", binOLCM37_BL.getResourceValue("BINOLPTRPS30", language, "PTRPS.counterName"), "20", "", "" },
				{ "saleAmount", binOLCM37_BL.getResourceValue("BINOLPTRPS30", language, "PTRPS.saleAmount"), "20", "float", "" },
				{ "saleCount", binOLCM37_BL.getResourceValue("BINOLPTRPS30", language, "PTRPS.saleCount"), "20", "int", "" },
				{ "saleQuantity", binOLCM37_BL.getResourceValue("BINOLPTRPS30", language, "PTRPS.saleQuantity"), "20", "int", "" },
				{ "saleQuantity1", binOLCM37_BL.getResourceValue("BINOLPTRPS30", language, "PTRPS.saleQuantity1"), "20", "int", "" },
				{ "memSaleAmount", binOLCM37_BL.getResourceValue("BINOLPTRPS30", language, "PTRPS.memSaleAmount"), "20", "float", "" },
				{ "notMemSaleAmout", binOLCM37_BL.getResourceValue("BINOLPTRPS30", language, "PTRPS.notMemSaleAmout"), "20", "float", "" },
				{ "promSaleAmount", binOLCM37_BL.getResourceValue("BINOLPTRPS30", language, "PTRPS.promSaleAmount"), "20", "float", "" },
				{ "inventoryAmount", binOLCM37_BL.getResourceValue("BINOLPTRPS30", language, "PTRPS.inventoryAmount"), "20", "float", "" }
		};
		int dataLen = this.getSPARptCount(map);
		map.put("dataLen", dataLen);
		map.put("sheetName", binOLCM37_BL.getResourceValue("BINOLPTRPS30", language, "sheetName"));
		map.put("downloadFileName", binOLCM37_BL.getResourceValue("BINOLPTRPS30", language, "downloadFileName"));
		map.put("titleRows", array);
		map.put(CherryConstants.SORT_ID, "counterCode desc");	
		return map;
	}

	@Override
	public String getBussnissDate(Map<String, Object> map) {
		return binOLPTRPS30_Service.getBusDate(map);
	}
}
