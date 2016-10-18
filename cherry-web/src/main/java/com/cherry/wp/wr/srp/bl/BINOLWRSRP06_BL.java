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
import com.cherry.cm.util.DateUtil;
import com.cherry.wp.wr.srp.interfaces.BINOLWRSRP06_IF;
import com.cherry.wp.wr.srp.service.BINOLWRSRP99_Service;

/**
 * 销售月报表BL
 * 
 * @author WangCT
 * @version 1.0 2014/10/29
 */
public class BINOLWRSRP06_BL implements BINOLWRSRP06_IF {
	
	@Resource
	private BINOLWRSRP99_Service binOLWRSRP99_Service;
	
	/** 导出共通BL **/
	@Resource
	private BINOLCM37_BL binOLCM37_BL;
	
	/** CodeTable **/
	@Resource
	private CodeTable code;

	@Override
	public Map<String, Object> getSaleByDayCountInfo(Map<String, Object> map) {
		return binOLWRSRP99_Service.getSaleByDayCountInfo(map);
	}

	@Override
	public List<Map<String, Object>> getSaleByDayList(Map<String, Object> map) {
		
		String year = (String)map.get("year");
		String month = (String)map.get("month");
		if(month.length() == 1) {
			month = "0" + month;
		}
		String saleDateStart = year+"-"+month+"-"+"01";
		String saleDateEnd = DateUtil.getFirstOrLastDateYMD(saleDateStart, 1);
		map.put("saleDateStart", saleDateStart);
		map.put("saleDateEnd", saleDateEnd);
		
		List<Map<String, Object>> saleByDayList = binOLWRSRP99_Service.getSaleByDayList(map);
		List<Map<String, Object>> memCountByDayList = binOLWRSRP99_Service.getMemCountByDayList(map);
		if(saleByDayList != null && !saleByDayList.isEmpty()) {
			for(int i = 0; i < saleByDayList.size(); i++) {
				Map<String, Object> saleByDayMap = saleByDayList.get(i);
				String saleDate = (String)saleByDayMap.get("saleDate");
				if(memCountByDayList != null && !memCountByDayList.isEmpty()) {
					for(int j = 0; j < memCountByDayList.size(); j++) {
						Map<String, Object> memCountByDayMap = memCountByDayList.get(j);
						String joinDate = (String)memCountByDayMap.get("saleDate");
						if(joinDate.equals(saleDate)) {
							saleByDayMap.put("memCount", memCountByDayMap.get("memCount"));
							memCountByDayList.remove(j);
							break;
						}
					}
				}
				Integer saleCount = (Integer)saleByDayMap.get("saleCount");
				BigDecimal quantity = (BigDecimal)saleByDayMap.get("quantity");
				BigDecimal memAmount = (BigDecimal)saleByDayMap.get("memAmount");
				BigDecimal amount = (BigDecimal)saleByDayMap.get("amount");
				if(saleCount == null) {
					saleCount = 0;
				}
				if(quantity == null) {
					quantity = new BigDecimal(0);
				}
				if(memAmount == null) {
					memAmount = new BigDecimal(0);
				}
				if(amount == null) {
					amount = new BigDecimal(0);
				}
				// 连带率
				if(saleCount != 0) {
					saleByDayMap.put("joinRate", CherryUtil.div(quantity.doubleValue(), saleCount, 2));
				}
				// 会员购买占比
				if(amount.doubleValue() != 0) {
					saleByDayMap.put("memAmountRate", CherryUtil.percent(memAmount.doubleValue(), amount.doubleValue(), 2));
				}
			}
			if(memCountByDayList != null && !memCountByDayList.isEmpty()) {
				saleByDayList.addAll(memCountByDayList);
			}
			return saleByDayList;
		} else {
			if(memCountByDayList != null && !memCountByDayList.isEmpty()) {
				return memCountByDayList;
			}
		}
		return null;
	}
	
	@Override
	public List<Map<String, Object>> getDataList(Map<String, Object> map)
			throws Exception {
		return this.getSaleByDayList(map);
	}

