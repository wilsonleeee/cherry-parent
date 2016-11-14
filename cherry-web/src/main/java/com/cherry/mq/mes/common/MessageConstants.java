/*		
 * @(#)MessageConstants.java     1.0 2010/12/01		
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
package com.cherry.mq.mes.common;

/**
 * 消息共通常量
 * @author huzude
 *
 */
public class MessageConstants {
	
	/**逗号*/
	public static final String comma = ",";
	
	/**回车换行*/
	public static final String enter = "\r\n";
	
	/**左中括号*/
	public static final String left_bracket = "[";
	
	/**右中括号*/
	public static final String right_bracket = "]";
	
	/** 消息版本 */
	public static final String MESSAGE_VERSION = "[Version],AMQ.001.001";
	
	/** 退库审核  消息版本 */
    public static final String MESSAGE_VERSION_RJ = "AMQ.007.001";
    
    /** 盘点审核  消息版本 */
    public static final String MESSAGE_VERSION_CJ = "AMQ.009.001";
	
    /** 调拨审核  消息版本 */
    public static final String MESSAGE_VERSION_BJ = "AMQ.011.001";
    
	/** 销售退货审核  消息版本 */
    public static final String MESSAGE_VERSION_SJ = "AMQ.018.001";
    
    /** 数据类型  会员扩展信息（和俱乐部关联）*/
    public static final String MESSAGE_TYPE_MZ_JSON  = "0019";
    
    /** 数据类型  线上购买线下提货订单状态变更*/
    public static final String MESSAGE_TYPE_NZ_JSON  = "0024";
    
	/** 数据类型 销售库存数据 */
	public static final String MESSAGE_TYPE_SALE_STOCK = "[Type],0001";
	
	/** 数据类型 机器连接信息 */
	public static final String MESSAGE_TYPE_MACHINE = "[Type],0002";
	
	/** 数据类型 会员,BA信息,BAS考勤 */
	public static final String MESSAGE_TYPE_MEMBER_BA = "[Type],0003";
	
	/** 数据类型 问卷信息 */
	public static final String MESSAGE_TYPE_MEMBER_QS = "[Type],0004";
	
	/** 数据类型  竞争对手销售信息 */
	public static final String MESSAGE_TYPE_MEMBER_RS = "[Type],0005";
	
	/** 数据类型  机器信息 */
	public static final String MESSAGE_TYPE_MACH_MI = "[Type],0006";
	
    /** 数据类型  新销售/退货信息 */
    public static final String MESSAGE_TYPE_SALE_RETURN = "[Type],0007";
	
    /** 数据类型  积分兑换信息 */
    public static final String MESSAGE_TYPE_DHHD_RETURN = "[Type],0014";
    
	/** 数据类型  销售库存信息 （新MQ消息体）*/
    public static final String MESSAGE_TYPE_SALE_STOCK_JSON  = "0001";
	
    /** 数据类型  BAS、BA相关 （新MQ消息体）*/
    public static final String MESSAGE_TYPE_BABAS_JSON  = "0003";
    
    /** 数据类型  机器绑定柜台信息 （新MQ消息体）*/
    public static final String MESSAGE_TYPE_MACH_MI_JSON  = "0006";
    
    /** 数据类型  库存合并 （新MQ消息体）*/
    public static final String MESSAGE_TYPE_STOCK_HB_JSON  = "0008";
    
    /** 数据类型  会员回访新格式带问卷 （JSON格式消息体）*/
    public static final String MESSAGE_TYPE_MEMBER_MV_JSON  = "0009";
    
    /** 数据类型  活动预约 （JSON格式消息体）*/
    public static final String MESSAGE_TYPE_ACT_PB_JSON  = "0011";

    /** 数据类型  积分兑换（无需预约） （JSON格式消息体）*/
    public static final String MESSAGE_TYPE_PX_JSON = "0014";
    
    /** 数据类型  会员初始积分录入 （JSON格式消息体）*/
    public static final String MESSAGE_TYPE_MT_JSON = "0015";
    
    /** 数据类型  更改销售单状态 （JSON格式消息体）*/
    public static final String MESSAGE_TYPE_SC_JSON = "0017";
    
    /** 数据类型  会员激活 （JSON格式消息体）*/
    public static final String MESSAGE_TYPE_MD_JSON = "0020";
    
    /** 数据类型  短信回复信息采集 （JSON格式消息体）*/
    public static final String MESSAGE_TYPE_DXCJ_JSON = "0021";
    
    /** 数据类型  调入确认 （JSON格式消息体）*/
    public static final String MESSAGE_TYPE_BC_JSON = "0022";
    
