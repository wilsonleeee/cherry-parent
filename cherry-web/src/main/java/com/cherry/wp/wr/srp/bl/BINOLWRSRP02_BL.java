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
import com.cherry.wp.wr.srp.interfaces.BINOLWRSRP02_IF;
import com.cherry.wp.wr.srp.service.BINOLWRSRP99_Service;

/**
 * 销售明细报表BL
 * 
 * @author WangCT
 * @version 1.0 2014/09/15
 */
public class BINOLWRSRP02_BL implements BINOLWRSRP02_IF {
	
	@Resource
	private BINOLWRSRP99_Service binOLWRSRP99_Service;
	
	/** 导出共通BL **/
	@Resource
	private BINOLCM37_BL binOLCM37_BL;
	
	/** CodeTable **/
	@Resource
	private CodeTable code;

	@Override
	public int getSaleRecordCount(Map<String, Object> map) {
		return binOLWRSRP99_Service.getSaleRecordCount(map);
	}
	
	@Override
	public Map<String, Object> getSaleCountInfo(Map<String, Object> map) {
		return binOLWRSRP99_Service.getSaleCountInfo(map);
	}

	@Override
	public List<Map<String, Object>> getSaleRecordList(Map<String, Object> map) {
		return binOLWRSRP99_Service.getSaleRecordList(map);
	}
	
	@Override
	public List<Map<String, Object>> getDataList(Map<String, Object> map)
			throws Exception {
		return binOLWRSRP99_Service.getSaleRecordDetailList(map);
	}

