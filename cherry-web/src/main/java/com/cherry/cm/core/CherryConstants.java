/*  
 * @(#)CherryConstants.java     1.0 2011/05/31      
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
package com.cherry.cm.core;

public class CherryConstants {
	
	// ====================== ibatis相关 ============================ //
	/** SqlIDkey */
	public static final String IBATIS_SQL_ID = "ibatis_sql_id";	
	
	/** 批处理一页最大数 */
	public final static int BATCH_PAGE_MAX_NUM = 5000;
	
	// ====================== session中存放数据使用的常量key ============================ //	
	/** 用户信息key */
	public static final String SESSION_USERINFO = "userinfo";
	/** 语言key */
	public static final String SESSION_LANGUAGE = "language";
	
	/** 语言key */
	public static final String SESSION_CHERRY_LANGUAGE = "cherry_language";
	
	/** 语言key 中文*/
	public static final String SESSION_LANGUAGE_CN = "zh_CN";
	
	/** 语言key 英文*/
	public static final String SESSION_LANGUAGE_EN = "en_US";
	
	/** 登录画面*/
	public static final String SESSION_CHECK_IMAGE = "checkimage";	
	
	/** topmenu key */
	public static final String SESSION_TOPMENU_LIST = "topmenu";
	
	/** 当前topmenu  */
	public static final String SESSION_TOPMENU_CURRENT = "topmenucurrent";
	
	/** 当前左菜单CherryMenu  */
	public static final String SESSION_LEFTMENUALL_CURRENT = "leftmenuallcurrent";
	
	/** leftmenu key */
	public static final String SESSION_LEFTMENU_XMLDOCMAP = "xmldocumentmap";
	
	/** 读取leftmenu菜单后生成的xml格式字符串文档存放于request中对应的key*/
	public static final String SESSION_LEFTMENU_XMLSTRING = "leftmenu";
	
	/** 上传文件的名字*/
	public static final String SESSION_UPLOAD_FILENAME = "uploadfilenamefull";
	
	/** 连接BI服务器对象*/
	public static final String SESSION_DWMDICONNECTION = "dwmdiconnection";
	
	/** 连接BI服务器对象(钻透用)*/
	public static final String SESSION_DWDBICONNECTION = "dwdbiconnection";
	
	/** 岗位级别标志*/
	public static final String SESSION_PRIVILEGE_FLAG = "privilegeFlag";
	
	/** 品牌自定义样式 */
	public static final String SESSION_CHERRY_BRANDCSS = "cherry_brandcss";
	
	/** 门店柜台信息 */
	public static final String SESSION_CHERRY_COUNTERINFO = "counterInfo";
	
	// ====================== struts2配置相关 ============================ //	
	/** 全局错误*/
	public static final String GLOBAL_ERROR = "globalError";
	/** action执行结果（错误，成功，字段校验错误）*/
	public static final String GLOBAL_ACCTION_RESULT = "globalAcctionResult";
	/** action执行结果（错误，成功）替换整张页面 */
	public static final String GLOBAL_ACCTION_RESULT_BODY = "globalAcctionResultBody";
	/** action执行结果（错误，成功）替换DIALOG指定的DIV */
	public static final String GLOBAL_ACCTION_RESULT_DIALOG = "globalAcctionResultDialog";
	/** action执行结果（错误，成功）替换整张页面,并刷新父页面 */
	public static final String GLOBAL_ACCTION_RESULT_PAGE = "globalAcctionResultPage";
	/** 登录画面*/
	public static final String GLOBAL_LOGIN = "globalLogin";
	
	// ====================== 登录机能中加载菜单时使用 ============================ //		
	/** 菜单表中的菜单类型 topmenu */
	public static final String MENUTYPE_TOPMENU="1";
	/** 菜单表中的菜单类型 左菜单中的父节点 */
	public static final String MENUTYPE_LEFTPARENT="2";
	/** 菜单表中的菜单类型 左菜单中的画面节点 */
	public static final String MENUTYPE_LEFTCHILD="3";
	/** 菜单表中的菜单类型 左菜单中的画面控件 */
	public static final String MENUTYPE_LEFTCONTROL="4";
	/** 菜单表中的菜单类型 弹出画面 */
	public static final String MENUTYPE_POPUPPAGE="5";
	
	// ====================== 系统配置相关 ============================ //		
	/** 组织全体共通*/
	public static final int ORG_CODE_ALL = -9999;
	
	/** 品牌全体共通*/
	public static final int Brand_CODE_ALL = -9999;
		
	/** 上传图片的最大M数*/
	public static final int IMAGE_MAX_SIZE = 2;
	
	/** 上传图片的类型*/
	public static final String IMAGE_TYPES = "jpg,gif,jpeg,png,bmp";
	
	/** 上传文件的结果(失败)*/
	public static final String result_NG = "NG";
	
	/** 上传文件的结果(成功)*/
	public static final String result_OK = "OK";
	
	/** 复制文件最大传输字节数*/
	public static final int COPYFILE_MAX_SIZE = 2097152;
	
	/** 根目录 */
	public static final String CHERRY_HOME = "CHERRY_HOME";
	
	/** 截止日期*/
	public static final String longLongAfter = "2100-01-01";
	
	/** 最小时间 */
	public static final String minTime = "00:00:00";
	
	/** 最大时间 */
	public static final String maxTime = "23:59:59";
	
	/** 作成日时 */
	public static final String CREATE_TIME = "createTime";
	
	/** 作成者 */
	public static final String CREATEDBY = "createdBy";
	
	/** 作成程序名 */
	public static final String CREATEPGM = "createPGM";
	
	/** 更新者 */
	public static final String UPDATEDBY = "updatedBy";
	
	/** 更新程序名 */
	public static final String UPDATEPGM = "updatePGM";
	
	/** 更新日时 */
	public static final String UPDATE_TIME = "updateTime";

	/** 修改时间 */
	public static final String MODIFY_TIME = "modifyTime";
	
	/** 修改次数 */
	public static final String MODIFY_COUNT = "modifyCount";
	
	/** 有效区分 */
	public static final String VALID_FLAG = "validFlag";
	
	/** 用户ID */
	public static final String USERID = "userId";
	
	/** 部门ID */
	public static final String ORGANIZATIONID = "organizationId";
	
	/** 部门名称 */
	public static final String DEPARTNAME = "departName";
	
	/** 部门类型  */
	public static final String DEPART_TYPE = "departType";
	
	/** 组织ID */
	public static final String ORGANIZATIONINFOID = "organizationInfoId";
	
	/** 组织Code */
	public static final String ORG_CODE = "orgCode";
	
	/** 组织ID */
	public static final String POSITIONCATEGORYID = "positionCategoryId";
	
	/** 岗位ID */
	public static final String POSITIONID = "positionId";
	
	/** 品牌ID */
	public static final String BRANDINFOID = "brandInfoId";
	
	/** 品牌Code */
	public static final String BRAND_CODE = "brandCode";
	
	/** 品牌名 */
	public static final String BRAND_NAME = "brandName";
	
	/** 树节点位置 */
	public static final String PATH = "path";

	/** 树节点级别 */
	public static final String LEVEL = "level";
	
	/** 业务类型 */
	public static final String BUSINESS_TYPE = "businessType";
	
	/** 业务类型 */
	public static final String BUSINESS_DATE = "businessDate";
	
	/** 业务类型--基础数据  */
	public static final String BUSINESS_TYPE0 = "0";
	
	/** 业务类型--库存数据  */
	public static final String BUSINESS_TYPE1 = "1";
	
	/** 业务类型--会员数据  */
	public static final String BUSINESS_TYPE2 = "2";
	
    /** 业务类型--销售数据  */
    public static final String BUSINESS_TYPE3 = "3";
	
	/** 操作类型 */
	public static final String OPERATION_TYPE = "operationType";
	
	/** 操作类型--更新（包括新增，删除）*/
	public static final String OPERATION_TYPE0 = "0";
	
	/** 操作类型--查询  */
	public static final String OPERATION_TYPE1 = "1";

	/** 品牌为总部时的值  */
	public static final int BRAND_INFO_ID_VALUE = -9999;
	
	/** 促销品ID */
	
	public static final String PROMOTIONPROID = "promotionProId";
	
	/** 促销品名称 */
	public static final String NAMETOTAL = "nameTotal";
	
	/** 厂商编码 */
	public static final String UNITCODE = "unitCode";
	
	/** 促销品条码 */
	public static final String BARCODE = "barCode";
	
	/** 促销品MODE */
	public static final String MODE = "mode";
	
	/**  促销品开始销售日期*/
	public static final String SELLSTARTDATE = "sellStartDate";
	
	/**  促销品结束销售日期*/
	public static final String SELLENDDATE = "sellEndDate";
	
    /** 促销品厂商ID */
	
	public static final String FACTORYID = "factoryId";
	
    /** 类别代码 */
	
	public static final String ITEMCLASSCODE = "itemClassCode";

	/** 逗号 */
	public static final String COMMA = ",";

	/** 空字符串 */
	public static final String BLANK = "";
	
	/** 点 */
	public static final String POINT = ".";
	
	/** 空格 */
	public static final String SPACE = " ";
	
	/** 下划线 */
	public static final String UNLINE = "_" ;
	 
	/** 斜线 */
	public static final String SLASH  = "/" ;
	
	/** 日期格式 */
	public static final String DATE_PATTERN = "yyyy-MM-dd" ;
	
	/** 日期格式 yyyyMMdd*/
	public static final String DATE_YYMMDD = "yyyyMMdd" ;
	
	/** 日期格式(年月) */
	public static final String DATEYYYYMM = "yyyyMM" ;
	
	/** 日期格式(24小时制) */
	public static final String DATE_PATTERN_24_HOURS = "yyyy-MM-dd HH:mm:ss" ;
	
	/** 日期 */
	public static final String DATE = "date" ;
	
	/** 年 */
	public static final String YEAR = "year" ;
	
	/** 月 */
	public static final String MONTH = "month" ;
	
	/** 日 */
	public static final String DAY = "day" ;
	
	/** 有效期默认年限 */
	public static final int USEFUL_LIFE  = 100 ;
	
	/** 柜台编号规则 1：普通规则 */
	public static final String CNTCODE_RULE1 = "1";
	
	/** 柜台编号规则 2：自然堂规则 */
	public static final String CNTCODE_RULE2 = "2";
	
	/** 促销品编码条码自动生成规则 1：手动生成   */
	public static final String PROM_UB_RULE1 = "1";
	
	/** 促销品编码条码自动生成规则  2：自然堂自动生成  */
	public static final String PROM_UB_RULE2 = "2";
	
	/** 是否小店云场景模式 0：非小店云模式  */
	public static final String IS_POSCLOUD_0 = "0";
	
	/** 是否小店云场景模式 1：小店云模式  */
	public static final String IS_POSCLOUD_1 = "1";
	
	/** 工作流任务名称 */
	public static enum TASK_NAME {
		
		OS_TaskName_ProType_N("N","产品",""),
		OS_TaskName_ProType_PRM("PRM","促销品",""),
		OS_TaskName_10("10","入库",""),
		OS_TaskName_11("11","入库单审核",""),
		OS_TaskName_12("12","入库单修改",""),
		OS_TaskName_13("13","入库单删除",""),
		OS_TaskName_20("20","出库",""),
		OS_TaskName_21("21","出库单审核",""),
		OS_TaskName_22("22","出库单修改",""),
		OS_TaskName_23("23","出库单删除",""),
		OS_TaskName_30("30","订货",""),
		OS_TaskName_31("31","订货单审核",""),
		OS_TaskName_32("32","订货单修改",""),
		OS_TaskName_33("33","订货单删除",""),
		OS_TaskName_34("34","订货单二审",""),
		OS_TaskName_40("40","发货",""),
		OS_TaskName_41("41","发货单审核",""),
		OS_TaskName_42("42","发货单修改",""),
		OS_TaskName_43("43","发货单删除",""),
		OS_TaskName_50("50","收货",""),
		OS_TaskName_51("51","收货单审核",""),
		OS_TaskName_52("52","收货单修改",""),
		OS_TaskName_53("53","收货单删除",""),
		OS_TaskName_60("60","退库",""),
		OS_TaskName_61("61","退库单审核",""),
		OS_TaskName_62("62","退库单修改",""),
		OS_TaskName_63("63","退库单删除",""),
		OS_TaskName_70("70","调入",""),
		OS_TaskName_71("71","调入单审核",""),
		OS_TaskName_72("72","调入单修改",""),
		OS_TaskName_73("73","调入单删除",""),
		OS_TaskName_80("80","调出",""),
		OS_TaskName_81("81","调出单审核",""),
		OS_TaskName_82("82","调出单修改",""),
		OS_TaskName_83("83","调出单删除",""),
		OS_TaskName_90("90","盘点",""),
		OS_TaskName_91("91","盘点单审核",""),
		OS_TaskName_92("92","盘点单修改",""),
		OS_TaskName_93("93","盘点单删除",""),
		OS_TaskName_100("100","移库",""),
		OS_TaskName_101("101","移库单审核",""),
		OS_TaskName_102("102","移库单修改",""),
		OS_TaskName_103("103","移库单删除",""),
		OS_TaskName_110("110","报损",""),
		OS_TaskName_111("111","报损单审核",""),
		OS_TaskName_112("112","报损单修改",""),
		OS_TaskName_113("113","报损单删除","");
		
		public static String getName(Object key, String language) {
			if(key == null)
				return "";
			TASK_NAME[] taskNames = TASK_NAME.values();
			for(TASK_NAME taskName : taskNames) {
				if(taskName.getKey().equals(key.toString())) {
					if(language != null && "en_US".equals(language)) {
						return taskName.getNameEN();
					} else {
						return taskName.getNameCN();
					}
				}
			}
			return "";
		}
		
		TASK_NAME(String key, String nameCN, String nameEN) {
			this.key = key;
			this.nameCN = nameCN;
			this.nameEN = nameEN;
		}
		
		public String getKey() {
			return key;
		}

		public String getNameCN() {
			return nameCN;
		}

		public String getNameEN() {
			return nameEN;
		}

		private String key;
		private String nameCN;
		private String nameEN;
	}
	
	/** datatable 过滤字段名 */
	public static final String FILTER_LIST_NAME = "filterList" ;
	
	/** 弹出datable -- 促销产品信息 */
	public static final String POP_PRMPRODUCT_LIST = "popProductInfoList" ;
	
	/** 弹出datatable 产品信息 **/
	public static final String POP_PRODUCT_LIST = "popProductInfoList" ;

	/** 分类树 产品信息 **/
	public static final String CATEGORY_TREE_LIST = "categoryTreeList" ;

	/** 弹出datatable 会员信息 **/
	public static final String POP_MEMBER_LIST = "popMemberInfoList" ;
	
	/** 弹出datatable 对象批次信息 **/
	public static final String POP_OBJBATCH_LIST = "popObjBatchInfoList" ;
	
	/** 树结构表中的默认根节点 */
	public static final String DUMMY_VALUE = "/";
	
	/** 测试柜台名称中必须包含的文字 */
	public static final String COUNTERNAME_TEST = "测试";
	
	/** 测试柜台名称中必须包含的文字 */
	public static final String COUNTERNAME_TEST_TW = "測試";
	
	/** 柜台的测试区分  正式柜台*/
	public static final String COUNTERKIND_OFFICIAL = "正式柜台";
	
	/** 柜台的测试区分  测试柜台*/
	public static final String COUNTERKIND_TEST = "测试柜台";
	
	/** 区域类型  省*/
	public static final String REGIONTYPE_PROVINCE="省";
	
	/** 区域类型  市*/
	public static final String REGIONTYPE_CITY="市";
	
	/** 扩展属性对象表 ----产品表*/
	public static final String EXTENDED_TABLE_PRODUCT = "BIN_Product";
	/** 扩展属性对象表 ----会员表*/
	public static final String EXTENDED_TABLE_MEMBER = "BIN_MemberInfo";	
	/** 开关类配置项-开*/
	public static final String SYSTEM_CONFIG_ENABLE = "1";	
	/** 开关类配置项-关*/
	public static final String SYSTEM_CONFIG_DISENABLE = "0";
	
	/** 默认厂商代码*/
	public static final String MANUFACTURERCODE = "9999";
	/** 默认厂商名称（中文）*/
	public static final String FACTORYNAMECN = "默认厂商";
	/** 默认厂商代码（中文简称）*/
	public static final String FACTORYNAMECNCNSHORT = "默认厂商";
	/** 默认厂商代码（英文）*/
	public static final String FACTORYNAMEEN = "default factory";
	/** 默认厂商代码（英文简称）*/
	public static final String FACTORYNAMEENSHORT = "default factory";
	/** 默认区分*/
	public static final String FACTORYDEFAULTFLAG = "1";
	/** 组织区域代码*/
	public static final String REGIONCODEORG = "ORG";
	/** 品牌区域代码*/
	public static final String REGIONCODEBRAND = "BRAND";
	/** 组织品牌对应的区域类型*/
	public static final String REGIONTYPEORGBRAND = "-1";
	/** 会员转柜类型 */
	public static final String MSG_MEMBER_MF = "MF";
	/** 会员问题类型 */
	public static final String MSG_MEMBER_MA = "QA";
	
	// ====================== 工作流业务类型code定义 ============================ //	
	/** 工作流     业务类型代码key */
	public static final String JBPM_BUSSINESS_CODE = "BussinessTypeCode" ;
	
	/** 工作流     业务单据ID */
	public static final String JBPM_MAIN_ID = "mainOrderID" ;
	
	/** 工作流     业务单据No */
	public static final String JBPM_MAIN_NO = "mainOrderNo" ;
	
	/** 工作流     任务发起者 */
	public static final String JBPM_TASK_INITIATOR = "TaskInitiator" ;
	
	/** 工作流     审核任务发起者类型   部门 */
	public static final String JBPM_INITIATOR_ORG_CODE= "3" ;
	
	/** 工作流      审核任务发起者类型  岗位类别 */
	public static final String JBPM_INITIATOR_POS_CODE= "2" ;
	
	/** 工作流      审核任务发起者类型   个人 */
	public static final String JBPM_INITIATOR_PER_CODE= "1" ;
	
	/** 工作流      审核任务参与者类型   部门 */
	public static final String JBPM_AUDITOR_ORG_CODE= "3" ;
	
	/** 工作流      审核任务参与者类型  岗位类别 */
	public static final String JBPM_AUDITOR_POS_CODE= "2" ;
	
	/** 工作流       审核任务参与者类型   个人 */
	public static final String JBPM_AUDITOR_PER_CODE= "1" ;
	
	
	
	
	/** 工作流      促销品 发货单审核 */
	public static final String JBPM_PROM_SEND_AUDIT = "100" ;
	
	/** 工作流      促销品  发货单修改 */
	public static final String JBPM_PROM_SEND_EDIT = "101" ;
	
	/** 工作流      促销品  发货单删除 */
	public static final String JBPM_PROM_SEND_DEL = "102" ;
	
	/** 工作流     促销品   发货单收货 */
	public static final String JBPM_PROM_SEND_REC = "103" ;
	
	
	/** 工作流      促销品   调入申请单审核 */
	public static final String JBPM_PROM_ALLO_AUDIT = "200" ;
	
	/** 工作流      促销品   调入申请单修改 */
	public static final String JBPM_PROM_ALLO_EDIT = "201" ;
	/** 工作流      促销品   调入申请单删除 */
	public static final String JBPM_PROM_ALLO_DEL = "202" ;	
	
	/** 工作流       促销品  调出确认单审核 */
	public static final String JBPM_PROM_ALLO_OUT_AUDIT = "203" ;
	
	/** 工作流      促销品   调出确认 */
	public static final String JBPM_PROM_ALLO_OUT_CONFIRM = "204" ;
	
	/** 工作流      促销品    调出确认单收货 */
	public static final String JBPM_PROM_ALLO_REC = "205" ;
	

	
	
	// ====================== 业务中涉及到的状态及业务类型定义 ============================ //
	
	/** 岗位类别（ 美容顾问）*/
	public static final String CATRGORY_CODE_BA="01";
	/** 岗位类别（ 柜台主管）*/
	public static final String CATRGORY_CODE_BAS="02";
	/** 岗位类别（ 区域经理）*/
	public static final String CATRGORY_CODE_AM="03";
	/** 岗位类别（ 品牌岗位）*/
	//public static final String CATRGORY_CODE_BR="04";
	/** 岗位类别（总部岗位）*/
	//public static final String CATRGORY_CODE_HQ="05";
	
	/** 部门类型 0：总部； 1：品牌 2：各级办事处 3：经销商 4：柜台 5：其他*/
	//public static final String ORGANIZATION_TYPE_HQ="0";
	
	/** 部门类型（品牌）*/
	//public static final String ORGANIZATION_TYPE_BR="1";
	
	/** 部门类型（各级办事处）*/
	public static final String ORGANIZATION_TYPE_OF="2";
	
	/** 部门类型（经销商）*/
	public static final String ORGANIZATION_TYPE_THREE="3";
	
	/** 部门类型（柜台）*/
	public static final String ORGANIZATION_TYPE_FOUR="4";
	
	/** 入出库表中的出入库区分    入库*/
	public static final String STOCK_TYPE_IN = "0";
	/** 入出库表中的出入库区分    出库*/
	public static final String STOCK_TYPE_OUT = "1";
	
    /** 入出库表中的截止计算区分    未反映到历史库存表中*/
    public static final String CLOSEFLAG_NO = "0";
    
    /** 入出库表中的截止计算区分    已经反映到历史库存表中*/
    public static final String CLOSEFLAG_YES = "1";
	
	/** 产品发货业务的处理状态  未处理（发货单）*/
    public static final String PRO_DELIVER_TRADESTATUS_UNSEND = "10";
	
	/** 发货业务中的入库区分    未发货（发货单）*/
	public static final String PROM_DELIVER_UNSEND = "1";
	
	/** 发货业务中的入库区分    已发货（发货单）*/
	public static final String PROM_DELIVER_SENT = "2";
	
	/** 发货业务中的入库区分    已收货（发货单，收货单）*/
	public static final String PROM_DELIVER_RECEIVE = "3";
	
	/** 发货业务中的入库区分    已入库（发货单，收货单）*/
	public static final String PROM_DELIVER_STOCK_IN = "4";
	
	/** 收货业务中的入库区分    未处理（收货单）*/
	public static final String PROM_RECEIVE_STOCK_UNDO = "10";
	
	/** 收货业务中的入库区分    废弃（收货单）*/
	public static final String PROM_RECEIVE_STOCK_discard = "11";
	
	/** 收货业务中的入库区分    已入库（收货单）*/
	public static final String PROM_RECEIVE_STOCK_IN = "12";

	
