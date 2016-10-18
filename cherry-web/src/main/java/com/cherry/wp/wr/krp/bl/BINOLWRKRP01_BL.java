package com.cherry.wp.wr.krp.bl;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.cmbussiness.bl.BINOLCM37_BL;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CodeTable;
import com.cherry.cm.core.PropertiesUtil;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.wp.wr.krp.interfaces.BINOLWRKRP01_IF;
import com.cherry.wp.wr.krp.service.BINOLWRKRP99_Service;

/**
 * 库存报表BL
 * 
 * @author WangCT
 * @version 1.0 2014/09/24
 */
public class BINOLWRKRP01_BL implements BINOLWRKRP01_IF {
	
	@Resource
	private BINOLWRKRP99_Service binOLWRKRP99_Service;
	
	/** 导出共通BL **/
	@Resource
	private BINOLCM37_BL binOLCM37_BL;
	
	/** CodeTable **/
	@Resource
	private CodeTable code;

	@Override
	public int getProStockCount(Map<String, Object> map) {
		return binOLWRKRP99_Service.getProStockCount(map);
	}

	@Override
	public Map<String, Object> getProStockCountInfo(Map<String, Object> map) {
		return binOLWRKRP99_Service.getProStockCountInfo(map);
	}

	@Override
	public List<Map<String, Object>> getProStockList(Map<String, Object> map) {
		List<Map<String, Object>> proStockList = binOLWRKRP99_Service.getProStockList(map);
		if(proStockList != null && !proStockList.isEmpty()) {
			List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
			List<String[]> keyList = new ArrayList<String[]>();
			String[] key1 = {"bigClassId", "bigClassCode", "bigClassName"};
			String[] key2 = {"smallClassId", "smallClassCode", "smallClassName"};
			keyList.add(key1);
			keyList.add(key2);
			ConvertUtil.convertList2DeepList(proStockList,list,keyList,0);
			return list;
		}
		return null;
	}

	@Override
	public List<Map<String, Object>> getDataList(Map<String, Object> map)
			throws Exception {
		return binOLWRKRP99_Service.getProStockList(map);
	}

	@Override
	public Map<String, Object> getExportParam(Map<String, Object> map) {
		
		Map<String, Object> exportMap = new HashMap<String, Object>();
		exportMap.put(CherryConstants.ORGANIZATIONINFOID, map.get(CherryConstants.ORGANIZATIONINFOID));
		exportMap.put(CherryConstants.BRANDINFOID, map.get(CherryConstants.BRANDINFOID));
		String language = (String)map.get(CherryConstants.SESSION_CHERRY_LANGUAGE);
		
		exportMap.put("conditionMap", map);
		
        exportMap.put("sheetName", CherryUtil.getResourceValue("wp", "BINOLWRKRP01", language, "sheetName"));
        
        String conditionContent = this.getConditionStr(map);
		if(conditionContent != null && !"".equals(conditionContent)) {
			exportMap.put("header", conditionContent);
		}
        
        List<String[]> titleRowList = new ArrayList<String[]>();
        titleRowList.add(new String[]{"bigClassCode", CherryUtil.getResourceValue("wp", "BINOLWRKRP01", language, "WRKRP01_bigClassCode"), "15", "", ""});
        titleRowList.add(new String[]{"bigClassName", CherryUtil.getResourceValue("wp", "BINOLWRKRP01", language, "WRKRP01_bigClassName"), "15", "", ""});
        titleRowList.add(new String[]{"smallClassCode", CherryUtil.getResourceValue("wp", "BINOLWRKRP01", language, "WRKRP01_smallClassCode"), "15", "", ""});
        titleRowList.add(new String[]{"smallClassName", CherryUtil.getResourceValue("wp", "BINOLWRKRP01", language, "WRKRP01_smallClassName"), "15", "", ""});
        titleRowList.add(new String[]{"productName", CherryUtil.getResourceValue("wp", "BINOLWRKRP01", language, "WRKRP01_productName"), "20", "", ""});
        titleRowList.add(new String[]{"unitCode", CherryUtil.getResourceValue("wp", "BINOLWRKRP01", language, "WRKRP01_unitCode"), "20", "", ""});
        titleRowList.add(new String[]{"barCode", CherryUtil.getResourceValue("wp", "BINOLWRKRP01", language, "WRKRP01_barCode"), "20", "", ""});
        titleRowList.add(new String[]{"quantity", CherryUtil.getResourceValue("wp", "BINOLWRKRP01", language, "WRKRP01_quantity"), "15", "int", ""});
        exportMap.put("titleRows", titleRowList.toArray(new String[][]{}));
        
        return exportMap;
	}