	@Override
	public Map<String, Object> getExportParam(Map<String, Object> map) {
		
		Map<String, Object> exportMap = new HashMap<String, Object>();
		exportMap.put(CherryConstants.ORGANIZATIONINFOID, map.get(CherryConstants.ORGANIZATIONINFOID));
		exportMap.put(CherryConstants.BRANDINFOID, map.get(CherryConstants.BRANDINFOID));
		String language = (String)map.get(CherryConstants.SESSION_CHERRY_LANGUAGE);
		
		exportMap.put("conditionMap", map);
		
        exportMap.put("sheetName", CherryUtil.getResourceValue("wp", "BINOLWRSRP02", language, "sheetName"));
        
        String conditionContent = this.getConditionStr(map);
		if(conditionContent != null && !"".equals(conditionContent)) {
			exportMap.put("header", conditionContent);
		}
        
        List<String[]> titleRowList = new ArrayList<String[]>();
        titleRowList.add(new String[]{"billCode", CherryUtil.getResourceValue("wp", "BINOLWRSRP02", language, "WRSRP02_billCode"), "30", "", ""});
        titleRowList.add(new String[]{"billCodePre", CherryUtil.getResourceValue("wp", "BINOLWRSRP02", language, "WRSRP02_billCodePre"), "30", "", ""});
        titleRowList.add(new String[]{"departCode", CherryUtil.getResourceValue("wp", "BINOLWRSRP02", language, "WRSRP02_departCode"), "20", "", ""});
        titleRowList.add(new String[]{"departName", CherryUtil.getResourceValue("wp", "BINOLWRSRP02", language, "WRSRP02_departName"), "20", "", ""});
        titleRowList.add(new String[]{"employeeCode", CherryUtil.getResourceValue("wp", "BINOLWRSRP02", language, "WRSRP02_employeeId"), "20", "", ""});
        titleRowList.add(new String[]{"employeeName", CherryUtil.getResourceValue("wp", "BINOLWRSRP02", language, "WRSRP02_employeeName"), "20", "", ""});
        titleRowList.add(new String[]{"memCode", CherryUtil.getResourceValue("wp", "BINOLWRSRP02", language, "WRSRP02_memCode"), "20", "", ""});
        titleRowList.add(new String[]{"memberName", CherryUtil.getResourceValue("wp", "BINOLWRSRP02", language, "WRSRP02_memberName"), "20", "", ""});
        titleRowList.add(new String[]{"unitCode", CherryUtil.getResourceValue("wp", "BINOLWRSRP02", language, "WRSRP02_unitCode"), "20", "", ""});
        titleRowList.add(new String[]{"barCode", CherryUtil.getResourceValue("wp", "BINOLWRSRP02", language, "WRSRP02_barCode"), "20", "", ""});
        titleRowList.add(new String[]{"productName", CherryUtil.getResourceValue("wp", "BINOLWRSRP02", language, "WRSRP02_productName"), "20", "", ""});
        titleRowList.add(new String[]{"sName", CherryUtil.getResourceValue("wp", "BINOLWRSRP02", language, "WRSRP02_sName"), "20", "", ""});
        titleRowList.add(new String[]{"mName", CherryUtil.getResourceValue("wp", "BINOLWRSRP02", language, "WRSRP02_mName"), "20", "", ""});
        titleRowList.add(new String[]{"bName", CherryUtil.getResourceValue("wp", "BINOLWRSRP02", language, "WRSRP02_bName"), "20", "", ""});
        titleRowList.add(new String[]{"buyQuantity", CherryUtil.getResourceValue("wp", "BINOLWRSRP02", language, "WRSRP02_quantity"), "15", "int", ""});
        titleRowList.add(new String[]{"buyAmount", CherryUtil.getResourceValue("wp", "BINOLWRSRP02", language, "WRSRP02_amount"), "15", "float", ""});
        titleRowList.add(new String[]{"discount", CherryUtil.getResourceValue("wp", "BINOLWRSRP02", language, "WRSRP02_discount"), "15", "float", ""});
        titleRowList.add(new String[]{"saleType", CherryUtil.getResourceValue("wp", "BINOLWRSRP02", language, "WRSRP02_saleType"), "15", "", "1055"});
        titleRowList.add(new String[]{"saleTypeDetail", CherryUtil.getResourceValue("wp", "BINOLWRSRP02", language, "WRSRP02_saleTypeDetail"), "15", "", "1136"});
        titleRowList.add(new String[]{"inActivity", CherryUtil.getResourceValue("wp", "BINOLWRSRP02", language, "WRSRP02_inActivity"), "20", "", ""});
        titleRowList.add(new String[]{"saleTime", CherryUtil.getResourceValue("wp", "BINOLWRSRP02", language, "WRSRP02_saleTime"), "20", "", ""});
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
        String downloadFileName = CherryUtil.getResourceValue("wp", "BINOLWRSRP02", language, "downloadFileName");
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
		
		// 销售单号
		String billCode = (String)map.get("billCode");
		if(billCode != null && !"".equals(billCode)) {
			String paramName = CherryUtil.getResourceValue("wp", "BINOLWRSRP02", language, "WRSRP02_billCode");
			String paramValue = billCode;
			condition.append(paramName + "：" + paramValue + "\0\0\0\0\0");
		}
		
		// 流水号
		String saleRecordCode = (String)map.get("saleRecordCode");
		if(saleRecordCode != null && !"".equals(saleRecordCode)) {
			String paramName = CherryUtil.getResourceValue("wp", "BINOLWRSRP02", language, "WRSRP02_saleRecordCode");
			String paramValue = saleRecordCode;
			condition.append(paramName + "：" + paramValue + "\0\0\0\0\0");
		}
		
		// 营业员
		String employeeName = (String)map.get("employeeName");
		if(employeeName != null && !"".equals(employeeName)) {
			String paramName = CherryUtil.getResourceValue("wp", "BINOLWRSRP02", language, "WRSRP02_employeeId");
			String paramValue = employeeName;
			condition.append(paramName + "：" + paramValue + "\0\0\0\0\0");
		}
		
		// 活动名称
		String campaignName = (String)map.get("campaignName");
		if(campaignName != null && !"".equals(campaignName)) {
			String paramName = CherryUtil.getResourceValue("wp", "BINOLWRSRP02", language, "WRSRP02_campaignCode");
			String paramValue = campaignName;
			condition.append(paramName + "：" + paramValue + "\0\0\0\0\0");
		}
		
		// 会员卡号
		String memCode = (String)map.get("memCode");
		if(memCode != null && !"".equals(memCode)) {
			String paramName = CherryUtil.getResourceValue("wp", "BINOLWRSRP02", language, "WRSRP02_memCode");
			String paramValue = memCode;
			condition.append(paramName + "：" + paramValue + "\0\0\0\0\0");
		}
		
		// 销售日期
		String saleDateStart = (String)map.get("startDate");
		String saleDateEnd = (String)map.get("endDate");
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
		String saleDateStr = saleDateCondition.toString();
		if(saleDateStr != null && !"".equals(saleDateStr)) {
			String paramName = CherryUtil.getResourceValue("wp", "BINOLWRSRP02", language, "WRSRP02_saleDate");
			String paramValue = saleDateStr;
			condition.append(paramName + "：" + paramValue + "\0\0\0\0\0");
		}
		
		// 交易类型
		String saleType = (String)map.get("saleType");
		if(saleType != null && !"".equals(saleType)) {
			String paramName = CherryUtil.getResourceValue("wp", "BINOLWRSRP02", language, "WRSRP02_saleType");
			String paramValue = code.getVal("1055", saleType);
			condition.append(paramName + "：" + paramValue + "\0\0\0\0\0");
		}
		
		// 消费者类型
		String consumerType = (String)map.get("consumerType");
		if(consumerType != null && !"".equals(consumerType)) {
			String paramName = CherryUtil.getResourceValue("wp", "BINOLWRSRP02", language, "WRSRP02_consumerType");
			String paramValue = code.getVal("1105", consumerType);
			condition.append(paramName + "：" + paramValue + "\0\0\0\0\0");
		}
		
		// 支付方式
		String payTypeCode = (String)map.get("payTypeCode");
		if(payTypeCode != null && !"".equals(payTypeCode)) {
			String paramName = CherryUtil.getResourceValue("wp", "BINOLWRSRP02", language, "WRSRP02_payTypeCode");
			String paramValue = code.getVal("1175", payTypeCode);
			condition.append(paramName + "：" + paramValue + "\0\0\0\0\0");
		}
		
		// 单据类型
		String ticketType = (String)map.get("ticketType");
		if(ticketType != null && !"".equals(ticketType)) {
			String paramName = CherryUtil.getResourceValue("wp", "BINOLWRSRP02", language, "WRSRP02_ticketType");
			String paramValue = code.getVal("1261", ticketType);
			condition.append(paramName + "：" + paramValue + "\0\0\0\0\0");
		}
		
		return condition.toString();
	}
	
}
