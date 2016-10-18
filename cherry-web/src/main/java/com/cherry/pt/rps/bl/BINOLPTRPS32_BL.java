package com.cherry.pt.rps.bl;

import java.io.File;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.cmbussiness.bl.BINOLCM37_BL;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.PropertiesUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.pt.rps.interfaces.BINOLPTRPS32_IF;
import com.cherry.pt.rps.service.BINOLPTRPS32_Service;

public class BINOLPTRPS32_BL implements BINOLPTRPS32_IF {

	@Resource(name="binOLPTRPS32_Service")
	private BINOLPTRPS32_Service binOLPTRPS32_Service;
	@Resource(name="binOLCM37_BL")
	private BINOLCM37_BL binOLCM37_BL;
	
	@Override
	public List<Map<String, Object>> getDataList(Map<String, Object> map)
			throws Exception {
		return this.getCouponUsedDetailList(map);
	}

	@Override
	public int getBaCouponUsedInfoCount(Map<String, Object> map) {
		return binOLPTRPS32_Service.getBaCouponUsedInfoCount(map);
	}

	@Override
	public List<Map<String, Object>> getBaCouponUsedInfoList(
			Map<String, Object> map) {
		return binOLPTRPS32_Service.getBaCouponUsedInfoList(map);
	}
	
	@Override
	public Map<String, Object> getSumInfo(Map<String, Object> map) {
		return binOLPTRPS32_Service.getSumInfo(map);
	}

	@Override
	public Map<String, Object> getResellerNameFromCode(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return binOLPTRPS32_Service.getResellerNameFromCode(map);
	}

	@Override
	public int getCouponUsedDetailCount(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return binOLPTRPS32_Service.getCouponUsedDetailCount(map);
	}

	@Override
	public List<Map<String, Object>> getCouponUsedDetailList(
			Map<String, Object> map) {
		List<Map<String, Object>> list = binOLPTRPS32_Service.getCouponUsedDetailList(map);
		if(list != null && !list.isEmpty()) {
			String language = ConvertUtil.getString(map.get(CherryConstants.SESSION_LANGUAGE));
			String channel = binOLCM37_BL.getResourceValue("BINOLPTRPS32", language, "RPS32_website");
			for(Map<String, Object> listMap : list) {
				listMap.put("usedChannel", channel);
			}
		}
		return list;
	}

	@Override
	public Map<String, Object> getExportMap(Map<String, Object> map) {
		String language = ConvertUtil.getString(map.get(CherryConstants.SESSION_LANGUAGE));
		String[][] array = {
				{ "parentResellerCode", binOLCM37_BL.getResourceValue("BINOLPTRPS32", language, "RPS32_reseller1LCode"), "15", "", "" },
				{ "parentResellerName", binOLCM37_BL.getResourceValue("BINOLPTRPS32", language, "RPS32_reseller1LName"), "20", "", "" },
				{ "baCode", binOLCM37_BL.getResourceValue("BINOLPTRPS32", language, "RPS32_reseller2LCode"), "15", "", "" },
				{ "baNameOnly", binOLCM37_BL.getResourceValue("BINOLPTRPS32", language, "RPS32_reseller2LName"), "20", "", "" },
				{ "batchCode", binOLCM37_BL.getResourceValue("BINOLPTRPS32", language, "RPS32_batchCode"), "15", "", "" },
				{ "batchName", binOLCM37_BL.getResourceValue("BINOLPTRPS32", language, "RPS32_batchName"), "20", "", "" },
				{ "couponCode", binOLCM37_BL.getResourceValue("BINOLPTRPS32", language, "RPS32_couponCode"), "30", "", "" },
				{ "usedDate", binOLCM37_BL.getResourceValue("BINOLPTRPS32", language, "RPS32_usedDate"), "15", "", "" },
				{ "usedChannel", binOLCM37_BL.getResourceValue("BINOLPTRPS32", language, "RPS32_usedChannel"), "15", "", "" },
				{ "memberCode", binOLCM37_BL.getResourceValue("BINOLPTRPS32", language, "RPS32_memberCode"), "15", "", "" },
				{ "memberName", binOLCM37_BL.getResourceValue("BINOLPTRPS32", language, "RPS32_memberName"), "20", "", "" },
				{ "mobilePhone", binOLCM37_BL.getResourceValue("BINOLPTRPS32", language, "RPS32_mobilePhone"), "15", "", "" },
				{ "quantity", binOLCM37_BL.getResourceValue("BINOLPTRPS32", language, "RPS32_quantity"), "15", "int", "" },
				{ "amount", binOLCM37_BL.getResourceValue("BINOLPTRPS32", language, "RPS32_saleAmount"), "20", "float", "" },
				{ "deductAmount", binOLCM37_BL.getResourceValue("BINOLPTRPS32", language, "RPS32_deductAmount"), "20", "float", "" }
		};
		int dataLen = this.getCouponUsedDetailCount(map);
		map.put("dataLen", dataLen);
		map.put("sheetName", binOLCM37_BL.getResourceValue("BINOLPTRPS32", language, "sheetName"));
		map.put("downloadFileName", binOLCM37_BL.getResourceValue("BINOLPTRPS32", language, "downloadFileName"));
		map.put("titleRows", array);
		map.put(CherryConstants.SORT_ID, "usedDate desc");
		return map;
	}

	@Override
	public String exportCsv(Map<String, Object> map) throws Exception {
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