	@Override
	public String exportCSV(Map<String, Object> map) throws Exception {
		
		// 获取导出参数
		Map<String, Object> exportMap = this.getExportParam(map);
        
		String language = (String)map.get(CherryConstants.SESSION_CHERRY_LANGUAGE);
		
    	String tempFilePath = PropertiesUtil.pps.getProperty("tempFilePath");
        String sessionId = (String)map.get("sessionId");
        // 下载文件临时目录
        tempFilePath = tempFilePath + File.separator + sessionId;
        exportMap.put("tempFilePath", tempFilePath);
        
        // 下载文件名
        String downloadFileName = CherryUtil.getResourceValue("wp", "BINOLWRKRP01", language, "downloadFileName");
        exportMap.put("tempFileName", downloadFileName);
        
        exportMap.put("charset", map.get("charset"));
        
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
	
	private String getConditionStr(Map<String, Object> map) {
		StringBuffer condition = new StringBuffer();
		String language = (String)map.get(CherryConstants.SESSION_CHERRY_LANGUAGE);
		
		// 大分类
		String bigClassName = (String)map.get("bigClassName");
		if(bigClassName != null && !"".equals(bigClassName)) {
			String paramName = CherryUtil.getResourceValue("wp", "BINOLWRKRP01", language, "WRKRP01_bigClassId");
			String paramValue = bigClassName;
			condition.append(paramName + "：" + paramValue + "\0\0\0\0\0");
		}
		
		// 小分类
		String smallClassName = (String)map.get("smallClassName");
		if(smallClassName != null && !"".equals(smallClassName)) {
			String paramName = CherryUtil.getResourceValue("wp", "BINOLWRKRP01", language, "WRKRP01_smallClassId");
			String paramValue = smallClassName;
			condition.append(paramName + "：" + paramValue + "\0\0\0\0\0");
		}
		
		// 逻辑仓库
		String logicInventoryName = (String)map.get("logicInventoryName");
		if(logicInventoryName != null && !"".equals(logicInventoryName)) {
			String paramName = CherryUtil.getResourceValue("wp", "BINOLWRKRP01", language, "WRKRP01_logicInventoryInfoId");
			String paramValue = logicInventoryName;
			condition.append(paramName + "：" + paramValue + "\0\0\0\0\0");
		}
		
		// 交易类型
		String validFlag = (String)map.get("validFlag");
		if(validFlag != null && !"".equals(validFlag)) {
			String paramName = CherryUtil.getResourceValue("wp", "BINOLWRKRP01", language, "WRKRP01_validFlag");
			String paramValue = code.getVal("1137", validFlag);
			condition.append(paramName + "：" + paramValue + "\0\0\0\0\0");
		}
		
		// 产品名称
		String nameTotal = (String)map.get("nameTotal");
		if(nameTotal != null && !"".equals(nameTotal)) {
			String paramName = CherryUtil.getResourceValue("wp", "BINOLWRKRP01", language, "WRKRP01_productName");
			String paramValue = nameTotal;
			condition.append(paramName + "：" + paramValue + "\0\0\0\0\0");
		}
		
		return condition.toString();
	}

}
