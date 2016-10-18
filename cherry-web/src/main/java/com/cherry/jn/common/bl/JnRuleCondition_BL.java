/*
 * @(#)JnRuleCondition_BL.java     1.0 2011/05/16
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
package com.cherry.jn.common.bl;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.cherry.cp.common.dto.CampRuleConditionDTO;
import com.cherry.jn.common.interfaces.JnRuleCondition_IF;

/**
 * 会员入会规则条件明细
 * 
 * @author hub
 * @version 1.0 2011.05.16
 */
public class JnRuleCondition_BL implements JnRuleCondition_IF{
	
	/**
	 * 运行指定名称的方法
	 * 
	 * @param String
	 *            指定的方法名
	 * @param Map
	 *            参数集合
	 * 
	 */
	private void invokeMd(String mdName, Map<String, Object> map, List<CampRuleConditionDTO> list) {
		try {
			Method[] mdArr = this.getClass().getMethods();
			for (Method method : mdArr) {
				if (method.getName().equals(mdName)) {
					method.invoke(this, map, list);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 创建会员入会规则条件明细List
	 * 
	 * 
	 * @param List
	 *            模板List
	 * @return 会员入会规则条件明细List
	 *  
	 */
	@Override
	public List<CampRuleConditionDTO> createJnRuleConditionList(
			List<Map<String, Object>> camTempList) {
		if (null != camTempList) {
			// 会员入会规则条件明细List
			List<CampRuleConditionDTO> jnRuleConditionList = new ArrayList<CampRuleConditionDTO>();
			// 循环模板List
			for (Map<String, Object> camTemp : camTempList) {
				// 模板编号
				String tempCode = (String) camTemp.get("tempCode");
				invokeMd(tempCode + "_condition", camTemp, jnRuleConditionList);
			}
			return jnRuleConditionList;
		}
		return null;
	}
	
	/**
	 * 单次购买规则
	 * 
	 * @param Map
	 *            模板提交的参数
	 * @param Map
	 *            关系
	 * @return String 规则
	 * 
	 */
	@SuppressWarnings("unchecked")
	public void BUS000001_condition(Map<String, Object> map, List<CampRuleConditionDTO> list) {
		if (null != map && null != list) {
			// 选中状态
			String isChecked = (String) map.get("isChecked");
			if (null != isChecked && !"".equals(isChecked)) {
				List<Map<String, Object>> combTempList = (List<Map<String, Object>>) map
				.get("combTemps");
				if (null != combTempList) {
					for (Map<String, Object> combTemp : combTempList) {
						// 模板编号
						String tempCode = (String) combTemp.get("tempCode");
						invokeMd(tempCode + "_condition", combTemp, list);
					}
				}
			}
		}
	}
	
	/**
	 * 累积购买规则
	 * 
	 * @param Map
	 *            模板提交的参数
	 * @param Map
	 *            关系
	 * @return String 规则
	 * 
	 */
	@SuppressWarnings("unchecked")
	public void BUS000002_condition(Map<String, Object> map, List<CampRuleConditionDTO> list) {
		if (null != map && null != list) {
			// 选中状态
			String isChecked = (String) map.get("isChecked");
			if (null != isChecked && !"".equals(isChecked)) {
				List<Map<String, Object>> combTempList = (List<Map<String, Object>>) map
				.get("combTemps");
				if (null != combTempList) {
					for (Map<String, Object> combTemp : combTempList) {
						// 模板编号
						String tempCode = (String) combTemp.get("tempCode");
						invokeMd(tempCode + "_condition", combTemp, list);
					}
				}
			}
		}
	}
	
	/**
	 * 累积时间模板
	 * 
	 * @param Map
	 *            模板提交的参数
	 * @return boolean 验证结果
	 * 
	 */
	public void BASE000003_condition(Map<String, Object> map, List<CampRuleConditionDTO> list) {
		if (null != map && null != list) {
			// 月数
			String monthNum = (String) map.get("BASE000003_monthNum");
			if (null != monthNum && !"".equals(monthNum)) {
				// 基础属性
				CampRuleConditionDTO camRuleConditionDTO = new CampRuleConditionDTO();
//				// 条件名称
//				camRuleConditionDTO.setCondition("累积时间");
//				// 属性条件
//				camRuleConditionDTO.setPropertyName("MonthNum");
//				// 属性字段类型
//				camRuleConditionDTO.setFieldType(3);
				// 属性值
				camRuleConditionDTO.setBasePropValue1(monthNum);
				list.add(camRuleConditionDTO);
			}
		}
	}
}
