/*	
 * @(#)BINBEMBARC07_BL.java     1.0 2013/12/19
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
package com.cherry.mb.arc.bl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.cm.activemq.dto.MQInfoDTO;
import com.cherry.cm.activemq.interfaces.BINOLMQCOM01_IF;
import com.cherry.cm.cmbussiness.bl.BINOLCM03_BL;
import com.cherry.cm.cmbussiness.interfaces.BINOLCM31_IF;
import com.cherry.cm.core.BatchLoggerDTO;
import com.cherry.cm.core.CherryBatchConstants;
import com.cherry.cm.core.CherryBatchLogger;
import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.util.CherryBatchUtil;
import com.cherry.dr.cmbussiness.dto.mq.LevelDetailDTO;
import com.cherry.dr.cmbussiness.dto.mq.LevelMainDTO;
import com.cherry.dr.cmbussiness.interfaces.BINBEDRCOM01_IF;
import com.cherry.dr.cmbussiness.util.DroolsConstants;
import com.cherry.dr.cmbussiness.util.RuleFilterUtil;
import com.cherry.mb.arc.service.BINBEMBARC07_Service;
import com.cherry.mq.mes.common.MessageConstants;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

/**
 * 珀莱雅会员等级调整 BL
 * 
 * @author hub
 * @version 1.0 2013/12/19
 */
public class BINBEMBARC07_BL {
	
	/** 珀莱雅会员等级调整Service */
	@Resource
	private BINBEMBARC07_Service binBEMBARC07_Service;
	
	private static Logger logger = LoggerFactory.getLogger(BINBEMBARC07_BL.class.getName());
	
	/** BATCH处理标志 */
	private int flag = CherryBatchConstants.BATCH_SUCCESS;

	/** 失败条数 */
	private int failCount = 0;
	
	/** 共通Batch Log处理*/
	private CherryBatchLogger cherryBatchLogger;
	
	/** 共通BatchLogger*/
	private BatchLoggerDTO batchLoggerDTO;
	
	@Resource
	private BINOLCM31_IF binOLCM31_BL;
	
	@Resource
	private BINBEDRCOM01_IF binbedrcom01BL;
	
	/** 取得各种业务类型的单据流水号 */
	@Resource
	private BINOLCM03_BL binOLCM03_BL;
	
	/** 发送MQ消息共通处理 IF */
	@Resource
	private BINOLMQCOM01_IF binOLMQCOM01_BL;
	