//	/** 调拨业务中处理状态    已出库（调出确认单）*/
//	public static final String PROM_ALLOCATION_STOCK_OUT = "3";
//	
//	/** 调拨业务中处理状态    已入库（调入申请单）*/
//	public static final String PROM_ALLOCATION_STOCK_IN = "4";

	/** 自定义密钥 */
	public static final String CUSTOMKEY = "f5a4897fsb778gkbi52ziou6nphq2syz0dpzb83hyrv0l9rrx4p3t37x77puv8extg9pb894kz5488ql5sgcb1zve6b88kv8iifmj9ptxquxke79x1q506wckzw81war";

	/**智能促銷定义密钥（家化）*/
	public static final String PROMOTIONKEY = "jh0321,.";
			
	/* 登录用户的DataSource的键值 */
	public static final String CHERRY_SECURITY_CONTEXT_KEY = "CHERRY_SECURITY_CONTEXT";
	
	// 默认仓库名称
	public static final String IVT_NAME_CN_DEFAULT = "默认仓库";
	// 缺省仓库区分
	public static final String IVT_DEFAULTFLAG = "1";
	
	/** 系统默认组织  */
	public static final String ORGANIZATION_CODE_DEFAULT = "-9999";
	
	/** 柜台数据sheet名  */
	public static final String COUNTER_SHEET_NAME = "柜台数据";
	
	/** 柜台数据sheet名  */
	public static final String PRT_SHEET_NAME = "特定商品数据";
	
	/** BA数据sheet名 */
	public static final String BA_SHEET_NAME = "BA数据";
	
	/** 字段说明sheet名*/
	public static final String DESCRIPTION_SHEET_NAME = "字段说明";
	
	/** 会员数据sheet名  */
	public static final String MEMBER_SHEET_NAME = "会员数据";
	
	/** Coupon数据sheet名  */
	public static final String COUPON_SHEET_NAME = "Coupon信息";
	
	/** 活动预约数据sheet名  */
	public static final String RESMEM_SHEET_NAME = "活动预约数据";

	/** 积分数据sheet名  */
	public static final String POINT_SHEET_NAME = "积分数据";
	
	/**销售目标数据sheet名*/
	public static final String SALETARGET_SHEET_NAME = "销售目标数据";
	
	/** 柜台消息数据sheet名*/
	public static final String COUNTERMESSAGE_SHEET_NAME = "柜台消息数据";

	/** 新增会员档案信息sheet名  */
	public static final String MEMFLIE_ADDMEMBER_NAME= "新增会员档案信息";
	
	/** 更新会员档案信息sheet名  */
	public static final String MEMFLIE_UPDMEMBER_NAME= "更新会员档案信息";
	
	/** 活动产品库存信息sheet名*/
	public static final String CAMPFILE_CAMPAIGNSTOCK_SHEET_NAME = "活动产品库存信息";
	
	/** 会员关键属性信息sheet名  */
	public static final String MEMFLIE_KEY_NAME= "会员关键属性信息";
	
	/** 产品方案明细数据sheet名*/
	public static final String PRTSOLUDETAIL_SHEET_NAME = "产品方案明细";
	
	/** 产品功能开启时间明细数据sheet名*/
	public static final String PRTFUNDETAIL_SHEET_NAME = "产品列表";
	
	/** 入库导入数据sheet名*/
	public static final String INDEPOT_SHEET_NAME = "入库数据";
	
	/** 发货导入数据sheet名*/
	public static final String DELIVER_SHEET_NAME = "发货数据";
	
    /** 退库申请导入数据sheet名*/
    public static final String PRORETURNREQUEST_SHEET_NAME = "退库申请数据";
	
	/** 订货导入数据sheet名*/
	public static final String ORDER_SHEET_NAME = "订货数据";
	
	/** 后台销售导入数据sheet名*/
	public static final String BACKSTAGESALE_SHEET_NAME = "销售数据";
	
	/** 促销品入库导入数据sheet名*/
	public static final String PRMINDEPOT_SHEET_NAME = "促销品入库数据";
	
	/** 销售产品数据sheet名 */
	public static final String SALEPRODUCT_SHEET_NAME = "销售产品数据";
	
	/** BB柜台数据sheet名 */
	public static final String BBCOUNTER_SHEET_NAME = "BB柜台数据";
	
	/** 银行对账单数据sheet名*/
	public static final String BANKBILL_SHEET_NAME = "银行对账单";
	
	/** 银行对账单数据sheet名*/
	public static final String ASSESSMENTSCORD_SHEET_NAME = "营业员当月得分维护";
	
	/** 唯一码激活明细sheet名 */
	public static final String UNQDETAILS_SHEET_NAME = "唯一码维护明细";
	
	// 未知节点的部门代码
	public static final String UNKNOWN_DEPARTCODE = "ZZZ";
	
	// 未知节点的部门名称
	public static final String UNKNOWN_DEPARTNAME = "未知";
	
	// 未知节点的部门类型
	public static final String UNKNOWN_DEPARTTYPE = "Z";
	
	/** 每次批处理条数 */
	public static final int DATE_SIZE = 100;
	
	/** 分页 开始条数*/
	public static final String START = "START" ;

	/** 分页 结束条数*/
	public static final String END = "END" ;
	
	/** 分页 排序字段(字符)*/
	public static final String SORT_ID = "SORT_ID" ;
	
	//=============================================业务单据数据同步区分====================//
	/** 业务单据数据同步区分  可同步 */
	public static final String BILL_SYNCHFLAG_1 = "1";
	/** 业务单据数据同步区分  同步中 */
	public static final String BILL_SYNCHFLAG_2 = "2";
	/** 业务单据数据同步区分  已同步 */
	public static final String BILL_SYNCHFLAG_3 = "3";
	
	//=============================================业务中单据状态====================//
	/** 单据状态  订货单  未处理*/
    public static final String BILLTYPE_PRO_OD_UNDO = "10";
	/** 单据状态  订货单  已发货*/
    public static final String BILLTYPE_PRO_OD_SEND = "12";
	/** 单据状态  订货单  已收货*/
    public static final String BILLTYPE_PRO_OD_RECIVE = "13";
    
	/** 单据状态  发货单  未处理*/
    public static final String BILLTYPE_PRO_SD_UNDO = "10";
	/** 单据状态  发货单  已发货*/
    public static final String BILLTYPE_PRO_SD_SEND = "12";
	/** 单据状态  发货单  已收货*/
    public static final String BILLTYPE_PRO_SD_RECIVE = "13";
    
    /** 单据状态  调拨单  未处理*/
    public static final String BILLTYPE_PRO_BG_UNDO = "10";
    /** 单据状态  调拨单  已发货*/
    public static final String BILLTYPE_PRO_BG_SEND = "12";
    /** 单据状态  调拨单  已收货*/
    public static final String BILLTYPE_PRO_BG_RECIVE = "13";
    
	/** 单据状态  发货单  未处理*/
    public static final String BILLTYPE_PRM_SD_UNDO = "1";
	/** 单据状态  发货单  已发货*/
    public static final String BILLTYPE_PRM_SD_SEND = "2";
	/** 单据状态  发货单  已收货*/
    public static final String BILLTYPE_PRM_SD_RECIVE = "3";    
    
	/** 调拨业务中处理状态    未调出确认（调入申请单）*/
	public static final String BILLTYPE_PRM_BG_UNRES = "1";
	
	/** 调拨业务中处理状态    已调出确认（调入申请单，调出确认单）*/
	public static final String BILLTYPE_PRM_BGLG_RES = "2";
	
	/** 单据状态  入库单  0：未入库*/
    public static final String BILLTYPE_GR_UNDO = "0";
    
    /** 单据状态  入库单  1：已入库*/
    public static final String BILLTYPE_GR_FINISH = "1";
    
    //============================================业务单据的审核状态================//
	/** 审核区分   未提交审核*/
	public static final String AUDIT_FLAG_UNSUBMIT = "0" ;
	
	/** 审核区分   已提交审核*/
	public static final String AUDIT_FLAG_SUBMIT = "1" ;
	
	/** 审核区分   审核通过*/
	public static final String AUDIT_FLAG_AGREE = "2" ;
	
	/** 审核区分   审核退回*/
	public static final String AUDIT_FLAG_DISAGREE = "3";
	
    /** 审核区分   废弃（自定义审核区分废弃也用这个值）*/
    public static final String AUDIT_FLAG_DISCARD = "D";
	
    //============================================订单的审核状态================//
    /** 订单审核区分   未提交审核*/
    public static final String ODAUDIT_FLAG_UNSUBMIT = "0" ;
    
    /** 订单审核区分   一审中*/
    public static final String ODAUDIT_FLAG_SUBMIT1 = "5" ;
    
    /** 订单审核区分   一审通过*/
    public static final String ODAUDIT_FLAG_AGREE1 = "9";
    
    /** 订单审核区分   一审退回*/
    public static final String ODAUDIT_FLAG_DISAGREE1 = "4";
    
    /** 订单审核区分  二审中 */
    public static final String ODAUDIT_FLAG_SUBMIT2 = "6" ;
    
    /** 订单审核区分   二审通过*/
    public static final String ODAUDIT_FLAG_AGREE = "7";
    
    /** 订单审核区分   二审退回*/
    public static final String ODAUDIT_FLAG_DISAGREE2 = "8";
    
    /** 订单审核区分   审核中*/
    public static final String ODAUDIT_FLAG_SUBMIT_10 = "10";
    
    /** 订单审核区分   审核通过*/
    public static final String ODAUDIT_FLAG_AGREE_11 = "11";
    
    /** 订单审核区分   审核退回*/
    public static final String ODAUDIT_FLAG_DISAGREE_12 = "12";
    
    /** 订单审核区分   三审中*/
    public static final String ODAUDIT_FLAG_SUBMIT3 = "13";
    
    /** 订单审核区分   三审通过*/
    public static final String ODAUDIT_FLAG_AGREE3 = "14";
    
    /** 订单审核区分   三审退回*/
    public static final String ODAUDIT_FLAG_DISAGREE3 = "15";
    
    /** 订单审核区分   四审中*/
    public static final String ODAUDIT_FLAG_SUBMIT4= "16";
    
    /** 订单审核区分   四审通过*/
    public static final String ODAUDIT_FLAG_AGREE4 = "17";
    
    /** 订单审核区分   四审退回*/
    public static final String ODAUDIT_FLAG_DISAGREE4 = "18";
    
    //============================================发货单的审核状态================//
	
    /** 发货单审核区分  二审中 */
    public static final String SDAUDIT_FLAG_SUBMIT2 = "4" ;
    
    /** 发货单审核区分  二审通过 */
    public static final String SDAUDIT_FLAG_AGREE2 = "5" ;
    
    /** 发货单审核区分  二审退回 */
    public static final String SDAUDIT_FLAG_DISAGREE2 = "6" ;
    
    //============================================盘点申请单的审核状态================//
    
    /** 盘点申请审核区分   未提交审核*/
    public static final String CRAUDIT_FLAG_UNSUBMIT = "0" ;
    
    /** 盘点申请审核区分   审核中*/
    public static final String CRAUDIT_FLAG_SUBMIT = "1" ;
    
    /** 盘点申请审核区分   审核通过*/
    public static final String CRAUDIT_FLAG_AGREE = "2";
    
    /** 盘点申请审核区分   审核退回*/
    public static final String CRAUDIT_FLAG_DISAGREE = "3";
    
    /** 盘点申请审核区分  一审退回 */
    public static final String CRAUDIT_FLAG_DISAGREE1 = "4" ;
    
    /** 盘点申请审核区分   一审中*/
    public static final String CRAUDIT_FLAG_SUBMIT1 = "5";
    
    /** 盘点申请审核区分   二审中*/
    public static final String CRAUDIT_FLAG_SUBMIT2 = "6";
    
    /** 盘点申请审核区分   二审通过*/
    public static final String CRAUDIT_FLAG_AGREE2 = "7";
    
    /** 盘点申请审核区分   二审退回*/
    public static final String CRAUDIT_FLAG_DISAGREE2 = "8";

    /**审核区分-审核通过（‘Y’）*/
	public static final String CRAUDIT_FLAG_CONFIRM = "Y";
    //============================================退库申请单的审核状态================//
    
    /** 退库申请审核区分   未提交审核*/
    public static final String RAAUDIT_FLAG_UNSUBMIT = "0" ;
    
    /** 退库申请审核区分   审核中*/
    public static final String RAAUDIT_FLAG_SUBMIT = "1" ;
    
    /** 退库申请审核区分   审核通过*/
    public static final String RAAUDIT_FLAG_AGREE = "2";
    
    /** 退库申请审核区分   审核退回*/
    public static final String RAAUDIT_FLAG_DISAGREE = "3";
    
    /** 退库申请审核区分  一审退回 */
    public static final String RAAUDIT_FLAG_DISAGREE1 = "4" ;
    
    /** 退库申请审核区分   一审中*/
    public static final String RAAUDIT_FLAG_SUBMIT1 = "5";
    
    /** 退库申请审核区分   二审中*/
    public static final String RAAUDIT_FLAG_SUBMIT2 = "6";
    
    /** 退库申请审核区分   二审通过*/
    public static final String RAAUDIT_FLAG_AGREE2 = "7";
    
    /** 退库申请审核区分   二审退回*/
    public static final String RAAUDIT_FLAG_DISAGREE2 = "8";
    
    /** 退库申请审核区分   三审中*/
    public static final String RAAUDIT_FLAG_SUBMIT3 = "9";
    
    /** 退库申请审核区分   三审通过*/
    public static final String RAAUDIT_FLAG_AGREE3 = "10";
    
    /** 退库申请审核区分   三审退回*/
    public static final String RAAUDIT_FLAG_DISAGREE3 = "11";
    
    /** 退库申请审核区分   四审中*/
    public static final String RAAUDIT_FLAG_SUBMIT4 = "12";
    
    /** 退库申请审核区分   四审通过*/
    public static final String RAAUDIT_FLAG_AGREE4 = "13";
    
    /** 退库申请审核区分   四审退回*/
    public static final String RAAUDIT_FLAG_DISAGREE4 = "14";
    
    //============================================入库单的审核状态================//
    
    /** 入库审核区分   未提交审核*/
    public static final String GRAUDIT_FLAG_UNSUBMIT = "0" ;
    
    /** 入库审核区分   审核中*/
    public static final String GRAUDIT_FLAG_SUBMIT = "1" ;
    
    /** 入库审核区分   审核通过*/
    public static final String GRAUDIT_FLAG_AGREE = "2";
    
    /** 入库审核区分   审核退回*/
    public static final String GRAUDIT_FLAG_DISAGREE = "3";
    
    /** 入库审核区分  一审退回 */
    public static final String GRAUDIT_FLAG_DISAGREE1 = "4" ;
    
    /** 入库审核区分   一审中*/
    public static final String GRAUDIT_FLAG_SUBMIT1 = "5";
    
    /** 入库审核区分   二审中*/
    public static final String GRAUDIT_FLAG_SUBMIT2 = "6";
    
    /** 入库审核区分   二审通过*/
    public static final String GRAUDIT_FLAG_AGREE2 = "7";
    
    /** 入库审核区分   二审退回*/
    public static final String GRAUDIT_FLAG_DISAGREE2 = "8";
    
    //============================================盘点单的审核状态================//
    
    /** 盘点审核区分   未提交审核*/
    public static final String CAAUDIT_FLAG_UNSUBMIT = "0" ;
    
    /** 盘点审核区分   审核中*/
    public static final String CAAUDIT_FLAG_SUBMIT = "1" ;
    
    /** 盘点审核区分   审核通过*/
    public static final String CAAUDIT_FLAG_AGREE = "2";
    
    /** 盘点审核区分   审核退回*/
    public static final String CAAUDIT_FLAG_DISAGREE = "3";
    
    /** 盘点审核区分  一审退回 */
    public static final String CAAUDIT_FLAG_DISAGREE1 = "4" ;
    
    /** 盘点审核区分   一审中*/
    public static final String CAAUDIT_FLAG_SUBMIT1 = "5";
    
    /** 盘点审核区分   二审中*/
    public static final String CAAUDIT_FLAG_SUBMIT2 = "6";
    
    /** 盘点审核区分   二审通过*/
    public static final String CAAUDIT_FLAG_AGREE2 = "7";
    
    /** 盘点审核区分   二审退回*/
    public static final String CAAUDIT_FLAG_DISAGREE2 = "8";
    
    //============================================报损单的审核状态================//
    
    /** 报损审核区分   未提交审核*/
    public static final String LSAUDIT_FLAG_UNSUBMIT = "0" ;
    
    /** 报损审核区分   审核中*/
    public static final String LSAUDIT_FLAG_SUBMIT = "1" ;
    
    /** 报损审核区分   审核通过*/
    public static final String LSAUDIT_FLAG_AGREE = "2";
    
    /** 报损审核区分   审核退回*/
    public static final String LSAUDIT_FLAG_DISAGREE = "3";
    
    /** 报损审核区分  一审退回 */
    public static final String LSAUDIT_FLAG_DISAGREE1 = "4" ;
    
    /** 报损审核区分   一审中*/
    public static final String LSAUDIT_FLAG_SUBMIT1 = "5";
    
    /** 报损审核区分   二审中*/
    public static final String LSAUDIT_FLAG_SUBMIT2 = "6";
    
    /** 报损审核区分   二审通过*/
    public static final String LSAUDIT_FLAG_AGREE2 = "7";
    
    /** 报损审核区分   二审退回*/
    public static final String LSAUDIT_FLAG_DISAGREE2 = "8";
    
    //============================================移库单的审核状态================//
    
    /** 移库审核区分   未提交审核*/
    public static final String MVAUDIT_FLAG_UNSUBMIT = "0" ;
    
    /** 移库审核区分   审核中*/
    public static final String MVAUDIT_FLAG_SUBMIT = "1" ;
    
    /** 移库审核区分   审核通过*/
    public static final String MVAUDIT_FLAG_AGREE = "2";
    
    /** 移库审核区分   审核退回*/
    public static final String MVAUDIT_FLAG_DISAGREE = "3";
    
    /** 移库审核区分  一审退回 */
    public static final String MVAUDIT_FLAG_DISAGREE1 = "4" ;
    
    /** 移库审核区分   一审中*/
    public static final String MVAUDIT_FLAG_SUBMIT1 = "5";
    
    /** 移库审核区分   二审中*/
    public static final String MVAUDIT_FLAG_SUBMIT2 = "6";
    
    /** 移库审核区分   二审通过*/
    public static final String MVAUDIT_FLAG_AGREE2 = "7";
    
    /** 移库审核区分   二审退回*/
    public static final String MVAUDIT_FLAG_DISAGREE2 = "8";
    
    
  //============================================退货申请单的审核状态================//
    /** 退货申请审核区分   审核通过*/
    public static final String SAAUDIT_FLAG_AGREE = "2";
    
    /** 退货申请审核区分   审核退回*/
    public static final String SAAUDIT_FLAG_DISAGREE = "D";
    
	//================================================OS WorkFlow 专用==================//
	/** 工作流     操作区分 */
	public static final String OS_MAINKEY_OPERATE_FLAG = "OS_Operate_Flag" ;
	/** 工作流     通过后台操作 */
	public static final String OS_MAINKEY_OPERATE_BACK = "OS_Operate_Back" ;
	/** 工作流     通过pos机操作 */
	public static final String OS_MAINKEY_OPERATE_POS = "OS_Operate_Pos" ;
	
	/** 工作流     产品类型key 区分是给产品用的还是促销品用*/
	public static final String OS_MAINKEY_PROTYPE = "OS_ProType" ;
	public static final String OS_MAINKEY_PROTYPE_PRODUCT = "N" ;
	public static final String OS_MAINKEY_PROTYPE_PROMOTION = "PRM" ;
	/** 工作流     ITEM_KEY 业务类型key */
	public static final String OS_MAINKEY_BILLTYPE = "OS_BillType" ;
	/** 工作流      ITEM_KEY 业务单据ID */
	public static final String OS_MAINKEY_BILLID = "OS_BillID" ;
	/** 工作流     ITEM_KEY 业务单据 编号 */
	public static final String OS_MAINKEY_BILLCODE = "OS_BillCode" ;
	/** 工作流     ITEM_KEY 单据 生成者 */
	public static final String OS_MAINKEY_BILLCREATOR_EMPLOYEE = "OS_BillCreator" ;
	/** 工作流     ITEM_KEY 单据 生成者 */
	public static final String OS_MAINKEY_BILLCREATOR_USER = "OS_BillCreator_User" ;
	/** 工作流     ITEM_KEY 单据 生成者 */
	public static final String OS_MAINKEY_BILLCREATOR_POSITION = "OS_BillCreator_Position" ;
	/** 工作流     ITEM_KEY 单据 生成者 */
	public static final String OS_MAINKEY_BILLCREATOR_DEPART = "OS_BillCreator_Depart" ;
	
	public static final String OS_MAINKEY_BILLRECEIVER_DEPART = "OS_BillReceiver_Depart";
	
	/**工作流 发起者类型缩写*/
	public static final String OS_ROLETYPE_CREATER = "RTC";
	/** 工作流 接收者类型缩写 */
	public static final String OS_ROLETYPE_RECEIVER = "RTR";
	
	/** 工作流     ITEM_KEY 单据 生成者名字 */
	public static final String OS_PSKEY_CREATOR_NAME = "OS_Creator_Name" ;
	/** 工作流     ITEM_KEY 单据 生成者所属部门名字 */
	public static final String OS_PSKEY_DEPART_NAME = "OS_Creator_Depart_Name" ;
	
	/** 工作流     ITEM_KEY 场景区分flag */
	public static final String OS_MAINKEY_SCENE_FLAG = "OS_SceneFlag";
	
	/** 工作流     ITEM_KEY 工作流实例ID */
	public static final String OS_MAINKEY_ENTRYID = "OS_EntryID" ;
	/** 工作流    ITEM_KEY  工作流当前业务操作 */
	public static final String OS_MAINKEY_CURRENT_OPERATE = "OS_Current_Operate" ;
	
	//工作流的流程文件中，在action下会用meta标签描述一些信息，一下为meta标签的name
	public static final String OS_META_ButtonNameCode = "OS_ButtonNameCode";
	public static final String OS_META_ButtonClass = "OS_ButtonClass";
	public static final String OS_META_OperateCode = "OS_OperateCode";
	public static final String OS_META_OperateResultCode = "OS_OperateResultCode";
	//工作流的流程文件中，在step下会用meta标签描述一些信息，用于在画面上显示流程图片；
	public static final String OS_META_StepShowFlag = "OS_StepShowFlag";
	public static final String OS_META_StepShowText = "OS_StepShowText";
	public static final String OS_META_StepShowOrder = "OS_StepShowOrder";	
	
	public static final String OS_META_Rule = "OS_Rule";
	
	
	/** 工作流     任务参与者key*/
	public static final String OS_ACTOR = "participant" ;	
	/** 工作流     任务参与者类型 用户*/
	public static final String OS_ACTOR_TYPE_USER = "userID" ;
	/** 工作流     任务参与者类型 岗位*/
	public static final String OS_ACTOR_TYPE_POSITION = "positionID" ;
	/** 工作流     任务参与者类型 部门*/
	public static final String OS_ACTOR_TYPE_DEPART = "organizationID" ;
	/** 工作流     员工ID*/
	public static final String OS_ACTOR_TYPE_EMPLOYEE = "employeeID" ;
	
	/**工作流     任务参与者类型  缩写  用户*/
	public static final String OS_ACTOR_TYPE_U = "U";
	
	/**工作流     任务参与者类型  缩写  岗位*/
	public static final String OS_ACTOR_TYPE_P = "P";
	
	/**工作流     任务参与者类型  缩写  部门*/
	public static final String OS_ACTOR_TYPE_D = "D";
    
    /**工作流     任务参与者类型  缩写  第三方*/
    public static final String OS_ACTOR_TYPE_T = "T";
    
    /**工作流  无规则*/
    public static final String OS_RULETYPE_NO = "1";
    
    /**工作流  简单模式*/
    public static final String OS_RULETYPE_EASY = "2";
    
    /**工作流  复杂模式(审核)*/
    public static final String OS_RULETYPE_HARD = "3";
    
    /**工作流  cherryshow模式*/
    public static final String OS_RULETYPE_CHERRYSHOW = "4";
    
    /**工作流 代收模式（确认）*/
    public static final String OS_RULETYPE_INSTEAD = "5";
	
	/** 工作流     流程方法中插入，更新，删除等操作影响的行数 key*/
	public static final String OS_CHANGE_COUNT = "ChangeCount" ;

	/** 工作流     业务类型   入库*/
	public static final String OS_BILLTYPE_GR = "GR" ;
	/** 工作流     业务类型   移库*/
	public static final String OS_BILLTYPE_MV = "MV" ;
	/** 工作流     业务类型  报损*/
	public static final String OS_BILLTYPE_LS = "LS" ;	
	/** 工作流     业务类型  盘点*/
	public static final String OS_BILLTYPE_CA = "CA" ;	
	/** 工作流     业务类型  订货*/
	public static final String OS_BILLTYPE_OD = "OD" ;
	/** 工作流     业务类型  发货*/
	public static final String OS_BILLTYPE_SD = "SD" ;
	/** 工作流     业务类型  退库*/
    public static final String OS_BILLTYPE_RR = "RR" ;
    /** 工作流     业务类型  退库申请*/
    public static final String OS_BILLTYPE_RA = "RA" ;
    /** 工作流     业务类型  退库审核*/
    public static final String OS_BILLTYPE_RJ = "RJ" ;
    /** 工作流     业务类型  盘点申请*/
    public static final String OS_BILLTYPE_CR = "CR" ;
    /** 工作流     业务类型  盘点审核*/
    public static final String OS_BILLTYPE_CJ = "CJ" ;
    
	/*** 工作流     业务类型  调入申请 */
	public static final String OS_BILLTYPE_BG = "BG";
	/*** 工作流     业务类型  调出确认申请 */
	public static final String OS_BILLTYPE_LG = "LG";
    /*** 工作流     业务类型  调入申请审核 */
    public static final String OS_BILLTYPE_BJ  = "BJ";
	
	/*** 工作流     业务类型  销售 */
	public static final String OS_BILLTYPE_NS="NS";
	
	/** 工作流     业务类型  销售退货*/
	public static final String OS_BILLTYPE_SA = "SA" ;
	
	/** 工作流     业务类型  销售退货审核*/
	public static final String OS_BILLTYPE_SJ = "SJ" ;
	
	/**130     退库申请单作成*/
	public static final String OPERATE_SA_CREATE = "160";
	
	/**161     退库申请单审核*/
	public static final String OPERATE_SA_AUDIT = "161";
	
	/**162     退库申请单确认*/
	public static final String OPERATE_SA_CONFIRM = "162";
	
	/**163	退货*/
	public static final String OPERATE_SA="163";
	
    /** 工作流     权限范围     管辖 */
    public static final String OS_PRIVILEGEFLAG_FOLLOW = "1";
    /** 工作流     权限范围     关注 */
    public static final String OS_PRIVILEGEFLAG_LIKE = "0";
    /** 工作流     权限范围     管辖和关注 */
    public static final String OS_PRIVILEGEFLAG_ALL = "A";
    //============================================业务中涉及到的操作   产品部分================//	
	/**10	入库*/
	public static final String OPERATE_GR="10";
	
	/**11	入库单审核*/
	public static final String OPERATE_GR_AUDIT="11";
	/**12	入库单修改*/
	public static final String OPERATE_GR_EDIT="12";
	/**13	入库单删除*/
	public static final String OPERATE_GR_DELETE="13";
	/**15       确认入库*/
    public static final String OPERATE_GR_CONFIRM="15";
    /**16   入库单二审*/
    public static final String OPERATE_GR_AUDIT2="16";
	/**20	出库*/
	public static final String OPERATE_OT="20";
	/**21	出库单审核*/
	public static final String OPERATE_OT_AUDIT="21";
	/**22	出库单修改*/
	public static final String OPERATE_OT_EDIT="22";
	/**23	出库单删除*/
	public static final String OPERATE_OT_DELETE="23";
	/**30	订货*/
	public static final String OPERATE_OD="30";
	/**31	订货单审核*/
	public static final String OPERATE_OD_AUDIT="31";
	/**32	订货单修改*/
	public static final String OPERATE_OD_EDIT="32";
	/**33	订货单删除*/
	public static final String OPERATE_OD_DELETE="33";
	/**34	订货单审核  二审*/
	public static final String OPERATE_OD_AUDIT_SEC="34";
    /**35   订货单 生成订货单 */
    public static final String OPERATE_OD_CREATE = "35";
    /**36   订货单 提交 */
    public static final String OPERATE_OD_SUBMIT = "36";
    /**37   订货单审核  三审*/
    public static final String OPERATE_OD_AUDIT3="37";
    /**38  订货单审核  四审*/
    public static final String OPERATE_OD_AUDIT4="38";
    
    /**订单审核状态的枚举*/
    public static enum ENUMODAUDIT {
        audit1(OPERATE_OD_AUDIT),
        audit2(OPERATE_OD_AUDIT_SEC),
        audit3(OPERATE_OD_AUDIT3),
        audit4(OPERATE_OD_AUDIT4);
        
        /**判断当前操作状态是否是审核状态*/
        public static boolean isAudit(Object key) {
            boolean flag = false;
            ENUMODAUDIT[] eAudits = ENUMODAUDIT.values();
            for(ENUMODAUDIT eAudit : eAudits) {
                if(eAudit.getAuditCode().equals(key)) {
                    flag = true;
                    break;
                }
            }
            return flag;
        }
        
        ENUMODAUDIT(String auditCode) {
            this.auditCode = auditCode;
        }
        
        public String getAuditCode() {
            return auditCode;
        }

        private String auditCode;
    }
    	
	/**40	发货*/
	public static final String OPERATE_SD="40";
	/**41	发货单审核*/
	public static final String OPERATE_SD_AUDIT="41";
	/**42	发货单修改*/
	public static final String OPERATE_SD_EDIT="42";
	/**43	发货单删除*/
	public static final String OPERATE_SD_DELETE="43";
	/**44       生成发货单*/
	public static final String OPERATE_SD_CREATE="44";
	/**46       发货单二审*/
    public static final String OPERATE_SD_AUDIT2="46";
	
	/**50	收货*/
	public static final String OPERATE_RD="50";
	/**51	收货单审核*/
	/**52	收货单修改*/
	/**53	收货单删除*/
	/**60	退库*/
	public static final String OPERATE_RR="60";
	/**61	退库单审核*/
	public static final String OPERATE_RR_AUDIT = "61";
	/**62	退库单修改*/
	public static final String OPERATE_RR_EDIT = "62";
	/**63	退库单删除*/
	public static final String OPERATE_RR_DELETE = "63";
	/**70	调入*/
	public static final String OPERATE_BG="70";
	/**71	调入单审核*/
	public static final String OPERATE_BG_AUDIT="71";
	/**72	调入单修改*/
	public static final String OPERATE_BG_EDIT="72";
	/**73	调入单删除*/
	public static final String OPERATE_BG_DELETE="73";
    /**74   调入单做成*/
    public static final String OPERATE_BG_CREATE="74";
    /**75   调拨申请*/
    public static final String OPERATE_AC="75";
    /**76   调拨申请单审核*/
    public static final String OPERATE_AC_AUDIT="76";
	/**80	调出*/
	public static final String OPERATE_LG="80";
	/**81	调出单审核*/
	public static final String OPERATE_LG_AUDIT="81";
	/**82	调出单修改*/
	public static final String OPERATE_LG_EDIT="82";
	/**83	调出单删除*/
	public static final String OPERATE_LG_DELETE="83";
    /**84   调出单作成*/
    public static final String OPERATE_LG_CREATE="84";
	/**90	盘点*/
	public static final String OPERATE_CA="90";
	/**91	盘点单审核*/
	public static final String OPERATE_CA_AUDIT="91";
	/**92	盘点单修改*/
	public static final String OPERATE_CA_EDIT="92";
	/**93	盘点单删除*/
	public static final String OPERATE_CA_DELETE="93";
	/**94   盘点单作成*/
    public static final String OPERATE_CA_CREATE ="94";
    /**95   盘点单二审*/
    public static final String OPERATE_CA_AUDIT2 ="95";
	/**100	移库*/
	public static final String OPERATE_MV="100";
	/**101	移库单审核*/
	public static final String OPERATE_MV_AUDIT="101";
	/**102	移库单修改*/
	public static final String OPERATE_MV_EDIT="102";
	/**103	移库单删除*/
	public static final String OPERATE_MV_DELETE="103";
	/**104  移库单作成*/
    public static final String OPERATE_MV_CREATE="104";
    /**101  移库单二审*/
    public static final String OPERATE_MV_AUDIT2="105";
	/**110	报损*/
	public static final String OPERATE_LS="110";
	/**111	报损单审核*/
	public static final String OPERATE_LS_AUDIT="111";
	/**112	报损单修改*/
	public static final String OPERATE_LS_EDIT="112";
	/**113	报损单删除*/
    public static final String OPERATE_LS_DELETE = "113";
    /**114  报损单作成*/
    public static final String OPERATE_LS_CREATE = "114";
    /**115  报损单二审*/
    public static final String OPERATE_LS_AUDIT2 = "115";

	/**130     退库申请单作成*/
	public static final String OPERATE_RA_CREATE = "130";
	/**131     退库申请单审核*/
	public static final String OPERATE_RA_AUDIT = "131";
	/**132     退库申请单修改*/
	public static final String OPERATE_RA_EDIT = "132";
	/**133     退库申请单删除*/
	public static final String OPERATE_RA_DELETE = "133";
	/**134     确认退库*/
	public static final String OPERATE_RA_CONFIRM = "134";
	/**135  SAP确认*/
    public static final String OPERATE_RA_SAPCONFIRM = "135";
    /**136  物流确认*/
    public static final String OPERATE_RA_LOGISTICSCONFIRM = "136";
    /**137  退库申请单二审*/
    public static final String OPERATE_RA_AUDIT2 = "137";
    /**138  退库申请单三审*/
    public static final String OPERATE_RA_AUDIT3 = "138";
    /**139  退库申请单四审*/
    public static final String OPERATE_RA_AUDIT4 = "139";
	
	/**140     盘点申请单作成 作成*/
    public static final String OPERATE_CR_CREATE = "140";
    /**141  盘点申请单审核*/
    public static final String OPERATE_CR_AUDIT = "141";
    /**142  盘点申请单修改*/
    public static final String OPERATE_CR_EDIT = "142";
    /**143  盘点申请单删除*/
    public static final String OPERATE_CR_DELETE = "143";
    /**144  柜台确认盘点*/
    public static final String OPERATE_CR_CONFIRM = "144";
    /**145  盘点申请单二审*/
    public static final String OPERATE_CR_AUDIT2 = "145";
    
    /**0    销售单草稿状态*/
    public static final String BILLSTATE_SL_DRAFT = "0";
    /**10   销售单审核中状态*/
    public static final String BILLSTATE_SL_VERIFIED = "10";
    /**20   销售单待发货状态*/
    public static final String BILLSTATE_SL_UNDELIVER = "20";
    /**80   销售单废弃状态*/
    public static final String BILLSTATE_SL_ABANDON = "80";
    /**99   销售单完成状态*/
    public static final String BILLSTATE_SL_FINISH = "99";
    
    /**150     后台销售单作成*/
    public static final String OPERATE_SL = "150";
    /**151     后台销售单审核*/
    public static final String OPERATE_SL_AUDIT = "151";
    /**152     后台销售单修改*/
    public static final String OPERATE_SL_EDIT = "152";
    /**153     后台销售单删除*/
    public static final String OPERATE_SL_DELETE = "153";
    
	/**100	促销品  发货单审核    照顾以前的数据*/
	public static final String OPERATE_SD_AUDIT_PRM="100";
	/**200	促销品  发货单审核    照顾以前的数据*/
	public static final String OPERATE_BG_AUDIT_PRM="200";