	@Override
	public Map<String, Object> getExportParam(Map<String, Object> map) {
		
		Map<String, Object> exportMap = new HashMap<String, Object>();
		exportMap.put(CherryConstants.ORGANIZATIONINFOID, map.get(CherryConstants.ORGANIZATIONINFOID));
		exportMap.put(CherryConstants.BRANDINFOID, map.get(CherryConstants.BRANDINFOID));
		String language = (String)map.get(CherryConstants.SESSION_CHERRY_LANGUAGE);
		
		exportMap.put("conditionMap", map);
		
        exportMap.put("sheetName", CherryUtil.getResourceValue("wp", "BINOLWRSRP06", language, "sheetName"));
        
        String conditionContent = this.getConditionStr(map);
		if(conditionContent != null && !"".equals(conditionContent)) {
			exportMap.put("header", conditionContent);
		}
        
        List<String[]> titleRowList = new ArrayList<String[]>();
        titleRowList.add(new String[]{"saleDate", CherryUtil.getResourceValue("wp", "BINOLWRSRP06", language, "WRSRP06_saleDate"), "20", "", ""});
        titleRowList.add(new String[]{"quantity", CherryUtil.getResourceValue("wp", "BINOLWRSRP06", language, "WRSRP06_quantity"), "20", "int", ""});
        titleRowList.add(new String[]{"amount", CherryUtil.getResourceValue("wp", "BINOLWRSRP06", language, "WRSRP06_amount"), "20", "float", ""});
        titleRowList.add(new String[]{"joinRate", CherryUtil.getResourceValue("wp", "BINOLWRSRP06", language, "WRSRP06_joinRate"), "20", "float", ""});
        titleRowList.add(new String[]{"memCount", CherryUtil.getResourceValue("wp", "BINOLWRSRP06", language, "WRSRP06_memCount"), "20", "int", ""});
        titleRowList.add(new String[]{"memAmountRate", CherryUtil.getResourceValue("wp", "BINOLWRSRP06", language, "WRSRP06_memAmountRate"), "20", "", ""});
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
        String downloadFileName = CherryUtil.getResourceValue("wp", "BINOLWRSRP06", language, "downloadFileName");
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
		String year = (String)map.get("year");
		String month = (String)map.get("month");
		if(month.length() == 1) {
			month = "0" + month;
		}
		StringBuffer saleDateCondition = new StringBuffer();
		saleDateCondition.append(year+"-"+month);
		String paramValue = saleDateCondition.toString();
		if(paramValue != null && !"".equals(paramValue)) {
			String paramName = CherryUtil.getResourceValue("wp", "BINOLWRSRP06", language, "WRSRP06_saleDate");
			condition.append(paramName + "：" + paramValue + "\0\0\0\0\0");
		}
		
		// 交易类型
		String saleType = (String)map.get("saleType");
		if(saleType != null && !"".equals(saleType)) {
			String paramName = CherryUtil.getResourceValue("wp", "BINOLWRSRP06", language, "WRSRP06_saleType");
			paramValue = code.getVal("1055", saleType);
			condition.append(paramName + "：" + paramValue + "\0\0\0\0\0");
		}
		
		// 支付方式
		String payTypeCode = (String)map.get("payTypeCode");
		if(payTypeCode != null && !"".equals(payTypeCode)) {
			String paramName = CherryUtil.getResourceValue("wp", "BINOLWRSRP06", language, "WRSRP06_payTypeCode");
			paramValue = code.getVal("1175", payTypeCode);
			condition.append(paramName + "：" + paramValue + "\0\0\0\0\0");
		}
		
		// 营业员
		String employeeName = (String)map.get("employeeName");
		if(employeeName != null && !"".equals(employeeName)) {
			String paramName = CherryUtil.getResourceValue("wp", "BINOLWRSRP06", language, "WRSRP06_employeeId");
			paramValue = employeeName;
			condition.append(paramName + "：" + paramValue + "\0\0\0\0\0");
		}
		
		return condition.toString();
	}

}