    /** 数据类型  反向催单信息确认 （JSON格式消息体）*/
    public static final String MESSAGE_TYPE_VD_JSON = "0023";

    /** 主数据标记 */
	public static final String MAIN_MESSAGE_SIGN = "[MainDataLine],";
	
	/** 明细数据标记 */
	public static final String DETAIL_MESSAGE_SIGN = "[DetailDataLine],";
	
	/** 主数据标记（新MQ消息体） */
	public static final String MAINDATA_MESSAGE_SIGN = "MainData";
	
	/** 明细数据标记（新MQ消息体） */
	public static final String DETAILDATA_MESSAGE_SIGN = "DetailDataDTOList";
	
	/** 消息类型标记 */
	public static final String MESSAGE_TYPE_SIGN = "[Type],";
	
	/** 结束标记 */
	public static final String END_MESSAGE_SIGN = "[End]";
	
	/** 消息版本便标记 */
	public static final String MESSAGE_VERSION_SIGN = "[Version],";
	
	/** 消息版本标题 */
	public static final String MESSAGE_VERSION_TITLE = "Version";
	
	/** 消息命令类型标题 */
	public static final String MESSAGE_TYPE_TITLE = "Type";
	
	/** 消息数据类型标记*/
	public static final String MESSAGE_DATATYPE_SIGN = "[DataType],";
	
	/** 消息数据类型标题*/
	public static final String MESSAGE_DATATYPE_TITLE = "DataType";
	
	/** 主数据标题*/
	public static final String MAIN_MESSAGE_TITLE = "MainDataLine";
	
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
	
	/** 数据类型标题*/
	public static final String TYPE_TITLE = "Type";
	
	/** 消息业务类型--销售/退货*/
	public static final String MSG_TRADETYPE_SALE = "NS";
	
    /** 消息业务类型--更改单据状态*/
    public static final String MSG_CHANGESALESTATE = "SC";
	
	/** 消息业务类型--入库/退库*/
	public static final String MSG_STOCK_IN_OUT = "GR";
	
	/** 消息业务类型--调入申请单*/
	public static final String MSG_ALLOCATION_IN = "BG";
	
	/** 消息业务类型--调出确认单*/
	public static final String MSG_ALLOCATION_OUT = "LG";
	
    /** 消息业务类型--调入确认单*/
    public static final String MSG_ALLOCATION_IN_CONFRIM = "BC";
	
	/** 消息业务类型--盘点单*/
	public static final String MSG_STOCK_TAKING = "CA";
	
	/** 消息业务类型--生日礼领用单*/
	public static final String MSG_BIR_PRESENT = "SP";
	
	/** 消息业务类型--产品订货单*/
	public static final String MSG_PRO_ORDER = "OD";
	
	/** 消息业务类型--金蝶K3导入发货单*/
    public static final String MSG_KS_DELIVER = "KS";
    
    /** 消息业务类型--移库*/
    public static final String MSG_ProductShift = "YK";
    
    /** 消息业务类型--退库申请*/
    public static final String MSG_ReturnRequest = "RA";
	
    /** 消息业务类型--盘点申请*/
    public static final String MSG_StocktakeRequest = "CR";
    
    /** 消息业务类型--盘点确认(老后台发送到新后台) */
    public static final String MSG_StocktakeConfirm = "CC";
    
    /** 消息业务类型--合并库存(老后台发送到新后台) */
    public static final String MSG_STOCK_HB = "HB";
    
    /** 消息业务类型--积分兑换（无需预约）(老后台发送到新后台) */
    public static final String MSG_TRADETYPE_PX = "PX";
    
	/** 消息业务类型--会员*/
	public static final String MSG_MEMBER = "MB";
	
	/** 消息业务类型--会员初始数据导入*/
	public static final String MSG_MEMBER_MS = "MS";
	
	/** 消息业务类型--化妆次数使用*/
	public static final String MSG_MEMBER_BU = "BU";
	
	/** 消息业务类型--会员回访*/
	public static final String MSG_MEMBER_MV = "MV";
	
	/** 消息业务类型--会员积分（新后台到新后台）*/
    public static final String MSG_MEMBER_POINT_MY = "MY";
	
    /** 消息业务类型--会员激活*/
    public static final String MSG_MEMBER_ACTIVE = "MD";
    
    /** 消息业务类型--短信回复信息采集*/
    public static final String MSG_MEMBER_DXCJ = "DXCJ";
    
	/** 消息业务类型--BA信息*/
	public static final String MSG_BA_INFO = "BA";
	
