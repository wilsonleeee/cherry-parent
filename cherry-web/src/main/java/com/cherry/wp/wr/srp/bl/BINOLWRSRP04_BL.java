package com.cherry.wp.wr.srp.bl;

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
import com.cherry.wp.wr.srp.interfaces.BINOLWRSRP04_IF;
import com.cherry.wp.wr.srp.service.BINOLWRSRP99_Service;

/**
 * 商品销售排行BL
 * 
 * @author WangCT
 * @version 1.0 2014/09/29
 */
public class BINOLWRSRP04_BL implements BINOLWRSRP04_IF {
	
	@Resource
	private BINOLWRSRP99_Service binOLWRSRP99_Service;
	
	/** 导出共通BL **/
	@Resource
	private BINOLCM37_BL binOLCM37_BL;
	
	/** CodeTable **/
	@Resource
	private CodeTable code;

	@Override
	public int getSaleByPrtCount(Map<String, Object> map) {
		return binOLWRSRP99_Service.getSaleByPrtCount(map);
	}

	@Override
	public Map<String, Object> getSaleByPrtCountInfo(Map<String, Object> map) {
		return binOLWRSRP99_Service.getSaleByPrtCountInfo(map);
	}

	@Override
	public List<Map<String, Object>> getSaleByPrtList(Map<String, Object> map) {
		return binOLWRSRP99_Service.getSaleByPrtList(map);
	}

	@Override
	public List<Map<String, Object>> getDataList(Map<String, Object> map)
			throws Exception {
		return this.getSaleByPrtList(map);
	}

	@Override
	public Map<String, Object> getExportParam(Map<String, Object> map) {
		
		Map<String, Object> exportMap = new HashMap<String, Object>();
		exportMap.put(CherryConstants.ORGANIZATIONINFOID, map.get(CherryConstants.ORGANIZATIONINFOID));
		exportMap.put(CherryConstants.BRANDINFOID, map.get(CherryConstants.BRANDINFOID));
		String language = (String)map.get(CherryConstants.SESSION_CHERRY_LANGUAGE);
		
		exportMap.put("conditionMap", map);
		
        exportMap.put("sheetName", CherryUtil.getResourceValue("wp", "BINOLWRSRP04", language, "sheetName"));
        
        String conditionContent = this.getConditionStr(map);
		if(conditionContent != null && !"".equals(conditionContent)) {
			exportMap.put("header", conditionContent);
		}
        
        List<String[]> titleRowList = new ArrayList<String[]>();
        titleRowList.add(new String[]{"productName", CherryUtil.getResourceValue("wp", "BINOLWRSRP04", language, "WRSRP04_productName"), "20", "", ""});
        titleRowList.add(new String[]{"unitCode", CherryUtil.getResourceValue("wp", "BINOLWRSRP04", language, "WRSRP04_unitCode"), "20", "", ""});
        titleRowList.add(new String[]{"barCode", CherryUtil.getResourceValue("wp", "BINOLWRSRP04", language, "WRSRP04_barCode"), "20", "", ""});
        titleRowList.add(new String[]{"moduleCode", CherryUtil.getResourceValue("wp", "BINOLWRSRP04", language, "WRSRP04_moduleCode"), "20", "", "1190"});
        titleRowList.add(new String[]{"salePrice", CherryUtil.getResourceValue("wp", "BINOLWRSRP04", language, "WRSRP04_salePrice"), "15", "float", ""});
        titleRowList.add(new String[]{"quantity", CherryUtil.getResourceValue("wp", "BINOLWRSRP04", language, "WRSRP04_quantity"), "15", "int", ""});
        titleRowList.add(new String[]{"amount", CherryUtil.getResourceValue("wp", "BINOLWRSRP04", language, "WRSRP04_amount"), "15", "float", ""});
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
        String downloadFileName = CherryUtil.getResourceValue("wp", "BINOLWRSRP04", language, "downloadFileName");
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
		
		// 销售日期
		String saleDateStart = (String)map.get("saleDateStart");
		String saleDateEnd = (String)map.get("saleDateEnd");
		String notLimit = CherryUtil.getResourceValue(null, language, "global.page.notLimit");
		StringBuffer saleDateCondition = new StringBuffer();
		if(saleDateStart != null && !"".equals(saleDateStart)) {
			if(saleDateEnd != null && !"".equals(saleDateEnd)) {
				saleDateCondition.append(saleDateStart+"\0~\0"+saleDateEnd);
			} else {
				saleDateCondition.append(saleDateStart+"\0~\0"+notLimit);
			}
		} else {
			if(saleDateEnd != null && !"".equals(saleDateEnd)) {
				saleDateCondition.append(notLimit+"\0~\0"+saleDateEnd);
			}
		}
		String paramValue = saleDateCondition.toString();
		if(paramValue != null && !"".equals(paramValue)) {
			String paramName = CherryUtil.getResourceValue("wp", "BINOLWRSRP04", language, "WRSRP04_saleDate");
			condition.append(paramName + "：" + paramValue + "\0\0\0\0\0");
		}
		
		// 交易类型
		String saleType = (String)map.get("saleType");
		if(saleType != null && !"".equals(saleType)) {
			String paramName = CherryUtil.getResourceValue("wp", "BINOLWRSRP04", language, "WRSRP04_saleType");
			paramValue = code.getVal("1055", saleType);
			condition.append(paramName + "：" + paramValue + "\0\0\0\0\0");
		}
		
		// 支付方式
		String payTypeCode = (String)map.get("payTypeCode");
		if(payTypeCode != null && !"".equals(payTypeCode)) {
			String paramName = CherryUtil.getResourceValue("wp", "BINOLWRSRP04", language, "WRSRP04_payTypeCode");
			paramValue = code.getVal("1175", payTypeCode);
			condition.append(paramName + "：" + paramValue + "\0\0\0\0\0");
		}
		
		// 营业员
		String employeeName = (String)map.get("employeeName");
		if(employeeName != null && !"".equals(employeeName)) {
			String paramName = CherryUtil.getResourceValue("wp", "BINOLWRSRP04", language, "WRSRP04_employeeId");
			paramValue = employeeName;
			condition.append(paramName + "：" + paramValue + "\0\0\0\0\0");
		}
		
		// 大分类
		String bigClassName = (String)map.get("bigClassName");
		if(bigClassName != null && !"".equals(bigClassName)) {
			String paramName = CherryUtil.getResourceValue("wp", "BINOLWRSRP04", language, "WRSRP04_bigClassId");
			paramValue = bigClassName;
			condition.append(paramName + "：" + paramValue + "\0\0\0\0\0");
		}
		
		// 会员卡号
		String memCode = (String)map.get("memCode");
		if(memCode != null && !"".equals(memCode)) {
			String paramName = CherryUtil.getResourceValue("wp", "BINOLWRSRP04", language, "WRSRP04_memCode");
			paramValue = memCode;
			condition.append(paramName + "：" + paramValue + "\0\0\0\0\0");
		}
		
		return condition.toString();
	}

}
