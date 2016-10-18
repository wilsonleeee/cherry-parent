package com.cherry.wp.wr.srp.bl;

import java.io.File;
import java.math.BigDecimal;
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
import com.cherry.wp.wr.srp.interfaces.BINOLWRSRP03_IF;
import com.cherry.wp.wr.srp.service.BINOLWRSRP99_Service;

/**
 * 销售分类统计BL
 * 
 * @author WangCT
 * @version 1.0 2014/09/19
 */
public class BINOLWRSRP03_BL implements BINOLWRSRP03_IF {
	
	@Resource
	private BINOLWRSRP99_Service binOLWRSRP99_Service;
	
	/** 导出共通BL **/
	@Resource
	private BINOLCM37_BL binOLCM37_BL;
	
	/** CodeTable **/
	@Resource
	private CodeTable code;

	@Override
	public Map<String, Object> getSaleCountByClass(Map<String, Object> map) {
		Map<String, Object> saleCountByClassInfo = new HashMap<String, Object>();
		// 总金额
		BigDecimal totalAmount = new BigDecimal(0);
		// 总数量
		BigDecimal totalQuantity = new BigDecimal(0);
		// 分类统计List
		List<Map<String, Object>> saleCountByClassList = new ArrayList<Map<String,Object>>();
		// 按分类统计销售
 		List<Map<String, Object>> _saleCountByClassList = binOLWRSRP99_Service.getSaleCountByClass(map);
		if(_saleCountByClassList != null && !_saleCountByClassList.isEmpty()) {
			int bigClassId = -1;
			List<Map<String, Object>> smallClassList = new ArrayList<Map<String,Object>>();
			String language = (String)map.get(CherryConstants.SESSION_CHERRY_LANGUAGE);
			for(int i = 0; i < _saleCountByClassList.size(); i++) {
				Map<String, Object> _saleCountByClassMap = _saleCountByClassList.get(i);
				int _bigClassId = (Integer)_saleCountByClassMap.get("bigClassId");
				
				Map<String, Object> smallClassMap = new HashMap<String, Object>();
				smallClassMap.put("smallClassId", _saleCountByClassMap.get("smallClassId"));
				smallClassMap.put("smallClassCode", _saleCountByClassMap.get("smallClassCode"));
				smallClassMap.put("smallClassName", _saleCountByClassMap.get("smallClassName"));
				smallClassMap.put("quantity", _saleCountByClassMap.get("quantity"));
				smallClassMap.put("amount", _saleCountByClassMap.get("amount"));
				
				if(_bigClassId != bigClassId) {
					bigClassId = _bigClassId;
					Map<String, Object> saleCountByClassMap = new HashMap<String, Object>();
					saleCountByClassMap.put("bigClassId", _bigClassId);
					saleCountByClassMap.put("bigClassCode", _saleCountByClassMap.get("bigClassCode"));
					saleCountByClassMap.put("bigClassName", _saleCountByClassMap.get("bigClassName"));
					saleCountByClassMap.put("smallClassName", CherryUtil.getResourceValue("wp", "BINOLWRSRP03", language, "WRSRP03_totalName"));
					smallClassList = new ArrayList<Map<String,Object>>();
					saleCountByClassMap.put("smallClassList", smallClassList);
					saleCountByClassList.add(saleCountByClassMap);
				}
				smallClassList.add(smallClassMap);
			}
			
			for(int i = 0; i < saleCountByClassList.size(); i++) {
				Map<String, Object> saleCountByClassMap = saleCountByClassList.get(i);
				smallClassList = (List)saleCountByClassMap.get("smallClassList");
				BigDecimal bigAmount = new BigDecimal(0);
				BigDecimal bigQuantity = new BigDecimal(0);
				for(int j = 0; j < smallClassList.size(); j++) {
					Map<String, Object> smallClassMap = smallClassList.get(j);
					BigDecimal amount = (BigDecimal)smallClassMap.get("amount");
					BigDecimal quantity = (BigDecimal)smallClassMap.get("quantity");
					bigAmount = bigAmount.add(amount);
					bigQuantity = bigQuantity.add(quantity);
				}
				saleCountByClassMap.put("amount", bigAmount);
				saleCountByClassMap.put("quantity", bigQuantity);
				totalAmount = totalAmount.add(bigAmount);
				totalQuantity = totalQuantity.add(bigQuantity);
			}
		}
		
		saleCountByClassInfo.put("sumAmount", totalAmount);
		saleCountByClassInfo.put("sumQuantity", totalQuantity);
		saleCountByClassInfo.put("saleCountByClassList", saleCountByClassList);
		return saleCountByClassInfo;
	}

