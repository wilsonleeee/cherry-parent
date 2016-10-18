/*      
 * @(#)BINOLCM31_Service.java     1.0 2012/05/10        
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
package com.cherry.cm.cmbussiness.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;
import com.cherry.dr.cmbussiness.dto.core.CampBaseDTO;
import com.cherry.dr.cmbussiness.dto.core.PointChangeDTO;
import com.cherry.dr.cmbussiness.dto.core.PointChangeDetailDTO;
import com.cherry.dr.cmbussiness.dto.core.PointDTO;
import com.cherry.dr.cmbussiness.util.DoubleUtil;
import com.googlecode.jsonplugin.JSONUtil;

/**
 * 规则处理 Service
 * @author hub
 *
 */
public class BINOLCM31_Service extends BaseService{
	@Resource
	private  BINOLCM31_Service binOLCM31_Service;
	/**
	 * 取得会员等级信息
	 * 
	 * @param map
	 * 			参数集合
	 * @return Map
	 * 			会员等级信息
	 */
	public Map<String, Object> getMemLevelcomInfo(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM31.getMemLevelcomInfo");
		return (Map<String, Object>) baseServiceImpl.get(map);
	}
	
	/**
	 * 取得系统默认等级
	 * @param map
	 * @return
	 */
	public int getDefaultLevel(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM31.getDefaultLevel");
		Integer result = (Integer) baseServiceImpl.get(map);
		if (null != result) {
			return result;
		}
		return 0;
	}
	
	/**
	 * 取得注册会员信息
	 * 
	 * @param Map
	 *			查询条件
	 * @return Map
	 *			注册会员信息
	 */
	public Map<String, Object> getMemRegisInfo(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM31.getMemRegisInfo");
		return (Map<String, Object>) baseServiceImpl.get(map);
	}
	
	/**
	 * 取得会员信息
	 * 
	 * @param Map
	 *			查询条件
	 * @return Map
	 *			会员信息
	 */
	public Map<String, Object> getMemberInfo(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM31.getMemberInfo");
		return (Map<String, Object>) baseServiceImpl.get(map);
	}
	
	/**
	 * 取得会员等级List(非默认)
	 * 
	 * @param map
	 * 			
	 * @return List
	 * 			会员等级List
	 * @throws Exception 
	 */
	public List<Map<String, Object>> getNoDefLevelList(Map<String, Object> map) throws Exception{
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM31.getNoDefLevelList");
		List<Map<String, Object>> levelList = (List<Map<String, Object>>) baseServiceImpl.getList(map);
		// 循环等级列表
		if (null != levelList) {
			for (Map<String, Object> levelInfo : levelList) {
				// 会员等级有效期
				String periodValidity = (String) levelInfo.get("periodValidity");
				if (!CherryChecker.isNullOrEmpty(periodValidity)) {
					Map<String, Object> periodValidityMap = (Map<String,Object>) 
					JSONUtil.deserialize(periodValidity);
					levelInfo.put("periodValidity", periodValidityMap);
				}
			}
		}
		return levelList;
	}
	
	/**
	 * 插入天猫会员同步表
	 * 
	 * @param map
	 * 			会员同步信息
	 */
	public void addTmallMemSyncInfo(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM31.addTmallMemSyncInfo");
		baseServiceImpl.save(map);
	}
	
	/**
	 * 
	 * 更新会员同步信息
	 * 
	 * @param map 更新条件
	 * @return 更新件数
	 */
	public int updateMemSyncInfo(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM31.updateMemSyncInfo");
		return baseServiceImpl.update(map);
	}
	