    /** 消息业务类型--BA信息下发*/
    public static final String MSG_BAI_INFO = "SE";
    
    /** 消息业务类型--产品下发通知*/
    public static final String MSG_PR = "PR";
    
    /** 消息业务子类型--产品信息  */
    public static final String MSG_SPRT_PRT = "PRT";
    
    /** 消息业务子类型--促销产品信息  */
    public static final String MSG_SPRT_PRM = "PRM";
    
    /** 消息业务子类型--柜台产品*/
    public static final String MSG_SPRT_DPRT = "DPRT";
    
    /** 消息业务类型--终端设定销售目标*/
    public static final String MSG_SALETARGET = "ST";
	
	/** 消息业务类型--柜台信息下发*/
	public static final String MSG_COUNTER_INFO = "CT";
	
	/** 消息业务类型--BAS考勤*/
	public static final String MSG_BAS_INFO = "CQ";
	
    /** 消息业务类型--普通考勤信息 */
    public static final String MSG_BAATTENDANCE = "AT";
	
	/** 消息业务类型--问卷*/
	public static final String MSG_MEMBER_QUESTION = "MQ";
	
	/** 消息业务类型--订发货类型*/
	public static final String MSG_DEPOT_ORDERDELIVERTYPE = "OT";
	
    /** 消息业务类型--机器信息*/
    public static final String MSG_MACHINE_INFO = "MI";
	
    /** 消息业务类型--机器绑定柜台信息类型*/
    public static final String MSG_MACHINE_COUNTER = "MC";
    
    /** 消息业务类型--柜台产品陈列数*/
    public static final String MSG_EXHIBIT_QUANTITY = "EQ";
	
	/** 问卷类型--普通问卷*/
	public static final String MSG_QUESTION_GENERAL = "0";
	
	/** 问卷类型--会员问卷*/
	public static final String MSG_QUESTION_PERSON = "1";
	
	/** 问卷类型--商场问卷*/
	public static final String MSG_QUESTION_MARKET = "2";
	
	/** 消息业务类型--CS考核问卷*/
	public static final String MSG_CS_QUESTION = "CS";
	
	/** 新后台业务类型 --仓库发货*/
	public static final String CHERRY_TRADETYPE_CONSIGNMENT  = "1";
	
	/** 新后台业务类型 --仓库收货*/
	public static final String CHERRY_TRADETYPE_RECEIVE = "2";
	
	/** 新后台业务类型 --仓库退库*/
	public static final String CHERRY_TRADETYPE_CANCELLING_STOCKS = "3";
	
	/** 新后台业务类型 --接收退库*/
	public static final String CHERRY_TRADETYPE_RECEIVE_CANCEL_STOCKS = "4";
	
	/** 新后台业务类型--调入申请 */
	public static final String CHERRY_TRADETYPE_ALLOCATION_IN_APPLY = "5";
	
	/** 新后台业务类型--调出确认 */
	public static final String CHERRY_TRADETYPE_ALLOCATION_OUT_CONFIRM = "6";
	
	/** 新后台业务类型 --自由入库*/
	public static final String CHERRY_TRADETYPE_FREEDOM_STOCK_IN = "7";
	
	/** 新后台业务类型 --自由出库*/
	public static final String CHERRY_TRADETYPE_FREEDOM_STOCK_OUT = "8";
	
	/** 新后台业务类型 --销售*/
	public static final String CHERRY_TRADETYPE_SALE = "N";
	
	/** 新后台业务类型 --退货*/
	public static final String CHERRY_TRADETYPE_RETURN_PURCHASE = "R";
	
	/** 新后台业务类型 --盘点*/
	public static final String CHERRY_TRADETYPE_TAKING = "P";
	
	/** 新后台业务类型 --礼品领用（生日礼）*/
	public static final String CHERRY_TRADETYPE_BIRPRSSENT = "S";
	
	/** 新后台业务类型 --产品订货  */
	public static final String CHERRY_TRADETYPE_ProductOrder = "O";
	
	/** 入出库区分 -- 入库*/
	public static final String STOCK_TYPE_IN = "0";
	
	/** 入出库区分 -- 出库*/
	public static final String STOCK_TYPE_OUT = "1";
	
	/** 调拨区分 -- 入库*/
	public static final String ALLOCATION_FLAG_IN = "I";
	
	/** 盘点区分 -- 促销品商品盘点*/
	public static final String PROMOTION_BUSINESS_STOCK_TAKING = "P4";
	
