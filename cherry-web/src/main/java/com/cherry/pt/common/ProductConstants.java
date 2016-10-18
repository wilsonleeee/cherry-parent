/*  
 * @(#)ProductConstants.java     1.0 2011/05/31      
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
package com.cherry.pt.common;

public class ProductConstants {
	// 产品数据sheet名
	public static final String DATE_SHEET_NAME = "产品数据";
	// 产品ID
	public static final String PRODUCTID = "productId";
	// 产品ID集合
	public static final String PRODUCTIDS = "productIds";
	// 产品厂商ID
	public static final String PRT_VENDORID = "prtVendorId";
	// JSON
	public static final String JSON = "json";
	// 产品分类顺序
	public static final String SEQ = "seq";
	// 产品状态初始化
	public static final String STATUS = "status";
	// 产品状态初始化
	public static final String PRODUCT_DEF_STATUS = "E";
	// 产品状态 E：表明产品生效，可销售可订货；
	public static final String PRODUCT_STATUS_E = "E";
	// 产品状态 D：表明下柜产品
	public static final String PRODUCT_STATUS_D = "D";
	// 分类属性值(4位)
	public static final String PROPVALUE = "propValue";
	// 分类属性值(大于4位)
	public static final String PROPVALUECHERRY = "propValueCherry";
	// 分类属性值长度
	public static final int CATE_LENGTH = 4;
	// 分类1属性值
	public static final String PROPVALUECHERRY_1 = "propValueCherry1";
	// 分类2属性值
	public static final String PROPVALUECHERRY_2 = "propValueCherry2";
	// 分类3属性值
	public static final String PROPVALUECHERRY_3 = "propValueCherry3";
	// 分类属性名
	public static final String PROPNAME = "propValueCN";
	// 分类属性名1
	public static final String PROPNAME_1 = "propValueCN1";
	// 分类属性名2
	public static final String PROPNAME_2 = "propValueCN2";
	// 分类属性名3
	public static final String PROPNAME_3 = "propValueCN3";
	// 产品分类中文名
	public static final String PROPNAMECN = "propNameCN";
	// 产品分类英文名
	public static final String PROPNAMEEN = "propNameEN";
	// 产品分类属性值中文名
	public static final String PROPVALUECN = "propValueCN";
	// 产品分类属性值英文名
	public static final String PROPVALUEEN = "propValueEN";
	// 产品分类名(大分类)
	public static final String PROPNAMECN_1 = "大分类";
	// 产品分类名(中分类)
	public static final String PROPNAMECN_3 = "中分类";
	// 产品分类名(小分类)
	public static final String PROPNAMECN_2 = "小分类";
	// 终端使用区分 */
	public static final String TEMINALFLAG ="teminalFlag";
	// 终端使用区分(大分类下发)
	public static final String TEMINALFLAG_1 = "1";
	// 终端使用区分(小分类下发)
	public static final String TEMINALFLAG_2 = "2";
	// 终端使用区分(中分类下发)
	public static final String TEMINALFLAG_3 = "3";
	// 销售价格
	public static final String SALEPRICE = "salePrice";
	// 会员价格
	public static final String MEMPRICE = "memPrice";
	// 成本价格
	public static final String STANDARDCOST = "standardCost";
	// 采购价格
	public static final String ORDERPRICE = "orderPrice";
	// 默认价格
	public static final String DEF_PRICE = "0.00";
	// 行号
	public static final String LINENO = "lineNo";
	// 产品集合key
	public static final String LIST = "list";
	// 价格开始日期
	public static final String PRICESTARTDATE = "priceStartDate";
	// 价格截止日期
	public static final String PRICEENDDATE = "priceEndDate";
	// 分类ID
	public static final String PROPID = "propId";
	// 分类1ID
	public static final String PROPID_1 = "propId1";
	// 分类2ID
	public static final String PROPID_2 = "propId2";
	// 分类3ID
	public static final String PROPID_3 = "propId3";
	// 分类属性值ID
	public static final String PROPVALID = "propValId";
	// 绑定大分类属性值ID
	public static final String BINDBIGCLASS = "bindBigClass";
	// 产品操作成功数
	public static final String OPTCOUNT = "optCount";
	// 产品更新成功数
	public static final String UPDCOUNT = "updCount";
	// 产品添加成功数
	public static final String ADDCOUNT = "addCount";
	// 属性扩展表名
	public static final String EXTENDED_TABLE = "extendedTable";
	// 扩展属性
	public static final String EXTENDPROPERTYLIST = "extendPropertyList";
	// 图片路径
	public static final String IMAGE_PATH = "imagePath";
	// 产品分类信息
	public static final String CATE_INFO = "cateInfo";
	// 价格信息
	public static final String PRICE_INFO = "priceInfo";
	// 产品条码信息
	public static final String BARCODE_INFO = "barCodeInfo";
	// 原产品条码
	public static final String OLD_UNITCODE = "oldUnitCode";
	// 原产品编码
	public static final String OLD_BARCODE = "oldBarCode";
	// 操作标识
	public static final String OPTION = "option";
	// 更新操作
	public static final String OPTION_1 = "1";
	// 删除操作
	public static final String OPTION_2 = "2";
	// 停用时间
	public static final String CLOSING_TIME = "closingTime";
	// 可否作为BOM的组成
	public static final String ISBOM = "isBOM";
	// 产品类型
	public static final String ISBOM_1 = "1";
	// 产品类型
	public static final String MODE = "mode";
	// 产品类型:产品
	public static final String MODE_0 = "N";
	// 产品类型：BOM
	public static final String MODE_1 = "BOM";
	// 产品类型：促销品
	public static final String MODE_2 = "PRM";
	// 产品类型：促销品套装
	public static final String MODE_3 = "PBOM";
	// BOM信息
	public static final String BOM_INFO = "bomInfo";
	// BOM价格
	public static final String BOM_PRICE = "bomPrice";
	// BOM数量
	public static final String BOM_QUANTITY = "bomQuantity";
	// 是否管理库存
	public static final String ISSTOCK = "isStock";
	// 是否管理库存_是
	public static final String ISSTOCK_YES = "是";
	// 是否管理库存_否
	public static final String ISSTOCK_NO = "否";
	// 是否需要积分兑换
	public static final String ISEXCHANGED = "isExchanged";
	// 是否需要积分兑换_是
	public static final String ISEXCHANGED_YES = "是";
	// 是否需要积分兑换_否
	public static final String ISEXCHANGED_NO = "否";
	/** 仓库类型 非柜台仓库 */
    public static final String DepotType_NotCounter = "01";
    /** 仓库类型 柜台仓库 */
    public static final String DepotType_Counter = "02";
    /** 盲盘标志 打开*/
    public static final String Blind_Open = "1";
    /** 盲盘标志 关闭 */
    public static final String Blind_Close = "0";
    
    /** 是否支持终端盘点 	支持*/
    public static final String WITPOSTAKING_YES = "1";
    
    /** 是否支持终端盘点 	不支持*/
    public static final String WITPOSTAKING_NO = "0";
    
	/** 产品分类类型 */
	public static final String CATE_TYPE = "cateType";
	
	/** 产品分类代码 */
	public static final String CATE_CODE = "cateCode";
	
	/** 产品分类名称 */
	public static final String CATE_NAME = "cateName";
	
	/** 终端使用区分(中分类下发) */
	public static final String CATE_TYPE_3 = "3";
	
	/** 终端使用区分(大分类下发) */
	public static final String CATE_TYPE_1 = "1";
	
	/** 终端使用区分(小分类下发) */
	public static final String CATE_TYPE_2 = "2";
	
	/** 接口表产品大类代码*/
	public static final String BCLASSCODE = "bCode";

	/** 接口表产品大类名称*/
	public static final String BCLASSNAME = "bName";
	
	/** 接口表产品中类代码*/
	public static final String MCLASSCODE = "mCode";

	/** 接口表产品中类名称*/
	public static final String MCLASSNAME = "mName";

	/** 接口表产品小类代码*/
	public static final String LCLASSCODE = "lCode";
	
	/** 接口表产品小类名称*/
	public static final String LCLASSNAME = "lName";
	
	
	/** 启用日时 */
	public static final String STARTTIME = "startTime";
	
	/** 停用日时 */
	public static final String CLOSINGTIME = "closingTime";
	
	/** 产品启用状态 在销售日期区间 */
	public static final String SellDateFlag_0 = "0";
	
	/** 产品启用状态 不在销售日期区间 */
	public static final String SellDateFlag_1 = "1";
	
	/** 方案分配地点选择类型--按区域 */
	public static final String LOTION_TYPE_REGION = "1";
	
	/** 方案分配地点选择类型--按区域并且指定柜台 */
	public static final String LOTION_TYPE_REGION_COUNTER = "2";
	
	/** 方案分配地点选择类型--按渠道*/
	public static final String LOTION_TYPE_CHANNELS= "3";
	
	/** 方案分配地点选择类型--按渠道并且指定柜台*/
	public static final String LOTION_TYPE_CHANNELS_COUNTER= "4";
	
	/** 方案分配地点选择类型--全部柜台 */
	public static final String LOTION_TYPE_ALL_COUNTER= "5";
	
	/** 方案分配地点选择类型--导入柜台 */
	public static final String LOTION_TYPE_IMPORT_COUNTER= "6";
	
	/** 柜台产品使用模式 1:严格校验 2:补充校验 3:标准产品 */
	public static final String CNT_PRT_MODE_CONIFG_1 = "1";
	
	/** 柜台产品使用模式 1:严格校验 2:补充校验 3:标准产品 */
	public static final String CNT_PRT_MODE_CONIFG_2 = "2";
	
	/** 柜台产品使用模式 1:严格校验 2:补充校验 3:标准产品 */
	public static final String CNT_PRT_MODE_CONIFG_3 = "3";
	
	/** 产品方案添加产品模式1:标准模式 */
	public static final String SOLU_ADD_MODE_CONFIG_1 = "1";
	
	/** 产品方案添加产品模式 2:颖通模式(分类并集)  */
	public static final String SOLU_ADD_MODE_CONFIG_2 = "2";
	
	/** 产品方案添加产品模式 3：颖通模式(分类交集) */
	public static final String SOLU_ADD_MODE_CONFIG_3 = "3";
	
	
	
	
}
