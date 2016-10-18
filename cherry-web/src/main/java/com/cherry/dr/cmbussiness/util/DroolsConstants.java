/*  
 * @(#)DroolsConstants.java     1.0 2011/08/23      
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
package com.cherry.dr.cmbussiness.util;

/**
 * 规则执行常量定义文件
 * 
 * @author hub
 * @version 1.0 2011.08.23
 */
public class DroolsConstants {
	
	/** 规则执行理由     0: 规则计算 */
	public static final int RULE_EXEC_REASON0 = 0;
	
	/** 规则执行理由     1: 初始数据导入 */
	public static final int RULE_EXEC_REASON1 = 1;
	
	/** 规则执行理由     2: 金额清零 */
	public static final int RULE_EXEC_REASON2 = 2;

	/** 履历区分 ：等级 */
	public static final int RECORDKBN_0 = 0;
	
	/** 履历区分 ：累积金额 */
	public static final int RECORDKBN_1 = 1;
	
	/** 履历区分 ：化妆次数 */
	public static final int RECORDKBN_2 = 2;
	
	/** 履历区分 ：积分 */
	public static final int RECORDKBN_3 = 3;
	
	/** 履历区分 ：可兑换金额(化妆次数用) */
	public static final int RECORDKBN_4 = 4;
	
	/** 履历区分 ：累计积分 */
	public static final int RECORDKBN_5 = 5;
	
	/** 履历区分 ：累计兑换积分 */
	public static final int RECORDKBN_6 = 6;
	
	/** 履历区分 ：可兑换积分 */
	public static final int RECORDKBN_7 = 7;
	
	/** 履历区分 ：累计失效积分 */
	public static final int RECORDKBN_8 = 8;
	
	/** 履历区分 ：上次清零处理的截止积分变化时间 */
	public static final int RECORDKBN_9 = 9;
	
	/** 重算类型     0: 等级和化妆次数重算 */
	public static final int RECALCTYPE0 = 0;
	
	/** 重算次数 ：没有重算 */
	public static final int RECALCCOUNT_0 = 0;
	
	/** 重算次数 ：重算 */
	public static final int RECALCCOUNT_1 = 1;
	
	/** 重算区分 ：没有重算 */
	public static final int RECALCFLG_0 = 0;
	
	/** 重算区分 ：重算 */
	public static final int RECALCFLG_1 = 1;
	
	/** 理由 ：规则计算 */
	public static final int REASON_0 = 0;
	
	/** 理由 ：初始数据导入 */
	public static final int REASON_1 = 1;
	
	/** 理由 ：金额清零 */
	public static final int REASON_2 = 2;
	
	/** 理由 ：积分维护 */
	public static final int REASON_5 = 5;
	
	/** 退货的标识 ：普通的退货 */
	public static final String SALESRTYPE_1 = "1";
	
	/** 退货的标识 ：关联退货 */
	public static final String SALESRTYPE_2 = "2";
	
	/** 退货的标识 ：销售 */
	public static final String SALESRTYPE_3 = "3";
	
	/** 业务类型 ：销售 */
	public static final String TRADETYPE_NS = "NS";
	
	/** 业务类型 ：退货 */
	public static final String TRADETYPE_SR = "SR";
	
	/** 业务类型 ：清零 */
	public static final String TRADETYPE_ZC = "ZC";
	
	/** 业务类型 ：降级 */
	public static final String TRADETYPE_DG = "DG";
	
	/** 业务类型 ：积分清零 */
	public static final String TRADETYPE_PC = "PC";
	
	/** 需要更新重算日期 */
	public static final String NEEDUP_RECALCDATE = "1";
	
	/** 作成者 */
	public static final String CREATEDBY = "createdBy";
	
	/** 更新者 */
	public static final String UPDATEDBY = "updatedBy";
	
	/** 作成者名称 */
	public static final String CREATED_NAME = "Drools";
	
	/** 更新者名称 */
	public static final String UPDATED_NAME = "Drools";
	
	/** 作成程序名 */
	public static final String CREATEPGM = "createPGM";
	
	/** 更新程序名 */
	public static final String UPDATEPGM = "updatePGM";
	
	/** 会员活动共通 */
	public static final String PGM_BINBEDRCOM01 = "BINBEDRCOM01";
	