	/** 盘点区分 -- 促销品商品盘点(盲盘)*/
	public static final String PROMOTION_BUSINESS_STOCK_TAKING_UNLOOK = "P5";
	
	/** 反向催单信息却仍*/
	public static final String MSG_REMINDER_CONFIRM = "VD";
	
	public static final String ALLOCATION_FLAG_OUT = "O";
	
	/** 套装折扣unitCode */
	public static final String TZZK_UNITCODE = "TZZK999999";
	
	/** 审核区分   未提交审核*/
	public static final String AUDIT_FLAG_UNSUBMIT = "0" ;
	
	/** 审核区分   已提交审核*/
	public static final String AUDIT_FLAG_SUBMIT = "1" ;
	
	/** 审核区分   审核通过*/
	public static final String AUDIT_FLAG_AGREE = "2" ;
	
	/** 审核区分   审核退回*/
	public static final String AUDIT_FLAG_DISAGREE = "3";
	
	/** 接收消息错误提示  01 **/
	public static final String MSG_ERROR_01 = "消息版本异常";
	
	/** 接收消息错误提示  02 **/
	public static final String MSG_ERROR_02 = "主数据解析异常";
	
	/** 接收消息错误提示  03 **/
	public static final String MSG_ERROR_03 = "明细数据解析异常";
	
	/** 接收消息错误提示  04 **/
	public static final String MSG_ERROR_04 = "验证消息完整性异常";
	
	/** 接收消息错误提示  05 **/
	public static final String MSG_ERROR_05 = "没有查询到相关组织品牌信息";
	
	/** 接收消息错误提示  06 **/
	public static final String MSG_ERROR_06 = "没有查询到相关部门信息";
	
	/** 接收消息错误提示  07 **/
	public static final String MSG_ERROR_07 = "没有查询到相关员工信息";
	
	/** 接收消息错误提示  08 **/
	public static final String MSG_ERROR_08 = "没有查询到相关仓库信息";
	
	/** 接收消息错误提示  09 **/
	public static final String MSG_ERROR_09 = "没有查询到相关商品基本信息";
	
	/** 接收消息错误提示  10 **/
	public static final String MSG_ERROR_10 = "没有查询到相关产品包装信息";
	
	/** 接收消息错误提示  11 **/
	public static final String MSG_ERROR_11 = "没有查询到相关逻辑仓库信息";
	
	/** 接收消息错误提示  12 **/
	public static final String MSG_ERROR_12 = "插入MongoDB异常";
	
	/** 接收消息错误提示  13 **/
	public static final String MSG_ERROR_13 = "没有查询到相关问卷问题信息";
	
	/** 接收消息错误提示  14 **/
	public static final String MSG_ERROR_14 = "没有查询到品牌数据源信息";
	
	/** 接收消息错误提示  15 **/
	public static final String MSG_ERROR_15 = "消息类型解析异常";
	
	/** 接收消息错误提示  16 **/
	public static final String MSG_ERROR_16 = "更新机器连接时间异常";
	
	/** 重复的MQ接收数据  **/
	public static final String MSG_REPEAT_DATA = "重复的MQ接收数据被忽略";
	
	/** 是重复的正常销售 17 **/
	public static final String MSG_ERROR_17 = "重复的销售数据被忽略";
	
	/** 是重复的正常退货 18 **/
	public static final String MSG_ERROR_18 = "重复的退货数据被忽略";
	
	/** 是重复的修改销售数据 19 **/
	public static final String MSG_ERROR_19 = "重复的修改销售数据被忽略";
	
	/** 是重复的退库数据 20 **/
	public static final String MSG_ERROR_20 = "重复的退库数据被忽略";
	
	/** 是重复的入库数据 21 **/
	public static final String MSG_ERROR_21 = "重复的入库数据被忽略";
	
	/** 是重复的调入申请数据 22 **/
	public static final String MSG_ERROR_22 = "重复的调入申请数据被忽略";
	
	/** 是重复的调出确认数据 23 **/
	public static final String MSG_ERROR_23 = "重复的调出确认数据被忽略";
	
	/** 是重复的盘点数据 24 **/
	public static final String MSG_ERROR_24 = "重复的盘点数据被忽略";
	
	/** 是重复的会员新增数据 25 **/
	public static final String MSG_ERROR_25 = "重复的会员新增数据被忽略";
	
	/** 是重复的盘点数据 26 **/
	public static final String MSG_ERROR_26 = "重复的生日礼品领用数据被忽略";
	
	/** 接收消息错误提示  27 **/
	public static final String MSG_ERROR_27 = "没有此业务类型";
	