	@Override
	public List<Map<String, Object>> getDataList(Map<String, Object> map)
			throws Exception {
		
		Map<String, Object> saleCountByClassInfo = this.getSaleCountByClass(map);
		if(saleCountByClassInfo != null && !saleCountByClassInfo.isEmpty()) {
			List<Map<String, Object>> saleCountByClassList = (List)saleCountByClassInfo.get("saleCountByClassList");
			if(saleCountByClassList != null && !saleCountByClassList.isEmpty()) {
				List<Map<String, Object>> dataList = new ArrayList<Map<String,Object>>();
				for(int i = 0; i < saleCountByClassList.size(); i++) {
					Map<String, Object> saleCountByClassMap = saleCountByClassList.get(i);
					List<Map<String, Object>> smallClassList = (List)saleCountByClassMap.get("smallClassList");
					saleCountByClassMap.remove("smallClassList");
					dataList.add(saleCountByClassMap);
					dataList.addAll(smallClassList);
				}
				return dataList;
			}
		}
		return null;
	}

	@Override
	public Map<String, Object> getExportParam(Map<String, Object> map) {
		
		Map<String, Object> exportMap = new HashMap<String, Object>();
		exportMap.put(CherryConstants.ORGANIZATIONINFOID, map.get(CherryConstants.ORGANIZATIONINFOID));
		exportMap.put(CherryConstants.BRANDINFOID, map.get(CherryConstants.BRANDINFOID));
		String language = (String)map.get(CherryConstants.SESSION_CHERRY_LANGUAGE);
		
		exportMap.put("conditionMap", map);
		
        exportMap.put("sheetName", CherryUtil.getResourceValue("wp", "BINOLWRSRP03", language, "sheetName"));
        
        String conditionContent = this.getConditionStr(map);
		if(conditionContent != null && !"".equals(conditionContent)) {
			exportMap.put("header", conditionContent);
		}
        
        List<String[]> titleRowList = new ArrayList<String[]>();
        titleRowList.add(new String[]{"bigClassCode", CherryUtil.getResourceValue("wp", "BINOLWRSRP03", language, "WRSRP03_bigClassCode"), "20", "", ""});
        titleRowList.add(new String[]{"bigClassName", CherryUtil.getResourceValue("wp", "BINOLWRSRP03", language, "WRSRP03_bigClassName"), "20", "", ""});
        titleRowList.add(new String[]{"smallClassCode", CherryUtil.getResourceValue("wp", "BINOLWRSRP03", language, "WRSRP03_smallClassCode"), "20", "", ""});
        titleRowList.add(new String[]{"smallClassName", CherryUtil.getResourceValue("wp", "BINOLWRSRP03", language, "WRSRP03_smallClassName"), "20", "", ""});
        titleRowList.add(new String[]{"quantity", CherryUtil.getResourceValue("wp", "BINOLWRSRP03", language, "WRSRP03_quantity"), "15", "int", ""});
        titleRowList.add(new String[]{"amount", CherryUtil.getResourceValue("wp", "BINOLWRSRP03", language, "WRSRP03_amount"), "15", "float", ""});
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
        String downloadFileName = CherryUtil.getResourceValue("wp", "BINOLWRSRP03", language, "downloadFileName");
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
		String startDate = (String)map.get("startDate");
		String endDate = (String)map.get("endDate");
		String notLimit = CherryUtil.getResourceValue(null, language, "global.page.notLimit");
		StringBuffer saleDateCondition = new StringBuffer();
		if(startDate != null && !"".equals(startDate)) {
			if(endDate != null && !"".equals(endDate)) {
				saleDateCondition.append(startDate+"\0~\0"+endDate);
			} else {
				saleDateCondition.append(startDate+"\0~\0"+notLimit);
			}
		} else {
			if(endDate != null && !"".equals(endDate)) {
				saleDateCondition.append(notLimit+"\0~\0"+endDate);
			}
		}
		String paramValue = saleDateCondition.toString();
		if(paramValue != null && !"".equals(paramValue)) {
			String paramName = CherryUtil.getResourceValue("wp", "BINOLWRSRP03", language, "WRSRP03_saleDate");
			condition.append(paramName + "：" + paramValue + "\0\0\0\0\0");
		}
		
		// 交易类型
		String saleType = (String)map.get("saleType");
		if(saleType != null && !"".equals(saleType)) {
			String paramName = CherryUtil.getResourceValue("wp", "BINOLWRSRP03", language, "WRSRP03_saleType");
			paramValue = code.getVal("1055", saleType);
			condition.append(paramName + "：" + paramValue + "\0\0\0\0\0");
		}
		
		// 营业员
		String employeeName = (String)map.get("employeeName");
		if(employeeName != null && !"".equals(employeeName)) {
			String paramName = CherryUtil.getResourceValue("wp", "BINOLWRSRP03", language, "WRSRP03_employeeId");
			paramValue = employeeName;
			condition.append(paramName + "：" + paramValue + "\0\0\0\0\0");
		}
		
		// 大分类
		String bigClassName = (String)map.get("bigClassName");
		if(bigClassName != null && !"".equals(bigClassName)) {
			String paramName = CherryUtil.getResourceValue("wp", "BINOLWRSRP03", language, "WRSRP03_bigClassId");
			paramValue = bigClassName;
			condition.append(paramName + "：" + paramValue + "\0\0\0\0\0");
		}
		
		return condition.toString();
	}

}
