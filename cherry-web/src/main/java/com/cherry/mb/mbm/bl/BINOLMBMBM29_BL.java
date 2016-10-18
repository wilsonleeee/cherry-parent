package com.cherry.mb.mbm.bl;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.cmbussiness.bl.BINOLCM33_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM37_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM39_BL;
import com.cherry.cm.cmbussiness.interfaces.BINOLCM37_IF;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.PropertiesUtil;
import com.cherry.cm.util.CherryUtil;

public class BINOLMBMBM29_BL implements BINOLCM37_IF {
	
	/** 会员检索画面共通BL **/
	@Resource
	private BINOLCM33_BL binOLCM33_BL;
	
	/** 导出会员信息共通BL **/
	@Resource
	private BINOLCM37_BL binOLCM37_BL;
	
	/** 会员检索条件转换共通BL **/
	@Resource
	private BINOLCM39_BL binOLCM39_BL;
	
	/**
     * 导出处理
     */
	public String export(Map<String, Object> map) throws Exception {
		
		// 获取导出参数
		Map<String, Object> exportMap = this.getExportParam(map);
        
		String language = (String)map.get(CherryConstants.SESSION_CHERRY_LANGUAGE);
		
    	String tempFilePath = PropertiesUtil.pps.getProperty("tempFilePath");
        String sessionId = (String)map.get("sessionId");
        // 下载文件临时目录
        tempFilePath = tempFilePath + File.separator + sessionId;
        exportMap.put("tempFilePath", tempFilePath);
        
        // 下载文件名
        String downloadFileName = binOLCM37_BL.getResourceValue("BINOLMBMBM09", language, "downloadFileNameMemSale");
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
     * 获取导出参数
     */
	public Map<String, Object> getExportParam(Map<String, Object> map) {
		
		Map<String, Object> exportMap = new HashMap<String, Object>();
		exportMap.put(CherryConstants.ORGANIZATIONINFOID, map.get(CherryConstants.ORGANIZATIONINFOID));
		exportMap.put(CherryConstants.BRANDINFOID, map.get(CherryConstants.BRANDINFOID));
		String language = (String)map.get(CherryConstants.SESSION_CHERRY_LANGUAGE);
		exportMap.put("charset", map.get("charset"));
		
		Map<String, Object> conditionMap = new HashMap<String, Object>();
		conditionMap.putAll(map);
		// 剔除map中的空值
		conditionMap = CherryUtil.remEmptyVal(conditionMap);
		conditionMap.put(CherryConstants.SESSION_CHERRY_LANGUAGE, language);
		// 把会员搜索条件转换为文字说明
		String conditionContent = binOLCM39_BL.conditionDisplay(conditionMap);
		if(conditionContent != null && !"".equals(conditionContent)) {
			exportMap.put("header", binOLCM37_BL.getResourceValue("BINOLMBMBM09", language, "binolmbmbm09_conditionContent")+conditionContent);
		}
		
		List<String> selectors = new ArrayList<String>();
		selectors.add("memCode");
		map.put("selectors", selectors);
		
		exportMap.put("conditionMap", map);
		
        exportMap.put("sheetName", binOLCM37_BL.getResourceValue("BINOLMBMBM09", language, "sheetNameMemSale"));
        String saleCouFlag = (String)map.get("saleCouFlag");
        List<String[]> titleRowList = new ArrayList<String[]>();
        titleRowList.add(new String[]{"memberCode", binOLCM37_BL.getResourceValue("BINOLMBMBM09", language, "binolmbmbm09_memCode"), "20", "", ""});
        titleRowList.add(new String[]{"billCode", binOLCM37_BL.getResourceValue("BINOLMBMBM09", language, "binolmbmbm09_billCode"), "50", "", ""});
        titleRowList.add(new String[]{"saleType", binOLCM37_BL.getResourceValue("BINOLMBMBM09", language, "binolmbmbm09_saleType"), "15", "", "1055"});
        titleRowList.add(new String[]{"saleTime", binOLCM37_BL.getResourceValue("BINOLMBMBM09", language, "binolmbmbm09_saleTime"), "30", "", ""});
        if(saleCouFlag != null && "1".equals(saleCouFlag)) {
        	 titleRowList.add(new String[]{"departCode", binOLCM37_BL.getResourceValue("BINOLMBMBM09", language, "binolmbmbm09_departCode"), "15", "", ""});
             titleRowList.add(new String[]{"departName", binOLCM37_BL.getResourceValue("BINOLMBMBM09", language, "binolmbmbm09_departName"), "15", "", ""});
        }
        titleRowList.add(new String[]{"amount", binOLCM37_BL.getResourceValue("BINOLMBMBM09", language, "binolmbmbm09_amount"), "15", "float", ""});
        titleRowList.add(new String[]{"quantity", binOLCM37_BL.getResourceValue("BINOLMBMBM09", language, "binolmbmbm09_quantity"), "15", "int", ""});
        exportMap.put("titleRows", titleRowList.toArray(new String[][]{}));
        
        return exportMap;
	}

	@Override
	public List<Map<String, Object>> getDataList(Map<String, Object> map)
			throws Exception {
		
		List<Map<String, Object>> dataList = null;
		Map<String, Object> resultMap = binOLCM33_BL.getMemSaleRecord(map);
		if(resultMap != null) {
			dataList = (List)resultMap.get("memSaleList");
		}
		return dataList;
	}

}
