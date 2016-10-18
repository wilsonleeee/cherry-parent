/*	
 * @(#)CampUtil.java     1.0 2011/11/01		
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
package com.cherry.cp.common.util;


/**
 * 会员活动工具类
 * 
 * @author hub
 * @version 1.0 2011.11.01
 */
public class CampUtil {
	
	/** 字段错误信息 */
	public static final int ERROR_LEVEL_1 = 1;
	
	/** Action错误信息 */
	public static final int ERROR_LEVEL_2 = 2;
	
	/** 查询SQL的ID */
	public static final String SQLID_KEY = "SQLID";
	
	/** 查询SQL的返回字段 */
	public static final String SEARCHNAME_KEY = "SEARCHNAME";
	
	/** 测试用 */
	public static final int TESTFLAG_1 = 1;
	
	/** 测试结果：成功 */
	public static final int RESULTFLAG_S = 0;
	
	/** 测试结果：失败 */
	public static final int RESULTFLAG_E = -1;
	
	/** 顿号 */
	public static final String MARK_COMMA = "、";
	
	/** 空格 */
	public static final String SPACE = " ";
	
	/** 全角逗号 */
	public static final String FULL_COMMA = "，";
	
	/** 半角逗号 */
	public static final String HALF_COMMA = ",";
	
	/** 半角点号 */
	public static final String HALF_DOTS = ".";
	
	/** 半角引号 */
	public static final String HALF_QUOTE = "\"";
	
	/** 左括号 */
	public static final String LEFT_BRACKET = "(";
	
	/** 右括号 */
	public static final String RIGHT_BRACKET = ")";
	
	/** 验证对象 */
	public static final String CAMP_NAME = "this";
	
	/** 过滤器参数名称 */
	public static final String FILTER_NAME = "\\$f";
	
	/** 规则验证共通方法名 */
	public static final String CHECK_METHOD_NAME = "doCheck";
	
	/** 规则验证条件集合 */
	public static final String CHECK_FILTERS_NAME = "allFilters";
	
	/** 规则名称 */
	public static final String RULE_NAME = "\"\\$\\{RULE_NAME\\}\"";
	
	/** 规则名称正则表达式 */
	public static final String RULE_NAME_REGEX = "\\\\\\$\\\\\\{RULE_NAME\\\\\\}";
	
	/** 验证类: 过滤器共通验证类*/
	public static final String CHECKER_CLASS1 = "RuleFilterUtil";
	
	/** 验证规则是否需要执行 */
	public static final String CHECKER_CLASS2 = "binbedrcom03IF";
	
	/** 验证类: 判断规则是否属于执行队列 */
	public static final String RULEKEYS_CHECK = "ruleKeys contains \"\\$\\{CAMPAIGN_ID\\}\"";
	
	/** 最大执行顺序 */
	public static final int MAX_SALIENCE = 100;
	
	/** 最大执行顺序(活动组) */
	public static final int MAX_SALIENCE_GROUP = -1;
	
	/** 默认规则代号 */
	public static final String GROUPCODE_DEFAULTRULE = "DEFAULTRULE";
	
	/** 组合规则代号 */
	public static final String GROUPCODE_COMBRULE = "COMBRULE";
	
	/**  */
	public static enum TEMPLATE_BYTYPE {
		template1("1", "jon/template_jon01", "jonCreateRule"),
		template2("2", "jon/template_jon01", "jonCreateRule"),
		template3("3", "point/template_point01", "pointCreateRule");
		
		public static String getTemplateName(Object key) {
			return getValue(key, 0);
		}
		
		public static String getRuleBeanName(Object key) {
			return getValue(key, 1);
		}
		
		public static String getValue(Object key, int kbn) {
			if(null == key || "".equals(key)) {
				return null;
			}
			TEMPLATE_BYTYPE[] opers = TEMPLATE_BYTYPE.values();
			for(TEMPLATE_BYTYPE oper : opers) {
				if(oper.getKey().equals(key)) {
					if (0 == kbn) {
						return oper.getTemplateName();
					} else if (1 == kbn) {
						return oper.getRuleBeanName();
					}
				}
			}
			return null;
		}
		