	/** 接收消息错误提示  28 **/
	public static final String MSG_ERROR_28 = "没有此命令类型";
	
	/** 接收消息错误提示  29 **/
	public static final String MSG_ERROR_29 = "没有关联到相关员工";
	
	/** 接收消息错误提示  30 **/
	public static final String MSG_ERROR_30 = "员工信息没有关联到岗位信息";
		
	/** 接收消息错误提示  31 **/
	public static final String MSG_ERROR_31 = "重复的BAS考勤数据被忽略";
	
	/** 接收消息错误提示  32 **/
	public static final String MSG_ERROR_32 = "重复的产品订货数据被忽略";
	
	/** 接收消息错误提示  33 **/
	public static final String MSG_ERROR_33 = "销售/退货数据接收未成功时，却又进行修改销售操作";
	
	/** 接收消息错误提示  34 **/
	public static final String MSG_ERROR_34 = "没有查询到相关会员信息";
	
	/** 接收消息错误提示  35 **/
	public static final String MSG_ERROR_35 = "重复的会员初始数据被忽略";
	
	/** 接收消息错误提示  36 **/
	public static final String MSG_ERROR_36 = "没有关联到实体仓库信息";
	
	/** 接收消息错误提示  37 **/
	public static final String MSG_ERROR_37 = "没有关联到逻辑仓库信息";
	
	/** 接收消息错误提示 38 **/
	public static final String MSG_ERROR_38 = "查询MongoDB异常";
	
	/** 接收消息错误提示 39 **/
	public static final String MSG_ERROR_39 = "更新机器信息异常";
	
	/** 接收消息错误提示 40 **/
	public static final String MSG_ERROR_40 = "MQ消息体命令类型（Type）异常";
	
	/** 接收消息错误提示 41 **/
	public static final String MSG_ERROR_41 = "MQ消息体数据类型（DataType）异常";
	
	/** 接收消息错误提示 42 **/
	public static final String MSG_ERROR_42 = "MQ消息体数据（DataLine）异常";
	
	/** 接收消息错误提示 43 **/
	public static final String MSG_ERROR_43 = "将JSON格式的字符串解析成Map时发生异常";
	
	/** 接收消息错误提示 44 **/
	public static final String MSG_ERROR_44 = "暂未提供解析MXL格式的方法";
	
	/** 接收消息错误提示 45 **/
	public static final String MSG_ERROR_45 = "错误的MQ消息数据类型（DataType）";
	
	/** 接收消息错误提示 46 **/
	public static final String MSG_ERROR_46 = "MQ消息中主数据（MainData）设定错误";
	
	/** 接收消息错误提示 47 **/
	public static final String MSG_ERROR_47 = "将DataLine中的数据转换成JSON字符串时出错";
	
	/** 接收消息错误提示 48 **/
	public static final String MSG_ERROR_48 = "暂未提供包装MXL数据结构的方法";
	
	/** 接收消息错误提示 49 **/
	public static final String MSG_ERROR_49 = "MQ消息解析失败";
	
    /** 接收消息错误提示 50 **/
    public static final String MSG_ERROR_50 = "移出数量与移入数量不一致";
	
    /** 接收消息错误提示 51 **/
    public static final String MSG_ERROR_51 = "移出方的逻辑仓库不存在";
    
    /** 接收消息错误提示 52 **/
    public static final String MSG_ERROR_52 = "移入方的逻辑仓库不存在";
    
    /** 接收消息错误提示  53 **/
	public static final String MSG_ERROR_53 = "没有查询到相关的消息处理器";
	
	/** 接收消息错误提示  54 **/
	public static final String MSG_ERROR_54 = "没有查询到相关的规则处理器";
	
	/** 接收消息错误提示  55 **/
	public static final String MSG_ERROR_55 = "不存在执行规则的明细数据";
	
	/** 接收消息错误提示  56 **/
	public static final String MSG_ERROR_56 = "会员已经存在新卡，不能再激活老卡";
	
    /** 接收消息错误提示  57 **/
    public static final String MSG_ERROR_57 = "盘点申请账面数量错误";
    
    /** 接收消息错误提示  58 **/
    public static final String MSG_ERROR_58 = "盘点申请盘差错误";
    
    /** 接收消息错误提示 59 **/
    public static final String MSG_ERROR_59 = "机器绑定柜台异常";
    
    /** 接收消息错误提示 60 **/
    public static final String MSG_ERROR_60 = "订货拒绝时，产品订货单不存在，单据号为";
    
    /** 接收消息错误提示 61 **/
    public static final String MSG_ERROR_61 = "退库申请拒绝时，产品退库申请单不存在，单据号为";
    
