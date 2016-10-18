/*      
 * @(#)BINOLCM31_BL.java     1.0 2012/05/10        
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
package com.cherry.cm.cmbussiness.bl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.cm.activemq.dto.MQInfoDTO;
import com.cherry.cm.activemq.interfaces.BINOLMQCOM01_IF;
import com.cherry.cm.cmbeans.Member;
import com.cherry.cm.cmbussiness.dto.BaseDTO;
import com.cherry.cm.cmbussiness.interfaces.BINOLCM31_IF;
import com.cherry.cm.cmbussiness.service.BINOLCM31_Service;
import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherrySecret;
import com.cherry.cm.core.TmallKeyDTO;
import com.cherry.cm.core.TmallKeys;
import com.cherry.cm.mongo.MongoDB;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.cm.util.DateUtil;
import com.cherry.cm.util.SignTool;
import com.cherry.dr.cmbussiness.core.CherryDRException;
import com.cherry.dr.cmbussiness.dto.core.CampBaseDTO;
import com.cherry.dr.cmbussiness.dto.core.PointChangeDTO;
import com.cherry.dr.cmbussiness.dto.core.PointChangeDetailDTO;
import com.cherry.dr.cmbussiness.dto.core.PointDTO;
import com.cherry.dr.cmbussiness.dto.mq.LevelDetailDTO;
import com.cherry.dr.cmbussiness.util.DoubleUtil;
import com.cherry.dr.cmbussiness.util.DroolsConstants;
import com.cherry.dr.cmbussiness.util.DroolsMessageUtil;
import com.cherry.dr.cmbussiness.util.RuleFilterUtil;
import com.cherry.mq.mes.common.MessageConstants;
import com.googlecode.jsonplugin.JSONUtil;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.taobao.api.request.TmallMeiCrmMemberSyncRequest;
import com.taobao.api.response.TmallMeiCrmMemberSyncResponse;


/**
 * 规则处理 BL
 * @author hub
 *
 */
public class BINOLCM31_BL implements BINOLCM31_IF{
	
	@Resource
	private BINOLCM31_Service binOLCM31_Service;
	
	@Resource
	private BINOLCM03_BL binOLCM03_BL;
	
	/** 发送MQ消息共通处理 IF */
	@Resource
	private BINOLMQCOM01_IF binOLMQCOM01_BL;
	
	@Resource
	private BINOLCM14_BL binOLCM14_BL;
	
	private static final Logger logger = LoggerFactory.getLogger(BINOLCM31_BL.class);
	
