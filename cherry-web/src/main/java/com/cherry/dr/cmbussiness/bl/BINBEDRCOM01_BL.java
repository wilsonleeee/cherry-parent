/*	
 * @(#)BINBEDRCOM01_BL.java     1.0 2011/05/12	
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
package com.cherry.dr.cmbussiness.bl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.cm.activemq.dto.MQInfoDTO;
import com.cherry.cm.activemq.interfaces.BINOLMQCOM01_IF;
import com.cherry.cm.cmbussiness.bl.BINOLCM03_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM14_BL;
import com.cherry.cm.cmbussiness.interfaces.BINOLCM31_IF;
import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.DateUtil;
import com.cherry.dr.cmbussiness.core.CherryDRException;
import com.cherry.dr.cmbussiness.dto.core.CampBaseDTO;
import com.cherry.dr.cmbussiness.dto.core.PointDTO;
import com.cherry.dr.cmbussiness.dto.mq.BTimesDetailDTO;
import com.cherry.dr.cmbussiness.dto.mq.BTimesMainDTO;
import com.cherry.dr.cmbussiness.dto.mq.LevelBTimesDtlDTO;
import com.cherry.dr.cmbussiness.dto.mq.LevelBTimesMainDTO;
import com.cherry.dr.cmbussiness.dto.mq.LevelDetailDTO;
import com.cherry.dr.cmbussiness.dto.mq.LevelMainDTO;
import com.cherry.dr.cmbussiness.dto.mq.MQLogDTO;
import com.cherry.dr.cmbussiness.interfaces.BINBEDRCOM01_IF;
import com.cherry.dr.cmbussiness.service.BINBEDRCOM01_Service;
import com.cherry.dr.cmbussiness.util.DoubleUtil;
import com.cherry.dr.cmbussiness.util.DroolsConstants;
import com.cherry.dr.cmbussiness.util.DroolsMessageUtil;
import com.cherry.dr.cmbussiness.util.RuleFilterUtil;
import com.cherry.mq.mes.common.CherryMQException;
import com.cherry.mq.mes.common.MessageConstants;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

/**
 * 会员活动共通 BL
 * 
 * @author hub
 * @version 1.0 2011.05.12
 */
public class BINBEDRCOM01_BL implements BINBEDRCOM01_IF{
	
	@Resource
	private BINBEDRCOM01_Service binbedrcom01_Service;
	
	@Resource
	private BINOLCM03_BL binOLCM03_BL;
	
	@Resource
	private BINOLCM31_IF binOLCM31_BL;
	
	/** 发送MQ消息共通处理 IF **/
	@Resource
	private BINOLMQCOM01_IF binOLMQCOM01_BL;
	
	@Resource
	private BINOLCM14_BL binOLCM14_BL;
	
	private static Logger logger = LoggerFactory
	.getLogger(BINBEDRCOM01_BL.class.getName());
	
	/**
	 * 验证是否需要重算
	 * 
	 * @param map 
	 * 				查询条件
	 * @return boolean 
	 * 				验证结果 true: 需要  false: 不需要
	 */
	public boolean needReCalc(Map<String, Object> map) throws Exception {
		// 等级和化妆次数重算
		map.put("reCalcType", DroolsConstants.RECALCTYPE0);
		// 单据日期
		map.put("reCalcDate", map.get("saleTime"));
		// 单据修改次数
		int billModifyCounts = 0;
		Object modifyCountsObj = map.get("billModifyCounts");
		if (null != modifyCountsObj) {
			billModifyCounts = Integer.parseInt(modifyCountsObj.toString().trim());
		}
		// 退货的标志
		String saleSRtype = (String) map.get("saleSRtype");
		boolean flag = true;
		// 关联退货
		if (DroolsConstants.SALESRTYPE_2.equals(saleSRtype)) {
			flag = false;
			// 关联单号
			String relevantNo = (String) map.get("relevantNo");
			if (CherryChecker.isNullOrEmpty(relevantNo) || relevantNo.indexOf("SR") == 0) {
				// 关联退货单的关联单号不正确
				String errMsg = DroolsMessageUtil.getMessage(
						DroolsMessageUtil.EDR00013, null);
				throw new CherryMQException(errMsg);
			}
			// 取得关联单据信息
			Map<String, Object> relevantSaleMap = binbedrcom01_Service.getRelevantSaleInfo(map);
			if (null == relevantSaleMap || CherryChecker.isNullOrEmpty(relevantSaleMap.get("ticketDate"))) {
				flag = true;
			} else {
				// 重算时间
				map.put("reCalcDate", relevantSaleMap.get("ticketDate"));
			}
		} 
		if (flag) {
			if (0 == billModifyCounts) {
				// 取得规则执行记录数
				int count = binbedrcom01_Service.getRuleExecCount(map);
				if (0 == count) {
					// 取得重算信息记录数
				int reCalcCount = binbedrcom01_Service.getReCalcInfoCount(map);
				if (0 == reCalcCount) {
					// 判断会员是否正在进行重算
							boolean isReCalcExec = binOLCM31_BL.isReCalcExec(map);
							if (!isReCalcExec) {
								return false;
							}
						}
					}
			}
		}
		// 作成者
		map.put(DroolsConstants.CREATEDBY, DroolsConstants.CREATED_NAME);
		// 更新者
		map.put(DroolsConstants.UPDATEDBY, DroolsConstants.CREATED_NAME);
		// 作成程序名
		map.put(DroolsConstants.CREATEPGM, DroolsConstants.PGM_BINBEDRCOM01);
		// 更新程序名
		map.put(DroolsConstants.UPDATEPGM, DroolsConstants.PGM_BINBEDRCOM01);
		// 插入重算信息表
		binbedrcom01_Service.insertReCalcInfo(map);
		// 发送MQ重算消息进行实时重算
		sendReCalcMsg(map);
		if (!CherryChecker.isNullOrEmpty(map.get("oldClubId"))) {
			Object clubId = map.get("memberClubId");
			map.put("memberClubId", map.get("oldClubId"));
			// 插入重算信息表
			binbedrcom01_Service.insertReCalcInfo(map);
			// 发送MQ重算消息进行实时重算
			sendReCalcMsg(map);
			map.put("memberClubId", clubId);
		}
		return true;
	}
	
	/**
	 * 保存并发送重算MQ
	 * 
	 * @param map
	 * 			查询参数
	 * @throws Exception 
	 * 
	 */
	@Override
	public void saveAndSendReCalcMsg (Map<String, Object> map) throws Exception {
		// 等级和化妆次数重算
		map.put("reCalcType", DroolsConstants.RECALCTYPE0);
		// 单据日期
		map.put("reCalcDate", map.get("reCalcTime"));
		// 作成者
		map.put(DroolsConstants.CREATEDBY, DroolsConstants.PGM_BINBEDRCOM01);
		// 更新者
		map.put(DroolsConstants.UPDATEDBY, DroolsConstants.PGM_BINBEDRCOM01);
		// 作成程序名
		map.put(DroolsConstants.CREATEPGM, DroolsConstants.PGM_BINBEDRCOM01);
		// 更新程序名
		map.put(DroolsConstants.UPDATEPGM, DroolsConstants.PGM_BINBEDRCOM01);
		// 插入重算信息表
		binbedrcom01_Service.insertReCalcInfo(map);
		// 发送MQ重算消息进行实时重算
		sendReCalcMsg(map);
	}
	
	/**
	 * 取得关联单据信息
	 * 
	 * @param map
	 * 			查询参数
	 * @return Map
	 * 			销售记录
	 * 
	 */
	@Override
	public Map<String, Object> getRelevantSaleInfo(Map<String, Object> map) {
		// 取得关联单据信息
		return binbedrcom01_Service.getRelevantSaleInfo(map);
	}
	
	/**
	 * 根据业务类型，业务时间取得执行次数
	 * 
	 * @param map 查询条件
	 * @throws Exception 
	 */
	@Override
	public int getCountByType(Map<String, Object> map) {
		// 根据业务类型，业务时间取得执行次数
		return binbedrcom01_Service.getCountByType(map);
	}
	
	/**
	 * 验证是否需要重算(Batch处理)
	 * 
	 * @param map 
	 * 				查询条件
	 * @return boolean 
	 * 				验证结果 true: 需要  false: 不需要
	 */
	@Override
	public boolean needReCalcBatch(Map<String, Object> map) throws Exception {
		// 取得规则执行记录数
		int count = 0;
		if ("1".equals(map.get("BTLELAD"))) {
			// 取得规则执行记录数(调整等级用)
			count = binbedrcom01_Service.getRuleByKbnCount(map);
		} else {
			// 取得规则执行记录数
			count = binbedrcom01_Service.getRuleExecCount(map);
		}
		if (0 == count) {
			// 取得重算信息记录数
			int reCalcCount = binbedrcom01_Service.getReCalcInfoCount(map);
			if (0 == reCalcCount) {
				// 判断会员是否正在进行重算
				boolean isReCalcExec = binOLCM31_BL.isReCalcExec(map);
				if (!isReCalcExec) {
					return false;
				}
			}
		}
		return true;
	}
	
	/**
	 * 取得重算信息记录数(BATCH)
	 * 
	 * @param map
	 * 			查询参数
	 * @return int
	 * 			重算信息记录数
	 * 
	 */
	@Override
	public int getBTReCalcCount(Map<String, Object> map) {
		// 取得重算信息记录数(BATCH)
		return binbedrcom01_Service.getBTReCalcCount(map);
	}
	
	/**
	 * 查询重算日期信息
	 * 
	 * @param map
	 * 			查询参数
	 * @return Map
	 * 			重算信息
	 * 
	 */
	@Override
	public Map<String, Object> getReCalcDateInfo(Map<String, Object> map) {
		// 查询重算日期信息 
		return binbedrcom01_Service.getReCalcDateInfo(map);
		
	}
	
	/**
	 * 插入重算信息表
	 * 
	 * @param map 重算信息
	 * @throws Exception 
	 */
	@Override
	public void insertReCalcInfo(Map<String, Object> map) throws Exception {
		// 作成者
		map.put(DroolsConstants.CREATEDBY, DroolsConstants.CREATED_NAME);
		// 更新者
		map.put(DroolsConstants.UPDATEDBY, DroolsConstants.CREATED_NAME);
		// 作成程序名
		map.put(DroolsConstants.CREATEPGM, DroolsConstants.PGM_BINBEDRCOM01);
		// 更新程序名
		map.put(DroolsConstants.UPDATEPGM, DroolsConstants.PGM_BINBEDRCOM01);
		// 插入重算信息表
		binbedrcom01_Service.insertReCalcInfo(map);
	}
	
	/**
	 * 发送MQ重算消息进行实时重算
	 * 
	 * @param map 发送信息
	 * @throws Exception 
	 */
	@Override
	public void sendReCalcMsg(Map<String, Object> map) throws Exception {
		
		// 设定MQ消息DTO
		MQInfoDTO mqInfoDTO = new MQInfoDTO();
		// 品牌代码
		mqInfoDTO.setBrandCode((String)map.get("brandCode"));
		// 组织代码
		mqInfoDTO.setOrgCode((String)map.get("orgCode"));
		// 组织ID
		mqInfoDTO.setOrganizationInfoId(Integer.parseInt(map.get("organizationInfoID").toString()));
		// 品牌ID
		mqInfoDTO.setBrandInfoId(Integer.parseInt(map.get("brandInfoID").toString()));
		String billType = CherryConstants.MESSAGE_TYPE_MR;
		String billCode = binOLCM03_BL.getTicketNumber(Integer.parseInt(map.get("organizationInfoID").toString()), 
				Integer.parseInt(map.get("brandInfoID").toString()), "", billType);
		// 业务类型
		mqInfoDTO.setBillType(billType);
		// 单据号
		mqInfoDTO.setBillCode(billCode);
		// 消息发送队列名
		mqInfoDTO.setMsgQueueName(CherryConstants.CHERRYTOCHERRYMSGQUEUE);
		
		// 设定消息内容
		Map<String,Object> msgDataMap = new HashMap<String,Object>();
		// 设定消息版本号
		msgDataMap.put("Version", CherryConstants.MESSAGE_VERSION_MR);
		// 设定消息命令类型
		msgDataMap.put("Type", CherryConstants.MESSAGE_TYPE_1001);
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
		// 修改次数
		mainData.put("ModifyCounts", "0");
		// 会员ID
		mainData.put("memberInfoId", map.get("memberInfoId"));
		// 会员俱乐部ID
		mainData.put("memberClubId", map.get("memberClubId"));
		dataLine.put("MainData", mainData);
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
		mqInfoDTO.setDbObject(dbObject);
		
		// 发送MQ消息
		binOLMQCOM01_BL.sendMQMsg(mqInfoDTO);
	}
	