//	/**42	促销品  发货单修改*/
//	/**43	促销品  发货单删除*/
//	/**50	促销品  收货*/
//	public static final String OPERATE_RD="50";
//	/**51	促销品  收货单审核*/
//	/**52	促销品  收货单修改*/
//	/**53	促销品  收货单删除*/

	//==========================================================业务类型定义==================//
	/** 库存业务类型   发货*/
	public static final String BUSINESS_TYPE_DELIVER_SEND = "1";
	
	/** 库存业务类型  收货*/
	public static final String BUSINESS_TYPE_DELIVER_RECEIVE = "2";
	
	/** 库存业务类型   退货*/
	public static final String BUSINESS_TYPE_DELIVER_RETURN = "3";
	
	/** 库存业务类型  接受退库*/
	public static final String BUSINESS_TYPE_CANCELLING = "4";
	
	/** 库存业务类型  调拨申请*/
	public static final String BUSINESS_TYPE_ALLOCATION_REQUEST = "5";
	
	/** 库存业务类型  调拨确认*/
	public static final String BUSINESS_TYPE_ALLOCATION_RESPONSE = "6";
	
	/** 库存业务类型  入库*/
	public static final String BUSINESS_TYPE_STORAGE_IN = "7";
	
	/** 库存业务类型  出库*/
	public static final String BUSINESS_TYPE_STORAGE_OUT = "8";
	
	/** 库存业务类型  盘点*/
	public static final String BUSINESS_TYPE_STOCKTAKE = "P";
	
	/** 库存业务类型  礼品*/
	public static final String BUSINESS_TYPE_GIFT = "S";
	
	/** 库存业务类型  销售出库*/
	public static final String BUSINESS_TYPE_SALE_OUT = "N";
	
	/** 库存业务类型  销售入库*/
	public static final String BUSINESS_TYPE_SALE_IN = "R";
	
	/** 发货*/
	public static final String BUSINESS_TYPE_SD = "SD";	
	/** 收货*/
	public static final String BUSINESS_TYPE_RD = "RD";
	/** 调入*/
	public static final String BUSINESS_TYPE_BG = "BG";	
	/** 调出*/
	public static final String BUSINESS_TYPE_LG = "LG";	
	/** 报损*/
	public static final String BUSINESS_TYPE_LS = "LS";
	/** 移库*/
	public static final String BUSINESS_TYPE_MV = "MV";
	/** 盘点*/
	public static final String BUSINESS_TYPE_CA = "CA";
	/** 入库*/
	public static final String BUSINESS_TYPE_GR = "GR";
	/** 退库*/
	public static final String BUSINESS_TYPE_RR = "RR";
    /** 接收退库*/
    public static final String BUSINESS_TYPE_AR = "AR";  
    /**销售出库*/
    public static final String BUSINESS_TYPE_NS = "NS";
    /**销售入库*/
    public static final String BUSINESS_TYPE_SR = "SR";
    /**自由出库*/
    public static final String BUSINESS_TYPE_OT = "OT";
    /**礼品领用*/
    public static final String BUSINESS_TYPE_SP = "SP";
    //===================================================
    
    //=================================================逻辑仓库业务
	/**后台逻辑仓库业务配置用业务 OD 接收订货*/
	public static final String LOGICDEPOT_BACKEND_OD="OD";
	
	/**后台逻辑仓库业务配置用业务 SD 发货*/
	public static final String LOGICDEPOT_BACKEND_SD="SD";
	
	/**后台逻辑仓库业务配置用业务 AR 接受退库*/
	public static final String LOGICDEPOT_BACKEND_AR="AR";
	
	/**后台逻辑仓库业务配置用业务 CA 盘点*/
	public static final String LOGICDEPOT_BACKEND_CA="CA";
	
	/**后台逻辑仓库业务配置用业务 RD 收货*/
	public static final String LOGICDEPOT_BACKEND_RD="RD";
	
	/**后台逻辑仓库业务配置用业务 MV 移库*/
	public static final String LOGICDEPOT_BACKEND_MV="MV";
	
	/**后台逻辑仓库业务配置用业务 LS 报损*/
	public static final String LOGICDEPOT_BACKEND_LS="LS";
	
	/**后台逻辑仓库业务配置用业务 GR 入库*/
	public static final String LOGICDEPOT_BACKEND_GR="GR";
	
    /**后台逻辑仓库业务配置用业务 BG 调入*/
    public static final String LOGICDEPOT_BACKEND_BG="BG";
    
    /**后台逻辑仓库业务配置用业务 LG 调出*/
    public static final String LOGICDEPOT_BACKEND_LG="LG";
    
    /**后台逻辑仓库业务配置用业务 RR 退库*/
    public static final String LOGICDEPOT_BACKEND_RR="RR";
	
	/**终端逻辑仓库业务配置用业务 OD 订货*/
    public static final String LOGICDEPOT_TERMINAL_OD="OD";
    
    /**终端逻辑仓库业务配置用业务 RR 退库*/
    public static final String LOGICDEPOT_TERMINAL_RR="RR";

    /**终端逻辑仓库业务配置用业务 GR 入库*/
    public static final String LOGICDEPOT_TERMINAL_GR="GR";
    
    /**终端逻辑仓库业务配置用业务NS 销售*/
    public static final String LOGICDEPOT_TERMINAL_NS="NS";
    
    /**终端逻辑仓库业务配置用业务BG 调入*/
    public static final String LOGICDEPOT_TERMINAL_BG="BG";
    
    /**终端逻辑仓库业务配置用业务LG 调出*/
    public static final String LOGICDEPOT_TERMINAL_LG="LG";
    
    /**终端逻辑仓库业务配置用业务CA 盘点*/
    public static final String LOGICDEPOT_TERMINAL_CA="CA";
	
	public static final String CHERRY_WIT_SECURITY_CONTEXT_KEY = "CHERRY_WIT_SECURITY_CONTEXT";
	
	//======================= 报表导出共通 ================//	
	/** 报表模板后缀名  */
	public static final String JASPER=".jasper";
	/** 报表模板中的图片后缀名  */
	public static final String JPG=".jpg";
	/** 报表模板默认文件夹  */
	public static final String JASPER_DEF_DIR="def";
	/** 报表页面ID  */
	public static final String PAGE_ID="pageId";
	/** 单据ID  */
	public static final String BILL_ID="billId";
	/** 单据号  */
	public static final String BILL_NO="billNo";
	/** 报表导出打印每页显示行数  */
	public static final int NUM_OF_PAGE_DEF = 12;
	
	/** 报表模式 */
	public static final String REPORT_MODE= "reportMode";
	/** 报表模式_一览模式  */
	public static final int REPORT_MODE_0= 0;
	/** 报表模式_详细模式 */
	public static final int REPORT_MODE_1= 1;
	
	/** 业务单据  */
	public static final String TRADE_TYPE= "tradeType";
	/** 业务单据 ：发货业务单据  */
	public static final int TRADE_TYPE_1= 1;
	/** 业务单据 ：收货业务单据*/
	public static final int TRADE_TYPE_2= 2;
	
	/** 打印状态：未打印  */
	public static final int PRINT_STATUS_0= 0;
	
	/** 打印状态：已打印 */
	public static final int PRINT_STATUS_1= 1;
	
	public static final String CHAR_ENCODING = "UTF-8";
	
	public static final String CHAR_ENCODING_ISO = "ISO8859-1";
	
	public static final String CHAR_ENCODING_GBK = "GBK";
	
	public static final String EXPORTTYPE = "exportType";
	
	public static final String EXPORTNAME = "exportName";
	
	public static final String EXPORTTYPE_PDF = "pdf";
	
	public static final String EXPORTTYPE_XLS = "xls";
	
	public static final String EXPORTTYPE_HTML = "html";
	
	public static final String EXPORTTYPE_XML = "xml";
	
	/** Excel导出每张工作表最大显示行数 无系统配置项时使用 */
	public static final String DEFAULT_EXCEL_MAXROW = "50000";
	
	/** Excel导出最大数据量 */
	public static final int EXPORTEXCEL_MAXCOUNT = 60000;
	
	/** Csv导出最大数据量 */
	public static final int EXPORTCSV_MAXCOUNT = 5000000;
	
	//======================= 选择范围共通条 ================//
	
	public static final String MODE_DEPART = "dpat";
	
	public static final String MODE_AREA = "area";
	
	public static final String MODE_CHANNEL = "chan";
	
	public static final String MODE_DEPOT = "dpot";
	
	//======================== 系统基本信息配置=============//
	/**仓库业务配置  按权限大小关系*/
	public static final String DEPOTBUSINESS_DEPART = "1";
	/**仓库业务配置  按区域大小关系*/
	public static final String DEPOTBUSINESS_REGION = "0";
	
	// ====================== MQ消息共通常量 ============================ //		
	/** 消息版本号标记 */
	public static final String MESSAGE_VERSION = "[Version]";
	
	/** 消息版本便标记 */
	public static final String MESSAGE_VERSION_SIGN = "[Version],";
	
	/** 消息版本标题 */
	public static final String MESSAGE_VERSION_TITLE = "Version";
	
	/** 消息命令类型标题 */
	public static final String MESSAGE_TYPE_TITLE = "Type";
	
	/** 消息类型标记 */
	public static final String MESSAGE_TYPE_SIGN = "[Type],";
	
	/** 消息数据类型标记*/
	public static final String MESSAGE_DATATYPE_SIGN = "[DataType],";
	
	/** 消息数据类型标题*/
	public static final String MESSAGE_DATATYPE_TITLE = "DataType";
	
	/** 消息体数据--xml和json格式的数据标记*/
	public static final String DATALINE_JSON_XML_SIGN = "[DataLine],";
	
	/** 消息体数据--xml和json格式的数据标题*/
	public static final String DATALINE_JSON_XML = "DataLine";
	
	/** 明细数据标题*/
	public static final String DETAIL_MESSAGE_TITLE = "DetailDataLine";
	
	/** 消息体数据类型--逗号分隔的字符串*/
	public static final String DATATYPE_TEXT_PLAIN = "text/plain";
	
	/** 消息体数据类型--xml语义格式*/
	public static final String DATATYPE_APPLICATION_XML = "application/xml";
	
	/** 消息体数据类型--json数据格式*/
	public static final String DATATYPE_APPLICATION_JSON = "application/json";
	
	/** 字段名标记 */
	public static final String MESSAGE_COMMLINE = "[CommLine]";
	
	/** 主数据标记 */
	public static final String MAIN_MESSAGE_SIGN = "[MainDataLine]";
	
	/** 明细数据标记 */
	public static final String DETAIL_MESSAGE_SIGN = "[DetailDataLine]";
	
	/** 结束标记 */
	public static final String END_MESSAGE_SIGN = "[End]";
	
	/** 逗号分割符 */
	public static final String MESSAGE_SPLIT_COMMA = ",";
	
	/** 全角逗号分割符 */
	public static final String MESSAGE_SPLIT_FULL_COMMA = "，";
	
	/** 换行符 */
	public static final String MESSAGE_LINE_BREAK = "\r\n";
	
	/** U盘绑定信息 */
	public static final String MESSAGE_TYPE_UDISKBIND = "AMQ.003.001";
	
	/** 全角冒号分割符 */
	public static final String MESSAGE_SPLIT_COLON = "：";
	
	/** 数据插入方标志:CHERRY */
	public static final String MQ_SOURCE_CHERRY = "CHERRY";
	
	/** 消息方向:发送 */
	public static final String MQ_SENDORRECE_S = "S";
	
	/** MongoDB 业务流水表 **/
	public static final String MQ_BUS_LOG_COLL_NAME = "MGO_BusinessLog";
	
	/** MQ消息发送日志表 **/
	public static final String MGO_MQSENDLOG = "MGO_MQSendLog";
	
	/** MQ收发日志表（新后台内部发送消息用） **/
	public static final String MGO_MQLOG = "MGO_MQLog";
	
	/** 尝试插入MongoDB的最大次数 **/
	public static final int MGO_MAX_RETRY = 3;
	
	/** 延迟等待时间(ms) **/
	public static final long MGO_SLEEP_TIME = 50;
	
	/** 消息发送接收标志位:未比对 */
	public static final String MQ_RECEIVEFLAG_0 = "0";
	
	/** 默认修改回数 */
	public static final String DEFAULT_MODIFYCOUNTS = "0";
	
	/** 业务主体  会员 */
	public static final String TRADEENTITY_0 = "0";
	
	/** 业务主体  内部员工 */
	public static final String TRADEENTITY_1 = "1";
	
	/** 新后台 ->witpos队列名 */
	public static final String CHERRYTOPOSMSGQUEUE = "cherryToPosMsgQueue";
	
	/** 新后台 ->witpos队列名 */
	public static final String CHERRYTOPOSMEMBER = "cherryToPosMEMBER";
	
	/** 新后台 ->witpos队列名 */
	public static final String CHERRYTOPOSSP = "cherryToPosSP";
	
	/** 新后台 ->witpos队列名(通知) */
	public static final String cherryToPosCMD = "cherryToPosCMD";
	
	/** witpos->新后台队列名 */
	public static final String POSTOCHERRYMSGQUEUE = "posToCherryMsgQueue";
	
	/** 新后台->batch队列名 */
	public static final String CHERRYTOBATCHMSGQUEUE = "cherryToBatchMsgQueue";
	
	/** 消息体信息key */
	public static final String MSGDATA = "msgData";
	
	/** 消息队列key */
	public static final String MSGQUEUENAME = "msgQueueName";
	
	/** 消息数据库日志key */
	public static final String MSGLOG = "msgLog";
	
	/** 消息mongodb日志key */
	public static final String MSGMONGOLOG = "msgMongoLog";
	
	/** 等级和化妆次数实时重算MQ消息业务类型 */
	public static final String MESSAGE_TYPE_MR = "MR";
	
	/** 刷新业务处理器MQ消息业务类型 */
	public static final String MESSAGE_TYPE_RF = "RF";
	
	/** 累计积分MQ消息业务类型 */
	public static final String MESSAGE_TYPE_PT = "PT";
	
	/** 初始积分MQ消息业务类型 */
	public static final String MESSAGE_TYPE_MT = "MT";
	
	/** 发送优惠券MQ消息业务类型 */
	public static final String MESSAGE_TYPE_TC = "TC";
	
	/** 等级和化妆次数实时重算MQ消息版本 */
	public static final String MESSAGE_VERSION_MR = "AMQ.101.001";
	
	/** 刷新业务处理器MQ消息版本 */
	public static final String MESSAGE_VERSION_RF = "AMQ.108.001";
	
	/** 实时下发会员俱乐部MQ消息版本 */
	public static final String MESSAGE_VERSION_CB = "AMQ.110.001";
	
	/** 累计积分MQ消息消息版本 */
	public static final String MESSAGE_VERSION_PT = "AMQ.104.001";
	
	/** 发送优惠券MQ消息版本 */
	public static final String MESSAGE_VERSION_TC = "AMQ.114.001";
	
	/** 等级和化妆次数实时重算MQ消息业务类型 */
	public static final String MESSAGE_TYPE_1001 = "1001";
	
	/** 刷新业务处理器MQ消息业务类型 */
	public static final String MESSAGE_TYPE_1008 = "1008";
	
	/** 实时下发会员俱乐部MQ消息业务类型 */
	public static final String MESSAGE_TYPE_1010 = "1010";
	
	/** 发送优惠券MQ消息业务类型 */
	public static final String MESSAGE_TYPE_1014 = "1014";
	
	/** 实时刷新数据权限MQ消息业务类型 */
	public static final String MESSAGE_TYPE_RP = "RP";
	
	/** 实时下发会员俱乐部 */
	public static final String MESSAGE_TYPE_CB = "CB";
	
	/** 实时刷新数据权限MQ消息版本 */
	public static final String MESSAGE_VERSION_RP = "AMQ.105.001";
	
	/** 累计积分MQ消息业务类型 */
	public static final String MESSAGE_TYPE_1004 = "1004";
	
	/** 实时刷新数据权限MQ消息业务类型 */
	public static final String MESSAGE_TYPE_1005 = "1005";
	
	/** 沟通调度MQ消息版本 */
	public static final String MESSAGE_VERSION_PX = "AMQ.101.001";
	
	/** 沟通调度MQ消息业务类型 */
	public static final String MESSAGE_TYPE_PX = "PX";
	
	/** 实时刷新数据权限MQ消息业务类型 */
	public static final String MESSAGE_TYPE_0014 = "0014";
	
	/** 新后台 ->新后台队列名 */
	public static final String CHERRYTOCHERRYMSGQUEUE = "cherryToCherryMsgQueue";
	
	/** 实时刷新数据权限队列名 */
	public static final String CHERRYPRIVILEGEMSGQUEUE = "cherryPrivilegeMsgQueue";
	
	/**积分维护消息处理*/
	public static final String CHERRYPOINTMSGQUEUE = "cherryPointMsgQueue";
	
	/**微信消息处理*/
	public static final String CHERRYTOWECHATMSGQUEUE = "cherryToWeChatMsgQueue";
	
	/**模板类型:会员活动*/
	public static final String TEMPLATE_TYPE_0 = "0";
	
	/**模板类型:活动组*/
	public static final String TEMPLATE_TYPE_1 = "1";
	
	/**模板区分:文件头*/
	public static final String TEMPLATEKBN_0 = "0";
	
	/**模板区分:文件主体*/
	public static final String TEMPLATEKBN_1 = "1";
	
	/**会员活动类型:共通*/
	public static final String CAMPAIGN_TYPE_ALL = "A";
	
	/**集合标识符:共通*/
	public static final String GROUP_CODE_ALL = "A";
	
	/** 会员等级状态：默认*/
	public static final String LEVELSTATUS_1 = "1";
	
	/** 会员等级状态：实际等级*/
	public static final String LEVELSTATUS_2 = "2";
	
	/** 会员等级状态：无效*/
	public static final String LEVELSTATUS_3 = "3";

	/** 权限类型等级定义 */
	public static enum PRIVILEGETYPE {
		
		// 只包括管辖范围
		PRIVILEGETYPE0(0, 1),
		// 包括管辖和关注范围
		PRIVILEGETYPE1(1, 4),
		// 包括管辖和直接关注范围
		PRIVILEGETYPE2(2, 2),
		// 包括管辖和关注范围(所属部门除外)
		PRIVILEGETYPE3(3, 3);
		
		public static int getGradeByType(int type) {
			
			PRIVILEGETYPE[] privilegeTypes = PRIVILEGETYPE.values();
			for(PRIVILEGETYPE privilegeType : privilegeTypes) {
				if(privilegeType.getType() == type) {
					return privilegeType.getGrade();
				}
			}
			return -1;
		}
		
		public static int compare(int type1, int type2) {
			if(getGradeByType(type1) > getGradeByType(type2)) {
				return 1;
			} else if(getGradeByType(type1) < getGradeByType(type2)) {
				return -1;
			} else {
				return 0;
			}
		}
		
		PRIVILEGETYPE(int type, int grade) {
			this.type = type;
			this.grade = grade;
		}
		
		public int getType() {
			return type;
		}
		public int getGrade() {
			return grade;
		}

		private int type;
		private int grade;
	}
	
	/** 内容代号：000001*/
	public static final String CONTENTCODE1 = "000001";
	
	/** 内容代号：000002*/
	public static final String CONTENTCODE2 = "000002";
	
	/** 内容代号：000002*/
	public static final String CONTENTCODE3 = "000003";
	
	/** 内容代号：000004*/
	public static final String CONTENTCODE4 = "000004";
	
	/** 内容1：系统默认等级*/
	public static final String CONTENT1 = "给予系统默认等级";
	
	/** 内容2：会员资料上传*/
	public static final String CONTENT2 = "会员资料上传";
	
	/** 内容3：非关联退货引起等级调整*/
	public static final String CONTENT3 = "非关联退货引起等级调整";
	
	/** 内容4：重算后给予默认等级*/
	public static final String CONTENT4 = "重算后给予默认等级";
	
	/**  */
	public static enum RULECONTENT {
		rulecontent1(CONTENTCODE1, CONTENT1),
		rulecontent2(CONTENTCODE2, CONTENT2),
		rulecontent3(CONTENTCODE3, CONTENT3),
		rulecontent4(CONTENTCODE4, CONTENT4);
		public static String getRuleContent(Object key) {
			return getValue(key);
		}

		private static String getValue(Object key) {
			if(null == key || "".equals(key)) {
				return null;
			}
			RULECONTENT[] opers = RULECONTENT.values();
			for(RULECONTENT oper : opers) {
				if(oper.getKey().equals(key)) {
					return oper.getContent();
				}
			}
			return null;
		}
		RULECONTENT(String key, String content) {
			this.key = key;
			this.content = content;
		}
		
		public String getKey() {
			return key;
		}

		public String getContent() {
			return content;
		}

		public void setContent(String content) {
			this.content = content;
		}


		private String key;
		private String content;
	}
	
	/** 共通的规则内容*/
	public static final int RULE_ALL = -8888;
	
	/**业务处理器类型:退货规则*/
	public static final String HANDLERTYPE_RE06 = "RE06";
	
	/**业务处理器类型:会员化妆次数*/
	public static final String HANDLERTYPE_RE03 = "RE03";
	
	/**业务处理器类型:清零处理*/
	public static final String HANDLERTYPE_RE05 = "RE05";
	
	/**业务处理器类型:降级处理*/
	public static final String HANDLERTYPE_RE07 = "RE07";
	
	/**业务处理器类型:建档处理*/
	public static final String HANDLERTYPE_RE08 = "RE08";
	
	/**业务处理器类型:计算升级所需金额处理*/
	public static final String HANDLERTYPE_RE01 = "RE01";
	
	/**业务处理器类型:积分计算*/
	public static final String HANDLERTYPE_PT01 = "PT01";
	
	/**业务处理器类型:换卡扣积分*/
	public static final String HANDLERTYPE_PT02 = "PT02";
	
	/**业务处理器类型:积分清零*/
	public static final String HANDLERTYPE_PT04 = "PT04";
	
	/**业务处理器类型:推荐会员积分奖励*/
	public static final String HANDLERTYPE_PT05 = "PT05";
	
	/** 有效性区分   有效*/
	public static final String VALIDFLAG_ENABLE = "1";
	
	/** 有效性区分   无效*/
	public static final String VALIDFLAG_DISABLE = "0";
	
	/** BA与柜台绑定关系变化	新增*/
	public static final String BA_COUNTERS_ADD = "1";
	
	/** BA与柜台绑定关系变化	解除*/
	public static final String BA_COUNTERS_DELETE = "-1";
	
	/** BA与柜台绑定关系变化	保持*/
	public static final String BA_COUNTERS_KEEP = "0";
	
	/** 会员回访任务进行状态   进行中*/
	public static final String TASK_FLAG_PEOCEED = "0" ;
	
	/** 会员回访任务进行状态   已取消*/
	public static final String TASK_FLAG_CANCEL = "1" ;
	
	/** 会员回访任务进行状态   已完成*/
	public static final String TASK_FLAG_AGREE = "2" ;
	
	/** 会员回访结果状态   未回访*/
	public static final String TASK_RESULT_1 = "VISIT_RESULT001" ;
	
	/** 会员回访结果状态 未接通*/
	public static final String TASK_RESULT_2 = "VISIT_RESULT002" ;
	
	/** 会员回访结果状态   有效回访*/
	public static final String TASK_RESULT_3 = "VISIT_RESULT003" ;
	
	/** 会员回访结果状态   无效回访*/
	public static final String TASK_RESULT_4 = "VISIT_RESULT004" ;
	
	//======================== BI配置属性信息 =============//
	/** BI配置属性信息   IP*/
	public static final String BIConfig_IP = "IP";
	
	/** BI配置属性信息   Port*/
	public static final String BIConfig_Port = "Port";
	
	/** BI配置属性信息   WebURL*/
	public static final String BIConfig_WebURL = "WebURL";
	
	/** BI配置属性信息   UserName*/
	public static final String BIConfig_UserName = "UserName";
	
	/** BI配置属性信息   UserPassword*/
	public static final String BIConfig_UserPassword = "UserPassword";
	
	//======================== WebService =============//
	/** 访问WebService的方法类型：GET方法 */
	public static final String WS_TYPE_GET = "GET";
	
	/** 访问WebService的方法类型：POST方法 */
	public static final String WS_TYPE_POST = "POST";
	
	/** 访问WebService的方法类型：PUT方法 */
	public static final String WS_TYPE_PUT = "PUT";
	
	/** 访问WebService的方法类型：DELETE方法 */
	public static final String WS_TYPE_DELETE = "DELETE";
	
	/** 访问会员信息的PATH */
	public static final String WS_MEMINFO = "memInfo";
	
	/** 访问会员销售信息的PATH */
	public static final String WS_MEMSALEINFO = "memSaleInfo";
	
	/** 实时刷新会员信息的PATH */
	public static final String WS_REFRESH = "refresh";

	//======================= 沟通模块业务 ================//	
	/** 沟通设置初始化    ROWID */
	public static final String COMMUNICATION_INIT_ROWNUMBER = "1";
	
	/** 沟通设置初始化    页面编号 */
	public static final String COMMUNICATION_INIT_CODE = "1";
	
	/** 沟通设置初始化    沟通名称 */
	public static final String COMMUNICATION_INIT_NAME = "第1次沟通";
	
	/** 沟通设置初始化    沟通发送时间点 */
	public static final String COMMUNICATION_INIT_SENDTIME = "10:00";
	
	/** 沟通设置初始化    沟通提前或推迟天数 */
	public static final String COMMUNICATION_INIT_DATEVALUE = "1";
	
	/** 沟通设置初始化    循环沟通信息发送月日 */
	public static final String COMMUNICATION_INIT_DAYVALUE = "1";
	
	/** 沟通设置初始化    沟通条件值 */
	public static final String COMMUNICATION_INIT_CONDITIONVALUE = "1000";
	
	/** 沟通设置初始化    全部沟通对象 */
	public static final String COMMUNICATION_INIT_ALL = "不限";
	
	/** 沟通设置初始化    全部会员对象*/
	public static final String COMMUNICATION_INIT_ALLMEMBER = "全部会员";
	
	/** 沟通设置初始化   默认 客户类型1*/
	public static final String CUSTOMERTYPE_1 = "1";
	
	/** 沟通设置初始化    默认客户类型2*/
	public static final String CUSTOMERTYPE_2 = "4";
	
	/** 沟通设置初始化    默认沟通对象搜索记录类型*/
	public static final String RECORDTYPE = "1";
	
	/** 沟通设置初始化    默认沟通对象来源*/
	public static final String FROMTYPE = "1";
	
	/** 沟通设置初始化    沟通计划名称后缀*/
	public static final String PLANNAMEFIX = "沟通";
	
	/** 新建沟通模板时默认显示的变量组 */
	public static final String DEFAULTVARIABLEGROUP = "HDTZ";
	
	/** 测试沟通信息默认事件类型 */
	public static final String TESTMSGEVENTTYPE = "99";
	
	/** 获取Cherry登陆密码默认事件类型 */
	public static final String GETPWDMSGEVENTTYPE = "90";
	
	/** 信息发送默认事件类型 */
	public static final String SENDMSGEVENTTYPE = "10";
	
	/** 信息重新发送事件类型 */
	public static final String SENDMSGAGAINEVENTTYPE = "9";
	
	/** 每天的信息发送默认开始时间 */
	public static final String SMGTIMEBEGINOFDAY = "09:00";
	
	/** 每天的信息发送默认截止时间 */
	public static final String SMGTIMEENDOFDAY = "19:59";
	
	/** 调度任务类型 （沟通调度）*/
	public static final String TASK_TYPE_VALUE = "CS" ;
	
	/** 会员字段名称与代码对应关系 */
	public static enum MEMINFOFIELD {
		
		MEMINFOFIELD_0("0", "memCode", "string"),
		MEMINFOFIELD_1("1", "name", "string"),
		MEMINFOFIELD_2("2", "telephone", "string"),
		MEMINFOFIELD_3("3", "mobilePhone", "string"),
		MEMINFOFIELD_4("4", "gender", "string"),
		MEMINFOFIELD_5("5", "provinceId", "string"),
		MEMINFOFIELD_6("6", "cityId", "string"),
		MEMINFOFIELD_7("7", "address", "string"),
		MEMINFOFIELD_8("8", "zipCode", "string"),
		MEMINFOFIELD_9("9", "birthYear", "string"),
		MEMINFOFIELD_10("10", "birthDay", "string"),
		MEMINFOFIELD_11("11", "email", "string"),
		MEMINFOFIELD_12("12", "employeeId", "string"),
		MEMINFOFIELD_13("13", "organizationId", "string"),
		MEMINFOFIELD_25("25", "joinDate", "string"),
		MEMINFOFIELD_14("14", "referrerId", "string"),
		MEMINFOFIELD_15("15", "birthYearGetType", "string"),
		MEMINFOFIELD_16("16", "blogId", "string"),
		MEMINFOFIELD_17("17", "messageId", "string"),
		MEMINFOFIELD_18("18", "identityCard", "string"),
		MEMINFOFIELD_19("19", "maritalStatus", "string"),
		MEMINFOFIELD_20("20", "active", "string"),
		MEMINFOFIELD_21("21", "isReceiveMsg", "string"),
		MEMINFOFIELD_22("22", "profession", "string"),
		MEMINFOFIELD_23("23", "connectTime", "string"),
		MEMINFOFIELD_24("24", "memType", "string"),
		MEMINFOFIELD_27("27", "initTotalAmount", "number"),
		MEMINFOFIELD_28("28", "channelCode", "string");//,
		// 需要添加CODE值"1242"对应的KEY
//		MEMINFOFIELD_29("29", "nickname", "string"),
//		MEMINFOFIELD_30("30", "creditRating", "string");
		
		MEMINFOFIELD(String code, String name, String type) {
			this.code = code;
			this.name = name;
			this.type = type;
		}
		public String getCode() {
			return code;
		}
		public String getName() {
			return name;
		}
		public String getType() {
			return type;
		}
		private String code;
		private String name;
		private String type;
	}
	/** 沟通调度MQ消息版本 */
	public static final String MESSAGE_VERSION_GT = "AMQ.106.001";
	
	/** 沟通调度MQ消息业务类型 */
	public static final String MESSAGE_TYPE_RT = "RT";
	
	/** 沟通调度队列名 */
	public static final String CHERRYSCHEDULETASKMSGQUEUE = "cherryScheduleTaskMsgQueue";
	
	/** 实时刷新数据权限MQ消息业务类型 */
	public static final String MESSAGE_TYPE_1006 = "1006";
	
	/** 沟通事件触发MQ消息版本 */
	public static final String MESSAGE_VERSION_ES = "AMQ.107.001";
	
	/** 微信事件触发MQ消息版本 */
	public static final String MESSAGE_VERSION_WP = "AMQ.113.001";
	
	/** 沟通事件触发MQ消息业务类型 */
	public static final String MESSAGE_TYPE_ES = "GT";
	
	/** 微信事件触发MQ消息业务类型 */
	public static final String MESSAGE_TYPE_WP = "WETEMP";
	
	/** 沟通事件触发队列名 */
	public static final String CHERRYEVENTSCHEDULEMSGQUEUE = "cherryEventScheduleMsgQueue";
	
	/** 沟通事件触发业务类型 */
	public static final String MESSAGE_TYPE_1007 = "1007";
	
	/** 微信事件触发业务类型 */
	public static final String MESSAGE_TYPE_1013 = "1013";
	
	/** 沟通事件的名称 */
	public static final String EVENTNAME_CODE_9 = "短信重发";
	
	public static final String EVENTNAME_CODE_10 = "手动发送";
	
	public static final String EVENTNAME_CODE_99 = "测试信息";
	
	public static final String EVENTNAME_CODE_100 = "系统事件";
	
	//====================================================//
	
	public static final int SUCCESS = 0;
	
	public static final int ERROR = 2;
	
	public static final int WARN = 1;
	
	public static final String MSG = "msg";
	
	public static final String MSG_LEVEL  = "level";

	/**重复会员不导入*/
	public static final String IMPORTRESULT_0 = "重复会员不导入";
	
	/**导入成功*/
	public static final String IMPORTRESULT_1 = "导入成功";
	
	/**时间重复，一条也不执行*/
	public static final String IMPORTRESULT_3 = "积分时间重复，一条也不执行";
	//====================================================//
	//============会员档案常导入量定义START================//
	/**性别：男*/
	public static final String GENDER_1= "男";
	/**性别：女*/
	public static final String GENDER_2= "女";
	/**年龄获取方式：会员本人告知*/
	public static final String MEMAGEGETMETHOD_0= "会员本人告知";
	/**年龄获取方式：BA通过观察猜测年龄段而来*/
	public static final String MEMAGEGETMETHOD_1= "BA通过观察猜测年龄段而来";
	/**是否接收短信：否*/
	public static final String ISRECMSG_0= "否";
	/**是否接收短信：是*/
	public static final String ISRECMSG_1= "是";
	/**是否接测试会员：测试会员*/
	public static final String TESTMEMFLAG_0= "测试会员";
	/**是否接测试会员：正式会员*/
	public static final String TESTMEMFLAG_1= "正式会员";
	/**重复会员不导入*/
	public static final String IMPORTRESULT_4 = "提供数据中存在相同会员卡号,一条也不导入!";
	/**新增模式*/
	public static final String IMPORT_TYPE_1 = "新增模式";
	/**更新模式*/
	public static final String IMPORT_TYPE_2 = "更新模式";
	/**清空时所填写*/
	public static final String CHERRY_CLEAR = "CHERRY_CLEAR";
	
	
	//============会员档案常导入量定义END=================//
	//============会员活动下发区分START=================//
	/**未下发*/
	public static final String SEND_FLAG_ALL= "全部";
	/**未下发*/
	public static final String SEND_FLAG_0= "未下发";
	/**已下发*/
	public static final String SEND_FLAG_1= "已下发";
	/**需要再次下发*/
	public static final String SEND_FLAG_2= "需要再次下发";
	//============会员活动下发区分END=================//
	
	//============终端信息同步START=================//
	/**操作类型*/
	public static final String SYNCHROMACHINE_OPERATE= "Operate";
	/**未下发*/
	public static final String SYNCHROMACHINE_OPERATE_ADD= "add";
	/**停用/启用*/
	public static final String SYNCHROMACHINE_OPERATE_DISCONF= "disConf";
	/**绑定/解绑 */
	public static final String SYNCHROMACHINE_OPERATE_BINGCOUNTER= "bingCounter";
	/**删除 */
	public static final String SYNCHROMACHINE_OPERATE_DELETE= "delete";
	/**升级 */
	public static final String SYNCHROMACHINE_OPERATE_UPGRADE= "upgrade";
	
	//===========竞争对手同步START==============//
	/**操作类型*/
	public static final String SYNCHRORIVAL_OPERATEFLAG = "OperateFlag";
	/**新增竞争对手*/
	public static final String SYNCHRORIVAL_OPERATEFLAG_ADD = "add";
	/**编辑竞争对手信息*/
	public static final String SYNCHRORIVAL_OPERATEFLAG_UPDATE = "update";
	/**删除竞争对手信息*/
	public static final String SYNCHRORIVAL_OPERATEFLAG_DELETE = "delete";
	
	//===========竞争对手同步END==============//
	
	/** 微信激活MQ消息版本 */
	public static final String MESSAGE_VERSION_MA = "AMQ.013.001";
	
	/** 微信激活MQ消息业务类型 */
	public static final String MESSAGE_TYPE_MA = "MA";
	
	/** 激活途径 */
	public static final String ACTIVECHANNEL = "Wechat";
	//============终端信息同步END=================//
	
    //============Excel模板名称 START=================//
	/**产品入库导入模板文件名*/
    public static final String PRODUCTINDEPOTEXCEL_FILENAME = "产品入库单导入模版.xls";

    //============Excel模板名称 END=================//
	
	//============Excel模板版本号 START=================//
	/**产品入库单导入模板版本号*/
	public static final String PRODUCTINDEPOTEXCEL_VERSION = "V1.0.2";
	
	/** 产品入库单导入模板（大仓）版本号 */
	public static final String HQ_PRODUCTINDEPOTEXCEL_VERSION = "V1.0.0";

    /**产品退库申请单导入模板版本号*/
    public static final String PRORETURNREQUEST_EXCEL_VERSION = "V1.0.0";
	
	/**产品信息导入模板版本号*/
	public static final String PRODUCT_EXCEL_VERSION = "V1.0.7";
	
	/**产品信息(门店用)导入模板版本号*/
	public static final String PRODUCT_CNT_EXCEL_VERSION = "V1.0.1";
	
	/**柜台产品方案信息导入模板版本号*/
	public static final String PRODUCTSOLU_EXCEL_VERSION = "V1.0.0";

	/**产品功能开启时间明细信息导入模板版本号*/
	public static final String PRTFUN_EXCEL_VERSION = "V1.0.0";

	/**柜台产品方案(门店用)信息导入模板版本号*/
	public static final String PRODUCTSOLU_CNT_EXCEL_VERSION = "V1.0.0";
	
	/** BB柜台导入模版版本号 **/
	public static final String BBCOUNTER_EXCEL_VERSION = "V1.0.1";
	
	/**柜台信息导入模板版本号*/
	public static final String COUNTER_EXCEL_VERSION = "V1.0.7";
	
	/**新增会员导入模板版本号*/
	public static final String ADDMEMBER_EXCEL_VERSION = "V1.0.1";
	
	/**更新会员导入模板版本号*/
	public static final String UPDATEMEMBER_EXCEL_VERSION = "V1.0.1";
	
	/**活动产品库存模板版本号*/
	public static final String CAMPAIGNSTOCK_EXCEL_VERSION = "V1.0.1";
	
	/**唯一码激活明显模板版本号*/
	public static final String UNQDETAILS_EXCEL_VERSION = "V1.0.1";
	

	//============Excel模板版本号 END=================//
	
	/** 销售单据取号前缀  */
	public static final String BILLTYPE_NS = "NS";
	
	/** 相对日期  */
	public static final String REFERDATE = "referDate";
	
	//============销售目标设定表 数据来源 START=================//
	/**1：后台设定*/
	public static final String SALETARGET_SOURCE_BACKEND = "1";
	
	/**2：终端设定*/
    public static final String SALETARGET_SOURCE_TERMINAL = "2";
	//============销售目标设定表 数据来源 END =================//
	
	/** 品牌模式 */
	public static final String BRANDMODE = "brandMode";
	
	/** 品牌模式 0:单品牌*/
	public static final String BRANDMODE_SINGLE = "0";
	
	/**  品牌模式 1:多品牌*/
	public static final String BRANDMODE_MULTITON = "1";

	
	//======================= WebPos业务 =====================//
	
	/** WebPos数据源 */
	public static final String WP_WEBPOS_SOURCE = "WEBPOS";
	
	/** 成功状态 */
	public static final String WP_SUCCESS_STATUS = "0";
	
	/** 失败状态 */
	public static final String WP_ERROR_STATUS = "-1";
	
	/** 成功返回代号 */
	public static final String WP_SUCCESS_CODE = "0";
	
	/** 没有找到符合条件的会员 */
	public static final String WP_ERROR_NOTGETMEMBER = "WP1001";
	
	/** 找到多个符合条件的会员 */
	public static final String WP_ERROR_GETMULTIPLEMEMBER = "WP1002";
	
	
	/** WebPos销售 */
	public static final String WP_SALETYPE_NS = "NS";
	
	/** WebPos退货 */
	public static final String WP_SALETYPE_SR = "SR";
	
	/** WebPos积分兑换 */
	public static final String WP_SALETYPE_PX = "PX";
	
	/** WebPos客户类型（会员客户） */
	public static final String WP_CUSTOMERTYPE_MP = "MP";
	
	/** WebPos客户类型（普通客户） */
	public static final String WP_CUSTOMERTYPE_NP = "NP";
	
	/** WebPos销售单据前缀 */
	public static final String WP_BILLPREFIX_WN = "WN";
	
	/** WebPos退货单据前缀 */
	public static final String WP_BILLPREFIX_WR = "WR";
	
	
	/** WebPos现金支付方式代号 */
	public static final String WP_PAYTYPECODE_CA = "CA";
	
	/** WebPos信用卡支付方式代号  */
	public static final String WP_PAYTYPECODE_CR = "CR";
	
	/** WebPos银行卡支付方式代号  */
	public static final String WP_PAYTYPECODE_BC = "BC";
	
	/** WebPos支付宝支付方式代号  */
	public static final String WP_PAYTYPECODE_PT = "PT";
	
	/** WebPos微信支付方式代号  */
	public static final String WP_PAYTYPECODE_WP = "WP";
	
	/** WebPos储值卡支付方式代号  */
	public static final String WP_PAYTYPECODE_CZK = "CZK";
	
	/** WebPos积分支付方式代号  */
	public static final String WP_PAYTYPECODE_EX = "EX";
	
	/** WebPos其它支付方式代号  */
	public static final String WP_PAYTYPECODE_OT = "OT";
	
	/** WebPos现金支付方式名称 */
	public static final String WP_PAYTYPENAME_CA = "现金";
	
	/** WebPos信用卡支付方式名称  */
	public static final String WP_PAYTYPENAME_CR = "信用卡";
	
	/** WebPos银行卡支付方式名称  */
	public static final String WP_PAYTYPENAME_BC = "银行卡";
	
	/** WebPos支付宝支付方式名称  */
	public static final String WP_PAYTYPENAME_PT = "支付宝";
	
	/** WebPos微信支付方式名称  */
	public static final String WP_PAYTYPENAME_WP = "微信支付";
	
	/** WebPos积分支付方式名称  */
	public static final String WP_PAYTYPENAME_EX = "积分支付";
	
	/** WebPos其它支付方式名称  */
	public static final String WP_PAYTYPENAME_OT = "其它";

	
	
	/** WebPos活动领取礼品金额不足活动兑换金额情况下用于补足差额的促销品默认代号 */
	public static final String WP_DEFPROMOTIONCODE_DXJE = "DXJE";
	
	/** WebPos活动领取礼品金额不足活动兑换金额情况下用于补足差额的促销品默认名称 */
	public static final String WP_DEFPROMOTIONNAME_DXJE = "抵消金额";
	
	//======================= WebPos业务 END =================//
	
	/**  是否有POS机 */
	public static final String POSFLAG = "posFlag";
	/**  是否有POS机_是 */
	public static final String POSFLAG_YES = "是";
	/**  是否有POS机_否 */
	public static final String POSFLAG_NO = "否";
	
	/**  柜台级别 */
	public static final String COUNTER_LEVEL = "counterLevel";
	/**  柜台级别 A */
	public static final String COUNTER_LEVEL_A = "A";
	/**  柜台级别 B */
	public static final String COUNTER_LEVEL_B = "B";
	/**  柜台级别 C */
	public static final String COUNTER_LEVEL_C = "C";
	
	/**  柜台状态 0：营业中,1：筹备中,2：停业中,3：装修中,4：关店 */
	public static final String COUNTER_STATUS = "status";
	/**  柜台状态   0：营业中 */
	public static final String COUNTER_STATUS_0 = "营业中";
	/**  柜台状态 1：筹备中 */
	public static final String COUNTER_STATUS_1 = "筹备中";
	/**  柜台状态 2：停业中 */
	public static final String COUNTER_STATUS_2 = "停业中";
	/**  柜台状态 3：装修中 */
	public static final String COUNTER_STATUS_3 = "装修中";
	/**  柜台状态 4：关店 */
	public static final String COUNTER_STATUS_4 = "关店";
	/** 产品方案添加模式 1：标准模式  */
	public static final String soluAddMode_1 = "1";
	/** 产品方案添加模式 2：颖通模式  */
	public static final String soluAddMode_2 = "2";
	
	
