package com.cherry.ia.common;

public class ProductConstants {

	/** 生效日期*/
	public static final String STRAT_DATE = "startDate" ;
	
	/** 失效日期*/
	public static final String END_DATE = "endDate" ;
	
	/** 产品ID*/
	public static final String PRODUCT_ID = "productId" ;
	
	/** 默认失效日期*/
	public static final String DEFAULT_END_DATE = "21001231" ;
	
	/** 品牌CODE */
	public static final String BRANDCODE = "brandCode" ;
	
	/** old产品编码 */
	public static final String OLDUNITCODE = "oldUnitCode" ;
	
	/** old产品条码 */
	public static final String OLDBARCODE = "oldBarCode" ;
	
	/** 接口表产品价格*/
	public static final String PRICE = "price" ;
		
	/** 接口表产品代码*/
	public static final String BAR_CODE = "barCode" ;

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

	/** 接口表产品厂商编码*/
	public static final String UNITCODE = "unitCode";

	/** 接口表产品中文名*/
	public static final String NAMETOTAL = "nameTotal";

	/** 产品分类类型 */
	public static final String CATE_TYPE = "cateType";
	
	/** 产品分类代码 */
	public static final String CATE_CODE = "cateCode";
	
	/** 产品分类名称 */
	public static final String CATE_NAME = "cateName";
	
	/** 产品分类名(大分类) */
	public static final String CATE_NAME_B = "大分类";
	
	/** 产品分类名(中分类) */
	public static final String CATE_NAME_M = "中分类";
	
	/** 产品分类名(小分类) */
	public static final String CATE_NAME_L = "小分类";
	
	/** 终端使用区分 */
	public static final String TEMINALFLAG = "teminalFlag";
	
	/** 终端使用区分(中分类下发) */
	public static final String CATE_TYPE_3 = "3";
	
	/** 终端使用区分(大分类下发) */
	public static final String CATE_TYPE_1 = "1";
	
	/** 终端使用区分(小分类下发) */
	public static final String CATE_TYPE_2 = "2";
	
	/** 产品分类ID */
	public static final String PRTCATPROPID = "prtCatPropId";
	
	/** 产品分类名 */
	public static final String PROPNAMECN = "propNameCN";
	
	/** 产品分类类别属性 */
	public static final String PROP_PROPERTY = "property";
	
	/** 分类属性值长度*/
	public static final int CATE_LENGTH = 4;
	
	/** 产品分类属性值ID */
	public static final String CATPROPVALID = "catPropValId";
	
	/** 产品分类属性值中文名 */
	public static final String PROPVALUE_CN = "propValueCN";
	
	/** 产品分类属性值 */
	public static final String PROPVALUE = "propValue";
	
	/** 产品分类属性值(品牌显示用) */
	public static final String PROPVALUECHERRY = "propValueCherry";
	
	/** 绑定大分类属性值ID */
	public static final String BINDBIGCLASS = "bindBigClass";
	
	/** 产品厂商ID */
	public static final String PROVENDORID = "proVendorId";
	
	/** 启用日时 */
	public static final String STARTTIME = "startTime";
	
	/** 停用日时 */
	public static final String CLOSINGTIME = "closingTime";
	
	/** 产品启用状态 在销售日期区间 */
	public static final String SellDateFlag_0 = "0";
	
	/** 产品启用状态 不在销售日期区间 */
	public static final String SellDateFlag_1 = "1";
	
	/** 产品状态 E：可销售可订货 */
	public static final String PRODUCT_STATUS_E = "E";
	/** 产品状态 D：表明下柜产品 */
	public static final String PRODUCT_STATUS_D = "D";
	
	/** 方案分配地点选择类型--按区域 */
	public static final String LOTION_TYPE_REGION = "1";
	
	/** 方案分配地点选择类型--按区域并且指定柜台 */
	public static final String LOTION_TYPE_REGION_COUNTER = "2";
	
	/** 方案分配地点选择类型--按渠道*/
	public static final String LOTION_TYPE_CHANNELS= "3";
	
	/** 方案分配地点选择类型--按渠道并且指定柜台*/
	public static final String LOTION_TYPE_CHANNELS_COUNTER= "4";
	
	/** 方案分配地点选择类型--全部柜台*/
	public static final String LOTION_TYPE_ALL_COUNTER= "5";
	
	/** 方案分配地点选择类型--导入柜台*/
	public static final String LOTION_TYPE_IMPORT_COUNTER= "6";
	
	// 价格开始日期
	public static final String PRICESTARTDATE = "priceStartDate";
	// 价格截止日期
	public static final String PRICEENDDATE = "priceEndDate";
	
	// 操作标识
	public static final String OPTION = "option";
	// 更新操作
	public static final String OPTION_1 = "1";
	// 删除操作
	public static final String OPTION_2 = "2";
	
	/** 产品方案添加产品模式1:标准模式 */
	public static final String SOLU_ADD_MODE_CONFIG_1 = "1";
	
	/** 产品方案添加产品模式 2:颖通模式(分类并集)  */
	public static final String SOLU_ADD_MODE_CONFIG_2 = "2";
	
	/** 产品方案添加产品模式 3：颖通模式(分类交集) */
	public static final String SOLU_ADD_MODE_CONFIG_3 = "3";
}