	/**
	 * 取得会员等级有效期开始日和结束日
	 * 
	 * @param tradeType
	 * 			业务类型
	 * @param ticketDate
	 * 			单据时间
	 * @param memberLevel
	 * 			当前会员等级
	 * @param reCalcDate
	 * 			重算日期（非重算调用设为null）
	 * @param memberId
	 * 			会员ID
	 * @return Map
	 * 			levelStartDate : 等级有效期开始日
	 * 			levelEndDate : 等级有效期结束日
	 * @throws Exception 
	 */
	@Override
	public Map<String, Object> getLevelDateInfo(String tradeType, String ticketDate, int memberLevel, String reCalcDate, int memberId) throws Exception {
		if (!CherryChecker.isNullOrEmpty(ticketDate) && 0 != memberLevel) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("memberLevel", memberLevel);
			//  取得会员等级信息
			Map<String, Object> memLevelInfo = binOLCM31_Service.getMemLevelcomInfo(map);
			if (null != memLevelInfo && !memLevelInfo.isEmpty()) {
				// 会员等级有效期
				String periodValidityStr = (String) memLevelInfo.get("periodValidity");
				if (!CherryChecker.isNullOrEmpty(periodValidityStr)) {
					Map<String, Object> periodValidity = (Map<String,Object>) 
					JSONUtil.deserialize(periodValidityStr);
					if (null != periodValidity && !periodValidity.isEmpty()) {
						boolean isDown = "DG".equals(tradeType);
						String levelStartDate = null;
						// 开始时间区分
						String startTimeKbn = (String) periodValidity.get("startTimeKbn");
						// 成为正式等级当天
						if ("1".equals(startTimeKbn)) {
							// 查询有效期开始的单据信息
							Map<String, Object> validStartInfo = getValidStartInfo(memberLevel, memberId, reCalcDate);
							if (null != validStartInfo) {
								levelStartDate = (String) validStartInfo.get("ticketDate");
							}
						}
						if (null == levelStartDate) {
							// 降级处理
							if (isDown) {
								String ticketDateStr = DateUtil.coverTime2YMD(ticketDate, DateUtil.DATETIME_PATTERN);
								String nextDay = DateUtil.addDateByDays(DateUtil.DATE_PATTERN, ticketDateStr, 1);
								levelStartDate = DateUtil.suffixDate(nextDay, 0);
							} else {
								levelStartDate = ticketDate;
							}
						}
						// 结束日区分
						String endDateKbn = (String) periodValidity.get("normalYear");
						// 等级有效期结束日
						String levelEndDate =  null;
						String reCalcDay = null;
						if (!CherryChecker.isNullOrEmpty(reCalcDate)) {
							reCalcDay = DateUtil.coverTime2YMD(reCalcDate, DateUtil.DATETIME_PATTERN);
						}
						String startDay = DateUtil.coverTime2YMD(levelStartDate, DateUtil.DATETIME_PATTERN);
						String levelStartDateTmp = null;
						int index = 0;
						for (;;) {
							index++;
							// 自然年
							if ("0".equals(endDateKbn)) {
								Date startDate = DateUtil.coverString2Date(startDay);
								Calendar cal = Calendar.getInstance();
								cal.setTime(startDate);
								// 单据年份
								int year = cal.get(Calendar.YEAR);
								// 自然年数
								String memberDate = null;
								if (isDown && "1".equals(periodValidity.get("kpLevel"))) {
									memberDate = (String) periodValidity.get("kpLevelEnd");
								} else {
									memberDate = (String) periodValidity.get("memberDate0");
								}
								if (!CherryChecker.isNullOrEmpty(memberDate)) {
									int memberDateInt = Integer.parseInt(memberDate) - 1;
									// 结束日的年份
									year += memberDateInt;
									// 等级有效期结束日
									levelEndDate = DateUtil.createDate(year, 11, 31, DateUtil.DATE_PATTERN);
								}
								// 年(满12个月)
							} else if ("1".equals(endDateKbn)) {
								// 年数
								String memberDate = (String) periodValidity.get("memberDate1");
								if (!CherryChecker.isNullOrEmpty(memberDate)) {
									int memberDateInt = Integer.parseInt(memberDate);
									int months = memberDateInt * 12;
									// 从开始日起加上设定的年数
									String endDate = DateUtil.addDateByMonth(DateUtil.DATE_PATTERN, startDay, months);
									// 截止日区分
									String endKbn1 = (String) periodValidity.get("endKbn1");
									// 前一天
									if (CherryChecker.isNullOrEmpty(endKbn1) || "0".equals(endKbn1)) {
										levelEndDate = DateUtil.addDateByDays(DateUtil.DATE_PATTERN, endDate, -1);
									} else {
										// 降级处理
										if (index > 1 || "DG".equals(tradeType)) {
											levelEndDate = DateUtil.addDateByDays(DateUtil.DATE_PATTERN, endDate, -1);
										} else {
											// 当天
											if ("1".equals(endKbn1)) {
												levelEndDate = endDate;
												// 当月月底
											} else {
												levelEndDate = DateUtil.getFirstOrLastDateYMD(endDate, 1);
											}
										}
									}
								}
								// 月
							} else if ("2".equals(endDateKbn)) {
								// 月数
								String memberDate = (String) periodValidity.get("memberDate2");
								if (!CherryChecker.isNullOrEmpty(memberDate)) {
									int memberDateInt = Integer.parseInt(memberDate);
									int months = memberDateInt;
									// 从开始日起加上设定的月数
									String endDate = DateUtil.addDateByMonth(DateUtil.DATE_PATTERN, startDay, months);
									// 截止日区分
									String endKbn2 = (String) periodValidity.get("endKbn2");
									// 前一天
									if (CherryChecker.isNullOrEmpty(endKbn2) || "0".equals(endKbn2)) {
										levelEndDate = DateUtil.addDateByDays(DateUtil.DATE_PATTERN, endDate, -1);
										// 当天
									} else {
										// 降级处理
										if (index > 1 || "DG".equals(tradeType)) {
											levelEndDate = DateUtil.addDateByDays(DateUtil.DATE_PATTERN, endDate, -1);
										} else {
											if ("1".equals(endKbn2)) {
												levelEndDate = endDate;
												// 当月月底
											} else {
												levelEndDate = DateUtil.getFirstOrLastDateYMD(endDate, 1);
											}
										}
									}
								}
							}
							if (CherryChecker.isNullOrEmpty(levelEndDate) || CherryChecker.isNullOrEmpty(reCalcDay)) {
								break;
							} else {
								if (DateUtil.compareDate(levelEndDate, reCalcDay) >= 0) {
									if (null != levelStartDateTmp) {
										// 是否使用原有效期开始日
										String moreDate = (String) periodValidity.get("moreDate");
										if ("1".equals(moreDate)) {
											levelStartDate = levelStartDateTmp;
										}
									}
									break;
								} else {
									startDay = DateUtil.addDateByDays(DateUtil.DATE_PATTERN, levelEndDate, 1);
									levelStartDateTmp = DateUtil.suffixDate(startDay, 0);
								}
							}
						}
						levelEndDate = DateUtil.suffixDate(levelEndDate, 1);
						Map<String, Object> levelDateInfo = new HashMap<String, Object>();
						// 等级有效期开始日
						levelDateInfo.put("levelStartDate", levelStartDate);
						// 等级有效期结束日
						levelDateInfo.put("levelEndDate", levelEndDate);
						return levelDateInfo;
					}
				}
			}
		}
		return null;
	}
	/**
	 * 查询有效期开始的单据信息
	 * 
	 * @param memberLevel
	 * 			会员等级ID
	 * @param memberId
	 * 			会员ID
	 * @param lelEndTime
	 * 			截止时间
	 * @return Map
	 * 			单据信息
	 * @throws Exception 
	 */
	@Override
	public Map<String, Object> getValidStartInfo(int memberLevel, int memberId, String lelEndTime) throws Exception{
		Map<String, Object> map = new HashMap<String, Object>();
		// 取得会员等级列表
		List<Map<String, Object>> levelList = binOLCM31_Service.getComLevelList(map);
		// 该等级有效期信息
		Map<String, Object> validityMap = RuleFilterUtil.findLevelValidInfo(memberLevel, levelList);
		if (null != validityMap) {
			map.put("memberInfoId", memberId);
			if (!CherryChecker.isNullOrEmpty(lelEndTime, true)) {
				map.put("lelEndTime", lelEndTime);
			}
			// 查询有效期开始的单据信息
			List<Map<String, Object>> validStartList = binOLCM31_Service.getValidStartList(map);
			if (null == validStartList || validStartList.isEmpty()) {
				return null;
			}
			int size = validStartList.size();
			if (size == 1) {
				return validStartList.get(0);
			}
			int index = size - 1;
			for (int i = 0; i < size; i++) {
				Map<String, Object> validStart = validStartList.get(i);
				// 会员等级
				String newValue = (String) validStart.get("newValue");
				if (!CherryChecker.isNullOrEmpty(newValue, true)) {
					int level = Integer.parseInt(newValue);
					// 非正式等级
					if (!RuleFilterUtil.isFormalLevel(level, levelList)) {
						index = i - 1;
						break;
					}
				}
			}
			if (index >= 0) {
				return validStartList.get(index);
			}
		}
		return null;
	}
	/**
	 * 取得系统默认等级
	 * @param map
	 * 			organizationInfoID: 所属组织ID
	 * 			brandInfoID: 所属品牌ID
	 * 			
	 * @return int 
	 * 			系统默认等级
	 */
	@Override
	public int getDefaultLevel(Map<String, Object> map){
		// 会员俱乐部模式
		String clubMod = binOLCM14_BL.getConfigValue("1299", String.valueOf(map.get("organizationInfoID")), String.valueOf(map.get("brandInfoID")));
		if (!"3".equals(clubMod)) {
			return 0;
		}
		Map<String, Object> searchMap = new HashMap<String, Object>();
		// 所属组织ID
		searchMap.put("organizationInfoID", map.get("organizationInfoID"));
		// 所属品牌ID
		searchMap.put("brandInfoID", map.get("brandInfoID"));
		// 取得系统默认等级
		return binOLCM31_Service.getDefaultLevel(searchMap);
	}
	
	/**
	 * 取得系统天猫默认等级
	 * @param map
	 * 			organizationInfoID: 所属组织ID
	 * 			brandInfoID: 所属品牌ID
	 * 			
	 * @return int 
	 * 			天猫默认等级
	 */
	@Override
	public int getTmallDefaultLevel(Map<String, Object> map){
		Map<String, Object> searchMap = new HashMap<String, Object>();
		// 所属组织ID
		searchMap.put("organizationInfoID", map.get("organizationInfoID"));
		// 所属品牌ID
		searchMap.put("brandInfoID", map.get("brandInfoID"));
		// 取得系统默认等级
		return binOLCM31_Service.getDefaultLevel(searchMap);
	}
	
	/**
	 * 取得俱乐部默认等级
	 * @param map
	 * 			organizationInfoID: 所属组织ID
	 * 			brandInfoID: 所属品牌ID
	 * 			mClubId: 会员俱乐部ID
	 * @return int 
	 * 			俱乐部默认等级
	 */
	@Override
	public int getClubDefaultLevel(Map<String, Object> map){
		Map<String, Object> searchMap = new HashMap<String, Object>();
		// 所属组织ID
		searchMap.put("organizationInfoID", map.get("organizationInfoID"));
		// 所属品牌ID
		searchMap.put("brandInfoID", map.get("brandInfoID"));
		// 会员俱乐部ID
		searchMap.put("mClubId", map.get("mClubId"));
		// 取得系统默认等级
		return binOLCM31_Service.getDefaultLevel(searchMap);
	}
	
	/**
	 * 取得等级(根据入会途径)
	 * @param map
	 * 			organizationInfoID: 所属组织ID
	 * 			brandInfoID: 所属品牌ID
	 * 			sourse: 入会途径
	 * @return int 
	 * 			相应等级
	 */
	@Override
	public int getLevelByChannel(Map<String, Object> map) throws Exception{
		// 入会途径
		String sourse = (String) map.get("sourse");
		if (CherryChecker.isNullOrEmpty(sourse, true)) {
			return 0;
		}
		Map<String, Object> searchMap = new HashMap<String, Object>();
		// 所属组织ID
		searchMap.put("organizationInfoID", map.get("organizationInfoID"));
		// 所属品牌ID
		searchMap.put("brandInfoID", map.get("brandInfoID"));
		// 取得系统默认等级
		List<Map<String, Object>> levelList = binOLCM31_Service.getNoDefLevelList(searchMap);
		if (null != levelList) {
			for (Map<String, Object> levelInfo : levelList) {
				Map<String, Object> periodValidity = (Map<String, Object>) levelInfo.get("periodValidity");
				if (null != periodValidity) {
					// 选择的入会途径
					String cls = (String) periodValidity.get("cls");
					if (!CherryChecker.isNullOrEmpty(cls)) {
						String[] clsArr = cls.split(",");
						if (ConvertUtil.isContain(clsArr, sourse)) {
							return Integer.parseInt(levelInfo.get("levelId").toString());
						}
					}
				}
			}
		}
		return 0;
	}
	
	/**
	 * 取得规则ID
	 * @param code
	 * 			内容代号  			
	 * @return int 
	 * 			规则ID
	 */
	@Override
	public int getRuleIdByCode(String code){
		Map<String, Object> searchMap = new HashMap<String, Object>();
		// 内容代号
		searchMap.put("contentCode", code);
		// 取得规则ID
		int ruleId = binOLCM31_Service.getRuleIdByCode(searchMap);
		if (0 == ruleId) {
			// 内容代号
			searchMap.put("ruleContent", CherryConstants.RULECONTENT.getRuleContent(code));
			// 内容代号
			searchMap.put("campaignRuleId", CherryConstants.RULE_ALL);
			// 作成程序名
			searchMap.put(CherryConstants.CREATEPGM, "BINOLCM31BL");
			// 更新程序名
			searchMap.put(CherryConstants.UPDATEPGM, "BINOLCM31BL");
			// 作成者
			searchMap.put(CherryConstants.CREATEDBY, "BINOLCM31BL");
			// 更新者
			searchMap.put(CherryConstants.UPDATEDBY, "BINOLCM31BL");
			// 插入规则表并返回规则ID
			ruleId = binOLCM31_Service.insertRuleContent(searchMap);
		}
		return ruleId;
	}
	
	/**
	 * 取得规则内容
	 * @param int
	 * 			规则ID			
	 * @return String 
	 * 			规则内容
	 */
	@Override
	public String getRuleContentById(int ruleId){
		Map<String, Object> searchMap = new HashMap<String, Object>();
		// 规则ID
		searchMap.put("ruleId", ruleId);
		// 取得规则 内容
		return binOLCM31_Service.getRuleContent(searchMap);
	}
	
	/**
	 * 取得会员初始采集信息
	 * @param memberInfoId
	 * 			会员ID  			
	 * @return Map 
	 * 			initialMemLevel: 初始会员等级
	 * 			initialDate: 初始采集日期
	 */
	@Override
	public Map<String, Object> getMemInitialInfo(int memberInfoId) {
		Map<String, Object> searchMap = new HashMap<String, Object>();
		// 会员ID
		searchMap.put("memberInfoId", memberInfoId);
		return binOLCM31_Service.getMemInitialInfo(searchMap);
	}
	
	/**
	 * 取得会员初始采集信息(会员俱乐部)
	 * @param memberInfoId
	 * 			会员ID  			
	 * @return Map 
	 * 			initialMemLevel: 初始会员等级
	 * 			initialDate: 初始采集日期
	 */
	@Override
	public Map<String, Object> getClubMemInitialInfo(int memberInfoId, int memberClubId) {
		Map<String, Object> searchMap = new HashMap<String, Object>();
		// 会员ID
		searchMap.put("memberInfoId", memberInfoId);
		searchMap.put("memberClubId", memberClubId);
		return binOLCM31_Service.getClubMemInitialInfo(searchMap);
	}
	
	/**
	 * 取得会员初始积分信息
	 * @param memberInfoId
	 * 			会员ID  			
	 * @return Map 
	 * 			initialTime: 初始导入时间
	 */
	@Override
	public Map<String, Object> getMemPointInitInfo(int memberInfoId) {
		Map<String, Object> searchMap = new HashMap<String, Object>();
		// 会员ID
		searchMap.put("memberInfoId", memberInfoId);
		return binOLCM31_Service.getMemPointInitInfo(searchMap);
	}
	
	/**
	 * 取得会员初始积分信息(会员俱乐部)
	 * @param memberInfoId
	 * 			会员ID  			
	 * @return Map 
	 * 			initialTime: 初始导入时间
	 */
	@Override
	public Map<String, Object> getClubMemPointInitInfo(int memberInfoId, int memberClubId) {
		Map<String, Object> searchMap = new HashMap<String, Object>();
		// 会员ID
		searchMap.put("memberInfoId", memberInfoId);
		searchMap.put("memberClubId", memberClubId);
		return binOLCM31_Service.getMemPointInitInfo(searchMap);
	}
	
	/**
	 * 取得单据对应的BA卡号及柜台号信息
	 * 
	 * @param orgId
	 * 			组织信息ID
	 * @param brandId
	 * 			品牌信息ID
	 * @param ticketNo
	 * 			单据号
	 * @param tradeType
	 * 			业务类型
	 * @return Map
	 * 			baCode : BA卡号
	 * 			counterCode : 柜台号
	 * @throws Exception 
	 */
	@Override
	public Map<String, Object> getBaCounterInfo(int orgId, int brandId, String ticketNo, String tradeType) {
		Map<String, Object> searchMap = new HashMap<String, Object>();
		// 组织信息ID
		searchMap.put("organizationInfoId", orgId);
		// 品牌信息ID
		searchMap.put("brandInfoId", brandId);
		// 单据号
		searchMap.put("billCode", ticketNo);
		// 销售单据
		if ("NS".equals(tradeType) || "PX".equals(tradeType)) {
			// 取得单据对应的BA卡号及柜台号信息(销售)
			Map<String, Object> resultMap = binOLCM31_Service.getSaleBaCounterInfo(searchMap);
			if (null == resultMap || resultMap.isEmpty()) {
				// 取得单据对应的BA卡号及柜台号信息(订单)
				return binOLCM31_Service.getESBaCounterInfo(searchMap);
			}
			// 初始数据采集单据
		} else if ("MS".equals(tradeType)) {
			// 取得单据对应的BA卡号及柜台号信息(初始数据采集)
			return binOLCM31_Service.getMSBaCounterInfo(searchMap);
		}
		return null;
	}
	/**
     * 取得会员等级List
     * 
     * @param map
     * @return
     * 		会员等级List
     */
	
    public List<Map<String, Object>> getMemberLevelList(Map<String, Object> map) {
    	List<Map<String, Object>> list = binOLCM31_Service.getMemberLevelList(map);
		return list;
	}
    
    /**
     * 更新会员信息扩展表
     * 
     * @param map
     * 		参数集合
     * 			memberInfoId : 会员信息ID
     * 			totalAmounts : 累计金额
     * 			curBtimes : 化妆次数
     * 			depositAmount : 储蓄卡金额
     */
    @Override
    public void updateMemberExtInfo(Map<String, Object> map) {
    	// 设置共通的更新信息
    	setUpInfo(map);
		// 更新会员信息扩展表
		int result = binOLCM31_Service.updateMemberExtInfo(map);
		if (0 == result) {
			// 插入会员信息扩展表
			binOLCM31_Service.addMemberExtInfo(map);
		}
    }
    
    /**
     * 更新会员俱乐部等级信息扩展表
     * 
     * @param map
     * 		参数集合
     * 			clubLevelId : 等级ID
     * 			memberInfoId : 会员信息ID
     * 			memberClubId : 会员俱乐部ID
     * 			totalAmounts : 累计金额
     * 			upLevelAmount : 化妆次数
     */
    @Override
    public void updateMemberClubExtInfo(Map<String, Object> map) {
    	// 设置共通的更新信息
    	setUpInfo(map);
		// 更新会员俱乐部等级扩展信息
    	int result = binOLCM31_Service.updateMemberClubExtInfo(map);
    	if (0 == result) {
			// 插入会员俱乐部等级扩展信息
			binOLCM31_Service.addMemberClubExtInfo(map);
    	}
    }
    
    /**
     * 更新会员积分信息表
     * 
     * @param map
     * 		参数集合
     * 			memberInfoId : 会员信息ID
     * 			curTotalPoint : 累计积分
     * 			curChangablePoint : 可兑换积分
     */
    @Override
    public void updateMemberPointInfo(Map<String, Object> map) {
    	// 设置共通的更新信息
    	setUpInfo(map);
		// 更新会员积分信息表
		int result = binOLCM31_Service.updateMemberPointInfo(map);
		if (0 == result) {
			// 插入会员积分信息表
			binOLCM31_Service.addMemberPointInfo(map);
		}
    }
    
    /**
     * 更新会员积分信息表(历史记录)
     * 
     * @param map
     * 		参数集合
     * 			memberInfoId : 会员信息ID
     * 			curTotalPoint : 累计积分
     * 			curChangablePoint : 可兑换积分
     */
    @Override
    public void updateHistoryPointInfo(Map<String, Object> map) {
    	// 设置共通的更新信息
    	setUpInfo(map);
		// 更新会员积分信息表
		int result = binOLCM31_Service.updateHistoryPointInfo(map);
		if (0 == result) {
			// 插入会员积分信息表
			binOLCM31_Service.addHistoryPointInfo(map);
		}
    }
    
    /**
     *更新会员前卡积分值
     * 
     * @param map
     * 		参数集合
     * 			memberInfoId : 会员信息ID
     * 			preCardPoint : 前卡积分
     * @return int
     * 			更新件数
     */
    @Override
    public int updatePreCardPoint(Map<String, Object> map) {
    	// 设置共通的更新信息
    	setUpInfo(map);
		// 更新会员前卡积分值
		return binOLCM31_Service.updateMemPreCardPoint(map);
    }
    
    /**
     * 设置共通的更新信息
     * 
     * @param map
     * 		参数集合
     */
    private void setUpInfo(Map<String, Object> map) {
    	// 作成者
		map.put(DroolsConstants.CREATEDBY, "BINOLCM31");
		// 更新者
		map.put(DroolsConstants.UPDATEDBY, "BINOLCM31");
		// 作成程序名
		map.put(DroolsConstants.CREATEPGM, "BINOLCM31");
		// 更新程序名
		map.put(DroolsConstants.UPDATEPGM, "BINOLCM31");
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
    @Override
    public Map<String, Object> getMemberPointInfo(Map<String, Object> map) {
    	// 取得会员当前积分信息
		return binOLCM31_Service.getMemberPointInfo(map);
    }
    
    /**
     * 取得会员当前积分信息(实体)
     * 
     * @param Map
     * 			memberInfoId : 会员信息ID
     * @return PointDTO
     * 			
     */
    @Override
    public PointDTO getMemberPointDTO(Map<String, Object> map) {
    	// 取得会员当前积分信息
		return binOLCM31_Service.getMemberPointDTO(map);
    }
    
    /**
     * 处理会员积分变化信息
     * 
     * @param pointChange
     * 		会员积分变化信息
     * 			
     */
    @Override
    public void execPointChangeInfo(CampBaseDTO campBaseDTO) throws Exception{
    	// 积分信息 DTO
    	PointDTO pointInfo = campBaseDTO.getPointInfo();
    	if (null == pointInfo) {
    		throw new Exception("No point info exception!");
    	}
    	// 会员积分变化主记录
		PointChangeDTO pointChange = pointInfo.getPointChange();
		if (null == pointChange) {
    		throw new Exception("No point change info exception!");
    	}
		// 会员积分变化明细记录
    	List<PointChangeDetailDTO> changeDetailList = pointChange.getChangeDetailList();
    	if (null == changeDetailList || changeDetailList.isEmpty()) {
    		throw new Exception("No point change detail info exception!");
    	}
    	BaseDTO baseDto = new BaseDTO();
		// 作成程序名
		baseDto.setCreatePGM("BINOLCM31");
		// 更新程序名
		baseDto.setUpdatePGM("BINOLCM31");
		// 作成者
		baseDto.setCreatedBy("BINOLCM31");
		// 更新者
		baseDto.setUpdatedBy("BINOLCM31");
		ConvertUtil.convertDTO(pointChange, baseDto, false);
		PointChangeDetailDTO changeDetail = changeDetailList.get(0);
		// 积分类型
		String pointType = changeDetail.getPointType();
		// 积分类型
		String ptmainType = null;
		if (DroolsConstants.POINTTYPE99.equals(pointType) 
				|| DroolsConstants.POINTTYPE0.equals(pointType)) {
			Map<String, Object> extParams = changeDetail.getExtParams();
			if (null != extParams && extParams.containsKey("PTMAINTYPE")) {
				ptmainType = (String) extParams.get("PTMAINTYPE");
			}
			if (null != ptmainType && !"".equals(ptmainType)) {
				changeDetail.setPointType(ptmainType);
			} else {
				changeDetail.setPointType(DroolsConstants.POINTTYPE0);
			}
		}
		// 会员积分变化信息
		Map<String, Object> pointReCalcInfo = null;
		// 积分维护：差值/总值
		if (campBaseDTO.getReCalcFlg() == DroolsConstants.RECALCFLG_1 ) {
			if (DroolsConstants.POINTTYPE99.equals(pointType) 
					|| DroolsConstants.POINTTYPE0.equals(pointType)) {
				// 取得会员积分变化信息(积分维护)
				pointReCalcInfo = binOLCM31_Service.getMSPointChangeInfo(pointChange);
				// 积分维护：总值
				if (null != pointReCalcInfo && !pointReCalcInfo.isEmpty() && 
						DroolsConstants.POINTTYPE0.equals(pointType)) {
					// 明细中的积分值
					double detailPoint = 0;
					Object detailPointObj = pointReCalcInfo.get("point");
					if (null != detailPointObj) {
						detailPoint = Double.parseDouble(detailPointObj.toString());
					}
					// 本次积分维护计算产生的差值
					double point = pointChange.getPoint();
					// 本次积分维护与第一次计算的值不一致
					if (point != detailPoint) {
						// 积分差值
						double subPoint = DoubleUtil.sub(point, detailPoint);
						changeDetail.setPoint(detailPoint);
						// 新增一条差分明细
						PointChangeDetailDTO subChangeDetailDTO = new PointChangeDetailDTO();
						// 积分值
						subChangeDetailDTO.setPoint(subPoint);
						// 积分类型:积分维护
						if (null != ptmainType && !"".equals(ptmainType)) {
							subChangeDetailDTO.setPointType(ptmainType);
						} else {
							subChangeDetailDTO.setPointType(DroolsConstants.POINTTYPE0);
						}
						// 系统自动维护
						subChangeDetailDTO.setReason(DroolsConstants.REASON3);
						changeDetailList.add(subChangeDetailDTO);
					}
				}
			} else {
				// 取得会员积分变化信息
				pointReCalcInfo = binOLCM31_Service.getPointChangeInfo(pointChange);
			}
		}
		if (null == pointReCalcInfo || pointReCalcInfo.isEmpty()) {
			// 柜台ID
			Integer orgId = pointChange.getOrganizationId();
			if (null == orgId || 0 == orgId) {
				// 取得会员所属柜台信息
				Map<String, Object> orgInfo = binOLCM31_Service.getMemOrgInfo(campBaseDTO);
				if (null != orgInfo && !orgInfo.isEmpty()) {
					if (null != orgInfo.get("organizationId")) {
						pointChange.setOrganizationId(Integer.parseInt(orgInfo.get("organizationId").toString()));
					}
				}
			}
			// 插入会员积分变化主表
			binOLCM31_Service.addPointChange(pointChange);
		} else {
			// 会员积分变化ID
			int pointChangeId = Integer.parseInt(pointReCalcInfo.get("pointChangeId").toString());
			// 重算次数
			int reCalcCount = Integer.parseInt(pointReCalcInfo.get("reCalcCount").toString());
			pointChange.setPointChangeId(pointChangeId);
			pointChange.setReCalcCount(reCalcCount);
			// 更新会员积分变化主表
			binOLCM31_Service.updatePointChange(pointChange);
			Map<String, Object> delMap = new HashMap<String, Object>();
			delMap.put("pointChangeId", pointChangeId);
			// 删除会员积分变化明细表
			binOLCM31_Service.delPointChangeDetail(delMap);
		}
		for (PointChangeDetailDTO changeDetailDTO : changeDetailList) {
			ConvertUtil.convertDTO(changeDetailDTO, baseDto, false);
			// 会员积分变化主ID
			changeDetailDTO.setPointChangeId(pointChange.getPointChangeId());
			// 插入会员积分变化明细表
			binOLCM31_Service.addPointChangeDetail(changeDetailDTO);
		}
	}
    
    /**
     * 判断会员是否正在进行重算
     * 
     * @param map
     * 		参数集合
     * 			memberInfoId : 会员信息ID
     * 			brandCode : 品牌代码
     * 			orgCode : 组织代码
     * @return true : 正在重算中   false : 非重算中
     * @throws Exception 
     */
    @Override
    public boolean isReCalcExec(Map<String, Object> map) throws Exception {
    	// 查询条件
		DBObject query = new BasicDBObject();
		String memberInfoId = null;
		Object memberIdObj = map.get("memberInfoId");
		if (null != memberIdObj) {
			memberInfoId = String.valueOf(memberIdObj);
		}
		// 会员ID
		query.put("BIN_MemberInfoID", memberInfoId);
		// 品牌代码
		query.put("BrandCode", map.get("brandCode"));
		// 组织代码
		query.put("OrgCode", map.get("orgCode"));
		int count = MongoDB.findCount("MGO_MemReCalcRecord", query);
		if (count < 1) {
			return false;
		}
		return true;
    }
    

    /**
     * 判断会员是否正在进行重算
     * 
     * @param map
     * 		参数集合
     * 			memberInfoId : 会员信息ID
     * 			brandCode : 品牌代码
     * 			orgCode : 组织代码
     * @return true : 正在重算中   false : 非重算中
     * @throws Exception 
     */
    @Override
    public boolean isMemReCalcExec(Map<String, Object> map) throws Exception {
    	if (null == map.get("reCalcType")) {
    		map.put("reCalcType", "0");
    	}
    	if (0 == binOLCM31_Service.getReCalcCount(map)) {
    		return isReCalcExec(map);
    	}
    	return true;
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
	 * 			channelId : 渠道ID
	 * 			cityId : 柜台所属城市ID
	 */
    @Override
    public Map<String, Object> getComCounterInfo(Map<String, Object> map){
    	// 取得柜台信息
    	return binOLCM31_Service.getComCounterInfo(map);
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
    @Override
    public Map<String, Object> getComEmployeeInfo(Map<String, Object> map){
    	// 取得员工信息
    	return binOLCM31_Service.getComEmployeeInfo(map);
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
	@Override
	public String getMemCard(Map<String, Object> map) {
		// 取得会员卡号
		return binOLCM31_Service.getMemCard(map);
	}
	
	/**
	 * 验证会员卡号是否为当前卡
	 * 
	 * @param memberInfoId
	 * 			会员信息ID
	 * @param memCard
	 * 			会员卡号
	 * @return 判断结果
	 * 			true: 当前卡   false: 非当前卡
	 * 
	 */
	@Override
	public boolean isCurCard(int memberInfoId, String memCode) {
		Map<String, Object> searchMap = new HashMap<String, Object>();
		searchMap.put("memberInfoId", memberInfoId);
		// 取得会员卡号
		String curCard = getMemCard(searchMap);
		if (null != memCode && null != curCard && 
				(memCode.toUpperCase().trim()).equals(curCard.toUpperCase().trim())) {
			return true;
		}
		return false;
	}
	
	/**
	 * 验证会员是否只有一张卡
	 * 
	 * @param memberInfoId
	 * 			会员信息ID
	 * @return 判断结果
	 * 			true: 只有一张卡   false: 多张卡
	 * 
	 */
	@Override
	public boolean isSingleCard(int memberInfoId) {
		Map<String, Object> searchMap = new HashMap<String, Object>();
		// 会员信息ID
		searchMap.put("memberInfoId", memberInfoId);
		// 取得会员卡件数
		int count = binOLCM31_Service.getMemCardCount(searchMap);
		if (1 == count) {
			return true;
		}
		return false;
	}
	
	/**
	 * 重新计算前卡积分
	 * 
	 * @param memberInfoId
	 * 			会员信息ID
	 * @param memCode
	 * 			会员卡号
	 * @param curPoint
	 * 			当前总积分
	 * @param memberClubId
	 * 			会员俱乐部ID
	 * @return double
	 * 			重算后的前卡积分
	 * 
	 */
	@Override
	public double recPreCardPoint(int memberInfoId, String memCode, double curPoint, int memberClubId) {
		if (curPoint <= 0) {
			return 0;
		}
		Map<String, Object> searchMap = new HashMap<String, Object>();
		// 会员信息ID
		searchMap.put("memberInfoId", memberInfoId);
		// 会员卡号
		searchMap.put("memCode", memCode);
		if (0 != memberClubId) {
			searchMap.put("memberClubId", memberClubId);
		}
		// 取得会员当前卡的积分值之和
		Map<String, Object> curPointInfo = binOLCM31_Service.getCurPointInfo(searchMap);
		// 当前卡的积分之和
		double curCardPoint = 0;
		if (null != curPointInfo && 
				null != curPointInfo.get("curCardPoint")) {
			// 当前卡的积分之和
			curCardPoint = Double.parseDouble(curPointInfo.get("curCardPoint").toString());
		}
		// 前卡积分
		double preCardPoint = DoubleUtil.sub(curPoint, curCardPoint);
		preCardPoint = (preCardPoint >= 0)? preCardPoint : 0;
		return preCardPoint;
	}
	
	/**
	 * 封装积分消息体
	 * 
	 * @param msg
	 * 			积分消息内容
	 * @return String
	 * 			封装好的积分消息体
	 */
	@Override
	public String getPointData(String msg){
		if (!CherryChecker.isNullOrEmpty(msg)) {
			StringBuffer buffer = new StringBuffer();
			// 版本号
			buffer.append("[Version]")
				.append(DroolsConstants.SPLIT1)
				.append("AMQ.008.001")
				.append(DroolsConstants.LINE_BREAK)
				// 命令类型
				.append("[Type]")
				.append(DroolsConstants.SPLIT1)
				.append("0003")
				.append(DroolsConstants.LINE_BREAK)
				// 数据类型
				.append("[DataType]")
				.append(DroolsConstants.SPLIT1)
				.append("application/json")
				.append(DroolsConstants.LINE_BREAK)
				// 消息内容
				.append("[DataLine]")
				.append(DroolsConstants.SPLIT1)
				.append(msg)
				.append(DroolsConstants.LINE_BREAK)
				.append(DroolsConstants.MQ_END);
			return buffer.toString();
		}
		return null;
	}
	
	/**
     * 取得规则名称列表
     * 
     * @param map
     * 			brandInfoId : 品牌ID
     * 			organizationInfoId : 组织ID
     * 			campaignType : 规则类型
     * @return
     * 		规则名称列表
     */
	@Override
    public List<Map<String, Object>> getCampRuleNameList(Map<String, Object> map) {
    	// 取得规则名称列表
    	return binOLCM31_Service.getCampRuleNameList(map);
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
	@Override
    public Map<String, Object> getTotalPointInfo(Map<String, Object> map) {
    	// 取得规则名称列表
    	return binOLCM31_Service.getComTotalPointInfo(map);
    }
	
	/**
	 * 设置会员属性
	 * 
	 * @param c
	 * 			会员信息DTO
	 * 
	 */
	@Override
    public void execMemberInfo(CampBaseDTO c) {
    	Map<String, Object> searchMap = new HashMap<String, Object>();
    	// 会员信息ID
    	searchMap.put("memberInfoId", c.getMemberInfoId());
    	// 取得柜台信息
    	Map<String, Object> resultMap = null;
    	if (0 != c.getMemberClubId()) {
    		searchMap.put("memberClubId", c.getMemberClubId());
    		resultMap = binOLCM31_Service.getComClubCouBelongInfo(searchMap);
    		String joinDate = null;
    		if (null != resultMap && !resultMap.isEmpty()) {
    			joinDate = (String) resultMap.get("joinDate");
    		}
    		c.setJoinDate(joinDate);
    	}
    	if (null == resultMap || resultMap.isEmpty()) {
	    	// 取得柜台信息
	    	resultMap = binOLCM31_Service.getComCouBelongInfo(searchMap);
    	}
    	if (null != resultMap && !resultMap.isEmpty()) {
			// 部门ID(开卡柜台)
			Object organizationId = resultMap.get("organizationId");
			if (null != organizationId) {
				c.setBelDepartId(Integer.parseInt(organizationId.toString()));
			}
			// 部门名称(开卡柜台)
			c.setBelDepartName((String) resultMap.get("departName"));
			// 柜台代号
			c.setBelCounterCode((String) resultMap.get("counterCode"));
			// 渠道ID(开卡柜台)
			Object channelId = resultMap.get("channelId");
			if (null != channelId) {
				c.setBelChannelId(Integer.parseInt(channelId.toString()));
			}
			// 城市ID(开卡柜台)
			Object cityId = resultMap.get("cityId");
			if (null != cityId) {
				c.setBelCounterCityId(Integer.parseInt(cityId.toString()));
			}
		}
    	if (0 == c.getMemberClubId()) {
	    	// 推荐会员信息
	    	if (c.getReferrerId() != 0) {
	    		Map<String, Object> map = new HashMap<String, Object>();
	    		map.put("memberInfoId", c.getReferrerId());
	    		// 推荐会员卡号
	    		String referrerCode = binOLCM31_Service.getMemCard(map);
	    		c.setReferrerCode(referrerCode);
	    	}
    	} else {
    		c.setReferrerId(0);
    		Map<String, Object> mzMap = new HashMap<String, Object>();
    		mzMap.put("memberInfoId", c.getMemberInfoId());
    		mzMap.put("mzClubId", c.getMemberClubId());
    		// 查询会员俱乐部扩展信息
    		List<Map<String, Object>> clubExtList = binOLCM31_Service.getClubExtList(mzMap);
    		if (null != clubExtList && !clubExtList.isEmpty()) {
    			Object referrerId = clubExtList.get(0).get("referrerId");
    			if (null != referrerId) {
	    			c.setReferrerId(Integer.parseInt(String.valueOf(referrerId)));
	    			mzMap.put("memberInfoId", c.getReferrerId());
	    			// 推荐会员卡号
		    		String referrerCode = binOLCM31_Service.getMemCard(mzMap);
		    		c.setReferrerCode(referrerCode);
    			}
    		}
    	}
    }
	
	/**
	 * 取得积分MQ消息体(实时)
	 * 
	 * @param campBaseDTO
	 * @throws Exception 
	 */
	@Override
	public MQInfoDTO getPointMQMessage(CampBaseDTO campBaseDTO) throws Exception{
		// 假登录返回null
		if(1 == campBaseDTO.getMemRegFlg()) {
			return null;
		}
		// 积分信息
		PointDTO pointDTO = campBaseDTO.getPointInfo();
		if (null != pointDTO) {
			Map<String, Object> cardMap = new HashMap<String, Object>();
			// 会员信息ID
			cardMap.put("memberInfoId", campBaseDTO.getMemberInfoId());
			// 取得会员卡号
			String memCode = getMemCard(cardMap);
			if (null == memCode || "".equals(memCode)) {
				// 会员信息无记录
				String errMsg = DroolsMessageUtil.getMessage(DroolsMessageUtil.EDR00007, 
						new String[]{String.valueOf(campBaseDTO.getMemberInfoId())});
				throw new CherryDRException(errMsg);
			}
			Map<String, Object> dataLineMap = new HashMap<String, Object>();
			// 主数据行
			Map<String, Object> mainDataMap = new HashMap<String, Object>();
			// 单据主记录的明细行
			Map<String, Object> tradeDataMap = new HashMap<String, Object>();
			// 单据主记录的明细行列表
			List<Map<String, Object>> tradeDataList = new ArrayList<Map<String, Object>>();
			// 单据明细记录的明细行列表
			List<Map<String, Object>> detailDataList = new ArrayList<Map<String, Object>>();
			// 品牌代码
			mainDataMap.put("BrandCode", campBaseDTO.getBrandCode());
			if (0 != campBaseDTO.getMemberClubId()) {
				// 会员俱乐部代号
				mainDataMap.put("ClubCode", campBaseDTO.getClubCode());
			}
			// 单据号
			String ticketNumber = binOLCM03_BL.getTicketNumber(String.valueOf(campBaseDTO.getOrganizationInfoId()),
					String.valueOf(campBaseDTO.getBrandInfoId()), DroolsConstants.CREATED_NAME, DroolsConstants.MQ_BILLTYPE_MP);
			// 单据号
			mainDataMap.put("TradeNoIF", ticketNumber);
			// 业务类型
			mainDataMap.put("TradeType", DroolsConstants.MQ_BILLTYPE_MP);
			// 子类型
			mainDataMap.put("SubType", "");
			// 会员卡号
			mainDataMap.put("Membercode", memCode);
			// 总积分
			mainDataMap.put("TotalPoint", DoubleUtil.douToStr(pointDTO.getCurTotalPoint()));
			// 累计兑换积分
			mainDataMap.put("TotalChanged", DoubleUtil.douToStr(pointDTO.getCurTotalChanged()));
			// 可兑换积分
			mainDataMap.put("ChangablePoint", DoubleUtil.douToStr(pointDTO.getCurChangablePoint()));
			// 冻结积分
			mainDataMap.put("FreezePoint", DoubleUtil.douToStr(pointDTO.getFreezePoint()));
			// 累计失效积分 
			mainDataMap.put("TotalDisablePoint", DoubleUtil.douToStr(pointDTO.getTotalDisablePoint()));
			// 本次将失效积分 
			mainDataMap.put("CurDisablePoint", DoubleUtil.douToStr(pointDTO.getCurDisablePoint()));
			String preDisableDate = "";
			if (!CherryChecker.isNullOrEmpty(pointDTO.getPreDisableDate())) {
				preDisableDate = DateUtil.coverTime2YMD(pointDTO.getPreDisableDate(), DateUtil.DATE_PATTERN);
			}
			// 上回积分失效日期  
			mainDataMap.put("PreDisableDate", preDisableDate);
			String curDealDate = "";
			if (!CherryChecker.isNullOrEmpty(pointDTO.getCurDealDate())) {
				curDealDate = DateUtil.coverTime2YMD(pointDTO.getCurDealDate(), DateUtil.DATE_PATTERN);
			}
			// 本次积分失效日期  
			mainDataMap.put("CurDealDate", curDealDate);
			// 前卡积分  
			mainDataMap.put("PreCardPoint", DoubleUtil.douToStr(pointDTO.getPreCardPoint()));
			// 该会员的计算时间   
			mainDataMap.put("Caltime", campBaseDTO.getCalcDate());
			// 单据的积分情况
			PointChangeDTO pointChange = pointDTO.getPointChange();
			if (null != pointChange && null != pointChange.getChangeDetailList()) {
				// 会员号
				tradeDataMap.put("Membercode", pointChange.getMemCode());
				// 机器号
				tradeDataMap.put("MachineCode", ConvertUtil.getString(pointChange.getMachineCode()));
				// 单据号
				tradeDataMap.put("Billid", pointChange.getTradeNoIF());
				// 业务类型
				tradeDataMap.put("BizType", pointChange.getTradeType());
				// 积分变化日期
				String changeTime = pointChange.getChangeDate();
				// 出入库日期
				String tradeDate = "";
				// 出入库时间
				String tradeTime = "";
				if (!CherryChecker.isNullOrEmpty(changeTime) && changeTime.length() >= 19) {
					tradeDate = changeTime.substring(0, 10);
					tradeTime = changeTime.substring(11, 19);
				}
				// 出入库日期
				tradeDataMap.put("TradeDate", tradeDate);
				// 出入库时间
				tradeDataMap.put("TradeTime", tradeTime);
				// 柜台号
				tradeDataMap.put("Countercode", ConvertUtil.getString(campBaseDTO.getCounterCode()));
				// 变化的积分
				tradeDataMap.put("Points", DoubleUtil.douToStr(pointChange.getPoint()));
				// 总数量
				tradeDataMap.put("TotalQuantity", String.valueOf(pointChange.getQuantity()));
				// 总金额
				tradeDataMap.put("TotalAmount", String.valueOf(pointChange.getAmount()));
				// 修改回数
				tradeDataMap.put("ModifyCount", String.valueOf(pointChange.getModifiedTimes()));
				// 重算次数
				tradeDataMap.put("ReCalcCount", String.valueOf(pointChange.getReCalcCount()));
				// 有效区分
				tradeDataMap.put("ValidFlag", "1");
				tradeDataList.add(tradeDataMap);
				String tradeType = pointChange.getTradeType();
				if ("PX".equals(tradeType)) {
					tradeType = "NS";
				}
				for (PointChangeDetailDTO changeDetail : pointChange.getChangeDetailList()) {
					// 单据明细记录的明细行
					Map<String, Object> detailDataMap = new HashMap<String, Object>();
					// 单据号
					detailDataMap.put("Billid", pointChange.getTradeNoIF());
					// 业务类型
					detailDataMap.put("TradeType", tradeType);
					// Ba卡号
					detailDataMap.put("Bacode", ConvertUtil.getString(campBaseDTO.getEmployeeCode()));
					// 产品条码
					detailDataMap.put("Barcode", ConvertUtil.getString(changeDetail.getBarCode()));
					// 厂商编码
					detailDataMap.put("Unitcode", ConvertUtil.getString(changeDetail.getUnitCode()));
					// 销售日期
					detailDataMap.put("TradeDate", tradeDate);
					// 销售时间
					detailDataMap.put("TradeTime", tradeTime);
					// 销售类型
					String saleType = ConvertUtil.getString(changeDetail.getSaleType());
					if (!"".equals(saleType)) {
						saleType = saleType.toLowerCase();
					}
					detailDataMap.put("SaleType", saleType);
					// 价格
					detailDataMap.put("Price", String.valueOf(changeDetail.getPrice()));
					// 数量
					detailDataMap.put("Quantity", String.valueOf(changeDetail.getQuantity()));
					// 积分值 
					detailDataMap.put("Point", DoubleUtil.douToStr(changeDetail.getPoint()));
					// 积分类型
					detailDataMap.put("PointType", ConvertUtil.getString(changeDetail.getPointType()));
					// 理由
					detailDataMap.put("Reason", ConvertUtil.getString(changeDetail.getReason()));
					// 积分有效期(月) 
					detailDataMap.put("ValidMonths", String.valueOf(changeDetail.getValidMonths()));
					detailDataList.add(detailDataMap);
				}
			}
			dataLineMap.put("MainData", mainDataMap);
			dataLineMap.put("TradeDataList", tradeDataList);
			dataLineMap.put("DetailDataList", detailDataList);
			// 消息体
			String data = getPointData(CherryUtil.map2Json(dataLineMap));
			// MQ收发日志DTO
			MQInfoDTO mqInfoDTO = new MQInfoDTO();
			// 所属组织
			mqInfoDTO.setOrganizationInfoId(campBaseDTO.getOrganizationInfoId());
			// 所属品牌
			mqInfoDTO.setBrandInfoId(campBaseDTO.getBrandInfoId());
			// 单据类型
			mqInfoDTO.setBillType(DroolsConstants.MQ_BILLTYPE_MP);
			// 单据号
			mqInfoDTO.setBillCode(ticketNumber);
			// 消息发送接收标志位
			mqInfoDTO.setReceiveFlag(DroolsConstants.MQ_RECEIVEFLAG_0);
			// 消息体
			mqInfoDTO.setData(data);
			// 业务流水
			DBObject dbObject = new BasicDBObject();
			//组织代号
			dbObject.put("OrgCode", campBaseDTO.getOrgCode());
			// 品牌代码，即品牌简称
			dbObject.put("BrandCode", campBaseDTO.getBrandCode());
			// 业务类型
			dbObject.put("TradeType", DroolsConstants.MQ_BILLTYPE_MP);
			// 单据号
			dbObject.put("TradeNoIF", ticketNumber);
			// 修改次数
			dbObject.put("ModifyCounts", DroolsConstants.DEF_MODIFYCOUNTS);
			// 业务主体
		    dbObject.put("TradeEntity", DroolsConstants.TRADEENTITY_0);
		    // 业务主体代码
		    dbObject.put("TradeEntityCode", memCode);
		    // 业务主体名称
		    dbObject.put("TradeEntityName", campBaseDTO.getMemName());
		    // 发生时间
		    dbObject.put("OccurTime", campBaseDTO.getCalcDate());
		    // 事件内容
		    dbObject.put("Content", data);
		    mqInfoDTO.setDbObject(dbObject);
			return mqInfoDTO;
		}
		return null;
	}
	
	/**
	 * 取得积分MQ消息体(历史积分)
	 * 
	 * @param campBaseDTO
	 * @throws Exception 
	 */
	@Override
	public MQInfoDTO getPointMQMessageHist(CampBaseDTO campBaseDTO) throws Exception{
		// 积分信息
		PointDTO pointDTO = campBaseDTO.getPointInfo();
		if (null != pointDTO) {
			// 单据的积分情况
			PointChangeDTO pointChange = pointDTO.getPointChange();
			if (null != pointChange && null != pointChange.getChangeDetailList()) {
				Map<String, Object> cardMap = new HashMap<String, Object>();
				// 会员信息ID
				cardMap.put("memberInfoId", campBaseDTO.getMemberInfoId());
				// 取得会员卡号
				String memCode = getMemCard(cardMap);
				if (null == memCode || "".equals(memCode)) {
					// 会员信息无记录
					String errMsg = DroolsMessageUtil.getMessage(DroolsMessageUtil.EDR00007, 
							new String[]{String.valueOf(campBaseDTO.getMemberInfoId())});
					throw new CherryDRException(errMsg);
				}
				Map<String, Object> dataLineMap = new HashMap<String, Object>();
				// 主数据行
				Map<String, Object> mainDataMap = new HashMap<String, Object>();
				// 单据主记录的明细行
				Map<String, Object> tradeDataMap = new HashMap<String, Object>();
				// 单据主记录的明细行列表
				List<Map<String, Object>> tradeDataList = new ArrayList<Map<String, Object>>();
				// 单据明细记录的明细行列表
				List<Map<String, Object>> detailDataList = new ArrayList<Map<String, Object>>();
				// 品牌代码
				mainDataMap.put("BrandCode", campBaseDTO.getBrandCode());
				if (0 != campBaseDTO.getMemberClubId()) {
					// 会员俱乐部代号
					mainDataMap.put("ClubCode", campBaseDTO.getClubCode());
				}
				// 单据号
				String ticketNumber = binOLCM03_BL.getTicketNumber(String.valueOf(campBaseDTO.getOrganizationInfoId()),
						String.valueOf(campBaseDTO.getBrandInfoId()), DroolsConstants.CREATED_NAME, DroolsConstants.MQ_BILLTYPE_MP);
				// 单据号
				mainDataMap.put("TradeNoIF", ticketNumber);
				// 业务类型
				mainDataMap.put("TradeType", DroolsConstants.MQ_BILLTYPE_MP);
				// 子类型
				mainDataMap.put("SubType", "");
				// 会员卡号
				mainDataMap.put("Membercode", memCode);
				// 总积分
				mainDataMap.put("TotalPoint", DoubleUtil.douToStr(pointDTO.getCurTotalPoint()));
				// 累计兑换积分
				mainDataMap.put("TotalChanged", DoubleUtil.douToStr(pointDTO.getCurTotalChanged()));
				// 可兑换积分
				mainDataMap.put("ChangablePoint", DoubleUtil.douToStr(pointDTO.getCurChangablePoint()));
				// 冻结积分
				mainDataMap.put("FreezePoint", DoubleUtil.douToStr(pointDTO.getFreezePoint()));
				// 累计失效积分 
				mainDataMap.put("TotalDisablePoint", DoubleUtil.douToStr(pointDTO.getTotalDisablePoint()));
				// 本次将失效积分 
				mainDataMap.put("CurDisablePoint", DoubleUtil.douToStr(pointDTO.getCurDisablePoint()));
				String preDisableDate = "";
				if (!CherryChecker.isNullOrEmpty(pointDTO.getPreDisableDate())) {
					preDisableDate = DateUtil.coverTime2YMD(pointDTO.getPreDisableDate(), DateUtil.DATE_PATTERN);
				}
				// 上回积分失效日期  
				mainDataMap.put("PreDisableDate", preDisableDate);
				String curDealDate = "";
				if (!CherryChecker.isNullOrEmpty(pointDTO.getCurDealDate())) {
					curDealDate = DateUtil.coverTime2YMD(pointDTO.getCurDealDate(), DateUtil.DATE_PATTERN);
				}
				// 本次积分失效日期  
				mainDataMap.put("CurDealDate", curDealDate);
				// 前卡积分  
				mainDataMap.put("PreCardPoint", DoubleUtil.douToStr(pointDTO.getPreCardPoint()));
				// 该会员的计算时间   
				mainDataMap.put("Caltime", campBaseDTO.getCalcDate());
				// 会员号
				tradeDataMap.put("Membercode", pointChange.getMemCode());
				// 机器号
				tradeDataMap.put("MachineCode", ConvertUtil.getString(pointChange.getMachineCode()));
				// 单据号
				tradeDataMap.put("Billid", pointChange.getTradeNoIF());
				// 业务类型
				tradeDataMap.put("BizType", pointChange.getTradeType());
				// 积分变化日期
				String changeTime = pointChange.getChangeDate();
				// 出入库日期
				String tradeDate = "";
				// 出入库时间
				String tradeTime = "";
				if (!CherryChecker.isNullOrEmpty(changeTime) && changeTime.length() >= 19) {
					tradeDate = changeTime.substring(0, 10);
					tradeTime = changeTime.substring(11, 19);
				}
				// 出入库日期
				tradeDataMap.put("TradeDate", tradeDate);
				// 出入库时间
				tradeDataMap.put("TradeTime", tradeTime);
				// 柜台号
				tradeDataMap.put("Countercode", ConvertUtil.getString(campBaseDTO.getCounterCode()));
				// 变化的积分
				tradeDataMap.put("Points", DoubleUtil.douToStr(pointChange.getPoint()));
				// 总数量
				tradeDataMap.put("TotalQuantity", String.valueOf(pointChange.getQuantity()));
				// 总金额
				tradeDataMap.put("TotalAmount", String.valueOf(pointChange.getAmount()));
				// 修改回数
				tradeDataMap.put("ModifyCount", String.valueOf(pointChange.getModifiedTimes()));
				// 重算次数
				tradeDataMap.put("ReCalcCount", String.valueOf(pointChange.getReCalcCount()));
				// 有效区分
				tradeDataMap.put("ValidFlag", pointChange.getValidFlag());
				tradeDataList.add(tradeDataMap);
				if ("1".equals(pointChange.getValidFlag())) {
					String tradeType = pointChange.getTradeType();
					if ("PX".equals(tradeType)) {
						tradeType = "NS";
					}
					for (PointChangeDetailDTO changeDetail : pointChange.getChangeDetailList()) {
						// 单据明细记录的明细行
						Map<String, Object> detailDataMap = new HashMap<String, Object>();
						// 单据号
						detailDataMap.put("Billid", pointChange.getTradeNoIF());
						// 业务类型
						detailDataMap.put("TradeType", tradeType);
						// Ba卡号
						detailDataMap.put("Bacode", ConvertUtil.getString(campBaseDTO.getEmployeeCode()));
						// 产品条码
						detailDataMap.put("Barcode", ConvertUtil.getString(changeDetail.getBarCode()));
						// 厂商编码
						detailDataMap.put("Unitcode", ConvertUtil.getString(changeDetail.getUnitCode()));
						// 销售日期
						detailDataMap.put("TradeDate", tradeDate);
						// 销售时间
						detailDataMap.put("TradeTime", tradeTime);
						// 销售类型
						String saleType = ConvertUtil.getString(changeDetail.getSaleType());
						if (!"".equals(saleType)) {
							saleType = saleType.toLowerCase();
						}
						detailDataMap.put("SaleType", saleType);
						// 价格
						detailDataMap.put("Price", String.valueOf(changeDetail.getPrice()));
						// 数量
						detailDataMap.put("Quantity", String.valueOf(changeDetail.getQuantity()));
						// 积分值 
						detailDataMap.put("Point", DoubleUtil.douToStr(changeDetail.getPoint()));
						// 积分类型
						detailDataMap.put("PointType", ConvertUtil.getString(changeDetail.getPointType()));
						// 理由
						detailDataMap.put("Reason", ConvertUtil.getString(changeDetail.getReason()));
						// 积分有效期(月) 
						detailDataMap.put("ValidMonths", String.valueOf(changeDetail.getValidMonths()));
						detailDataList.add(detailDataMap);
					}
				}
				dataLineMap.put("MainData", mainDataMap);
				dataLineMap.put("TradeDataList", tradeDataList);
				dataLineMap.put("DetailDataList", detailDataList);
				// 消息体
				String data = getPointData(CherryUtil.map2Json(dataLineMap));
				// MQ收发日志DTO
				MQInfoDTO mqInfoDTO = new MQInfoDTO();
				// 所属组织
				mqInfoDTO.setOrganizationInfoId(campBaseDTO.getOrganizationInfoId());
				// 所属品牌
				mqInfoDTO.setBrandInfoId(campBaseDTO.getBrandInfoId());
				// 单据类型
				mqInfoDTO.setBillType(DroolsConstants.MQ_BILLTYPE_MP);
				// 单据号
				mqInfoDTO.setBillCode(ticketNumber);
				// 消息发送接收标志位
				mqInfoDTO.setReceiveFlag(DroolsConstants.MQ_RECEIVEFLAG_0);
				// 消息体
				mqInfoDTO.setData(data);
				// 业务流水
				DBObject dbObject = new BasicDBObject();
				//组织代号
				dbObject.put("OrgCode", campBaseDTO.getOrgCode());
				// 品牌代码，即品牌简称
				dbObject.put("BrandCode", campBaseDTO.getBrandCode());
				// 业务类型
				dbObject.put("TradeType", DroolsConstants.MQ_BILLTYPE_MP);
				// 单据号
				dbObject.put("TradeNoIF", ticketNumber);
				// 修改次数
				dbObject.put("ModifyCounts", DroolsConstants.DEF_MODIFYCOUNTS);
				// 业务主体
			    dbObject.put("TradeEntity", DroolsConstants.TRADEENTITY_0);
			    // 业务主体代码
			    dbObject.put("TradeEntityCode", memCode);
			    // 业务主体名称
			    dbObject.put("TradeEntityName", campBaseDTO.getMemName());
			    // 发生时间
			    dbObject.put("OccurTime", campBaseDTO.getCalcDate());
			    // 事件内容
			    dbObject.put("Content", data);
			    mqInfoDTO.setDbObject(dbObject);
				return mqInfoDTO;
			}
		}
		return null;
	}
	
	/**
     * 发送会员资料MQ消息
     * 
     * @param map 会员信息
     * @throws Exception 
     */
	@Override
	public void sendMEMQMsg(Map<String, Object> map) throws Exception {
		// 设定MQ消息DTO
		MQInfoDTO mqInfoDTO = new MQInfoDTO();
		// 品牌代码
		mqInfoDTO.setBrandCode((String) map.get("brandCode"));
		// 组织代码
		mqInfoDTO.setOrgCode((String) map.get("orgCode"));
		// 组织ID
		int organizationInfoId = Integer.parseInt(map.get(
				"organizationInfoId").toString());
		mqInfoDTO.setOrganizationInfoId(organizationInfoId);
		// 品牌ID
		int brandInfoId = Integer.parseInt(map.get("brandInfoId")
				.toString());
		mqInfoDTO.setBrandInfoId(brandInfoId);
		String billType = MessageConstants.MESSAGE_TYPE_ME;
		// 单据号
		String billCode = binOLCM03_BL.getTicketNumber(organizationInfoId, brandInfoId, "", billType);
		// 业务类型
		mqInfoDTO.setBillType(billType);
		// 单据号
		mqInfoDTO.setBillCode(billCode);
		// 消息发送队列名
		mqInfoDTO.setMsgQueueName(CherryConstants.CHERRYTOPOSMEMBER);

		// 设定消息内容
		Map<String, Object> msgDataMap = new HashMap<String, Object>();
		// 设定消息版本号
		msgDataMap.put("Version", MessageConstants.MESSAGE_VERSION_ME);
		// 设定消息数据类型
		msgDataMap.put("DataType", MessageConstants.DATATYPE_APPLICATION_JSON);
		// 设定消息的数据行
		Map<String, Object> dataLine = new HashMap<String, Object>();
		// 消息的主数据行
		Map<String, Object> mainData = new HashMap<String, Object>();
		// 品牌代码
		mainData.put("BrandCode", map.get("brandCode"));
		// 业务类型
		mainData.put("TradeType", billType);
		// 子类型
		String subType = (String)map.get("subType");
		if(subType == null || "".equals(subType)) {
			subType = "0";
		}
		mainData.put("SubType", subType);
		// 单据号
		mainData.put("TradeNoIF", billCode);
		// 业务时间
		mainData.put("TradeDate", binOLCM31_Service.getForwardSYSDate());
		dataLine.put("MainData", mainData);
		// 消息的明细数据行
		List<Map<String, Object>> detailDataList = new ArrayList<Map<String, Object>>();
		Map<String, Object> detailDataMap = new HashMap<String, Object>();
		// 会员号
		detailDataMap.put("MemberCode", ConvertUtil.getString(map.get("memCode")));
		// 会员名字 
		detailDataMap.put("MemName", ConvertUtil.getString(map.get("memName")));
		// 会员电话 
		detailDataMap.put("MemPhone", ConvertUtil.getString(map.get("telephone")));
		// 会员手机号
		detailDataMap.put("MemMobile", ConvertUtil.getString(map.get("mobilePhone")));
		// 会员性别
		detailDataMap.put("MemSex", ConvertUtil.getString(map.get("gender")));
		// 会员省份
		detailDataMap.put("MemProvince", ConvertUtil.getString(map.get("memProvince")));
		// 会员城市
		detailDataMap.put("MemCity", ConvertUtil.getString(map.get("memCity")));
		// 会员区县
		detailDataMap.put("MemCounty", ConvertUtil.getString(map.get("memCounty")));
		// 会员地址
		detailDataMap.put("MemAddress", ConvertUtil.getString(map.get("address")));
		// 会员邮编 
		detailDataMap.put("MemPostcode", ConvertUtil.getString(map.get("postcode")));
		// 会员生日
		detailDataMap.put("MemBirthday", ConvertUtil.getString(map.get("birth")));
		// 会员年龄获取方式 
		detailDataMap.put("MemAgeGetMethod", ConvertUtil.getString(map.get("memAgeGetMethod")));
		// 会员邮箱 
		detailDataMap.put("MemMail", ConvertUtil.getString(map.get("email")));
		// 会员开卡时间
		detailDataMap.put("MemGranddate", ConvertUtil.getString(map.get("joinDate")));
		// 开卡BA
		detailDataMap.put("BAcode", ConvertUtil.getString(map.get("baCodeBelong")));
		// 开卡柜台 
		detailDataMap.put("CardCounter", ConvertUtil.getString(map.get("counterCodeBelong")));
		// 新卡号
		detailDataMap.put("NewMemcode", ConvertUtil.getString(map.get("newMemcode")));
		// 会员换卡时间
		detailDataMap.put("MemChangeTime", ConvertUtil.getString(map.get("memChangeTime")));
		// 会员等级
		detailDataMap.put("MemLevel", ConvertUtil.getString(map.get("memLevel")));
		// 是否更改生日的标志
		detailDataMap.put("ModifyBirthdayFlag", ConvertUtil.getString(map.get("modifyBirthdayFlag")));
		// 入会时间
		detailDataMap.put("JoinTime", ConvertUtil.getString(map.get("joinTime")));
		// 推荐会员卡号 
		detailDataMap.put("Referrer", ConvertUtil.getString(map.get("referrer")));
		// 是否愿意接收短信
		detailDataMap.put("IsReceiveMsg", ConvertUtil.getString(map.get("isReceiveMsg")).trim());
		// 是否测试会员
		// 默认为正式会员
		String testType = "1";
		if (null != map.get("testType")) {
			testType = map.get("testType").toString();
		}
		detailDataMap.put("TestMemFlag", testType);
		String version  = ConvertUtil.getString(map.get("version"));
		if(version == null || "".equals(version)) {
			version = "1";
		}
		// 版本号
		detailDataMap.put("Version", version);
		// 备注1
		detailDataMap.put("Memo1", ConvertUtil.getString(map.get("memo1")));
		// 会员密码
		detailDataMap.put("MemberPassword", ConvertUtil.getString(map.get("memberPassword")));
		// 激活状态
		detailDataMap.put("Active", ConvertUtil.getString(map.get("active")));
		// 激活时间
		detailDataMap.put("ActiveDate", ConvertUtil.getString(map.get("activeDate")));
		// 激活途径
		detailDataMap.put("ActiveChannel", ConvertUtil.getString(map.get("activeChannel")));
		// 微信号
		detailDataMap.put("MessageId", ConvertUtil.getString(map.get("messageId")));
		// 微信绑定时间
		detailDataMap.put("WechatBindTime", ConvertUtil.getString(map.get("wechatBindTime")));
		// 会员信息登记区分
		detailDataMap.put("MemInfoRegFlg", ConvertUtil.getString(map.get("memInfoRegFlg")));
		detailDataList.add(detailDataMap);
		dataLine.put("DetailDataDTOList", detailDataList);
		msgDataMap.put("DataLine", dataLine);
		mqInfoDTO.setMsgDataMap(msgDataMap);

		// 设定插入到MongoDB的信息
		DBObject dbObject = new BasicDBObject();
		// 组织代码
		dbObject.put("OrgCode", map.get("orgCode"));
		// 品牌代码
		dbObject.put("BrandCode", map.get("brandCode"));
		// 业务类型
		dbObject.put("TradeType", billType);
		// 单据号
		dbObject.put("TradeNoIF", billCode);
		// 修改次数
		dbObject.put("ModifyCounts", "0");
		// 业务时间
		dbObject.put("OccurTime", "");
		// 会员卡号
		dbObject.put("TradeEntityCode", "");
		mqInfoDTO.setDbObject(dbObject);
		// 发送MQ消息
		binOLMQCOM01_BL.sendMQMsg(mqInfoDTO);
	}
	
	/**
     * 发送会员扩展信息MQ消息
     * 
     * @param map 会员扩展信息
     * @throws Exception 
     */
	@Override
	public void sendMZMQMsg(Map<String, Object> map) throws Exception {
		// 设定MQ消息DTO
		MQInfoDTO mqInfoDTO = new MQInfoDTO();
		// 品牌代码
		mqInfoDTO.setBrandCode((String) map.get("brandCode"));
		// 组织代码
		mqInfoDTO.setOrgCode((String) map.get("orgCode"));
		// 组织ID
		int organizationInfoId = Integer.parseInt(map.get(
				"organizationInfoId").toString());
		mqInfoDTO.setOrganizationInfoId(organizationInfoId);
		// 品牌ID
		int brandInfoId = Integer.parseInt(map.get("brandInfoId")
				.toString());
		mqInfoDTO.setBrandInfoId(brandInfoId);
		String billType = MessageConstants.MESSAGE_TYPE_MZ;
		// 单据号
		String billCode = binOLCM03_BL.getTicketNumber(organizationInfoId, brandInfoId, "", billType);
		// 业务类型
		mqInfoDTO.setBillType(billType);
		// 单据号
		mqInfoDTO.setBillCode(billCode);
		// 消息发送队列名
		mqInfoDTO.setMsgQueueName(CherryConstants.CHERRYTOPOSMSGQUEUE);
		// 设定消息内容
		Map<String, Object> msgDataMap = new HashMap<String, Object>();
		// 设定消息版本号
		msgDataMap.put("Version", MessageConstants.MESSAGE_VERSION_MZ);
		// 设定消息数据类型
		msgDataMap.put("DataType", MessageConstants.DATATYPE_APPLICATION_JSON);
		// 设定消息的数据行
		Map<String, Object> dataLine = new HashMap<String, Object>();
		// 消息的主数据行
		Map<String, Object> mainData = new HashMap<String, Object>();
		// 品牌代码
		mainData.put("BrandCode", map.get("brandCode"));
		// 会员号
		String memCode = binOLCM31_Service.getMemCard(map);
		mainData.put("MemberCode", memCode);
		// 业务类型
		mainData.put("TradeType", billType);
		// 子类型
		mainData.put("SubType", "");
		// 数据来源
		mainData.put("Sourse", "Cherry");
		// 单据号
		mainData.put("TradeNoIF", billCode);
		// 操作该笔业务的员工号
		mainData.put("TradeBAcode", ConvertUtil.getString(map.get("employeeCode")));
		// 业务时间
		String time = binOLCM31_Service.getForwardSYSDate();
		mainData.put("TradeTime", time);
		dataLine.put("MainData", mainData);
		// 消息的明细数据行
		List<Map<String, Object>> detailDataList = new ArrayList<Map<String, Object>>();
		Map<String, Object> detailDataMap = new HashMap<String, Object>();
		// 俱乐部代号
		detailDataMap.put("ClubCode", binOLCM31_Service.getClubCode(map));
		// 是否愿意接收短信
		detailDataMap.put("IsReceiveMsg", ConvertUtil.getString(map.get("isReceiveMsg")));
		// 推荐会员
		detailDataMap.put("Referrer", ConvertUtil.getString(map.get("referrer")));
		// 俱乐部发卡柜台号
		detailDataMap.put("CounterCodeBelong", ConvertUtil.getString(map.get("counterCode")));
		// 推荐会员
		detailDataMap.put("BaCodeBelong", ConvertUtil.getString(map.get("baCode")));
		// 推荐会员
		detailDataMap.put("JoinTime", ConvertUtil.getString(map.get("joinTime")));
		// 版本号
		detailDataMap.put("version", ConvertUtil.getString(map.get("version")));
		detailDataList.add(detailDataMap);
		dataLine.put("DetailDataDTOList", detailDataList);
		msgDataMap.put("DataLine", dataLine);
		mqInfoDTO.setMsgDataMap(msgDataMap);

		// 设定插入到MongoDB的信息
		DBObject dbObject = new BasicDBObject();
		// 组织代码
		dbObject.put("OrgCode", map.get("orgCode"));
		// 品牌代码
		dbObject.put("BrandCode", map.get("brandCode"));
		// 业务类型
		dbObject.put("TradeType", billType);
		// 单据号
		dbObject.put("TradeNoIF", billCode);
		// 修改次数
		dbObject.put("ModifyCounts", "0");
		// 业务时间
		dbObject.put("OccurTime", time);
		// 会员卡号
		dbObject.put("TradeEntityCode", memCode);
		mqInfoDTO.setDbObject(dbObject);
		// 发送MQ消息
		binOLMQCOM01_BL.sendMQMsg(mqInfoDTO);
	}
	/**
     * 发送会员扩展信息MQ消息(全部记录)
     * 
     * @param map 会员扩展信息
     * @throws Exception 
     */
	@Override
	public void sendAllMZMQMsg(Map<String, Object> map) throws Exception {
		// 查询会员俱乐部扩展信息
		List<Map<String, Object>> clubExtList = binOLCM31_Service.getClubExtList(map);
		if (null != clubExtList && !clubExtList.isEmpty()) {
			// 设定MQ消息DTO
			MQInfoDTO mqInfoDTO = new MQInfoDTO();
			// 品牌代码
			mqInfoDTO.setBrandCode((String) map.get("brandCode"));
			// 组织代码
			mqInfoDTO.setOrgCode((String) map.get("orgCode"));
			// 组织ID
			int organizationInfoId = Integer.parseInt(map.get(
					"organizationInfoId").toString());
			mqInfoDTO.setOrganizationInfoId(organizationInfoId);
			// 品牌ID
			int brandInfoId = Integer.parseInt(map.get("brandInfoId")
					.toString());
			mqInfoDTO.setBrandInfoId(brandInfoId);
			String billType = MessageConstants.MESSAGE_TYPE_MZ;
			// 单据号
			String billCode = binOLCM03_BL.getTicketNumber(organizationInfoId, brandInfoId, "", billType);
			// 业务类型
			mqInfoDTO.setBillType(billType);
			// 单据号
			mqInfoDTO.setBillCode(billCode);
			// 消息发送队列名
			mqInfoDTO.setMsgQueueName(CherryConstants.CHERRYTOPOSMSGQUEUE);
			// 设定消息内容
			Map<String, Object> msgDataMap = new HashMap<String, Object>();
			// 设定消息版本号
			msgDataMap.put("Version", MessageConstants.MESSAGE_VERSION_MZ);
			// 设定消息数据类型
			msgDataMap.put("DataType", MessageConstants.DATATYPE_APPLICATION_JSON);
			// 设定消息的数据行
			Map<String, Object> dataLine = new HashMap<String, Object>();
			// 消息的主数据行
			Map<String, Object> mainData = new HashMap<String, Object>();
			// 品牌代码
			mainData.put("BrandCode", map.get("brandCode"));
			// 会员号
			String memCode = binOLCM31_Service.getMemCard(map);
			mainData.put("MemberCode", memCode);
			// 业务类型
			mainData.put("TradeType", billType);
			// 子类型
			mainData.put("SubType", "");
			// 数据来源
			mainData.put("Sourse", "Cherry");
			// 单据号
			mainData.put("TradeNoIF", billCode);
			// 操作该笔业务的员工号
			mainData.put("TradeBAcode", "");
			// 业务时间
			String time = binOLCM31_Service.getForwardSYSDate();
			mainData.put("TradeTime", time);
			dataLine.put("MainData", mainData);
			// 消息的明细数据行
			List<Map<String, Object>> detailDataList = new ArrayList<Map<String, Object>>();
			for (Map<String, Object> clubExtInfo : clubExtList) {
				
				Map<String, Object> detailDataMap = new HashMap<String, Object>();
				// 俱乐部代号
				detailDataMap.put("ClubCode", clubExtInfo.get("clubCode"));
				// 是否愿意接收短信
				detailDataMap.put("IsReceiveMsg", ConvertUtil.getString(clubExtInfo.get("isReceiveMsg")));
				String referrer = null;
				if (null != clubExtInfo.get("referrerId")) {
					clubExtInfo.put("memberInfoId", clubExtInfo.get("referrerId"));
					referrer = binOLCM31_Service.getMemCard(clubExtInfo);
				}
				// 推荐会员
				detailDataMap.put("Referrer", ConvertUtil.getString(referrer));
				// 俱乐部发卡柜台号
				detailDataMap.put("CounterCodeBelong", ConvertUtil.getString(clubExtInfo.get("counterCode")));
				// 推荐会员
				detailDataMap.put("BaCodeBelong", ConvertUtil.getString(clubExtInfo.get("baCode")));
				// 推荐会员
				detailDataMap.put("JoinTime", ConvertUtil.getString(clubExtInfo.get("joinTime")));
				// 版本号
				detailDataMap.put("version", ConvertUtil.getString(clubExtInfo.get("version")));
				detailDataList.add(detailDataMap);
			}
			dataLine.put("DetailDataDTOList", detailDataList);
			msgDataMap.put("DataLine", dataLine);
			mqInfoDTO.setMsgDataMap(msgDataMap);
			// 设定插入到MongoDB的信息
			DBObject dbObject = new BasicDBObject();
			// 组织代码
			dbObject.put("OrgCode", map.get("orgCode"));
			// 品牌代码
			dbObject.put("BrandCode", map.get("brandCode"));
			// 业务类型
			dbObject.put("TradeType", billType);
			// 单据号
			dbObject.put("TradeNoIF", billCode);
			// 修改次数
			dbObject.put("ModifyCounts", "0");
			// 业务时间
			dbObject.put("OccurTime", time);
			// 会员卡号
			dbObject.put("TradeEntityCode", memCode);
			mqInfoDTO.setDbObject(dbObject);
			// 发送MQ消息
			binOLMQCOM01_BL.sendMQMsg(mqInfoDTO);
		}
	}
	/**
	 * 取得会员等级有效期信息
	 * 
	 * @param map
	 * 			memberLevelId : 会员等级ID
	 * @return Map
	 * 			validity : 有效期信息
	 */
	@Override
    public Map<String, Object> getLevelValidInfo(int levelId) throws Exception{
		Map<String, Object> map = new HashMap<String, Object>();
		// 会员等级ID
		map.put("memberLevelId", levelId);
    	// 取得会员等级有效期信息
    	Map<String, Object> validityMap = binOLCM31_Service.getComLevelValidInfo(map);
    	if (null != validityMap && !validityMap.isEmpty()) {
    		// 有效期信息
    		String validity = (String) validityMap.get("validity");
    		if (!CherryChecker.isNullOrEmpty(validity, true)) {
    			return (Map<String, Object>) JSONUtil.deserialize(validity);
    		}
    	}
    	return null;
    }
	
	
	/**
	 * 查询等级所有变化履历
	 * 
	 * @param map
	 * 			参数集合
	 * @return List
	 * 			等级所有变化履历
	 */
	@Override
	public List<Map<String, Object>> getLevelAllRecordList(Map<String, Object> map) {
		// 查询等级所有变化履历
		return binOLCM31_Service.getLevelAllRecordList(map);
	}
	
	/**
     * 取得所有等级变化明细
     * 
     * @param campBaseDTO 
     * 				会员实体
     * @param levelDetailList 
     * 				等级变化明细
	 * @return List
     * 				所有等级变化明细
     * @throws Exception 
     */
	@Override
	public List<LevelDetailDTO> getLevelAllRecords(CampBaseDTO campBaseDTO, List<LevelDetailDTO> levelDetailList) throws Exception{
		Map<String, Object> map = new HashMap<String, Object>();
		// 会员信息ID
		map.put("memberInfoId", campBaseDTO.getMemberInfoId());
		if (campBaseDTO.getMemberClubId() != 0) {
			map.put("memberClubId", campBaseDTO.getMemberClubId());
		}
		// 等级变化信息
		List<Map<String, Object>> levelRecordList = binOLCM31_Service.getLevelAllRecordList(map);
		if (null != levelRecordList) {
			List<LevelDetailDTO> allDetailList = new ArrayList<LevelDetailDTO>();
			// 取得会员等级列表
			List<Map<String, Object>> allLevelList = getAllLevelList(campBaseDTO);
			for (int i = 0; i < levelRecordList.size(); i++) {
				Map<String, Object> levelRecord = levelRecordList.get(i);
				// 单据号
				String billId = (String) levelRecord.get("billId");
				boolean delFlag = false;
				for (LevelDetailDTO levelDetail : levelDetailList) {
					// 单据号相同
					if (billId.equals(levelDetail.getRelevantNo())) {
						delFlag = true;
						break;
					}
				}
				if (delFlag) {
					levelRecordList.remove(i);
					i--;
				} else {
					// 等级MQ明细业务 DTO
					LevelDetailDTO levelDetailDTO = new LevelDetailDTO();
					// 会员卡号
					levelDetailDTO.setMemberCode((String) levelRecord.get("memCode"));
					// 操作类型
					levelDetailDTO.setOperateType(DroolsConstants.OPERATETYPE_I);
					// 变化类型
					levelDetailDTO.setChangeType((String) levelRecord.get("changeType"));
					// 变更前等级
					String oldValue = (String) levelRecord.get("oldValue");
					// 变更后等级
					String newValue = (String) levelRecord.get("newValue");
					if (!CherryChecker.isNullOrEmpty(oldValue)) {
						int oldLevel = Integer.parseInt(oldValue);
						if (0 != oldLevel) {
							// 变更前等级
							levelDetailDTO.setMemberlevelOld(RuleFilterUtil.findLevelCode(oldLevel, allLevelList));
						}
					}
					if (!CherryChecker.isNullOrEmpty(newValue)) {
						int newLevel = Integer.parseInt(newValue);
						// 变更后等级
						levelDetailDTO.setMemberlevelNew(RuleFilterUtil.findLevelCode(newLevel, allLevelList));
					}
					// 业务类型
					String tradeType = (String) levelRecord.get("tradeType");
					levelDetailDTO.setBizType(tradeType);
					// 关联单据时间
					levelDetailDTO.setRelevantTicketDate((String) levelRecord.get("ticketDate"));
					// 关联单号
					levelDetailDTO.setRelevantNo(billId);
					// 重算次数
					String reCalcCount = DroolsConstants.DEF_MODIFYCOUNTS;
					if (null != levelRecord.get("reCalcCount")) {
						reCalcCount = levelRecord.get("reCalcCount").toString();
					}
					levelDetailDTO.setReCalcCount(reCalcCount);
					// 取得单据对应的BA卡号及柜台号信息
					Map<String, Object> baCounterInfo = getBaCounterInfo(
							campBaseDTO.getOrganizationInfoId(), campBaseDTO.getBrandInfoId(), billId, tradeType);
					if (null != baCounterInfo && !baCounterInfo.isEmpty()) {
						// 柜台号
						String counterCode = (String) baCounterInfo.get("counterCode");
						levelDetailDTO.setCounterCode(counterCode);
						if (null != counterCode && !"".equals(counterCode)) {
							// 员工编号
							levelDetailDTO.setEmployeeCode((String) baCounterInfo.get("baCode"));
						}
						// 变动渠道
						levelDetailDTO.setChannel((String) baCounterInfo.get("channel"));
					}
					// 变化原因
					levelDetailDTO.setReason(getRuleReason((String) levelRecord.get("ruleIDs")));
					// 单据号
					map.put("billId", billId);
					// 累计金额
					map.put("recordKbn", DroolsConstants.RECORDKBN_1);
					// 查询指定单据某个属性信息
					Map<String, Object> billInfo = binOLCM31_Service.getBillInfo(map);
					if (null != billInfo && !billInfo.isEmpty()) {
						double totalAmount = 0;
						if (null != billInfo.get("newValue")) {
							totalAmount = Double.parseDouble(billInfo.get("newValue").toString());
						}
						// 累计金额
						levelDetailDTO.setTotalAmount(String.valueOf(totalAmount));
					}
					allDetailList.add(levelDetailDTO);
				}
			}
			if (!allDetailList.isEmpty()) {
				allDetailList.addAll(levelDetailList);
				return allDetailList;
			}
		}
		return levelDetailList;
	}
	
	/**
     * 取得会员等级列表
     * 			
     * @param campBaseDTO
     * 			会员实体
     * @return List
     * 			会员等级列表
     */
	@Override
    public List<Map<String, Object>> getAllLevelList(CampBaseDTO campBaseDTO) {
		// 会员等级列表
		List<Map<String, Object>> levelList = campBaseDTO.getMemberLevels();
		if (null == levelList || levelList.isEmpty()) {
			Map<String, Object> map = new HashMap<String, Object>();
			// 取得会员等级列表
			return binOLCM31_Service.getAllLevelList(map);
		}
		return levelList;
    }
	
	/**
     * 取得规则理由描述信息
     * 			
     * @param ruleIds
     * 			规则理由ID
     * @return String
     * 			规则理由描述信息
     */
	@Override
    public String getRuleReason(String ruleIds) {
    	if (null != ruleIds && !"".equals(ruleIds)) {
			String[] ruleIdArr = ruleIds.split(",");
			Map<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put("ruleIdArr", ruleIdArr);
			// 取得规则内容
			List<Map<String, Object>> ruleContentList = binOLCM31_Service.getRuleContentList(paramMap);
			StringBuffer buffer = new StringBuffer();
			if (null != ruleContentList) {
				for (int i = 0; i < ruleContentList.size(); i++) {
					Map<String, Object> ruleContentMap = ruleContentList.get(i);
					buffer.append(i + 1).append(DroolsConstants.MQ_REASON_COMMA).append(ruleContentMap.get("ruleContent"))
					.append(DroolsConstants.LINE_SPACE);
				}
				// 变化原因
				String reason = buffer.toString();
				if (reason.length() > DroolsConstants.REASON_MAX_LENGTH) {
					reason = reason.substring(0, DroolsConstants.REASON_MAX_LENGTH);
				}
				return reason;
			}
		}
    	return null;
	}
	
	/**
	 * 
	 * 取得会员保级信息
	 * 
	 * @param campBaseDTO 
	 * 				会员实体
	 * @return Map
	 * 				会员保级信息
	 * 
	 */
	@Override
	public Map<String, Object> getKeepMemLevelInfo(CampBaseDTO campBaseDTO) throws Exception {
		if (null != campBaseDTO.getExtArgs().get(DroolsConstants.KEEP_LEVELID)) {
			// 保留的等级
			int keepLevelId = Integer.parseInt(campBaseDTO.getExtArgs().get(DroolsConstants.KEEP_LEVELID).toString());
			// 等级一致不处理
			if (keepLevelId == campBaseDTO.getCurLevelId()) {
				return null;
			}
			Map<String, Object> map = new HashMap<String, Object>();
			// 组织
			map.put(CherryConstants.ORGANIZATIONINFOID, campBaseDTO.getOrganizationInfoId());
			// 组织code
			map.put(CherryConstants.ORG_CODE, campBaseDTO.getOrgCode());
			// 品牌code
			map.put(CherryConstants.BRAND_CODE, campBaseDTO.getBrandCode());
			// 品牌
			map.put(CherryConstants.BRANDINFOID, campBaseDTO.getBrandInfoId());
			//会员id
			map.put("memberInfoId", campBaseDTO.getMemberInfoId());
			//会员卡号
			map.put("memberCode", campBaseDTO.getMemCode());
			// 更新前等级Id
			String oldMemberLevelId = "";
			if (0 != campBaseDTO.getCurLevelId()) {
				oldMemberLevelId = String.valueOf(campBaseDTO.getCurLevelId());
			}
			map.put("oldMemberLevelId", oldMemberLevelId);
			// 更新后等级ID
			map.put("memberLevelId", String.valueOf(keepLevelId));
			//更新前入会时间
			map.put("oldJoinDate", "");
			//入会时间
			map.put("joinDate", "");
			//更新前化妆次数
			map.put("oldBtimes", "");
			//化妆次数
			map.put("btimes", "");
			//更新前累计金额
			String totalAmount = String.valueOf(campBaseDTO.getCurTotalAmount());
			map.put("oldTotalAmount", totalAmount);
			//累计金额
			map.put("totalAmount", totalAmount);
			//备注
			map.put("reason", DroolsConstants.KEEP_LEVEL_REASON);
			// 取得会员当前的更新信息
			Map<String, Object> upInfo = binOLCM31_Service.getMemberUpInfo(campBaseDTO);
			if (null != upInfo && !upInfo.isEmpty()) {
				map.putAll(upInfo);
			}
			return map;
		}
		return null;
	}
	
	/**
	 * 
	 * 取得会员等级维护信息
	 * 
	 * @param campBaseDTO 
	 * 				会员实体
	 * @return Map
	 * 				会员等级维护信息
	 * 
	 */
	@Override
	public Map<String, Object> getMemLevelChangeInfo(CampBaseDTO campBaseDTO) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		// 组织
		map.put(CherryConstants.ORGANIZATIONINFOID, campBaseDTO.getOrganizationInfoId());
		// 组织code
		map.put(CherryConstants.ORG_CODE, campBaseDTO.getOrgCode());
		// 品牌code
		map.put(CherryConstants.BRAND_CODE, campBaseDTO.getBrandCode());
		// 品牌
		map.put(CherryConstants.BRANDINFOID, campBaseDTO.getBrandInfoId());
		//会员id
		map.put("memberInfoId", campBaseDTO.getMemberInfoId());
		//会员卡号
		map.put("memberCode", campBaseDTO.getMemCode());
		// 更新前等级Id
		String oldMemberLevelId = "";
		if (0 != campBaseDTO.getOldLevelId()) {
			oldMemberLevelId = String.valueOf(campBaseDTO.getOldLevelId());
		}
		map.put("oldMemberLevelId", oldMemberLevelId);
		// 更新后等级ID
		map.put("memberLevelId", String.valueOf(campBaseDTO.getCurLevelId()));
		//更新前入会时间
		map.put("oldJoinDate", "");
		//入会时间
		map.put("joinDate", "");
		//更新前化妆次数
		map.put("oldBtimes", "");
		//化妆次数
		map.put("btimes", "");
		//更新前累计金额
		String totalAmount = String.valueOf(campBaseDTO.getCurTotalAmount());
		map.put("oldTotalAmount", totalAmount);
		//累计金额
		map.put("totalAmount", totalAmount);
		String reason = null;
		if (CherryChecker.isNullOrEmpty(campBaseDTO.getExtArgs().get("LCK_ID"), true)) {
			reason = DroolsConstants.CHANGE_LEVEL_REASON;
		} else {
			int ruleId = Integer.parseInt(String.valueOf(campBaseDTO.getExtArgs().get("LCK_ID")));
			reason = getRuleContentById(ruleId);
		}
		//备注
		map.put("reason", reason);
		// 取得会员当前的更新信息
		Map<String, Object> upInfo = binOLCM31_Service.getMemberUpInfo(campBaseDTO);
		if (null != upInfo && !upInfo.isEmpty()) {
			map.putAll(upInfo);
		}
		return map;
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
	@Override
	public String getFirstBillDate(CampBaseDTO campBaseDTO) {
		return (String) binOLCM31_Service.getFirstBillDate(campBaseDTO);
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
	@Override
	public List<Map<String, Object>> getPointChangeTimesList(Map<String, Object> map) {
		return binOLCM31_Service.getPointChangeTimesList(map);
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
	@Override
	public List<Map<String, Object>> getPCTimesList(Map<String, Object> map) {
		return binOLCM31_Service.getPCTimesList(map);
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
	@Override
	public List<Map<String, Object>> getPCTimesSRList(Map<String, Object> map) {
		return binOLCM31_Service.getPCTimesSRList(map);
	}
	
	/**
     *更新积分导入明细表
     * 
     * @param map
     * 		参数集合
     * 			memCode : 会员卡号
     * 			pointTime : 积分时间
     * @return int
     * 			更新件数
     */
    @Override
    public void updateMemPointImportDetail(Map<String, Object> map) {
    	// 设置共通的更新信息
    	setUpInfo(map);
		// 更新会员前卡积分值
		binOLCM31_Service.updateMemPointImportDetail(map);
    }
    
    /**
     * 验证某个单号是否是首单
     * 
     * @param map
     * @return boolean
     * 			true: 是    false: 否
     */
    @Override
    public boolean checkFirstBill(Map<String, Object> map) {
    	// 单次
    	boolean result = false;
    	// 取得一段时间内的购买信息
    	List<Map<String, Object>> billCodeList = binOLCM31_Service.getBillCodeList(map);
    	// 关联退货单信息
    	Map<String, Object> srMap = new HashMap<String, Object>();
    	if (null != billCodeList && !billCodeList.isEmpty()) {
    		for (int i = 0; i < billCodeList.size(); i++) {
    			Map<String, Object> billCodeInfo = billCodeList.get(i);
    			// 退货单
    			if ("SR".equals(billCodeInfo.get("saleType"))) {
    				// 关联单号
    				String billCodePre = (String) billCodeInfo.get("billCodePre");
    				if (!CherryChecker.isNullOrEmpty(billCodePre, true)) {
    					// 金额
    					double amount = Double.parseDouble(billCodeInfo.get("amount").toString());
    					// 累计退货金额
    					double totalAmount = 0;
    					if (srMap.containsKey(billCodePre)) {
    						totalAmount = Double.parseDouble(srMap.get(billCodePre).toString());
    					}
    					totalAmount = DoubleUtil.add(totalAmount, amount);
    					srMap.put(billCodePre, totalAmount);
    				}
    				billCodeList.remove(i);
    				i--;
    			}
    		}
	    	// 单号
	    	String billId = (String) map.get("billId");
	    	if (!CherryChecker.isNullOrEmpty(billId, true)) {
	    		for (int i = 0; i < billCodeList.size(); i++) {
	    			Map<String, Object> billCodeInfo = billCodeList.get(i);
	    			// 单号
	    	    	String billCode = (String) billCodeInfo.get("billCode");
	    			// 重新计算原单剩余金额
	    			if (srMap.containsKey(billCode)) {
	    				// 累计退货金额
	    				double srAmount = Double.parseDouble(srMap.get(billCode).toString());
	    				// 原单金额
	    				double amount = Double.parseDouble(billCodeInfo.get("amount").toString());
	    				amount = DoubleUtil.sub(amount, srAmount);
	    				if (amount <= 0) {
	    					if (billId.equals(billCode)) {
	    						return false;
	    					}
	    					continue;
	    				}
	    			}
	    	    	if (billId.equals(billCode)) {
	    	    		return true;
	    	    	} else {
	    	    		return false;
	    	    	}
	    		}
	    	}
    	}
        return result;
    }
    
    /**
     * 取得当前业务日期对应的可兑换积分截止日期
     * 
     * @param brandInfoId
     * 			品牌ID
     * @param busDate
     * 			业务日期
     * @return String
     * 			可兑换积分截止日期
     */
    @Override
    public String getExPointDateFromCamp(int brandInfoId, String busDate) {
    	Map<String, Object> map = new HashMap<String, Object>();
    	// 品牌ID
    	map.put("brandInfoId", brandInfoId);
    	// 业务日期
    	map.put("busDate", busDate);
    	// 取得当前业务日期对应的可兑换积分截止日期
		return binOLCM31_Service.getExPointDate(map);
    }
    
    /**
     * 清除会员清零标识
     * 
     * @param memberId
     * 			会员ID
     */
    @Override
    public void delClearFlag(int memberId) {
    	Map<String, Object> searchMap = new HashMap<String, Object>();
    	// 会员信息ID
    	searchMap.put("memberInfoId", memberId);
    	// 取得会员上次积分清零信息
    	Map<String, Object> preClearDateInfo = binOLCM31_Service.getPreClearDate(searchMap);
    	if (null != preClearDateInfo && !preClearDateInfo.isEmpty()) {
    		// 上一次清零最后的业务时间
    		String fromTime = (String) preClearDateInfo.get("preDisPointTime");
    		if (null != fromTime) {
    			searchMap.put("fromTime", fromTime);
    		}
	    	// 取得会员积分变化件数
	    	int count = binOLCM31_Service.getPointChangeCount(searchMap);
	    	if (count > 0) {
	    		// 更新者
	    		searchMap.put(DroolsConstants.UPDATEDBY, "BINOLCM31");
	    		// 更新程序名
	    		searchMap.put(DroolsConstants.UPDATEPGM, "BINOLCM31");
	    		// 更新积分变化表清零标识
	    		binOLCM31_Service.delClearFlag(searchMap);
	    	}
    	}
    }
    
    /**
     * 清除会员清零标识
     * 
     * @param memberId
     * 			会员ID
     */
    @Override
    public void delClubClearFlag(int memberId, int clubId) {
    	Map<String, Object> searchMap = new HashMap<String, Object>();
    	// 会员信息ID
    	searchMap.put("memberInfoId", memberId);
    	searchMap.put("memberClubId", clubId);
    	// 取得会员上次积分清零信息
    	Map<String, Object> preClearDateInfo = binOLCM31_Service.getPreClearDate(searchMap);
    	if (null != preClearDateInfo && !preClearDateInfo.isEmpty()) {
    		// 上一次清零最后的业务时间
    		String fromTime = (String) preClearDateInfo.get("preDisPointTime");
    		if (null != fromTime) {
    			searchMap.put("fromTime", fromTime);
    		}
	    	// 取得会员积分变化件数
	    	int count = binOLCM31_Service.getPointChangeCount(searchMap);
	    	if (count > 0) {
	    		// 更新者
	    		searchMap.put(DroolsConstants.UPDATEDBY, "BINOLCM31");
	    		// 更新程序名
	    		searchMap.put(DroolsConstants.UPDATEPGM, "BINOLCM31");
	    		// 更新积分变化表清零标识
	    		binOLCM31_Service.delClearFlag(searchMap);
	    	}
    	}
    }
    
    /**
	 * 取得沟通短信消息体(实时)
	 * 
	 * @param map
	 * 			参数集合
	 * @throws Exception
	 */
	@Override
	public MQInfoDTO getGTMQMessage(Map<String, Object> map) throws Exception{
		// 组织ID
		Object orgId = map.get("organizationInfoID");
		// 品牌ID
		Object brandId = map.get("brandInfoID");
		// 组织代码
		String orgCode = (String)map.get("orgCode");
		// 品牌代码
		String brandCode = (String)map.get("brandCode");
		// 设定MQ消息DTO
		MQInfoDTO mqInfoDTO = new MQInfoDTO();
		// 品牌代码
		mqInfoDTO.setBrandCode(brandCode);
		// 组织代码
		mqInfoDTO.setOrgCode(orgCode);
		// 组织ID
		mqInfoDTO.setOrganizationInfoId(Integer.parseInt(orgId.toString()));
		// 品牌ID
		mqInfoDTO.setBrandInfoId(Integer.parseInt(brandId.toString()));
		String billType = CherryConstants.MESSAGE_TYPE_ES;
		String billCode = binOLCM03_BL.getTicketNumber(Integer.parseInt(orgId.toString()), 
				Integer.parseInt(brandId.toString()), "", billType);
		// 业务类型
		mqInfoDTO.setBillType(billType);
		// 单据号
		mqInfoDTO.setBillCode(billCode);
		// 消息发送队列名
		mqInfoDTO.setMsgQueueName(CherryConstants.CHERRYEVENTSCHEDULEMSGQUEUE);
		// 设定消息内容
		Map<String,Object> msgDataMap = new HashMap<String,Object>();
		// 设定消息版本号
		msgDataMap.put("Version", CherryConstants.MESSAGE_VERSION_ES);
		// 设定消息命令类型
		msgDataMap.put("Type", CherryConstants.MESSAGE_TYPE_1007);
		// 设定消息数据类型
		msgDataMap.put("DataType", CherryConstants.DATATYPE_APPLICATION_JSON);
		// 设定消息的数据行
		Map<String,Object> dataLine = new HashMap<String,Object>();
		// 消息的主数据行
		Map<String,Object> mainData = new HashMap<String,Object>();
		mainData.put("BrandCode", brandCode);
		mainData.put("TradeType", billType);
		mainData.put("TradeNoIF", billCode);
		mainData.put("EventType", ConvertUtil.getString(map.get("eventType")));
		mainData.put("EventId", ConvertUtil.getString(map.get("eventId")));
		mainData.put("EventDate", ConvertUtil.getString(map.get("eventDate")));
		mainData.put("MessageContents", ConvertUtil.getString(map.get("messageContents")));
		mainData.put("Sourse", ConvertUtil.getString(map.get("sourse")));
		dataLine.put("MainData", mainData);
		msgDataMap.put("DataLine", dataLine);
		mqInfoDTO.setMsgDataMap(msgDataMap);
		
		// 设定插入到MongoDB的信息
		DBObject dbObject = new BasicDBObject();
		// 组织代码
		dbObject.put("OrgCode", orgCode);
		// 品牌代码
		dbObject.put("BrandCode", brandCode);
		// 业务类型
		dbObject.put("TradeType", billType);
		// 单据号
		dbObject.put("TradeNoIF", billCode);
		// 修改次数
		dbObject.put("ModifyCounts", "0");
		mqInfoDTO.setDbObject(dbObject);
		return mqInfoDTO;
	}
	
	/**
	 * 取得微信短信消息体(实时)
	 * 
	 * @param map
	 * 			参数集合
	 * @throws Exception
	 */
	@Override
	public MQInfoDTO getWXMQMessage(Map<String, Object> map) throws Exception{
		// 组织ID
		Object orgId = map.get("organizationInfoID");
		// 品牌ID
		Object brandId = map.get("brandInfoID");
		// 组织代码
		String orgCode = (String)map.get("orgCode");
		// 品牌代码
		String brandCode = (String)map.get("brandCode");
		// 设定MQ消息DTO
		MQInfoDTO mqInfoDTO = new MQInfoDTO();
		// 品牌代码
		mqInfoDTO.setBrandCode(brandCode);
		// 组织代码
		mqInfoDTO.setOrgCode(orgCode);
		// 组织ID
		mqInfoDTO.setOrganizationInfoId(Integer.parseInt(orgId.toString()));
		// 品牌ID
		mqInfoDTO.setBrandInfoId(Integer.parseInt(brandId.toString()));
		// 单据时间
		String changeTime = (String) map.get("changeTime");
		int index = changeTime.indexOf(".");
		if (index > 0) {
			changeTime = changeTime.substring(0, index);
		}
		String billCode = CherryConstants.MESSAGE_TYPE_WP + 
				changeTime.replaceAll("-", "").replace(":", "").replaceAll(" ", "");
		// 业务类型
		mqInfoDTO.setBillType(CherryConstants.MESSAGE_TYPE_WP);
		// 单据号
		mqInfoDTO.setBillCode(billCode);
		// 消息发送队列名
		mqInfoDTO.setMsgQueueName(CherryConstants.CHERRYTOWECHATMSGQUEUE);
		// 设定消息内容
		Map<String,Object> msgDataMap = new HashMap<String,Object>();
		// 设定消息版本号
		msgDataMap.put("Version", CherryConstants.MESSAGE_VERSION_WP);
		// 设定消息命令类型
		msgDataMap.put("Type", CherryConstants.MESSAGE_TYPE_1013);
		// 设定消息数据类型
		msgDataMap.put("DataType", CherryConstants.DATATYPE_APPLICATION_JSON);
		// 设定消息的数据行
		Map<String,Object> dataLine = new HashMap<String,Object>();
		// 消息的主数据行
		Map<String,Object> mainData = new HashMap<String,Object>();
		mainData.put("BrandCode", brandCode);
		mainData.put("TradeType", CherryConstants.MESSAGE_TYPE_WP);
		mainData.put("TradeNoIF", billCode);
		mainData.put("TempType", "1");
		mainData.put("MemberCode", ConvertUtil.getString(map.get("memberCode")));
		mainData.put("MemName", ConvertUtil.getString(map.get("memName")));
		mainData.put("ChangeTime", changeTime);
		mainData.put("PointType", ConvertUtil.getString(map.get("pointType")));
		mainData.put("NewPoint", ConvertUtil.getString(map.get("newPoint")));
		mainData.put("TotalPoint", ConvertUtil.getString(map.get("totalPoint")));
		mainData.put("CounterName", ConvertUtil.getString(map.get("counterName")));
		mainData.put("SaleType", ConvertUtil.getString(map.get("saleType")));
		mainData.put("SaleAmount", map.get("saleAmount"));
		dataLine.put("MainData", mainData);
		msgDataMap.put("DataLine", dataLine);
		mqInfoDTO.setMsgDataMap(msgDataMap);
		
		// 设定插入到MongoDB的信息
		DBObject dbObject = new BasicDBObject();
		// 组织代码
		dbObject.put("OrgCode", orgCode);
		// 品牌代码
		dbObject.put("BrandCode", brandCode);
		// 业务类型
		dbObject.put("TradeType", CherryConstants.MESSAGE_TYPE_WP);
		// 单据号
		dbObject.put("TradeNoIF", billCode);
		// 修改次数
		dbObject.put("ModifyCounts", "0");
		mqInfoDTO.setDbObject(dbObject);
		return mqInfoDTO;
	}
	
	/**
	 * 取得会员最近一次积分变化信息
	 * 
	 * @param campBaseDTO 查询条件
	 * @return 会员最近一次积分变化信息
	 */
	@Override
	public Map<String, Object> getLastPointChangeInfo(CampBaseDTO campBaseDTO) {
		// 取得会员最近一次积分变化信息
		return binOLCM31_Service.getLastPointChangeInfo(campBaseDTO);
	}
	
	/**
	 * 取得业务日期
	 * 
	 * @param campBaseDTO 查询条件
	 * @return 业务日期
	 */
	@Override
	public String getBusDate(CampBaseDTO campBaseDTO) {
		Map<String, Object> map = new HashMap<String, Object>();
		// 组织ID
		map.put("organizationInfoId", campBaseDTO.getOrganizationInfoId());
		// 品牌ID
		map.put("brandInfoId", campBaseDTO.getBrandInfoId());
		return binOLCM31_Service.getBusDate(map);
	}
	
	/**
	 * 取得会员生日变更履历
	 * 
	 * @param map 查询条件
	 * @return 会员生日变更履历
	 */
	@Override
	public List<Map<String, Object>> getBirthModyList(Map<String, Object> map) {
		// 取得会员生日变更履历
		List<Map<String, Object>> birthModyList = binOLCM31_Service.getBirthModyList(map);
		if (null != birthModyList && !birthModyList.isEmpty()) {
			if (birthModyList.size() == 1) {
				Map<String, Object> birthMap = birthModyList.get(0);
				// 修改前
				String oldBirth = (String) birthMap.get("oldBirth");
				if (CherryChecker.isNullOrEmpty(oldBirth, true)) {
					birthModyList.remove(0);
				}
			}
		}
		if (birthModyList.size() > 1) {
			for (int i = 0; i < birthModyList.size(); i++) {
				Map<String, Object> birthMapi = birthModyList.get(i);
				// 修改时间
				String modifyTimei = (String) birthMapi.get("modifyTime");
				for (int j = i + 1; j < birthModyList.size(); j++) {
					Map<String, Object> birthMapj = birthModyList.get(j);
					// 修改时间
					String modifyTimej = (String) birthMapj.get("modifyTime");
					// 修改时间为同一天取最后的修改生日
					if (DateUtil.compareDate(modifyTimei, modifyTimej) == 0) {
						// 修改后生日
						birthMapi.put("newBirth", birthMapj.get("newBirth"));
						birthModyList.remove(j);
						j--;
					} else {
						break;
					}
				}
			}
		}
		return birthModyList;
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
	@Override
	public String[] getBirthRuleArr(Map<String, Object> map) throws Exception {
		// 取得会员生日类规则ID
		return binOLCM31_Service.getBirthRuleArr(map);
	}
	
	/**
	 * 
	 * 取得会员生日规则匹配的履历
	 * 
	 * @param map 查询参数
	 * @return 销售记录List
	 * 
	 */
	@Override
	public Map<String, Object> getBirthPointDeatilInfo(Map<String, Object> map) {
		// 取得会员生日规则匹配的履历
		return binOLCM31_Service.getBirthPointDeatilInfo(map);
	}
	
	/**
	 * 重设生日
	 * 
	 * @param campBaseDTO 查询条件
	 * @throws Exception 
	 * 
	 */
	@Override
	public void resetBirth (CampBaseDTO campBaseDTO) throws Exception {
		if (CherryChecker.isNullOrEmpty(campBaseDTO.getBirthday(), true)) {
			return;
		}
		Map<String, Object> map = new HashMap<String, Object>();
		// 单据时间
		String ticketDate = campBaseDTO.getTicketDate();
		// 开始时间
		String beginTime = ticketDate.substring(0, 4) + "-01-01";
		map.put("beginTime", beginTime);
		// 会员ID
		map.put("memberInfoId", campBaseDTO.getMemberInfoId());
		// 取得会员生日变更履历
		List<Map<String, Object>> birthModyList = getBirthModyList(map);
		if (null != birthModyList && !birthModyList.isEmpty()) {
			// 品牌ID
			map.put("brandInfoId", campBaseDTO.getBrandInfoId());
			// 组织ID
			map.put("organizationInfoId", campBaseDTO.getOrganizationInfoId());
			// 取得会员生日类规则ID
			String[] arr = getBirthRuleArr(map);
			if (null != arr && arr.length > 0) {
				// 生日类规则
				map.put("mainIdArr", arr);
				// 截止时间
				map.put("toDate", ticketDate);
				map.put("fromDate", beginTime);
				// 取得会员生日规则匹配的履历
				Map<String, Object> birthDetail = getBirthPointDeatilInfo(map);
				if (null != birthDetail && !birthDetail.isEmpty()) {
					// 积分变化时间
					String changeDate = DateUtil.coverTime2YMD((String) birthDetail.get("changeDate"), DateUtil.DATE_PATTERN);
					// 通过业务时间获取生日
					String birthDay = getBirth(changeDate, birthModyList);
					campBaseDTO.setBirthday(birthDay);
				}
			}
		}
	}
	
	/**
	 * 
	 * 通过业务时间获取生日
	 * 
	 * @param billDate 业务时间
	 * @param birthModyList 生日修改履历列表
	 * @return String 生日
	 * 
	 */
	private String getBirth(String billDate, List<Map<String, Object>> birthModyList) {
		String birthDay = null;
		if (birthModyList.size() >= 1) {
			Map<String, Object> marthMap = null;
			for (Map<String, Object> birthModyMap : birthModyList) {
				// 生日变更时间
				String modifyTime = (String) birthModyMap.get("modifyTime");
				if (null != modifyTime && DateUtil.compareDate(billDate, modifyTime) >= 0) {
					marthMap = birthModyMap;
				} else {
					break;
				}
			}
			if (null == marthMap) {
				marthMap = birthModyList.get(0);
				// 老的生日
				String birth = (String) marthMap.get("oldBirth");
				if (CherryChecker.isNullOrEmpty(birth, true)) {
					 birth = (String) marthMap.get("newBirth");
				}
				if (!CherryChecker.isNullOrEmpty(birth, true)) {
					birthDay = "1992-" + birth.substring(0, 2) + "-" + birth.substring(2);
				}
			} else {
				// 新的生日
				String newBirth = (String) marthMap.get("newBirth");
				if (!CherryChecker.isNullOrEmpty(newBirth, true)) {
					birthDay = "1992-" + newBirth.substring(0, 2) + "-" + newBirth.substring(2);
				}
			}
		}
		return birthDay;
	}
	
	/**
	 * 
	 * 取得规则条件
	 * 
	 * @param map 查询参数
	 * @return 规则条件
	 * @throws Exception 
	 * 
	 */
	@Override
	public Map<String, Object> getRuleCond(Map<String, Object> map) throws Exception {
		// 取得规则条件
		Map<String, Object> ruleMap = binOLCM31_Service.getRuleCond(map);
		if (null != ruleMap && !ruleMap.isEmpty()) {
			String ruleFilter = (String) ruleMap.get("ruleFilter");
			if (null != ruleFilter && !ruleFilter.isEmpty()) {
				// 规则条件
				List<Map<String, Object>> filterMapList = (List<Map<String, Object>>) JSONUtil.deserialize(ruleFilter);
				if (null != filterMapList && !filterMapList.isEmpty()) {
					return (Map<String, Object>) filterMapList.get(0).get("params");
				}
			}
		}
		return null;
	}
	
	/**
	 * 
	 * 取得规则条件列表
	 * 
	 * @param map 查询参数
	 * @return 规则条件列表
	 * @throws Exception 
	 * 
	 */
	@Override
	public List<Map<String, Object>> getRuleCondList(Map<String, Object> map) throws Exception {
		// 取得规则条件
		List<Map<String, Object>> ruleList = binOLCM31_Service.getRuleCondList(map);
		if (null != ruleList && !ruleList.isEmpty()) {
			List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
			for (Map<String, Object> ruleMap : ruleList) {
				String ruleFilter = (String) ruleMap.get("ruleFilter");
				if (null != ruleFilter && !ruleFilter.isEmpty()) {
					// 规则条件
					List<Map<String, Object>> filterMapList = (List<Map<String, Object>>) JSONUtil.deserialize(ruleFilter);
					if (null != filterMapList && !filterMapList.isEmpty()) {
						resultList.add((Map<String, Object>) filterMapList.get(0).get("params"));
					}
				}
			}
			if (!resultList.isEmpty()) {
				return resultList;
			}
		}
		return null;
	}
	
	/**
     * 取得会员等级列表
     * 
     * @param map
     * @return
     * 		会员等级列表
	 * @throws Exception 
     */
	@Override
    public List<Map<String, Object>> getLevelList(Map<String, Object> map) throws Exception {
    	// 取得会员等级列表
    	return binOLCM31_Service.getComLevelList(map);
    }
    
	/**
	 * 
	 * 调整会员等级(珀莱雅)
	 * 
	 * @param map 
	 * 				查询参数
	 * @return int
	 * 				调整结果
	 * @throws Exception 
	 * 
	 */
	@Override
	public int levelAdjust(Map<String, Object> map) throws Exception {
		boolean isBatch = "1".equals(map.get("BATCH_FLAG"));
		Map<String, Object> searchMap = new HashMap<String, Object>();
		// 会员ID
		int memberInfoId = Integer.parseInt(map.get("memberInfoId").toString());
		searchMap.put("memberInfoId", memberInfoId);
		// 开始时间
		searchMap.put("fromDate", "2013-01-01 00:00:00");
		// 结束时间
		searchMap.put("toDate", "2014-01-01 00:00:00");
		if (isBatch) {
			int count = binOLCM31_Service.getSaleCount(searchMap);
			if (0 == count) {
				// 等级区分
				String lelStatus = (String) map.get("lelStatus");
				if (CherryChecker.isNullOrEmpty(lelStatus) ||
						"1".equals(lelStatus)) {
					return -1;
				}
				// 有效期开始时间
				String lelStartDate = (String) map.get("lelStartDate");
				if (null != lelStartDate && !lelStartDate.isEmpty()
						&& CherryChecker.compareDate(lelStartDate, "2014-01-01") >= 0) {
					return -1;
				}
			}
		}
		double totalAmount = 0;
		// 统计某段时间的累计金额
		Map<String, Object> amountMap = binOLCM31_Service.getTotalAmountByTime(searchMap);
		if (null != amountMap && !amountMap.isEmpty() && 
				null != amountMap.get("totalAmount")) {
			// 销售总金额
			totalAmount = Double.parseDouble(amountMap.get("totalAmount").toString());
			// 退货标识
			searchMap.put("SRFLAG", "1");
			// 退货总金额
			amountMap = binOLCM31_Service.getTotalAmountByTime(searchMap);
			if (null != amountMap && !amountMap.isEmpty() &&
					null != amountMap.get("totalAmount")) {
				double srAmount = Double.parseDouble(amountMap.get("totalAmount").toString());
				// 扣减退货金额
				totalAmount = DoubleUtil.sub(totalAmount, srAmount);
			}
		}
		// 等级代号
		String levelCode = null;
		if (totalAmount < 500) {
			// 普通会员
			levelCode = "WMLC002";
		} else if (totalAmount < 1500) {
			// 美妍会员
			levelCode = "WMLC003";
		} else if (totalAmount < 6000) {
			// 美妍钻石会员
			levelCode = "WMLC004";
		} else {
			// 大客户会员
			levelCode = "WMLC005";
		}
		// 会员等级列表
		List<Map<String, Object>> levelList = (List<Map<String, Object>>) map.get("LEVEL_LIST");
		if (null == levelList || levelList.isEmpty()) {
			// 取得会员等级列表
			levelList = binOLCM31_Service.getComLevelList(searchMap);
		}
		// 会员等级ID
		int levelId = RuleFilterUtil.findLevelId(levelCode, levelList);
		// 业务时间
		searchMap.put("businessTime", "2014-01-01 00:00:00");
		// 作成程序名
		searchMap.put(CherryConstants.CREATEPGM, "BINOLCM31BL");
		// 更新程序名
		searchMap.put(CherryConstants.UPDATEPGM, "BINOLCM31BL");
		// 作成者
		searchMap.put(CherryConstants.CREATEDBY, "BINOLCM31BL");
		// 更新者
		searchMap.put(CherryConstants.UPDATEDBY, "BINOLCM31BL");
		if (!isBatch) {
			// 取得会员等级维护记录
			Map<String, Object> levelRecord = binOLCM31_Service.getMSLevelRecord(searchMap);
			if (null != levelRecord && !levelRecord.isEmpty()) {
				// 会员等级
				String memberLevel = (String) levelRecord.get("memberLevel");
				if (!CherryChecker.isNullOrEmpty(memberLevel, true)) {
					int level = Integer.parseInt(memberLevel);
					if (levelId == level) {
						return -1;
					}
					searchMap.put("usedDetailId", levelRecord.get("usedDetailId"));
					searchMap.put("memberLevel", String.valueOf(levelId));
					// 理由
					searchMap.put("reason", DroolsConstants.LEVEL_ADJUST_RESON);
					// 更新新的等级
					binOLCM31_Service.updateMSLevelInfo(searchMap);
					return 0;
				}
			} else {
				return -1;
			}
		}
		// 会员等级ID
		searchMap.put("memberLevel", String.valueOf(levelId));
		// 组织ID
		searchMap.put("organizationInfoId", map.get("organizationInfoId"));
		// 品牌ID
		searchMap.put("brandInfoId", map.get("brandInfoId"));
		// 单据号
		String ticketNumber = "LAD140101" + memberInfoId;
		searchMap.put("tradeNoIF", ticketNumber);
		// 单据类型
		searchMap.put("tradeType", MessageConstants.MSG_MEMBER_MS);
		// 来源
		searchMap.put("ladChannel", "Cherry");
		// 插入会员使用化妆次数积分主表
		int useId = binOLCM31_Service.addMSLevelInfo(searchMap);
		searchMap.put("memUsedInfoId", useId);
		// 会员卡号
		searchMap.put("memberCode", map.get("memberCode"));
		// 理由
		searchMap.put("reason", DroolsConstants.LEVEL_ADJUST_RESON);
		// 插入会员使用化妆次数积分明细表
		binOLCM31_Service.addMSLevelDetail(searchMap);
		if (isBatch) {
			map.put("NEW_LEVEL", levelId);
			map.put("BTbillId", ticketNumber);
			map.put("NEW_CODE", levelCode);
			map.put("TOTAMOUNT", totalAmount);
		}
		return 0;
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
		// 取得规则执行次数
		return binOLCM31_Service.getRuleExecCount(map);
	}
	/**
	 * 取得会员积分明细总数
	 * @param memberInfoId
	 * 			会员ID
	 * @return int 
	 * 			积分明细总数
	 */
	@Override
	public int getTotalPtlNum(int memberInfoId) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("memberInfoId", memberInfoId);
		// 取得会员积分明细总数
		return binOLCM31_Service.getTotalPtlNum(map);
	}
	
	/**
	 * 取得会员俱乐部当前等级信息
	 * 
	 * @param map
	 * 			参数集合
	 * @return Map
	 * 			会员俱乐部当前等级信息
	 */
	@Override
	public Map<String, Object> getClubCurLevelInfo(Map<String, Object> map){
		// 取得会员俱乐部当前等级信息
		return binOLCM31_Service.getClubCurLevelInfo(map);
	}
	
	/**
	 * 
	 * 更新会员俱乐部扩展属性
	 * 
	 * @param map 更新条件
	 */
	public void updateClubExtInfo(Map<String, Object> map){
		// 更新会员俱乐部扩展属性
		binOLCM31_Service.updateClubExtInfo(map);
	}
	
	/**
	 * 
	 * 通过销售更新会员俱乐部属性
	 * 
	 * @param map 更新条件
	 * @throws Exception 
	 */
	@Override
	public void updateClubInfoBySale(Map<String, Object> map) throws Exception {
		// 查询会员俱乐部ID
		Integer memClubId = selMemClubId(map);
		if (null == memClubId) {
			return;
		}
		map.put("memberClubId", memClubId);
		Map<String, Object> memClubInfo = binOLCM31_Service.selMemClubInfo(map);
		if (null == memClubInfo || memClubInfo.isEmpty()) {
			return;
		}
		int memberInfoId = Integer.parseInt(String.valueOf(memClubInfo.get("memberInfoId")));
		map.put("memberInfoId", memberInfoId);
		int memInfoRegFlg = Integer.parseInt(memClubInfo.get("memInfoRegFlg").toString());
		if (1 == memInfoRegFlg) {
			return;
		}
		int clubLeveId = 0;
		int organizationId = 0;
		if (memClubInfo.get("clubLeveId") != null) {
			clubLeveId = Integer.parseInt(memClubInfo.get("clubLeveId").toString());
		}
		if (memClubInfo.get("organizationId") != null) {
			organizationId = Integer.parseInt(memClubInfo.get("organizationId").toString());
		}
		// 俱乐部开卡时间等属性是否于首单一致
		boolean isFirst = binOLCM14_BL.isConfigOpen("1343", String.valueOf(map.get("organizationInfoId")), String.valueOf(map.get("brandInfoId"))); 
		if (clubLeveId == 0 || organizationId == 0 || isFirst) {
			// 查询会员俱乐部首单信息
			Map<String, Object> firstSaleMap = binOLCM31_Service.selComMemClubFirstSale(map);
			if (null == firstSaleMap || firstSaleMap.isEmpty()) {
				return;
			}
			if (isFirst && clubLeveId != 0 && organizationId != 0) {
				String preJoinDate = (String) memClubInfo.get("joinDate");
				if (null != preJoinDate) {
					String saleDate = (String) firstSaleMap.get("saleDate");
					if (DateUtil.compareDate(preJoinDate, saleDate) <= 0) {
						return;
					}
				}
			}
			int employeeId = Integer.parseInt(String.valueOf(firstSaleMap.get("employeeId")));
			if (0 == employeeId) {
				Map<String, Object> baInfo = binOLCM31_Service.selBAInfoBySaleId(firstSaleMap);
				if (null != baInfo && !baInfo.isEmpty()) {
					map.put("employeeCode", baInfo.get("employeeCode"));
					Map<String, Object> empIdInfo = binOLCM31_Service.getComEmployeeInfo(map);
					if (null != empIdInfo && !empIdInfo.isEmpty()) {
						map.put("employeeId", empIdInfo.get("employeeId"));
					}
				}
			} else {
				map.put("employeeCode", firstSaleMap.get("employeeCode"));
				map.put("employeeId", firstSaleMap.get("employeeId"));
			}
			Map<String, Object> upInfo = new HashMap<String, Object>();
			// 作成程序名
			upInfo.put(CherryConstants.CREATEPGM, "BINOLCM31BL");
			// 更新程序名
			upInfo.put(CherryConstants.UPDATEPGM, "BINOLCM31BL");
			// 作成者
			upInfo.put(CherryConstants.CREATEDBY, "BINOLCM31BL");
			// 更新者
			upInfo.put(CherryConstants.UPDATEDBY, "BINOLCM31BL");
			upInfo.put("memberInfoId", memberInfoId);
			upInfo.put("memberClubId", memClubId);
			upInfo.put("organizationId", firstSaleMap.get("organizationId"));
			upInfo.put("departCode", firstSaleMap.get("departCode"));
			upInfo.put("employeeCode", map.get("employeeCode"));
			upInfo.put("employeeId", map.get("employeeId"));
			upInfo.put("joinTime", firstSaleMap.get("saleTime"));
			if (clubLeveId != 0) {
				upInfo.put("clubLeveId", clubLeveId);
				upInfo.put("preOrganizationId", memClubInfo.get("organizationId"));
				upInfo.put("preCounterCode", memClubInfo.get("counterCode"));
				upInfo.put("preEmployeeCode", memClubInfo.get("employeeId"));
				upInfo.put("preBaCode", memClubInfo.get("baCode"));
				upInfo.put("preJoinTime", memClubInfo.get("joinTime"));
				// 更新会员俱乐部属性 
				binOLCM31_Service.updateMemClubInfo(upInfo);
			} else {
				// 插入会员俱乐部属性
				binOLCM31_Service.addMemClubInfo(upInfo);
			}
			map.put("mzClubId", memClubId);
			// 发送会员扩展信息MQ消息(全部记录)
			sendAllMZMQMsg(map);
		}
	}
	
	/**
	 * 取得会员俱乐部代号
	 * 
	 * @param map
	 * 			参数集合
	 * @return String
	 * 			会员俱乐部代号
	 */
	@Override
	public String getClubCode(Map<String, Object> map){
		// 取得会员俱乐部代号
		return binOLCM31_Service.getClubCode(map);
	}
	
	/**
	 * 查询会员俱乐部ID
	 * @param map
	 *			参数集合
	 * @return Integer
	 * 			会员俱乐部ID
	 */
	@Override
	public Integer selMemClubId(Map map) {
		// 查询会员俱乐部ID
		return binOLCM31_Service.selMemClubId(map);
	}
	
	/**
	 * 验证柜台是否需要执行规则
	 * @param map
	 *			参数集合
	 * @return boolean
	 * 			true: 执行  false: 不执行
	 * @throws Exception 
	 */
	@Override
	public boolean isRuleCounter(Map<String, Object> map) throws Exception {
		// 匹配规则前是否执行柜台验证
		String counterFlag = binOLCM14_BL.getConfigValue("1302", String.valueOf(map.get("organizationInfoID")), String.valueOf(map.get("brandInfoID")));
		if ("1".equals(counterFlag)) {
			// 查询条件
			DBObject query = new BasicDBObject();
			// 品牌代码
			query.put("BrandCode", map.get("brandCode"));
			// 组织代码
			query.put("OrgCode", map.get("orgCode"));
			DBObject obj = MongoDB.findOne("MGO_RuleCounter", query);
			if (null != obj && !CherryChecker.isNullOrEmpty(obj.get("CounterCodes"))) {
				String counterCodes = (String) obj.get("CounterCodes");
				List<Object> counterList = (List<Object>) JSONUtil.deserialize(counterCodes);
				if (null != counterList && !counterList.isEmpty()) {
					// 柜台号
					String counterCode = (String) map.get("counterCode");
					if (CherryChecker.isNullOrEmpty(counterCode)) {
						// 取得会员所属柜台信息
						Map<String, Object> couBelInfo = binOLCM31_Service.getComCouBelongInfo(map);
						if (null == couBelInfo || couBelInfo.isEmpty() || CherryChecker.isNullOrEmpty(couBelInfo.get("counterCode"))) {
							return true;
						}
						counterCode = String.valueOf(couBelInfo.get("counterCode"));
					}
					boolean result = false;
					for (int i = 0; i < counterList.size(); i++) {
						String counter = String.valueOf(counterList.get(i));
						if (counterCode.trim().equalsIgnoreCase(counter)) {
							result = true;
							break;
						}
					}
					return result;
				}
			}
		}
		return true;
	}
	
	/**
	 * 查询会员俱乐部规则列表
	 * @param map
	 * @return
	 * 		会员俱乐部规则列表
	 */
	@Override
	public List<Map<String, Object>> selClubRuleList(Map<String, Object> map){
		return binOLCM31_Service.selComClubRuleList(map);
	}
	
	/**
     * 取得一段时间内的购买金额
     * 
     * @param map
     * @return
     * 		购买金额
     */
	@Override
    public double getTtlAmount(Map<String, Object> map) {
    	return binOLCM31_Service.getTtlAmount(map);
    }
	
	/**
     * 取得会员信息
     * 
     * @param map
     * @return
     * 		会员信息
	 * @throws Exception 
     */
	@Override
	public void getMember(Map<String, Object> map, Member member) throws Exception {
		Map<String, Object> memInfo = getMemberInfo(map);
		if (null != memInfo && !memInfo.isEmpty()) {
			member.setPoint(Long.parseLong(String.valueOf(memInfo.get("point"))));
			member.setMobile(String.valueOf(memInfo.get("mobile")));
			member.setLevel(Long.parseLong(memInfo.get("level").toString()));
		}
	}
	
	/**
     * 取得会员信息
     * 
     * @param map
     * @return
     * 		会员信息
	 * @throws Exception 
     */
	@Override
	public Map<String, Object> getMemberInfo(Map<String, Object> map) throws Exception {
		String tmallCounters = TmallKeys.getTmallCounters((String) map.get("brandCode"));
		if (!CherryChecker.isNullOrEmpty(tmallCounters)) {
			map.put("tmallCounterArr", tmallCounters.split(","));
		}
		Map<String, Object> memInfo = binOLCM31_Service.getMemberInfo(map);
		if (null != memInfo && !memInfo.isEmpty()) {
			int level = Integer.parseInt(memInfo.get("level").toString());
			if (0 == level) {
				map.put("brandInfoID", map.get("brandInfoId"));
				map.put("organizationInfoID", map.get("organizationInfoId"));
				level = binOLCM31_Service.getDefaultLevel(map);
				level = level > 0? level : 1;
			}
			memInfo.put("level", (long) level);
			double point = Double.parseDouble(memInfo.get("point").toString());
			if (point < 0) {
				memInfo.put("point", 0L);
			} else {
				memInfo.put("point", (long) point);
			}
			String mobile = (String) memInfo.get("mobile");
			if (null == mobile) {
				memInfo.put("mobile","");
			} else {
				memInfo.put("mobile",CherrySecret.decryptData((String) map.get("brandCode"), mobile));
			}
		}
		return memInfo;
	}
	
	/**
     * 是否需要同步天猫会员
     * 
     * @param memberInfoId 会员ID
     * @param brandCode 品牌代码
     * 
     * @return boolean true: 需要同步 false：不需要同步
     */
	@Override
	public boolean needSync(int memberInfoId, String brandCode) {
		TmallKeyDTO tmallKey = TmallKeys.getTmallKeyBybrandCode(brandCode);
		if (null == tmallKey) {
			return false;
		}
		// 检查天猫会员是否已绑定
		if (binOLCM31_Service.getBindMemCount(memberInfoId) == 1) {
			return true;
		}
		return false;
	}
	
	/**
     * 同步天猫会员
     * 
     * @param map
     * @return
	 * @throws Exception 
     */
	@Override
	public void syncTmall(Map<String, Object> map) throws Exception {
		TmallKeyDTO tmallKey = TmallKeys.getTmallKeyBybrandCode((String) map.get("brandCode"));
		if (null == tmallKey) {
			return;
		}
		// 会员ID
		Object memberIdObj = map.get("memberInfoId");
		// 取得会员信息
		Map<String, Object> member = getMemberInfo(map);
		TmallMeiCrmMemberSyncRequest req =
				new TmallMeiCrmMemberSyncRequest();
		// 明文手机号
		String mobile = (String) member.get("mobile");
		if (CherryChecker.isNullOrEmpty(mobile)) {
			String nick = (String) member.get("nick");
			if (CherryChecker.isNullOrEmpty(nick)) {
				String nickName = (String) member.get("tNickName");
				if (CherryChecker.isNullOrEmpty(nickName)) {
					Map<String, Object> registInfo = binOLCM31_Service.getMemRegisInfo(map);
					String mixNick = null;
					if (null != registInfo && !registInfo.isEmpty()) {
						mixNick = (String) registInfo.get("mixNick");
					}
					if (CherryChecker.isNullOrEmpty(mixNick)) {
						logger.error("同步所需参数都为空,不执行同步处理!会员ID：" + memberIdObj);
						return;
					}
					req.setMixNick(mixNick);
				} else {
					req.setNick(nickName);
				}
			} else {
				req.setNick(nick);
			}
		} else {
			req.setMobile(mobile);
		}
		// 积分值
		req.setPoint(Long.parseLong(String.valueOf(member.get("point"))));
		// 等级
		req.setLevel(Long.parseLong(String.valueOf(member.get("level"))));
		// 版本信息
		long version = System.currentTimeMillis();
		req.setVersion(version);
		req.setExtend("");
		map.put("syncVersion", version);
		// 程序名
		String pgmName = (String) map.get("PgmName");
		if (null == pgmName) {
			pgmName = "BINOLCM31";
		}
		map.put("point", member.get("point"));
		map.put("level", member.get("level"));
		map.put("createdBy", pgmName);
		map.put("createPGM", pgmName);
		map.put("updatedBy", pgmName);
		map.put("updatePGM", pgmName);
		StringBuilder builder = null;
		// 同步处理
		for (int i = 0; i < 4; i++) {
			try {
				TmallMeiCrmMemberSyncResponse response = SignTool.syncResponse(req, tmallKey.getAppKey(), tmallKey.getAppSecret(), tmallKey.getSessionKey());
				if (response.isSuccess()) {
					String body = response.getBody();
					//Map<String, Object> bodyMap = CherryUtil.json2Map(body);
					//String nick = (String) bodyMap.get("nick");
					String extraInfo = response.getMeiExtraInfo();
					if (!CherryChecker.isNullOrEmpty(extraInfo)) {
						try {
							Map<String, Object> extraMap = CherryUtil.json2Map(extraInfo);
							String nick = (String) extraMap.get("nick");
							String mobileNew = (String) extraMap.get("mobile");
							if (!CherryChecker.isNullOrEmpty(nick)) {
								map.put("taobao_nick", nick);
							}
							if (!CherryChecker.isNullOrEmpty(mobileNew)) {
								map.put("mobileNew", mobileNew);
							}
						} catch (Exception e) {
							logger.error("*********************************天猫同步返回结果转换Map失败！" + extraInfo);
						}
					}
					if (body.length() > 800) {
						body = body.substring(0, 800);
					}
					map.put("resultMsg", body);
					// 成功
					map.put("syncResult", 0);
					map.put("tmallSyncFlg", 1);
				} else {
					// 失败
					map.put("syncResult", 1);
					map.put("errorCode", response.getErrorCode());
					map.put("tmallSyncFlg", 2);
					builder = emptyBuilder(builder);
					// 异常原因
					builder.append("会员ID：").append(memberIdObj)
					.append(" 手机号：").append(mobile)
					.append(" 异常代号：").append(response.getErrorCode())
					.append(" 异常原因：").append(response.getMsg())
					.append(" 子异常代号：").append(response.getSubCode())
					.append(" 子异常原因：").append(response.getSubMsg());
					logger.error(builder.toString());
				}
				// 插入天猫会员同步表
				binOLCM31_Service.addTmallMemSyncInfo(map);
				// 更新会员同步信息
				binOLCM31_Service.updateMemSyncInfo(map);
				break;
			} catch (Exception e) {
				builder = emptyBuilder(builder);
				builder.append("会员ID：").append(memberIdObj)
				.append(" 手机号：").append(mobile)
				.append(" 回调次数：").append(i + 1)
				.append(" 异常信息：").append(e.getMessage());
				logger.error(builder.toString(),e);
				if (i == 3) {
					// 异常
					map.put("syncResult", 1);
					map.put("errorCode", "SYSERROR");
					map.put("tmallSyncFlg", 2);
					// 插入天猫会员同步表
					binOLCM31_Service.addTmallMemSyncInfo(map);
					// 更新会员同步信息
					binOLCM31_Service.updateMemSyncInfo(map);
					break;
				}
			}
		}
	}
	
	private StringBuilder emptyBuilder(StringBuilder builder) {
		if (null == builder) {
			builder = new StringBuilder();
		} else {
			if (builder.length() > 0) {
				builder.delete(0, builder.length());
			}
		}
		return builder;
	}
	
	/**
	 * 将消息发送到积分维护的MQ队列里
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	@Override
	public void sendPointsMQ(Map map) throws Exception {
		//积分维护明细数据
		List<Map<String,Object>> detailDataList = new ArrayList<Map<String,Object>>();
		Map<String,Object> detailMap = new HashMap<String,Object>();
			//会员卡号
			detailMap.put("MemberCode", map.get("MemberCode"));
			// 会员俱乐部ID
			detailMap.put("MemberClubId", map.get("memberClubId"));
			//修改的积分
			detailMap.put("ModifyPoint", map.get("ModifyPoint"));
			//业务时间
			detailMap.put("BusinessTime", map.get("BusinessTime"));
			//备注
			detailMap.put("Reason", map.get("Reason"));
			//员工Code
			detailMap.put("EmployeeCode", map.get("EmployeeCode"));
			String sourse = (String)map.get("Sourse");
			if(sourse == null || "".equals(sourse)) {
				sourse = "Cherry";
			}
			detailDataList.add(detailMap);
		    //设定MQ消息DTO
			MQInfoDTO mqInfoDTO = new MQInfoDTO();
			// 品牌代码
			mqInfoDTO.setBrandCode(ConvertUtil.getString(map.get("brandCode")));
			// 组织代码
			mqInfoDTO.setOrgCode(ConvertUtil.getString(map.get("orgCode")));
			// 组织ID
			mqInfoDTO.setOrganizationInfoId(Integer.parseInt(ConvertUtil.getString(map.get("organizationInfoId"))));
			// 品牌ID
			mqInfoDTO.setBrandInfoId(Integer.parseInt(ConvertUtil.getString(map.get("brandInfoId"))));
			//单据类型
			String billType = CherryConstants.MESSAGE_TYPE_PT;
			//单据号
			String billCode = binOLCM03_BL.getTicketNumber(Integer.parseInt(ConvertUtil.getString(map.get("organizationInfoId"))), 
					Integer.parseInt(ConvertUtil.getString(map.get("brandInfoId"))), "", billType);
			// 业务类型
			mqInfoDTO.setBillType(billType);
			// 单据号
			mqInfoDTO.setBillCode(billCode);
			// 消息发送队列名
			mqInfoDTO.setMsgQueueName(CherryConstants.CHERRYPOINTMSGQUEUE);
			// 设定消息内容
			Map<String,Object> msgDataMap = new HashMap<String,Object>();
			// 设定消息版本号
			msgDataMap.put("Version", CherryConstants.MESSAGE_VERSION_PT);
			// 设定消息命令类型
			msgDataMap.put("Type", CherryConstants.MESSAGE_TYPE_1004);
			// 设定消息数据类型
			msgDataMap.put("DataType", MessageConstants.DATATYPE_APPLICATION_JSON);
			// 设定消息的数据行
			Map<String,Object> dataLine = new HashMap<String,Object>();
			// 消息的主数据行
			Map<String,Object> mainData = new HashMap<String,Object>();
			// 品牌代码
			mainData.put("BrandCode", map.get("brandCode"));
			// 业务类型
			mainData.put("TradeType", billType);
			// 单据号
			mainData.put("TradeNoIF", billCode);
			// 数据来源
			mainData.put("Sourse", sourse);
			//修改模式
			mainData.put("SubTradeType", map.get("pointType"));
			if(!CherryChecker.isNullOrEmpty(map.get("MaintainType"), true)){
				//积分类型
				mainData.put("MaintainType", map.get("MaintainType"));
			}
			if(null != map.get("TmRecordId")){
				//积分类型
				mainData.put("TmRecordId", map.get("TmRecordId"));
			}
			dataLine.put("MainData", mainData);
			//积分明细
			dataLine.put("DetailDataDTOList", detailDataList);
			msgDataMap.put("DataLine", dataLine);
			mqInfoDTO.setMsgDataMap(msgDataMap);
			// 设定插入到MongoDB的信息
			DBObject dbObject = new BasicDBObject();
			// 组织代码
			dbObject.put("OrgCode", map.get("orgCode"));
			// 品牌代码
			dbObject.put("BrandCode", map.get("brandCode"));
			// 业务类型
			dbObject.put("TradeType", billType);
			// 单据号
			dbObject.put("TradeNoIF", billCode);
			// 数据来源
			dbObject.put("Sourse", sourse);
			mqInfoDTO.setDbObject(dbObject);
			// 发送MQ消息
			binOLMQCOM01_BL.sendMQMsg(mqInfoDTO);
	}
	
	/**
	 * 等级调整会员插入到临时表
	 * @param memberId
	 */
	@Override
	public void addTempAdjustMember(int memberId, int orgId, int brandId) {
		try {
			//判断等级变更是否自动修改单据
			if(binOLCM14_BL.isConfigOpen("1375", orgId, brandId)){
				binOLCM31_Service.insertTempAdjustMember(memberId);
			}
		} catch (Exception e) {
			logger.error("等级调整会员插入到临时表:", e);
		}
	}
}