	/**
	 * 更新重算信息表
	 * 
	 * @param map 
	 * 				查询条件
	 */
	@Override
	public void updateReCalc(Map<String, Object> map) {
		if (null != map.get("saleTime")) {
			// 等级和化妆次数重算
			map.put("reCalcType", DroolsConstants.RECALCTYPE0);
			// 单据日期
			map.put("reCalcDate", map.get("saleTime"));
			// 作成者
			map.put(DroolsConstants.CREATEDBY, DroolsConstants.CREATED_NAME);
			// 更新者
			map.put(DroolsConstants.UPDATEDBY, DroolsConstants.CREATED_NAME);
			// 作成程序名
			map.put(DroolsConstants.CREATEPGM, DroolsConstants.PGM_BINBEDRCOM01);
			// 更新程序名
			map.put(DroolsConstants.UPDATEPGM, DroolsConstants.PGM_BINBEDRCOM01);
			// 更新重算信息表
			int result = binbedrcom01_Service.updateReCalcInfo(map);
			if (0 == result) {
				// 插入重算信息表
				binbedrcom01_Service.insertReCalcInfo(map);
			}
		}
	}
	
	/**
	 * 更新重算信息表(MQ)
	 * 
	 * @param map 
	 * 				查询条件
	 * @throws Exception 
	 */
	@Override
	public void updateReCalcMQ(Map<String, Object> map) throws Exception {
		// 验证是否是有效的会员
		boolean isMember = isValidMember(map);
		if (isMember) {
			// 更新重算信息表
			updateReCalc(map);
		}
	}
	
	/**
	 * 验证是否是有效的会员
	 * 
	 * @param map 
	 * 				查询条件
	 * @return boolean 
	 * 				验证结果 true: 是  false: 否
	 * @throws Exception 
	 */
	@Override
	public boolean isValidMember(Map<String, Object> map) throws Exception {
		// 会员卡号
		String memberCode = (String) map.get("memberCode");
		if (DroolsConstants.INVALID_MEMBERCODE.equals(memberCode)) {
			return false;
		}
		// 通过会员卡号取得会员ID
		int memberInfoId = binbedrcom01_Service.getMemberInfoId(map);
		if (0 == memberInfoId) {
			// 会员信息无记录
			String errMsg = DroolsMessageUtil.getMessage(DroolsMessageUtil.EDR00001, new String[]{memberCode});
			throw new CherryDRException(errMsg);
		}
		map.put("memberInfoId", memberInfoId);
		return true;
	}
	
	/**
	 * 验证是否需要执行规则
	 * 
	 * @param map 
	 * 				查询条件
	 * @return boolean 
	 * 				验证结果 true: 需要  false: 不需要
	 * @throws Exception 
	 */
	@Override
	public boolean isRuleExec(Map<String, Object> map) throws Exception {
		// 验证是否是有效的会员
		boolean validFlg = isValidMember(map);
		if (!validFlg) {
			return false;
		}
		String saleTime = (String) map.get("saleTime");
		int memberInfoId = Integer.parseInt(map.get("memberInfoId").toString());
		// 取得会员初始采集信息
		Map<String, Object> MemInitialInfo = null;
		if (CherryChecker.isNullOrEmpty(map.get("memberClubId"))) {
			MemInitialInfo = binOLCM31_BL.getMemInitialInfo(memberInfoId);
			if (null != MemInitialInfo && !MemInitialInfo.isEmpty()) {
				// 初始采集日期
				String initialDate = (String) MemInitialInfo.get("initialDate");
				if (!CherryChecker.isNullOrEmpty(saleTime) && !CherryChecker.isNullOrEmpty(initialDate)) {
					String saleDate = DateUtil.coverTime2YMD(saleTime, DateUtil.DATETIME_PATTERN);
					// 单据时间早于初始采集日期
					if (DateUtil.compareDate(saleDate, initialDate) < 0) {
						return false;
					}
				}
			}
		}
		// 验证是否需要重算
		boolean needFlg = needReCalc(map);
		if (needFlg) {
			return false;
		}
		return true;
	}
	
	/**
	 * 验证是否需要执行规则(包含积分处理器)
	 * 
	 * @param map 
	 * 				查询条件
	 * @return int 
	 * 				-1: 不执行   0: 执行等级积分  1: 仅执行积分
	 * @throws Exception 
	 */
	@Override
	public int isRuleExecPT(Map<String, Object> map) throws Exception {
		// 验证是否是有效的会员
		boolean validFlg = isValidMember(map);
		if (!validFlg) {
			return -1;
		}
		String saleTime = (String) map.get("saleTime");
		int memberInfoId = Integer.parseInt(map.get("memberInfoId").toString());
		if (CherryChecker.isNullOrEmpty(map.get("memberClubId"))) {
			// 取得会员初始采集信息
			Map<String, Object> MemInitialInfo = null;
			MemInitialInfo = binOLCM31_BL.getMemInitialInfo(memberInfoId);
			if (null != MemInitialInfo && !MemInitialInfo.isEmpty() && !CherryChecker.isNullOrEmpty(saleTime)) {
				// 初始采集日期
				String initialDate = (String) MemInitialInfo.get("initialDate");
				if (!CherryChecker.isNullOrEmpty(initialDate)) {
					String saleDate = DateUtil.coverTime2YMD(saleTime, DateUtil.DATETIME_PATTERN);
					// 单据时间早于初始采集日期
					if (DateUtil.compareDate(saleDate, initialDate) < 0) {
						return 1;
					}
				}
			}
			// 取得会员初始积分信息
			MemInitialInfo = binOLCM31_BL.getMemPointInitInfo(memberInfoId);
			if (null != MemInitialInfo && !MemInitialInfo.isEmpty()) {
				// 初始导入时间
				String initialTime = (String) MemInitialInfo.get("initialTime");
				if (!CherryChecker.isNullOrEmpty(initialTime)) {
					Calendar cal1 = Calendar.getInstance();
					cal1.setTime(DateUtil.coverString2Date(saleTime, DateUtil.DATETIME_PATTERN));
					Calendar cal2 = Calendar.getInstance();
					cal2.setTime(DateUtil.coverString2Date(initialTime, DateUtil.DATETIME_PATTERN));
					// 比较单据时间是否在初始导入时间之前
					if (cal1.before(cal2)) {
						return 1;
					}
				}
			}
		} else {
			int clubId = Integer.parseInt(String.valueOf(map.get("memberClubId")));
			// 取得会员初始采集信息
			Map<String, Object> MemInitialInfo = null;
			MemInitialInfo = binOLCM31_BL.getClubMemInitialInfo(memberInfoId, clubId);
			if (null != MemInitialInfo && !MemInitialInfo.isEmpty() && !CherryChecker.isNullOrEmpty(saleTime)) {
				// 初始采集日期
				String initialDate = (String) MemInitialInfo.get("initialDate");
				if (!CherryChecker.isNullOrEmpty(initialDate)) {
					String saleDate = DateUtil.coverTime2YMD(saleTime, DateUtil.DATETIME_PATTERN);
					// 单据时间早于初始采集日期
					if (DateUtil.compareDate(saleDate, initialDate) < 0) {
						return 1;
					}
				}
			}
			// 取得会员初始积分信息
			MemInitialInfo = binOLCM31_BL.getClubMemPointInitInfo(memberInfoId, clubId);
			if (null != MemInitialInfo && !MemInitialInfo.isEmpty()) {
				// 初始导入时间
				String initialTime = (String) MemInitialInfo.get("initialTime");
				if (!CherryChecker.isNullOrEmpty(initialTime)) {
					Calendar cal1 = Calendar.getInstance();
					cal1.setTime(DateUtil.coverString2Date(saleTime, DateUtil.DATETIME_PATTERN));
					Calendar cal2 = Calendar.getInstance();
					cal2.setTime(DateUtil.coverString2Date(initialTime, DateUtil.DATETIME_PATTERN));
					// 比较单据时间是否在初始导入时间之前
					if (cal1.before(cal2)) {
						return 1;
					}
				}
			}
		}
		// 验证是否需要重算
		boolean needFlg = needReCalc(map);
		if (needFlg) {
			return -1;
		}
		return 0;
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
		return binbedrcom01_Service.getMemCard(map);
	}
	/**
	 * 取得当前会员信息(会员俱乐部)
	 * 
	 * @param map 
	 * 				查询条件
	 * @return CampBaseDTO 
	 * 				当前会员信息
	 * @throws CherryDRException 
	 */
	@Override
	public CampBaseDTO getCurMemberInfoMZ(Map<String, Object> map) throws Exception {
		// 会员卡号
		String memberCode = (String) map.get("memberCode");
		// 取得会员信息
		CampBaseDTO campBaseDTO = binbedrcom01_Service.getCampBaseDTOMB(map);
		if (null == campBaseDTO) {
			// 会员信息无记录
			String errMsg = DroolsMessageUtil.getMessage(DroolsMessageUtil.EDR00001, new String[]{memberCode});
			throw new CherryDRException(errMsg);
		}
		// 会员俱乐部ID
		campBaseDTO.setMemberClubId(Integer.parseInt(map.get("memberClubId").toString()));
		// 会员俱乐部代号
		campBaseDTO.setClubCode((String) map.get("clubCode"));
		// 取得会员俱乐部当前等级信息
		Map<String, Object> clubLevelMap = binOLCM31_BL.getClubCurLevelInfo(map);
		if (null == clubLevelMap || clubLevelMap.isEmpty() || !"1".equals(clubLevelMap.get("mLevelStatus"))) {
			return null;
		}
		int memberLevel = Integer.parseInt(clubLevelMap.get("memberLevel").toString());
		if (0 == memberLevel) {
			return null;
		}
		campBaseDTO.setMemberLevel(memberLevel);
		campBaseDTO.setCurLevelId(campBaseDTO.getMemberLevel());
		campBaseDTO.setLevelStartDate((String) clubLevelMap.get("levelStartDate"));
		campBaseDTO.setLevelEndDate((String) clubLevelMap.get("levelEndDate"));
		campBaseDTO.setLevelAdjustDay((String) clubLevelMap.get("levelAdjustDay"));
		campBaseDTO.setGrantMemberLevel(Integer.parseInt(clubLevelMap.get("grantMemberLevel").toString()));
		campBaseDTO.setUpgradeFromLevel(Integer.parseInt(clubLevelMap.get("upgradeFromLevel").toString()));
		campBaseDTO.setCurTotalAmount(Double.parseDouble(clubLevelMap.get("totalAmount").toString()));
		campBaseDTO.setMemClubLeveId(Integer.parseInt(clubLevelMap.get("clubLeveId").toString()));
		// 会员卡号
		campBaseDTO.setMemCode(memberCode);
		// 单据号
		String ticketNo = (String) map.get("tradeNoIF");
		campBaseDTO.setBillId(ticketNo);
		// 业务类型
		campBaseDTO.setTradeType((String) map.get("tradeType"));
		// 来源
		campBaseDTO.setChannel((String) map.get("sourse"));
		// 系统日期
		String sysDate = binbedrcom01_Service.getForwardSYSDate();
		// 计算日期
		campBaseDTO.setCalcDate(sysDate);
		// 重算区分
		campBaseDTO.setReCalcCount(DroolsConstants.RECALCCOUNT_0);
		// 履历区分 ：等级
		map.put("recordKbn", DroolsConstants.RECORDKBN_0);
		// 根据履历区分取得履历次数
		int count = binbedrcom01_Service.getRecordCountByKbn(map);
		if (0 == count) {
			// 单据日期
			String ticketDateStr = campBaseDTO.getLevelAdjustDay();
			campBaseDTO.setTicketDate(ticketDateStr);
			// 履历区分 ：等级
			campBaseDTO.setRecordKbn(DroolsConstants.RECORDKBN_0);
			// 当前等级
			campBaseDTO.setNewValue(String.valueOf(campBaseDTO.getCurLevelId()));
			// 变化类型: 升级
			campBaseDTO.setChangeType(DroolsConstants.UPKBN_1);
			// 理由
			campBaseDTO.setReason(DroolsConstants.REASON_0);
			// 取得规则ID
			int ruleId = binOLCM31_BL.getRuleIdByCode(CherryConstants.CONTENTCODE2);
			String ruleIdStr = String.valueOf(ruleId);
			campBaseDTO.addRuleId(ruleIdStr);
			campBaseDTO.addAllRules(ruleIdStr, DroolsConstants.RECORDKBN_0);
			// 作成者
			campBaseDTO.setCreatedBy(DroolsConstants.CREATED_NAME);
			// 更新者
			campBaseDTO.setUpdatedBy(DroolsConstants.UPDATED_NAME);
			// 做成程序名
			campBaseDTO.setCreatePGM(DroolsConstants.PGM_BINBEDRCOM01);
			// 更新程序名
			campBaseDTO.setUpdatePGM(DroolsConstants.PGM_BINBEDRCOM01);
			// 插入规则执行履历表:等级
			addRuleExecRecord(campBaseDTO, DroolsConstants.RECORDKBN_0, true);
			// 柜台号
			campBaseDTO.setCounterCode((String) map.get("counterCode"));
			// 员工编号
			campBaseDTO.setEmployeeCode((String) map.get("BAcode"));
			return campBaseDTO;
		} 
		return null;
	}
	
