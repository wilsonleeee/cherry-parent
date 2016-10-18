/*  
 * @(#)DroolsMessageUtil.java     1.0 2011/08/23      
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
 * 规则执行信息LOG信息列表
 * 
 * @author hub
 * @version 1.0 2011.08.23
 */
public class DroolsMessageUtil {
	
	public static String getMessage(String msg, String[]params) {
		if (msg != null && params != null) {
			for (int i = 0; i < params.length; i++) {
				String reg = "{" + i + "}";
				if (null == params[i]) {
					params[i] = "";
				}
				msg = msg.replace(reg, params[i]);
			}
		}
		return msg;
	}
	/** 会员信息无记录 */
	public static final String EDR00001 = "该会员卡号无记录！会员卡号：{0}";
	
	/** 添加会员规则执行履历表发生错误 */
	public static final String EDR00002 = "添加会员规则执行履历表发生错误！单据号：{0}；履历区分：{1}";
	
	/** 更新会员信息表错误  */
	public static final String EDR00003 = "更新会员信息表错误！会员卡号：{0}";
	
	/** 加载规则文件失败  */
	public static final String EDR00004 = "加载规则文件失败！文件名：{0}";
	
	/** MQ消息体为空  */
	public static final String EDR00005 = "发送MQ消息异常，MQ消息体为空！单据号：{0}";
	
	/** 明细业务数据行获取失败  */
	public static final String EDR00006 = "会员资料处理器发生异常！会员卡号：{0}。详细异常信息:{1}";
	
	/** 该会员无有效的会员卡号  */
	public static final String EDR00007 = "该会员无有效的会员卡号！会员ID：{0}";
	
	/** 会员等级和化妆次数处理器执行规则时发生异常  */
	public static final String EDR00008 = "会员等级和化妆次数处理器执行规则时发生异常！会员卡号：{0}。详细异常信息:{1}";
	
	/** 没有查到对应商品 */
	public static final String EDR00009 = "规则计算发生异常！没有查到对应商品，产品条码为：{0}，厂商编码为：{1}";
	
	/** 加载规则文件失败  */
	public static final String EDR00010 = "加载规则文件失败！规则类型：{0}";
	
	/** 生成单号失败  */
	public static final String EDR00011 = "清零处理生成单号失败！会员卡号：{0}";
	
	/** 单据时间不正确  */
	public static final String EDR00012 = "单据时间不正确";
	
	/** 关联退货单的关联单号不正确  */
	public static final String EDR00013 = "关联退货单的关联单号不正确！";
	
	/** 执行结果  */
	public static final String EDR00014 = "规则名称：{0}，匹配结果：{1}，原因：{2}，组织代号：{3}，品牌代号：{4}，单据号：{5}，会员卡号：{6}";
	
	/** 执行结果  */
	public static final String EDR00015 = "规则名称：{0}，匹配结果：{1}，计算结果：{2}，组织代号：{3}，品牌代号：{4}，单据号：{5}，会员卡号：{6}";
	
	/** 执行结果  */
	public static final String EDR00016 = "主规则和附属规则计算结果整合开始！{0}是{1}的附属规则，需要对计算结果进行整合，组织代号：{2}，品牌代号：{3}，单据号：{4}，会员卡号：{5}";
	
	/** 执行结果  */
	public static final String EDR00017 = "主规则和附属规则计算结果整合结束！宿主规则：{0}，整合结果：{1}，组织代号：{2}，品牌代号：{3}，单据号：{4}，会员卡号：{5}";
	
	/** 执行结果  */
	public static final String EDR00018 = "规则：{0}需要同时执行默认规则，整合默认规则结果：{1}，组织代号：{2}，品牌代号：{3}，单据号：{4}，会员卡号：{5}";
	
	/** 执行结果  */
	public static final String EDR00019 = "规则：{0}匹配成功，继续匹配下一条规则，组织代号：{1}，品牌代号：{2}，单据号：{3}，会员卡号：{4}";
	
