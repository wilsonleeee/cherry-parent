/*
 * @(#)BINOLMBMBM28_BL.java     1.0 2013.09.23
 * 
 * Copyright (c) 2010 SHANGHAI BINGKUN DIGITAL TECHNOLOGY CO.,LTD
 * All rights reserved
 * 
 * This software is the confidential and proprietary information of 
 * SHANGHAI BINGKUN.("Confidential Information").  You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with SHANGHAI BINGKUN.
 */
package com.cherry.mb.mbm.bl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import com.cherry.mb.mbm.service.BINOLMBMBM28_Service;

/**
 * 会员问题处理画面BL
 * 
 * @author WangCT
 * @version 1.0 2013.09.23
 */
public class BINOLMBMBM28_BL {
	
	/** 会员问题处理画面Service **/
	@Resource
	private BINOLMBMBM28_Service binOLMBMBM28_Service;
	
	/**
	 * 取得会员问题明细
	 * 
	 * @param map 查询条件
	 * @return 会员问题明细
	 */
	public Map<String, Object> getIssueDetail(Map<String, Object> map) {
		
		// 取得会员问题明细
		Map<String, Object> issueDetailMap = binOLMBMBM28_Service.getIssueDetail(map);
		if(issueDetailMap != null) {
			String description = (String)issueDetailMap.get("description");
			issueDetailMap.put("descriptionHtml", convertStrToHtml(description));
			// 取得会员问题处理信息List
			List<Map<String, Object>> issueActionList = binOLMBMBM28_Service.getIssueActionList(issueDetailMap);
			if(issueActionList != null && !issueActionList.isEmpty()) {
				for(Map<String, Object> issueActionMap : issueActionList) {
					String actionBody = (String)issueActionMap.get("actionBody");
					issueActionMap.put("actionBodyHtml", convertStrToHtml(actionBody));
				}
				issueDetailMap.put("issueActionList", issueActionList);
			}
		}
		return issueDetailMap;
	}
	
	/**
	 * 添加会员问题处理内容
	 * 
	 * @param map 添加内容
	 */
	public void tran_addIssueAction(Map<String, Object> map) {
		
		String issueActionId = (String)map.get("issueActionId");
		if(issueActionId != null && !"".equals(issueActionId)) {
			binOLMBMBM28_Service.updIssueAction(map);
		} else {
			// 添加会员问题处理内容
			binOLMBMBM28_Service.addIssueAction(map);
			
			// 解决结果
			String resolution = (String)map.get("resolutionAdd");
			// 如果解决结果为解决的场合，把问题票更新成已解决状态
			if(resolution != null && !"0".equals(resolution)) {
				map.put("issueStatus", "2");
				// 更新会员问题状态
				binOLMBMBM28_Service.updIssueStatus(map);
			}
		}
	}
	
	/**
	 * 删除会员问题处理内容
	 * 
	 * @param map 删除条件
	 */
	public void tran_delIssueAction(Map<String, Object> map) {
		
		// 删除会员问题处理内容
		binOLMBMBM28_Service.delIssueAction(map);
	}
	
	/**
	 * 删除会员问题
	 * 
	 * @param map 删除条件
	 */
	public void tran_delIssue(Map<String, Object> map) {
		
		// 删除会员问题处理内容
		binOLMBMBM28_Service.delIssue(map);
	}
	
	/**
	 * 取得会员问题明细(编辑用)
	 * 
	 * @param map 查询条件
	 * @return 会员问题明细
	 */
	public Map<String, Object> getIssueEditDetail(Map<String, Object> map) {
		
		// 取得会员问题明细
		return binOLMBMBM28_Service.getIssueDetail(map);
	}
	
	/**
	 * 编辑问题处理
	 * 
	 * @param map 编辑内容
	 */
	public void tran_editIssue(Map<String, Object> map) {
		
		String sysDate = binOLMBMBM28_Service.getSYSDateTime();
		map.put("updateTime", sysDate);
		
		// 解决结果
		String resolution = (String)map.get("resolutionAdd");
		// 变更前解决结果
		String oldResolutionAdd = (String)map.get("oldResolutionAdd");
		// 如果解决结果为未解决那么设置问题状态为待处理，否则设置为已解决
		if(resolution == null || "0".equals(resolution)) {
			map.put("issueStatus", "0");
		} else {
			map.put("issueStatus", "2");
		}
		// 问题从未解决到解决的场合，设置解决时间
		if(oldResolutionAdd == null || "0".equals(oldResolutionAdd)) {
			if(resolution != null && !"0".equals(resolution)) {
				map.put("resolutionDate", sysDate);
			}
		}
		
		// 更新会员问题
		binOLMBMBM28_Service.updIssue(map);
		
		// 解决方案
		String actionBody = (String)map.get("actionBodyAdd");
		// 解决方案不为空的场合添加解决方案
		if(actionBody != null && !"".equals(actionBody)) {
			// 添加会员问题处理内容
			binOLMBMBM28_Service.addIssueAction(map);
		}
	}
	
	/**
	 * 把数据库中的字符串转换成HTML画面显示用的字符串
	 * 
	 * @param str 待转换的字符串
	 * @return HTML画面显示用的字符串
	 */
	public String convertStrToHtml(String str) {
    	if(str != null && !"".equals(str)) {
    		Pattern p = Pattern.compile("QA[\\d]{18}");
    		Matcher m = p.matcher(str);
    		List<String> findStrList = new ArrayList<String>();
    		while (m.find()) {
    			String findStr = m.group();
    			if(!findStrList.contains(findStr)) {
    				str = str.replaceAll(findStr, "<a href=\"#\" class=\"issueLink\">"+findStr+"</a>");
    				findStrList.add(findStr);
    			}
    		}
    		return str.replaceAll("\r\n", "<br/>");
    	}
		return str;
    }

}
