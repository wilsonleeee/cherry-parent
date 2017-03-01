/*  
 * @(#)PromotionConstants.java     1.0 2011/05/31      
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
package com.cherry.ss.common;

public class PromotionConstants {
	/** 促销活动地点选择类型--按区域 */
	public static final String LOTION_TYPE_REGION = "1";
	
	/** 促销活动地点选择类型--按区域并且指定柜台 */
	public static final String LOTION_TYPE_REGION_COUNTER = "2";
	
	/** 促销活动地点选择类型--按渠道*/
	public static final String LOTION_TYPE_CHANNELS= "3";
	
	/** 促销活动地点选择类型--按渠道并且指定柜台*/
	public static final String LOTION_TYPE_CHANNELS_COUNTER= "4";
	
	/** 促销活动地点选择类型--全部柜台*/
	public static final String LOTION_TYPE_ALL_COUNTER= "5";
	
	/** 促销活动地点选择类型--导入柜台*/
	public static final String LOTION_TYPE_IMPORT_COUNTER= "6";
	
	public static final String LOTION_TYPE_7= "7";
	
	public static final String LOTION_TYPE_8= "8";
	
	/** 促销活动地点选择类型--按组织结构*/
	public static final String LOTION_TYPE_ORGANIZATION= "9";
	
	/** 促销活动地点选择类型--按组织结构并且指定柜台*/
	public static final String LOTION_TYPE_ORGANIZATION_COUNTER= "10";
	
	/** 促销活动基础条件 --时间 */
	public static final String BASE_PROP_TIME = "baseProp_time";
	
	/** 促销活动基础条件 --区域市 */
	public static final String BASE_PROP_CITY = "baseProp_city";
	
	/** 促销活动基础条件 --渠道*/
	public static final String BASE_PROP_CHANNEL = "baseProp_channal";
	
	public static final String BASEPROP_FACTION = "baseProp_faction";
	
	/** 促销活动基础条件 --柜台*/
	public static final String BASE_PROP_COUNTER = "baseProp_counter";
	
	/** 促销活动基础条件 --组织*/
	public static final String BASE_PROP_ORGANIZATION ="baseProp_organization";
	
	/** 促销活动基础条件--消费金额 */
	public static final String BASE_PROP_AMOUNT = "baseProp_amount";
	
	/** 促销活动基础条件--购买产品 */
	public static final String BASE_PROP_PRODUCT = "baseProp_product";
	
	/** 促销活动终端下发延迟时间 */
	public static final String PROMOTION_PUBLIC_TIMELAG = "24";
	
	/** 促销活动模糊查询最大查询个数*/
	public static final String PROMOTION_INDSEARCH_COUNT = "15";
	
	/** 促销活动套装折扣产品条码*/
	public static final String PROMOTION_TZZK_UNIT_CODE = "TZZK999999";
	
	/** 促销活动套装折扣类别码*/
	public static final String PROMOTION_TZZK_TYPE_CODE = "TZZK";
	
	/** 促销活动套装折扣名称*/
	public static final String PROMOTION_TZZK_NAME = "套装折扣";
	
	/** 促销活动积分兑礼类别码*/
	public static final String PROMOTION_DHCP_TYPE_CODE = "DHCP";
	
	/** 促销活动积分兑现类别码*/
	public static final String PROMOTION_DHMY_TYPE_CODE = "DHMY";
	
	/** 促销活动积分兑礼名称*/
	public static final String PROMOTION_DHCP_NAME = "积分兑礼";
	
	/** 促销活动促销礼品类别码*/
	public static final String PROMOTION_CXLP_TYPE_CODE = "CXLP";
	
	/** 促销活动类型-促销活动*/
	public static final String PROMOTION_CXHD_TYPE_CODE = "CXHD";
	
	/** 促销活动类型-兑换活动*/
	public static final String PROMOTION_DHHD_TYPE_CODE = "DHHD";
	
	/** 促销活动促销礼品名称*/
	public static final String PROMOTION_CXLP_NAME = "促销礼品";

	/** 促销活动促销礼品类别码*/
	public static final String PROMOTION_ZDTL_TYPE_CODE = "ZDTL";
	
	/** 促销活动促销礼品名称*/
	public static final String PROMOTION_ZDTL_NAME = "整单去零";
	/** 默认结束时间 */
	public static final String DEFAULT_END_DATE = "2020-01-01";
	
	/** 促销活动查询时间间隔 */
	public static final String PRM_ACT_SPACE_OF_TIME = "365";
	
	/** mangoDB 促销规则DRL表名*/
	public static final String PRM_RULE_COLL_NAME = "MGO_PrmRuleDetail";
	
	/** mangoDB 促销规则详细定义表名*/
	public static final String PRM_RULE_HEAD_NAME = "MGO_PrmRuleHeader";
	
	/** 分类类型 大分类*/
	public static final String CategoryType_primary = "0";
	
	/** 分类类型 中分类*/
	public static final String CategoryType_secondry = "1";

	/** 分类类型 小分类*/
	public static final String CategoryType_small = "2";
	
	public static final String PRMVENDORID = "prmVendorId";
	
	public static final String PRMCATE ="prmCate";
	
	public static final String GROUPTYPE ="groupType";
	
	public static final String GROUPTYPE_1 ="1";
	
	public static final String GROUPTYPE_2 ="2";
	
	public static final String GROUPTYPE_3 ="3";
	
	public static final String PRICE ="price";
	
	public static final String OLD_PRICE ="oldPrice";
	
	public static final String EX_POINT ="exPoint";
	
	public static final String QUANTITY = "quantity";

	/** ajax 返回code */
	public static final String RESULT_CODE = "resultCode";

	/** ajax 返回result */
	public static final String RESULT_MESSAGE = "resultMsg";

	/** ajax返回 成功件数 */
	public static final String RESULT_SUCCESS_COUNT = "successCount";

	/** ajax返回 失败件数 */
	public static final String RESULT_FAIL_COUNT = "failCount";

	/** 导入批次号 */
	public static final String SEARCH_CODE = "searchCode";

	/** ajax 返回导入全部成功 */
	public static final String RESULT_CODE_0 = "0";

	/** ajax 返回导入有失败件数 */
	public static final String RESULT_CODE_1 = "1";

	/** ajax 返回导入出错*/
	public static final String RESULT_CODE_2 = "-1";

	/** 会员数据 */
	public static final String MEMBER_SHEET_NAME = "会员数据";

	/** 产品数据 */
	public static final String PRODUCT_SHEET_NAME = "产品数据";

	/** 赠送礼品数据 */
	public static final String PRECENT_PRODUCT_SHEET_NAME = "赠送礼品数据";
	/** 单品特价数据 */
	public static final String SPECIAL_PRODUCT_SHEET_NAME = "单品特价数据";
	/** 单品折扣数据 */
	public static final String DISCOUNT_PRODUCT_SHEET_NAME = "单品折扣数据";

	/** 导入模式 1为增量 2为覆盖 */
	public static final String UPMODE_1 = "1";

	/** 导入类型 1为柜台 2为产品 3为会员 */
	public static final String Fail_OperateType_1 = "1";

	public static final String Fail_OperateType_2 = "2";

	public static final String Fail_OperateType_3 = "3";

	/** 黑白名单 1白名单 2黑名单 */
	public static final String FILTERTYPE_1 = "1";

	public static final String FILTERTYPE_2 = "2";

	/**
	 * excel文件加载类型
	 */
	public static final String EXECLOADTYPE_1 = "shoppingCart";
	public static final String EXECLOADTYPE_2 = "GIFT";
	public static final String EXECLOADTYPE_3 = "DPZK";
	public static final String EXECLOADTYPE_4 = "DPTJ";
	public static final String YES = "是";
	/** 比较值 */
	public static final String EQ = "等于";

	public static final String NE = "不等于";

	public static final String LE = "小于等于";

	public static final String GT = "大于";

	public static final String GE = "大于等于";

	public static final String LT = "小于";

	public static final String QUANTITY_Shop = "数量";

	public static final String AMOUNT_Shop = "金额";
}
