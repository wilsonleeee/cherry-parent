/*
 * @(#)BINOLMBMBM16_BL.java     1.0 2013/05/20
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

import javax.annotation.Resource;

import com.cherry.cm.cmbussiness.service.BINOLCM31_Service;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.mb.mbm.service.BINOLMBMBM16_Service;

/**
 * 会员升降级履历
 * 
 * @author Luohong
 * @version 1.0 2013/05/20
 */
public class BINOLMBMBM16_BL {
	/**
	 * 会员升降级履历Service
	 */
	@Resource
	private BINOLMBMBM16_Service binOLMBMBM16_Service;

	@Resource
	private BINOLCM31_Service binOLCM31_Service;

	/**
	 * 会员升降级履历List
	 * 
	 * @param map
	 *            检索条件
	 * @return 会员升降级履历List
	 */
	public List<Map<String, Object>> getMemLevelList(Map<String, Object> map) {
		//会员升降级履历List
		List<Map<String, Object>> ruleList = binOLMBMBM16_Service
				.getMemLevelList(map);
		// 取得会员升级级信息
		getleveList(map, ruleList);
		// 取得原因List
		getReasonCom(ruleList);
		// 查询会员销售信息List
		return ruleList;
	}

	/**
	 * 取得会员等级List
	 * 
	 * @param map
	 * @param ruleList
	 * @return
	 */
	public List<Map<String, Object>> getleveList(Map<String, Object> map,
			List<Map<String, Object>> ruleList) {
		if (null != ruleList) {
			for (Map<String, Object> ruleMap : ruleList) {
				// 变化前等级Id
				String oldLevelId = ConvertUtil.getString(ruleMap
						.get("oldValue"));
				// 变化后等级Id
				String newLevelId = ConvertUtil.getString(ruleMap
						.get("newValue"));
				// 会员等级List
				List<Map<String, Object>> leveList = binOLCM31_Service
						.getMemberLevelList(map);
				if (null != leveList) {
					for (Map<String, Object> levelMap : leveList) {
						String levelId = ConvertUtil.getString(levelMap
								.get("memberLevelId"));
						if (oldLevelId.equals(levelId)) {
							ruleMap.put("oldLevelName",
									levelMap.get("levelName"));
						}
						if (newLevelId.equals(levelId)) {
							ruleMap.put("newLevelName",
									levelMap.get("levelName"));
						}
					}
				}
				// 取得规则原因Id
				Map<String, Object> rulesMap = getArray(ruleMap, "ruleId");
				String ruleId = ConvertUtil.getString(rulesMap.get("ruleId"));
				if (!"".equals(ruleId)) {
					// 规则原因List
					List<Map<String, Object>> reasonList = binOLMBMBM16_Service
							.getLevelReasonList(rulesMap);
					if (null != reasonList && reasonList.size() > 0) {
						ruleMap.put("reasonList", reasonList);
					}
				} else {
					ruleMap.putAll(map);
					// 取得手动修改的原因
					List<Map<String, Object>> msList = binOLMBMBM16_Service
							.getReasonList(ruleMap);
					if (null != msList && msList.size() > 0) {
						for (Map<String, Object> msMap : msList) {
							List<Map<String, Object>> arrayList = new ArrayList<Map<String, Object>>();
							// 手动变化后的等级
							String memberLevel = ConvertUtil.getString(msMap
									.get("memberLevel"));
							if (newLevelId.equals(memberLevel)) {
								arrayList.add(msMap);
								ruleMap.put("reasonList",arrayList);
							}
						}
					}
				}
				// 取得匹配规则名称
				Map<String, Object> campNameMap = getArray(ruleMap,
						"subCampCode");
				String subCampCode = ConvertUtil.getString(rulesMap
						.get("subCampCode"));
				if (map.containsKey("memberClubId")) {
					campNameMap.put("memberClubId", map.get("memberClubId"));
				}
				if (!"".equals(subCampCode)) {
					// 规则原因List
					List<Map<String, Object>> campRuleList = binOLMBMBM16_Service
							.getCampNameList(campNameMap);
					if (null != campRuleList && campRuleList.size()>0) {
						ruleMap.put("campRuleList", campRuleList);
					}
				}
			}
		}
		return ruleList;
	}

	/**
	 * string转数组
	 * 
	 * @param infoMap
	 * @param key
	 * @return
	 */
	private Map<String, Object> getArray(Map<String, Object> infoMap, String key) {
		// 会员规则Id
		Object value = infoMap.get(key);
		if (value != null) {
			List<String> _value = new ArrayList<String>();
			if (value instanceof String) {
				if (!"".equals(value.toString())) {
					_value.add(value.toString());
				}
			} else {
				_value = (List) value;
			}
			infoMap.put(key, _value);
		}
		return infoMap;
	}
	/**
	 * 截取文字超过固定长度
	 * @param list
	 */
	private void getReasonCom(List<Map<String, Object>> list) {
		// 取得会员修改状态List
		if(null != list && list.size() > 0){
			for(Map<String, Object> rulemap:list){
				List<Map<String, Object>> reasonList = (List<Map<String, Object>>) rulemap.get("reasonList");
				if (null != reasonList && reasonList.size() > 0) {
					int size = reasonList.size();
					for (int i = 0; i < size; i++) {
						Map<String, Object> temp = reasonList.get(i);
						// 取得备注信息
						String messageReason = (String) temp.get("reason");
						if (null == messageReason) {
							messageReason = "";
						}
						String messageReason_temp = "";
						char[] messageReasonArr = messageReason.toCharArray();
						// 取得备注信息的长度
						int messageBodyLength = messageReasonArr.length;
						//
						int count = 0;
						label2: for (int j = 0; j < messageBodyLength; j++) {
							// 控制在15个汉字长度之内
							if (count > 20) {
								messageReason_temp = messageReason.substring(0, j)
										+ " ...";
								break label2;
							}
							// 如果是汉字则加2，否则加1
							if (messageReasonArr[j] >= 0x0391
									&& messageReasonArr[j] <= 0xFFE5) {
								count += 2;
							} else {
								count++;
							}
						}
						if ("".equals(messageReason_temp)) {
							messageReason_temp = messageReason;
						}
						temp.put("messageReason_temp", messageReason_temp);
					}
				}
			}
		}
	}
}
