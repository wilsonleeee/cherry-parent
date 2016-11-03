package com.cherry.ss.common;

public class PromotionConstants {
    /** 用户名*/
    public static final String USERNAME = "BATCH";
    
    /** 默认员工ID*/
    public static final String EMPLOYEEID = "-9999" ;
    
    /** 审核区分   审核通过*/
    public static final String AUDIT_FLAG_AGREE = "2" ;
    
    /** 库存业务类型  盘点*/
    public static final String BUSINESS_TYPE_STOCKTAKE = "P";
    
    /** 盘点类型  自由盘点*/
    public static final String STOCKTAKING_TYPE_FREEDOM = "F3";
    
    /** 促销品库存同步画面ID*/
    public static final String BINBESSPRM05 = "BINBESSPRM05";
    
    /** 盘点处理方式 终端盘点录入*/
    public static final String HandleType_MANUAL ="2";
    
    /** 库存业务类型  入库*/
    public static final String BUSINESS_TYPE_STORAGE_IN = "7";
    
    /** 库存业务类型  出库*/
    public static final String BUSINESS_TYPE_STORAGE_OUT = "8";
    

	/** 促销活动地点选择类型--按区域 */
	public static final String LOTION_TYPE_REGION = "1";
	
	/** 促销活动地点选择类型--按区域并且指定柜台 */
	public static final String LOTION_TYPE_REGION_COUNTER = "2";
	
	/** 促销活动地点选择类型--按渠道*/
	public static final String LOTION_TYPE_CHANNELS= "3";
	
	/** 促销活动地点选择类型--按渠道并且指定柜台*/
	public static final String LOTION_TYPE_CHANNELS_COUNTER= "4";
	
	/** 促销活动基础条件 --时间 */
	public static final String BASE_PROP_TIME = "baseProp_time";
	
	/** 促销活动基础条件 --区域市 */
	public static final String BASE_PROP_CITY = "baseProp_city";
	
	/** 促销活动基础条件 --渠道*/
	public static final String BASE_PROP_CHANNEL = "baseProp_channal";
	
	/** 促销活动基础条件 --柜台*/
	public static final String BASE_PROP_COUNTER = "baseProp_counter";
	
	/** 促销活动基础条件--消费金额 */
	public static final String BASE_PROP_AMOUNT = "baseProp_amount";
	
	/** 促销活动基础条件--购买产品 */
	public static final String BASE_PROP_PRODUCT = "baseProp_product";
	
	/** 促销活动基础条件 --组织*/
	public static final String BASE_PROP_ORGANIZATION ="baseProp_organization";
	
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
	
	/** 促销活动促销礼品类别码*/
	public static final String PROMOTION_CXLP_TYPE_CODE = "CXLP";
	
	/** 促销活动促销礼品名称*/
	public static final String PROMOTION_CXLP_NAME = "促销礼品";
	
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
	
	
	/** 促销活动地点选择类型--全部柜台*/
	public static final String LOTION_TYPE_ALL_COUNTER= "0";
	
	/** 促销活动地点选择类型--导入柜台*/
	public static final String LOTION_TYPE_IMPORT_COUNTER= "5";
	
	public static final String LOTION_TYPE_7= "7";
	
	public static final String LOTION_TYPE_8= "8";

	public static final String BASEPROP_FACTION = "baseProp_faction";

}
