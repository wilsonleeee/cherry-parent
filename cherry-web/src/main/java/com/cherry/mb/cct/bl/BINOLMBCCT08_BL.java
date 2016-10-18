package com.cherry.mb.cct.bl;

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
import com.cherry.mb.cct.interfaces.BINOLMBCCT08_IF;
import com.cherry.mb.cct.service.BINOLMBCCT08_Service;

public class BINOLMBCCT08_BL implements BINOLMBCCT08_IF{
	@Resource(name="binOLMBCCT08_Service")
	private BINOLMBCCT08_Service binOLMBCCT08_Service;
	
	@Resource(name="binOLCM37_BL")
	private BINOLCM37_BL binOLCM37_BL;
	
	@Override
	@SuppressWarnings("rawtypes")
	public List getCustomerList(Map<String, Object> map) {
		// 获取符合查询条件的客户列表
		List<Map<String, Object>> customerList = binOLMBCCT08_Service.getCustomerList(map);
		List<Map<String, Object>> newCustomerList = new ArrayList<Map<String, Object>>();
		for(Map<String,Object> customerMap : customerList){
			Map<String,Object> newCustomerMap = new HashMap<String, Object>();
			String memo = ConvertUtil.getString(customerMap.get("memo"));
			if(memo.length() > 30){
				newCustomerMap.putAll(customerMap);
				newCustomerMap.put("memoCut", memo.substring(0, 30)+" ...");
			}else{
				newCustomerMap.putAll(customerMap);
				newCustomerMap.put("memoCut", memo);
			}
			newCustomerList.add(newCustomerMap);
		}
		return newCustomerList;
	}
	
	@Override
	public int getCustomerCount(Map<String, Object> map) {
		// 获取符合查询条件的客户数量
		return binOLMBCCT08_Service.getCustomerCount(map);
	}

	@Override
	public Map<String, Object> getExportMap(Map<String, Object> map) {
		String language = ConvertUtil.getString(map.get(CherryConstants.SESSION_LANGUAGE));
		String[][] array = {
				{ "customerCode", binOLCM37_BL.getResourceValue("BINOLMBCCT08", language, "mbcct.customerCode"), "20", "", "" },
				{ "customerName", binOLCM37_BL.getResourceValue("BINOLMBCCT08", language, "mbcct.customerName"), "15", "", "" },
				{ "gender", binOLCM37_BL.getResourceValue("BINOLMBCCT08", language, "mbcct.gender"), "10", "", "1006" },
				{ "mobilePhone", binOLCM37_BL.getResourceValue("BINOLMBCCT08", language, "mbcct.mobilePhone"), "20", "", "" },
				{ "telephone", binOLCM37_BL.getResourceValue("BINOLMBCCT08", language, "mbcct.telephone"), "20", "", "" },
				{ "birthDay", binOLCM37_BL.getResourceValue("BINOLMBCCT08", language, "mbcct.birthDay"), "15", "", "" },
				{ "joinTime", binOLCM37_BL.getResourceValue("BINOLMBCCT08", language, "mbcct.joinTime"), "25", "", "" },
				{ "customerType", binOLCM37_BL.getResourceValue("BINOLMBCCT08", language, "mbcct.customerType"), "10", "", "1286" },
				{ "company", binOLCM37_BL.getResourceValue("BINOLMBCCT08", language, "mbcct.company"), "20", "", "" },
				{ "post", binOLCM37_BL.getResourceValue("BINOLMBCCT08", language, "mbcct.post"), "20", "", "" },
				{ "industry", binOLCM37_BL.getResourceValue("BINOLMBCCT08", language, "mbcct.industry"), "20", "", "" },
				{ "zip", binOLCM37_BL.getResourceValue("BINOLMBCCT08", language, "mbcct.zip"), "10", "", "" },
				{ "messageId", binOLCM37_BL.getResourceValue("BINOLMBCCT08", language, "mbcct.messageId"), "15", "", "" },
				{ "email", binOLCM37_BL.getResourceValue("BINOLMBCCT08", language, "mbcct.email"), "25", "", "" },
				{ "province", binOLCM37_BL.getResourceValue("BINOLMBCCT08", language, "mbcct.province"), "15", "", "" },
				{ "city", binOLCM37_BL.getResourceValue("BINOLMBCCT08", language, "mbcct.city"), "15", "", "" },
				{ "address", binOLCM37_BL.getResourceValue("BINOLMBCCT08", language, "mbcct.address"), "30", "", "" },
				{ "memo", binOLCM37_BL.getResourceValue("BINOLMBCCT08", language, "mbcct.memo"), "30", "", "" },
				{ "isReceiveMsg", binOLCM37_BL.getResourceValue("BINOLMBCCT08", language, "mbcct.isReceiveMsg"), "10", "", "1231" },
		};
		int dataLen = binOLMBCCT08_Service.getCustomerCount(map);
		map.put("dataLen", dataLen);
		map.put("sheetName", binOLCM37_BL.getResourceValue("BINOLMBCCT08", language, "mbcct.sheetName"));
		map.put("downloadFileName", binOLCM37_BL.getResourceValue("BINOLMBCCT08", language, "mbcct.downloadFileName"));
		map.put("titleRows", array);
		map.put(CherryConstants.SORT_ID, "joinTime desc");
		return map;
	}
	
	@Override
	public List<Map<String, Object>> getDataList(Map<String, Object> map)
			throws Exception {
		// 获取导出数据
		List<Map<String, Object>> customerList = binOLMBCCT08_Service.getCustomerList(map);
		return customerList;
	}

	@Override
	public String exportCsv(Map<String, Object> map) throws Exception {
		// 获取导出参数
		Map<String, Object> exportMap = map;
    	String tempFilePath = PropertiesUtil.pps.getProperty("tempFilePath");
        String sessionId = (String)map.get("sessionId");
        // 下载文件临时目录
        tempFilePath = tempFilePath +File.separator + sessionId;
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