//	/** 产品方案添加产品模式1:标准模式  */
//	public static final String SOLU_ADD_MODE_CONFIG_1 = "1";
//	
//	/** 产品方案添加产品模式  2:颖通模式(分类并集)  */
//	public static final String SOLU_ADD_MODE_CONFIG_2 = "2";
//	
//	/** 产品方案添加产品模式 3：颖通模式(分类交集) */
//	public static final String SOLU_ADD_MODE_CONFIG_3 = "3";
	
	
	/** WebPos单据类型（即时业务）  */
	public static final String WP_TICKETTYPE_NE = "NE";
	
	/** WebPos单据类型（补登业务）  */
	public static final String WP_TICKETTYPE_LA = "LA";
	
	/** WebPos退货方式（普通退货）  */
	public static final String WP_SALESRTYPE_1 = "1";
	
	/** WebPos退货方式（关联退货）  */
	public static final String WP_SALESRTYPE_2 = "2";
	
	/** WebPos退货方式（正常销售）  */
	public static final String WP_SALESRTYPE_3 = "3";
	
	/** WebPos销售数据标识（正常） */
	public static final String WP_DATASTATE_0 = "0";
	
	/** WebPos销售数据标识（已退单）  */
	public static final String WP_DATASTATE_1 = "1";
	
	/** WebPos销售数据标识（有退货）  */
	public static final String WP_DATASTATE_2 = "2";
	
	/** WebPos销售数据标识（已修改）  */
	public static final String WP_DATASTATE_3 = "3";

	/** 信息模板会员所属柜台替换表达式*/
	public static final String COUNTER_NAME = "<#COUNTER_NAME#>" ;
	
	
	/** 付款通知模板订货客服名称替换表达式*/
	public static final String OrderName = "<#OrderName#>" ;
	
	/** 信息模板订货日期替换表达式*/
	public static final String OrderDate = "<#OrderDate#>" ;
	
	/** 信息模板订货数量替换表达式*/
	public static final String OrderQuantity = "<#OrderQuantity#>" ;
	
	/** 信息模板订货金额替换表达式*/
	public static final String OrderMoney = "<#OrderMoney#>" ;
	
	//======================= WebPos业务 END =================//
	
	//==================微商申请单据状态START=====================
	
	/*待分配*/
	public static final String WEM_STATUS_1 = "1";
	
	/*待审核*/
	public static final String WEM_STATUS_2 = "2";
	
	/*审核通过*/
	public static final String WEM_STATUS_3 = "3";
	
	/*审核不通过*/
	public static final String WEM_STATUS_4 = "4";
	
	//==================微商申请单据状态END=====================
	//MF天猫加密密钥
	public static final String TMALLENCRYPT="Zloft2";

	
}
