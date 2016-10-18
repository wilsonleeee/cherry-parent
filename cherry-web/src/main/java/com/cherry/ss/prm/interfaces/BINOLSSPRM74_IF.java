/*
 * @(#)BINOLSSPRM67_IF.java    1.0 2015/09/21
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


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.cherry.cm.core.ICherryInterface;
import com.cherry.wp.common.entity.SaleActivityDetailEntity;
import com.cherry.wp.common.entity.SaleDetailEntity;
import com.cherry.wp.common.entity.SaleProductDetailEntity;
import com.cherry.wp.common.entity.SaleRuleResultEntity;

public interface BINOLSSPRM74_IF extends ICherryInterface {

	/**
	 * 封装传入的参数
	 * @param param
	 * @return
	 */
	public Map<String,Object> convert2Entity(Map<String,Object> main_map,List<Map<String,Object>> cart_list,List<Map<String,Object>> coupon_list);
	/**
	 * 封装传出参数，为页面所用(其中为2部分，一个为页面对应的id,一个为页面展示的result)
	 * @param ruleResult_list
	 * @return
	 */
	public Map<String,Object> convert2JSP(ArrayList<SaleRuleResultEntity> ruleResult_list,ArrayList<SaleActivityDetailEntity> saleactivity_out);
	/**
	 * json串转换为方法需要的类型
	 * @param param
	 * @return
	 */
	public ArrayList<SaleRuleResultEntity> convert2Rule(List<Map<String,Object>> param);
	/**
	 * 明细转成Map
	 * @param detail_list
	 * @return
	 * @throws Exception
	 */
	public List<Map<String,Object>> detail2List(ArrayList<SaleDetailEntity> detail_list) throws Exception;
	/**
	 * 封装成2个部分，主单信息 原始购物车信息
	 * @param param
	 * @return
	 */
	public Map<String,Object> convert2Part(Map<String,Object> param);
	/**
	 * 插入主单数据
	 * @param main_map
	 * @return
	 */
	public void insertMain(Map<String,Object> main_map);
	/**
	 * 插入优惠券数据
	 * @param rule_list
	 */
	public void insertCoupon(List<Map<String,Object>> rule_list,String TN);
	/**
	 * 插入规则数据
	 * @param rule_list
	 */
	public void insertRule(List<Map<String,Object>> rule_list,String TN);
	/**
	 * 插入购物车数据
	 * @param cart_list
	 */
	public void insertCart(List<Map<String,Object>> cart_list,String TN);
	/**
	 * 获取对应数据源名称
	 * @param param_map
	 * @return
	 */
	public Map<String,Object> getDateSourceName(Map<String,Object> param_map);
	/**
	 * 封装页面右侧的产品列表，通过maincode分组
	 * @param saleproduct_out
	 * @return
	 */
	public List<Map<String,Object>> convertProduct(ArrayList<SaleProductDetailEntity> saleproduct_out);
	/**
	 * 获取组织ID
	 * @param param_map
	 * @return
	 */
	public Map<String,Object> getOrganizationID(Map<String,Object> param_map);
	/**
	 * 查询主单数据中是否存在数据，存在的话先进行物理删除
	 * @param param
	 */
	public void checkMain(Map<String,Object> param);
	/**
	 * 计算方法时，将原有的全部优惠券列表与计算后的优惠券信息进行合并
	 * @param coupon_list
	 * @param couponAll_list
	 * @return
	 */
	public List<Map<String,Object>> convert2Coupon(List<Map<String,Object>> coupon_list,List<Map<String,Object>> couponAll_list,List<Map<String,Object>> couponCheck_list);
	/**
	 * 获取该柜台与适应全部柜台的活动
	 */
	public List<Map<String,Object>> convert2AllActivity(Map<String,Object> param);
	/**
	 * 优惠券智能促销双重计算后的购物车数据
	 * @param detail_list
	 * @return
	 * @throws Exception
	 */
	public List<Map<String,Object>> detail2ComputedList(ArrayList<SaleDetailEntity> detail_list) throws Exception ;
	/**
	 * 获取计算完毕的促销活动TZZK与DH结果
	 * @param detail_list
	 * @return
	 * @throws Exception
	 */
	public List<Map<String,Object>> detail2RuleList(ArrayList<SaleDetailEntity> detail_list) throws Exception ;
	/**
	 * 转换购物车添加产商ID字段
	 * @return
	 */
	public List<Map<String,Object>> collect2pro(List<Map<String,Object>> cart_list);
	/**
	 * 通过单据号获得主单信息
	 * @param param
	 * @return
	 */
	public Map<String,Object> getMainByTradeNo(Map<String,Object> param);
	/**
	 * 通过单据号获得活动购物车信息
	 * @param param
	 * @return
	 */
	public List<Map<String,Object>> getShoppingCartByTradeNo(Map<String,Object> param);
	/**
	 * 通过单据号获得原单参加的活动信息
	 * @param param
	 * @return
	 */
	public List<Map<String,Object>> getRuleByTradeNo(Map<String,Object> param);
	/**
	 * 通过单据号获得原单参加的Coupon信息
	 * @param param
	 * @return
	 */
	public List<Map<String,Object>> getCouponByTradeNo(Map<String,Object> param);
	/**
	 * 对返回的rule_list进行封装，加上标识 让前端同一组的用radio 单独的用checkbox
	 * @param rule_list
	 * @return
	 */
	public List<Map<String,Object>> groupSendRule(List<Map<String,Object>> rule_list);
	/**
	 * 接口返回的产品根据Maincode进行分组
	 * @param ProductList
	 * @return
	 */
	public List<Map<String,Object>> couponProOrder(List<Map<String,Object>> ProductList);
	/**
	 * 删选对应checkFlag为1的活动
	 * @param rule_list
	 * @return
	 */
	public List<Map<String,Object>> checkRule(List<Map<String,Object>> rule_list);
	/**
	 * 代物券核券操作
	 * @param param
	 */
	public void updateProCoupon(Map<String,Object> param);
	/**
	 * 获取无门槛券个数
	 * @param param
	 * @return
	 */
	public int getNoMemberCouponCount();
	/**
	 * 合并完整的促销活动 1+（2&3）
	 * @param rule_list 1、计算完毕的规则（N类型）和产品有交互的活动
	 * @param promotionRule_list 2、促销引擎计算完毕的规则（所有P类型）
	 * @param pointRule_list 3、计算完毕的P类型积分兑换活动(和2数据进行合并用)
	 * @return
	 */
	public List<Map<String, Object>> convertRuleList(List<Map<String,Object>>  rule_list,List<Map<String,Object>>  promotionRule_list,List<Map<String,Object>>  pointRule_list);
	
}
