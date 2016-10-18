/*		
 * @(#)BINOLSSPRM73_IF.java     1.0 2016/03/28		
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
package com.cherry.ss.prm.interfaces;

import java.util.List;
import java.util.Map;

import com.cherry.ss.prm.dto.CouponRuleDTO;
import com.cherry.ss.prm.dto.ResultDTO;

/**
 * 优惠券规则IF
 * @author hub
 * @version 1.0 2016.03.28
 */
public interface BINOLSSPRM73_IF {
	
	/**
	 * 取得优惠券规则总数
	 * 
	 * @param map 检索条件
	 * @return 优惠券规则总数
	 */
	public int getRuleInfoCount(Map<String, Object> map);
	
	/**
	 * 取得优惠券规则List
	 * 
	 * @param map 检索条件
	 * @return 优惠券规则List
	 */
	public List<Map<String, Object>> getRuleInfoList(Map<String, Object> map);
	
	/**
	 * 取得优惠券规则详细信息
	 * 
	 * @param map 检索条件
	 * @return 优惠券规则详细信息
	 */
	public CouponRuleDTO getCouponRuleInfo(Map<String, Object> map);
	
	/**
	 * 批量生成优惠券(非会员)
	 * 
	 * @param couponRule 优惠券规则内容
	 * @param coupNum 本批次需要生成的数量
	 * 
	 * @return ResultDTO 执行结果
	 * @throws Exception 
	 */
	public ResultDTO tran_couponBatch(CouponRuleDTO couponRule, int coupNum) throws Exception;
	public ResultDTO tran_couponBatch(CouponRuleDTO couponRule, int coupNum,List<Map<String,Object>> mem_list,List<Map<String,Object>> couponResultList) throws Exception;
	/**
	 * 保存优惠券规则
	 * 
	 * @param map
	 * @return 无
	 * @throws Exception 
	 */
	public void tran_saveCouponRule(CouponRuleDTO couponRule) throws Exception;
	
	/**
	 * 更新审核状态
	 * 
	 * @param ruleCode 规则代码
	 * 
	 */
	public void tran_check(String ruleCode);
	
	/**
	 * 
	 * 导入柜台处理
	 * 
	 * @param map
	 *            导入文件等信息
	 * @return 处理结果信息
	 * 
	 */
	public Map<String, Object> tran_importCounter(Map<String, Object> map) throws Exception;
	
	/**
	 * 取得柜台总数
	 * 
	 * @param map 检索条件
	 * @return 柜台总数
	 */
	public int getCounterDialogCount(Map<String, Object> map);
	
	/**
	 * 取得柜台信息List
	 * 
	 * @param map 检索条件
	 * @return 柜台信息List
	 */
	public List<Map<String, Object>> getCounterDialogList(Map<String, Object> map);
	
	/**
	 * 
	 * 导入会员处理
	 * 
	 * @param map
	 *            导入文件等信息
	 * @return 处理结果信息
	 * 
	 */
	public Map<String, Object> tran_importMember(Map<String, Object> map) throws Exception;
	
	/**
	 * 取得会员总数
	 * 
	 * @param map 检索条件
	 * @return 会员总数
	 */
	public int getMemberDialogCount(Map<String, Object> map);
	
	/**
	 * 取得会员信息List
	 * 
	 * @param map 检索条件
	 * @return 会员信息List
	 */
	public List<Map<String, Object>> getMemberDialogList(Map<String, Object> map);
	public List<Map<String, Object>> getMemberInfoListWithoutPage(Map<String, Object> map);
	/**
	 * 取得等级列表
	 * 
	 * @param brandInfoId 品牌ID
	 * @return 等级列表
	 */
	public List<Map<String, Object>> getLevelList(int brandInfoId);
	
	/**
	 * 设置产品等条件
	 * 
	 * @param map 发券门槛
	 * @return
	 */
	public void condSetting(Map<String, Object> map, int brandInfoId);
	
	/**
	 * 设置内容等条件
	 * 
	 * @param map 发券门槛
	 * @return
	 */
	public void contentSetting(Map<String, Object> map, int brandInfoId);
	/**
	 * 批量生成与会员绑定优惠券时，获取的相关信息
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> tran_importCouponMember(Map<String, Object> map) throws Exception;
	
	public List<Map<String, Object>> getChannelList(Map<String, Object> map) throws Exception;

}