	/** MQ收发日志表共通处理 */
	public static final String PGM_BINBEDRCOM02 = "BINBEDRCOM02";
	
	/** 会员积分规则处理器 */
	public static final String PGM_BINBEDRPOI01 = "BINBEDRPOI01";
	
	/** 历史积分调整 */
	public static final String PGM_BINBEDRPOI03 = "BINBEDRPOI03";
	
	/** 无效会员卡号 */
	public static final String INVALID_MEMBERCODE = "000000000";
	
	/** 非会员 */
	public static final int NOT_MEMBER = 0;
	
	/** 无效会员卡 */
	public static final int CARDVALIDFLAG_0 = 0;
	
	/** 有效会员卡 */
	public static final int CARDVALIDFLAG_1 = 1;
	
	/** 默认修改回数 */
	public static final String DEF_MODIFYCOUNTS = "0";
	
	/** 换行符 */
	public static final String LINE_BREAK = "\r\n";
	
	/** 空格 */
	public static final String LINE_SPACE = " ";
	
	/** 原因最大长度 */
	public static final int REASON_MAX_LENGTH = 1000;
	
	/** 分割符1：逗号 */
	public static final String SPLIT1 = ",";
	
	/** 版本号 */
	public static final String MQ_VERSION = "[Version]";
	
	/** 字段名行 */
	public static final String MQ_COMMLINE = "[CommLine]";
	
	/** 主数据行 */
	public static final String MQ_MAINDATALINE = "[MainDataLine]";
	
	/** 明细数据行 */
	public static final String MQ_DETAILDATALINE = "[DetailDataLine]";
	
	/** 结尾 */
	public static final String MQ_END = "[End]";
	
	/** 结尾 */
	public static final String MQ_REASON_COMMA = "、";
	
	/** 数据插入方标志:CHERRY */
	public static final String MQ_SOURCE_CHERRY = "CHERRY";
	
	/** 消息方向:发送 */
	public static final String MQ_SENDORRECE_S = "S";
	
	/** 单据类型:化妆次数 */
	public static final String MQ_BILLTYPE_MG = "MG";
	
	/** 单据类型:等级 */
	public static final String MQ_BILLTYPE_ML = "ML";
	
	/** 单据类型:积分 */
	public static final String MQ_BILLTYPE_MP = "MP";
	
	/** 消息发送接收标志位:未做比对处理 */
	public static final String MQ_RECEIVEFLAG_0 = "0";
	
	/** 消息发送接收标志位:对方接收已成功 */
	public static final String MQ_RECEIVEFLAG_1 = "1";
	
	/** 消息发送接收标志位:对方接收失败 */
	public static final String MQ_RECEIVEFLAG_2 = "2";
	
	/** 变化原因（化妆次数使用） */
	public static final String MQ_USEDTIMES_REASON = "化妆次数使用";
	
	/** 变化原因（初始数据采集） */
	public static final String MQ_INITDATA_REASON = "初始数据采集";
	
	/** 变化原因（会员等级化妆次数重算） */
	public static final String MQ_RECALCMEMLEVEL_REASON = "会员等级化妆次数重算";
	
	/** 变化原因（初始导入） */
	public static final String MQ_INITIMPORT_REASON = "初始导入";
	
	/** 保持的等级ID */
	public static final String KEEP_LEVEL_REASON = "系统自动进行保级处理";
	
	/** 通过会员资料维护等级 */
	public static final String CHANGE_LEVEL_REASON = "通过会员资料维护等级";
	
	/** 保持的等级ID */
	public static final String KEEP_LEVELID = "KEEP_LEVELID";
	
	/** 保持的等级条件 */
	public static final String KEEP_LEVELINFO = "KEEP_LEVELINFO";
	
	/** 日期格式 */
	public static final String DATE_PATTERN = "yyyy-MM-dd";
	
	/** 会员消息类型  新增会员 */
	public static final String MEM_TYPE_NEW_MEMBER = "0";
	
	/** 会员消息类型  修改会员 */
	public static final String MEM_TYPE_EDIT_MEMBER = "1";
	
	/** 会员消息类型  会员换卡 */
	public static final String MEM_TYPE_CARD_CHANGE = "2";
	
	/** 业务主体  会员 */
	public static final String TRADEENTITY_0 = "0";
	