		TEMPLATE_BYTYPE(String key, String templateName, String ruleBeanName) {
			this.key = key;
			this.templateName = templateName;
			this.ruleBeanName = ruleBeanName;
		}
		
		public String getKey() {
			return key;
		}

		public String getTemplateName() {
			return templateName;
		}
		
		public String getRuleBeanName() {
			return ruleBeanName;
		}
		
		private String key;
		private String templateName;
		private String ruleBeanName;
	}
	/** 时间 */
	public static final String BASEPROP_TIME = "baseProp_time";
	/** 备货时间 */
	public static final String BASEPROP_STOCKING_TIME = "baseprop_stocking_time";
	/** 预约时间 */
	public static final String BASEPROP_RESE_TIME = "baseProp_rese_time";
	/** 领用时间 */
	public static final String BASEPROP_OBTAIN_TIME = "baseProp_obtain_time";
	/** 区域市 */
	public static final String BASEPROP_CITY = "baseProp_city";
	/** 渠道 */
	public static final String BASEPROP_CHANNAL = "baseProp_channal";
	/** 渠道 */
	public static final String BASEPROP_FACTION = "baseProp_faction";
	/** 柜台 */
	public static final String BASEPROP_COUNTER = "baseProp_counter";
	/** 消费金额 */
	public static final String BASEPROP_AMOUNT = "baseProp_amount";
	/** 购买产品*/
	public static final String BASEPROP_PRODUCT = "baseProp_product";
	/** 会员等级*/
	public static final String BASEPROP_MEMBERLEVEL = "baseProp_mebLevel";
	/** 活动对象 */
	public static final String BASEPROP_CUSTOMER = "baseProp_customer";
	/** 积分值 */
	public static final String BASEPROP_INTEGRAL = "baseProp_integral";
	/** 追加金额 */
	public static final String BASEPROP_ADDAMOUNT = "baseProp_addAmount";
	
	/**  */
	public static enum BASEPROPS {
		baseprop1("baseProp_time", "时间", "7"),
		baseprop2("baseProp_city", "区域市", "3"),
		baseprop3("baseProp_channal", "渠道", "3"),
		baseprop4("baseProp_counter", "柜台", "3"),
		baseprop5("baseProp_amount", "消费金额", "5"),
		baseprop6("baseProp_product", "购买产品", "3"),
		baseprop7("baseProp_mebLevel", "会员等级", "3"),
		baseprop8("baseprop_stocking_time", "备货时间", "7"),
		baseprop9("baseProp_rese_time", "预约时间", "7"),
		baseprop10("baseProp_obtain_time", "领用时间", "7"),
		baseprop11("baseProp_integral", "积分值", "3"),
		baseprop12("baseProp_addAmount", "追加金额", "5"),
		baseprop13("baseProp_customer", "活动对象", "3"),
		baseprop14("baseProp_faction", "所属系统", "3");
		
		public static String getCondition(Object key) {
			return getValue(key, 0);
		}
		
		public static int getFieldType(Object key) {
			String fieldTypeStr = getValue(key, 1);
			return Integer.parseInt(fieldTypeStr);
		}
		private static String getValue(Object key, int kbn) {
			if(null == key || "".equals(key)) {
				return null;
			}
			BASEPROPS[] opers = BASEPROPS.values();
			for(BASEPROPS oper : opers) {
				if(oper.getKey().equals(key)) {
					if (0 == kbn) {
						return oper.getCondition();
					} else if (1 == kbn) {
						return oper.getFieldType();
					}
				}
			}
			return null;
		}
		BASEPROPS(String key, String condition, String fieldType) {
			this.key = key;
			this.condition = condition;
			this.fieldType = fieldType;
		}
		
		public String getKey() {
			return key;
		}
		
		public String getCondition() {
			return condition;
		}

		public void setCondition(String condition) {
			this.condition = condition;
		}
		
		public String getFieldType() {
			return fieldType;
		}

		public void setFieldType(String fieldType) {
			this.fieldType = fieldType;
		}

		private String key;
		private String condition;
		private String fieldType;
	}
}