	/** 执行结果  */
	public static final String EDR00020 = "多个规则匹配成功：{0}，需要按执行策略选取结果，计算结果：{1}，组织代号：{2}，品牌代号：{3}，单据号：{4}，会员卡号：{5}";
	
	/** 执行结果  */
	public static final String EDR00021 = "该单最终计算结果：{0}，理由：{1}，组织代号：{2}，品牌代号：{3}，单据号：{4}，会员卡号：{5}";
	
	/** 会员建档处理失败  */
	public static final String EDR00022 = "会员建档处理失败！会员卡号：{0}";
	
	/** 手机号码格式不正确  */
	public static final String EDR00023 = "手机号码格式不正确，不进行建档处理！会员卡号：{0}";
	
	/** 手机号码重复  */
	public static final String EDR00024 = "手机号码重复，不进行建档处理！会员卡号：{0}";
	
	/** 积分上限  */
	public static final String IDR00000 = "超过{0}分，超出部分不计积分({1})";
	
	/** 积分上限  */
	public static final String IDR00001 = "超过{0}分，超出部分按{1}倍重新计算({2})";
	
	/** 程序运行时间  */
	public static final String IDR00002 = "规则计算的运行时间：{0}ms";
	
	/** 换卡扣积分  */
	public static final String IDR00003 = "换卡扣积分，关联单号{0}";
	
	/** 单据日期在初始导入积分日期三个月之前  */
	public static final String IDR00004 = "单据日期在初始导入积分日期三个月之前，不进行积分计算！单号{0}";
	
	/** 单据日期在初始导入积分日期三个月之前  */
	public static final String IDR00005 = "该会员没有初始导入积分记录，不进行历史积分调整处理！单号{0}";
	
	/** 程序运行时间  */
	public static final String IDR00006 = "规则匹配前相关查询的运行时间：{0}ms";
	
	/** 程序运行时间  */
	public static final String IDR00007 = "规则匹配及匹配后的处理总运行时间：{0}ms";
	
	/** 程序运行时间  */
	public static final String IDR00008 = "规则匹配(入会)运行时间：{0}ms";
	
	/** 程序运行时间  */
	public static final String IDR00009 = "计算等级有效期执行时间：{0}ms";
	
	/** 程序运行时间  */
	public static final String IDR00010 = "统计累计金额(升级判断)执行时间：{0}ms";
	
	/** 程序运行时间  */
	public static final String IDR00011 = "规则匹配(升级)运行时间：{0}ms";
	
	/** 程序运行时间  */
	public static final String IDR00012 = "规则匹配(降级)运行时间：{0}ms";
	
	/** 程序运行时间  */
	public static final String IDR00013 = "重算基准点查询运行时间：{0}ms";
	
	/** 程序运行时间  */
	public static final String IDR00014 = "重算主处理运行时间：{0}ms";
	
	/** 程序运行时间  */
	public static final String IDR00015 = "重算后发送MQ运行时间：{0}ms";
	
	/** 程序运行时间  */
	public static final String IDR00016 = "单个会员重算完成总运行时间：{0}ms";
	
	/** 程序运行时间  */
	public static final String IDR00017 = "获取累计金额开始日期执行时间：{0}ms";
	
	/** 程序运行时间  */
	public static final String IDR00018 = "查询累计金额执行时间：{0}ms";
	
	/** 程序运行时间  */
	public static final String IDR00019 = "查询最后一次引起某一属性变化的单据信息执行时间：{0}ms";
	
	/** 支付方式 */
	public static final String IDR00020 = "使用{0}支付的{1}{2}不计算积分";
	
	/** 除去折扣(元)，剩余金额参与积分 */
	public static final String IDR00021 = "折扣抵消{0}元，剩余金额计算积分";
	
	/** 推荐会员积分奖励  */
	public static final String IDR00022 = "推荐新会员{0}({1})入会，获得推荐积分。";
	
	/** 匹配结果  :失败*/
	public static final String PDR00001 = "失败";
	