	/**
	 * 取得当前会员信息(会员资料)
	 * 
	 * @param map 
	 * 				查询条件
	 * @return CampBaseDTO 
	 * 				当前会员信息
	 * @throws CherryDRException 
	 */
	@Override
	public CampBaseDTO getCurMemberInfoMB(Map<String, Object> map) throws Exception {
		// 取得会员销售记录数
//		int count = binbedrcom01_Service.getSaleRecordCount(map);
//		// 无销售记录
//		if (0 == count) {
//			return null;
//		}
		// 会员卡号
		String memberCode = (String) map.get("memberCode");
		// 取得会员信息
		CampBaseDTO campBaseDTO = binbedrcom01_Service.getCampBaseDTOMB(map);
		if (null == campBaseDTO) {
			// 会员信息无记录
			String errMsg = DroolsMessageUtil.getMessage(DroolsMessageUtil.EDR00001, new String[]{memberCode});
			throw new CherryDRException(errMsg);
		}
		boolean isClub = !CherryChecker.isNullOrEmpty(map.get("memberClubId"));
		// 无当前等级
		if (!isClub && 0 == campBaseDTO.getCurLevelId()) {
			return null;
		}
		if (isClub) {
			// 会员俱乐部ID
			campBaseDTO.setMemberClubId(Integer.parseInt(map.get("memberClubId").toString()));
			// 会员俱乐部代号
			campBaseDTO.setClubCode((String) map.get("clubCode"));
			// 取得会员俱乐部当前等级信息
			Map<String, Object> clubLevelMap = binOLCM31_BL.getClubCurLevelInfo(map);
			if (null != clubLevelMap && !clubLevelMap.isEmpty()) {
				campBaseDTO.setMemberLevel(Integer.parseInt(clubLevelMap.get("memberLevel").toString()));
				campBaseDTO.setCurLevelId(campBaseDTO.getMemberLevel());
				campBaseDTO.setLevelStartDate((String) clubLevelMap.get("levelStartDate"));
				campBaseDTO.setLevelEndDate((String) clubLevelMap.get("levelEndDate"));
				campBaseDTO.setLevelAdjustDay((String) clubLevelMap.get("levelAdjustDay"));
				campBaseDTO.setGrantMemberLevel(Integer.parseInt(clubLevelMap.get("grantMemberLevel").toString()));
				campBaseDTO.setUpgradeFromLevel(Integer.parseInt(clubLevelMap.get("upgradeFromLevel").toString()));
				campBaseDTO.setCurTotalAmount(Double.parseDouble(clubLevelMap.get("totalAmount").toString()));
				campBaseDTO.setMemClubLeveId(Integer.parseInt(clubLevelMap.get("clubLeveId").toString()));
			} else {
				return null;
			}
		}
		// 会员卡号
		campBaseDTO.setMemCode(memberCode);
		// 单据号
		String ticketNo = (String) map.get("tradeNoIF");
		campBaseDTO.setBillId(ticketNo);
		// 业务类型
		campBaseDTO.setTradeType((String) map.get("tradeType"));
		// 来源
		campBaseDTO.setChannel((String) map.get("sourse"));
		// 系统日期
		String sysDate = binbedrcom01_Service.getForwardSYSDate();
		// 计算日期
		campBaseDTO.setCalcDate(sysDate);
		// 重算区分
		campBaseDTO.setReCalcCount(DroolsConstants.RECALCCOUNT_0);
		// 非会员换卡
		if (!DroolsConstants.MEM_TYPE_CARD_CHANGE.equals(map.get("subType"))) {
			// 需要调用等级维护处理
			if ("1".equals(map.get("isMBLevelExec"))) {
				return campBaseDTO;
			}
			// 履历区分 ：等级
			map.put("recordKbn", DroolsConstants.RECORDKBN_0);
			// 根据履历区分取得履历次数
			int count = binbedrcom01_Service.getRecordCountByKbn(map);
			if (0 == count) {
				// 单据日期 
//				String ticketDateStr = null;
//				if (!CherryChecker.isNullOrEmpty(ticketNo)) {
//					int startIndex = ticketNo.length() - 17;
//					int endIndex = ticketNo.length() - 3;
//					if (startIndex > 0) {
//						// 单据日期 
//						ticketDateStr = ticketNo.substring(startIndex, endIndex);
//					}
//				}
//				// 验证单据日期格式是否正确
//				if (!CherryChecker.checkDate(ticketDateStr, "yyyyMMddHHmmss")) {
//					// 单据时间不正确
//					String errMsg = DroolsMessageUtil.getMessage(DroolsMessageUtil.EDR00006, new String[]{DroolsMessageUtil.EDR00012});
//					throw new CherryDRException(errMsg);
//				}
//				Date ticketDate = DateUtil.coverString2Date(ticketDateStr, "yyyyMMddHHmmss");
//				SimpleDateFormat dateFm = new SimpleDateFormat(DateUtil.DATETIME_PATTERN);
//				ticketDateStr = dateFm.format(ticketDate);
				// 单据日期
				String ticketDateStr = campBaseDTO.getLevelAdjustDay();
				campBaseDTO.setTicketDate(ticketDateStr);
				// 履历区分 ：等级
				campBaseDTO.setRecordKbn(DroolsConstants.RECORDKBN_0);
				// 当前等级
				campBaseDTO.setNewValue(String.valueOf(campBaseDTO.getCurLevelId()));
				// 变化类型: 升级
				campBaseDTO.setChangeType(DroolsConstants.UPKBN_1);
				// 理由
				campBaseDTO.setReason(DroolsConstants.REASON_0);
				// 取得规则ID
				int ruleId = binOLCM31_BL.getRuleIdByCode(CherryConstants.CONTENTCODE2);
				String ruleIdStr = String.valueOf(ruleId);
				campBaseDTO.addRuleId(ruleIdStr);
				campBaseDTO.addAllRules(ruleIdStr, DroolsConstants.RECORDKBN_0);
				// 作成者
				campBaseDTO.setCreatedBy(DroolsConstants.CREATED_NAME);
				// 更新者
				campBaseDTO.setUpdatedBy(DroolsConstants.UPDATED_NAME);
				// 做成程序名
				campBaseDTO.setCreatePGM(DroolsConstants.PGM_BINBEDRCOM01);
				// 更新程序名
				campBaseDTO.setUpdatePGM(DroolsConstants.PGM_BINBEDRCOM01);
				// 插入规则执行履历表:等级
				addRuleExecRecord(campBaseDTO, DroolsConstants.RECORDKBN_0, true);
				// 柜台号
				campBaseDTO.setCounterCode((String) map.get("counterCode"));
				// 员工编号
				campBaseDTO.setEmployeeCode((String) map.get("BAcode"));
				return campBaseDTO;
			} else {
				campBaseDTO.setTradeType(null);
				// 单据日期
				String ticketDate = campBaseDTO.getLevelAdjustDay();
				campBaseDTO.setTicketDate(ticketDate);
				campBaseDTO.getExtArgs().put(DroolsConstants.SEND_RECORDS_KBN, DroolsConstants.SEND_HIST_RECORDS);
				return campBaseDTO;
			}
		}
		campBaseDTO.setTradeType(null);
		// 单据日期
		String ticketDate = campBaseDTO.getLevelAdjustDay();
		campBaseDTO.setTicketDate(ticketDate);
		if (!isClub) {
			boolean amountflag = true;
			// 取得会员扩展信息
			Map<String, Object> memberExtInfo = binbedrcom01_Service.getMemberExtInfo(campBaseDTO);
			if (null != memberExtInfo && !memberExtInfo.isEmpty()) {
				if (null != memberExtInfo.get("initTotalAmount")) {
					campBaseDTO.setInitAmount(Double.parseDouble(memberExtInfo.get("initTotalAmount").toString()));
				}
				// 初始导入的会员从扩展表中获取当前累计金额
				if (null != memberExtInfo.get("initTotalAmount")
						&& null != memberExtInfo.get("totalAmount")) {
					campBaseDTO.setCurTotalAmount(Double.parseDouble(memberExtInfo.get("totalAmount").toString()));
					amountflag = false;
				}
			}
			if (amountflag) {
				// 累计金额
				campBaseDTO.setRecordKbn(DroolsConstants.RECORDKBN_1);
				// 当前累计金额
				String totalAmount = binbedrcom01_Service.getCurNewValue(campBaseDTO);
				if (!CherryChecker.isNullOrEmpty(totalAmount, true)) {
					campBaseDTO.setCurTotalAmount(Double.parseDouble(totalAmount));
				}
			}
			// 化妆次数
			campBaseDTO.setRecordKbn(DroolsConstants.RECORDKBN_2);
			// 当前化妆次数
			String btimes = binbedrcom01_Service.getCurNewValue(campBaseDTO);
			if (!CherryChecker.isNullOrEmpty(btimes, true)) {
				campBaseDTO.setCurBtimes(Integer.parseInt(btimes));
			}
		}
		// 改变前的会员等级
		campBaseDTO.setOldLevelId(campBaseDTO.getUpgradeFromLevel());
		// 等级
		campBaseDTO.setRecordKbn(DroolsConstants.RECORDKBN_0);
		// 查询最后一次引起某一属性变化的单据信息
		Map<String, Object> lastChangeInfo = binbedrcom01_Service.getLastChangeInfo(campBaseDTO);
		if (null != lastChangeInfo && !lastChangeInfo.isEmpty()) {
			// 单据号
			String billId = (String) lastChangeInfo.get("billId");
			// 业务类型
			String tradeType = (String) lastChangeInfo.get("tradeType");
			// 取得单据对应的BA卡号及柜台号信息
			Map<String, Object> baCounterInfo = binOLCM31_BL.getBaCounterInfo(
					campBaseDTO.getOrganizationInfoId(), campBaseDTO.getBrandInfoId(), billId, tradeType);
			if (null != baCounterInfo && !baCounterInfo.isEmpty()) {
				// 柜台号
				String counterCode = (String) baCounterInfo.get("counterCode");
				campBaseDTO.setCounterCode(counterCode);
				if (null != counterCode && !"".equals(counterCode)) {
					// 员工编号
					campBaseDTO.setEmployeeCode((String) baCounterInfo.get("baCode"));
				}
			}
		}
		return campBaseDTO;
	}
	
	/**
	 * 查询最后一次引起某一属性变化的单据信息
	 * 
	 * @param campBaseDTO
	 * 			会员活动基础 DTO
	 * @return String
	 * 			单据信息
	 */
	public Map<String, Object> getLastChangeInfo(CampBaseDTO campBaseDTO){
		return (Map<String, Object>) binbedrcom01_Service.getLastChangeInfo(campBaseDTO);
	}
	
