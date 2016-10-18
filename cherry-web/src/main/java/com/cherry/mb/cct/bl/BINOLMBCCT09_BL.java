package com.cherry.mb.cct.bl;

import java.io.File;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.cmbussiness.bl.BINOLCM37_BL;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.PropertiesUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.mb.cct.interfaces.BINOLMBCCT09_IF;
import com.cherry.mb.cct.service.BINOLMBCCT09_Service;

public class BINOLMBCCT09_BL implements BINOLMBCCT09_IF{
	@Resource(name="binOLMBCCT09_Service")
	private BINOLMBCCT09_Service binOLMBCCT09_Service;
	
	@Resource(name="binOLCM37_BL")
	private BINOLCM37_BL binOLCM37_BL;
	
	@Override
	@SuppressWarnings("rawtypes")
	public List getCallLogList(Map<String, Object> map) {
		// 获取符合查询条件的来电记录列表
		List<Map<String, Object>> callLogList = binOLMBCCT09_Service.getCallLogList(map);
		return callLogList;
	}

	@Override
	public int getCallLogCount(Map<String, Object> map) {
		// 获取符合查询条件的来电记录数量
		return binOLMBCCT09_Service.getCallLogCount(map);
	}

	@Override
	public Map<String, Object> getExportMap(Map<String, Object> map) {
		String language = ConvertUtil.getString(map.get(CherryConstants.SESSION_LANGUAGE));
		String[][] array = {
				{ "callId", binOLCM37_BL.getResourceValue("BINOLMBCCT09", language, "mbcct.callId"), "30", "", "" },
				{ "cNo", binOLCM37_BL.getResourceValue("BINOLMBCCT09", language, "mbcct.cNo"), "15", "", "" },
				{ "customerNumber", binOLCM37_BL.getResourceValue("BINOLMBCCT09", language, "mbcct.customerNumber"), "20", "", "" },
				{ "customerCode", binOLCM37_BL.getResourceValue("BINOLMBCCT09", language, "mbcct.customerCode"), "20", "", "" },
				{ "customerName", binOLCM37_BL.getResourceValue("BINOLMBCCT09", language, "mbcct.customerName"), "20", "", "" },
				{ "isMember", binOLCM37_BL.getResourceValue("BINOLMBCCT09", language, "mbcct.isMember"), "15", "", "" },
				{ "callType", binOLCM37_BL.getResourceValue("BINOLMBCCT09", language, "mbcct.callType"), "15", "", "1288" },
				{ "callTime", binOLCM37_BL.getResourceValue("BINOLMBCCT09", language, "mbcct.callTime"), "25", "", "" },
		};
		int dataLen = binOLMBCCT09_Service.getCallLogCount(map);
		map.put("dataLen", dataLen);
		map.put("sheetName", binOLCM37_BL.getResourceValue("BINOLMBCCT09", language, "mbcct.sheetName"));
		map.put("downloadFileName", binOLCM37_BL.getResourceValue("BINOLMBCCT09", language, "mbcct.downloadFileName"));
		map.put("titleRows", array);
		map.put(CherryConstants.SORT_ID, "callTime desc");
		return map;
	}
	
	@Override
	public List<Map<String, Object>> getDataList(Map<String, Object> map)
			throws Exception {
		String language = ConvertUtil.getString(map.get(CherryConstants.SESSION_LANGUAGE));
		// 获取符合查询条件的来电记录列表
		List<Map<String, Object>> callLogList = binOLMBCCT09_Service.getCallLogList(map);
		for(Map<String, Object> callLogMap : callLogList){
			String isMember = ConvertUtil.getString(callLogMap.get("isMember"));
			if("1".equals(isMember)){
				callLogMap.put("isMember", binOLCM37_BL.getResourceValue("BINOLMBCCT09", language, "mbcct.memberCall"));
			}else{
				callLogMap.put("isMember", binOLCM37_BL.getResourceValue("BINOLMBCCT09", language, "mbcct.notMemberCall"));
			}
		}
		return callLogList;
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
