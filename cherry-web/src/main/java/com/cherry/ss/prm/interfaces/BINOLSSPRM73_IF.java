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

import com.cherry.ss.prm.dto.CouponRuleDTO;
import com.cherry.ss.prm.dto.ResultDTO;

import java.util.List;
import java.util.Map;

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
	 * 设置内容等条件
	 *
	 * @param map
	 * @return
	 */
	public void contentSetting2(Map<String, Object> map, int brandInfoId);
	/**
	 * 批量生成与会员绑定优惠券时，获取的相关信息
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> tran_importCouponMember(Map<String, Object> map) throws Exception;
	
	public List<Map<String, Object>> getChannelList(Map<String, Object> map) throws Exception;

	/**
	 * 券规则设置导入柜台通用
	 * @param map
	 * @return
	 * @throws Exception
     */
	public Map<String,Object> tran_importCounterExecl(Map<String, Object> map) throws Exception;

	/**
	 * 券规则设置导入产品通用
	 * @param map
	 * @return
	 * @throws Exception
     */
	public Map<String,Object> tran_importProductExecl(Map<String, Object> map) throws Exception;

	/**
	 * 券规则设置导入会员通用
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public Map<String,Object> tran_importMemberExecl(Map<String, Object> map) throws Exception;

	/**
	 * 获取失败导入总数
	 * @param map
	 * @return
     */
	public int getFailUploadCount(Map<String,Object> map);

	/**
	 * 获取导入失败List
	 * @param map
	 * @return
	 * @throws Exception
     */
	public List<Map<String,Object>> getFailUploadList(Map<String,Object> map) throws Exception;

//	/**
//	 * 获取导入失败产品List
//	 * @param map
//	 * @return
//	 * @throws Exception
//	 */
//	public List<Map<String,Object>> getFailUploadProductList(Map<String,Object> map) throws Exception;
//
//	/**
//	 * 获取导入失败会员List
//	 * @param map
//	 * @return
//	 * @throws Exception
//	 */
//	public List<Map<String,Object>> getFailUploadMemberList(Map<String,Object> map) throws Exception;

	/**
	 * 获取电子券产品明细表数据
	 * @param map
	 * @return
	 */
	public List<Map<String,Object>> getPrtForCouponProductDetail(Map<String,Object> map);

	/**
	 * 获取电子券产品明细表导入的数据(有2先取2,无2取1)
	 * @param map
	 * @return
	 */
	public List<Map<String,Object>> getImpPrtForCouponProductDetail(Map<String,Object> map);

	/**
	 * 获取电子券产品明细表数据
	 * @param map
	 * @return
	 */
	public List<Map<String,Object>> getCateForCouponProductDetail(Map<String,Object> map);
	/**
	 * 获取柜台列表（黑名单/白名单）(发送门槛/使用门槛)
	 * @param map
	 * @return
	 * @throws Exception
     */
	public List<Map<String,Object>> getCounterList(Map<String,Object> map) throws Exception ;

	/**
	 * 删除柜台列表（黑名单/白名单）(发送门槛/使用门槛)
	 * @param map
	 * @return
	 * @throws Exception
     */
	public int delCounter(Map<String,Object> map) throws Exception ;

	/**
	 * 获取会员导入列表List
	 * @param map
	 * @return
	 * @throws Exception
     */
	public List<Map<String,Object>> getExeclUploadMemberList(Map<String,Object> map) throws Exception;

	/**
	 *
	 * 删除电子券产品明细数据
	 *
	 * @param map
	 * 			删除条件
	 *
	 */
	public int delCouponProductDetail(Map<String, Object> map);

	/**
	 *
	 * 删除电子券导入产品明细数据
	 *
	 * @param map
	 * 			删除条件
	 *
	 */
	public int tran_delImpCouponProductDetail(Map<String, Object> map);

	/**
	 * 删除电子券导入会员明细
	 * @param map
	 * 删除条件
	 */
	public int tran_delImpCouponMemberDetail(Map<String, Object> map);

	/**
	 * 新增电子券产品明细表
	 * @param list
	 */
	public void addCouponProductDetail(List<Map<String, Object>> list);

	public void setMemberList(List<Map<String,Object>> levelList,Map<String,Object> sendCond,String ruleCode);

	public byte[] exportExcel(Map<String, Object> map) throws Exception;

	/**
	 * 根据ruleCode清除明细表中isTemp = 2的临时数据
	 * @param ruleCode
     */
	public void deleteTempDataByRuleCode(String ruleCode);

	public int removeCntLi(Map<String,Object> param);

	public List<Map<String,Object>> seachChannelWay(Map<String,Object> param);

	/**
	 * 使用门槛-确定后处理导入数据的问题
	 * @param param
	 * @return
     */
	public String tran_confirmUseCond(Map<String,Object> param);

	/**
	 * 使用门槛-取消后处理导入数据的问题
	 * @param param
	 * @return
     */
	public String tran_cancelUseCond(Map<String,Object> param);
}