	/**
	 * 检查天猫会员是否已绑定
	 * 
	 * @param map
	 * @return
	 */
	public int getBindMemCount(int memberInfoId) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("memberInfoId", memberInfoId);
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM31.getBindMemCount");
		return baseServiceImpl.getSum(map);
	}
	
	/**
	 * 取得规则ID
	 * @param map
	 * @return
	 */
	public int getRuleIdByCode(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM31.getRuleIdByCode");
		Integer result = (Integer) baseServiceImpl.get(map);
		if (null != result) {
			return result;
		}
		return 0;
	}
	
	/**
	 * 取得规则内容
	 * @param map
	 * @return
	 */
	public String getRuleContent(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM31.getRuleContent");
		return (String) baseServiceImpl.get(map);
	}
	
	/**
	 * 插入规则表并返回规则ID
	 * 
	 * @param map
	 * @return int
	 */
	public int insertRuleContent(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID,
				"BINOLCM31.insertRuleContent");
		return baseServiceImpl.saveBackId(map);
	}
	
	/**
	 * 取得会员初始采集信息
	 * 
	 * @param map
	 * 			参数集合
	 * @return Map
	 * 			会员等级信息
	 */
	public Map<String, Object> getMemInitialInfo(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM31.getMemberInitialInfo");
		return (Map<String, Object>) baseServiceImpl.get(map);
	}
	
	/**
	 * 取得会员初始采集信息(会员俱乐部)
	 * 
	 * @param map
	 * 			参数集合
	 * @return Map
	 * 			会员等级信息
	 */
	public Map<String, Object> getClubMemInitialInfo(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM31.getClubMemberInitialInfo");
		return (Map<String, Object>) baseServiceImpl.get(map);
	}
	
	/**
	 * 取得会员初始积分信息
	 * 
	 * @param map
	 * 			参数集合
	 * @return Map
	 * 			会员初始积分信息
	 */
	public Map<String, Object> getMemPointInitInfo(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM31.getMemPointInitInfo");
		return (Map<String, Object>) baseServiceImpl.get(map);
	}
	
	/**
	 * 取得单据对应的BA卡号及柜台号信息(销售)
	 * 
	 * @param map
	 * 			参数集合
	 * @return Map
	 * 			BA卡号及柜台号信息
	 */
	public Map<String, Object> getSaleBaCounterInfo(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM31.getSaleBaCounterInfo");
		return (Map<String, Object>) baseServiceImpl.get(map);
	}
	
	/**
	 * 取得单据对应的BA卡号及柜台号信息(订单)
	 * 
	 * @param map
	 * 			参数集合
	 * @return Map
	 * 			BA卡号及柜台号信息
	 */
	public Map<String, Object> getESBaCounterInfo(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM31.getESBaCounterInfo");
		return (Map<String, Object>) baseServiceImpl.get(map);
	}
	
	/**
	 * 取得单据对应的BA卡号及柜台号信息(初始数据采集)
	 * 
	 * @param map
	 * 			参数集合
	 * @return Map
	 * 			BA卡号及柜台号信息
	 */
	public Map<String, Object> getMSBaCounterInfo(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM31.getMSBaCounterInfo");
		return (Map<String, Object>) baseServiceImpl.get(map);
	}
	/**
     * 取得会员等级List
     * 
     * @param map
     * @return
     * 		会员等级List
     */
    public List<Map<String, Object>> getMemberLevelList(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		// 组织信息ID
		paramMap.put(CherryConstants.ORGANIZATIONINFOID, map
				.get(CherryConstants.ORGANIZATIONINFOID));
		// 品牌信息ID
		paramMap.put(CherryConstants.BRANDINFOID, map
				.get(CherryConstants.BRANDINFOID));
		// 取得业务系统时间
		String busDate = binOLCM31_Service.getBussinessDate(paramMap);
		// 插入业务日期
		paramMap.put("busDate", busDate);
		// 会员等级ID
		paramMap.put("memberLevelId", map.get("memberLevelId"));
		// 有效期开始日
		paramMap.put("campaignFromDate", map.get("campaignFromDate"));
		// 有效期结束日
		paramMap.put("campaignToDate", map.get("campaignToDate"));
		paramMap.put("memberClubId", map.get("memberClubId"));
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM31.getMemberLevelList");
        return baseServiceImpl.getList(paramMap);
    }
    
    /**
	 * 
	 * 插入会员信息扩展表
	 * 
	 * @param map
	 */
	public void addMemberExtInfo(Map<String, Object> map){
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM31.addComMemberExtInfo");
		baseServiceImpl.save(paramMap);
	}
	
	/**
	 * 
	 * 更新会员信息扩展表
	 * 
	 * @param map 更新条件
	 * @return 更新件数
	 */
	public int updateMemberExtInfo(Map<String, Object> map){
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM31.updateComMemberExtInfo");
		return baseServiceImpl.update(paramMap);
	}
	
	/**
	 * 
	 * 插入会员俱乐部等级扩展信息
	 * 
	 * @param map
	 */
	public void addMemberClubExtInfo(Map<String, Object> map){
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM31.addComMemberClubExtInfo");
		baseServiceImpl.save(paramMap);
	}
	
	/**
	 * 
	 * 插入会员俱乐部属性
	 * 
	 * @param map
	 */
	public void addMemClubInfo(Map<String, Object> map){
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM31.addComMemClubInfo");
		baseServiceImpl.save(paramMap);
	}
	
	/**
	 * 
	 * 更新会员俱乐部等级扩展信息
	 * 
	 * @param map 更新条件
	 * @return 更新件数
	 */
	public int updateMemberClubExtInfo(Map<String, Object> map){
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM31.updateComMemberClubExtInfo");
		return baseServiceImpl.update(paramMap);
	}
	
	/**
	 * 
	 * 更新会员俱乐部扩展属性
	 * 
	 * @param map 更新条件
	 * @return 更新件数
	 */
	public int updateClubExtInfo(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM31.updateComClubExtInfo");
		return baseServiceImpl.update(map);
	}
	
	/**
	 * 
	 * 更新会员俱乐部属性 
	 * 
	 * @param map 更新条件
	 * @return 更新件数
	 */
	public int updateMemClubInfo(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM31.updateComMemClubInfo");
		return baseServiceImpl.update(map);
	}
	
	/**
	 * 
	 * 插入会员积分信息表
	 * 
	 * @param map
	 */
	public void addMemberPointInfo(Map<String, Object> map){
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM31.addComMemberPointInfo");
		baseServiceImpl.save(paramMap);
	}
	
	/**
	 * 
	 * 插入会员积分信息表
	 * 
	 * @param map
	 */
	public void addHistoryPointInfo(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM31.addComHistoryPointInfo");
		baseServiceImpl.save(map);
	}
	
	/**
	 * 
	 * 更新会员积分信息表
	 * 
	 * @param map 更新条件
	 * @return 更新件数
	 */
	public int updateMemberPointInfo(Map<String, Object> map){
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM31.updateComMemberPointInfo");
		return baseServiceImpl.update(paramMap);
	}
	
	/**
	 * 
	 * 更新会员积分信息表
	 * 
	 * @param map 更新条件
	 * @return 更新件数
	 */
	public int updateHistoryPointInfo(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM31.updateComHistoryPointInfo");
		return baseServiceImpl.update(map);
	}
	
	/**
	 * 
	 * 更新会员前卡积分值
	 * 
	 * @param map 更新条件
	 * @return 更新件数
	 */
	public int updateMemPreCardPoint(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM31.updateMemPreCardPoint");
		return baseServiceImpl.update(map);
	}
	
	/**
	 * 取得柜台信息
	 * 
	 * @param map
	 * 			参数集合
	 * @return Map
	 * 			柜台信息
	 * 			organizationId : 部门ID
	 * 			departName : 部门名称
	 */
	public Map<String, Object> getComCounterInfo(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM31.getComCounterInfo");
		return (Map<String, Object>) baseServiceImpl.get(map);
	}
	
	/**
	 * 取得员工信息
	 * 
	 * @param map
	 * 			参数集合
	 * @return Map
	 * 			员工信息
	 * 			employeeId : 员工ID
	 */
	public Map<String, Object> getComEmployeeInfo(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM31.getComEmployeeInfo");
		return (Map<String, Object>) baseServiceImpl.get(map);
	}
	
	/**
	 * 插入会员积分变化主表
	 * 
	 * @param pointChangeDTO
	 * 			会员积分变化主 DTO
	 */
	public int addPointChange(PointChangeDTO pointChangeDTO){
		return baseServiceImpl.saveBackId(pointChangeDTO, "BINOLCM31.addComPointChange");
	}
	
	/**
	 * 更新会员积分变化主表
	 * 
	 * @param pointChangeDTO
	 * 			会员积分变化主 DTO
	 */
	public int updatePointChange(PointChangeDTO pointChangeDTO){
		return baseServiceImpl.update(pointChangeDTO, "BINOLCM31.updateComPointChange");
	}
	
	/**
	 * 插入会员积分变化明细表
	 * 
	 * @param pointChangeDTO
	 * 			会员积分变化明细DTO
	 */
	public void addPointChangeDetail(PointChangeDetailDTO pointChangeDetailDTO){
		baseServiceImpl.save(pointChangeDetailDTO, "BINOLCM31.addComPointChangeDetail");
	}
	
	/**
	 * 
	 * 删除会员积分变化明细表
	 * 
	 * @param pointChangeDTO
	 * 			会员积分变化主 DTO
	 * 
	 */
	public int delPointChangeDetail(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM31.delComPointChangeDetail");
		return baseServiceImpl.remove(map);
	}
	
	/**
	 * 取得会员积分变化信息
	 * 
	 * @param campBaseDTO
	 * 			会员活动基础 DTO
	 * @return Map
	 * 			会员积分变化信息
	 * 
	 */
	public Map<String, Object> getPointChangeInfo(PointChangeDTO pointChangeDTO) {
		return (Map<String, Object>) baseServiceImpl.get(pointChangeDTO, "BINOLCM31.getComPointChangeInfo");
	}
	
	/**
	 * 取得会员积分变化信息(积分维护)
	 * 
	 * @param map
	 * 			参数集合
	 * @return Map
	 * 			会员积分变化信息(积分维护)
	 * 
	 */
	public Map<String, Object> getMSPointChangeInfo(PointChangeDTO pointChangeDTO) {
		return (Map<String, Object>) baseServiceImpl.get(pointChangeDTO, "BINOLCM31.getComMSPointChangeInfo");
	}
	
	/**
	 * 取得会员卡号
	 * 
	 * @param map
	 * 			查询参数
	 * @return String
	 * 			会员卡号
	 * 
	 */
	public String getMemCard(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		// 会员信息ID
		paramMap.put("memberInfoId", map.get("memberInfoId"));
		paramMap.put(CherryConstants.IBATIS_SQL_ID,
				"BINOLCM31.getComMemCard");
		return (String) baseServiceImpl.get(paramMap);
	}
	
	/**
     * 取得规则名称列表
     * 
     * @param map
     * @return
     * 		规则名称列表
     */
    public List<Map<String, Object>> getCampRuleNameList(Map<String, Object> map) {
    	map.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM31.getCampRuleNameList");
        return baseServiceImpl.getList(map);
    }
    
    /**
	 * 统计时间段内某规则的所有积分总和
	 * 
	 * @param map
	 * 			参数集合
	 * 			memberInfoId : 会员信息ID
	 * 			fromDate : 开始日期
	 * 			toDate : 结束日期
	 * 			searchIdKbn : 查询的规则ID区分
	 * 			ruleId : 规则ID
	 * @return Map
	 * 			积分总和信息
	 * 			totalPoint : 积分总和
	 */
	public Map<String, Object> getComTotalPointInfo(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM31.getComTotalPointInfo");
		return (Map<String, Object>) baseServiceImpl.get(map);
	}
	
	/**
	 * 取得会员所属柜台信息
	 * 
	 * @param map
	 * 			参数集合
	 * @return Map
	 * 			会员所属柜台信息
	 * 			organizationId : 部门ID
	 * 			cityId : 城市ID
	 * 			counterCode : 柜台号
	 * 			departName  : 柜台名称
	 * 			channelId : 渠道ID
	 */
	public Map<String, Object> getComCouBelongInfo(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM31.getComCouBelongInfo");
		return (Map<String, Object>) baseServiceImpl.get(map);
	}
	
	/**
	 * 取得会员所属柜台信息
	 * 
	 * @param map
	 * 			参数集合
	 * @return Map
	 * 			会员所属柜台信息
	 * 			organizationId : 部门ID
	 * 			cityId : 城市ID
	 * 			counterCode : 柜台号
	 * 			departName  : 柜台名称
	 * 			channelId : 渠道ID
	 */
	public Map<String, Object> getComClubCouBelongInfo(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM31.getComClubCouBelongInfo");
		return (Map<String, Object>) baseServiceImpl.get(map);
	}
	
	/**
     * 取得会员当前积分信息
     * 
     * @param Map
     * 			memberInfoId : 会员信息ID
     * @return Map
     * 			memberPointId : 会员积分ID
     * 			curTotalPoint : 总积分
	 * 			curTotalChanged : 可兑换积分
	 * 			curChangablePoint : 已兑换积分
     */
	public Map<String, Object> getMemberPointInfo(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM31.getComMemberPointInfo");
		return (Map<String, Object>) baseServiceImpl.get(map);
	}
	
	/**
	 * 取得会员当前的积分信息(实体)
	 * @param map
	 * 			查询条件
	 * @return PointDTO
	 * 			会员当前的积分信息
	 */
	public PointDTO getMemberPointDTO(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM31.getPointDTOInfo");
		return (PointDTO)baseServiceImpl.get(map);
	}
	
	/**
	 * 取得会员卡件数
	 * 
	 * @param map
	 * 			查询参数
	 * @return int
	 * 			会员卡件数
	 * 
	 */
	public int getMemCardCount(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID,
				"BINOLCM31.getComMemCardCount");
		return baseServiceImpl.getSum(map);
	}
	
	/**
     * 取得会员当前卡的积分值之和
     * 
     * @param Map
     * 			memberInfoId : 会员信息ID
     * 			memCode : 会员卡号
     * @return Map
     * 			curCardPoint : 当前卡的积分
     */
	public Map<String, Object> getCurPointInfo(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM31.getCurPointInfo");
		return (Map<String, Object>) baseServiceImpl.get(map);
	}
	
	/**
     * 取得会员等级有效期信息
     * 
     * @param Map
     * 			memberLevelId : 会员等级ID
     * @return Map
     * 			validity : 有效期信息
     */
	public Map<String, Object> getComLevelValidInfo(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM31.getComLevelValidInfo");
		return (Map<String, Object>) baseServiceImpl.get(map);
	}
	
	/**
     * 取得会员等级列表
     * 
     * @param map
     * @return
     * 		会员等级列表
	 * @throws Exception 
     */
    public List<Map<String, Object>> getComLevelList(Map<String, Object> map) throws Exception {
    	map.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM31.getComLevelList");
    	List<Map<String, Object>> levelList = (List<Map<String, Object>>) baseServiceImpl.getList(map);
    	// 循环等级列表
		if (null != levelList) {
			for (Map<String, Object> levelInfo : levelList) {
				// 会员等级有效期
				String periodValidity = (String) levelInfo.get("periodValidity");
				if (!CherryChecker.isNullOrEmpty(periodValidity)) {
					Map<String, Object> periodValidityMap = (Map<String,Object>) 
					JSONUtil.deserialize(periodValidity);
					levelInfo.put("periodValidity", periodValidityMap);
				}
			}
		}
		return levelList;
    }
    
    /**
     * 取得会员等级列表
     * 
     * @param map
     * @return
     * 		会员等级列表
     */
    public List<Map<String, Object>> getAllLevelList(Map<String, Object> map) {
    	map.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM31.getComLevelList");
		return (List<Map<String, Object>>) baseServiceImpl.getList(map);
    }
    
    /**
	 * 查询有效期开始的单据信息
	 * 
	 * @param map
	 * 			参数集合
	 * @return List
	 * 			有效期开始的单据信息
	 */
	public List<Map<String, Object>> getValidStartList(Map<String, Object> map){
		List<Map<String, Object>> validStartList = new ArrayList<Map<String, Object>>();
		// 查询有效期开始的单据信息
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM31.getComValidStartList");
		List<Map<String, Object>> validList = (List<Map<String, Object>>) baseServiceImpl.getList(map);
		if (null != validList && !validList.isEmpty()) {
			validStartList.addAll(validList);
		}
		// 查询有效期开始的单据信息(历史履历)
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM31.getComValidStartHistoryList");
		validList = (List<Map<String, Object>>) baseServiceImpl.getList(map);
		if (null != validList && !validList.isEmpty()) {
			validStartList.addAll(validList);
		}
		return validStartList;
	}
	
	/**
	 * 查询等级所有变化履历
	 * 
	 * @param map
	 * 			参数集合
	 * @return List
	 * 			等级所有变化履历
	 */
	public List<Map<String, Object>> getLevelAllRecordList(Map<String, Object> map){
		// 查询等级所有变化履历
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM31.getComLevelAllRecordList");
		return (List<Map<String, Object>>) baseServiceImpl.getList(map);
	}
	
	/**
	 * 取得规则内容
	 * 
	 * @param map
	 * 			查询参数
	 * @return List
	 * 			规则内容List
	 * 
	 */
	public List<Map<String, Object>> getRuleContentList(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		// 规则ID
		paramMap.put("ruleIdArr", map.get("ruleIdArr"));
		paramMap.put(CherryConstants.IBATIS_SQL_ID,
		"BINOLCM31.getComRuleContentList");
		return (List<Map<String, Object>>) baseServiceImpl.getList(paramMap);
	}
	
	/**
	 * 查询指定单据某个属性信息
	 * 
	 * @param map
	 * 			参数集合
	 * @return Map
	 * 			指定单据某个属性信息
	 */
	public Map<String, Object> getBillInfo(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM31.getComBillInfo");
		// 查询指定单据某个属性信息
		Map<String, Object> billInfo = (Map<String, Object>) baseServiceImpl.get(map);
		if (null == billInfo || billInfo.isEmpty()) {
			map.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM31.getComBillHistoryInfo");
			billInfo = (Map<String, Object>) baseServiceImpl.get(map);
		}
		return billInfo;
	}
	
	/**
	 * 取得会员当前的更新信息
	 * 
	 * @param campBaseDTO 查询条件
	 * @return 会员当前的更新信息
	 */
	public Map<String, Object> getMemberUpInfo(CampBaseDTO campBaseDTO){
		return (Map) baseServiceImpl.get(campBaseDTO, "BINOLCM31.getMemberUpInfo");
	}
	
	/**
	 * 查询首单时间
	 * 
	 * @param campBaseDTO
	 * 			会员活动基础 DTO
	 * @return String
	 * 			首单时间
	 * 
	 */
	public String getFirstBillDate(CampBaseDTO campBaseDTO) {
		return (String) baseServiceImpl.get(campBaseDTO, "BINOLCM31.getFirstBillDate");
	}
	
	/**
	 * 查询指定时间段的积分情况
	 * 
	 * @param map
	 * 			查询参数
	 * @return List
	 * 			指定时间段的积分情况
	 * 
	 */
	public List<Map<String, Object>> getPointChangeTimesList(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID,
		"BINOLCM31.getPointChangeTimesList");
		return (List<Map<String, Object>>) baseServiceImpl.getList(map);
	}
	
	/**
	 * 查询指定时间段的积分情况(包含积分为0的记录)
	 * 
	 * @param map
	 * 			查询参数
	 * @return List
	 * 			指定时间段的积分情况
	 * 
	 */
	public List<Map<String, Object>> getPCTimesList(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID,
		"BINOLCM31.getPCTimesList");
		return (List<Map<String, Object>>) baseServiceImpl.getList(map);
	}
	
	/**
	 * 整单全退的单据
	 * 
	 * @param map
	 * 			查询参数
	 * @return List
	 * 			整单全退的单据
	 * 
	 */
	public List<Map<String, Object>> getPCTimesSRList(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID,
		"BINOLCM31.getPCTimesSRList");
		return (List<Map<String, Object>>) baseServiceImpl.getList(map);
	}
	
	/**
	 * 
	 * 更新积分导入明细表
	 * 
	 * @param map 更新条件
	 * @return 更新件数
	 */
	public int updateMemPointImportDetail(Map<String, Object> map){
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM31.updateMemPointImportDetail");
		return baseServiceImpl.update(paramMap);
	}
	
	/**
     * 取得一段时间内的购买信息
     * 
     * @param map
     * @return
     * 		购买信息
     */
    public List<Map<String, Object>> getBillCodeList(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM31.getCMBillCodeList");
        return (List<Map<String, Object>>) baseServiceImpl.getList(map);
    }
    
    /**
	 * 取得当前业务日期对应的可兑换积分截止日期
	 * @param map
	 * @return
	 */
	public String getExPointDate(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM31.getCMExPointDate");
		return (String) baseServiceImpl.get(map);
	}
	
	/**
	 * 取得会员积分变化件数
	 * 
	 * @param map
	 * 			查询参数
	 * @return int
	 * 			会员积分变化件数
	 * 
	 */
	public int getPointChangeCount(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID,
				"BINOLCM31.getComPTChangeCount");
		return baseServiceImpl.getSum(map);
	}
	
	/**
	 * 
	 * 更新积分变化表清零标识
	 * 
	 * @param map 更新条件
	 * @return 更新件数
	 */
	public int delClearFlag(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM31.updateDelClearFlag");
		return baseServiceImpl.update(map);
	}
	
	/**
	 * 取得会员上次积分清零信息
	 * 
	 * @param campBaseDTO 查询条件
	 * @return 会员上次积分清零信息
	 */
	public Map<String, Object> getPreClearDate(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM31.getCMPreClearDate");
		return (Map) baseServiceImpl.get(map);
	}
	
	/**
	 * 取得会员最近一次积分变化信息
	 * 
	 * @param campBaseDTO 查询条件
	 * @return 会员最近一次积分变化信息
	 */
	public Map<String, Object> getLastPointChangeInfo(CampBaseDTO campBaseDTO){
		return (Map) baseServiceImpl.get(campBaseDTO, "BINOLCM31.getComLastPtChangeInfo");
	}
	
	/**
	 * 
	 * 取得会员生日变更履历
	 * 
	 * @param map 查询参数
	 * @return 会员生日变更履历
	 * 
	 */
	public List<Map<String, Object>> getBirthModyList(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM31.getComBirthModyList");
		return (List)baseServiceImpl.getList(map);
	}
	
	/**
	 * 
	 * 取得会员生日类规则ID
	 * 
	 * @param map 查询参数
	 * @return 会员生日类规则ID
	 * @throws Exception 
	 * 
	 */
	public String[] getBirthRuleArr(Map<String, Object> map) throws Exception {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM31.getComBTPointRuleList");
		// 积分规则ID
		List<Map<String, Object>> pointRuleList = baseServiceImpl.getList(map);
		List<String> birthRuleList = new ArrayList<String>();
		if (null != pointRuleList && !pointRuleList.isEmpty()) {
			for (Map<String, Object> pointRule : pointRuleList) {
				String ruleDetail = (String) pointRule.get("ruleDetail");
				// 规则内容
				Map<String, Object> detailMap = (Map<String, Object>) JSONUtil.deserialize(ruleDetail);
				if (null != detailMap) {
					Map<String, Object> baseParams = (Map<String, Object>) detailMap.get("baseParams");
					// 生日类规则
					if (null != baseParams && "PT05".equals(baseParams.get("templateType"))) {
						birthRuleList.add(String.valueOf(pointRule.get("campaignRuleId")));
					}
				}
			}
		}
		String[] arr = null;
		if (!birthRuleList.isEmpty()) {
			arr = birthRuleList.toArray(new String[]{});
		}
		return arr;
	}
	
	/**
	 * 
	 * 取得会员生日规则匹配的履历
	 * 
	 * @param map 查询参数
	 * @return 销售记录List
	 * 
	 */
	public Map<String, Object> getBirthPointDeatilInfo(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM31.getComBirthPointInfo");
		return (Map<String, Object>) baseServiceImpl.get(map);
	}
	
	/**
	 * 
	 * 取得规则条件
	 * 
	 * @param map 查询参数
	 * @return 规则条件
	 * 
	 */
	public Map<String, Object> getRuleCond(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM31.getComRuleCond");
		return (Map<String, Object>) baseServiceImpl.get(map);
	}
	
	/**
	 * 
	 * 取得规则条件列表
	 * 
	 * @param map 查询参数
	 * @return 规则条件
	 * 
	 */
	public List<Map<String, Object>> getRuleCondList(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM31.getComRuleCondList");
		return (List<Map<String, Object>>) baseServiceImpl.getList(map);
	}
	
	/**
	 * 
	 * 统计某段时间的累计金额
	 * 
	 * @param map 查询参数
	 * @return 某段时间的累计金额
	 * 
	 */
	public Map<String, Object> getTotalAmountByTime(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM31.getTotalAmountByTime");
		return (Map<String, Object>) baseServiceImpl.get(map);
	}
	
	/**
	 * 
	 * 查询会员俱乐部属性
	 * 
	 * @param map 查询参数
	 * @return 会员俱乐部属性
	 * 
	 */
	public Map<String, Object> selMemClubInfo(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM31.selComMemClubInfo");
		return (Map<String, Object>) baseServiceImpl.get(map);
	}
	
	/**
	 * 
	 * 查询会员俱乐部首单信息
	 * 
	 * @param map 查询参数
	 * @return 会员俱乐部属性
	 * 
	 */
	public Map<String, Object> selComMemClubFirstSale(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM31.selComMemClubFirstSale");
		return (Map<String, Object>) baseServiceImpl.get(map);
	}
	
	/**
	 * 
	 * 取得会员等级维护记录
	 * 
	 * @param map 查询参数
	 * @return 会员等级维护记录
	 * 
	 */
	public Map<String, Object> getMSLevelRecord(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM31.getMSLevelRecord");
		return (Map<String, Object>) baseServiceImpl.get(map);
	}
	
	/**
	 * 
	 * 更新状态维护明细信息
	 * 
	 * @param map 更新条件
	 * @return 更新件数
	 */
	public int updateMSLevelInfo(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM31.updateMSLevelInfo");
		return baseServiceImpl.update(map);
	}
	
	/**
	 * 
	 * 插入会员使用化妆次数积分主表
	 * 
	 * @param map
	 */
	public int addMSLevelInfo(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM31.addMSLevelInfo");
		return baseServiceImpl.saveBackId(map);
	}
	
	/**
	 * 
	 * 插入会员使用化妆次数积分明细表
	 * 
	 * @param map
	 */
	public void addMSLevelDetail(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM31.addMSLevelDetail");
		baseServiceImpl.save(map);
	}
	
	/**
	 * 取得会员购买记录数
	 * 
	 * @param map
	 * @return
	 */
	public int getSaleCount(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM31.getCMSaleCount");
		return baseServiceImpl.getSum(map);
	}
	
	/**
	 * 取得会员所属柜台信息
	 * 
	 * @param campBaseDTO 查询条件
	 * @return 会员所属柜台信息
	 */
	public Map<String, Object> getMemOrgInfo(CampBaseDTO campBaseDTO){
		return (Map) baseServiceImpl.get(campBaseDTO, "BINOLCM31.getComMemOrgInfo");
	}
	
	/**
	 * 取得规则执行次数
	 * 
	 * @param map
	 * 			查询参数
	 * @return int
	 * 			规则执行次数
	 * 
	 */
	public int getRuleExecCount(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID,
		"BINOLCM31.getComRuleCount");
		return baseServiceImpl.getSum(map);
	}
	
	/**
	 * 取得会员积分明细总数
	 * 
	 * @param map
	 * 			查询参数
	 * @return int
	 * 			会员积分明细总数
	 * 
	 */
	public int getTotalPtlNum(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID,
		"BINOLCM31.getComPTLCount");
		return baseServiceImpl.getSum(map);
	}
	
	/**
	 * 取得会员俱乐部当前等级信息
	 * 
	 * @param map
	 * 			参数集合
	 * @return Map
	 * 			会员俱乐部当前等级信息
	 */
	public Map<String, Object> getClubCurLevelInfo(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM31.getComClubCurLevelInfo");
		return (Map<String, Object>) baseServiceImpl.get(map);
	}
	
	/**
	 * 查询销售单的BA信息
	 * 
	 * @param map
	 * 			参数集合
	 * @return Map
	 * 			销售单的BA信息
	 */
	public Map<String, Object> selBAInfoBySaleId(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM31.selComBAInfoBySaleId");
		return (Map<String, Object>) baseServiceImpl.get(map);
	}
	/**
	 * 取得会员俱乐部代号
	 * 
	 * @param map
	 * 			参数集合
	 * @return String
	 * 			会员俱乐部代号
	 */
	public String getClubCode(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM31.getComClubCode");
		return (String) baseServiceImpl.get(map);
	}
	
	/**
	 * 查询会员俱乐部ID
	 * @param map
	 * @return 
	 */
	public Integer selMemClubId(Map map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM31.selComClubId");
		Map<String, Object> clubMap = (Map<String, Object>) baseServiceImpl.get(map);
		if (null != clubMap && null != clubMap.get("memberClubId")) {
			return Integer.parseInt(clubMap.get("memberClubId").toString());
		}
		return null;
	}
	
	/**
	 * 查询会员俱乐部扩展信息
	 * @param map
	 * @return
	 * 		会员俱乐部扩展信息
	 */
	public List<Map<String, Object>> getClubExtList(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM31.getComClubExtList");
		return (List<Map<String, Object>>)baseServiceImpl.getList(map);
	}
	
	/**
	 * 查询会员俱乐部规则列表
	 * @param map
	 * @return
	 * 		会员俱乐部扩展信息
	 */
	public List<Map<String, Object>> selComClubRuleList(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM31.selComClubRuleList");
		return (List<Map<String, Object>>)baseServiceImpl.getList(map);
	}
	
	/**
     * 取得一段时间内的购买金额
     * 
     * @param map
     * @return
     * 		购买金额
     */
    public double getTtlAmount(Map<String, Object> map) {
    	double ttlAmount = 0;
    	map.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM31.getComTtlAmountInfo");
    	Map<String, Object> resultMap = (Map<String, Object>) baseServiceImpl.get(map);
        if (null != resultMap && null != resultMap.get("ttlAmount")) {
        	ttlAmount = Double.parseDouble(resultMap.get("ttlAmount").toString());
        	if (ttlAmount > 0) {
	        	map.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM31.getComTtlSRAmountInfo");
	        	resultMap = (Map<String, Object>) baseServiceImpl.get(map);
	        	if (null != resultMap && null != resultMap.get("ttlSRAmount")) {
	        		double ttlSRAmount = Double.parseDouble(resultMap.get("ttlSRAmount").toString());
	        		if (ttlSRAmount > 0) {
	        			ttlAmount = DoubleUtil.sub(ttlAmount, ttlSRAmount);
	        		}
	        	}
        	}
        }
        map.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM31.getComESTtlAmountInfo");
    	resultMap =  (Map<String, Object>) baseServiceImpl.get(map);
    	if (null != resultMap && null != resultMap.get("ttlAmount")) {
    		double esAmount = Double.parseDouble(resultMap.get("ttlAmount").toString());
    		ttlAmount = DoubleUtil.add(ttlAmount, esAmount);
    	}
        return ttlAmount;
    }
    
    /**
	 * 取得重算信息记录数
	 * 
	 * @param map
	 * 			查询参数
	 * @return int
	 * 			重算信息记录数
	 * 
	 */
	public int getReCalcCount(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID,
				"BINOLCM31.getReCalcCount");
		return baseServiceImpl.getSum(map);
	}
	
	/**
	 * 等级调整会员插入到临时表
	 * @param memberId
	 */
	public void insertTempAdjustMember(int memberId){
		Map<String,Object> map =new HashMap<String, Object>();
		map.put("memberId", memberId);
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM31.insertTempAdjustMember");
		baseServiceImpl.save(map);
	}
}