	/**
	 * 
	 * 珀莱雅会员等级调整主处理
	 * 
	 * @param map 传入参数
	 * @return int BATCH处理标志
	 * 
	 */
	public int tran_levelCalc(Map<String, Object> map) throws Exception {
		// 共通Batch Log处理
		cherryBatchLogger = new CherryBatchLogger(this.getClass());
		// 共通BatchLogger
		batchLoggerDTO = new BatchLoggerDTO();
		// 将需要处理的会员转移到等级调整履历表的处理开始
		batchLoggerDTO.clear();
		batchLoggerDTO.setCode("IMB00012");
		batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_INFO);
		cherryBatchLogger.BatchLogger(batchLoggerDTO);
		// 是否需要进行履历已存在判断
		if (!"1".equals(map.get("levelCalcFlag"))) {
			// 数据查询长度
			int dataSize = CherryBatchConstants.DATE_SIZE;
			// 数据抽出次数
			int currentNum = 0;
			// 查询开始位置
			int startNum = 0;
			// 查询结束位置
			int endNum = 0;
			// 排序字段
			map.put(CherryBatchConstants.SORT_ID, "memberInfoId");
			while (true) {
				// 查询开始位置
				startNum = dataSize * currentNum + 1;
				// 查询结束位置
				endNum = startNum + dataSize - 1;
				// 数据抽出次数累加
				currentNum++;
				// 查询开始位置
				map.put(CherryBatchConstants.START, startNum);
				// 查询结束位置
				map.put(CherryBatchConstants.END, endNum);
				// 取得需要调整等级的会员List
				List<Map<String, Object>> levelAdjustList = binBEMBARC07_Service.getLevelAdjustList(map);
				// 会员List不为空
				if (!CherryBatchUtil.isBlankList(levelAdjustList)) {
					try {
						// 插入会员等级调整履历表
						binBEMBARC07_Service.addLevelAdjustRecord(levelAdjustList);
						// 提交事务
						binBEMBARC07_Service.manualCommit();
					} catch (Exception e) {
						try {
							// 事务回滚
							binBEMBARC07_Service.manualRollback();
						} catch (Exception ex) {	
							
						}
						logger.error("Members insert Record table exception：" + e.getMessage(),e);
					}
					// 会员少于一次抽取的数量，即为最后一页，跳出循环
					if(levelAdjustList.size() < dataSize) {
						break;
					}
				} else {
					break;
				}
			}
		} else {
			try {
				// 取得需要调整等级的会员List
				List<Map<String, Object>> levelAdjustList = binBEMBARC07_Service.getLevelAdjustIncreList(map);
				if (null != levelAdjustList && !levelAdjustList.isEmpty()) {
					// 插入会员等级调整履历表(增量)
					binBEMBARC07_Service.addLevelAdjustIncreRecord(levelAdjustList);
					// 提交事务
					binBEMBARC07_Service.manualCommit();
				}
			} catch (Exception e) {
				try {
					// 事务回滚
					binBEMBARC07_Service.manualRollback();
				} catch (Exception ex) {	
					
				}
				logger.error("Members insert Record table exception：" + e.getMessage(),e);
			}
		}
		// 将需要处理的会员转移到等级调整履历表的处理结束
		batchLoggerDTO.clear();
		batchLoggerDTO.setCode("IMB00013");
		batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_INFO);
		cherryBatchLogger.BatchLogger(batchLoggerDTO);
		try {
			// 去除执行标识
			binBEMBARC07_Service.updateClearExecFlag(map);
			// 更新执行标识
			binBEMBARC07_Service.updateExecFlag(map);
			// 提交事务
			binBEMBARC07_Service.manualCommit();
		} catch (Exception e) {
			try {
				// 事务回滚
				binBEMBARC07_Service.manualRollback();
			} catch (Exception ex) {	
				
			}
			logger.error("update AdjustStatus exception：" + e.getMessage(),e);
			throw e;
		}
		// 数据查询长度
		int dataSize = CherryBatchConstants.DATE_SIZE;
		// 数据抽出次数
		int currentNum = 0;
		// 查询开始位置
		int startNum = 0;
		// 查询结束位置
		int endNum = 0;
		// 排序字段
		map.put(CherryBatchConstants.SORT_ID, "adjustRecordId");
		// 取得需要处理的会员总数
		int count = binBEMBARC07_Service.getAdjustExecCount(map);
		// 没有需要处理的会员总数
		if (0 == count) {
			// 未新增需要等级调整的会员记录，不执行调整
			batchLoggerDTO.clear();
			batchLoggerDTO.setCode("IMB00014");
			batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_INFO);
			cherryBatchLogger.BatchLogger(batchLoggerDTO);
		} else {
			// 分批取得需要等级调整的会员记录，并处理
			batchLoggerDTO.clear();
			batchLoggerDTO.setCode("IMB00015");
			batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_INFO);
			batchLoggerDTO.addParam(String.valueOf(count));
			cherryBatchLogger.BatchLogger(batchLoggerDTO);
			List<Map<String, Object>> levelList = binOLCM31_BL.getLevelList(map);
			map.put("LEVEL_LIST", levelList);
			// 所属组织
			map.put("organizationInfoID", map.get("organizationInfoId"));
			// 所属品牌
			map.put("brandInfoID", map.get("brandInfoId"));
			// 等级和化妆次数重算
			map.put("reCalcType", DroolsConstants.RECALCTYPE0);
			// 单据日期
			map.put("saleTime", "2014-01-01");
			// 等级调整区分
			map.put("BTLELAD", "1");
			while (true) {
				// 查询开始位置
				startNum = dataSize * currentNum + 1;
				// 查询结束位置
				endNum = startNum + dataSize - 1;
				// 数据抽出次数累加
				currentNum++;
				// 查询开始位置
				map.put(CherryBatchConstants.START, startNum);
				// 查询结束位置
				map.put(CherryBatchConstants.END, endNum);
				// 取得需要调整等级的会员List
				List<Map<String, Object>> levelAdjustList = binBEMBARC07_Service.getAdjustExecList(map);
				// 会员List不为空
				if (!CherryBatchUtil.isBlankList(levelAdjustList)) {
					try {
						// 执行等级调整处理
						executeLevelAdjust(levelAdjustList, map, currentNum);
					} catch (Exception e) {
						logger.error("Level Adjust exception：" + e.getMessage(),e);
					}
					// 会员少于一次抽取的数量，即为最后一页，跳出循环
					if(levelAdjustList.size() < dataSize) {
						break;
					}
				} else {
					break;
				}
			}
			try {
				// 去除执行标识
				binBEMBARC07_Service.updateClearExecFlag(map);
				// 提交事务
				binBEMBARC07_Service.manualCommit();
			} catch (Exception e) {
				try {
					// 事务回滚
					binBEMBARC07_Service.manualRollback();
				} catch (Exception ex) {	
					
				}
				logger.error(e.getMessage(),e);
			}
			// 总件数
			BatchLoggerDTO batchLoggerDTO1 = new BatchLoggerDTO();
			batchLoggerDTO1.setCode("IIF00001");
			batchLoggerDTO1.setLevel(CherryBatchConstants.LOGGER_INFO);
			batchLoggerDTO1.addParam(String.valueOf(count));
			// 成功总件数
			BatchLoggerDTO batchLoggerDTO2 = new BatchLoggerDTO();
			batchLoggerDTO2.setCode("IIF00002");
			batchLoggerDTO2.setLevel(CherryBatchConstants.LOGGER_INFO);
			batchLoggerDTO2.addParam(String.valueOf(count - failCount));
			// 失败件数
			BatchLoggerDTO batchLoggerDTO5 = new BatchLoggerDTO();
			batchLoggerDTO5.setCode("IIF00005");
			batchLoggerDTO5.setLevel(CherryBatchConstants.LOGGER_INFO);
			batchLoggerDTO5.addParam(String.valueOf(failCount));
			CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(this
					.getClass());
			// 处理总件数
			cherryBatchLogger.BatchLogger(batchLoggerDTO1);
			// 成功总件数
			cherryBatchLogger.BatchLogger(batchLoggerDTO2);
			// 失败件数
			cherryBatchLogger.BatchLogger(batchLoggerDTO5);
		}
		return flag;
	}
	
	/**
	 * 执行等级调整处理
	 * 
	 * @param campBaseDTOList 
	 * 			会员信息List
	 * @throws Exception 
	 * 
	 */
	public void executeLevelAdjust(List<Map<String, Object>> levelAdjustList, Map<String, Object> map, int pageNum) throws Exception {
		// 总件数
		int totalCount = levelAdjustList.size();
		// 失败件数
		int adjustFailCount = 0;
		// 系统日期
		String sysDate = binBEMBARC07_Service.getForwardSYSDate();
		for (Map<String, Object> memberInfo : levelAdjustList) {
			int memberInfoId = Integer.parseInt(memberInfo.get("memberInfoId").toString());
			try {
				// BATCH执行标识
				memberInfo.put("BATCH_FLAG", "1");
				// 会员等级列表
				memberInfo.put("LEVEL_LIST", map.get("LEVEL_LIST"));
				// 会员卡号
				String memberCode = (String) memberInfo.get("memberCode");
				if (null == memberCode) {
					// 会员卡号
					memberCode = binbedrcom01BL.getMemCard(map);
					memberInfo.put("memberCode", memberCode);
				}
				// 调整会员等级(珀莱雅)
				int result = binOLCM31_BL.levelAdjust(memberInfo);
				// MQ发送区分
				boolean sendFlag = false;
				// 成功
				int adjustStatus = 2;
				if (0 == result) {
					// 会员ID
					map.put("memberInfoId", memberInfoId);
					// 业务日期
					String businessTime = "2014-01-01 00:00:00";
					// 验证是否需要重算(Batch处理)
					boolean needReCalc = binbedrcom01BL.needReCalcBatch(map);
					if (needReCalc) {
						// 单据日期
						map.put("reCalcTime", businessTime);
						// 保存并发送重算MQ
						binbedrcom01BL.saveAndSendReCalcMsg(map);
						// 成功
						memberInfo.put("adjustStatus", 2);
					} else {
						String levelStr = memberInfo.get("NEW_LEVEL").toString();
						int newLevel = Integer.parseInt(levelStr);
						// 业务日期
						memberInfo.put("businessTime", businessTime);
						// 业务类型
						memberInfo.put("tradeType", MessageConstants.MSG_MEMBER_MS);
						// 设定履历区分为等级
						memberInfo.put("recordKbn", DroolsConstants.RECORDKBN_0);
						// 新等级
						memberInfo.put("newValue", levelStr);
						// 计算时间
						memberInfo.put("calcDate", sysDate);
						// 升级区分
						memberInfo.put("changeType", DroolsConstants.UPKBN_1);
						// 插入规则执行履历表
						binBEMBARC07_Service.addRuleExecRecord(memberInfo);
						// 取得会员等级有效期开始日和结束日
						Map<String, Object> levelDateInfo = binOLCM31_BL.getLevelDateInfo(
								MessageConstants.MSG_MEMBER_MS, businessTime, newLevel, null, memberInfoId);
						if(levelDateInfo != null && !levelDateInfo.isEmpty()) {
							if(!CherryChecker.isNullOrEmpty(levelDateInfo.get("levelStartDate"))) {
								// 等级有效期开始日
								memberInfo.put("levelStartDate", levelDateInfo.get("levelStartDate"));
							}
							if(!CherryChecker.isNullOrEmpty(levelDateInfo.get("levelEndDate"))) {
								// 等级有效期结束日
								memberInfo.put("levelEndDate", levelDateInfo.get("levelEndDate"));
							}
						}
						// 更新会员等级信息
						binBEMBARC07_Service.updateMemberLevelInfo(memberInfo);
						sendFlag = true;
					}
				} else {
					// 不处理
					adjustStatus = 4;
				}
				// 等级调整状态
				memberInfo.put("adjustStatus", adjustStatus);
				// 更新等级调整状态
				binBEMBARC07_Service.updateAdjustStatus(memberInfo);
				// 发送MQ
				if (sendFlag) {
					this.sendLevelMes(memberInfo, map);
				}
				binBEMBARC07_Service.manualCommit();
			} catch (Exception e) {
				adjustFailCount++;
				flag = CherryBatchConstants.BATCH_WARNING;
				try {
					// 事务回滚
					binBEMBARC07_Service.manualRollback();
				} catch (Exception ex) {	
					
				}
				logger.error(e.getMessage(),e);
				batchLoggerDTO.clear();
				// 调整会员等级发生异常
				batchLoggerDTO.setCode("EMB00034");
				// 会员ID
				batchLoggerDTO.addParam(String.valueOf(memberInfoId));
				batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_ERROR);
				cherryBatchLogger.BatchLogger(batchLoggerDTO);
				try {
					// 失败
					memberInfo.put("adjustStatus", 3);
					// 更新等级调整状态
					binBEMBARC07_Service.updateAdjustStatus(memberInfo);
					binBEMBARC07_Service.manualCommit();
				} catch (Exception exp) {
					logger.error(e.getMessage(),e);
					batchLoggerDTO.clear();
					// 标记调整状态为失败时发生异常
					batchLoggerDTO.setCode("EMB00035");
					// 会员ID
					batchLoggerDTO.addParam(String.valueOf(memberInfoId));
					batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_ERROR);
					cherryBatchLogger.BatchLogger(batchLoggerDTO);
				}
			}
		}
		// 本批次会员等级调整完成
		batchLoggerDTO.clear();
		batchLoggerDTO.setCode("IMB00016");
		batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_INFO);
		batchLoggerDTO.addParam(String.valueOf(pageNum));
		batchLoggerDTO.addParam(String.valueOf(totalCount));
		batchLoggerDTO.addParam(String.valueOf(adjustFailCount));
		cherryBatchLogger.BatchLogger(batchLoggerDTO);
	}
	
	/**
	 * 
	 * 发送等级下发MQ消息
	 * 
	 * @param detailMap 初始数据明细信息
	 * @param levelDetailList 等级MQ明细业务数据List
	 * 
	 */
	public void sendLevelMes(Map<String, Object> detailMap, Map<String, Object> map) throws Exception {
		// 假登录
		if (null != detailMap.get("memInfoRegFlg") 
				&& "1".equals(detailMap.get("memInfoRegFlg").toString())) {
			return;
		}
		// 等级MQ明细业务List
		List<LevelDetailDTO> levelDetailList = new ArrayList<LevelDetailDTO>();
		// 等级MQ明细业务 DTO
		LevelDetailDTO levelDetailDTO = new LevelDetailDTO();
		// 变化类型
		levelDetailDTO.setChangeType(DroolsConstants.UPKBN_1);
		// 会员卡号
		String memberCode = (String) detailMap.get("memberCode");
		levelDetailDTO.setMemberCode(memberCode);
		// 业务类型
		levelDetailDTO.setBizType(MessageConstants.MSG_MEMBER_MS);
		// 关联单据时间
		levelDetailDTO.setRelevantTicketDate(String.valueOf(detailMap.get("businessTime")));
		// 关联单号
		levelDetailDTO.setRelevantNo(String.valueOf(detailMap.get("BTbillId")));
		// 变动渠道
		levelDetailDTO.setChannel("Cherry");
		// 重算次数
		levelDetailDTO.setReCalcCount(DroolsConstants.DEF_MODIFYCOUNTS);
		// 操作类型
		levelDetailDTO.setOperateType(DroolsConstants.OPERATETYPE_I);
		// 柜台号
//		levelDetailDTO.setCounterCode("");
//		// 员工编号
//		levelDetailDTO.setEmployeeCode("");
//		// 变更前等级
//		levelDetailDTO.setMemberlevelOld("");
		// 变更后等级
		levelDetailDTO.setMemberlevelNew(String.valueOf(detailMap.get("NEW_CODE")));
		// 变化原因
		levelDetailDTO.setReason(DroolsConstants.LEVEL_ADJUST_RESON);
		// 累计金额
		levelDetailDTO.setTotalAmount(String.valueOf(detailMap.get("TOTAMOUNT")));
		levelDetailList.add(levelDetailDTO);

		LevelMainDTO levelMainDTO = new LevelMainDTO();
		// 品牌代码
		levelMainDTO.setBrandCode((String) map.get("brandCode"));
		// 单据号
		String orgId = String.valueOf(detailMap.get("organizationInfoId"));
		String brandId = String.valueOf(detailMap.get("brandInfoId"));
		String ticketNumber = binOLCM03_BL.getTicketNumber(orgId, 
				brandId, (String) detailMap.get("createdBy"), DroolsConstants.MQ_BILLTYPE_ML);
		levelMainDTO.setTradeNoIF(ticketNumber);
		// 会员卡号
		levelMainDTO.setMemberCode(memberCode);
		// 修改回数
		levelMainDTO.setModifyCounts(DroolsConstants.DEF_MODIFYCOUNTS);
		// 业务类型
		levelMainDTO.setTradeType(DroolsConstants.MQ_BILLTYPE_ML);
		// 当前等级
		levelMainDTO.setMember_level(levelDetailDTO.getMemberlevelNew());
		// 计算时间
		levelMainDTO.setCaltime((String) detailMap.get("calcDate"));
//		// 柜台号
//		levelMainDTO.setCountercode("");
//		// 员工编号
//		levelMainDTO.setBacode("");
		// 开卡等级
		Map<String, Object> paramMap = new HashMap<String, Object>();
		// 开卡等级ID
		paramMap.put("memberLevelId", detailMap.get("grantMemberLevel"));
		// 通过会员等级ID取得会员等级代码
		int graMemLevel = Integer.parseInt(detailMap.get("graMemLevel").toString());
		if (0 != graMemLevel) {
			String grantMemberLevelCode = RuleFilterUtil.findLevelCode(
					graMemLevel, (List<Map<String, Object>>) map.get("LEVEL_LIST"));
			levelMainDTO.setGrantMemberLevel(grantMemberLevelCode);
		}
//		// 上一次等级
//		levelMainDTO.setPrevLevel(levelDetailDTO.getMemberlevelOld());
		// 本次等级变化时间
		levelMainDTO.setLevelAdjustTime(levelDetailDTO.getRelevantTicketDate());
		// 入会时间
		levelMainDTO.setJoinDate("");
		// 等级MQ明细业务List
		levelMainDTO.setLevelDetailList(levelDetailList);
		// MQ消息DTO
		MQInfoDTO mqInfoDTO = new MQInfoDTO();
		// 所属组织
		mqInfoDTO.setOrganizationInfoId(Integer.parseInt(orgId));
		// 所属品牌
		mqInfoDTO.setBrandInfoId(Integer.parseInt(brandId));
		// 单据类型
		mqInfoDTO.setBillType(levelMainDTO.getTradeType());
		// 单据号
		mqInfoDTO.setBillCode(ticketNumber);
		// 消息发送接收标志位
		mqInfoDTO.setReceiveFlag(DroolsConstants.MQ_RECEIVEFLAG_0);
		// 消息体
		mqInfoDTO.setData(levelMainDTO.getMQMsg());
		// 业务流水
		DBObject dbObject = new BasicDBObject();
		//组织代号
		dbObject.put("OrgCode", map.get("orgCode"));
		// 品牌代码，即品牌简称
		dbObject.put("BrandCode", map.get("brandCode"));
		// 业务类型
		dbObject.put("TradeType", DroolsConstants.MQ_BILLTYPE_ML);
		// 单据号
		dbObject.put("TradeNoIF", ticketNumber);
		// 修改次数
		dbObject.put("ModifyCounts", DroolsConstants.DEF_MODIFYCOUNTS);
		// 业务主体
	    dbObject.put("TradeEntity", DroolsConstants.TRADEENTITY_0);
	    // 业务主体代码
	    dbObject.put("TradeEntityCode", levelMainDTO.getMemberCode());
	    // 业务主体名称
	    dbObject.put("TradeEntityName", detailMap.get("memberName"));
	    // 发生时间
	    dbObject.put("OccurTime", levelMainDTO.getCaltime());
	    // 事件内容
	    dbObject.put("Content", mqInfoDTO.getData());
	    mqInfoDTO.setDbObject(dbObject);
		// MQ消息发送处理
		binOLMQCOM01_BL.sendMQMsg(mqInfoDTO);
	}
	
	
}
