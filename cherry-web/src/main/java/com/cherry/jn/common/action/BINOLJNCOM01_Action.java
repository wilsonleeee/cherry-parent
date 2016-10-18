/*	
 * @(#)BINOLJNCOM01_Action.java     1.0 2011/4/18		
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
package com.cherry.jn.common.action;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryChecker;
import com.googlecode.jsonplugin.JSONUtil;

/**
 * 会员入会共通 Action
 * 
 * @author hub
 * @version 1.0 2011.4.18
 */
public class BINOLJNCOM01_Action extends BaseAction {

	private static final long serialVersionUID = -7878655161607712545L;

	/** 参数验证结果 */
	public boolean isCorrect = true;
	
	/** 单次购买或者累积购买选中 */
	public boolean BUS000001_isChecked = false;

	/** 购买模板索引 */
	private int BASE000002_INDEX;

	/** 累积时间索引 */
	private int BASE000003_INDEX;

	/**
	 * 运行指定名称的方法
	 * 
	 * @param String
	 *            指定的方法名
	 * @param Map
	 *            参数集合
	 * 
	 */
	private void invokeMd(String mdName, Map<String, Object> map) {
		try {
			Method[] mdArr = this.getClass().getMethods();
			for (Method method : mdArr) {
				if (method.getName().equals(mdName)) {
					method.invoke(this, map);
					break;
				}
			}
			// Method md = this.getClass().getMethod(mdName, new
			// Class[]{Map.class});
			// md.invoke(this, map);
		} catch (Exception e) {
			// return;
			e.printStackTrace();
		}
	}

	/**
	 * 验证提交的参数
	 * 
	 * @param String
	 *            活动模板信息
	 * @return boolean 验证结果
	 * 
	 */
	@SuppressWarnings("unchecked")
	public void validateForm(String camTemps) {
		try {
			// 活动模板List
			List<Map<String, Object>> camTempList = (List<Map<String, Object>>) JSONUtil
					.deserialize(String.valueOf(camTemps));
			if (null != camTempList) {
				boolean hasBUS000001 = false;
				for (Map<String, Object> camTemp : camTempList) {
					// 模板编号
					String tempCode = (String) camTemp.get("tempCode");
					tempCode = ("BUS000002".equals(tempCode)) ? "BUS000001"
							: tempCode;
					if ("BUS000001".equals(tempCode)) {
						hasBUS000001 = true;
						// 选中状态
						String isChecked = (String) camTemp.get("isChecked");
						if (!BUS000001_isChecked && null != isChecked && !"".equals(isChecked)) {
							BUS000001_isChecked = true;
						} else {
							continue;
						}
					}
					invokeMd(tempCode + "_check", camTemp);
				}
				if (!hasBUS000001) {
					BUS000001_isChecked = true;
				}
			}
		} catch (Exception e) {
			isCorrect = false;
		}
	}

	/**
	 * 验证单次购买或者累积购买模板
	 * 
	 * @param Map
	 *            模板提交的参数
	 * @return boolean 验证结果
	 * 
	 */
	@SuppressWarnings("unchecked")
	public void BUS000001_check(Map<String, Object> map) {
		if (null != map) {
			if (map.containsKey("combTemps")) {
				List<Map<String, Object>> combTempList = (List<Map<String, Object>>) map
						.get("combTemps");
				if (null != combTempList) {
					for (Map<String, Object> combTemp : combTempList) {
						// 模板编号
						String tempCode = (String) combTemp.get("tempCode");
						invokeMd(tempCode + "_check", combTemp);
					}
				}
			}
		}
	}

	/**
	 * 验证购买模板
	 * 
	 * @param Map
	 *            模板提交的参数
	 * @return boolean 验证结果
	 * 
	 */
	public void BASE000002_check(Map<String, Object> map) {
		if (null != map) {
			// 最低消费
			String minAmount = (String) map.get("BASE000002_minAmount");
			// 最高消费
			String maxAmount = (String) map.get("BASE000002_maxAmount");
			if (null != minAmount && !"".equals(minAmount)) {
				if (!CherryChecker.isFloatValid(minAmount, 10, 2)) {
					this.addFieldError("BASE000002_minAmount-"
							+ BASE000002_INDEX, getText("ECM00024",
							new String[] { "最低消费", "10", "2" }));
					isCorrect = false;
				}
			}
			if (null != maxAmount && !"".equals(maxAmount)) {
				if (!CherryChecker.isFloatValid(maxAmount, 10, 2)) {
					this.addFieldError("BASE000002_maxAmount-"
							+ BASE000002_INDEX, getText("ECM00024",
							new String[] { "最高消费", "10", "2" }));
					isCorrect = false;
				}
			}
			BASE000002_INDEX++;
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
	public void BASE000003_check(Map<String, Object> map) {
		if (null != map) {
			// 月数
			String monthNum = (String) map.get("BASE000003_monthNum");
			// 月数验证
			if (null != monthNum && !"".equals(monthNum)) {
				if (monthNum.length() > 3) {
					this.addFieldError("BASE000003_monthNum-"
							+ BASE000003_INDEX, getText("ECM00020",
							new String[] { "月数", "3" }));
					isCorrect = false;
				}
				// 是否为数字
				if (!CherryChecker.isNumeric(monthNum)) {
					this.addFieldError("BASE000003_monthNum-"
							+ BASE000003_INDEX, getText("ECM00021",
							new String[] { "月数" }));
					isCorrect = false;
				}
			}
			BASE000003_INDEX++;
		}
	}
}