	/**
	 * 查询第一次成为正式等级的变更记录
	 * 
	 * @param campBaseDTO
	 * 			会员活动基础 DTO
	 * @return Map
	 * 			第一次成为正式等级的变更记录
	 */
	@Override
	public Map<String, Object> getFirstFormalLevelInfo(CampBaseDTO campBaseDTO){
		// 会员当前等级
		int levelId = campBaseDTO.getCurLevelId();
		if (0 == levelId) {
			return null;
		}
		// 会员等级列表
		List<Map<String, Object>> levelList = campBaseDTO.getMemberLevels();
		// 查询有效期开始的单据信息
		List<Map<String, Object>> validStartList = binbedrcom01_Service.getValidStartList(campBaseDTO);
		if (null == validStartList || validStartList.isEmpty() || validStartList.size() == 1) {
			return null;
		}
		int size = validStartList.size();
		// 成为正式等级当天
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
		if (index >= 0 && index != size - 1) {
			return validStartList.get(index);
		} else {
			return null;
		}
	}
	
	/**
	 * 查询有效期开始的单据信息
	 * 
	 * @param campBaseDTO
	 * 			会员活动基础 DTO
	 * @return Map
	 * 			单据信息
	 */
	@Override
	public Map<String, Object> getValidStartInfo(CampBaseDTO campBaseDTO){
		// 会员当前等级
		int levelId = campBaseDTO.getCurLevelId();
		if (0 == levelId) {
			return null;
		}
		// 会员等级列表
		List<Map<String, Object>> levelList = campBaseDTO.getMemberLevels();
		// 该等级有效期信息
		Map<String, Object> validityMap = RuleFilterUtil.findLevelValidInfo(levelId, levelList);
		// 查询有效期开始的单据信息
		List<Map<String, Object>> validStartList = binbedrcom01_Service.getValidStartList(campBaseDTO);
		if (null == validStartList || validStartList.isEmpty()) {
			return null;
		}
		int size = validStartList.size();
		if (size == 1) {
			return validStartList.get(0);
		}
		if (null != validityMap) {
			// 开始时间区分
			String startTimeKbn = (String) validityMap.get("startTimeKbn");
			// 成为正式等级当天
			if ("1".equals(startTimeKbn)) {
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
				} else {
					return null;
				}
			}
		}
		int index = size - 1;
		for (int i = 0; i < size; i++) {
			Map<String, Object> validStart = validStartList.get(i);
			if ("SR".equals(validStart.get("tradeType"))) {
				continue;
			}
			// 会员等级
			String newValue = (String) validStart.get("newValue");
			if (!CherryChecker.isNullOrEmpty(newValue, true)) {
				int level = Integer.parseInt(newValue);
				if (level == campBaseDTO.getCurLevelId()) {
					index = i;
					break;
				}
			}
		}
		return validStartList.get(index);
	}
	
	/**
	 * 获取当前 等级和化妆次数
	 * 
	 * @param campBaseDTO 
	 * 				执行规则前的DTO
	 */
	@Override
	public String getCurNewValueByKbn(CampBaseDTO campBaseDTO) {
		// 获取当前 属性值
		return binbedrcom01_Service.getCurNewValue(campBaseDTO);
	}
	
	/**
	 * 获取当前 等级和化妆次数
	 * 
	 * @param campBaseDTO 
	 * 				执行规则前的DTO
	 */
	@Override
	public void getCurNewValue(CampBaseDTO campBaseDTO) {
//		// 会员等级
//		campBaseDTO.setRecordKbn(DroolsConstants.RECORDKBN_0);
//		// 当前会员等级
//		String levelId = binbedrcom01_Service.getCurNewValue(campBaseDTO);
//		if (null != levelId && !"".equals(levelId)) {
//			campBaseDTO.setCurLevelId(Integer.parseInt(levelId));
//		}
		// 化妆次数
//		campBaseDTO.setRecordKbn(DroolsConstants.RECORDKBN_2);
//		// 当前化妆次数
//		String btimes = binbedrcom01_Service.getCurNewValue(campBaseDTO);
//		if (null != btimes && !"".equals(btimes)) {
//			campBaseDTO.setCurBtimes(Integer.parseInt(btimes));
//		}
//		boolean amountflag = true;
		// 取得会员扩展信息
		Map<String, Object> memberExtInfo = binbedrcom01_Service.getMemberExtInfo(campBaseDTO);
		if (null != memberExtInfo && !memberExtInfo.isEmpty()) {
			//if (null != memberExtInfo.get("initTotalAmount")) {
				campBaseDTO.setInitAmount(Double.parseDouble(memberExtInfo.get("initTotalAmount").toString()));
			//}
			// 初始导入的会员从扩展表中获取当前累计金额
//			if (null != memberExtInfo.get("initTotalAmount")
//					&& null != memberExtInfo.get("totalAmount")) {
				campBaseDTO.setCurTotalAmount(Double.parseDouble(memberExtInfo.get("totalAmount").toString()));
//				amountflag = false;
			// 首单时间
			campBaseDTO.setFirstTicketTime((String) memberExtInfo.get("firstSaleDate"));
//			}
		}
//		if (amountflag) {
//			// 累计金额
//			campBaseDTO.setRecordKbn(DroolsConstants.RECORDKBN_1);
//			// 当前累计金额
//			String totalAmount = binbedrcom01_Service.getCurNewValue(campBaseDTO);
//			double totalAmountDou = 0;
//			if (null != totalAmount && !"".equals(totalAmount)) {
//				totalAmountDou = Double.parseDouble(totalAmount);
//			}
//			// 当前累计金额
//			campBaseDTO.setCurTotalAmount(totalAmountDou);
//		}
		// 可兑换金额(化妆次数用)
//		campBaseDTO.setRecordKbn(DroolsConstants.RECORDKBN_4);
//		// 当前可兑换金额(化妆次数用)
//		String btimesAmount = binbedrcom01_Service.getCurNewValue(campBaseDTO);
//		double btimesAmountDou = 0;
//		if (null != btimesAmount && !"".equals(btimesAmount)) {
//			btimesAmountDou = Double.parseDouble(btimesAmount);
//		}
//		// 当前的可兑换金额(化妆次数用)
//		campBaseDTO.setCurBtimesAmount(btimesAmountDou);
	}
	
	/**
	 * 生成执行规则前的DTO
	 * 
	 * @param map 
	 * 				查询条件
	 * @return CampBaseDTO 
	 * 				执行规则前的DTO
	 * @throws Exception 
	 */
	@Override
	public CampBaseDTO getCampBaseDTO(Map<String, Object> map) throws Exception {
		// 会员卡号
		String memberCode = (String) map.get("memberCode");
		// 取得会员信息
		CampBaseDTO campBaseDTO = binbedrcom01_Service.getCampBaseDTO(map);
		if (null == campBaseDTO) {
			// 会员信息无记录
			String errMsg = DroolsMessageUtil.getMessage(DroolsMessageUtil.EDR00001, new String[]{memberCode});
			throw new CherryDRException(errMsg);
		}
		// 会员生日
		String birthday = campBaseDTO.getBirthday();
		if (null != birthday && birthday.length() == 8) {
			// 生日为19000101时是否作为无值处理
			if ("19000101".equals(birthday) &&
					binOLCM14_BL.isConfigOpen("1344", String.valueOf(campBaseDTO.getOrganizationInfoId()), String.valueOf(campBaseDTO.getBrandInfoId()))) {
				birthday = null;
			} else {
				birthday = birthday.substring(0,4) + "-" + birthday.substring(4,6) + "-" + birthday.substring(6,8);
			}
			campBaseDTO.setBirthday(birthday);
		}
		// 会员卡号
		campBaseDTO.setMemCode(memberCode);
		// 查询销售记录
		Map<String, Object> saleRecordInfo = null;
		// 非在线订单
		if (!"1".equals(map.get("ESFlag"))) {
			// 查询销售记录
			saleRecordInfo = binbedrcom01_Service.getSaleRecordInfo(map);
		} else {
			saleRecordInfo = binbedrcom01_Service.getESOrderInfo(map);
			campBaseDTO.setEsFlag("1");
		}
		if (null != saleRecordInfo && !saleRecordInfo.isEmpty()) {
			Object totalAmount = saleRecordInfo.get("amount");
			if (null != totalAmount) {
				// 单次购买消费金额
				double amount = Double.parseDouble(totalAmount.toString());
				campBaseDTO.setAmount(amount);
			}
			campBaseDTO.setPointFlag((String) saleRecordInfo.get("pointFlag"));
		}
		// 单据号
		campBaseDTO.setBillId((String) map.get("tradeNoIF"));
		// 退货的标志
		String saleSRtype = (String) map.get("saleSRtype");
		// 业务类型: 销售
		String tradeType = (String) map.get("tradeType");
		if (null == tradeType || "".equals(tradeType)) {
			tradeType = DroolsConstants.TRADETYPE_NS;
		} else {
			tradeType = tradeType.toUpperCase();
		}
		// 退货
		if (DroolsConstants.SALESRTYPE_1.equals(saleSRtype) ||
				DroolsConstants.SALESRTYPE_2.equals(saleSRtype)) {
			// 业务类型: 退货
			tradeType = DroolsConstants.TRADETYPE_SR;
		}
		// 业务类型
		campBaseDTO.setTradeType(tradeType);
		// 单据产生日期
		campBaseDTO.setTicketDate((String) map.get("saleTime"));
		// 来源
		campBaseDTO.setChannel((String) map.get("data_source"));
		// 系统日期
		String sysDate = binbedrcom01_Service.getForwardSYSDate();
		// 计算日期
		campBaseDTO.setCalcDate(sysDate);
		// 重算区分
		campBaseDTO.setReCalcCount(DroolsConstants.RECALCCOUNT_0);
		// 理由
		campBaseDTO.setReason(DroolsConstants.REASON_0);
		// 作成者
		campBaseDTO.setCreatedBy(DroolsConstants.CREATED_NAME);
		// 更新者
		campBaseDTO.setUpdatedBy(DroolsConstants.UPDATED_NAME);
		// 做成程序名
		campBaseDTO.setCreatePGM(DroolsConstants.PGM_BINBEDRCOM01);
		// 更新程序名
		campBaseDTO.setUpdatePGM(DroolsConstants.PGM_BINBEDRCOM01);
		// 包含会员俱乐部
		if (!CherryChecker.isNullOrEmpty(map.get("memberClubId"))) {
			// 会员俱乐部ID
			campBaseDTO.setMemberClubId(Integer.parseInt(map.get("memberClubId").toString()));
			// 会员俱乐部代号
			campBaseDTO.setClubCode((String) map.get("clubCode"));
			// 取得会员俱乐部当前等级信息
			Map<String, Object> clubLevelMap = binOLCM31_BL.getClubCurLevelInfo(map);
			if (null != clubLevelMap && !clubLevelMap.isEmpty()) {
				campBaseDTO.setMemberLevel(Integer.parseInt(clubLevelMap.get("memberLevel").toString()));
				if ("1".equals(clubLevelMap.get("mLevelStatus"))) {
					campBaseDTO.setCurLevelId(0);
				} else {
					campBaseDTO.setCurLevelId(campBaseDTO.getMemberLevel());
				}
				campBaseDTO.setLevelStartDate((String) clubLevelMap.get("levelStartDate"));
				campBaseDTO.setLevelEndDate((String) clubLevelMap.get("levelEndDate"));
				campBaseDTO.setLevelAdjustDay((String) clubLevelMap.get("levelAdjustDay"));
				campBaseDTO.setGrantMemberLevel(Integer.parseInt(clubLevelMap.get("grantMemberLevel").toString()));
				campBaseDTO.setUpgradeFromLevel(Integer.parseInt(clubLevelMap.get("upgradeFromLevel").toString()));
				campBaseDTO.setCurTotalAmount(Double.parseDouble(clubLevelMap.get("totalAmount").toString()));
				campBaseDTO.setMemClubLeveId(Integer.parseInt(clubLevelMap.get("clubLeveId").toString()));
			} else {
				campBaseDTO.setMemberLevel(0);
				campBaseDTO.setCurLevelId(0);
				campBaseDTO.setLevelStartDate(null);
				campBaseDTO.setLevelEndDate(null);
				campBaseDTO.setLevelAdjustDay(null);
				campBaseDTO.setGrantMemberLevel(0);
				campBaseDTO.setUpgradeFromLevel(0);
				campBaseDTO.setCurTotalAmount(0);
			}
		} else {
			// 获取当前 等级和化妆次数
			getCurNewValue(campBaseDTO);
		}
		// 改变前的累计金额
		campBaseDTO.setOldTotalAmount(campBaseDTO.getCurTotalAmount());
		// 改变前的可兑换金额(化妆次数用)
//		campBaseDTO.setOldBtimesAmount(campBaseDTO.getCurBtimesAmount());
		// 改变前的会员等级
		campBaseDTO.setOldLevelId(campBaseDTO.getCurLevelId());
		// 改变前的化妆次数
//		campBaseDTO.setOldBtimes(campBaseDTO.getCurBtimes());
		// 柜台号
		campBaseDTO.setCounterCode((String) map.get("counterCode"));
		// 员工编号
		campBaseDTO.setEmployeeCode((String) map.get("BAcode"));
		Map<String, Object> searchMap = new HashMap<String, Object>();
		// 所属组织ID
		searchMap.put("organizationInfoId", campBaseDTO.getOrganizationInfoId());
		// 所属品牌ID
		searchMap.put("brandInfoId", campBaseDTO.getBrandInfoId());
		// 柜台号
		searchMap.put("counterCode", campBaseDTO.getCounterCode());
		// 员工编号
		searchMap.put("employeeCode", campBaseDTO.getEmployeeCode());
		// 取得柜台信息
		Map<String, Object> resultMap = binOLCM31_BL.getComCounterInfo(searchMap);
		if (null != resultMap && !resultMap.isEmpty()) {
			// 部门ID
			Object organizationId = resultMap.get("organizationId");
			if (null != organizationId) {
				campBaseDTO.setOrganizationId(Integer.parseInt(organizationId.toString()));
			}
			// 部门名称
			campBaseDTO.setDepartName((String) resultMap.get("departName"));
			// 渠道ID
			Object channelId = resultMap.get("channelId");
			if (null != channelId) {
				campBaseDTO.setChannelId(Integer.parseInt(channelId.toString()));
			}
			// 城市ID
			Object cityId = resultMap.get("cityId");
			if (null != cityId) {
				campBaseDTO.setCounterCityId(Integer.parseInt(cityId.toString()));
			}
		}
		// 取得员工信息
		resultMap = binOLCM31_BL.getComEmployeeInfo(searchMap);
		if (null != resultMap && !resultMap.isEmpty()) {
			// 员工ID
			campBaseDTO.setEmployeeId(Integer.parseInt(resultMap.get("employeeId").toString()));
		}
		// 设置会员属性
		binOLCM31_BL.execMemberInfo(campBaseDTO);
		// 退货
		if (DroolsConstants.TRADETYPE_SR.equals(campBaseDTO.getTradeType())) {
			// 当前累计金额
			campBaseDTO.setCurTotalAmount(DoubleUtil.sub(campBaseDTO.getCurTotalAmount(), campBaseDTO.getAmount()));
			// 当前的可兑换金额(化妆次数用)
//			campBaseDTO.setCurBtimesAmount(DoubleUtil.sub(campBaseDTO.getCurBtimesAmount(), campBaseDTO.getAmount()));
		} else {
			// 当前累计金额
			campBaseDTO.setCurTotalAmount(DoubleUtil.add(campBaseDTO.getCurTotalAmount(), campBaseDTO.getAmount()));
			// 当前的可兑换金额(化妆次数用)
//			campBaseDTO.setCurBtimesAmount(DoubleUtil.add(campBaseDTO.getCurBtimesAmount(), campBaseDTO.getAmount()));
		}
		return campBaseDTO;
	}
	
	/**
	 * 获取等级信息
	 * 
	 * @param campBaseDTO 
	 * 				执行规则前的DTO
	 * @throws Exception 
	 */
	@Override
	public List<Map<String, Object>> getMemLevelcomList(CampBaseDTO campBaseDTO) throws Exception {
		return binbedrcom01_Service.getMemLevelcomList(campBaseDTO);
	}
	
	/**
	 * 获取累计金额, 等级
	 * 
	 * @param campBaseDTO 
	 * 				执行规则前的DTO
	 */
	@Override
	public void getNewValueLevel(CampBaseDTO campBaseDTO) {
		// 会员等级
		campBaseDTO.setRecordKbn(DroolsConstants.RECORDKBN_0);
		// 当前会员等级
		String levelId = binbedrcom01_Service.getNewValue(campBaseDTO);
		if (null != levelId && !"".equals(levelId)) {
			campBaseDTO.setCurLevelId(Integer.parseInt(levelId));
		}
		// 累计金额
		campBaseDTO.setRecordKbn(DroolsConstants.RECORDKBN_1);
		// 当前累计金额
		String totalAmount = binbedrcom01_Service.getNewValue(campBaseDTO);
		double totalAmountDou = 0;
		if (null != totalAmount && !"".equals(totalAmount)) {
			totalAmountDou = Double.parseDouble(totalAmount);
		}
		// 当前累计金额
		campBaseDTO.setCurTotalAmount(totalAmountDou);
	}
	
	/**
	 * 获取化妆次数, 可兑换金额(化妆次数用)
	 * 
	 * @param campBaseDTO 
	 * 				执行规则前的DTO
	 */
	@Override
	public void getNewValueBtimes(CampBaseDTO campBaseDTO) {
		// 化妆次数
		campBaseDTO.setRecordKbn(DroolsConstants.RECORDKBN_2);
		// 当前化妆次数
		String btimes = binbedrcom01_Service.getNewValue(campBaseDTO);
		if (null != btimes && !"".equals(btimes)) {
			campBaseDTO.setCurBtimes(Integer.parseInt(btimes));
		}
		// 可兑换金额(化妆次数用)
		campBaseDTO.setRecordKbn(DroolsConstants.RECORDKBN_4);
		// 当前可兑换金额(化妆次数用)
		String btimesAmount = binbedrcom01_Service.getNewValue(campBaseDTO);
		double btimesAmountDou = 0;
		if (null != btimesAmount && !"".equals(btimesAmount)) {
			btimesAmountDou = Double.parseDouble(btimesAmount);
		}
		// 当前的可兑换金额(化妆次数用)
		campBaseDTO.setCurBtimesAmount(btimesAmountDou);	
	}

	/**
	 * 插入规则执行履历表
	 * 
	 * @param campBaseDTO
	 * @throws Exception 
	 */
	@Override
	public void addRuleExecRecord(CampBaseDTO campBaseDTO, int recordKbn, boolean isChange) throws Exception {
		try {
			// 会员状态没有发生变化的场合不需要添加或修改履历
			if(!isChange) {
				return;
			}
			// 更新前的值
			String oldValue = null;
			// 更新后的值
			String newValue = null;
			// 会员等级
			if (DroolsConstants.RECORDKBN_0 == recordKbn) {
				oldValue = String.valueOf(campBaseDTO.getOldLevelId());
				newValue = String.valueOf(campBaseDTO.getCurLevelId());
				// 累计金额
			} else if (DroolsConstants.RECORDKBN_1 == recordKbn) {
				oldValue = String.valueOf(campBaseDTO.getOldTotalAmount());
				newValue = String.valueOf(campBaseDTO.getCurTotalAmount());
				// 化妆次数
			} else if (DroolsConstants.RECORDKBN_2 == recordKbn) {
				oldValue = String.valueOf(campBaseDTO.getOldBtimes());
				newValue = String.valueOf(campBaseDTO.getCurBtimes());
				// 积分
			} else if (DroolsConstants.RECORDKBN_3 == recordKbn) {
				oldValue = String.valueOf(campBaseDTO.getOldPoint());
				newValue = String.valueOf(campBaseDTO.getCurPoint());
				// 可兑换金额(化妆次数用)
			} else if (DroolsConstants.RECORDKBN_4 == recordKbn) {
				oldValue = String.valueOf(campBaseDTO.getOldBtimesAmount());
				newValue = String.valueOf(campBaseDTO.getCurBtimesAmount());
			} else if (DroolsConstants.RECORDKBN_5 == recordKbn ||
					DroolsConstants.RECORDKBN_6 == recordKbn ||
					DroolsConstants.RECORDKBN_7 == recordKbn ||
					DroolsConstants.RECORDKBN_8 == recordKbn ||
					DroolsConstants.RECORDKBN_9 == recordKbn) {
				PointDTO pointInfo = campBaseDTO.getPointInfo();
				if (null != pointInfo) {
					// 累计积分
					if (DroolsConstants.RECORDKBN_5 == recordKbn) {
						oldValue = String.valueOf(pointInfo.getOldTotalPoint());
						newValue = String.valueOf(pointInfo.getCurTotalPoint());
						// 累计兑换积分
					} else if (DroolsConstants.RECORDKBN_6 == recordKbn) {
						oldValue = String.valueOf(pointInfo.getOldTotalChanged());
						newValue = String.valueOf(pointInfo.getCurTotalChanged());
						// 可兑换积分
					} else if (DroolsConstants.RECORDKBN_7 == recordKbn) {
						oldValue = String.valueOf(pointInfo.getOldChangablePoint());
						newValue = String.valueOf(pointInfo.getCurChangablePoint());
						// 累计失效积分
					} else if (DroolsConstants.RECORDKBN_8 == recordKbn) {
						oldValue = String.valueOf(pointInfo.getOldTotalDisPoint());
						newValue = String.valueOf(pointInfo.getTotalDisablePoint());
						 // 上次清零处理的截止积分变化时间
					} else if (DroolsConstants.RECORDKBN_9 == recordKbn) {
						oldValue = null;
						newValue = pointInfo.getPrePCBillTime();
					}
				}
			}
			// 履历区分
			campBaseDTO.setRecordKbn(recordKbn);
			// 更新前的值
			campBaseDTO.setOldValue(oldValue);
			// 更新后的值
			campBaseDTO.setNewValue(newValue);
			// 变化类型
			String oldChangType = null;
			if (DroolsConstants.RECORDKBN_0 != recordKbn) {
				oldChangType = campBaseDTO.getChangeType();
				campBaseDTO.setChangeType(null);
			}
			// 重算的场合
			if(campBaseDTO.getReCalcFlg() == DroolsConstants.RECALCFLG_1) {
				Map<String, Object> recordKbnInfo = campBaseDTO.getRecordKbnInfo();
				if(recordKbnInfo == null) {
					recordKbnInfo = new HashMap<String, Object>();
				}
				Map<String, Object> map = new HashMap<String, Object>();
				// 更新规则执行履历记录
				int updCount = binbedrcom01_Service.updRuleExecRecord(campBaseDTO);
				if(updCount == 0) {
					campBaseDTO.setReCalcCount(0);
					// 插入规则执行履历表
					binbedrcom01_Service.addRuleExecRecord(campBaseDTO);
					map.put("operateType", DroolsConstants.OPERATETYPE_I);
					map.put("reCalcCount", 0);
				} else {
					map.put("operateType", DroolsConstants.OPERATETYPE_U);
					map.put("reCalcCount", binbedrcom01_Service.getReCalcCount(campBaseDTO));
				}
				recordKbnInfo.put(String.valueOf(recordKbn), map);
			} else {
				// 插入规则执行履历表
				binbedrcom01_Service.addRuleExecRecord(campBaseDTO);
			}
			if (!CherryChecker.isNullOrEmpty(oldChangType, true)) {
				campBaseDTO.setChangeType(oldChangType);
			}
		} catch (Exception e) {
			logger.error(DroolsMessageUtil.getMessage(
					DroolsMessageUtil.EDR00002, new String[] {
							campBaseDTO.getBillId(),
							campBaseDTO.getRecordKbn() + "" }));
			throw e;
		}
	}
	
	/**
	 * 删除清零记录
	 * 
	 * @param campBaseDTO
	 * @throws Exception 
	 */
	@Override
	public void removeZCRecord(CampBaseDTO campBaseDTO, int recordKbn) throws Exception {
		// 履历区分
		campBaseDTO.setRecordKbn(recordKbn);
		// 删除清零记录
		binbedrcom01_Service.removeZCRecord(campBaseDTO);
	}

	/**
	 * 取得等级和化妆次数MQ消息体(实时)
	 * 
	 * @param campBaseDTO
	 * @throws Exception 
	 */
	@Override
	public MQLogDTO getLevelBtimesMQMessage(CampBaseDTO campBaseDTO) throws Exception{
		// 等级和化妆次数MQ主业务 DTO
		LevelBTimesMainDTO levelBTimesMainDTO = new LevelBTimesMainDTO();
		// 品牌代码
		levelBTimesMainDTO.setBrandCode(campBaseDTO.getBrandCode());
		// 单据号
		String ticketNumber = binOLCM03_BL.getTicketNumber(String.valueOf(campBaseDTO.getOrganizationInfoId()),
				String.valueOf(campBaseDTO.getBrandInfoId()), DroolsConstants.CREATED_NAME, DroolsConstants.MQ_BILLTYPE_MG);
		levelBTimesMainDTO.setTradeNoIF(ticketNumber);
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
		// 会员卡号
		levelBTimesMainDTO.setMemberCode(memCode);
		// 计算时间
		levelBTimesMainDTO.setCaltime(campBaseDTO.getCalcDate());
		// 等级和化妆次数MQ明细业务 DTO
		LevelBTimesDtlDTO levelBTimesDtlDTO = new LevelBTimesDtlDTO();
		// 会员卡号
		levelBTimesDtlDTO.setMemberCode(memCode);
		// 业务类型
		levelBTimesDtlDTO.setBizType(campBaseDTO.getTradeType());
		// 关联单据时间
		levelBTimesDtlDTO.setRelevantTicketDate(campBaseDTO.getTicketDate());
		// 关联单号
		levelBTimesDtlDTO.setRelevantNo(campBaseDTO.getBillId());
		// 变动渠道
		levelBTimesDtlDTO.setChannel(campBaseDTO.getChannel());
		// 重算次数
		levelBTimesDtlDTO.setReCalcCount("0");
		// 柜台号
		levelBTimesDtlDTO.setCounterCode(campBaseDTO.getCounterCode());
		// 员工编号
		levelBTimesDtlDTO.setEmployeeCode(campBaseDTO.getEmployeeCode());
		// 当前等级
		int memberLevel = campBaseDTO.getCurLevelId();
		Map<String, Object> levelMap = new HashMap<String, Object>();
		levelMap.put("memberLevelId", memberLevel);
		// 等级代码
		String levelCode = binbedrcom01_Service.getLevelCode(levelMap);
		// 当前等级
		levelBTimesMainDTO.setMember_level(levelCode);
		// 变更后等级
		levelBTimesDtlDTO.setMemberlevelNew(levelCode);
		if (campBaseDTO.getOldLevelId() != memberLevel) {
			levelMap.put("memberLevelId", campBaseDTO.getOldLevelId());
			// 等级代码
			levelCode = binbedrcom01_Service.getLevelCode(levelMap);
		}
		// 变更前等级
		levelBTimesDtlDTO.setMemberlevelOld(levelCode);
		// 当前化妆次数
		int btimes = campBaseDTO.getCurBtimes();
		// 变更前化妆次数
		int oldBtimes = campBaseDTO.getOldBtimes();
		// 化妆次数差分
		int diffBtimes = btimes - oldBtimes;
		// 当前化妆次数
		levelBTimesMainDTO.setCurBtimes(String.valueOf(btimes));
		// 变更后化妆次数
		levelBTimesDtlDTO.setCurBtimesNew(String.valueOf(btimes));
		// 变更前化妆次数
		levelBTimesDtlDTO.setBtimesOld(String.valueOf(oldBtimes));
		// 化妆次数差分
		levelBTimesDtlDTO.setDiffBtimes(String.valueOf(diffBtimes));
		
		String ruleIds = campBaseDTO.getAllRules(DroolsConstants.RECORDKBN_0);
		if (null != ruleIds && !"".equals(ruleIds)) {
			String[] ruleIdArr = ruleIds.split(",");
			Map<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put("ruleIdArr", ruleIdArr);
			// 取得规则内容
			List<Map<String, Object>> ruleContentList = binbedrcom01_Service.getRuleContentList(paramMap);
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
				levelBTimesDtlDTO.setReason(reason);
			}
		}
		List<LevelBTimesDtlDTO> levelBTimesDtlList = new ArrayList<LevelBTimesDtlDTO>();
		levelBTimesDtlList.add(levelBTimesDtlDTO);
		// 等级和化妆次数MQ明细业务List
		levelBTimesMainDTO.setLevelBTimesDtlList(levelBTimesDtlList);
		// 取得MQ消息体
		String msg = levelBTimesMainDTO.getMQMsg();
		// MQ收发日志DTO
		MQLogDTO mqLogDTO = new MQLogDTO();
		// 所属组织
		mqLogDTO.setOrganizationInfoId(campBaseDTO.getOrganizationInfoId());
		// 所属品牌
		mqLogDTO.setBrandInfoId(campBaseDTO.getBrandInfoId());
		// 单据类型
		mqLogDTO.setBillType(DroolsConstants.MQ_BILLTYPE_MG);
		// 单据号
		mqLogDTO.setBillCode(ticketNumber);
		// 消息发送接收标志位
		mqLogDTO.setReceiveFlag(DroolsConstants.MQ_RECEIVEFLAG_0);
		// 消息体
		mqLogDTO.setData(msg);
		// 业务流水
		DBObject dbObject = new BasicDBObject();
		//组织代号
		dbObject.put("OrgCode", campBaseDTO.getOrgCode());
		// 品牌代码，即品牌简称
		dbObject.put("BrandCode", campBaseDTO.getBrandCode());
		// 业务类型
		dbObject.put("TradeType", DroolsConstants.MQ_BILLTYPE_MG);
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
	    dbObject.put("Content", msg);
	    mqLogDTO.setDbObject(dbObject);
		return mqLogDTO;
	}
	
	/**
	 * 设置条件：等级信息，购买产品等
	 * 
	 * @param campBaseDTO
	 * @throws Exception
	 */
	@Override
	public void conditionSetting(CampBaseDTO campBaseDTO) throws Exception{
		if (null == campBaseDTO.getMemberLevels()) {
			// 取得会员等级List
			List<Map<String, Object>> memLevelList = binbedrcom01_Service.getMemLevelcomList(campBaseDTO);
			campBaseDTO.setMemberLevels(memLevelList);
		}
		// 不查询单据明细
		if ("0".equals(campBaseDTO.getExtArgs().get("NO_BUYINFO"))) {
			return;
		}
		// 取得购买的产品明细List
		getBuyProductList(campBaseDTO);
	}

	/**
	 * 取得购买的产品明细List
	 * 
	 * @param campBaseDTO
	 * @throws Exception
	 */
	public void getBuyProductList(CampBaseDTO campBaseDTO) throws Exception {
		boolean isNoEs = !"1".equals(campBaseDTO.getEsFlag());
		// 取得单据信息
		Map<String, Object> buyInfo = null;
		if (isNoEs) {
			buyInfo = binbedrcom01_Service.getBuyInfo(campBaseDTO);
		} else {
			buyInfo = binbedrcom01_Service.getESBuyInfo(campBaseDTO);
		}
		campBaseDTO.setBuyInfo(buyInfo);
		if (null == buyInfo || buyInfo.isEmpty()) {
			return;
		}
		// 取得购买的产品明细List
		List<Map<String, Object>> productList = null;
		if (isNoEs) {
			productList = binbedrcom01_Service.getBuyProductList(campBaseDTO);
		} else {
			productList = binbedrcom01_Service.getESBuyProductList(campBaseDTO);
		}
		if (null != productList) {
			// 重算的场合(非退货)
			if(isNoEs && campBaseDTO.getReCalcFlg() == DroolsConstants.RECALCFLG_1 &&
					!DroolsConstants.TRADETYPE_SR.equals(campBaseDTO.getTradeType())) {
				// 取得关联退货的产品明细List
				List<Map<String, Object>> srPrtList = binbedrcom01_Service.getSrProductList(campBaseDTO);
				if (null != srPrtList && !srPrtList.isEmpty()) {
					productList.addAll(srPrtList);
				}
			}
			for (Map<String, Object> productInfo : productList) {
				// 销售类型
				String saleType = (String) productInfo.get("saleType");
				if (null != saleType) {
					// 统一转化成大写
					saleType = saleType.toUpperCase();
					productInfo.put("saleType", saleType);
				}
				// 产品/促销品ID
				Object prtVendorIdObj = productInfo.get("prtVendorId");
				// 厂商编码
				String unitCode = (String) productInfo.get("unitCode");
				// 产品条码
				String barCode = (String) productInfo.get("barCode");
				int prtVendorId = 0;
				if (null != prtVendorIdObj) {
					prtVendorId = Integer.parseInt(prtVendorIdObj.toString());
				}
				
				Map<String, Object> searchMap = new HashMap<String, Object>();
				// 厂商编码
				searchMap.put("unitCode", unitCode);
				searchMap.put("unitcode", unitCode);
				// 产品条码
				searchMap.put("barCode", barCode);
				searchMap.put("barcode", barCode);
				// 品牌ID
				searchMap.put("brandInfoID", campBaseDTO.getBrandInfoId());
				// 组织ID
				searchMap.put("organizationInfoID", campBaseDTO
						.getOrganizationInfoId());
				// 单据时间
				searchMap.put("tradeDateTime", campBaseDTO.getTicketDate());
				// 促销产品
				if (DroolsConstants.SALE_TYPE_PROMOTION_SALE.equals(saleType)) {
					if (0 == prtVendorId) {
						// 查询促销产品信息
						Map<String, Object> resultMap = binbedrcom01_Service.selPrmProductInfo(searchMap);
						if (null == resultMap || resultMap.isEmpty()) {
							// 查询barcode变更后的促销产品信息
							resultMap = binbedrcom01_Service.selPrmProductPrtBarCodeInfo(searchMap);
							if (null != resultMap && !resultMap.isEmpty()) {
								Map<String, Object> tempMap = resultMap;
								searchMap.put("promotionProductVendorID", resultMap.get("promotionProductVendorID"));
								// 查询促销产品信息  根据促销产品厂商ID
								resultMap = binbedrcom01_Service.selPrmProductInfoByPrmVenID(searchMap);
								if (null == resultMap || resultMap.isEmpty()) {
									// 查询促销产品信息 根据促销产品厂商ID，去查产品ID，再去查有效的厂商ID
									List<Map<String, Object>> list = binbedrcom01_Service.selPrmAgainByPrmVenID(searchMap);
									if (list != null && !list.isEmpty()) {
										resultMap = (Map<String, Object>) list.get(0);
									} else {
										resultMap = tempMap;
									}
								}
							}
						}
						if (null == resultMap || resultMap.isEmpty()) {
							// 会员信息无记录
							String errMsg = DroolsMessageUtil.getMessage(
									DroolsMessageUtil.EDR00009, new String[] {
											barCode, unitCode });
							throw new CherryDRException(errMsg);
						}
						prtVendorId = Integer.parseInt(resultMap.get("promotionProductVendorID").toString());
						productInfo.put("prtVendorId", prtVendorId);
					}
					// 促销产品厂商ID
					searchMap.put("prtVendorId", prtVendorId);
					// 查询促销产品类别信息  根据促销产品厂商ID
					Map<String, Object> promotionCateMap = binbedrcom01_Service.selPromotionCateCd(searchMap);
					if (null != promotionCateMap && !promotionCateMap.isEmpty()) {
						// 促销产品类别
						productInfo.put("promotionCateCd", promotionCateMap.get("promotionCateCd"));
						// 兑换所需积分
						productInfo.put("exPoint", promotionCateMap.get("exPoint"));
					}
				} else {
					if (0 == prtVendorId) {
						// 查询促销产品信息
						Map<String, Object> resultMap = binbedrcom01_Service.selProductInfo(searchMap);
						if (null == resultMap || resultMap.isEmpty()) {
							// 查询barcode变更后的产品信息
							resultMap = binbedrcom01_Service.selPrtBarCode(searchMap);
							if (null != resultMap && !resultMap.isEmpty()) {
								Map<String, Object> tempMap = resultMap;
								// 产品厂商ID
								searchMap.put("productVendorID", resultMap.get("productVendorID"));
								// 查询产品信息 根据产品厂商ID
								resultMap = binbedrcom01_Service.selProductInfoByPrtVenID(searchMap);
								if (null == resultMap || resultMap.isEmpty()) {
									 // 查询产品信息  根据产品厂商ID，去查产品ID，再去查有效的厂商ID
									 List<Map<String, Object>> list = binbedrcom01_Service.selProAgainByPrtVenID(searchMap);
									 if(list != null && !list.isEmpty()){
										 resultMap = (Map<String, Object>)list.get(0);
									 } else {
										 resultMap = tempMap;
									 }
								}
							}
						}
						if (null == resultMap || resultMap.isEmpty()) {
							// 会员信息无记录
							String errMsg = DroolsMessageUtil.getMessage(
									DroolsMessageUtil.EDR00009, new String[] {
											barCode, unitCode });
							throw new CherryDRException(errMsg);
						}
						prtVendorId = Integer.parseInt(resultMap.get("productVendorID").toString());
						productInfo.put("prtVendorId", prtVendorId);
					}
					// 促销产品厂商ID
					searchMap.put("prtVendorId", prtVendorId);
					// 查询产品分类信息
					List<Map<String, Object>> prtCateList = binbedrcom01_Service.selPrtCateList(searchMap);
					productInfo.put("prtCateList", prtCateList);
				}
			}
			buyInfo.put("saleDetailList", productList);
		}
	}
	
	/**
	 * 取得累计金额
	 * 
	 * @param Map
	 * 			保存处理的参数集合
	 * @return double 
	 * 			累计金额
	 * @throws Exception
	 */
	@Override
	public double getTotalAmount(Map<String, Object> map) throws Exception {
		// 取得某个时间点的累计金额
		double fromAmount = getTotalAmountByTime(map);
		// 结束时间点的累计金额
		double toAmount = 0;
		Object toAmountObj = map.get("toAmount");
		if (null != toAmountObj) {
			toAmount = Double.parseDouble(toAmountObj.toString());
		}
		return DoubleUtil.sub(toAmount, fromAmount);
	}
	
	/**
	 * 取得某个时间点的累计金额
	 * 
	 * @param Map
	 * 			保存处理的参数集合
	 * @return double 
	 * 			某个时间点的累计金额
	 * @throws Exception
	 */
	@Override
	public double getTotalAmountByTime(Map<String, Object> map) throws Exception {
		// 开始时间
		String fromTime = (String) map.get("fromTime");
		// 结束时间
		String toTime = (String) map.get("toTime");
		Map<String, Object> searchMap = new HashMap<String, Object>();
		// 组织信息ID
		searchMap.put("organizationInfoId", map.get("organizationInfoId"));
		// 品牌ID
		searchMap.put("brandInfoId", map.get("brandInfoId"));
		// 会员ID
		searchMap.put("memberInfoId", map.get("memberInfoId"));
		// 累计金额
		searchMap.put("recordKbn", DroolsConstants.RECORDKBN_1);
		searchMap.put("fromTime", fromTime);
		searchMap.put("toTime", toTime);
		searchMap.put("memberClubId", map.get("memberClubId"));
		// 开始时间点的累计金额
		double fromAmount = 0;
		// 入会或者升级首单号
		String firstBillId = (String) map.get("firstBillId");
		boolean flag = true;
		if (!CherryChecker.isNullOrEmpty(firstBillId)) {
			searchMap.put("firstBillId", firstBillId);
			// 取得首单金额信息
			Map<String, Object> fromAmountInfo = binbedrcom01_Service.getFromAmountInfo(searchMap);
			if (null != fromAmountInfo && !fromAmountInfo.isEmpty()) {
				flag = false;
				// 首单累加前的累积金额
				String oldValue = (String) fromAmountInfo.get("oldValue");
				// 首单累加后的累积金额
				String newValue = (String) fromAmountInfo.get("newValue");
				// 是否包含首单
				String firstBillKbn = (String) map.get("firstBillKbn");
				// 包含首单
				if ("0".equals(firstBillKbn) && !CherryChecker.isNullOrEmpty(oldValue)) {
					fromAmount = Double.parseDouble(oldValue);
				} else if (!CherryChecker.isNullOrEmpty(newValue)){
					fromAmount = Double.parseDouble(newValue);
				}
			}
		} 
		if (flag) {
			String fromValue = binbedrcom01_Service.getFromNewValue(searchMap);
			if (!CherryChecker.isNullOrEmpty(fromValue)) {
				fromAmount = Double.parseDouble(fromValue);
			}
		}
		return fromAmount;
	}
	
	/**
	 * 取得购买次数
	 * 
	 * @param Map
	 * 			保存处理的参数集合
	 * @return int 
	 * 			购买次数
	 * @throws Exception
	 */
	@Override
	public int getBuyTimes(Map<String, Object> map) throws Exception {
		// 购买次数
		int buyTimes = 0;
		// 取得一段时间内的购买信息
		List<Map<String, Object>> billCodeList = binbedrcom01_Service.getBillList(map);
		if (null != billCodeList && !billCodeList.isEmpty()) {
			// 关联退货单信息
	    	Map<String, Object> srMap = new HashMap<String, Object>();
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
    		// 入会或者升级首单号
    		String firstBillId = (String) map.get("firstBillId");
    		// 是否包含首单
    		String firstBillKbn = (String) map.get("firstBillKbn");
    		// 结束时间
//    		String toTime = (String) map.get("toTime");
//    		Calendar cal1 = null;
//    		if (!CherryChecker.isNullOrEmpty(toTime, true)) {
//    			cal1 = Calendar.getInstance();
//    			cal1.setTime(DateUtil.coverString2Date(toTime, DateUtil.DATETIME_PATTERN));
//    		}
//    		int size = -1;
    		for (int i = 0; i < billCodeList.size(); i++) {
    			Map<String, Object> billCodeInfo = billCodeList.get(i);
//    			// 单据时间
//    			String saleTime = (String) billCodeInfo.get("saleTime");
//    			if (null != cal1) {
//    				Calendar cal12 = Calendar.getInstance();
//    				cal12.setTime(DateUtil.coverString2Date(saleTime, DateUtil.DATETIME_PATTERN));
//    				if (cal12.after(cal1)) {
//    					size = i;
//    					break;
//    				}
//    			}
    			// 单号
    	    	String billCode = (String) billCodeInfo.get("billCode");
    	    	boolean delFlag = false;
    	    	// 不包含首单
    	    	if ("1".equals(firstBillKbn) && billCode.equals(firstBillId)) {
    	    		delFlag = true;
    	    		// 重新计算原单剩余金额
    	    	} else if (srMap.containsKey(billCode)) {
    				// 累计退货金额
    				double srAmount = Double.parseDouble(srMap.get(billCode).toString());
    				// 原单金额
    				double amount = Double.parseDouble(billCodeInfo.get("amount").toString());
    				amount = DoubleUtil.sub(amount, srAmount);
    				if (amount <= 0) {
    					delFlag = true;
    				}
    			}
    	    	if (delFlag) {
    	    		billCodeList.remove(i);
    	    		i--;
    	    	}
    		}
    		buyTimes = billCodeList.size();
		}
		return buyTimes;
	}
	/**
	 * 取得等级MQ消息体(实时)
	 * 
	 * @param campBaseDTO
	 * @throws Exception 
	 */
	@Override
	public MQInfoDTO getLevelMQMessage(CampBaseDTO campBaseDTO) throws Exception{
		
		// 假登录或者等级没有发生变化的场合返回null
		if(1 == campBaseDTO.getMemRegFlg() || campBaseDTO.getCurLevelId() == campBaseDTO.getOldLevelId()) {
			return null;
		}
		// 等级MQ主业务 DTO
		LevelMainDTO levelMainDTO = new LevelMainDTO();
		// 品牌代码
		levelMainDTO.setBrandCode(campBaseDTO.getBrandCode());
		// 单据号
		String ticketNumber = binOLCM03_BL.getTicketNumber(String.valueOf(campBaseDTO.getOrganizationInfoId()),
				String.valueOf(campBaseDTO.getBrandInfoId()), DroolsConstants.CREATED_NAME, DroolsConstants.MQ_BILLTYPE_ML);
		levelMainDTO.setTradeNoIF(ticketNumber);
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
		// 会员卡号
		levelMainDTO.setMemberCode(memCode);
		// 计算时间
		levelMainDTO.setCaltime(campBaseDTO.getCalcDate());

		// 取得会员等级列表
		List<Map<String, Object>> allLevelList = binOLCM31_BL.getAllLevelList(campBaseDTO);
		// 当前等级
		String levelCode = RuleFilterUtil.findLevelCode(campBaseDTO.getCurLevelId(), allLevelList);
		levelMainDTO.setMember_level(levelCode);
		// 柜台号
		levelMainDTO.setCountercode(campBaseDTO.getCounterCode());
		// 员工编号
		levelMainDTO.setBacode(campBaseDTO.getEmployeeCode());
		// 开卡等级
		String grantMemberLevelCode = RuleFilterUtil.findLevelCode(campBaseDTO.getGrantMemberLevel(), allLevelList);
		levelMainDTO.setGrantMemberLevel(grantMemberLevelCode);
		// 上一次等级
		String oldLevelCode = RuleFilterUtil.findLevelCode(campBaseDTO.getOldLevelId(), allLevelList);
		levelMainDTO.setPrevLevel(oldLevelCode);
		// 本次等级变化时间
		levelMainDTO.setLevelAdjustTime(campBaseDTO.getTicketDate());
		// 入会时间调整准则
		String jnDateKbn = binOLCM14_BL.getConfigValue("1076", String.valueOf(campBaseDTO.getOrganizationInfoId()), String.valueOf(campBaseDTO.getBrandInfoId()));
		// 以首单销售为准
		if ("2".equals(jnDateKbn)) {
			// 入会时间
			levelMainDTO.setJoinDate(campBaseDTO.getJoinDate());
		} else {
			// 入会时间
			levelMainDTO.setJoinDate("");
		}
		// 等级MQ明细业务 DTO
		LevelDetailDTO levelDetailDTO = new LevelDetailDTO();
		// 变化类型
		levelDetailDTO.setChangeType(campBaseDTO.getChangeType());
		// 会员卡号
		levelDetailDTO.setMemberCode(campBaseDTO.getMemCode());
		// 业务类型
		levelDetailDTO.setBizType(campBaseDTO.getTradeType());
		// 关联单据时间
		levelDetailDTO.setRelevantTicketDate(campBaseDTO.getTicketDate());
		// 关联单号
		levelDetailDTO.setRelevantNo(campBaseDTO.getBillId());
		// 变动渠道
		levelDetailDTO.setChannel(campBaseDTO.getChannel());
		// 重算次数
		levelDetailDTO.setReCalcCount(DroolsConstants.DEF_MODIFYCOUNTS);
		// 操作类型
		levelDetailDTO.setOperateType(DroolsConstants.OPERATETYPE_I);
		// 柜台号
		levelDetailDTO.setCounterCode(campBaseDTO.getCounterCode());
		// 员工编号
		levelDetailDTO.setEmployeeCode(campBaseDTO.getEmployeeCode());
		// 变更前等级
		levelDetailDTO.setMemberlevelOld(oldLevelCode);
		// 变更后等级
		levelDetailDTO.setMemberlevelNew(levelCode);
		// 变化原因
		String ruleIds = campBaseDTO.getAllRules(DroolsConstants.RECORDKBN_0);
		if (null != ruleIds && !"".equals(ruleIds)) {
			String[] ruleIdArr = ruleIds.split(",");
			Map<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put("ruleIdArr", ruleIdArr);
			// 取得规则内容
			List<Map<String, Object>> ruleContentList = binbedrcom01_Service.getRuleContentList(paramMap);
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
				levelDetailDTO.setReason(reason);
			}
		}
		// 累计金额
		levelDetailDTO.setTotalAmount(String.valueOf(campBaseDTO.getCurTotalAmount()));
		// 发送区分
		String sendKbn = (String) campBaseDTO.getExtArgs().get(DroolsConstants.SEND_RECORDS_KBN);
		List<LevelDetailDTO> levelDetailList = new ArrayList<LevelDetailDTO>();
		if (!DroolsConstants.SEND_HIST_RECORDS.equals(sendKbn)) {
			levelDetailList.add(levelDetailDTO);
		}
		// 发送所有履历
		if (DroolsConstants.SEND_ALL_RECORDS.equals(sendKbn) ||
				DroolsConstants.SEND_HIST_RECORDS.equals(sendKbn)) {
			// 取得所有等级变化明细
			levelDetailList = binOLCM31_BL.getLevelAllRecords(campBaseDTO, levelDetailList);
			if (null == campBaseDTO.getTradeType() && 
					null == levelMainDTO.getCountercode() 
					&& null == levelMainDTO.getBacode()
					&& !levelDetailList.isEmpty()) {
				LevelDetailDTO lastLevelDetail = levelDetailList.get(0);
				levelMainDTO.setCountercode(lastLevelDetail.getCounterCode());
				levelMainDTO.setBacode(lastLevelDetail.getEmployeeCode());
			}
		}
		// 等级MQ明细业务List
		levelMainDTO.setLevelDetailList(levelDetailList);
		// 取得MQ消息体
		String msg = null;
		if (0 == campBaseDTO.getMemberClubId()) {
			msg = levelMainDTO.getMQMsg();
		} else {
			// 会员俱乐部代号
			levelMainDTO.setClubCode(campBaseDTO.getClubCode());
			msg = levelMainDTO.getClubMQMsg();
		}
		// MQ收发日志DTO
		MQInfoDTO mqInfoDTO = new MQInfoDTO();
		// 所属组织
		mqInfoDTO.setOrganizationInfoId(campBaseDTO.getOrganizationInfoId());
		// 所属品牌
		mqInfoDTO.setBrandInfoId(campBaseDTO.getBrandInfoId());
		// 单据类型
		mqInfoDTO.setBillType(DroolsConstants.MQ_BILLTYPE_ML);
		// 单据号
		mqInfoDTO.setBillCode(ticketNumber);
		// 消息发送接收标志位
		mqInfoDTO.setReceiveFlag(DroolsConstants.MQ_RECEIVEFLAG_0);
		// 消息体
		mqInfoDTO.setData(msg);
		// 业务流水
		DBObject dbObject = new BasicDBObject();
		//组织代号
		dbObject.put("OrgCode", campBaseDTO.getOrgCode());
		// 品牌代码，即品牌简称
		dbObject.put("BrandCode", campBaseDTO.getBrandCode());
		// 业务类型
		dbObject.put("TradeType", DroolsConstants.MQ_BILLTYPE_ML);
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
	    dbObject.put("Content", msg);
	    mqInfoDTO.setDbObject(dbObject);
		return mqInfoDTO;
	}
	
	/**
	 * 取得化妆次数MQ消息体(实时)
	 * 
	 * @param campBaseDTO
	 * @throws Exception 
	 */
	@Override
	public MQInfoDTO getBtimesMQMessage(CampBaseDTO campBaseDTO) throws Exception{		
		// 化妆次数没有发生变化的场合返回null
		if(campBaseDTO.getOldBtimes() == campBaseDTO.getCurBtimes()) {
			return null;
		}
		// 化妆次数MQ主业务 DTO
		BTimesMainDTO btimesMainDTO = new BTimesMainDTO();
		// 品牌代码
		btimesMainDTO.setBrandCode(campBaseDTO.getBrandCode());
		// 单据号
		String ticketNumber = binOLCM03_BL.getTicketNumber(String.valueOf(campBaseDTO.getOrganizationInfoId()),
				String.valueOf(campBaseDTO.getBrandInfoId()), DroolsConstants.CREATED_NAME, DroolsConstants.MQ_BILLTYPE_MG);
		btimesMainDTO.setTradeNoIF(ticketNumber);
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
		// 会员卡号
		btimesMainDTO.setMemberCode(memCode);
		// 当前化妆次数
		btimesMainDTO.setCurBtimes(String.valueOf(campBaseDTO.getCurBtimes()));
		// 计算时间
		btimesMainDTO.setCaltime(campBaseDTO.getCalcDate());
		
		// 化妆次数MQ明细业务 DTO
		BTimesDetailDTO btimesDetailDTO = new BTimesDetailDTO();
		// 会员卡号
		btimesDetailDTO.setMemberCode(campBaseDTO.getMemCode());
		// 业务类型
		btimesDetailDTO.setBizType(campBaseDTO.getTradeType());
		// 关联单据时间
		btimesDetailDTO.setRelevantTicketDate(campBaseDTO.getTicketDate());
		// 关联单号
		btimesDetailDTO.setRelevantNo(campBaseDTO.getBillId());
		// 变动渠道
		btimesDetailDTO.setChannel(campBaseDTO.getChannel());
		// 重算次数
		btimesDetailDTO.setReCalcCount(DroolsConstants.DEF_MODIFYCOUNTS);
		// 操作类型
		btimesDetailDTO.setOperateType(DroolsConstants.OPERATETYPE_I);
		// 柜台号
		btimesDetailDTO.setCounterCode(campBaseDTO.getCounterCode());
		// 员工编号
		btimesDetailDTO.setEmployeeCode(campBaseDTO.getEmployeeCode());
		// 当前化妆次数
		int btimes = campBaseDTO.getCurBtimes();
		// 变更前化妆次数
		int oldBtimes = campBaseDTO.getOldBtimes();
		// 化妆次数差分
		int diffBtimes = btimes - oldBtimes;
		// 变更前化妆次数
		btimesDetailDTO.setBtimesOld(String.valueOf(oldBtimes));
		// 变更后化妆次数
		btimesDetailDTO.setCurBtimesNew(String.valueOf(btimes));
		// 化妆次数差分
		btimesDetailDTO.setDiffBtimes(String.valueOf(diffBtimes));
		// 变化原因
		String ruleIds = campBaseDTO.getAllRules(DroolsConstants.RECORDKBN_2);
		if (null != ruleIds && !"".equals(ruleIds)) {
			String[] ruleIdArr = ruleIds.split(",");
			Map<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put("ruleIdArr", ruleIdArr);
			// 取得规则内容
			List<Map<String, Object>> ruleContentList = binbedrcom01_Service.getRuleContentList(paramMap);
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
				btimesDetailDTO.setReason(reason);
			}
		}
		List<BTimesDetailDTO> btimesDetailList = new ArrayList<BTimesDetailDTO>();
		btimesDetailList.add(btimesDetailDTO);
		// 化妆次数MQ明细业务List
		btimesMainDTO.setBtimesDetailList(btimesDetailList);
		// 取得MQ消息体
		String msg = btimesMainDTO.getMQMsg();
		// MQ收发日志DTO
		MQInfoDTO mqInfoDTO = new MQInfoDTO();
		// 所属组织
		mqInfoDTO.setOrganizationInfoId(campBaseDTO.getOrganizationInfoId());
		// 所属品牌
		mqInfoDTO.setBrandInfoId(campBaseDTO.getBrandInfoId());
		// 单据类型
		mqInfoDTO.setBillType(DroolsConstants.MQ_BILLTYPE_MG);
		// 单据号
		mqInfoDTO.setBillCode(ticketNumber);
		// 消息发送接收标志位
		mqInfoDTO.setReceiveFlag(DroolsConstants.MQ_RECEIVEFLAG_0);
		// 消息体
		mqInfoDTO.setData(msg);
		// 业务流水
		DBObject dbObject = new BasicDBObject();
		//组织代号
		dbObject.put("OrgCode", campBaseDTO.getOrgCode());
		// 品牌代码，即品牌简称
		dbObject.put("BrandCode", campBaseDTO.getBrandCode());
		// 业务类型
		dbObject.put("TradeType", DroolsConstants.MQ_BILLTYPE_MG);
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
	    dbObject.put("Content", msg);
	    mqInfoDTO.setDbObject(dbObject);
		return mqInfoDTO;
	}
	
	/**
	 * 取得积分MQ消息体(会员资料上传)
	 * 
	 * @param campBaseDTO
	 * 			会员信息DTO
	 * @param map
	 * 			参数集合
	 * @return MQInfoDTO
	 * 			MQ消息 DTO
	 * @throws Exception 
	 */
	@Override
	public MQInfoDTO getPointMQMessageMB(CampBaseDTO campBaseDTO, Map<String, Object> map) throws Exception{
		Map<String, Object> searchMap = new HashMap<String, Object>();
		// 会员信息ID
		searchMap.put("memberInfoId", campBaseDTO.getMemberInfoId());
		if (0 != campBaseDTO.getMemberClubId()) {
			searchMap.put("memberClubId", campBaseDTO.getMemberClubId());
		}
		// 积分信息
		PointDTO pointDTO = getCurMemPointInfo(searchMap);
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
			// 会员换卡的情况
			if (DroolsConstants.MEM_TYPE_CARD_CHANGE.equals(map.get("subType")) 
					&& memCode.equalsIgnoreCase(campBaseDTO.getMemCode())) {
				// 前卡积分值
				double preCardPoint = pointDTO.getCurTotalPoint();
				pointDTO.setPreCardPoint(preCardPoint);
				Map<String, Object> upMap = new HashMap<String, Object>();
				// 会员信息ID
				upMap.put("memberInfoId", campBaseDTO.getMemberInfoId());
				// 前卡积分值
				upMap.put("preCardPoint", preCardPoint);
				if (0 != campBaseDTO.getMemberClubId()) {
					upMap.put("memberClubId", campBaseDTO.getMemberClubId());
				}
				// 更新前卡积分值
				binOLCM31_BL.updatePreCardPoint(upMap);
 			}
			Map<String, Object> dataLineMap = new HashMap<String, Object>();
			// 主数据行
			Map<String, Object> mainDataMap = new HashMap<String, Object>();
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
			dataLineMap.put("MainData", mainDataMap);
			// 消息体
			String data = binOLCM31_BL.getPointData(CherryUtil.map2Json(dataLineMap));
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
	 * 更新会员信息表（会员等级）
	 * 
	 * @param campBaseDTO
	 * 			会员活动基础 DTO
	 */
	@Override
	public int updMemberInfo(CampBaseDTO campBaseDTO) {
		// 更新会员信息表（会员等级）
		return binbedrcom01_Service.updMemberInfo(campBaseDTO);
	}
	
	/**
	 * 更新会员信息表（会员等级）
	 * 
	 * @param campBaseDTO
	 * 			会员活动基础 DTO
	 */
	@Override
	public int updClubMemberInfo(CampBaseDTO campBaseDTO) {
		// 更新会员信息表（会员等级）
		return binbedrcom01_Service.updClubMemberInfo(campBaseDTO);
	}
	
	/**
	 * 获取单号(BATCH特定业务的单号获取：清零、降级等)
	 * 
	 * @param campBaseDTO
	 * 			会员活动基础 DTO
	 */
	@Override
	public String ticketNumberBatch(CampBaseDTO campBaseDTO) {
		// 业务类型
		String tradeType = campBaseDTO.getTradeType();
		// 业务日期
		String busiDate = campBaseDTO.getBusinessDate();
		// 会员卡号
		String memCode = campBaseDTO.getMemCode();
		// 不存在业务类型、业务日期、会员卡号时，不处理
		if (CherryChecker.isNullOrEmpty(tradeType) 
				|| CherryChecker.isNullOrEmpty(busiDate)
				|| CherryChecker.isNullOrEmpty(memCode)) {
			return null;
		}
		busiDate = busiDate.replaceAll("-", "");
		StringBuffer buffer = new StringBuffer();
		buffer.append(tradeType).append(busiDate).append(memCode);
		return buffer.toString();
	}
	
	/**
	 * 取得原会员卡号
	 * 
	 * @param campBaseDTO
	 * 			会员活动基础 DTO
	 * @return String
	 * 			原会员卡号
	 */
	@Override
	public Map<String, Object> getOldMemCodeInfo(CampBaseDTO campBaseDTO) {
		// 取得原会员卡号
		return binbedrcom01_Service.getOldMemCodeInfo(campBaseDTO);
	}
	
	/**
	 * 取得会员当前的积分信息
	 * @param map
	 * 			查询条件
	 * @return PointDTO
	 * 			会员当前的积分信息
	 */
	@Override
	public PointDTO getCurMemPointInfo(Map<String, Object> map){
		// 取得会员当前的积分信息
		return binbedrcom01_Service.getCurMemPointInfo(map);
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
	public boolean isCurCard(int memberInfoId, String memCard) {
		// 验证会员卡号是否为当前卡
		return binOLCM31_BL.isCurCard(memberInfoId, memCard);
	}
	
	/**
	 * 取得会员所属俱乐部列表
	 * 
	 * @param map
	 * 			查询参数
	 * @return List
	 * 			会员所属俱乐部列表
	 * 
	 */
	@Override
	public List<Map<String, Object>> getMemClubLevelList(Map<String, Object> map) {
		// 取得会员所属俱乐部列表
		return binbedrcom01_Service.getMemClubLevelList(map);
	}
}