	/** 匹配结果 :成功 */
	public static final String PDR00002 = "成功";
	
	/** 退货单据 */
	public static final String PDR00003 = "退货单据";
	
	/** 单据金额小于0 */
	public static final String PDR00004 = "单据总金额小于等于0";
	
	/** 会员生日为空 */
	public static final String PDR00005 = "会员生日为空";
	
	/** 非首单 */
	public static final String PDR00006 = "非首单";
	
	/** 不满足生日奖励条件 */
	public static final String PDR00007 = "不满足生日奖励条件";
	
	/** 非入会会员 */
	public static final String PDR00008 = "非入会会员";
	
	/** 不符合规则设定的等级 */
	public static final String PDR00009 = "不符合规则设定的等级";
	
	/** 不满足入会奖励条件 */
	public static final String PDR00010 = "不满足入会奖励条件";
	
	/** 不满足升级奖励条件 */
	public static final String PDR00011 = "不满足升级奖励条件";
	
	/** 不满足特定日期奖励条件 */
	public static final String PDR00012 = "不满足特定日期奖励条件";
	
	/** 不满足会员日奖励条件 */
	public static final String PDR00013 = "不满足会员日奖励条件";
	
	/** 不包含指定商品 */
	public static final String PDR00014 = "不包含指定商品 ";
	
	/** 单据时间小于规则开始日 */
	public static final String PDR00015 = "单据时间小于规则开始日 ";
	
	/** 单据时间大于规则结束日 */
	public static final String PDR00016 = "单据时间大于规则结束日 ";
	
	/** 不符合规则设定的柜台范围 */
	public static final String PDR00017 = "不符合规则设定的柜台范围 ";
	
	/** 匹配失败 */
	public static final String PDR00018 = "匹配失败";
	
	/** 该组合规则匹配方式为满足组内所有规则 */
	public static final String PDR00019 = "该组合规则匹配方式为满足组内所有规则！";
	
	/** 规则不存在 */
	public static final String PDR00020 = "规则ID为{0}，该规则不存在！";
	
	/** 组内规则都没有匹配成功*/
	public static final String PDR00021 = "组内规则都没有匹配成功！";
	
	/** 组内匹配成功的规则*/
	public static final String PDR00022 = "组内匹配成功的规则：";
	
	/** 积分值*/
	public static final String PDR00023 = "积分值：";
	
	/** 小数位处理方式*/
	public static final String PDR00024 = "小数位处理方式：";
	
	/** 该会员没有入会时间 */
	public static final String PDR00025 = "该会员没有入会时间 ";
	
	/** 会员生日日期格式不正确 */
	public static final String PDR00026 = "会员生日日期格式不正确";
	
	/** 元 */
	public static final String PDR00027 = "元";
	
	/** 不符合入会日期条件*/
	public static final String PDR00028 = "不符合入会日期条件";
	
	/** 不符合促销活动积分规则条件*/
	public static final String PDR00029 = "不符合促销活动积分规则条件";
	
	/** 不符合入会日期条件*/
	public static final String PDR00030 = "不符合入会途径条件";
	
	/** 不符合首单时间条件*/
	public static final String PDR00031 = "不符合首单时间条件";
	
	/** 特定商品明细数据有误*/
	public static final String PDR00032 = "特定商品明细数据有误";
	
	/** 积分上限类型:每单*/
	public static final String LIMIT_TYPE0 = "每单";
	
	/** 积分上限类型:每天*/
	public static final String LIMIT_TYPE1 = "每天";
	
	/** 积分上限类型:活动期间*/
	public static final String LIMIT_TYPE2 = "活动期间";
	
	/** 积分上限类型:规则配置每单*/
	public static final String LIMIT_TYPE3 = "规则配置每单";
	
	/** 积分上限类型:规则配置每天*/
	public static final String LIMIT_TYPE4 = "规则配置每天";
	
	/** 关联退货*/
	public static final String REASON_PRESR01 = "由于原单时间早于系统切换时间，该单按默认积分计算。";
}
