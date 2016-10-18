/*
 * @(#)BINOLPTRPS29_BL.java     1.0 2014/06/24
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
import com.cherry.pt.rps.interfaces.BINOLPTRPS29_IF;
import com.cherry.pt.rps.service.BINOLPTRPS29_Service;

/**
 * BB柜台销售统计
 * @author zhangle
 * @version 1.0 2014.06.24
 */
public class BINOLPTRPS29_BL implements BINOLPTRPS29_IF {
	
	@Resource(name="binOLPTRPS29_Service")
	private BINOLPTRPS29_Service binOLPTRPS29_Service;
	
	@Resource(name="binOLCM37_BL")
	private BINOLCM37_BL binOLCM37_BL;

	@Override
	public int getBBCounterSaleRptCount(Map<String, Object> map) {
		return binOLPTRPS29_Service.getBBCounterSaleRptCount(map);
	}


	@Override
	public List<Map<String, Object>> getBBCounterSaleRptList(
			Map<String, Object> map) {
		return binOLPTRPS29_Service.getBBCounterSaleRptList(map);
	}

	/**
	 * 导出Map
	 */
	@Override
	public Map<String, Object> getExportMap(Map<String, Object> map){
		String exportFormat = ConvertUtil.getString(map.get("exportFormat")); 
		if("0".equals(exportFormat) || "1".equals(exportFormat)){
			this.getExportMainMap(map);
		}else{
			this.getExportDetailMap(map);
		}
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
	
	/**
	 * 取得需要转成Excel文件的数据List
	 * @param map 查询条件
	 * @return 需要转成Excel文件的数据List
	 */
	@Override
	public List<Map<String, Object>> getDataList(Map<String, Object> map)
			throws Exception {
		String exportFormat = ConvertUtil.getString(map.get("exportFormat")); 
		List<Map<String, Object>> dataList = null;
		if("0".equals(exportFormat) || "1".equals(exportFormat)){
			dataList = this.getBBCounterSaleRptList(map);
		}else{
			dataList = binOLPTRPS29_Service.getBBCounterSaleDetailRptList(map);
		}
		return dataList;
	}
	
	/**
	 * 获取BB柜台销售统计导出Map
	 * @param map
	 * @return
	 */
	private Map<String, Object> getExportMainMap(Map<String, Object> map){
		String language = ConvertUtil.getString(map.get(CherryConstants.SESSION_LANGUAGE));
		String[][] array = {
				{ "resellerCode", binOLCM37_BL.getResourceValue("BINOLPTRPS29", language, "PTRPS.resellerCode"), "20", "", "" },
				{ "resellerName", binOLCM37_BL.getResourceValue("BINOLPTRPS29", language, "PTRPS.resellerName"), "20", "", "" },
				{ "counterCode", binOLCM37_BL.getResourceValue("BINOLPTRPS29", language, "PTRPS.counterCode"), "20", "", "" },
				{ "counterName", binOLCM37_BL.getResourceValue("BINOLPTRPS29", language, "PTRPS.counterName"), "20", "", "" },
				{ "startTime", binOLCM37_BL.getResourceValue("BINOLPTRPS29", language, "PTRPS.startTime"), "20", "", "" },
				{ "endTime", binOLCM37_BL.getResourceValue("BINOLPTRPS29", language, "PTRPS.endTime"), "20", "", "" },
				{ "totalQuantity", binOLCM37_BL.getResourceValue("BINOLPTRPS29", language, "PTRPS.totalQuantity"), "20", "int", "" },
				{ "totalAmount", binOLCM37_BL.getResourceValue("BINOLPTRPS29", language, "PTRPS.totalAmount"), "20", "float", "" },
				{ "totalBillCount", binOLCM37_BL.getResourceValue("BINOLPTRPS29", language, "PTRPS.totalBillCount"), "20", "int", "" }
		};
		int dataLen = binOLPTRPS29_Service.getBBCounterSaleRptCount(map);
		map.put("dataLen", dataLen);
		map.put("sheetName", binOLCM37_BL.getResourceValue("BINOLPTRPS29", language, "sheetName"));
		map.put("downloadFileName", binOLCM37_BL.getResourceValue("BINOLPTRPS29", language, "downloadFileName"));
		map.put("titleRows", array);
		map.put(CherryConstants.SORT_ID, "startTime desc");	
		return map;
	}
	
	/**
	 * 获取BB柜台销售明细导出Map
	 * @param map
	 * @return
	 */
	private Map<String, Object> getExportDetailMap(Map<String, Object> map){
		String language = ConvertUtil.getString(map.get(CherryConstants.SESSION_LANGUAGE));
		String[][] array = {
				{ "billCode", binOLCM37_BL.getResourceValue("BINOLPTRPS29", language, "PTRPS.billCode"), "40", "", "" },
				{ "billCodepre", binOLCM37_BL.getResourceValue("BINOLPTRPS29", language, "PTRPS.billCodepre"), "40", "", "" },
				{ "resellerCode", binOLCM37_BL.getResourceValue("BINOLPTRPS29", language, "PTRPS.resellerCode"), "20", "", "" },
				{ "resellerName", binOLCM37_BL.getResourceValue("BINOLPTRPS29", language, "PTRPS.resellerName"), "20", "", "" },
				{ "counterCode", binOLCM37_BL.getResourceValue("BINOLPTRPS29", language, "PTRPS.counterCode"), "20", "", "" },
				{ "counterName", binOLCM37_BL.getResourceValue("BINOLPTRPS29", language, "PTRPS.counterName"), "20", "", "" },
				{ "baCode", binOLCM37_BL.getResourceValue("BINOLPTRPS29", language, "PTRPS.baCode"), "20", "", "" },
				{ "baName", binOLCM37_BL.getResourceValue("BINOLPTRPS29", language, "PTRPS.baName"), "20", "", "" },
				{ "memberCode", binOLCM37_BL.getResourceValue("BINOLPTRPS29", language, "PTRPS.memberCode"), "20", "", "" },
				{ "memberName", binOLCM37_BL.getResourceValue("BINOLPTRPS29", language, "PTRPS.memberName"), "20", "", "" },
				{ "unitCode", binOLCM37_BL.getResourceValue("BINOLPTRPS29", language, "PTRPS.unitCode"), "20", "", "" },
				{ "barCode", binOLCM37_BL.getResourceValue("BINOLPTRPS29", language, "PTRPS.barCode"), "20", "", "" },
				{ "productName", binOLCM37_BL.getResourceValue("BINOLPTRPS29", language, "PTRPS.productName"), "20", "", "" },
				{ "quantity", binOLCM37_BL.getResourceValue("BINOLPTRPS29", language, "PTRPS.quantity"), "15", "int", "" },
				{ "amount", binOLCM37_BL.getResourceValue("BINOLPTRPS29", language, "PTRPS.amount"), "15", "float", "" },
				{ "saleType", binOLCM37_BL.getResourceValue("BINOLPTRPS29", language, "PTRPS.saleType"), "15", "", "1055" },
				{ "prtType", binOLCM37_BL.getResourceValue("BINOLPTRPS29", language, "PTRPS.prtType"), "15", "", "1106" },
				{ "actCode", binOLCM37_BL.getResourceValue("BINOLPTRPS29", language, "PTRPS.actCode"), "20", "", "" },
				{ "actName", binOLCM37_BL.getResourceValue("BINOLPTRPS29", language, "PTRPS.actName"), "20", "", "" },
				{ "saleTime", binOLCM37_BL.getResourceValue("BINOLPTRPS29", language, "PTRPS.saleTime"), "20", "", "" },
		};
		int dataLen = binOLPTRPS29_Service.getBBCounterSaleDetailRptCount(map);
		map.put("dataLen", dataLen);
		map.put("sheetName", binOLCM37_BL.getResourceValue("BINOLPTRPS29", language, "sheetNameDetail"));
		map.put("downloadFileName", binOLCM37_BL.getResourceValue("BINOLPTRPS29", language, "downloadFileNameDetail"));
		map.put("titleRows", array);
		map.put(CherryConstants.SORT_ID, "counterCode desc");	
		return map;
	}


	@Override
	public int getBBCounterSaleDetailRptCount(Map<String, Object> map) {
		return binOLPTRPS29_Service.getBBCounterSaleDetailRptCount(map);
	}
}
