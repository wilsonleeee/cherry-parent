/*
 * @(#)BINOLCM36_BL.java     1.0 2013/04/08
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
package com.cherry.cm.cmbussiness.bl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.cmbussiness.service.BINOLCM36_Service;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryConstants.MEMINFOFIELD;

/**
 * 会员信息修改履历管理BL
 * 
 * @author WangCT
 * @version 1.0 2013/04/08
 */
public class BINOLCM36_BL {
	
	/** 会员信息修改履历管理Service **/
	@Resource
	private BINOLCM36_Service binOLCM36_Service;
	
	/** 取得各种业务类型的单据流水号  **/
	@Resource
	private BINOLCM03_BL binOLCM03_BL;
	
	/**
	 * 添加会员信息修改履历
	 * 
	 * @param map 添加内容
	 */
	public void addMemberInfoRecord(Map<String, Object> map) {
		
		// 取得会员转柜批次号
		String batchNo = binOLCM03_BL.getTicketNumber(Integer.parseInt(map.get("organizationInfoId").toString()), 
				Integer.parseInt(map.get("brandInfoId").toString()), "", CherryConstants.MSG_MEMBER_MF);
		map.put("batchNo", batchNo);
		
		// 添加会员信息修改履历主表
		int memInfoRecordId = binOLCM36_Service.addMemInfoRecord(map);
		
		// 取得修改前会员信息
		Map<String, Object> oldMemInfo = (Map)map.get("oldMemInfo");
		if(oldMemInfo != null) {
			// 会员信息修改履历明细List
			List<Map<String, Object>> memInfoRecordDetailList = new ArrayList<Map<String,Object>>();
			// 判断会员基本属性是否变更，如果变更记录变更明细
			MEMINFOFIELD[] meminfofields = CherryConstants.MEMINFOFIELD.values();
			for(MEMINFOFIELD meminfofield : meminfofields) {
				String code = meminfofield.getCode();
				String name = meminfofield.getName();
				String type = meminfofield.getType();
				Object oldValue = oldMemInfo.get(name);
				Object newValue = map.get(name);
				if("birthYearGetType".equals(name)) {
					if(oldValue == null || "".equals(oldValue.toString())) {
						oldValue = 0;
					}
					if(newValue == null || "".equals(newValue.toString())) {
						newValue = 0;
					}
				}
				boolean isEqual = true;
				if(oldValue != null && !"".equals(oldValue.toString())) {
					if(newValue != null && !"".equals(newValue.toString())) {
						if("string".equals(type)) {
							if(!oldValue.toString().equals(newValue.toString())) {
								isEqual = false;
							}
						} else if("number".equals(type)) {
							if(Double.parseDouble(oldValue.toString()) != Double.parseDouble(newValue.toString())) {
								isEqual = false;
							}
						}
					} else {
						isEqual = false;
					}
				} else {
					if(newValue != null && !"".equals(newValue.toString())) {
						isEqual = false;
					}
				}
				if(!isEqual) {
					Map<String, Object> memInfoRecordDetailMap = new HashMap<String, Object>();
					memInfoRecordDetailMap.put("memInfoRecordId", memInfoRecordId);
					memInfoRecordDetailMap.put("modifyField", code);
					memInfoRecordDetailMap.put("oldValue", oldValue);
					memInfoRecordDetailMap.put("newValue", newValue);
					memInfoRecordDetailList.add(memInfoRecordDetailMap);
				}
			}
			if(memInfoRecordDetailList != null && !memInfoRecordDetailList.isEmpty()) {
				// 添加会员信息修改履历明细表
				binOLCM36_Service.addMemInfoRecordDetail(memInfoRecordDetailList);
			}
		}
	}
	
	/**
	 * 查询会员信息
	 * 
	 * @param map 查询条件
	 * @return 会员信息
	 */
	public Map<String, Object> getMemberInfo(Map<String, Object> map) {
		Map<String, Object> memberInfo = binOLCM36_Service.getMemberInfo(map);
		if(memberInfo != null) {
			List<Map<String, Object>> memberAddress = binOLCM36_Service.getMemberAddress(map);
			if(memberAddress != null && !memberAddress.isEmpty()) {
				memberInfo.putAll(memberAddress.get(0));
			}
		}
		return memberInfo;
	}

}