    /** 接收消息错误提示  62 **/
	public static final String MSG_ERROR_62 = "消息体中的会员ID为空";
	
	/** 接收消息错误提示  63 **/
	public static final String MSG_ERROR_63 = "通过webService实时刷新索引数据失败";
	
    /** 接收消息错误提示  64 **/
    public static final String MSG_ERROR_64 = "销售数据接收未成功时，却又进行关联退货操作";
    
    /** 接收消息错误提示  65 **/
	public static final String MSG_ERROR_65 = "没有查询到相关省市县信息";
	
    /** 接收消息错误提示  66 **/
    public static final String MSG_ERROR_66 = "积分兑换活动接收未成功时，却又进行积分兑换取消操作";
    
    /** 接收消息错误提示  67 **/
    public static final String MSG_ERROR_67 = "积分兑换预约扣减积分失败，单据总积分值与明细之和不等";
    
    /** 接收消息错误提示  68 **/
    public static final String MSG_ERROR_68 = "更新积分维护记录失败，原单还未接受成功";
    
    /** 接收消息错误提示  69 **/
    public static final String MSG_ERROR_69 = "维护积分处理失败，单据时间早于该会员的积分导入截止时间";
    
    /** 接收消息错误提示  70 **/
    public static final String MSG_ERROR_70 = "更新MongoDB异常";
    
    /** 接收消息错误提示  71 **/
    public static final String MSG_ERROR_71 = "没有查询到对应等级信息";
    
    /** 接收消息错误提示  72 **/
    public static final String MSG_ERROR_72 = "预约单已领用";
    
    /** 接收消息错误提示  73 **/
    public static final String MSG_ERROR_73 = "终端设定销售目标的SubType只能是BA或CT";
    
    /** 接收消息错误提示  74 **/
    public static final String MSG_ERROR_74 = "字段[%1$s]必填";
    
    /** 接收消息错误提示  75 **/
    public static final String MSG_ERROR_75 = "字段[%1$s]格式不正确，正确的格式%2$s";
    
    /** 接收消息错误提示  76 **/
    public static final String MSG_ERROR_76 = "明细行[%1$s]不能为空";
    
    /** 接收消息错误提示  77 **/
    public static final String MSG_ERROR_77 = "更改销售单据状态失败";
    
    /** 接收消息错误提示  78 **/
    public static final String MSG_ERROR_78 = "产品调拨申请单据不存在，单据号为";
    
    /** 接收消息错误提示  79 **/
    public static final String MSG_ERROR_79 = "该推荐会员卡号不存在，推荐会员卡号为";
    
    /** 接收消息错误提示  80 **/
    public static final String MSG_ERROR_80 = "更改会员激活信息失败";
    
    /** 接收消息错误提示 81 **/
    public static final String MSG_ERROR_81 = "调入确认时，调出单不存在，单据号为";
    
    /** 接收消息错误提示 82 */
    public static final String MSG_ERROR_82 = "盘点业务时无明细数据且SubType未定义，或者是其他业务没有相应明细";
    
    /** 接收消息错误提示  34 **/
	public static final String MSG_ERROR_83 = "没有查询到对应的天猫积分记录";
	
	/** 接收消息错误提示 84 **/
	public static final String  MSG_ERROR_84 = "线上购买线下提货订单状态变更时，订单号不存在，订单号为";
	
	/** 接收消息错误提示 85 **/
	public static final String  MSG_ERROR_85 = "订单号不能为空！";
	
	/** 接收消息错误提示 88 **/
	public static final String  MSG_ERROR_88 = "操作时间不能为空！";
	
	/** 接收消息错误提示 89 **/
	public static final String  MSG_ERROR_89 = "操作时间格式不正确！";
	
	/** 接收消息错误提示 90 **/
	public static final String  MSG_ERROR_90 = "订单状态不能为空！";
	
	 /** 接收消息错误提示 91 **/
    public static final String MSG_ERROR_91 = "退货审核确认时，关联的退货申请单号不存在！关联的退货申请单号为：";

	/** 接收消息错误提示 92 **/
	public static final String MSG_ERROR_92 = "单据号合并失败";

	/** 接收消息错误提示 93 **/
	public static final String MSG_ERROR_93 = "改交易解冻失败";


	/** 接收消息错误提示 94 **/
	public static final String  MSG_ERROR_94 = "没有找到相应的柜台信息";

	/** 接收消息错误提示 95 **/
	public static final String  MSG_ERROR_95 = "没有找到相应的产品厂商信息";

