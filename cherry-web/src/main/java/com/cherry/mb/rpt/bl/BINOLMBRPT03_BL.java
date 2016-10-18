package com.cherry.mb.rpt.bl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.cmbussiness.interfaces.BINOLCM37_IF;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.util.CherryUtil;
import com.cherry.mb.rpt.service.BINOLMBRPT01_Service;

/**
 * 会员等级统计BL
 * 
 * @author WangCT
 * @version 1.0 2014/11/13
 */
public class BINOLMBRPT03_BL implements BINOLCM37_IF {
	
	/** 会员销售报表Service **/
	@Resource
	private BINOLMBRPT01_Service binOLMBRPT01_Service;
	
	/**
	 * 会员等级统计
	 * 
	 * @param map 检索条件
	 * @return 统计结果List
	 */
	public List<Map<String, Object>> getMemLevelCount(Map<String, Object> map) {
		List<Map<String, Object>> memLevelCountList = binOLMBRPT01_Service.getMemLevelCount(map);
		if(memLevelCountList != null && !memLevelCountList.isEmpty()) {
			// 购买会员总人数
			int memTotalCount = 0;
			// 购买总金额
			BigDecimal totalAmount = new BigDecimal(0);
			for(int i = 0; i < memLevelCountList.size(); i++) {
				Map<String, Object> memLevelCountMap = memLevelCountList.get(i);
				Integer memCount = (Integer)memLevelCountMap.get("memCount");
				if(memCount != null) {
					memTotalCount += memCount;
				}
				BigDecimal amount = (BigDecimal)memLevelCountMap.get("amount");
				if(amount != null) {
					totalAmount = totalAmount.add(amount);
				}
			}
			for(int i = 0; i < memLevelCountList.size(); i++) {
				Map<String, Object> memLevelCountMap = memLevelCountList.get(i);
				Integer memCount = (Integer)memLevelCountMap.get("memCount");
				BigDecimal amount = (BigDecimal)memLevelCountMap.get("amount");
				if(memCount == null) {
					memCount = 0;
				}
				if(amount == null) {
					amount = new BigDecimal(0);
				}
				if(memTotalCount > 0) {
					// 购买会员人数占比
					memLevelCountMap.put("memCountRate", CherryUtil.percent(memCount, memTotalCount, 2));
				}
				if(totalAmount.doubleValue() > 0) {
					// 购买金额占比
					memLevelCountMap.put("amountRate", CherryUtil.percent(amount.doubleValue(), totalAmount.doubleValue(), 2));
				}
			}
		}
		return memLevelCountList;
	}

	@Override
	public List<Map<String, Object>> getDataList(Map<String, Object> map)
			throws Exception {
		return this.getMemLevelCount(map);
	}
	
	public Map<String, Object> getExportParam(Map<String, Object> map) {
		
		Map<String, Object> exportMap = new HashMap<String, Object>();
		exportMap.put(CherryConstants.ORGANIZATIONINFOID, map.get(CherryConstants.ORGANIZATIONINFOID));
		exportMap.put(CherryConstants.BRANDINFOID, map.get(CherryConstants.BRANDINFOID));
		String language = (String)map.get(CherryConstants.SESSION_CHERRY_LANGUAGE);
		
		exportMap.put("conditionMap", map);
		
        exportMap.put("sheetName", CherryUtil.getResourceValue("mb", "BINOLMBRPT03", language, "sheetName"));
        
        String conditionContent = this.getConditionStr(map);
		if(conditionContent != null && !"".equals(conditionContent)) {
			exportMap.put("header", conditionContent);
		}
        
        List<String[]> titleRowList = new ArrayList<String[]>();
        titleRowList.add(new String[]{"levelName", CherryUtil.getResourceValue("mb", "BINOLMBRPT03", language, "mbrpt_levelName"), "20", "", ""});
        titleRowList.add(new String[]{"memCount", CherryUtil.getResourceValue("mb", "BINOLMBRPT03", language, "mbrpt_memCount"), "20", "int", ""});
        titleRowList.add(new String[]{"amount", CherryUtil.getResourceValue("mb", "BINOLMBRPT03", language, "mbrpt_amount"), "20", "float", ""});
        titleRowList.add(new String[]{"memCountRate", CherryUtil.getResourceValue("mb", "BINOLMBRPT03", language, "mbrpt_memCountRate"), "20", "right", ""});
        titleRowList.add(new String[]{"amountRate", CherryUtil.getResourceValue("mb", "BINOLMBRPT03", language, "mbrpt_amountRate"), "20", "right", ""});
        exportMap.put("titleRows", titleRowList.toArray(new String[][]{}));
        
        return exportMap;
	}
	
	private String getConditionStr(Map<String, Object> map) {
		StringBuffer condition = new StringBuffer();
		String language = (String)map.get(CherryConstants.SESSION_CHERRY_LANGUAGE);
		
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
			String paramName = CherryUtil.getResourceValue("mb", "BINOLMBRPT03", language, "mbrpt_saleTime");
			condition.append(paramName + "：" + paramValue + "\0\0\0\0\0");
		}
		
		String channelName = (String)map.get("channelName");
		if(channelName != null && !"".equals(channelName)) {
			String paramName = CherryUtil.getResourceValue("mb", "BINOLMBRPT03", language, "mbrpt_channelId");
			paramValue = channelName;
			condition.append(paramName + "：" + paramValue + "\0\0\0\0\0");
		}
		
		String organizationName = (String)map.get("organizationName");
		if(organizationName != null && !"".equals(organizationName)) {
			String paramName = CherryUtil.getResourceValue("mb", "BINOLMBRPT03", language, "mbrpt_organizationId");
			paramValue = organizationName;
			condition.append(paramName + "：" + paramValue + "\0\0\0\0\0");
		}
		
		return condition.toString();
	}

}