	/** 业务主体  内部员工 */
	public static final String TRADEENTITY_1 = "1";
	
	/** 异常信息类型 1: 会员相关处理异常,需要更新会员重算表*/
	public static final int ERROR_TYPE_1 = 1;

	/** 全角逗号 */
	public static final String FULL_COMMA = "，";
	
	/** 全角分号 */
	public static final String FULL_SEMICOLON = "；";
	
	/** 促销活动积分兑换类别码*/
	public static final String TYPE_CODE_DHCP = "DHCP";
	
	/** 促销活动积分兑现类别码*/
	public static final String TYPE_CODE_DHMY = "DHMY";
	
	/** 积分类型:积分维护*/
	public static final String POINTTYPE0 = "0";
	
	/** 积分类型:积分奖励*/
	public static final String POINTTYPE1 = "1";
	
	/** 积分类型:积分清零*/
	public static final String POINTTYPE2 = "2";
	
	/** 积分类型:初始积分*/
	public static final String POINTTYPE4 = "4";
	
	/** 积分类型:兑换积分*/
	public static final String POINTTYPE6 = "6";
	
	/** 积分类型:积分维护(差值)*/
	public static final String POINTTYPE99 = "99";
	
	/** 积分计算理由*/
	public static final String POINTREASON0 = "超过{0}{1}金额上限，超出部分不计";
	
	/** 积分计算理由*/
	public static final String POINTREASON1 = "超过{0}{1}金额上限，超出部分重新计算。计算倍数为:{3}";
	
	/** 积分计算理由*/
	public static final String POINTREASON2 = "超过{0}{1}积分上限，超出部分不计";
	
	/** 积分计算理由*/
	public static final String POINTREASON3 = "超过{0}{1}积分上限，超出部分重新计算。计算倍数为:{3}";
	
	/** 积分计算理由的参数*/
	public static final String POINTREASON_P0 = "每单";
	
	/** 积分计算理由的参数*/
	public static final String POINTREASON_P1 = "每天";
	
	/** 积分计算理由的参数*/
	public static final String POINTREASON_P2 = "活动期间";
	
	/** 销售类型 正常销售 **/
	public static final String SALE_TYPE_NORMAL_SALE = "N";
	
	/** 销售类型 促销 **/
	public static final String SALE_TYPE_PROMOTION_SALE = "P";
	
	/** 积分失效区分:无有效期*/
	public static final String DISABLEFLAG0 = "0";
	
	/** 积分失效区分:具有有效期*/
	public static final String DISABLEFLAG1 = "1";
	
	/** 积分失效区分:永久有效*/
	public static final String DISABLEFLAG2 = "2";
	
	/** 单品四舍五入*/
	public static final String REASON0 = "单品四舍五入";
	
	/** 单品只舍不入*/
	public static final String REASON1 = "单品只舍不入";
	
	/** 单品只入不舍*/
	public static final String REASON2 = "单品只入不舍";
	
	/** 系统自动维护*/
	public static final String REASON3 = "系统自动维护";
	
	/** 下划线*/
	public static final String SYMBOL_UNDERLINE = "_";
	
	/** 会员活动类型：入会*/
	public static final String CAMPAIGN_TYPE1 = "1";
	
	/** 会员活动类型：升降级*/
	public static final String CAMPAIGN_TYPE2 = "2";
	
	/** 会员活动类型：积分*/
	public static final String CAMPAIGN_TYPE3 = "3";
	
	/** 会员活动类型：积分清零*/
	public static final String CAMPAIGN_TYPE8888 = "8888";
	
	/** 会员活动类型：降级*/
	public static final String CAMPAIGN_TYPE9999 = "9999";
	
	/** 升降级区分：升级*/
	public static final String UPKBN_1 = "1";
	
	/** 升降级区分：降级*/
	public static final String UPKBN_2 = "2";
	
	/** 升降级区分：失效*/
	public static final String UPKBN_3 = "3";
	
	/** 会员活动类型：入会*/
	public static final String CAMPAIGN_TYPE_1 = "1";
	
	/** 会员活动类型：升降级*/
	public static final String CAMPAIGN_TYPE_2 = "2";
	
	/** 会员活动类型：积分*/
	public static final String CAMPAIGN_TYPE_3 = "3";
	
	/** 入会区分：入会*/
	public static final int JOINKBN_1 = 1;
	