	/** 接收消息错误提示 96 **/
	public static final String  MSG_ERROR_96 = "没有找到相应的产品信息";

    /** 接收消息提示  01 **/
    public static final String MSG_INFO_01 = "积分兑换预约";
    
	/** 单据类型 修改销售记录 **/
	public static final String TICKET_TYPE_MODIFIE_SALE = "MO";
	
	/** 单据类型 补登销售记录 **/
	public static final String TICKET_TYPE_LATER_SALE = "LA";
	
	/** 退货类型 普通退货 **/
	public static final String SR_TYPE_GENERAL_RETURN = "1";
	
	/** 退货类型 关联退货 **/
	public static final String SR_LINKED_RETURN = "2";
	
	/** 退货类型 销售类型 **/
	public static final String SR_TYPE_SALE = "3";
	
	/** 非会员卡号 **/
	public static final String ON_MEMBER_CARD = "000000000";
	
	/** 销售类型 正常销售 **/
	public static final String SALE_TYPE_NORMAL_SALE = "N";
	
	/** 销售类型 促销 **/
	public static final String SALE_TYPE_PROMOTION_SALE = "P";
	
	/** 会员消息类型  新增会员 */
	public static final String MEM_TYPE_NEW_MEMBER = "0";
	
	/** 会员消息类型  修改会员 */
	public static final String MEM_TYPE_EDIT_MEMBER = "1";
	
	/** 会员消息类型  会员换卡 */
	public static final String MEM_TYPE_CARD_CHANGE = "2";
	
	/** 会员消息类型  删除卡号 */
	public static final String MEM_TYPE_DEL_MEMBER = "3";
	
	/** 会员消息数据来源类型 IPOS2 */
	public static final String MEM_SOURCE_TYPE_POS2= "POS2";
	
	/** 会员消息数据来源类型 IPOS3 */
	public static final String MEM_SOURCE_TYPE_POS3= "POS3";
	
	/** MongoDB 消息警告表 **/
	public static final String MQ_WARN_COLL_NAME = "MGO_MQWarn";
	
	/** MongoDB 消息信息表 **/
	public static final String MQ_INFO_COLL_NAME = "MGO_MQInfo";
	
	/** MongoDB 业务流水表 **/
	public static final String MQ_BUS_LOG_COLL_NAME = "MGO_BusinessLog";
	
    /** MongoDB 机器连接信息表 **/
    public static final String MQ_MCR_LOG_COLL_NAME = "MGO_MachineConnRecord";
    
    /** MongoDB 终端消息反馈日志表*/
    public static final String MQ_MGO_MQNOTICELOG_NAME = "MGO_MQNoticeLog";
	
	/**  是否需要管理库存   是 **/
	public static final String Stock_IS_YES = "1";
	
	/**  是否需要管理库存  否 **/
	public static final String Stock_IS_NO = "0";

	/** 发货*/
	public static final String BUSINESS_TYPE_SD = "SD";	
	
	/** 收货*/
	public static final String BUSINESS_TYPE_RD = "RD";
	
	/** 退库*/
	public static final String BUSINESS_TYPE_RR = "RR";
	
	/** 接收退库*/
	public static final String BUSINESS_TYPE_AR = "AR";
	
	/** 调入申请*/
	public static final String BUSINESS_TYPE_BG = "BG";
	
	/** 调出确认*/
	public static final String BUSINESS_TYPE_LG = "LG";
	
	/** 自由入库*/
	public static final String BUSINESS_TYPE_GR = "GR";
	
	/** 自由出库*/
	public static final String BUSINESS_TYPE_OT = "OT";
	
	/** 盘点*/
	public static final String BUSINESS_TYPE_CA = "CA";
	
	/** 销售出库*/
	public static final String BUSINESS_TYPE_NS = "NS";
	
	/** 销售入库(退货)*/
	public static final String BUSINESS_TYPE_SR = "SR";
	
	/** 礼品领用（生日礼）*/
	public static final String BUSINESS_TYPE_SP = "SP";
	
	/** 订货*/
	public static final String BUSINESS_TYPE_OD = "OD";
	
    /** 移库*/
    public static final String BUSINESS_TYPE_MV = "MV";
	
	/** 发送消息的目的地（实时推送的消息） */
	public static final String CHERRY4PUBMSGQUEUE = "cherry4PubMsgQueue";
	
	/** 发送消息的目的地（规则处理的消息） */
	public static final String CHERRY_RULE_MSGQUEUE = "cherryRuleMsgQueue";
	