	/** 会员状态变化的操作类型（添加） */
	public static final String OPERATETYPE_I = "I";
	
	/** 会员状态变化的操作类型（更新）*/
	public static final String OPERATETYPE_U = "U";
	
	/** 会员状态变化的操作类型（删除） */
	public static final String OPERATETYPE_D = "D";
	
	/** 赠送范围：整单 */
	public static final String RANGEKBN_1 = "1";
	
	/** 赠送范围：单品 */
	public static final String RANGEKBN_2 = "2";
	
	/** 附属方式：替换 */
	public static final String SUBRULE_KBN_1 = "1";
	
	/** 附属方式：累加 */
	public static final String SUBRULE_KBN_2 = "2";
	
	/** 积分理由：积分兑换 */
	public static final String POINT_REASON_1 = "积分兑换";
	
	/** 积分理由：剩余金额不积分 */
	public static final String POINT_REASON_2 = "包含积分兑礼，剩余金额不积分";
	
	/** 积分理由：关联退货对冲 */
	public static final String POINT_REASON_3 = "关联退货对冲";
	
	/** 积分理由：剩余金额计算积分 */
	public static final String POINT_REASON_4 = "包含积分兑礼，剩余金额计算积分";
	
	/** 积分理由：清除已失效积分 */
	public static final String POINT_REASON_5 = "清除已失效积分";
	
	/** 积分理由：小数位微调 */
	public static final String POINT_REASON_6 = "小数位微调({0})";
	
	/** 积分理由：折扣金额不参与积分 */
	public static final String POINT_REASON_7 = "折扣抵消";
	
	/** 积分理由：折扣产品不积分 */
	public static final String POINT_REASON_8 = "折扣产品不积分";
	
	/** 规则属性 : 普通规则 */
	public static final String POINTRULEKBN_1 = "1";
	
	/** 规则属性 : 附属规则 */
	public static final String POINTRULEKBN_2 = "2";
	
	/** 规则属性 : 组合规则 */
	public static final String POINTRULEKBN_3 = "3";
	
	/** 奖励范围 : 单品 */
	public static final String RANGE_FLAG_0 = "0";
	
	/** 奖励范围 : 整单 */
	public static final String RANGE_FLAG_1 = "1";
	
	/** 积分兑礼设置 : 剩余金额计算积分 */
	public static final String DHCP_FLAG_1 = "1";
	
	/** 积分兑礼设置 : 剩余金额不积分 */
	public static final String DHCP_FLAG_2 = "2";
	
	/** 执行策略 : 最大值 */
	public static final String EXECTYPE_1 = "执行策略 : 最大值";
	
	/** 执行策略 : 平均值*/
	public static final String EXECTYPE_2 = "执行策略 : 平均值";
	
	/** 执行策略 : 累加 */
	public static final String EXECTYPE_3 = "执行策略 : 累加";
	
	/** 组合策略 : 最大值 */
	public static final String COMB_EXECTYPE_1 = "组合策略 : 最大值";
	
	/** 组合策略 : 平均值*/
	public static final String COMB_EXECTYPE_2 = "组合策略 : 平均值";
	
	/** 组合策略 : 累加 */
	public static final String COMB_EXECTYPE_3 = "组合策略 : 累加";
	
	/** 查询的规则ID区分 : 附属规则ID */
	public static final String SEARCHIDKBN_1 = "1";
	
	/** 查询的规则ID区分 : 主规则ID */
	public static final String SEARCHIDKBN_2 = "2";
	
	/** 积分变化理由最大值*/
	public static final int MAX_POINT_RESON_SIZE = 300;
	
	/** 发送履历区分 */
	public static final String SEND_RECORDS_KBN = "SEND_RECORDS_KBN";
	
	/** 1:发送所有履历 */
	public static final String SEND_ALL_RECORDS = "1";
	
	/** 2:发送历史履历 */
	public static final String SEND_HIST_RECORDS = "2";
	
	/** 下发等级履历区分 */
	public static final String LEVEL_MQ_KBN = "LEVEL_MQ_KBN";
	
	/** 0:不下发*/
	public static final String NO_LEVEL_MQ = "0";
	
	/** 按新政策调整会员等级*/
	public static final String LEVEL_ADJUST_RESON = "按新政策调整会员等级";
}