	/** 发送消息的目的地（刷新索引的消息） */
	public static final String CHERRY_IR_MSGQUEUE = "cherryIRMsgQueue";
	
	/** 发送消息的目的地（规则处理的消息）用于 区分BATCH OR CHERRY */
	public static final String IS_CHE_RULE_MSG_FLAG = "CHERRY_RULE";
	
	/** U盘绑定   业务类型（从新后台到终端） */
	public static final String BUSINESS_TYPE_U_C = "UC";
	
	/** U盘绑定   区分  删除绑定   */
	public static final String FLAG_0_U_C = "0";
	
	/** U盘绑定   区分  新增绑定   */
	public static final String FLAG_1_U_C = "1";
	
	/** 规则处理消息业务类型 */
	public static final String MESSAGE_TYPE_RU = "RU";
	
	/** 会员档案消息业务类型 */
	public static final String MESSAGE_TYPE_ME = "ME";
	
	/** 会员扩展信息（和俱乐部关联） */
	public static final String MESSAGE_TYPE_MZ = "MZ";
	
	/** 规则处理消息版本 */
	public static final String MESSAGE_VERSION_RU = "AMQ.103.001";
	
	/** 刷新索引消息版本 */
	public static final String MESSAGE_VERSION_IR = "AMQ.102.001";
	
	/** 会员档案消息版本 */
	public static final String MESSAGE_VERSION_ME = "AMQ.010.001";
	
	/** 会员扩展信息（和俱乐部关联） */
	public static final String MESSAGE_VERSION_MZ = "AMQ.016.001";
	
	/** 规则处理消息业务类型 */
	public static final String MESSAGE_TYPE_1003 = "1003";

	/** 消费者类型 NP：普通个人*/
	public static final String ConsumerType_NP = "NP";
	
	/** 消费者类型 MP：会员个人*/
    public static final String ConsumerType_MP = "MP";

    /** 消费者类型 PF：批发*/
    public static final String ConsumerType_PF = "PF";
    
    /**销售/退货单  明细类型  产品销售*/
    public static final String DETAILTYPE_PRODUCT = "N";
    
    /**销售/退货单  明细类型  促销*/
    public static final String DETAILTYPE_PROMOTION = "P";
    
    /**销售/退货单  明细类型  代理商优惠券*/
    public static final String DETAILTYPE_BC = "BC";
    
    /**销售/退货单  明细类型  支付方式*/
    public static final String DETAILTYPE_PAY = "Y";

    /** 促销品的分类 促销礼品*/
    public static final String PROMOTIONCATECD_CXLP = "CXLP";
    
    /** 促销品的分类 套装折扣*/
    public static final String PROMOTIONCATECD_TZZK = "TZZK";
    
    /** 合并库存  子类型  合并入库 */
    public static final String STOCKHB_HBRK = "HBRK";
    
    /** 合并库存  子类型  合并出库 */
    public static final String STOCKHB_HBCK = "HBCK";

	/** 刷新索引消息业务类型 */
	public static final String MESSAGE_TYPE_1002 = "1002";
	
	/** 等级和化妆次数实时重算MQ消息业务类型 */
	public static final String MESSAGE_TYPE_MR = "MR";
	
	/** 刷新索引消息业务类型 */
	public static final String MESSAGE_TYPE_IR = "IR";
	
	/** 活动类型 促销活动*/
    public static final String ACTIVITYTYPE_PROM = "0";
    
    /** 活动类型 会员活动*/
    public static final String ACTIVITYTYPE_MEM = "1";
    
    /**预约单状态 OK：已经领用*/
    public static final String CAMPAIGNORDER_STATE_OK = "OK";
    
    /**预约单状态 RV：预约完成*/
    public static final String CAMPAIGNORDER_STATE_RV = "RV";
    
    /**预约单状态 AR：已经到货*/
    public static final String CAMPAIGNORDER_STATE_AR = "AR";
    
    /**预约单状态 CA：已经取消*/
    public static final String CAMPAIGNORDER_STATE_CA = "CA";
    
    /**积分兑换（无需预约）状态 0：积分兑换*/
    public static final String PX_TYPE_0 = "0";
    
    /**积分兑换（无需预约）状态 1：积分取消*/
    public static final String PX_TYPE_1 = "1";
    
    /** 问题名称（皮肤类型） */
	public static final String QUESTION_SKINTYPE = "皮肤类型";
	
	/** 盘单单无明细时设置的备注信息*/
	public static final String NULLSTOCKTAKING_COMMENTS = "没有盘差";
	
	/** 销售退货申请*/
	public static final String BUSINESS_TYPE_SA = "SA";
}
