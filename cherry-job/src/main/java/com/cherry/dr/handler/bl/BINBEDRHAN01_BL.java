/*	
 * @(#)BINBEDRHAN01_BL.java     1.0 2011/08/26	
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
package com.cherry.dr.handler.bl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;

import com.cherry.cm.activemq.dto.MQInfoDTO;
import com.cherry.cm.activemq.interfaces.BINOLMQCOM01_IF;
import com.cherry.cm.cmbussiness.bl.BINOLCM03_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM14_BL;
import com.cherry.cm.cmbussiness.interfaces.BINOLCM31_IF;
import com.cherry.cm.core.BatchExceptionDTO;
import com.cherry.cm.core.BatchLoggerDTO;
import com.cherry.cm.core.CherryBatchConstants;
import com.cherry.cm.core.CherryBatchException;
import com.cherry.cm.core.CherryBatchLogger;
import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.PropertiesUtil;
import com.cherry.cm.core.TmallKeyDTO;
import com.cherry.cm.core.TmallKeys;
import com.cherry.cm.mongo.MongoDB;
import com.cherry.cm.mongo.domain.MemRuleExecRecord;
import com.cherry.cm.mongo.domain.QMemRuleExecRecord;
import com.cherry.cm.mongo.repositories.MemRuleExecRecordRepository;
import com.cherry.cm.util.Bean2Map;
import com.cherry.cm.util.CherryBatchUtil;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.cm.util.DateUtil;
import com.cherry.cm.util.SignTool;
import com.cherry.dr.cmbussiness.core.CherryDRException;
import com.cherry.dr.cmbussiness.dto.core.CampBaseDTO;
import com.cherry.dr.cmbussiness.dto.core.PointChangeDTO;
import com.cherry.dr.cmbussiness.dto.core.PointChangeDetailDTO;
import com.cherry.dr.cmbussiness.dto.core.PointDTO;
import com.cherry.dr.cmbussiness.dto.mq.BTimesDetailDTO;
import com.cherry.dr.cmbussiness.dto.mq.BTimesMainDTO;
import com.cherry.dr.cmbussiness.dto.mq.LevelDetailDTO;
import com.cherry.dr.cmbussiness.dto.mq.LevelMainDTO;
import com.cherry.dr.cmbussiness.interfaces.BINBEDRCOM01_IF;
import com.cherry.dr.cmbussiness.interfaces.BaseHandler_IF;
import com.cherry.dr.cmbussiness.interfaces.CampRuleBatchExec_IF;
import com.cherry.dr.cmbussiness.interfaces.CampRuleExec_IF;
import com.cherry.dr.cmbussiness.interfaces.RuleEngine_IF;
import com.cherry.dr.cmbussiness.rule.KnowledgeEngine;
import com.cherry.dr.cmbussiness.util.CampRuleUtil;
import com.cherry.dr.cmbussiness.util.DoubleUtil;
import com.cherry.dr.cmbussiness.util.DroolsConstants;
import com.cherry.dr.cmbussiness.util.DroolsMessageUtil;
import com.cherry.dr.cmbussiness.util.RuleFilterUtil;
import com.cherry.dr.handler.service.BINBEDRHAN01_Service;
import com.cherry.mq.mes.bl.BINBEMQMES98_BL;
import com.cherry.mq.mes.common.CherryMQException;
import com.cherry.mq.mes.common.MessageConstants;
import com.cherry.mq.mes.interfaces.AnalyzeMemberInitDataMessage_IF;
import com.cherry.mq.mes.interfaces.CherryMessageHandler_IF;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mysema.query.BooleanBuilder;
import com.taobao.api.request.TmallMeiCrmCallbackPointChangeRequest;
import com.taobao.api.response.TmallMeiCrmCallbackPointChangeResponse;

/**
 * 会员等级和化妆次数重算BL
 * 
 * @author WangCT
 * @version 1.0 2011/08/26	
 */
public class BINBEDRHAN01_BL implements CherryMessageHandler_IF {
	private static Logger logger = LoggerFactory
			.getLogger(BINBEDRHAN01_BL.class.getName());
	
	/** 会员活动共通IF */
	@Resource
	private BINBEDRCOM01_IF binbedrcom01BL;
	
	@Resource
	private KnowledgeEngine knowledgeEngine;
	
	/** 会员等级和化妆次数重算Service */
	@Resource
	private BINBEDRHAN01_Service binBEDRHAN01_Service;
	
	@Resource
	private BINOLCM03_BL binOLCM03_BL;
	
	@Resource
	private BINOLCM31_IF binOLCM31_BL;
	
	@Resource
	private BaseHandler_IF levelPointHandler;
	
	/** 清零处理BL */
	@Resource
	private BaseHandler_IF binBEDRHAN02_BL;
	
	/** 降级处理BL */
	@Resource
	private BaseHandler_IF binBEDRHAN03_BL;
	
	/** 积分清零处理BL */
	@Resource
	private BaseHandler_IF binBEDRHAN04_BL;
	
	/** 发送MQ消息共通处理 IF */
	@Resource
	private BINOLMQCOM01_IF binOLMQCOM01_BL;
	
	/** 管理MQ消息处理器和规则计算处理器共通 BL **/
	@Resource
	private BINBEMQMES98_BL binBEMQMES98_BL;
	
	/** 系统配置项 共通 **/
	@Resource
	private BINOLCM14_BL binOLCM14_BL;
	
	/** 操作MongoDB的会员规则履历表对象 **/
	@Resource
	private MemRuleExecRecordRepository memRuleExecRecordRepository;
	
	@Resource(name="binBEMQMES08_BL")
	private AnalyzeMemberInitDataMessage_IF binBEMQMES08_BL;
	
	@Resource
	private CampRuleExec_IF binbedrjon10BL;
	
	@Resource
	private RuleEngine_IF ruleEngineIF;
	
	/** BATCH处理标志 */
	private int flag = CherryBatchConstants.BATCH_SUCCESS;
	
	/** 是否执行清零处理 */
	private boolean clearFlag = false;
	
	/** 是否执行降级处理 */
	private boolean downFlag = false;
	
	/** 是否执行积分计算处理 */
	private boolean pointFlag = false;
	
	/** 是否执行积分清零处理 */
	private boolean ptClearFlag = false;
	
	/** 是否下发积分清零明细 */
	private boolean pcSendFlag = false;
	
	/** 是否执行积分奖励处理 */
	private boolean ptRewardFlag = false;
	
	/** 是否替换过标识*/
	private boolean isFlagReplace = false;
	
	/** 是否执行积分计算处理 */
	private boolean pointFlagz = false;
	
	/** 是否执行积分清零处理 */
	private boolean ptClearFlagz = false;
	
	/** 是否执行积分奖励处理 */
	private boolean ptRewardFlagz = false;
	
	/** 是否接收天猫订单 */
	private boolean isTmall = false;
	
	/** 是否执行建档处理 */
	private boolean toMemFlag = false;
	
	/** 是否计算升级所需金额处理 */
	private boolean calcUpFlag = false;
	
	/** 是否按照卡号计算等级 */
	private boolean levelFlag = false;
	
	/** 生日修改是否需要重算 */
	private boolean birthFlag = false;
	
	/** 是否按俱乐部计算 */
	private boolean isNoClub = false;
	
	/** 是否按销售累计金额 */
	private boolean istj = false;
	
	/** 验证柜台是否需要执行规则 */
	private boolean ctFlag = false;
	
	/** 计算等级时是否包含初始累计金额 */
	private boolean isInitAmt = false;
	
	/** 是否配置了初始等级日期 */
	private boolean iszdate = false;
	
	/** 等级变更是否自动修改单据*/
	private boolean adjustFlag = false;
	
	/** 入会时间调整准则 */
	private String jnDateKbn = null;
	
	/** 处理总条数 */
	private int totalCount = 0;
	/** 成功条数 */
	private int successCount = 0;
	/** 失败条数 */
	private int failCount = 0;
	/** 所有的规则内容 */
	private Map<String, Object> allRulesMap = new HashMap<String, Object>();
	/** 所有的等级信息 */
	private Map<String, Object> allMemLevelMap = new HashMap<String, Object>();
	
	private List<Map<String, Object>> tmRecallList = null;
	
	/**
	 * 接收MQ消息处理
	 * 
	 * @param msg MQ消息
	 * @throws Exception 
	 */
	@Override
    public void handleMessage(Map<String, Object> map) throws Exception {
    	
    	Map<String, Object> paramMap = new HashMap<String, Object>();
    	paramMap.put("organizationInfoId", map.get("organizationInfoID"));
    	paramMap.put("brandInfoId", map.get("brandInfoID"));
    	paramMap.put("memberInfoId", map.get("memberInfoId"));
    	paramMap.put("orgCode", map.get("orgCode"));
    	paramMap.put("brandCode", map.get("brandCode"));
    	paramMap.put("memberClubId", map.get("memberClubId"));
    	int result = this.tran_reCalcMemLevel(paramMap);
    	if(result != CherryBatchConstants.BATCH_SUCCESS) {
    		throw new CherryMQException(PropertiesUtil.getMessage("EDR00001", new String[]{map.get("memberInfoId").toString()}));
    	}
    	DBObject dbObject = new BasicDBObject();
		// 组织代号
		dbObject.put("OrgCode", map.get("orgCode"));
		// 品牌代码
		dbObject.put("BrandCode", map.get("brandCode"));
		// 业务类型
		dbObject.put("TradeType", map.get("tradeType"));
		// 单据号
		dbObject.put("TradeNoIF", map.get("tradeNoIF"));
		// 修改回数
		dbObject.put("ModifyCounts", map.get("modifyCounts"));
		// 业务主体
		dbObject.put("TradeEntity", "0");
		// 业务主体代号
		dbObject.put("TradeEntityCode", map.get("memberInfoId"));
		// 消息体
		dbObject.put("Content", map.get("messageBody"));
		map.put("dbObject", dbObject);
    }
	
	/**
	 * 
	 * 会员等级和化妆次数重算batch主处理
	 * 
	 * @param map 传入参数
	 * @return BATCH处理标志
	 * 
	 */
	@SuppressWarnings("unchecked")
	public int tran_reCalcMemLevel(Map<String, Object> map) throws Exception {
		// 组织ID
		int organizationInfoId = Integer.parseInt(map.get("organizationInfoId").toString());
		// 品牌ID
		int brandInfoId = Integer.parseInt(map.get("brandInfoId").toString());
		// 刷新所有规则
		knowledgeEngine.refreshAllRule(organizationInfoId, brandInfoId);
		// 查询重算信息
		List<Map<String, Object>> reCalcInfoList = binBEDRHAN01_Service.getReCalcInfo(map);
		tmRecallList = null;
		// 存在重算信息的场合
		if(reCalcInfoList != null && !reCalcInfoList.isEmpty()) {
			// 把相同会员的重算日期合并成一个List，且List中第一个保存重算日期最小的对象
			Map<String, Object> reCalcInfoMap = this.mergerReCalcInfo(reCalcInfoList);
			if(reCalcInfoMap != null && !reCalcInfoMap.isEmpty()) {
				// 取得所有的规则内容
				List<Map<String, Object>> allRuleContent = binBEDRHAN01_Service.getAllRuleContent(map);
				if(allRuleContent != null && !allRuleContent.isEmpty()) {
					for(int i = 0; i < allRuleContent.size(); i++) {
						Map<String, Object> allRuleMap = allRuleContent.get(i);
						allRulesMap.put(allRuleMap.get("ruleId").toString(), allRuleMap.get("ruleContent"));
					}
				}
				// 查询会员等级信息List
				List<Map<String, Object>> memberLevelInfoList = binBEDRHAN01_Service.getMemberLevelInfoList(map);
				if(memberLevelInfoList != null && !memberLevelInfoList.isEmpty()) {
					for(int i = 0; i < memberLevelInfoList.size(); i++) {
						Map<String, Object> allLevelMap = memberLevelInfoList.get(i);
						allMemLevelMap.put(allLevelMap.get("memberLevelId").toString(), allLevelMap);
					}
				}
				// 组织代码
				String orgCode = (String) map.get("orgCode");
				// 品牌代码
				String brandCode = (String) map.get("brandCode");
				// 清零规则处理
				CampRuleExec_IF campRuleExec = binBEMQMES98_BL.getRuleExec(orgCode, brandCode, CherryConstants.HANDLERTYPE_RE05);
				if (null != campRuleExec) {
					clearFlag = true;
				}
				// 降级规则处理
				CampRuleBatchExec_IF campRuleBathExec = binBEMQMES98_BL.getRuleExec(orgCode, brandCode, CherryConstants.HANDLERTYPE_RE07);
				if (null != campRuleBathExec) {
					downFlag = true;
				}
				// 积分规则处理
				CampRuleExec_IF pointRuleExec = binBEMQMES98_BL.getRuleExec(orgCode, brandCode, CherryConstants.HANDLERTYPE_PT01);
				if (null != pointRuleExec) {
					pointFlag = true;
				}
				// 积分规则处理
				CampRuleBatchExec_IF pointClearExec = binBEMQMES98_BL.getRuleExec(orgCode, brandCode, CherryConstants.HANDLERTYPE_PT04);
				if (null != pointClearExec) {
					ptClearFlag = true;
				}
				// 积分规则处理
				CampRuleExec_IF pointRewardExec = binBEMQMES98_BL.getRuleExec(orgCode, brandCode, CherryConstants.HANDLERTYPE_PT05);
				if (null != pointRewardExec) {
					ptRewardFlag = true;
				}
				// 建档处理
				CampRuleExec_IF toMemExec = binBEMQMES98_BL.getRuleExec(orgCode, brandCode, CherryConstants.HANDLERTYPE_RE08);
				if (null != toMemExec) {
					toMemFlag = true;
				}
				// 计算升级所需金额处理
				CampRuleExec_IF campRuleExec01 = binBEMQMES98_BL.getRuleExec(orgCode, brandCode, CherryConstants.HANDLERTYPE_RE01);
				if (null != campRuleExec01) {
					calcUpFlag = true;
				}
				if (TmallKeys.getTmallKeyBybrandCode(brandCode) != null) {
					isTmall = true;
				}
				// 生日修改积分是否重算
				String orgIdStr = String.valueOf(organizationInfoId);
				String brandIdStr = String.valueOf(brandInfoId);
				String birthKbn = binOLCM14_BL.getConfigValue("1107", orgIdStr, brandIdStr);
				birthFlag = "2".equals(birthKbn);;
				// 会员等级计算方式
				String levelCalcKbn = binOLCM14_BL.getConfigValue("1101", orgIdStr, brandIdStr);
				levelFlag = "2".equals(levelCalcKbn);
				// 需要下发积分清零明细
				String psdKbn = binOLCM14_BL.getConfigValue("1118", orgIdStr, brandIdStr);
				pcSendFlag = "2".equals(psdKbn);
				// 入会时间调整准则
				jnDateKbn = binOLCM14_BL.getConfigValue("1076", orgIdStr, brandIdStr);
				// 等级计算时累计金额统计方式
				istj = "2".equals(binOLCM14_BL.getConfigValue("1137", orgIdStr, brandIdStr));
				isNoClub = "3".equals(binOLCM14_BL.getConfigValue("1299", orgIdStr, brandIdStr));
				ctFlag = "1".equals(binOLCM14_BL.getConfigValue("1302", orgIdStr, brandIdStr));
				isInitAmt = "1".equals(binOLCM14_BL.getConfigValue("1305", orgIdStr, brandIdStr));
				adjustFlag =  binOLCM14_BL.isConfigOpen("1375", orgIdStr, brandIdStr);
				// 品牌特殊参数
				String zparams = binOLCM14_BL.getConfigValue("1361", orgIdStr, brandIdStr);
				if (!CherryChecker.isNullOrEmpty(zparams, true)) {
					Map<String, Object> params = CherryUtil.json2Map(zparams.trim());
					String zdate = (String) params.get("zdate");
					if (!CherryChecker.isNullOrEmpty(zdate, true)) {
						iszdate = true;
					}
				}
				for(String memberInfoId : reCalcInfoMap.keySet()) {
					try {
						// 统计总条数
						totalCount++;
						// 取得重新信息
						List<Map<String, Object>> memReCalcInfoList = (List)reCalcInfoMap.get(memberInfoId);
						memberInfoId = memberInfoId.split("_")[0];
						// 取得日期最小的重算信息
						Map<String, Object> reCalcInfo = memReCalcInfoList.get(0);
						Object memberClubId = reCalcInfo.get("memberClubId");
						reCalcInfo.putAll(map);
						reCalcInfo.put("memberClubId", memberClubId);
						reCalcInfo.put("memberInfoId", memberInfoId);
						if(isRecalculation(reCalcInfo)) {
							if ("-9999".equals(memberInfoId)) {
								map.remove("LASTMEMBERID");
								// 取得上一次计算最后的会员ID
								int lastMemberId = getLastMemberId(reCalcInfo);
								if (0 != lastMemberId) {
									map.put("LASTMEMBERID", lastMemberId);
									// 删除重算完的会员ID
									delMemReCalcRecord(reCalcInfo);
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
								map.put(CherryBatchConstants.SORT_ID, "memberInfoId");
								BatchLoggerDTO batchLoggerDTO = new BatchLoggerDTO();
								CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(
										this.getClass());
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
									// 取得会员信息List
									List<Map<String, Object>> allMemberList = binBEDRHAN01_Service.getAllMemberList(map);
									// 会员信息List不为空
									if (!CherryBatchUtil.isBlankList(allMemberList)) {
										List<DBObject> memberList = new ArrayList<DBObject>();
										for (Map<String, Object> memberInfo : allMemberList) {
											DBObject dbObj = new BasicDBObject();
											// 重算ID
											dbObj.put("BIN_ReCalcInfoID", String.valueOf(reCalcInfo.get("reCalcInfoId")));
											// 品牌代码
											dbObj.put("BrandCode", reCalcInfo.get("brandCode"));
											// 组织代码
											dbObj.put("OrgCode", reCalcInfo.get("orgCode"));
											// 会员ID
											dbObj.put("BIN_MemberInfoID", String.valueOf(memberInfo.get("memberInfoId")));
											memberList.add(dbObj);
										}
										MongoDB.insert("MGO_MemReCalcRecord", memberList);
										for (Map<String, Object> memberInfo : allMemberList) {
											// 会员ID
											String memberId = String.valueOf(memberInfo.get("memberInfoId"));
											try {
												reCalcInfo.put("memberInfoId", memberId);
												if (isNoClub || 
														null != reCalcInfo.get("memberClubId") && !"0".equals(reCalcInfo.get("memberClubId").toString())) {
													// 重算单个会员
													reCalcOneMember(reCalcInfo);
												} else {
													// 查询会员俱乐部信息
													List<Map<String, Object>> memberClubList = binBEDRHAN01_Service.getMemberClubList(reCalcInfo);
													if (null == memberClubList || memberClubList.isEmpty() || null == memberClubList.get(0).get("memberClubId")) {
														logger.error("该会员没有业务数据，无需重算。会员ID：" + reCalcInfo.get("memberInfoId"));
														continue;
													}
													for (Map<String, Object> memberClub : memberClubList) {
														reCalcInfo.put("memberClubId", memberClub.get("memberClubId"));
														// 重算单个会员
														reCalcOneMember(reCalcInfo);
													}
												}
												
												// 提交事务
												binBEDRHAN01_Service.manualCommit();
											} catch (Exception e) {
												try {
													// 事务回滚
													binBEDRHAN01_Service.manualRollback();
												} catch (Exception ex) {	
													
												}
												batchLoggerDTO.clear();
												batchLoggerDTO.setCode("EDR00020");
												// 会员ID
												batchLoggerDTO.addParam(memberId);
												batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_ERROR);
												cherryBatchLogger.BatchLogger(batchLoggerDTO, e);
												// 重算日期
												String reCalcDate = (String) reCalcInfo.get("reCalcDate");
												// 记录到重算表中
												try {
													Map<String, Object> insertMap = new HashMap<String, Object>();
													// 所属组织
													insertMap.put("organizationInfoID", organizationInfoId);
													// 所属品牌
													insertMap.put("brandInfoID", brandInfoId);
													// 会员ID
													insertMap.put("memberInfoId", memberId);
													// 会员俱乐部ID
													Object clubId = reCalcInfo.get("memberClubId");
													if (null != clubId 
															&& 0 != Integer.parseInt(clubId.toString())) {
														insertMap.put("memberClubId", clubId);
													}
													// 等级和化妆次数重算
													insertMap.put("reCalcType", DroolsConstants.RECALCTYPE0);
													// 重算日期
													insertMap.put("reCalcDate", reCalcDate);
													// 插入重算信息表
													binbedrcom01BL.insertReCalcInfo(insertMap);
													// 提交事务
													binBEDRHAN01_Service.manualCommit();
												} catch (Exception exc) {
													try {
														// 事务回滚
														binBEDRHAN01_Service.manualRollback();
													} catch (Exception ex) {	
														
													}
													batchLoggerDTO.setCode("EDR00015");
													// 会员ID
													batchLoggerDTO.addParam(memberId);
													// 重算日期
													batchLoggerDTO.addParam(reCalcDate);
													batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_ERROR);
													cherryBatchLogger.BatchLogger(batchLoggerDTO, exc);
												}
												flag = CherryBatchConstants.BATCH_WARNING;
											}
										}
										// 删除重算完的会员ID
										delMemReCalcRecord(reCalcInfo);
										// 会员信息少于一次抽取的数量，即为最后一页，跳出循环
										if(allMemberList.size() < dataSize) {
											break;
										}
									} else {
										break;
									}
								}
							} else {
								if (isNoClub || 
										null != reCalcInfo.get("memberClubId") && !"0".equals(reCalcInfo.get("memberClubId").toString())) {
									// 重算单个会员
									reCalcOneMember(reCalcInfo);
								} else {
									// 查询会员俱乐部信息
									List<Map<String, Object>> memberClubList = binBEDRHAN01_Service.getMemberClubList(reCalcInfo);
									if (null == memberClubList || memberClubList.isEmpty() || null == memberClubList.get(0).get("memberClubId")) {
										logger.error("该会员没有业务数据，无需重算。会员ID：" + reCalcInfo.get("memberInfoId"));
									} else {
										for (Map<String, Object> memberClub : memberClubList) {
											reCalcInfo.put("memberClubId", memberClub.get("memberClubId"));
											// 重算单个会员
											reCalcOneMember(reCalcInfo);
										}
									}
								}
							}
						}
						try {
							// 更新者
							reCalcInfo.put(CherryBatchConstants.UPDATEDBY, CherryBatchConstants.UPDATE_NAME);
							// 更新程序名
							reCalcInfo.put(CherryBatchConstants.UPDATEPGM, "BINBEDRHAN01");
							// 删除重算信息
							binBEDRHAN01_Service.delReCalcInfo(memReCalcInfoList);
						} catch (Exception e) {
							BatchExceptionDTO batchExceptionDTO = new BatchExceptionDTO();
							batchExceptionDTO.setBatchName(this.getClass());
							batchExceptionDTO.setErrorCode("EDR00003");
							batchExceptionDTO.setErrorLevel(CherryBatchConstants.LOGGER_ERROR);
							// 会员ID
							batchExceptionDTO.addErrorParam(memberInfoId);
							batchExceptionDTO.setException(e);
							throw new CherryBatchException(batchExceptionDTO);
						}
						// 统计成功件数
						successCount++;
						// Cherry数据库提交事务
						binBEDRHAN01_Service.manualCommit();
						if (null != tmRecallList && !tmRecallList.isEmpty()) {
							for (Map<String, Object> tmRecallInfo : tmRecallList) {
								Long recordId = null;
								try {
									recordId = Long.parseLong(tmRecallInfo.get("recordId").toString());
									tmRecallInfo.put("updatedBy", "BINBEDRHAN01");
									tmRecallInfo.put("updatePGM", "BINBEDRHAN01");
									tmRecallInfo.put("tmallRecallFlag", 1);
									binBEDRHAN01_Service.updateTMUsedInfo(tmRecallInfo);
									String errCode = (String) tmRecallInfo.get("tmErrCode");
									String mixMobile = (String) tmRecallInfo.get("mixMobile");
									callbackTmall(mixMobile, recordId, errCode, (String) map.get("brandCode"));
								} catch (Exception e) {
									try {
										tmRecallInfo.put("tmallRecallFlag", 2);
										binBEDRHAN01_Service.updateTMUsedInfo(tmRecallInfo);
									} catch (Exception ex) {
										logger.error("******************************标记回调失败记录发生异常，记录ID:" + recordId);
										logger.error(ex.getMessage(),ex);
									}
									logger.error("******************************回调天猫积分处理结果发生异常，记录ID:" + recordId);
									logger.error(e.getMessage(),e);
								} finally {
									try {
										binBEDRHAN01_Service.manualCommit();
									} catch (Exception ex) {
										logger.error("******************************提交事务失败，记录ID:" + recordId);
										logger.error(ex.getMessage(),ex);
									}
								}
							}
						}
					} catch (CherryBatchException cherryBatchException) {
						try {
							// Cherry数据库回滚事务
							binBEDRHAN01_Service.manualRollback();
						} catch (Exception ex) {
							
						}
						// 失败件数加一
						failCount++;
						flag = CherryBatchConstants.BATCH_WARNING;
					} catch (Exception e) {
						try {
							// Cherry数据库回滚事务
							binBEDRHAN01_Service.manualRollback();
						} catch (Exception ex) {
							
						}
						// 失败件数加一
						failCount++;
						flag = CherryBatchConstants.BATCH_WARNING;
						BatchLoggerDTO batchLoggerDTO = new BatchLoggerDTO();
						batchLoggerDTO.setCode("EDR00001");
						// 会员ID
						batchLoggerDTO.addParam(memberInfoId);
						batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_ERROR);
						CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(this.getClass());
						cherryBatchLogger.BatchLogger(batchLoggerDTO, e);
					} catch (Throwable t) {
						try {
							// Cherry数据库回滚事务
							binBEDRHAN01_Service.manualRollback();
						} catch (Exception ex) {
							
						}
						// 失败件数加一
						failCount++;
						flag = CherryBatchConstants.BATCH_WARNING;
					}
				}
			}
			
		}
		
		// 处理总件数
		BatchLoggerDTO batchLoggerDTO1 = new BatchLoggerDTO();
		batchLoggerDTO1.setCode("IIF00001");
		batchLoggerDTO1.setLevel(CherryBatchConstants.LOGGER_INFO);
		batchLoggerDTO1.addParam(String.valueOf(totalCount));
		// 成功件数
		BatchLoggerDTO batchLoggerDTO2 = new BatchLoggerDTO();
		batchLoggerDTO2.setCode("IIF00002");
		batchLoggerDTO2.setLevel(CherryBatchConstants.LOGGER_INFO);
		batchLoggerDTO2.addParam(String.valueOf(successCount));
		// 失败件数
		BatchLoggerDTO batchLoggerDTO3 = new BatchLoggerDTO();
		batchLoggerDTO3.setCode("IIF00005");
		batchLoggerDTO3.setLevel(CherryBatchConstants.LOGGER_INFO);
		batchLoggerDTO3.addParam(String.valueOf(failCount));
		
		CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(this.getClass());
		// 处理总件数
		cherryBatchLogger.BatchLogger(batchLoggerDTO1);
		// 成功件数
		cherryBatchLogger.BatchLogger(batchLoggerDTO2);
		// 失败件数
		cherryBatchLogger.BatchLogger(batchLoggerDTO3);
		return flag;
	}
	/**
	 * 取得上一次计算最开始的会员ID
	 * 
	 * @param reCalcInfo 重算信息
	 * @return int 上一次计算最后的会员ID
	 * 
	 */
	private int getLastMemberId(Map<String, Object> reCalcInfo) throws Exception {
		// 查询条件
		DBObject query = new BasicDBObject();
		// 重算ID
		query.put("BIN_ReCalcInfoID", String.valueOf(reCalcInfo.get("reCalcInfoId")));
		// 品牌代码
		query.put("BrandCode", reCalcInfo.get("brandCode"));
		// 组织代码
		query.put("OrgCode", reCalcInfo.get("orgCode"));
		// 排序条件
		DBObject dbSort = new BasicDBObject();
		// 会员ID升序
		dbSort.put("BIN_MemberInfoID", 1);
		// 查询结果
		DBObject result = new BasicDBObject();
		result.put("BIN_MemberInfoID", 1);
		List<DBObject> memberList = MongoDB.find("MGO_MemReCalcRecord", query, result, dbSort, 0, 1);
		if (null != memberList && !memberList.isEmpty()) {
			DBObject member = memberList.get(0);
			Object memberIdObj = member.get("BIN_MemberInfoID");
			if (null != memberIdObj) {
				return Integer.parseInt(memberIdObj.toString());
			}
		}
		return 0;
	}
	
	/**
	 * 删除重算完的会员ID
	 * 
	 * @param reCalcInfo 重算信息
	 * 
	 */
	private void delMemReCalcRecord(Map<String, Object> reCalcInfo) throws Exception {
		// 查询条件
		DBObject query = new BasicDBObject();
		// 重算ID
		query.put("BIN_ReCalcInfoID", String.valueOf(reCalcInfo.get("reCalcInfoId")));
		// 品牌代码
		query.put("BrandCode", reCalcInfo.get("brandCode"));
		// 组织代码
		query.put("OrgCode", reCalcInfo.get("orgCode"));
		// 删除重算完的会员ID
		MongoDB.removeAll("MGO_MemReCalcRecord", query);
	}
	
	/**
	 * 重算单个会员
	 * 
	 * @param reCalcInfo 重算信息
	 */
	private void reCalcOneMember(Map<String, Object> reCalcInfo) throws Exception {
		if (isFlagReplace) {
			pointFlag = pointFlagz;
			ptClearFlag = ptClearFlagz;
			ptRewardFlag = ptRewardFlagz;
			isFlagReplace = false;
		}
		int clubId = 0;
		if (null != reCalcInfo.get("memberClubId")) {
			clubId = Integer.parseInt(reCalcInfo.get("memberClubId").toString());
			if (0 == clubId) {
				reCalcInfo.remove("memberClubId");
			}
		}
		String orgCode = (String) reCalcInfo.get("orgCode");
		String brandCode = (String) reCalcInfo.get("brandCode");
		if (clubId > 0) {
			// 取得组规则库
			Map<String, Object> groupRule = ruleEngineIF.getGroupRule(orgCode, 
					brandCode,  DroolsConstants.CAMPAIGN_TYPE1 + "_" + clubId);
			if (null == groupRule || groupRule.isEmpty()) {
				return;
			}
		}
		// 查询会员信息
		Map<String, Object> memberInfoMap = binBEDRHAN01_Service.getMemberInfo(reCalcInfo);
		// 会员不存在不进行重算
		if(memberInfoMap == null) {
			return;
		}
		int oldLevel = 0;
		if (isTmall || adjustFlag) {
			// 重算前等级
			oldLevel = Integer.parseInt(String.valueOf(memberInfoMap.get("membLevel")));
		}
		String counterBelong = (String) memberInfoMap.get("counterCbl");
		if (pointFlag && !CherryChecker.isNullOrEmpty(counterBelong)) {
			String noExecCounters = TmallKeys.getNoExecCounters(brandCode);
			if (!CherryChecker.isNullOrEmpty(noExecCounters) 
					&& ConvertUtil.isContain(noExecCounters.split(","), counterBelong))  {
				pointFlagz = pointFlag;
				ptClearFlagz = ptClearFlag;
				ptRewardFlagz = ptRewardFlag;
				ptRewardFlag = ptClearFlag = pointFlag = false;
				isFlagReplace = true;
			}
		}
		if (ctFlag) {
			Map<String, Object> checkMap = new HashMap<String, Object>();
			checkMap.put("organizationInfoID", reCalcInfo.get("organizationInfoId"));
			checkMap.put("brandInfoID", reCalcInfo.get("brandInfoId"));
			checkMap.put("brandCode", reCalcInfo.get("brandCode"));
			checkMap.put("orgCode", orgCode);
			checkMap.put("memberInfoId", reCalcInfo.get("memberInfoId"));
			checkMap.put("counterCode", memberInfoMap.get("counterCbl"));
			//  验证柜台是否需要执行规则
			if (!binOLCM31_BL.isRuleCounter(checkMap)) {
				return;
			}
		}
		CampBaseDTO campBaseDTO = new CampBaseDTO();
		// 会员卡号
		String memCard = (String)memberInfoMap.get("memCode");
		// 会员卡号
		campBaseDTO.setMemCode(memCard);
		// 等级状态
		String levStatus = (String) memberInfoMap.get("levStatus");
		if (isNoClub) {
			// 实际等级
			if (CherryConstants.LEVELSTATUS_2.equals(levStatus)) {
				// 会员当前等级
				int memberLevel = Integer.parseInt(memberInfoMap.get("membLevel").toString());
				if (toMemFlag) {
					memberLevel = getKeepId();
				}
				if (0 != memberLevel) {
					// 需要保级
					if (isKeepLevel(String.valueOf(memberLevel), campBaseDTO)) {
						campBaseDTO.getExtArgs().put(DroolsConstants.KEEP_LEVELID, memberLevel);
					}
				}
			}
		}
		if (null != memberInfoMap.get("initTotalAmount")) {
			campBaseDTO.setInitAmount(Double.parseDouble(memberInfoMap.get("initTotalAmount").toString()));
		}
		// 会员信息ID
		String memberInfoId = String.valueOf(reCalcInfo.get("memberInfoId"));
		int memInfoId = Integer.parseInt(memberInfoId);
		// 推荐会员ID
		int referrerId = Integer.parseInt(memberInfoMap.get("referrerId").toString());
		campBaseDTO.setReferrerId(referrerId);
		// 入会日期
		campBaseDTO.setJoinDate((String)memberInfoMap.get("joinDate"));
		// 入会时间
		campBaseDTO.setJoinTime((String)memberInfoMap.get("joinTime"));
		// 微信绑定时间
		campBaseDTO.setWechatBindTime((String)memberInfoMap.get("wechatBindTime"));
		// 会员生日
		String birthday = (String) memberInfoMap.get("memBirthday");
		if (null != birthday && birthday.length() == 8) {
			// 生日为19000101时是否作为无值处理
			if ("19000101".equals(birthday) &&
					binOLCM14_BL.isConfigOpen("1344", String.valueOf(reCalcInfo.get("organizationInfoId")), String.valueOf(reCalcInfo.get("brandInfoId")))) {
				birthday = null;
			} else {
				birthday = birthday.substring(0,4) + "-" + birthday.substring(4,6) + "-" + birthday.substring(6,8);
			}
			campBaseDTO.setBirthday(birthday);
			if (birthFlag) {
				// 取得会员生日变更履历
				List<Map<String, Object>> birthModyList = binOLCM31_BL.getBirthModyList(reCalcInfo);
				if (null != birthModyList && !birthModyList.isEmpty()) {
					reCalcInfo.put("BirthModyList", birthModyList);
				}
			}
		}
		// 入会途径
		campBaseDTO.setChannelCode((String) memberInfoMap.get("channelCode"));
		// 首单时间
		campBaseDTO.setFirstTicketTime(binBEDRHAN01_Service.getFirstTicketTime(reCalcInfo));
		// 会员名称
		campBaseDTO.setMemName((String)memberInfoMap.get("memberName"));
		// 入会时会员等级
		if(memberInfoMap.get("grantMemberLevel") != null) {
			campBaseDTO.setGrantMemberLevel((Integer)memberInfoMap.get("grantMemberLevel"));
		}
		// 会员登记区分
		int memRegFlg = Integer.parseInt(memberInfoMap.get("memRegFlg").toString());
		campBaseDTO.setMemRegFlg(memRegFlg);
		// 所属组织ID
		campBaseDTO.setOrganizationInfoId(Integer.parseInt(reCalcInfo.get("organizationInfoId").toString()));
		// 所属品牌ID
		campBaseDTO.setBrandInfoId(Integer.parseInt(reCalcInfo.get("brandInfoId").toString()));
		// 组织代码
		campBaseDTO.setOrgCode((String)reCalcInfo.get("orgCode"));
		// 品牌代码
		campBaseDTO.setBrandCode((String)reCalcInfo.get("brandCode"));
		// 作成者
		campBaseDTO.setCreatedBy(CherryBatchConstants.UPDATE_NAME);
		// 作成程序名
		campBaseDTO.setCreatePGM("BINBEDRHAN01");
		// 更新者
		campBaseDTO.setUpdatedBy(CherryBatchConstants.UPDATE_NAME);
		// 更新程序名
		campBaseDTO.setUpdatePGM("BINBEDRHAN01");
		// 重算区分设定为1表示重算
		campBaseDTO.setReCalcFlg(1);
		// 计算日期
		campBaseDTO.setCalcDate(binBEDRHAN01_Service.getForwardSYSDate());
		// 会员信息ID
		campBaseDTO.setMemberInfoId(memInfoId);
		// 理由
		campBaseDTO.setReason(4);
		// 规则日志
		String ruleLog = (String) campBaseDTO.getExtArgs().get("RULELOG");
		if (null == ruleLog || "".equals(ruleLog)) {
			// 规则日志
			ruleLog = binOLCM14_BL.getConfigValue("1065", String.valueOf(campBaseDTO.getOrganizationInfoId()), String.valueOf(campBaseDTO.getBrandInfoId()));
			campBaseDTO.getExtArgs().put("RULELOG", ruleLog);
		}
		// 是否打印日志
		boolean isLog = RuleFilterUtil.isRuleLog(campBaseDTO);
		// 开始时间
		long bfstartTime = 0;
		// 结束时间
		long bfendTime = 0;
		// 结束时间
		long aftendTime = 0;
		if (isLog) {
			bfstartTime = System.currentTimeMillis();
		}
		boolean isbd = false;
		boolean initFlag = false;
		// 重算日期
		String reCalcDate = (String)reCalcInfo.get("reCalcDate");
		if (isNoClub) {
			if (isTmall) {
				Map<String, Object> searchMap = new HashMap<String, Object>();
				searchMap.put("memberInfoId", reCalcInfo.get("memberInfoId"));
				searchMap.put("reCalcDate", reCalcInfo.get("reCalcDate"));
				Map<String, Object> timeMap = binBEDRHAN01_Service.getMinMultiTime(searchMap);
				if (null != timeMap && !timeMap.isEmpty()
						&& !CherryChecker.isNullOrEmpty(timeMap.get("ticketDate"))) {
					reCalcDate = timeMap.get("ticketDate") + ".000";
					reCalcInfo.put("reCalcDate", reCalcDate);
				}
			}
			// 初始采集日期
			String initialDate = (String)memberInfoMap.get("initialDate");
			String reCalcDay = reCalcDate.substring(0,10);
			// 存在初始采集日期而且重算日期不大于初始采集日期的场合，把重算日期改为初始采集日期
			if(initialDate != null && reCalcDay.compareTo(initialDate) <= 0) {
				reCalcDate = initialDate;
				reCalcInfo.put("reCalcDate", reCalcDate);
				initFlag = true;
			}
			// 品牌代码
			String braCode = (String) reCalcInfo.get("brandCode");
			if (null != braCode) {
				braCode = braCode.trim();
				if ("ply".equalsIgnoreCase(braCode)) {
					if (CherryChecker.compareDate(reCalcDay, "2014-01-01") < 0) {
						// 调整会员等级(珀莱雅)
						binOLCM31_BL.levelAdjust(reCalcInfo);
					}
				} else if ("ysl".equalsIgnoreCase(braCode)) {
					campBaseDTO.getExtArgs().put("BDKBN", "1");
					isbd = true;
				}
			}
		}
		int initClubLevel = 0;
		if (0 != clubId) {
			campBaseDTO.setMemberClubId(clubId);
			// 查询会员俱乐部信息
			Map<String, Object> clubMap = binBEDRHAN01_Service.selMemClubInfo(reCalcInfo);
			if (null == clubMap || clubMap.isEmpty()) {
				BatchExceptionDTO batchExceptionDTO = new BatchExceptionDTO();
				batchExceptionDTO.setBatchName(this.getClass());
				batchExceptionDTO.setErrorCode("EDR00055");
				batchExceptionDTO.setErrorLevel(CherryBatchConstants.LOGGER_ERROR);
				// 会员ID
				batchExceptionDTO.addErrorParam(memberInfoId);
				throw new CherryBatchException(batchExceptionDTO);
			}
			// 俱乐部代号
			campBaseDTO.setClubCode((String) clubMap.get("clubCode"));
			// 查询会员俱乐部等级信息
			Map<String, Object> clubLevelMap = binBEDRHAN01_Service.getMemClubLevelInfo(reCalcInfo);
			if (null != clubLevelMap && !clubLevelMap.isEmpty()) {
				// 初始采集日期
				String initialDate = (String)clubLevelMap.get("initialDate");
				String reCalcDay = reCalcDate.substring(0,10);
				// 存在初始采集日期而且重算日期不大于初始采集日期的场合，把重算日期改为初始采集日期
				if(initialDate != null && reCalcDay.compareTo(initialDate) <= 0) {
					reCalcDate = initialDate;
					reCalcInfo.put("reCalcDate", reCalcDate);
					initClubLevel = Integer.parseInt(String.valueOf(clubLevelMap.get("initialMemLevel")));
					initFlag = true;
				}
				// 当前等级
				campBaseDTO.setMemberLevel(Integer.parseInt(clubLevelMap.get("membLevel").toString()));
				// 入会时会员等级
				campBaseDTO.setGrantMemberLevel(Integer.parseInt(clubLevelMap.get("grantMemberLevel").toString()));
				// 俱乐部等级ID
				campBaseDTO.setMemClubLeveId(Integer.parseInt(clubLevelMap.get("clubLevelId").toString()));
			} else {
				// 当前等级
				campBaseDTO.setMemberLevel(0);
				// 入会时会员等级
				campBaseDTO.setGrantMemberLevel(0);
			}
		}
		// 重算日期
		campBaseDTO.setTicketDate(reCalcDate);
		// 存在初始采集日期而且重算日期不大于初始采集日期的场合，会员的基准点即为初始采集时的状态
		if(initFlag) {
			// 初始采集等级
			int initialMemLevelId = 0;
			if (isNoClub) {
				Object initialMemLevel = memberInfoMap.get("initialMemLevel");
				if (null != initialMemLevel) {
					initialMemLevelId = Integer.parseInt(initialMemLevel.toString());
				}
			} else {
				initialMemLevelId = initClubLevel;
			}
			// 查询会员初始金额信息
//			Map<String, Object> memExtInitInfo = binBEDRHAN01_Service.getMemExtInitInfo(campBaseDTO);
//			if (null != memExtInitInfo && !memExtInitInfo.isEmpty()) {
//				// 初始累计金额
//				if (null != memExtInitInfo.get("initTotalAmount")) {
//					campBaseDTO.setCurTotalAmount(Double.parseDouble(memExtInitInfo.get("initTotalAmount").toString()));
//				}
//			}
			if (isInitAmt && 0 != campBaseDTO.getInitAmount()) {
				campBaseDTO.setCurTotalAmount(campBaseDTO.getInitAmount());
			}
			// 基准点的会员等级
			campBaseDTO.setCurLevelId(initialMemLevelId);
			if (0 != initialMemLevelId) {
				// 会员等级状态
				campBaseDTO.setLevelStatus(CherryConstants.LEVELSTATUS_2);
			}
			// 升级前会员等级
			campBaseDTO.setUpgradeFromLevel(null);
			// 会员等级调整日
			campBaseDTO.setLevelAdjustDay(null);
			// 等级升降级区分
			campBaseDTO.setLevelChangeType(null);
			// 入会或者升级首单号
			campBaseDTO.setFirstBillId(null);
//			if (isbd) {
//				// 等级有效期开始日
//				campBaseDTO.setLevelStartDate((String) memberInfoMap.get("initStartDate"));
//				// 等级有效期结束日
//				campBaseDTO.setLevelEndDate((String) memberInfoMap.get("initEndDate"));
//			} else {
				if (istj) {
					// 等级有效期开始日
					campBaseDTO.setLevelStartDate((String) memberInfoMap.get("initStartDate"));
					// 等级有效期结束日
					campBaseDTO.setLevelEndDate((String) memberInfoMap.get("initEndDate"));
				} else {
					// 等级有效期开始日
					campBaseDTO.setLevelStartDate(null);
					// 等级有效期结束日
					campBaseDTO.setLevelEndDate(null);
				}
//			}
		} else {
			// 是否从MongoDB取基准点判断条件
			String moveMongoDBFlag = (String)reCalcInfo.get("moveMongoDBFlag");
			// 根据履历区分设定基准点的会员等级、累积金额、化妆次数、积分、可兑换金额
			// 设定基准点的等级
			campBaseDTO.setRecordKbn(DroolsConstants.RECORDKBN_0);
			this.getReferenceRecord(campBaseDTO, moveMongoDBFlag);
			// 设定基准点的累积金额
			campBaseDTO.setRecordKbn(DroolsConstants.RECORDKBN_1);
			this.getReferenceRecord(campBaseDTO, moveMongoDBFlag);
			// 设定基准点的化妆次数
			campBaseDTO.setRecordKbn(DroolsConstants.RECORDKBN_2);
			this.getReferenceRecord(campBaseDTO, moveMongoDBFlag);
			// 设定基准点的积分
			campBaseDTO.setRecordKbn(DroolsConstants.RECORDKBN_3);
			this.getReferenceRecord(campBaseDTO, moveMongoDBFlag);
			// 设定基准点的可兑换金额
			campBaseDTO.setRecordKbn(DroolsConstants.RECORDKBN_4);
			this.getReferenceRecord(campBaseDTO, moveMongoDBFlag);
		}
		// 重算前总积分
		double befTotalPoint = 0;
		// 是否进行过初始积分导入
		boolean isInitPt = false;
		// 初始导入时间
		String initPtTime = null;
		if (pointFlag) {
			boolean pointInitFlag = false;
			// 初始导入积分
			double initialPoint = 0;
			// 初始导入可兑换积分
			double initChangablePoint = 0;
			// 初始导入累计兑换积分
			double initTotalChanged = 0;
			// 初始导入时间
			String initialTime = null;
		//	if (isNoClub) {
				// 取得会员初始积分信息
				Map<String, Object> memPointInitInfo = null;
				if (clubId == 0) {
					memPointInitInfo = binOLCM31_BL.getMemPointInitInfo(memInfoId);
				} else {
					memPointInitInfo = binOLCM31_BL.getClubMemPointInitInfo(memInfoId, clubId);
				}
				if (null != memPointInitInfo && !memPointInitInfo.isEmpty()) {
					befTotalPoint = Double.parseDouble(memPointInitInfo.get("beftPoint").toString());
					// 初始导入时间
					initialTime = (String) memPointInitInfo.get("initialTime");
					if (!CherryChecker.isNullOrEmpty(initialTime)) {
						isInitPt = true;
						initPtTime = initialTime;
						Calendar cal1 = Calendar.getInstance();
						String reCalcTime = reCalcDate;
						if (CherryChecker.checkDate(reCalcTime)) {
							reCalcTime = reCalcTime + " 00:00:00";
						} else {
							int index = reCalcTime.indexOf(".");
							if (index > 0) {
								reCalcTime = reCalcDate.substring(0, index);
							}
						}
						cal1.setTime(DateUtil.coverString2Date(reCalcTime, DateUtil.DATETIME_PATTERN));
						Calendar cal2 = Calendar.getInstance();
						cal2.setTime(DateUtil.coverString2Date(initialTime, DateUtil.DATETIME_PATTERN));
						// 比较单据时间是否在初始导入时间之前
						if (cal1.before(cal2)) {
							pointInitFlag = true;
						}
						campBaseDTO.getExtArgs().put("INITIALTIME", initialTime);
					}
					initialPoint = Double.parseDouble(memPointInitInfo.get("initialPoint").toString());
					initChangablePoint = Double.parseDouble(memPointInitInfo.get("initChangablePoint").toString());
					initTotalChanged = Double.parseDouble(memPointInitInfo.get("initTotalChanged").toString());
				}
		//	}
			PointDTO pointInfo = new PointDTO();
			if (!isNoClub) {
				pointInfo.setMemberClubId(campBaseDTO.getMemberClubId());
				pointInfo.setClubIdStr(String.valueOf(pointInfo.getMemberClubId()));
			}
			boolean recalDateFlag = true;
			// 还原基准点的积分为初始导入的积分值
			if (pointInitFlag) {
				// 积分 DTO
				campBaseDTO.setPointInfo(pointInfo);
				// 设定基准点的累计积分
				pointInfo.setCurTotalPoint(initialPoint);
				// 设定基准点的累计兑换积分
				pointInfo.setCurTotalChanged(initTotalChanged);
				// 设定基准点的可兑换积分
				pointInfo.setCurChangablePoint(initChangablePoint);
				if (!CherryChecker.isNullOrEmpty(initialTime)) {
					reCalcInfo.put("pointReCalcDate", initialTime);
					recalDateFlag = false;
				}
			} else {
				// 是否从MongoDB取基准点判断条件
				String moveMongoDBFlag = (String)reCalcInfo.get("moveMongoDBFlag");
				Map<String, Object> extArgMap = campBaseDTO.getExtArgs();
				// 积分 DTO
				campBaseDTO.setPointInfo(pointInfo);
				// 设定基准点的累计积分
				campBaseDTO.setRecordKbn(DroolsConstants.RECORDKBN_5);
				this.getReferenceRecord(campBaseDTO, moveMongoDBFlag);
				if ("1".equals(extArgMap.get("POINT_RESET"))) {
					pointInfo.setCurTotalPoint(initialPoint);
				}
				// 设定基准点的累计兑换积分
				campBaseDTO.setRecordKbn(DroolsConstants.RECORDKBN_6);
				this.getReferenceRecord(campBaseDTO, moveMongoDBFlag);
				if ("1".equals(extArgMap.get("POINT_RESET"))) {
					pointInfo.setCurTotalChanged(initTotalChanged);
				}
				// 设定基准点的可兑换积分
				campBaseDTO.setRecordKbn(DroolsConstants.RECORDKBN_7);
				this.getReferenceRecord(campBaseDTO, moveMongoDBFlag);
				if ("1".equals(extArgMap.get("POINT_RESET"))) {
					pointInfo.setCurChangablePoint(initChangablePoint);
				}
				if (ptClearFlag) {
					// 设定基准点的累计失效积分
					campBaseDTO.setRecordKbn(DroolsConstants.RECORDKBN_8);
					this.getReferenceRecord(campBaseDTO, moveMongoDBFlag);
					
					// 设定基准点的上一次清零的积分变化时间
					campBaseDTO.setRecordKbn(DroolsConstants.RECORDKBN_9);
					this.getReferenceRecord(campBaseDTO, moveMongoDBFlag);
				}
			}
			if (recalDateFlag) {
				reCalcInfo.put("pointReCalcDate", reCalcInfo.get("reCalcDate"));
			}
			if (ptRewardFlag) {
				// 积分奖励集合
				Map<String, Object> prMap = new HashMap<String, Object>();
				campBaseDTO.getProcDates().put("PR", prMap);
				List<Map<String, Object>> ptRewardList = binBEDRHAN01_Service.getPtRewardList(reCalcInfo);
				if (null != ptRewardList) {
					for (Map<String, Object> ptRewardMap : ptRewardList) {
						// 积分变化单号
						String tradeNoIF = (String) ptRewardMap.get("tradeNoIF");
						prMap.put(tradeNoIF, ptRewardMap);
						// 重算前的奖励积分值
						double point = 0;
						Object usedCount = ptRewardMap.get("usedCount");
						if (!CherryChecker.isNullOrEmpty(usedCount, true)) {
							point = Double.parseDouble(usedCount.toString());
						}
						ptRewardMap.put("point", point);
					}
				}
			}
			if (ptClearFlag) {
				// 取得重算时间点后清零记录
				List<Map<String, Object>> clearDateList = binBEDRHAN01_Service.getPrePCList(reCalcInfo);
				if (null != clearDateList && !clearDateList.isEmpty()) {
					campBaseDTO.getProcDates().put("CLEARDATES", clearDateList);
				}
				if (pcSendFlag) {
					campBaseDTO.getExtArgs().put("PSDKBN","2");
				}
			}
			// 删除指定会员重算时间点以后的履历记录
			binBEDRHAN01_Service.deletePointChange(reCalcInfo);
			Map<String, Object> paramMap = new HashMap<String, Object>();
			// 会员ID
			paramMap.put("memberInfoId", campBaseDTO.getMemberInfoId());
			// 会员俱乐部ID
			if (0 != campBaseDTO.getMemberClubId()) {
				paramMap.put("memberClubId", campBaseDTO.getMemberClubId());
			}
			// 累计积分
			paramMap.put("totalPoint", pointInfo.getCurTotalPoint());
			// 累计兑换积分
			paramMap.put("totalChanged", pointInfo.getCurTotalChanged());
			// 可兑换积分
			paramMap.put("changablePoint", pointInfo.getCurChangablePoint());
			if (ptClearFlag) {
				// 上一次清零的积分变化时间
				String prePCBillTime = pointInfo.getPrePCBillTime();
				if (null != prePCBillTime) {
					paramMap.put("preDisPointTime", prePCBillTime);
					// 上一次积分清零日
					paramMap.put("preDisableDate", pointInfo.getPreDisableDate());
					// 累计失效积分
					paramMap.put("totalDisablePoint", pointInfo.getTotalDisablePoint());
				}
			}
			// 更新会员积分信息表
			binBEDRHAN01_Service.updateMemberPoint(paramMap);
		}
		if (isLog) {
			bfendTime = System.currentTimeMillis();
			// 运行时间
			double subTime = bfendTime - bfstartTime;
			// 运行时间日志内容
			String msg = DroolsMessageUtil.getMessage(
					DroolsMessageUtil.IDR00013, new String[] {String.valueOf(subTime)});
			logger.info(msg);
		}
		// 开始时间
		long rstartTime = 0;
		// 结束时间
		long rendTime = 0;
		if (isLog) {
			rstartTime = System.currentTimeMillis();
		}
		reCalcInfo.put("updatedBy", CherryBatchConstants.UPDATE_NAME);
		reCalcInfo.put("updatePGM", "BINBEDRHAN01");
		// 删除指定会员重算时间点以后的履历记录
		binBEDRHAN01_Service.deleteRuleExecRecord(reCalcInfo);
		
		// 等级MQ明细业务List
		List<LevelDetailDTO> levelDetailList = new ArrayList<LevelDetailDTO>();
		// 化妆次数MQ明细业务List
		List<BTimesDetailDTO> btimesDetailList = new ArrayList<BTimesDetailDTO>();
		
		Map<String, Object> paramMap = new HashMap<String, Object>();
		// 会员ID
		paramMap.put("memberInfoId", campBaseDTO.getMemberInfoId());
		// 变更后等级ID
		paramMap.put("memberLevelId", campBaseDTO.getCurLevelId());
		// 变更前等级ID
		paramMap.put("oldMemberLevelId", campBaseDTO.getUpgradeFromLevel());
		// 业务时间
		paramMap.put("levelAdjustDay", campBaseDTO.getLevelAdjustDay());
		// 变化类型
		paramMap.put("changeType", campBaseDTO.getLevelChangeType());
		// 会员等级状态
		paramMap.put("levelStatus", campBaseDTO.getLevelStatus());
		// 等级有效期开始日
		paramMap.put("levelStartDate", campBaseDTO.getLevelStartDate());
		// 等级有效期结束日
		paramMap.put("levelEndDate", campBaseDTO.getLevelEndDate());
		// 入会或者升级首单号
		paramMap.put("firstBillID", campBaseDTO.getFirstBillId());
		// 更新者
		paramMap.put(CherryBatchConstants.UPDATEDBY, CherryBatchConstants.UPDATE_NAME);
		// 更新程序名
		paramMap.put(CherryBatchConstants.UPDATEPGM, "BINBEDRHAN01");
		Map<String, Object> memExtMap = new HashMap<String, Object>();
		if (isNoClub) {
			// 更新会员等级
			binBEDRHAN01_Service.updateMemberLevel(paramMap);
			// 会员ID
			memExtMap.put("memberInfoId", campBaseDTO.getMemberInfoId());
			// 累积金额
			memExtMap.put("totalAmounts", campBaseDTO.getCurTotalAmount());
			// 化妆次数
			memExtMap.put("curBtimes", campBaseDTO.getCurBtimes());
			// 更新会员信息扩展表
			binOLCM31_BL.updateMemberExtInfo(memExtMap);
		} else {
			// 等级ID
			paramMap.put("clubLevelId", campBaseDTO.getMemClubLeveId());
			// 累积金额
			paramMap.put("totalAmounts", campBaseDTO.getCurTotalAmount());
			// 更新会员等级(会员俱乐部)
			binBEDRHAN01_Service.updateMemberClubLevel(paramMap);
		}
		
		// 所有需要重算的业务数据List
		List<Map<String, Object>> allBusDataList = new ArrayList<Map<String,Object>>();
		if (isbd) {
			reCalcInfo.put("BDFROMTIME", "2015-10-09 00:00:00");
		}
		// 查询需要重算的销售记录
		List<Map<String, Object>> saleRecordList = binBEDRHAN01_Service.getSaleRecord(reCalcInfo);
		if(saleRecordList != null && !saleRecordList.isEmpty()) {
			// 关联退货List
			List<Map<String, Object>> retSaleList = new ArrayList<Map<String, Object>>();
			for (int i = 0; i < saleRecordList.size(); i++) {
				Map<String, Object> saleRecordMap = saleRecordList.get(i);
				String billCodePre = (String) saleRecordMap.get("billCodePre");
				// 关联退货单
				if ("SR".equals(saleRecordMap.get("tradeType")) && !CherryChecker.isNullOrEmpty(billCodePre)) {
					boolean remFlag = true;
					if (billCodePre.indexOf("SR") != 0) {
						// 品牌ID
						saleRecordMap.put("brandInfoId", campBaseDTO.getBrandInfoId());
						// 组织ID
						saleRecordMap.put("organizationInfoId", campBaseDTO.getOrganizationInfoId());
						// 通过前置单号查询原单信息
						Map<String, Object> preSaleInfo = binBEDRHAN01_Service.getPreSaleInfo(saleRecordMap);
						if (null != preSaleInfo && !preSaleInfo.isEmpty()) {
							// 原单时间
							String saleTime = (String) preSaleInfo.get("saleTime");
							// 原单时间小于切换时间
							if (isInitPt && isBefore(saleTime, initPtTime)) {
								remFlag = false;
								saleRecordMap.put("PREBILL", billCodePre);
							}
						} else {
							remFlag = false;
						}
						if (remFlag) {
							retSaleList.add(saleRecordMap);
						}
					}
					if (remFlag) { 
						saleRecordList.remove(i);
						i--;
					}
				}
			}
			// 将退货单和原单拼在一起
			for (Map<String, Object> retSaleMap: retSaleList) {
				// 退货关联单号
				String billCodePre = (String) retSaleMap.get("billCodePre");
				for (Map<String, Object> saleRecordMap: saleRecordList) {
					// 单号
					String billId = (String) saleRecordMap.get("billId");
					if (billCodePre.equals(billId)) {
						// 原单总金额
						double amount = 0;
						if (null != saleRecordMap.get("amount")) {
							amount = Double.parseDouble(saleRecordMap.get("amount").toString());
						}
						// 退货单金额
						double retAmount = 0;
						if (null != retSaleMap.get("amount")) {
							retAmount = Double.parseDouble(retSaleMap.get("amount").toString());
						}
						saleRecordMap.put("amount", DoubleUtil.sub(amount, retAmount));
						
						break;
					}
				}
			}
			allBusDataList.addAll(saleRecordList);
		}
		// 查询需要重算的订单记录
		List<Map<String, Object>> esOrderList = binBEDRHAN01_Service.getESOrderRecord(reCalcInfo);
		if (null != esOrderList && !esOrderList.isEmpty()) {
			allBusDataList.addAll(esOrderList);
		}
		// 查询需要重算的化妆次数、积分的使用记录
		List<Map<String, Object>> memUsedCountList = binBEDRHAN01_Service.getMemUsedCount(reCalcInfo);
		if(memUsedCountList != null && !memUsedCountList.isEmpty()) {
			allBusDataList.addAll(memUsedCountList);
		}
		if (pointFlag) {
			// 查询需要重算的预约单信息
			List<Map<String, Object>> orderTicketList = binBEDRHAN01_Service.getOrderTicketList(reCalcInfo);
			if(orderTicketList != null && !orderTicketList.isEmpty()) {
				allBusDataList.addAll(orderTicketList);
			}
		}
		if(allBusDataList != null && !allBusDataList.isEmpty()) {
			// 按单据时间进行排序
			this.listSort(allBusDataList);
			// 把list按时间分组存放
			allBusDataList = this.convertListByDate(allBusDataList);
		}
		
		reCalcDate = reCalcDate.substring(0, 10);
		// 业务日期
		String bussinessDate = binBEDRHAN01_Service.getBussinessDate(reCalcInfo);
		// 业务日期
		campBaseDTO.setBusinessDate(reCalcDate);
		// 设置各类处理日期
		procDateSetting(campBaseDTO);
		// 从重算日期开始按天重算，直到业务日期为止结束
		Map<String, Object> procMap = new HashMap<String, Object>();
		procMap.put("levelDetailList", levelDetailList);
		procMap.put("btimesDetailList", btimesDetailList);
		procMap.put("fromDate", reCalcDate);
		Map<String, Object> pointDataMap = new HashMap<String, Object>();
		procMap.put("pointDataMap", pointDataMap);
		if (pointFlag) {
			procMap.put("pointDataMap", pointDataMap);
			// 单据主记录的明细行列表
			List<Map<String, Object>> tradeDataList = new ArrayList<Map<String, Object>>();
			// 单据明细记录的明细行列表
			List<Map<String, Object>> detailDataList = new ArrayList<Map<String, Object>>();
			// 退货关联销售单据的明细行列表
			List<Map<String, Object>> detailSRDataList = new ArrayList<Map<String, Object>>();
			pointDataMap.put("TradeDataList", tradeDataList);
			pointDataMap.put("DetailDataList", detailDataList);
			pointDataMap.put("DetailSRDataList", detailSRDataList);
		}
		// 设置会员属性
		binOLCM31_BL.execMemberInfo(campBaseDTO);
		// 业务数据存在的场合
		if(allBusDataList != null && !allBusDataList.isEmpty()) {
			String toDate = (String) allBusDataList.get(0).get("ticketDate");
			if (DateUtil.compareDate(reCalcDate, toDate) < 0) {
				procMap.put("toDate", toDate);
				// 执行各类处理
				procDateExec(campBaseDTO, procMap);
			}
			// 最后业务日期
			String lastDate = (String) (allBusDataList.get(allBusDataList.size() - 1)).get("ticketDate");
			if (DateUtil.compareDate(lastDate, bussinessDate) > 0) {
				bussinessDate = lastDate;
			}
			for (int i = 0; i < allBusDataList.size(); i++) {
				Map<String, Object> allBusDataMap = allBusDataList.get(i);
				List<Map<String, Object>> list = (List)allBusDataMap.get("list");
				for(int y  = 0; y < list.size(); y++) {
					Map<String, Object> busDataMap = list.get(y);
					// 按单笔业务重算会员等级和化妆次数
					this.reCalcAllBusData(busDataMap, campBaseDTO, reCalcInfo);
					// 设置等级MQ明细业务
					this.setLevelDetailDTO(campBaseDTO, levelDetailList);
					// 设置化妆次数MQ明细业务
					this.setBTimesDetailDTO(campBaseDTO, btimesDetailList);
					if (pointFlag) {
						// 设置积分MQ明细业务
						this.setPointDetailDTO(campBaseDTO, pointDataMap);
					}
				}
				String ticketDate = (String)allBusDataMap.get("ticketDate");
				if (DateUtil.compareDate(ticketDate, bussinessDate) < 0) {
					// 业务日期
					campBaseDTO.setBusinessDate(ticketDate);
					// 设置各类处理日期
					procDateSetting(campBaseDTO);
					if (i != allBusDataList.size() - 1) {
						toDate = (String) allBusDataList.get(i + 1).get("ticketDate");
					}
					if (DateUtil.compareDate(toDate, bussinessDate) > 0 || i == allBusDataList.size() - 1) {
						toDate = bussinessDate;
					}
					procMap.put("fromDate", ticketDate);
					procMap.put("toDate", toDate);
					// 执行各类处理
					procDateExec(campBaseDTO, procMap, "1");
				}
			}
		} else {
			procMap.put("toDate", bussinessDate);
			// 执行各类处理
			procDateExec(campBaseDTO, procMap);
		}
			
		// 累积金额
		memExtMap.put("totalAmounts", campBaseDTO.getCurTotalAmount());
		// 化妆次数
		memExtMap.put("curBtimes", campBaseDTO.getCurBtimes());
		if (calcUpFlag) {
			// 升级所需金额
			memExtMap.put("upLevelAmount", campBaseDTO.getUpLevelAmount());
		}
		if (isNoClub) {
			// 更新会员信息扩展表
			binOLCM31_BL.updateMemberExtInfo(memExtMap);
		} else {
			Map<String, Object> updateMap = new HashMap<String, Object>();
			// 会员信息ID
			updateMap.put("memberInfoId", campBaseDTO.getMemberInfoId());
			updateMap.put("totalAmounts", campBaseDTO.getCurTotalAmount());
			// 升级所需金额
			if (calcUpFlag) {
				updateMap.put("upLevelAmount", campBaseDTO.getUpLevelAmount());
			}
			// 会员俱乐部ID
			updateMap.put("memberClubId", campBaseDTO.getMemberClubId());
			// 更新会员俱乐部等级扩展信息
			binOLCM31_BL.updateMemberClubExtInfo(updateMap);
		}
		
		if (ptClearFlag) {
//			// 取得重算时间点后清零记录数
//			int ptClearCount = binBEDRHAN01_Service.getPtClearCount(reCalcInfo);
//			if (ptClearCount > 0) {
//				campBaseDTO.getExtArgs().put("RECLEAR_FLAG", "2");
//				// 取得会员重算时间点后第一条积分清零时间
//				Map<String, Object> firstPCInfo = binBEDRHAN01_Service.getMemFirstPCInfo(reCalcInfo);
//				if (null != firstPCInfo && null != firstPCInfo.get("changeDate")) {
//					reCalcInfo.put("firstPCDate", firstPCInfo.get("changeDate"));
//					// 取得会员清零后的负积分之和
//					Map<String, Object> subPointInfo = binBEDRHAN01_Service.getMemSubPoint(reCalcInfo);
//					if (null != subPointInfo && !subPointInfo.isEmpty()) {
//						double subPoint = Double.parseDouble(subPointInfo.get("subPoint").toString());
//						if (subPoint < 0) {
//							campBaseDTO.getExtArgs().put("PC_SUBPOINT", subPoint);
//						}
//					}
//				}
//			}
			// 最后的单据时间
			String ticketDate = campBaseDTO.getTicketDate();
			// 积分清零业务时间
			String pcTicketDate = null;
			if (null != ticketDate && !ticketDate.isEmpty()) {
				// 最后的单据日期
				String ticketDay = DateUtil.coverTime2YMD(ticketDate, DateUtil.DATE_PATTERN);
				if (DateUtil.compareDate(ticketDay, bussinessDate) < 0) {
					pcTicketDate = DateUtil.suffixDate(bussinessDate, 0);
				} else {
					pcTicketDate = ticketDate;
					if (CherryChecker.checkDate(ticketDate)) {
						pcTicketDate = ticketDate + " 00:00:00";
					} else {
						int index = ticketDate.indexOf(".");
						if (index > 0) {
							pcTicketDate = ticketDate.substring(0, index);
						}
					}
					Calendar calendar = Calendar.getInstance();
				    calendar.setTime(DateUtil.coverString2Date(pcTicketDate,"yyyy-MM-dd HH:mm:ss"));
				    calendar.add(Calendar.SECOND, 1);
				    pcTicketDate = DateUtil.date2String(calendar.getTime(), "yyyy-MM-dd HH:mm:ss");
				}
			}
			// 业务日期
			campBaseDTO.setBusinessDate(bussinessDate);
			// 会员有效期截止日时
			campBaseDTO.setTicketDate(pcTicketDate);
			// 积分清零处理
			this.pointClearData(campBaseDTO);
			// 设置积分MQ明细业务
			this.setPointDetailDTO(campBaseDTO, pointDataMap);
			if (pcSendFlag) {
				// 取得重算时间点后撤销的清零记录
				List<Map<String, Object>> delPtClearList = binBEDRHAN01_Service.getDelPtClearList(reCalcInfo);
				if (null != delPtClearList && !delPtClearList.isEmpty()) {
					// 已失效的清零记录
					List<Map<String, Object>> invaildList = new ArrayList<Map<String, Object>>();
					for (int i = 0; i < delPtClearList.size(); i++) {
						Map<String, Object> clearMap = delPtClearList.get(i);
						// 有效区分
						String validFlag = (String) clearMap.get("validFlag");
						// 已失效的记录
						if ("0".equals(validFlag)) {
							invaildList.add(clearMap);
							delPtClearList.remove(i);
							i--;
						}
					}
					for (Map<String, Object> invaildMap : invaildList) {
						boolean flag = true;
						// 清零日期
						String inClearDate = (String) invaildMap.get("clearedTime");
						for (int i = 0; i < delPtClearList.size(); i++) {
							Map<String, Object> clearMap = delPtClearList.get(i);
							// 清零日期
							String clearDate = (String) clearMap.get("clearedTime");
							int cp = compTime(inClearDate, clearDate);
							if (cp == 0) {
								flag = false;
								break;
							} else if (cp > 0) {
								delPtClearList.remove(i);
								i--;
							} else {
								break;
							}
						}
						if (flag) {
							// 查询已记录的履历
							Map<String, Object> clearRecord = binBEDRHAN01_Service.getClearRecordById(invaildMap);
							if (null != clearRecord && !clearRecord.isEmpty()) {
								// 被清积分值(历史记录)
								double clearedPointsPre = Double.parseDouble(clearRecord.get("clearedPoints").toString());
								if (0 != clearedPointsPre) {
									// 重新下发次数
									int reSendCount = Integer.parseInt(clearRecord.get("reSendCount").toString());
									reSendCount++;
									// 被清积分值
									clearRecord.put("clearedPoints", 0);
									// 重新下发次数
									clearRecord.put("reSendCount", reSendCount);
									// 下发状态
									clearRecord.put("sendStatus", 9);
									// 作成程序名
									clearRecord.put(CherryBatchConstants.CREATEPGM, "BINBEDRHAN01");
									// 更新程序名
									clearRecord.put(CherryBatchConstants.UPDATEPGM, "BINBEDRHAN01");
									// 作成者
									clearRecord.put(CherryBatchConstants.CREATEDBY, "BINBEDRHAN01");
									// 更新者
									clearRecord.put(CherryBatchConstants.UPDATEDBY, "BINBEDRHAN01");
									// 插入会员积分变化明细表
									binBEDRHAN01_Service.addPointsClearRecord(clearRecord);
								}
							}
						}
					}
				}
			}
		}
		if (isLog) {
			rendTime = System.currentTimeMillis();
			// 运行时间
			double subTime = rendTime - rstartTime;
			// 运行时间日志内容
			String msg = DroolsMessageUtil.getMessage(
					DroolsMessageUtil.IDR00014, new String[] {String.valueOf(subTime)});
			logger.info(msg);
		}
		try {
			// 开始时间
			long qstartTime = 0;
			// 结束时间
			long qendTime = 0;
			if (isLog) {
				qstartTime = System.currentTimeMillis();
			}
			// 会员卡号
			campBaseDTO.setMemCode(memCard);
			if (isNoClub) {
				// 设置默认等 级
				deftLevelSetting(campBaseDTO, levelDetailList, bussinessDate);
			} else {
				deftClubLevelSetting(campBaseDTO, levelDetailList, bussinessDate);
			}
			reCalcInfo.put("recordKbn", DroolsConstants.RECORDKBN_0);
			// 查询需要删除的等级履历记录
			List<Map<String, Object>> delRecordList = binBEDRHAN01_Service.getDelRecordList(reCalcInfo);
			// 发送等级下发MQ消息
			this.sendLevelMes(campBaseDTO, levelDetailList, delRecordList);
			if (isNoClub) {
				// 取得会员保级信息
				Map<String, Object> keepMap = binOLCM31_BL.getKeepMemLevelInfo(campBaseDTO);
				if (null != keepMap && !keepMap.isEmpty()) {
					// 会员修改属性处理
					binBEMQMES08_BL.updateMemberExtInfo(keepMap);
				} else {
					if (levelFlag) {
						if (CherryChecker.isNullOrEmpty(levStatus, true)) {
							levStatus = CherryConstants.LEVELSTATUS_1;
						}
						campBaseDTO.setLevelStatus(levStatus);
						String ticketTime = campBaseDTO.getTicketDate();
						// 等级计算
						binbedrjon10BL.ruleExec(campBaseDTO);
						if (campBaseDTO.isMatchRule()) {
							// 取得会员等级维护信息
							Map<String, Object> memLevelChangeInfo = binOLCM31_BL.getMemLevelChangeInfo(campBaseDTO);
							if (null != memLevelChangeInfo && !memLevelChangeInfo.isEmpty()) {
								// 会员修改属性处理
								binBEMQMES08_BL.updateMemberExtInfo(memLevelChangeInfo);
							}
						}
						campBaseDTO.setTicketDate(ticketTime);
					}
				}
				reCalcInfo.put("recordKbn", DroolsConstants.RECORDKBN_2);
				// 查询需要删除的化妆次数履历记录
				delRecordList = binBEDRHAN01_Service.getDelRecordList(reCalcInfo);
				// 发送化妆次数下发MQ消息
				this.sendBTimesMes(campBaseDTO, btimesDetailList, delRecordList);
			}
			if (pointFlag) {
				// 查询需要删除的化妆次数履历记录
				delRecordList = binBEDRHAN01_Service.getDelPointChangeList(reCalcInfo);
				// 发送积分下发MQ消息
				this.sendPointMes(campBaseDTO, pointDataMap, delRecordList, befTotalPoint);
				if (ptRewardFlag) {
					// 发送积分奖励MQ消息
					this.sendPointRewardMes(campBaseDTO);
				}
			}
//			if (!isNoClub && 0 != campBaseDTO.getMemberClubId() && 1 != campBaseDTO.getMemRegFlg()) {
//				Map<String, Object> mzMap = new HashMap<String, Object>();
//				mzMap.put("memberInfoId", campBaseDTO.getMemberInfoId());
//				mzMap.put("memberClubId", campBaseDTO.getMemberClubId());
//				mzMap.put("mzClubId", campBaseDTO.getMemberClubId());
//				mzMap.put("organizationInfoId", campBaseDTO.getOrganizationInfoId());
//				mzMap.put("brandInfoId", campBaseDTO.getBrandInfoId());
//				// 组织代码
//				mzMap.put("orgCode", campBaseDTO.getOrgCode());
//				// 品牌代码
//				mzMap.put("brandCode", campBaseDTO.getBrandCode());
//				// 取得会员俱乐部当前等级信息
//				Map<String, Object> clubLevelMap = binOLCM31_BL.getClubCurLevelInfo(mzMap);
//				if (null != clubLevelMap && !clubLevelMap.isEmpty() &&
//						0 == Integer.parseInt(clubLevelMap.get("organizationId").toString())) {
//					// 查询首单信息
//					Map<String, Object> firstMap = binBEDRHAN01_Service.getFirstTickInfo(mzMap);
//					if (null != firstMap && !firstMap.isEmpty()) {
//						mzMap.putAll(firstMap);
//						mzMap.put("employeeCode", firstMap.get("BAcode"));
//						// 取得员工信息
//						Map<String, Object> empMap = binOLCM31_BL.getComEmployeeInfo(mzMap);
//						if (null != empMap && !empMap.isEmpty()) {
//							mzMap.put("employeeID", empMap.get("employeeId"));
//						}
//						// 更新会员俱乐部扩展属性
//						binOLCM31_BL.updateClubExtInfo(mzMap);
//						// 发送会员扩展信息MQ消息(全部记录)
//						binOLCM31_BL.sendAllMZMQMsg(mzMap);
//					}
//				}
//			}
			// 需要同步天猫会员
			if (isTmall && binOLCM31_BL.needSync(campBaseDTO.getMemberInfoId(), brandCode)) {
				double curPoint = 0;
				if (null != campBaseDTO.getPointInfo()) {
					curPoint = campBaseDTO.getPointInfo().getCurTotalPoint();
				}
				// 等级或者积分发生变化
				if (oldLevel != campBaseDTO.getCurLevelId() || 
						befTotalPoint != curPoint) {
					Map<String, Object> tmSyncInfo = new HashMap<String, Object>();
					tmSyncInfo.put("memberInfoId", memberInfoId);
					tmSyncInfo.put("brandCode", brandCode);
					tmSyncInfo.put("PgmName", "BINBEDRHAN01");
					// 同步天猫会员
					binOLCM31_BL.syncTmall(tmSyncInfo);
				}
			} else {
				if (oldLevel != campBaseDTO.getCurLevelId()) {
					binOLCM31_BL.addTempAdjustMember(campBaseDTO.getMemberInfoId(),campBaseDTO.getOrganizationInfoId(),campBaseDTO.getBrandInfoId());
				}
			}
			if (isLog) {
				qendTime = System.currentTimeMillis();
				// 运行时间
				double subTime = qendTime - qstartTime;
				// 运行时间日志内容
				String msg = DroolsMessageUtil.getMessage(
						DroolsMessageUtil.IDR00015, new String[] {String.valueOf(subTime)});
				logger.info(msg);
			}
		} catch (Exception e) {
			BatchExceptionDTO batchExceptionDTO = new BatchExceptionDTO();
			batchExceptionDTO.setBatchName(this.getClass());
			batchExceptionDTO.setErrorCode("EDR00002");
			batchExceptionDTO.setErrorLevel(CherryBatchConstants.LOGGER_ERROR);
			// 会员ID
			batchExceptionDTO.addErrorParam(memberInfoId);
			batchExceptionDTO.setException(e);
			throw new CherryBatchException(batchExceptionDTO);
		}
		if (isLog) {
			aftendTime = System.currentTimeMillis();
			// 运行时间
			double subTime = aftendTime - bfstartTime;
			// 运行时间日志内容
			String msg = DroolsMessageUtil.getMessage(
					DroolsMessageUtil.IDR00016, new String[] {String.valueOf(subTime)});
			logger.info(msg);
		}
	}
	
	/**
	 * 
	 * 取得芳香会员的等级ID
	 * 
	 * @param int 芳香会员的等级ID
	 * 
	 */
	private int getKeepId() {
		for(Map.Entry<String,Object> en: allMemLevelMap.entrySet()){
			Map<String, Object> levelInfo = (Map<String, Object>) en.getValue();
			if (null != levelInfo && !levelInfo.isEmpty()) {
				// 等级代号
				String levelCode = (String) levelInfo.get("levelCode");
				if ("WMLC002".equals(levelCode)) {
					return Integer.parseInt(levelInfo.get("memberLevelId").toString());
				}
			}
		}
		return 0;
	}
	
	/**
	 * 
	 * 按单笔业务重算会员等级和化妆次数
	 * 
	 * @param map 业务信息
	 * @param campBaseDTO 重算需要的参数
	 * @param num 重算序号
	 * 
	 */
	public void reCalcAllBusData(Map<String, Object> map, CampBaseDTO campBaseDTO, Map<String, Object> reCalcInfo) throws Exception {
		
		// 会员ID
		String memberInfoId = String.valueOf(campBaseDTO.getMemberInfoId());
		// 单据号
		String billId = (String)map.get("billId");
		// 业务类型
		String tradeType = (String)map.get("tradeType");
		campBaseDTO.setEsFlag((String) map.get("ESFlag"));
		// 单据号
		campBaseDTO.setBillId(billId);
		// 业务类型
		campBaseDTO.setTradeType(tradeType);
		// 单据产生日期
		campBaseDTO.setTicketDate((String)map.get("ticketDate"));
		// 来源
		campBaseDTO.setChannel((String)map.get("channel"));
		// 柜台号
		campBaseDTO.setCounterCode((String)map.get("countercode"));
		// 员工编号
		campBaseDTO.setEmployeeCode((String)map.get("baCode"));
		// 变更前会员等级
		campBaseDTO.setOldLevelId(campBaseDTO.getCurLevelId());
		// 变更前累计金额
		campBaseDTO.setOldTotalAmount(campBaseDTO.getCurTotalAmount());
		// 变更前化妆次数
		campBaseDTO.setOldBtimes(campBaseDTO.getCurBtimes());
		// 变更前积分
		campBaseDTO.setOldPoint(campBaseDTO.getCurPoint());
		// 变更前可兑换金额(化妆次数用)
		campBaseDTO.setOldBtimesAmount(campBaseDTO.getCurBtimesAmount());
		// 每种履历下的操作类型和重算次数信息
		campBaseDTO.setRecordKbnInfo(new HashMap<String, Object>());
		// 升降级区分
		campBaseDTO.setChangeType(null);
		campBaseDTO.setPointInfo(null);
		if (pointFlag) {
			Map<String, Object> searchMap = new HashMap<String, Object>();
			// 所属组织ID
			searchMap.put("organizationInfoId", campBaseDTO.getOrganizationInfoId());
			// 所属品牌ID
			searchMap.put("brandInfoId", campBaseDTO.getBrandInfoId());
			// 柜台号
			searchMap.put("counterCode", campBaseDTO.getCounterCode());
			// 员工编号
			searchMap.put("employeeCode", campBaseDTO.getEmployeeCode());
			// 部门ID
			campBaseDTO.setOrganizationId(null);
			// 部门名称
			campBaseDTO.setDepartName(null);
			// 渠道ID
			campBaseDTO.setChannelId(0);
			// 城市ID
			campBaseDTO.setCounterCityId(0);
			// 员工ID
			campBaseDTO.setEmployeeId(null);
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
		}
		
		try {
			// 业务类型为销售的场合
			if("NS".equals(tradeType) || "PX".equals(tradeType)) {
				if (pointFlag && birthFlag) {
					if (!reCalcInfo.containsKey("FstBill")) {
						reCalcInfo.put("FstBill", billId);
					}
					// 根据生日修改履历重新设置生日
					resetBirth(campBaseDTO, reCalcInfo);
				}
				// 是否计算积分
				campBaseDTO.setPointFlag((String) map.get("pointFlag"));
				// 会员卡号
				campBaseDTO.setMemCode((String)map.get("memberCode"));
				// 单次购买消费金额
				campBaseDTO.setAmount(Double.parseDouble(map.get("amount").toString()));
				// 理由
				campBaseDTO.setReason(0);
				// 当前累计金额
				campBaseDTO.setCurTotalAmount(DoubleUtil.add(campBaseDTO.getCurTotalAmount(), campBaseDTO.getAmount()));
				// 当前可兑换金额(化妆次数用)
				campBaseDTO.setCurBtimesAmount(DoubleUtil.add(campBaseDTO.getCurBtimesAmount(), campBaseDTO.getAmount()));
				// 按单笔销售记录重算会员等级和化妆次数
				levelPointHandler.executeRuleFile(campBaseDTO);
			} else if("MS".equals(tradeType)) { // 业务类型为会员初始数据的场合
				// 会员卡号
				campBaseDTO.setMemCode((String)map.get("memCode"));
				// 理由
				campBaseDTO.setReason(1);
				// 理由内容
				campBaseDTO.getExtArgs().put("MS_REASON", map.get("reasonText"));
				campBaseDTO.emptyRuleIds();
				campBaseDTO.emptySubCampCodes();
				// 按会员初始数据重算会员等级和化妆次数
				this.reCalcMemInitData(map, campBaseDTO);
			} else if("BU".equals(tradeType)) { // 业务类型为化妆次数使用的场合
				// 会员卡号
				campBaseDTO.setMemCode((String)map.get("memCode"));
				// 理由
				campBaseDTO.setReason(3);
				// 按化妆次数使用重算会员等级和化妆次数
				this.reCalcMemUsedCount(map, campBaseDTO);
			} else if("SR".equals(tradeType)) { // 业务类型为退货的场合
				if (pointFlag) {
					// 前置单号
					campBaseDTO.getExtArgs().remove("PREBILL");
					String prebill = (String)map.get("PREBILL");
					if (null != prebill && !prebill.isEmpty()) {
						campBaseDTO.getExtArgs().put("PREBILL", prebill);
					}
				}
				// 是否计算积分
				campBaseDTO.setPointFlag((String) map.get("pointFlag"));
				// 会员卡号
				campBaseDTO.setMemCode((String)map.get("memberCode"));
				// 单次购买消费金额
				campBaseDTO.setAmount(Double.parseDouble(map.get("amount").toString()));
				// 理由
				campBaseDTO.setReason(0);
				// 当前累计金额
				campBaseDTO.setCurTotalAmount(DoubleUtil.sub(campBaseDTO.getCurTotalAmount(), campBaseDTO.getAmount()));
				// 当前可兑换金额(化妆次数用)
				campBaseDTO.setCurBtimesAmount(DoubleUtil.sub(campBaseDTO.getCurBtimesAmount(), campBaseDTO.getAmount()));
				// 按单笔销售记录重算会员等级和化妆次数
				levelPointHandler.executeRuleFile(campBaseDTO);
			} else if("PT".equals(tradeType) && pointFlag) { // 业务类型为积分维护的场合
				// 会员卡号
				campBaseDTO.setMemCode((String)map.get("memCode"));
				// 理由
				campBaseDTO.setReason(5);
				// 按积分维护数据重算会员的积分
				this.reCalcMemPointData(map, campBaseDTO, reCalcInfo);
			} else if("PB".equals(tradeType) && pointFlag) {
				// 理由
				campBaseDTO.setReason(0);
				// 按积分兑换预约数据重算会员的积分
				this.reCalcOrderPointData(map, campBaseDTO);
			} else if("MT".equals(tradeType) && pointFlag) { // 业务类型为积分维护的场合
				// 会员卡号
				campBaseDTO.setMemCode((String)map.get("memCode"));
				// 理由
				campBaseDTO.setReason(5);
				// 按会员初始积分数据重算会员的积分
				this.reCalcMTPointData(map, campBaseDTO);
			}
		} catch (Exception e) {
			BatchExceptionDTO batchExceptionDTO = new BatchExceptionDTO();
			batchExceptionDTO.setBatchName(this.getClass());
			// 业务类型为销售的场合
			if("NS".equals(tradeType)) {
				batchExceptionDTO.setErrorCode("EDR00010");
			} else if("MS".equals(tradeType)) { // 业务类型为会员初始数据的场合
				batchExceptionDTO.setErrorCode("EDR00011");
			} else if("BU".equals(tradeType)) { // 业务类型为化妆次数使用的场合
				batchExceptionDTO.setErrorCode("EDR00012");
			} else if("SR".equals(tradeType)) { // 业务类型为退货的场合
				batchExceptionDTO.setErrorCode("EDR00013");
			} else if("PT".equals(tradeType)) { // 业务类型为积分维护的场合
				batchExceptionDTO.setErrorCode("EDR00047");
			} else if("PB".equals(tradeType)) { // 业务类型为积分兑换预约的场合
				batchExceptionDTO.setErrorCode("EDR00052");
			} else if("MT".equals(tradeType)) { // 业务类型为积分兑换预约的场合
				batchExceptionDTO.setErrorCode("EDR00054");
			}
			batchExceptionDTO.setErrorLevel(CherryBatchConstants.LOGGER_ERROR);
			// 会员ID
			batchExceptionDTO.addErrorParam(memberInfoId);
			// 单据号
			batchExceptionDTO.addErrorParam(billId);
			batchExceptionDTO.setException(e);
			throw new CherryBatchException(batchExceptionDTO);
		}
	}
	
	/**
	 * 
	 * 根据生日修改履历重新设置生日
	 * 
	 * @param campBaseDTO 重算需要的参数
	 * 
	 */
	private void resetBirth(CampBaseDTO c, Map<String, Object> reCalcInfo) throws Exception {
		if (!CherryChecker.isNullOrEmpty(c.getBirthday(), true)) {
			// 生日修改履历
			List<Map<String, Object>> birthModyList = (List<Map<String, Object>>) reCalcInfo.get("BirthModyList");
			if (null != birthModyList && !birthModyList.isEmpty()) {
				if ("0".equals(reCalcInfo.get("NOBIRTHRULE"))) {
					return;
				}
				String ticketDate = c.getTicketDate();
				int idx = ticketDate.indexOf(".");
				if (idx > 0) {
					ticketDate = ticketDate.substring(0, idx);
				}
				// 重算第一单
				String fstBill = (String) reCalcInfo.get("FstBill");
				if (c.getBillId().equals(fstBill)) {
					// 取得会员生日类规则ID
					String[] arr = binOLCM31_BL.getBirthRuleArr(reCalcInfo);
					if (null != arr && arr.length > 0) {
						Map<String, Object> searchMap = new HashMap<String, Object>();
						// 生日类规则
						searchMap.put("mainIdArr", arr);
						// 会员ID
						searchMap.put("memberInfoId", c.getMemberInfoId());
						// 截止时间
						searchMap.put("toDate", ticketDate);
						// 开始时间
						String fromDate = ticketDate.substring(0, 4) + "-01-01";
						searchMap.put("fromDate", fromDate);
						// 取得会员生日规则匹配的履历
						Map<String, Object> birthDetail = binOLCM31_BL.getBirthPointDeatilInfo(searchMap);
						if (null != birthDetail && !birthDetail.isEmpty()) {
							String changeDate = (String) birthDetail.get("changeDate");
							// 通过业务时间获取生日
							String birthDay = getBirth(DateUtil.coverTime2YMD(changeDate, DateUtil.DATE_PATTERN), birthModyList);
							// 重新设置生日修改履历列表
							convertBirthList(changeDate, birthDay, birthModyList);
						}
						c.getExtArgs().put("BIRARR", arr);
					} else {
						reCalcInfo.put("NOBIRTHRULE", "0");
						return;
					}
				} else {
					// 匹配的时间
					String marthDate = (String) c.getExtArgs().get("MATDATE");
					if (!CherryChecker.isNullOrEmpty(marthDate)) {
						int index = marthDate.indexOf(".");
						if (index > 0) {
							marthDate = marthDate.substring(0, index);
						}
						// 重新设置生日修改履历列表
						convertBirthList(marthDate, c.getBirthday(), birthModyList);
						c.getExtArgs().remove("MATDATE");
					}
				}
				// 通过业务时间获取生日
				String birthDay = getBirth(DateUtil.coverTime2YMD(ticketDate, DateUtil.DATE_PATTERN), birthModyList);
				c.setBirthday(birthDay);
				// 记录生日积分规则匹配结果标识
				if (!c.getExtArgs().containsKey("BIRRD")) {
					c.getExtArgs().put("BIRRD", "1");
				}
			}
		}
	}
	
	/**
	 * 
	 * 重新设置生日修改履历列表
	 * 
	 * @param martchDate 匹配生日的业务时间
	 * @param birthDay 本次生日
	 * @param birthModyList 生日修改履历列表
	 * 
	 */
	private void convertBirthList(String martchDate, String birthDay, List<Map<String, Object>> birthModyList) {
		// 下一年
		int nextYear = Integer.parseInt(martchDate.substring(0, 4)) + 1;
		String modTime = nextYear + "-01-01";
		// 变更后的生日
		String newBirth = null;
		for (int i = 0; i < birthModyList.size(); i++) {
			Map<String, Object> birthModyMap = birthModyList.get(i);
			// 生日变更时间
			String modifyTime = (String) birthModyMap.get("modifyTime");
			if (null != modifyTime && DateUtil.compareDate(modifyTime, modTime) <= 0) {
				newBirth = (String) birthModyMap.get("newBirth");
				birthModyList.remove(i);
				i--;
			}
		}
		Map<String, Object> marthMap = new HashMap<String, Object>();
		marthMap.put("modifyTime", modTime);
		marthMap.put("SpecBirth", birthDay);
		marthMap.put("newBirth", newBirth);
		birthModyList.add(0, marthMap);
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
				birthDay = (String) marthMap.get("SpecBirth");
				if (null != birthDay) {
					return birthDay;
				}
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
	 * 执行各类处理
	 * 
	 * @param campBaseDTO 重算需要的参数
	 * 
	 */
	private void procDateExec(CampBaseDTO campBaseDTO, Map<String, Object> procMap, String... flag) throws Exception {
		// 开始日
		String fromDate = (String) procMap.get("fromDate");
		// 结束日
		String toDate = (String) procMap.get("toDate");
		List<String> dateList = new ArrayList<String>();
		// 各类处理日期集合
		Map<String, Object> procDates = campBaseDTO.getProcDates();
		if (procDates.isEmpty() || CherryChecker.isNullOrEmpty(fromDate) 
				|| CherryChecker.isNullOrEmpty(toDate)) {
			return;
		}
		// 需要执行清零处理
		if (clearFlag) {
			Map<String, Object> zcMap = (Map<String, Object>) procDates.get("ZC");
			if (null != zcMap && !zcMap.isEmpty()) {
				// 清零日期
				String clearDate = (String) zcMap.get("clearDate");
				if (!CherryChecker.isNullOrEmpty(clearDate)) {
					// 清零日期在开始和结束日期之间
					if (DateUtil.compareDate(fromDate, clearDate) <= 0 && DateUtil.compareDate(toDate, clearDate) > 0) {
						dateList.add(clearDate);
					}
				}
			}
		}
		// 需要执行降级处理
		if (downFlag) {
			// 有效期结束日
			String levelEndDate = (String) procDates.get("DG");
			if (!CherryChecker.isNullOrEmpty(levelEndDate)) {
				// 业务日期
				String businessDate = DateUtil.coverTime2YMD(levelEndDate, DateUtil.DATE_PATTERN);
				// 业务日期在开始和结束日期之间
				if (DateUtil.compareDate(fromDate, businessDate) <= 0 && DateUtil.compareDate(toDate, businessDate) > 0) {
					dateList.add(businessDate);
				}
			}
		}
		if (ptClearFlag) {
			List<Map<String, Object>> clearDateList = (List<Map<String, Object>>) procDates.get("CLEARDATES");
			if (null != clearDateList && !clearDateList.isEmpty()) {
				// 清零时间
				String clearDate = (String) clearDateList.get(0).get("clearDate");
				if (!CherryChecker.isNullOrEmpty(clearDate)) {
					// 业务日期
					String clearDay = DateUtil.coverTime2YMD(clearDate, DateUtil.DATE_PATTERN);
					if (null == flag || flag.length == 0) {
						// 业务日期在开始和结束日期之间
						if (DateUtil.compareDate(fromDate, clearDay) <= 0 && DateUtil.compareDate(toDate, clearDay) >= 0) {
							dateList.add(clearDay);
							clearDateList.remove(0);
							procDates.put("RECAL_PCDATE", clearDate);
						}
					} else {
						// 业务日期在开始和结束日期之间
						if (DateUtil.compareDate(fromDate, clearDay) < 0 && DateUtil.compareDate(toDate, clearDay) >= 0) {
							dateList.add(clearDay);
							clearDateList.remove(0);
							procDates.put("RECAL_PCDATE", clearDate);
						}
					}
				}
			}
		}
		if (dateList.isEmpty()) {
			return;
		}
		dateList.add(toDate);
		do {
			dateSort(dateList);
			fromDate = dateList.get(0);
			toDate = dateList.get(1);
			dateList.remove(0);
			procDateCalc(campBaseDTO, procMap, fromDate, toDate, dateList);
		} while (dateList.size() > 1);
	}
	
	/**
	 * 
	 * 执行各类处理
	 * 
	 * @param campBaseDTO 重算需要的参数
	 * 
	 */
	private void procDateCalc(CampBaseDTO campBaseDTO, Map<String, Object> procMap, String fromDate, String toDate, List<String> dateList) throws Exception {
		// 各类处理日期集合
		Map<String, Object> procDates = campBaseDTO.getProcDates();
		if (procDates.isEmpty()) {
			return;
		}
		String lastDate = dateList.get(dateList.size() - 1);
		// 等级MQ明细业务List
		List<LevelDetailDTO> levelDetailList = (List<LevelDetailDTO>) procMap.get("levelDetailList");
		// 化妆次数MQ明细业务List
		List<BTimesDetailDTO> btimesDetailList = (List<BTimesDetailDTO>) procMap.get("btimesDetailList");
		// 需要执行清零处理
		if (clearFlag) {
			Map<String, Object> zcMap = (Map<String, Object>) procDates.get("ZC");
			if (null != zcMap && !zcMap.isEmpty()) {
				// 清零日期
				String clearDate = (String) zcMap.get("clearDate");
				if (!CherryChecker.isNullOrEmpty(clearDate)) {
					// 清零日期在开始和结束日期之间
					if (DateUtil.compareDate(fromDate, clearDate) <= 0 && DateUtil.compareDate(toDate, clearDate) > 0) {
						// 业务日期
						campBaseDTO.setBusinessDate(clearDate);
						// 单据产生日期
						campBaseDTO.setTicketDate(DateUtil.suffixDate(clearDate, 1));
						// 清零处理
						this.clearData(campBaseDTO);
						// 设置等级MQ明细业务
						this.setLevelDetailDTO(campBaseDTO, levelDetailList);
						// 设置化妆次数MQ明细业务
						this.setBTimesDetailDTO(campBaseDTO, btimesDetailList);
						String nextDay = DateUtil.addDateByDays(DateUtil.DATE_PATTERN, clearDate, 1);
						// 业务日期
						campBaseDTO.setBusinessDate(nextDay);
						procDates.remove("ZC");
						// 重新设置清零处理日期
						String nextCearDate = clearDateSetting(campBaseDTO);
						if (null != nextCearDate && DateUtil.compareDate(nextCearDate, lastDate) < 0) {
							if (DateUtil.compareDate(clearDate, nextCearDate) == 0) {
								BatchExceptionDTO batchExceptionDTO = new BatchExceptionDTO();
								batchExceptionDTO.setBatchName(this.getClass());
								batchExceptionDTO.setErrorLevel(CherryBatchConstants.LOGGER_ERROR);
								// 清零处理日
								batchExceptionDTO.addErrorParam(PropertiesUtil.getMessage("PDR00001", null));
								// 会员ID
								batchExceptionDTO.addErrorParam(String.valueOf(campBaseDTO.getMemberInfoId()));
								// 计算结果
								batchExceptionDTO.addErrorParam(nextCearDate);
								throw new CherryBatchException(batchExceptionDTO);
							}
							dateList.add(nextCearDate);
						}
					}
				}
			}
		}
		if (ptClearFlag) {
			// 清零日期
			String pcTicketDate = (String) procDates.get("RECAL_PCDATE");
			if (!CherryChecker.isNullOrEmpty(pcTicketDate, true)) {
				// 业务日期
				String busDate = DateUtil.coverTime2YMD(pcTicketDate, DateUtil.DATE_PATTERN);
				// 业务日期在开始和结束日期之间
				if (DateUtil.compareDate(fromDate, busDate) == 0) {
					procDates.remove("RECAL_PCDATE");
					campBaseDTO.setBusinessDate(busDate);
					// 会员有效期截止日时
					campBaseDTO.setTicketDate(busDate + " 00:00:00");
					// 积分清零处理
					this.pointClearData(campBaseDTO);
					// 设置积分MQ明细业务
					this.setPointDetailDTO(campBaseDTO, (Map<String, Object>) procMap.get("pointDataMap"));
					List<Map<String, Object>> clearDateList = (List<Map<String, Object>>) procDates.get("CLEARDATES");
					if (null != clearDateList && !clearDateList.isEmpty()) {
						// 下一次清零时间
						String nextClearDate = (String) clearDateList.get(0).get("clearDate");
						if (!CherryChecker.isNullOrEmpty(nextClearDate, true)) {
							// 业务日期
							String clearDay = DateUtil.coverTime2YMD(nextClearDate, DateUtil.DATE_PATTERN);
							// 业务日期在开始和结束日期之间
							if (DateUtil.compareDate(fromDate, clearDay) < 0 && DateUtil.compareDate(clearDay, lastDate) <= 0) {
								dateList.add(clearDay);
								clearDateList.remove(0);
								procDates.put("RECAL_PCDATE", nextClearDate);
							}
						}
					}
				}
			}
		}
		// 需要执行降级处理
		if (downFlag) {
			// 有效期结束日
			String levelEndDate = (String) procDates.get("DG");
			if (!CherryChecker.isNullOrEmpty(levelEndDate)) {
				// 业务日期
				String businessDate = DateUtil.coverTime2YMD(levelEndDate, DateUtil.DATE_PATTERN);
					// 业务日期在开始和结束日期之间
					if (DateUtil.compareDate(fromDate, businessDate) <= 0 && DateUtil.compareDate(toDate, businessDate) > 0) {
					// 业务日期
					campBaseDTO.setBusinessDate(businessDate);
					// 降级处理
					this.downLevel(campBaseDTO);
					// 设置等级MQ明细业务
					this.setLevelDetailDTO(campBaseDTO, levelDetailList);
					// 重新设置降级处理日期
					String nextDownDate = DateUtil.coverTime2YMD(downDateSetting(campBaseDTO), DateUtil.DATE_PATTERN);
					if (null != nextDownDate && DateUtil.compareDate(nextDownDate, lastDate) < 0) {
						if (DateUtil.compareDate(businessDate, nextDownDate) == 0) {
							BatchExceptionDTO batchExceptionDTO = new BatchExceptionDTO();
							batchExceptionDTO.setBatchName(this.getClass());
							batchExceptionDTO.setErrorLevel(CherryBatchConstants.LOGGER_ERROR);
							// 降级处理日
							batchExceptionDTO.addErrorParam(PropertiesUtil.getMessage("PDR00002", null));
							// 会员ID
							batchExceptionDTO.addErrorParam(String.valueOf(campBaseDTO.getMemberInfoId()));
							// 计算结果
							batchExceptionDTO.addErrorParam(nextDownDate);
							throw new CherryBatchException(batchExceptionDTO);
						}
						dateList.add(nextDownDate);
					}
				}
			}
		}
	}
	
	/**
	 * 
	 * 设置各类处理日期
	 * 
	 * @param campBaseDTO 重算需要的参数
	 * 
	 */
	private void procDateSetting(CampBaseDTO campBaseDTO) throws Exception {
		// 设置清零处理日期
		clearDateSetting(campBaseDTO);
		// 设置降级处理日期
		downDateSetting(campBaseDTO);
	}
	/**
	 * 
	 * 设置降级处理日期
	 * 
	 * @param campBaseDTO 重算需要的参数
	 * 
	 */
	private String downDateSetting(CampBaseDTO campBaseDTO) throws Exception {
		// 各类处理日期集合
		Map<String, Object> procDates = campBaseDTO.getProcDates();
		// 降级处理日期
		String downDate = campBaseDTO.getLevelEndDate();
		// 需要执行降级处理
		if (downFlag) {
			// 降级处理日期
			procDates.put("DG", downDate);
		}
		return downDate;
	}
	
	/**
	 * 
	 * 设置清零处理日期
	 * 
	 * @param campBaseDTO 重算需要的参数
	 * 
	 */
	private String clearDateSetting(CampBaseDTO campBaseDTO) throws Exception {
		// 各类处理日期集合
		Map<String, Object> procDates = campBaseDTO.getProcDates();
		// 需要执行清零处理
		if (clearFlag) {
			if (1 != campBaseDTO.getCurLevelId()) {
				procDates.remove("ZC");
				return null;
			}
			// 入会日期
			String joinDate = campBaseDTO.getJoinDate();
			if (!CherryChecker.isNullOrEmpty(joinDate)) {
				Map<String, Object> zcMap = (Map<String, Object>) procDates.get("ZC");
				boolean calcFlag = false;
				if (null == zcMap || zcMap.isEmpty()) {
					calcFlag = true;
				} else {
					// 计算基准日期
					String startDate = (String) zcMap.get("startDate");
					if (!joinDate.equals(startDate)) {
						calcFlag = true;
					}
				}
				if (calcFlag) {
					if (null == zcMap) {
						zcMap = new HashMap<String, Object>();
					}
					// 根据会员有效期限计算出清零日期
					String clearDate = CampRuleUtil.calcClearDate(joinDate, campBaseDTO.getBusinessDate(), 12);
					zcMap.put("startDate", joinDate);
					zcMap.put("clearDate", clearDate);
					procDates.put("ZC", zcMap);
					return clearDate;
				}
			}
		}
		return null;
	}
	
	/**
	 * 
	 * 清零处理
	 * 
	 * @param map 重算信息
	 * @param campBaseDTO 重算需要的参数
	 * 
	 */
	public void clearData(CampBaseDTO campBaseDTO) throws Exception {
		
		// 变更前会员等级
		campBaseDTO.setOldLevelId(campBaseDTO.getCurLevelId());
		// 变更前累计金额
		campBaseDTO.setOldTotalAmount(campBaseDTO.getCurTotalAmount());
		// 变更前化妆次数
		campBaseDTO.setOldBtimes(campBaseDTO.getCurBtimes());
		// 变更前积分
		campBaseDTO.setOldPoint(campBaseDTO.getCurPoint());
		// 变更前可兑换金额(化妆次数用)
		campBaseDTO.setOldBtimesAmount(campBaseDTO.getCurBtimesAmount());
		// 单据号
		campBaseDTO.setBillId(null);
		// 理由
		campBaseDTO.setReason(DroolsConstants.REASON_2);
		// 业务类型
		campBaseDTO.setTradeType(DroolsConstants.TRADETYPE_ZC);
		// 每种履历下的操作类型和重算次数信息
		campBaseDTO.setRecordKbnInfo(new HashMap<String, Object>());
		// 升降级区分
		campBaseDTO.setChangeType(null);
		// 处理规则文件
		binBEDRHAN02_BL.executeRuleFile(campBaseDTO);
	}
	
	/**
	 * 
	 * 降级处理
	 * 
	 * @param campBaseDTO 重算需要的参数
	 * 
	 */
	public void downLevel(CampBaseDTO campBaseDTO) throws Exception {
		// 业务类型
		campBaseDTO.setTradeType(DroolsConstants.TRADETYPE_DG);
		// 每种履历下的操作类型和重算次数信息
		campBaseDTO.setRecordKbnInfo(new HashMap<String, Object>());
		// 升降级区分
		campBaseDTO.setChangeType(null);
		// 处理规则文件
		binBEDRHAN03_BL.executeRuleFile(campBaseDTO);
	}
	
	/**
	 * 
	 * 积分清零处理
	 * 
	 * @param map 重算信息
	 * @param campBaseDTO 重算需要的参数
	 * 
	 */
	public void pointClearData(CampBaseDTO campBaseDTO) throws Exception {
		campBaseDTO.setPointInfo(null);
		// 处理规则文件
		binBEDRHAN04_BL.executeRuleFile(campBaseDTO);
	}
	
	/**
	 * 
	 * 按会员初始数据重算会员等级和化妆次数
	 * 
	 * @param map 会员初始数据
	 * @param campBaseDTO 重算需要的参数
	 * 
	 */
	public void reCalcMemInitData(Map<String, Object> map, CampBaseDTO campBaseDTO) throws Exception {
		
		// 当前会员等级
		String memberLevel = (String)map.get("memberLevel");
		if(memberLevel != null && !"".equals(memberLevel)) {
			// 当前会员等级ID
			campBaseDTO.setCurLevelId(Integer.parseInt(memberLevel));
			// 等级发生变化的场合，更新会员等级信息
			if(campBaseDTO.getCurLevelId() != campBaseDTO.getOldLevelId() ||
					campBaseDTO.getBillId().startsWith("LAD")) {
				Map<String, Object> paramMap = new HashMap<String, Object>();
				// 变更前会员等级级别
				int oldMemLevelGrade = 0;
				// 变更后会员等级级别
				int memLevelGrade = 0;
				if(campBaseDTO.getOldLevelId() != 0) {
					// 取得变更前会员等级级别
					oldMemLevelGrade = this.getMemLevelGrade(String.valueOf(campBaseDTO.getOldLevelId()));
				}
				if(campBaseDTO.getCurLevelId() != 0) {
					// 取得变更后会员等级级别
					memLevelGrade = this.getMemLevelGrade(String.valueOf(campBaseDTO.getCurLevelId()));
				}
				// 变更后会员等级级别比变更前等级级别大的场合，表示会员升级，否则表示降级
				if(memLevelGrade >= oldMemLevelGrade) {
					// 变化类型
					campBaseDTO.setChangeType("1");
				} else {
					// 变化类型
					campBaseDTO.setChangeType("2");
				}
				// 会员ID
				paramMap.put("memberInfoId", campBaseDTO.getMemberInfoId());
				// 变更后等级ID
				paramMap.put("memberLevelId", campBaseDTO.getCurLevelId());
				// 变更前等级ID
				paramMap.put("oldMemberLevelId", campBaseDTO.getOldLevelId());
				// 业务时间
				paramMap.put("levelAdjustDay", campBaseDTO.getTicketDate());
				// 变化类型
				paramMap.put("changeType", campBaseDTO.getChangeType());
				// 会员等级状态
				paramMap.put("levelStatus", "2");
				// 取得会员等级有效期开始日和结束日
				Map<String, Object> levelDateInfo = binOLCM31_BL.getLevelDateInfo(campBaseDTO.getTradeType(), campBaseDTO.getTicketDate(), campBaseDTO.getCurLevelId(), null, campBaseDTO.getMemberInfoId());
				if (null != levelDateInfo && !levelDateInfo.isEmpty()) {
					// 等级有效期开始日
					paramMap.put("levelStartDate", levelDateInfo.get("levelStartDate"));
					// 等级有效期结束日
					paramMap.put("levelEndDate", levelDateInfo.get("levelEndDate"));
				}
				// 更新者
				paramMap.put(CherryBatchConstants.UPDATEDBY, CherryBatchConstants.UPDATE_NAME);
				// 更新程序名
				paramMap.put(CherryBatchConstants.UPDATEPGM, "BINBEDRHAN01");
				int clubId = campBaseDTO.getMemberClubId();
				if (clubId != 0) {
					paramMap.put(CherryBatchConstants.CREATEDBY, "BINBEDRHAN01");
					paramMap.put(CherryBatchConstants.CREATEPGM, "BINBEDRHAN01");
					paramMap.put("memberClubId", clubId);
					paramMap.put("acquiTime", campBaseDTO.getTicketDate());
					paramMap.put("firstBillID", campBaseDTO.getBillId());
					//  更新会员俱乐部等级信息（带排他）
					if (binBEDRHAN01_Service.updateMemberClubInfoExc(paramMap) == 0) {
						binBEDRHAN01_Service.addMemClubLevelExc(paramMap);
					}
				} else {
					// 更新会员等级
					binBEDRHAN01_Service.updateMemberLevel(paramMap);
				}
			}			
			// 插入规则执行履历表
			binbedrcom01BL.addRuleExecRecord(campBaseDTO, DroolsConstants.RECORDKBN_0, true);
		}
		String changeType = campBaseDTO.getChangeType();
		campBaseDTO.setChangeType(null);
		// 当前化妆次数
		String curBtimes = (String)map.get("curBtimes");
		if(curBtimes != null && !"".equals(curBtimes)) {
			// 当前化妆次数
			campBaseDTO.setCurBtimes(Integer.parseInt(curBtimes));
			// 插入规则执行履历表
			binbedrcom01BL.addRuleExecRecord(campBaseDTO, DroolsConstants.RECORDKBN_2, true);
		}
		
		// 当前累计金额
		String totalAmounts = (String)map.get("totalAmounts");
		if(totalAmounts != null && !"".equals(totalAmounts)) {
			// 当前累计金额
			campBaseDTO.setCurTotalAmount(Double.parseDouble(totalAmounts));
			// 插入规则执行履历表
			binbedrcom01BL.addRuleExecRecord(campBaseDTO, DroolsConstants.RECORDKBN_1, true);
			// 组织代码
			String orgCode = campBaseDTO.getOrgCode();
			// 品牌代码
			String brandCode = campBaseDTO.getBrandCode();
			// 会员化妆次数规则处理
			CampRuleExec_IF campRuleExec = binBEMQMES98_BL.getRuleExec(orgCode, brandCode, CherryConstants.HANDLERTYPE_RE03);
			if (null != campRuleExec) {
				// 重新计算可兑换金额
				campRuleExec.ruleExec(campBaseDTO);
			}
		}
		
		// 当前有效积分
		String curPoints = (String)map.get("curPoints");
		if(curPoints != null && !"".equals(curPoints)) {
			// 当前有效积分
			campBaseDTO.setCurPoint(Double.parseDouble(curPoints));
			// 插入规则执行履历表
			binbedrcom01BL.addRuleExecRecord(campBaseDTO, DroolsConstants.RECORDKBN_3, true);
		}
		campBaseDTO.setChangeType(changeType);
	}
	
	/**
	 * 
	 * 按化妆次数使用重算会员等级和化妆次数
	 * 
	 * @param map 化妆次数使用记录
	 * @param campBaseDTO 重算需要的参数
	 * 
	 */
	public void reCalcMemUsedCount(Map<String, Object> map, CampBaseDTO campBaseDTO) throws Exception {
		
		// 取得使用的化妆次数
		String usedCount = (String)map.get("usedCount");
		if(usedCount != null && !"".equals(usedCount)) {
			// 当前化妆次数
			campBaseDTO.setCurBtimes(campBaseDTO.getCurBtimes() + Integer.parseInt(usedCount));
			// 插入规则执行履历表
			binbedrcom01BL.addRuleExecRecord(campBaseDTO, DroolsConstants.RECORDKBN_2, true);
		}
	}
	
	/**
	 * 
	 * 按积分维护数据重算会员的积分
	 * 
	 * @param map 积分维护数据
	 * @param campBaseDTO 重算需要的参数
	 * 
	 */
	public void reCalcMemPointData(Map<String, Object> map, CampBaseDTO campBaseDTO, Map<String, Object> reCalcInfo) throws Exception {
		String initialTime = (String) campBaseDTO.getExtArgs().get("INITIALTIME");
		// 重算的时候
		if (!CherryChecker.isNullOrEmpty(initialTime)) {
			Calendar cal1 = Calendar.getInstance();
			String ticketDate = campBaseDTO.getTicketDate();
			if (CherryChecker.checkDate(ticketDate)) {
				ticketDate = ticketDate + " 00:00:00";
			} else {
				int index = ticketDate.indexOf(".");
				if (index > 0) {
					ticketDate = ticketDate.substring(0, index);
				}
			}
			cal1.setTime(DateUtil.coverString2Date(ticketDate, DateUtil.DATETIME_PATTERN));
			Calendar cal2 = Calendar.getInstance();
			cal2.setTime(DateUtil.coverString2Date(initialTime, DateUtil.DATETIME_PATTERN));
			// 比较单据时间是否在初始导入时间之前
			if (cal1.before(cal2)) {
				return;
			}
		}
		int clubId = campBaseDTO.getMemberClubId();
		if (0 != clubId) {
			map.put("memberClubId", clubId);
		}
		// 取得会员当前积分信息(实体)
		PointDTO pointInfo = binOLCM31_BL.getMemberPointDTO(map);
		if (null == pointInfo) {
			pointInfo = new PointDTO();
		}
		campBaseDTO.setPointInfo(pointInfo);
		// 取得子业务类型
		String subTradeType = (String)map.get("subTradeType");
		// 修改前积分
		double totalPoint = pointInfo.getCurTotalPoint();
		// 可兑换积分
		double ChangablePoint = pointInfo.getCurChangablePoint();
		// 修改后当前积分
		double curPoint = 0;
		// 积分差值
		double subPoint = 0;
		Long recordId = null;
		int tmallPointId = 0;
		String mixMobile = null;
		int tmRecallFlag = 0;
		if (map.get("tmRecordId") != null) {
			recordId = Long.parseLong(String.valueOf(map.get("tmRecordId")));
		}
		if (map.get("tmRecallFlag") != null) {
			tmRecallFlag = Integer.parseInt(String.valueOf(map.get("tmRecallFlag")));
		}
		boolean isTmpt = null != recordId && tmRecallFlag == 9;
		// 积分类型
		String pointType = null;
		// 修改积分总值的场合
		if("1".equals(subTradeType)) {
			Object curPointObj = map.get("curPoints");
			if (null != curPointObj) {
				curPoint = Double.parseDouble(curPointObj.toString().trim());
			}
			subPoint = DoubleUtil.sub(curPoint, totalPoint);
			pointType = DroolsConstants.POINTTYPE0;
		} else { // 修改积分差值的场合
			Object subPointObj = map.get("usedCount");
			if (null != subPointObj) {
				subPoint = Double.parseDouble(subPointObj.toString().trim());
			}
			curPoint = DoubleUtil.add(totalPoint, subPoint);
			pointType = DroolsConstants.POINTTYPE99;
		}
		if (isTmpt) {
			if (null == tmRecallList) {
				tmRecallList = new ArrayList<Map<String, Object>>();
			}
			map.put(CherryBatchConstants.UPDATEDBY, "BINBEDRHAN01");
			map.put(CherryBatchConstants.UPDATEPGM, "BINBEDRHAN01");
			Map<String, Object> tmPointInfo = binBEDRHAN01_Service.getTMPointInfo(map);
			mixMobile = (String) tmPointInfo.get("tmallMixMobile");
			tmallPointId = Integer.parseInt(tmPointInfo.get("tmallPointId").toString());
			// 积分不足
			if (curPoint < 0 && subPoint < 0) {
				String tmErrCode = "deduct-fail:point-no-enough";
				map.put("tmallPointId", tmallPointId);
				map.put("ptFlag", 2);
				map.put("ptResult", 1);
				map.put("tmErrCode", tmErrCode);
				// 更新会员天猫积分信息
				binBEDRHAN01_Service.updateTMPointInfo(map);
				map.put("usedVdFlag", 0);
				map.put("tmallRecallFlag", 0);
				// 更新积分变化主表
				binBEDRHAN01_Service.updateTMUsedInfo(map);
				Map<String, Object> tmRecallInfo = new HashMap<String, Object>();
				tmRecallInfo.put("mixMobile", mixMobile);
				tmRecallInfo.put("recordId", recordId);
				tmRecallInfo.put("tmErrCode", tmErrCode);
				tmRecallInfo.put("memUsedInfoId", map.get("memUsedInfoId"));
				tmRecallList.add(tmRecallInfo);
				return;
			}
		}
		// 当前可兑换积分
		double curChangePoint = DoubleUtil.add(ChangablePoint, subPoint);
		// 会员积分变化主记录
		PointChangeDTO pointChange = new PointChangeDTO();
		if (0 != clubId) {
			pointInfo.setMemberClubId(clubId);
			pointInfo.setClubIdStr(String.valueOf(clubId));
			pointChange.setMemberClubId(clubId);
			pointChange.setClubIdStr(pointInfo.getClubIdStr());
		}
		// 会员积分变化明细记录
    	List<PointChangeDetailDTO> changeDetailList = new ArrayList<PointChangeDetailDTO>();
    	// 所属组织ID
    	pointChange.setOrganizationInfoId(campBaseDTO.getOrganizationInfoId());
    	// 所属品牌ID
    	pointChange.setBrandInfoId(campBaseDTO.getBrandInfoId());
    	// 单据号
    	pointChange.setTradeNoIF((String) map.get("billId"));
    	// 业务类型
    	pointChange.setTradeType((String) map.get("tradeType"));
    	// 会员信息ID
    	pointChange.setMemberInfoId(campBaseDTO.getMemberInfoId());
    	// 会员卡号
    	pointChange.setMemCode((String) map.get("memCode"));
    	// 积分变化日期
    	pointChange.setChangeDate((String) map.get("ticketDate"));
    	// 积分值 
    	pointChange.setPoint(subPoint);
    	// 员工ID
    	pointChange.setEmployeeId(campBaseDTO.getEmployeeId());
    	// 柜台ID
    	pointChange.setOrganizationId(campBaseDTO.getOrganizationId());
    	// 会员积分变化明细
    	PointChangeDetailDTO changeDetail = new PointChangeDetailDTO();
    	// 积分值
    	changeDetail.setPoint(subPoint);
    	// 积分类型
    	changeDetail.setPointType(pointType);
    	// 理由
    	changeDetail.setReason((String) map.get("reasonText"));
    	changeDetailList.add(changeDetail);
    	pointChange.setChangeDetailList(changeDetailList);
    	pointInfo.setPointChange(pointChange);
    	// 积分类型
    	String maintainType = (String) map.get("maintainType");
    	if (null != maintainType && !"".equals(maintainType)) {
    		// 扩展信息
    		Map<String, Object> extParams = changeDetail.getExtParams();
    		if (null == extParams) {
    			extParams = new HashMap<String, Object>();
    			changeDetail.setExtParams(extParams);
    		}
    		extParams.put("PTMAINTYPE", maintainType);
    	}
    	// 处理会员积分变化信息
    	binOLCM31_BL.execPointChangeInfo(campBaseDTO);
    	// 当前积分
    	map.put("curTotalPoint", curPoint);
    	// 可兑换积分
    	map.put("curChangablePoint", curChangePoint);
    	// 设置组织ID
    	map.put("organizationInfoId", campBaseDTO.getOrganizationInfoId());
		// 设置品牌ID
    	map.put("brandInfoId", campBaseDTO.getBrandInfoId());
    	// 不更新积分最后变化时间
    	map.put("lcTimeKbn", "1");
    	// 更新会员积分信息表
    	binOLCM31_BL.updateMemberPointInfo(map);
    	// 改变前的累计积分
		pointInfo.setOldTotalPoint(totalPoint);
		// 当前累计积分
		pointInfo.setCurTotalPoint(curPoint);
		// 插入规则执行履历表:累计积分
		binbedrcom01BL.addRuleExecRecord(campBaseDTO, DroolsConstants.RECORDKBN_5, true);
		// 改变前的可兑换积分
		pointInfo.setOldChangablePoint(ChangablePoint);
		pointInfo.setCurChangablePoint(curChangePoint);
		// 插入规则执行履历表:可兑换积分
		binbedrcom01BL.addRuleExecRecord(campBaseDTO, DroolsConstants.RECORDKBN_7, true);
		if (isTmpt) {
			map.put("tmallPointId", tmallPointId);
			map.put("ptFlag", 2);
			map.put("ptResult", 0);
			// 更新会员天猫积分信息
			binBEDRHAN01_Service.updateTMPointInfo(map);
			map.put("tmallRecallFlag", 0);
			// 更新积分变化主表
			binBEDRHAN01_Service.updateTMUsedInfo(map);
			Map<String, Object> tmRecallInfo = new HashMap<String, Object>();
			tmRecallInfo.put("mixMobile", mixMobile);
			tmRecallInfo.put("recordId", recordId);
			tmRecallInfo.put("memUsedInfoId", map.get("memUsedInfoId"));
			tmRecallList.add(tmRecallInfo);
		}
	}
	
	/**
	 * 
	 * 按会员初始积分数据重算会员的积分
	 * 
	 * @param map 积分维护数据
	 * @param campBaseDTO 重算需要的参数
	 * 
	 */
	public void reCalcMTPointData(Map<String, Object> map, CampBaseDTO campBaseDTO) throws Exception {
		String initialTime = (String) campBaseDTO.getExtArgs().get("INITIALTIME");
		// 重算的时候
		if (!CherryChecker.isNullOrEmpty(initialTime)) {
			Calendar cal1 = Calendar.getInstance();
			String ticketDate = campBaseDTO.getTicketDate();
			if (CherryChecker.checkDate(ticketDate)) {
				ticketDate = ticketDate + " 00:00:00";
			} else {
				int index = ticketDate.indexOf(".");
				if (index > 0) {
					ticketDate = ticketDate.substring(0, index);
				}
			}
			cal1.setTime(DateUtil.coverString2Date(ticketDate, DateUtil.DATETIME_PATTERN));
			Calendar cal2 = Calendar.getInstance();
			cal2.setTime(DateUtil.coverString2Date(initialTime, DateUtil.DATETIME_PATTERN));
			// 比较单据时间是否在初始导入时间之前
			if (cal1.before(cal2)) {
				return;
			}
		}
		// 取得会员当前积分信息(实体)
		PointDTO pointInfo = binOLCM31_BL.getMemberPointDTO(map);
		if (null == pointInfo) {
			pointInfo = new PointDTO();
		}
		campBaseDTO.setPointInfo(pointInfo);
		// 修改前积分
		double totalPoint = pointInfo.getCurTotalPoint();
		// 可兑换积分
		double ChangablePoint = pointInfo.getCurChangablePoint();
		// 修改后当前积分
		double curPoint = 0;
		// 积分差值
		double subPoint = 0;
		// 积分类型
		String pointType = null;
		// 修改积分差值的场合
		Object subPointObj = map.get("usedCount");
		if (null != subPointObj) {
			subPoint = Double.parseDouble(subPointObj.toString().trim());
		}
		curPoint = DoubleUtil.add(totalPoint, subPoint);
		pointType = DroolsConstants.POINTTYPE99;
		// 当前可兑换积分
		double curChangePoint = DoubleUtil.add(ChangablePoint, subPoint);
		// 会员积分变化主记录
		PointChangeDTO pointChange = new PointChangeDTO();
		// 会员积分变化明细记录
    	List<PointChangeDetailDTO> changeDetailList = new ArrayList<PointChangeDetailDTO>();
    	// 所属组织ID
    	pointChange.setOrganizationInfoId(campBaseDTO.getOrganizationInfoId());
    	// 所属品牌ID
    	pointChange.setBrandInfoId(campBaseDTO.getBrandInfoId());
    	// 单据号
    	pointChange.setTradeNoIF((String) map.get("billId"));
    	// 业务类型
    	pointChange.setTradeType((String) map.get("tradeType"));
    	// 会员信息ID
    	pointChange.setMemberInfoId(campBaseDTO.getMemberInfoId());
    	// 会员卡号
    	pointChange.setMemCode((String) map.get("memCode"));
    	// 积分变化日期
    	pointChange.setChangeDate((String) map.get("ticketDate"));
    	// 积分值 
    	pointChange.setPoint(subPoint);
    	// 员工ID
    	pointChange.setEmployeeId(campBaseDTO.getEmployeeId());
    	// 柜台ID
    	pointChange.setOrganizationId(campBaseDTO.getOrganizationId());
    	// 会员积分变化明细
    	PointChangeDetailDTO changeDetail = new PointChangeDetailDTO();
    	// 积分值
    	changeDetail.setPoint(subPoint);
    	// 积分类型
    	changeDetail.setPointType(pointType);
    	// 理由
    	changeDetail.setReason("初始积分录入");
    	changeDetailList.add(changeDetail);
    	pointChange.setChangeDetailList(changeDetailList);
    	pointInfo.setPointChange(pointChange);
    	// 积分类型
		Map<String, Object> extParams = changeDetail.getExtParams();
		if (null == extParams) {
			extParams = new HashMap<String, Object>();
			changeDetail.setExtParams(extParams);
		}
		extParams.put("PTMAINTYPE", "4");
    	// 处理会员积分变化信息
    	binOLCM31_BL.execPointChangeInfo(campBaseDTO);
    	// 当前积分
    	map.put("curTotalPoint", curPoint);
    	// 可兑换积分
    	map.put("curChangablePoint", curChangePoint);
    	// 设置组织ID
    	map.put("organizationInfoId", campBaseDTO.getOrganizationInfoId());
		// 设置品牌ID
    	map.put("brandInfoId", campBaseDTO.getBrandInfoId());
    	// 不更新积分最后变化时间
    	map.put("lcTimeKbn", "1");
    	// 更新会员积分信息表
    	binOLCM31_BL.updateMemberPointInfo(map);
    	// 改变前的累计积分
		pointInfo.setOldTotalPoint(totalPoint);
		// 当前累计积分
		pointInfo.setCurTotalPoint(curPoint);
		// 插入规则执行履历表:累计积分
		binbedrcom01BL.addRuleExecRecord(campBaseDTO, DroolsConstants.RECORDKBN_5, true);
		// 改变前的可兑换积分
		pointInfo.setOldChangablePoint(ChangablePoint);
		pointInfo.setCurChangablePoint(curChangePoint);
		// 插入规则执行履历表:可兑换积分
		binbedrcom01BL.addRuleExecRecord(campBaseDTO, DroolsConstants.RECORDKBN_7, true);
	}
	
	/**
	 * 
	 * 按积分兑换预约数据重算会员的积分
	 * 
	 * @param map 积分兑换预约数据
	 * @param campBaseDTO 重算需要的参数
	 * 
	 */
	public void reCalcOrderPointData(Map<String, Object> map, CampBaseDTO campBaseDTO) throws Exception {
		int clubId = campBaseDTO.getMemberClubId();
		if (0 != clubId) {
			map.put("memberClubId", clubId);
		}
		// 取得会员当前积分信息(实体)
		PointDTO pointInfo = binOLCM31_BL.getMemberPointDTO(map);
		if (null == pointInfo) {
			pointInfo = new PointDTO();
		}
		campBaseDTO.setPointInfo(pointInfo);
		// 查询预约单明细信息
		List<Map<String, Object>> orderDetailList = binBEDRHAN01_Service.getOrderDetailList(campBaseDTO);
		if (null != orderDetailList && !orderDetailList.isEmpty()) {
			// 预约总积分
    		double totalPointRequired = Double.parseDouble(map.get("totalPointRequired").toString());
    		// 总数量
    		double totalQuantity = Double.parseDouble(map.get("totalQuantity").toString());
    		// 总金额
    		double totalAmout = Double.parseDouble(map.get("totalAmout").toString());
    		// 会员积分变化主记录
			PointChangeDTO pointChange = new PointChangeDTO();
			// 所属组织ID
	    	pointChange.setOrganizationInfoId(campBaseDTO.getOrganizationInfoId());
	    	// 所属品牌ID
	    	pointChange.setBrandInfoId(campBaseDTO.getBrandInfoId());
	    	// 单据号
	    	pointChange.setTradeNoIF(campBaseDTO.getBillId());
	    	// 业务类型
	    	pointChange.setTradeType(campBaseDTO.getTradeType());
	    	// 会员信息ID
	    	pointChange.setMemberInfoId(campBaseDTO.getMemberInfoId());
	    	// 会员卡号
	    	pointChange.setMemCode(campBaseDTO.getMemCode());
	    	// 积分变化日期
	    	pointChange.setChangeDate(campBaseDTO.getTicketDate());
	    	// 积分值 
	    	pointChange.setPoint(-totalPointRequired);
	    	// 组织结构ID
			pointChange.setOrganizationId(campBaseDTO.getOrganizationId());
	    	// 员工ID
	    	pointChange.setEmployeeId(campBaseDTO.getEmployeeId());
	    	// 整单金额
			pointChange.setAmount(totalAmout);
			// 整单数量
			pointChange.setQuantity(totalQuantity);
			// 机器号
			Object machineCodeObj = map.get("machineCode");
			if (null != machineCodeObj) {
				pointChange.setMachineCode(String.valueOf(machineCodeObj));
			}
			double totalPointPB = 0;
			List<PointChangeDetailDTO> changeDetailList = new ArrayList<PointChangeDetailDTO>();
			for (Map<String, Object> detailInfo : orderDetailList) {
				PointChangeDetailDTO pointChangeDetail = new PointChangeDetailDTO();
				
				// 厂商编码
				pointChangeDetail.setUnitCode((String) detailInfo.get("unitCode"));
				// 产品条码
				pointChangeDetail.setBarCode((String) detailInfo.get("barCode"));
				// 促销品/产品厂商ID
				pointChangeDetail.setPrmPrtVendorId(Integer.parseInt(String.valueOf(detailInfo.get("productVendorId"))));
				// 销售类型
				String saleType = (String) detailInfo.get("giftType");
				pointChangeDetail.setSaleType(saleType);
				// 定价
				pointChangeDetail.setPrice(Double.parseDouble(detailInfo.get("amout").toString()));
				// 数量
				double quantity = Double.parseDouble(detailInfo.get("quantity").toString());
				pointChangeDetail.setQuantity(quantity);
				double pointRequired = Double.parseDouble(detailInfo.get("pointRequired").toString());
				if (0 != pointRequired) {
					pointRequired = -DoubleUtil.mul(pointRequired, quantity);
					// 积分值
					pointChangeDetail.setPoint(pointRequired);
					totalPointPB = DoubleUtil.add(totalPointPB, pointRequired);
				}
				// 积分类型
				pointChangeDetail.setPointType("5");
		    	// 理由
				pointChangeDetail.setReason(MessageConstants.MSG_INFO_01);
				changeDetailList.add(pointChangeDetail);
			}
			// 总积分有误
//			if (totalPointPB != pointChange.getPoint()) {
//				BatchExceptionDTO batchExceptionDTO = new BatchExceptionDTO();
//				batchExceptionDTO.setBatchName(this.getClass());
//				batchExceptionDTO.setErrorCode("EDR00053");
//				batchExceptionDTO.setErrorLevel(CherryBatchConstants.LOGGER_ERROR);
//				// 会员ID
//				batchExceptionDTO.addErrorParam(campBaseDTO.getBillId());
//				throw new CherryBatchException(batchExceptionDTO);
//			}
			pointChange.setChangeDetailList(changeDetailList);
			// 更新前积分
			double totalPoint = pointInfo.getCurTotalPoint();
			// 更新前可兑换积分
			double ChangablePoint = pointInfo.getCurChangablePoint();
			// 更新前累计兑换积分
			double totalChanged = pointInfo.getCurTotalChanged();
			// 更新后当前积分
			double curPoint = DoubleUtil.sub(totalPoint, totalPointRequired);;
			// 更新后可兑换积分
			double curChangablePoint = DoubleUtil.sub(ChangablePoint, totalPointRequired);
			// 更新后累计兑换积分
			double curTotalChanged = DoubleUtil.add(totalChanged, totalPointRequired);
			// 当前总积分
	    	pointInfo.setCurTotalPoint(curPoint);
	    	// 累计兑换积分
	    	pointInfo.setCurTotalChanged(curTotalChanged);
	    	// 可兑换积分
	    	pointInfo.setCurChangablePoint(curChangablePoint);
	    	pointInfo.setPointChange(pointChange);
	    	if (0 != clubId) {
				pointInfo.setMemberClubId(clubId);
				pointInfo.setClubIdStr(String.valueOf(clubId));
				pointChange.setMemberClubId(clubId);
				pointChange.setClubIdStr(pointInfo.getClubIdStr());
			}
	    	// 处理会员积分变化信息
	    	binOLCM31_BL.execPointChangeInfo(campBaseDTO);
	    	// 当前积分
	    	map.put("curTotalPoint", curPoint);
	    	// 可兑换积分
	    	map.put("curChangablePoint", curChangablePoint);
	    	// 累计兑换积分
	    	map.put("curTotalChanged", curTotalChanged);
	    	// 设置组织ID
	    	map.put("organizationInfoId", campBaseDTO.getOrganizationInfoId());
			// 设置品牌ID
	    	map.put("brandInfoId", campBaseDTO.getBrandInfoId());
	    	// 不更新积分最后变化时间
	    	map.put("lcTimeKbn", "1");
	    	// 更新会员积分信息表
	    	binOLCM31_BL.updateMemberPointInfo(map);
	    	if (totalPoint != curPoint) {
		    	// 改变前的累计积分
				pointInfo.setOldTotalPoint(totalPoint);
				// 当前累计积分
				pointInfo.setCurTotalPoint(curPoint);
				// 插入规则执行履历表:累计积分
				binbedrcom01BL.addRuleExecRecord(campBaseDTO, DroolsConstants.RECORDKBN_5, true);
	    	}
	    	if (ChangablePoint != curChangablePoint) {
				// 改变前的可兑换积分
				pointInfo.setOldChangablePoint(ChangablePoint);
				// 当前可兑换积分
				pointInfo.setCurChangablePoint(curChangablePoint);
				// 插入规则执行履历表:可兑换积分
				binbedrcom01BL.addRuleExecRecord(campBaseDTO, DroolsConstants.RECORDKBN_7, true);
	    	}
	    	if (totalChanged != curTotalChanged) {
	    		// 改变前的累计兑换积分
	    		pointInfo.setOldTotalChanged(totalChanged);
	    		// 当前累计兑换积分
	    		pointInfo.setCurTotalChanged(curTotalChanged);
	    		// 插入规则执行履历表:累计兑换积分
				binbedrcom01BL.addRuleExecRecord(campBaseDTO, DroolsConstants.RECORDKBN_6, true);
	    	}
		}
	}
	
	/**
	 * 
	 * 发送等级下发MQ消息
	 * 
	 * @param campBaseDTO 重算需要的参数
	 * @param levelDetailList 等级MQ明细业务数据List
	 * @param delRecordList 需要删除的履历记录
	 * 
	 */
	public void sendLevelMes(CampBaseDTO campBaseDTO, List<LevelDetailDTO> levelDetailList, List<Map<String, Object>> delRecordList) throws Exception {
		// 不需要下发等级MQ
		if(1 == campBaseDTO.getMemRegFlg() || DroolsConstants.NO_LEVEL_MQ.equals(
				campBaseDTO.getExtArgs().get(DroolsConstants.LEVEL_MQ_KBN)) || 
				(levelDetailList == null || levelDetailList.isEmpty()) 
				&& (delRecordList == null || delRecordList.isEmpty())) {
			return;
		}
		LevelMainDTO levelMainDTO = new LevelMainDTO();
		// 品牌代码
		levelMainDTO.setBrandCode(campBaseDTO.getBrandCode());
		// 单据号
		String ticketNumber = binOLCM03_BL.getTicketNumber(String.valueOf(campBaseDTO.getOrganizationInfoId()), 
				String.valueOf(campBaseDTO.getBrandInfoId()), campBaseDTO.getCreatedBy(), DroolsConstants.MQ_BILLTYPE_ML);
		levelMainDTO.setTradeNoIF(ticketNumber);
		// 会员卡号
		levelMainDTO.setMemberCode(campBaseDTO.getMemCode());
		// 修改回数
		levelMainDTO.setModifyCounts(DroolsConstants.DEF_MODIFYCOUNTS);
		// 业务类型
		levelMainDTO.setTradeType(DroolsConstants.MQ_BILLTYPE_ML);
		// 计算时间
		levelMainDTO.setCaltime(campBaseDTO.getCalcDate());
		// 开卡等级
		String grantMemberLevelCode = this.getMemLevelCode(String.valueOf(campBaseDTO.getGrantMemberLevel()));
		levelMainDTO.setGrantMemberLevel(grantMemberLevelCode);
		if(levelDetailList != null && !levelDetailList.isEmpty()) {
			// 取得最新的等级明细
			LevelDetailDTO levelDetailDTO = levelDetailList.get(levelDetailList.size()-1);
			// 当前等级
			levelMainDTO.setMember_level(levelDetailDTO.getMemberlevelNew());
			// 柜台号
			levelMainDTO.setCountercode(levelDetailDTO.getCounterCode());
			// 员工编号
			levelMainDTO.setBacode(levelDetailDTO.getEmployeeCode());
			// 上一次等级
			levelMainDTO.setPrevLevel(levelDetailDTO.getMemberlevelOld());
			// 本次等级变化时间
			levelMainDTO.setLevelAdjustTime(levelDetailDTO.getRelevantTicketDate());
			// 以首单销售为准
			if ("2".equals(jnDateKbn)) {
				// 入会时间
				levelMainDTO.setJoinDate(campBaseDTO.getJoinDate());
			} else {
				// 入会时间
				levelMainDTO.setJoinDate("");
			}
		} else {
			levelDetailList = new ArrayList<LevelDetailDTO>();
			if(campBaseDTO.getUpgradeFromLevel() != null && campBaseDTO.getUpgradeFromLevel() != 0) {
				// 通过会员等级ID取得会员等级代码
				String oldLevelCode = this.getMemLevelCode(String.valueOf(campBaseDTO.getUpgradeFromLevel()));
				// 上一次等级
				levelMainDTO.setPrevLevel(oldLevelCode);
			}
			if(campBaseDTO.getCurLevelId() != 0) {
				// 通过会员等级ID取得会员等级代码
				String newLevelCode = this.getMemLevelCode(String.valueOf(campBaseDTO.getCurLevelId()));
				// 当前等级
				levelMainDTO.setMember_level(newLevelCode);
			}
			// 本次等级变化时间
			levelMainDTO.setLevelAdjustTime(campBaseDTO.getLevelAdjustDay());
			// 以首单销售为准
			if ("2".equals(jnDateKbn)) {
				// 入会时间
				levelMainDTO.setJoinDate(campBaseDTO.getJoinDate());
			} else {
				// 入会时间
				levelMainDTO.setJoinDate("");
			}
			campBaseDTO.setRecordKbn(DroolsConstants.RECORDKBN_0);
			// 查询最后一次引起某一属性变化的单据信息
			Map<String, Object> lastChangeInfo = binbedrcom01BL.getLastChangeInfo(campBaseDTO);
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
					levelMainDTO.setCountercode(counterCode);
					if (null != counterCode && !"".equals(counterCode)) {
						// 员工编号
						levelMainDTO.setBacode((String) baCounterInfo.get("baCode"));
					}
				}
			}
		}
		
		// 存在需要删除的履历记录
		if(delRecordList != null && !delRecordList.isEmpty()) {
			for(Map<String, Object> delRecordMap : delRecordList) {
				LevelDetailDTO delLevelDetailDTO = new LevelDetailDTO();
				// 会员卡号
				delLevelDetailDTO.setMemberCode((String)delRecordMap.get("memCode"));
				// 操作类型
				delLevelDetailDTO.setOperateType(DroolsConstants.OPERATETYPE_D);
				// 关联单号
				delLevelDetailDTO.setRelevantNo((String)delRecordMap.get("billId"));
				// 业务类型
				delLevelDetailDTO.setBizType((String)delRecordMap.get("tradeType"));
				// 重算次数
				delLevelDetailDTO.setReCalcCount(delRecordMap.get("reCalcCount").toString());
				levelDetailList.add(delLevelDetailDTO);
			}
		}
		// 发送所有履历
		if (DroolsConstants.SEND_ALL_RECORDS.equals(
				campBaseDTO.getExtArgs().get(DroolsConstants.SEND_RECORDS_KBN))) {
			// 取得所有等级变化明细
			levelDetailList = binOLCM31_BL.getLevelAllRecords(campBaseDTO, levelDetailList);
		}
		// 等级MQ明细业务List
		levelMainDTO.setLevelDetailList(levelDetailList);
		// MQ消息 DTO
		MQInfoDTO mqInfoDTO = new MQInfoDTO();
		// 所属组织
		mqInfoDTO.setOrganizationInfoId(campBaseDTO.getOrganizationInfoId());
		// 所属品牌
		mqInfoDTO.setBrandInfoId(campBaseDTO.getBrandInfoId());
		// 单据类型
		mqInfoDTO.setBillType(levelMainDTO.getTradeType());
		// 单据号
		mqInfoDTO.setBillCode(ticketNumber);
		// 消息发送接收标志位
		mqInfoDTO.setReceiveFlag(DroolsConstants.MQ_RECEIVEFLAG_0);
		// 取得MQ消息体
		String msg = null;
		if (0 == campBaseDTO.getMemberClubId()) {
			msg = levelMainDTO.getMQMsg();
		} else {
			// 会员俱乐部代号
			levelMainDTO.setClubCode(campBaseDTO.getClubCode());
			msg = levelMainDTO.getClubMQMsg();
		}
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
	    dbObject.put("TradeEntityCode", levelMainDTO.getMemberCode());
	    // 业务主体名称
	    dbObject.put("TradeEntityName", campBaseDTO.getMemName());
	    // 发生时间
	    dbObject.put("OccurTime", levelMainDTO.getCaltime());
	    // 事件内容
	    dbObject.put("Content", mqInfoDTO.getData());
	    mqInfoDTO.setDbObject(dbObject);
	    // 发送MQ消息
	    binOLMQCOM01_BL.sendMQMsg(mqInfoDTO);
	    
	}
	
	/**
	 * 
	 * 发送化妆次数下发MQ消息
	 * 
	 * @param campBaseDTO 重算需要的参数
	 * @param btimesDetailList 化妆次数MQ明细业务数据List
	 * @param delRecordList 需要删除的履历记录
	 * 
	 */
	public void sendBTimesMes(CampBaseDTO campBaseDTO, List<BTimesDetailDTO> btimesDetailList, List<Map<String, Object>> delRecordList) throws Exception {
		
		if((btimesDetailList == null || btimesDetailList.isEmpty()) 
				&& (delRecordList == null || delRecordList.isEmpty())) {
			return;
		}
		if(btimesDetailList == null || btimesDetailList.isEmpty()) {
			btimesDetailList = new ArrayList<BTimesDetailDTO>();
		}
		BTimesMainDTO btimesMainDTO = new BTimesMainDTO();
		// 品牌代码
		btimesMainDTO.setBrandCode(campBaseDTO.getBrandCode());
		// 单据号
		String ticketNumber = binOLCM03_BL.getTicketNumber(String.valueOf(campBaseDTO.getOrganizationInfoId()), 
				String.valueOf(campBaseDTO.getBrandInfoId()), campBaseDTO.getCreatedBy(), DroolsConstants.MQ_BILLTYPE_MG);
		btimesMainDTO.setTradeNoIF(ticketNumber);
		// 会员卡号
		btimesMainDTO.setMemberCode(campBaseDTO.getMemCode());
		// 修改回数
		btimesMainDTO.setModifyCounts(DroolsConstants.DEF_MODIFYCOUNTS);
		// 业务类型
		btimesMainDTO.setTradeType(DroolsConstants.MQ_BILLTYPE_MG);
		// 当前化妆次数
		btimesMainDTO.setCurBtimes(String.valueOf(campBaseDTO.getCurBtimes()));
		// 计算时间
		btimesMainDTO.setCaltime(campBaseDTO.getCalcDate());
		// 存在需要删除的履历记录
		if(delRecordList != null && !delRecordList.isEmpty()) {
			for(Map<String, Object> delRecordMap : delRecordList) {
				BTimesDetailDTO delBTimesDetailDTO = new BTimesDetailDTO();
				// 会员卡号
				delBTimesDetailDTO.setMemberCode((String)delRecordMap.get("memCode"));
				// 操作类型
				delBTimesDetailDTO.setOperateType(DroolsConstants.OPERATETYPE_D);
				// 关联单号
				delBTimesDetailDTO.setRelevantNo((String)delRecordMap.get("billId"));
				// 重算次数
				delBTimesDetailDTO.setReCalcCount(delRecordMap.get("reCalcCount").toString());
				btimesDetailList.add(delBTimesDetailDTO);
			}
		}
		// 化妆次数MQ明细业务List
		btimesMainDTO.setBtimesDetailList(btimesDetailList);
		// MQ消息 DTO
		MQInfoDTO mqInfoDTO = new MQInfoDTO();
		// 所属组织
		mqInfoDTO.setOrganizationInfoId(campBaseDTO.getOrganizationInfoId());
		// 所属品牌
		mqInfoDTO.setBrandInfoId(campBaseDTO.getBrandInfoId());
		// 单据类型
		mqInfoDTO.setBillType(btimesMainDTO.getTradeType());
		// 单据号
		mqInfoDTO.setBillCode(ticketNumber);
		// 消息发送接收标志位
		mqInfoDTO.setReceiveFlag(DroolsConstants.MQ_RECEIVEFLAG_0);
		// 消息体
		mqInfoDTO.setData(btimesMainDTO.getMQMsg());
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
	    dbObject.put("TradeEntityCode", btimesMainDTO.getMemberCode());
	    // 业务主体名称
	    dbObject.put("TradeEntityName", campBaseDTO.getMemName());
	    // 发生时间
	    dbObject.put("OccurTime", btimesMainDTO.getCaltime());
	    // 事件内容
	    dbObject.put("Content", mqInfoDTO.getData());
	    mqInfoDTO.setDbObject(dbObject);
	    // 发送MQ消息
	    binOLMQCOM01_BL.sendMQMsg(mqInfoDTO);
	}
	
	/**
	 * 
	 * 发送积分下发MQ消息
	 * 
	 * @param campBaseDTO 重算需要的参数
	 * @param pointDataMap 积分MQ明细业务
	 * @param delRecordList 需要删除的履历记录
	 * @param befTotalPoint 重算前总积分
	 */
	public void sendPointMes(CampBaseDTO campBaseDTO, Map<String, Object> pointDataMap, List<Map<String, Object>> delRecordList, double befTotalPoint) throws Exception {
		if (1 == campBaseDTO.getMemRegFlg() || null == pointDataMap || pointDataMap.isEmpty()) {
			return;
		}
		// 单据主记录的明细行列表
		List<Map<String, Object>> tradeDataList = (List<Map<String, Object>>) pointDataMap.get("TradeDataList");
		// 单据明细记录的明细行列表
		List<Map<String, Object>> detailDataList = (List<Map<String, Object>>) pointDataMap.get("DetailDataList");
		// 退货关联销售单据的明细行列表
		List<Map<String, Object>> detailSRDataList = (List<Map<String, Object>>) pointDataMap.get("DetailSRDataList");
//		if ((null == tradeDataList || tradeDataList.isEmpty()) 
//				&& (null == delRecordList || delRecordList.isEmpty())) {
//			return;
//		}
		if (null == tradeDataList) {
			tradeDataList = new ArrayList<Map<String, Object>>();
		}
		// 需要删除的积分变化记录
		if (null != delRecordList) {
			for (Map<String, Object> delRecord : delRecordList) {
				// 单据主记录的明细行
				Map<String, Object> tradeDataMap = new HashMap<String, Object>();
				// 会员号
				tradeDataMap.put("Membercode", delRecord.get("memCode"));
				// 机器号
				tradeDataMap.put("MachineCode", "");
				// 单据号
				tradeDataMap.put("Billid", delRecord.get("tradeNoIF"));
				// 业务类型
				tradeDataMap.put("BizType", delRecord.get("tradeType"));
				// 出入库日期
				String tradeDate = "";
				// 出入库时间
				String tradeTime = "";
				String changeTime = (String) delRecord.get("changeDate");
				if (!CherryChecker.isNullOrEmpty(changeTime) && changeTime.length() >= 19) {
					tradeDate = changeTime.substring(0, 10);
					tradeTime = changeTime.substring(11, 19);
				}
				// 出入库日期
				tradeDataMap.put("TradeDate", tradeDate);
				// 出入库时间
				tradeDataMap.put("TradeTime", tradeTime);
				// 柜台号
				tradeDataMap.put("Countercode", "");
				// 变化的积分
				double delPoint = 0;
				if (null != delRecord.get("point")) {
					delPoint = Double.parseDouble(String.valueOf(delRecord.get("point")));
				}
				tradeDataMap.put("Points", DoubleUtil.douToStr(delPoint));
				// 总数量
				double totalQuantity = 0;
				if (null != delRecord.get("quantity")) {
					totalQuantity = Double.parseDouble(String.valueOf(delRecord.get("quantity")));
				}
				tradeDataMap.put("TotalQuantity", String.valueOf(totalQuantity));
				// 总金额
				double totalAmount = 0;
				if (null != delRecord.get("amount")) {
					totalAmount = Double.parseDouble(String.valueOf(delRecord.get("amount")));
				}
				tradeDataMap.put("TotalAmount", String.valueOf(totalAmount));
				// 修改回数
				tradeDataMap.put("ModifyCount", "0");
				// 重算次数
				tradeDataMap.put("ReCalcCount", String.valueOf(delRecord.get("reCalcCount")));
				// 有效区分
				tradeDataMap.put("ValidFlag", "0");
				tradeDataList.add(tradeDataMap);
			}
		}
		Map<String, Object> cardMap = new HashMap<String, Object>();
		// 会员信息ID
		int memberInfoId = campBaseDTO.getMemberInfoId();
		cardMap.put("memberInfoId", memberInfoId);
		// 取得会员卡号
		String memCode = binbedrcom01BL.getMemCard(cardMap);
		if (null == memCode || "".equals(memCode)) {
			// 会员信息无记录
			String errMsg = DroolsMessageUtil.getMessage(DroolsMessageUtil.EDR00007, 
					new String[]{String.valueOf(campBaseDTO.getMemberInfoId())});
			throw new CherryDRException(errMsg);
		}
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
		Map<String, Object> searchMap = new HashMap<String, Object>();
		// 会员信息ID
		searchMap.put("memberInfoId", campBaseDTO.getMemberInfoId());
		if (0 != campBaseDTO.getMemberClubId()) {
			// 会员俱乐部ID
			searchMap.put("memberClubId", campBaseDTO.getMemberClubId());
		}
		// 取得会员当前的积分信息
		PointDTO pointDTO = binbedrcom01BL.getCurMemPointInfo(searchMap);
		if (null == pointDTO) {
			logger.error("Get the current point fail!");
			return;
		}
		// 重算后总积分
		double aftTotalPoint = pointDTO.getCurTotalPoint();
		// 重算前后总积分不一致，更新积分最后变化时间
		if (aftTotalPoint != befTotalPoint) {
			// 更新会员积分最后变化时间
			binBEDRHAN01_Service.updateMemPointLcTime(searchMap);
		}
		// 会员存在多张卡的情况下，重算前卡积分值
		if (!binOLCM31_BL.isSingleCard(memberInfoId)) {
			// 前卡积分值
			double preCardPoint = binOLCM31_BL.recPreCardPoint(memberInfoId, memCode, pointDTO.getCurTotalPoint(), campBaseDTO.getMemberClubId());
			Map<String, Object> upMap = new HashMap<String, Object>();
			// 会员信息ID
			upMap.put("memberInfoId", memberInfoId);
			// 前卡积分值
			upMap.put("preCardPoint", preCardPoint);
			// 更新前卡积分值
			binOLCM31_BL.updatePreCardPoint(upMap);
			pointDTO.setPreCardPoint(preCardPoint);
		}
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
		Map<String, Object> dataLineMap = new HashMap<String, Object>();
		dataLineMap.put("MainData", mainDataMap);
		dataLineMap.put("TradeDataList", tradeDataList);
		dataLineMap.put("DetailDataList", detailDataList);
		if (!detailSRDataList.isEmpty()) {
			dataLineMap.put("DetailSRDataList", detailSRDataList);
		}
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
	    // 发送MQ消息
	    binOLMQCOM01_BL.sendMQMsg(mqInfoDTO);
	}
	
	/**
	 * 
	 * 发送积分奖励MQ消息
	 * 
	 * @param campBaseDTO 重算需要的参数
	 */
	public void sendPointRewardMes(CampBaseDTO campBaseDTO) throws Exception {
		// 积分奖励集合
		Map<String, Object> prMap = (Map<String, Object>) campBaseDTO.getProcDates().get("PR");
		if (prMap.isEmpty()) {
			return;
		}
		List<Map<String, Object>> rewardList = new ArrayList<Map<String, Object>>();
		for(Map.Entry<String,Object> en: prMap.entrySet()){
			Map<String, Object> prBillInfo = (Map<String, Object>) en.getValue();
			rewardList.add(prBillInfo);
		}
		// 按单据时间排序
		this.listSort(rewardList);
		// 奖励撤销
		List<Map<String, Object>> delRewardList = new ArrayList<Map<String, Object>>();
		for (int i = 0; i < rewardList.size(); i++) {
			Map<String, Object> rewardInfo = rewardList.get(i);
			// 更新区分
			String upFlag = (String) rewardInfo.get("UPFLAG");
			if ("0".equals(upFlag) || CherryChecker.isNullOrEmpty(upFlag, true)) {
				delRewardList.add(rewardInfo);
				rewardList.remove(i);
				i--;
			}
		}
		// 将撤销的记录放在前面
		if (!delRewardList.isEmpty()) {
			rewardList.addAll(0, delRewardList);
		}
		// 循环发送到维护积分队列
		for (Map<String, Object> rewardInfo : rewardList) {
			// 积分值
			double point = Double.parseDouble(rewardInfo.get("point").toString());
			//积分维护明细数据
			List<Map<String,Object>> detailDataList = new ArrayList<Map<String,Object>>();
			Map<String,Object> detailMap = new HashMap<String,Object>();
			//会员卡号(推荐会员)
			detailMap.put("MemberCode", campBaseDTO.getReferrerCode());
			if (!isNoClub) {
				// 会员俱乐部ID
				detailMap.put("MemberClubId", campBaseDTO.getMemberClubId());
			}
			//修改的积分
			detailMap.put("ModifyPoint", point);
			detailMap.put("BusinessTime", rewardInfo.get("ticketDate"));
			// 关联单号
			String billId = (String) rewardInfo.get("tradeNoIF");
			// 会员卡号
			String memCode = (String) rewardInfo.get("memCode");
			//备注
			String reason = DroolsMessageUtil.getMessage(
					DroolsMessageUtil.IDR00022, new String[] {campBaseDTO.getMemName(), memCode, billId});
			detailMap.put("Reason", reason);
			//员工Code
			detailMap.put("EmployeeCode", ConvertUtil.getString(rewardInfo.get("baCode")));
			detailDataList.add(detailMap);
			//设定MQ消息DTO
			MQInfoDTO mqInfoDTO = new MQInfoDTO();
			// 品牌代码
			String brandCode = campBaseDTO.getBrandCode();
			mqInfoDTO.setBrandCode(brandCode);
			// 组织代码
			String orgCode = campBaseDTO.getOrgCode();
			mqInfoDTO.setOrgCode(orgCode);
			// 组织ID
			mqInfoDTO.setOrganizationInfoId(campBaseDTO.getOrganizationInfoId());
			// 品牌ID
			mqInfoDTO.setBrandInfoId(campBaseDTO.getBrandInfoId());
			//单据类型
			String billType = CherryConstants.MESSAGE_TYPE_PT;;
			//单据号
			String billCode = binOLCM03_BL.getTicketNumber(campBaseDTO.getOrganizationInfoId(), 
					campBaseDTO.getBrandInfoId(), "", billType);
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
			mainData.put("BrandCode", brandCode);
			// 业务类型
			mainData.put("TradeType", billType);
			// 单据号
			mainData.put("TradeNoIF", billCode);
			// 数据来源
			mainData.put("Sourse", "Cherry");
			//修改模式
			mainData.put("SubTradeType","2");
			// 积分类型:奖励积分
			mainData.put("MaintainType", "1");
			// 关联单号
			mainData.put("RelevantNo", billId);
			// 柜台号
			mainData.put("CounterCode", ConvertUtil.getString(rewardInfo.get("countercode")));
			// 更新区分
			String upFlag = (String) rewardInfo.get("UPFLAG");
			if ("1".equals(upFlag)) {
				// 更新
				mainData.put("ptUpFlag", "1");
			} else if ("0".equals(upFlag) || CherryChecker.isNullOrEmpty(upFlag, true)) {
				// 撤销
				mainData.put("ptUpFlag", "0");
			} else if ("2".equals(upFlag)) {
				// 关联的奖励单号
				rewardInfo.put("relevantUsedNo", billCode);
				// 更新积分变化关联的奖励单号
				binBEDRHAN01_Service.upReleUsedNo(rewardInfo);
			}
			dataLine.put("MainData", mainData);
			//积分明细
			dataLine.put("DetailDataDTOList", detailDataList);
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
			// 数据来源
			dbObject.put("Sourse", "Cherry");
			mqInfoDTO.setDbObject(dbObject);
			// 发送MQ消息
			binOLMQCOM01_BL.sendMQMsg(mqInfoDTO);
		}
	}
	
	/**
	 * 
	 * 设置等级MQ明细业务
	 * 
	 * @param campBaseDTO 重算需要的参数
	 * @param levelDetailList 等级MQ明细业务数据List
	 * 
	 */
	@SuppressWarnings("unchecked")
	public void setLevelDetailDTO(CampBaseDTO campBaseDTO, List<LevelDetailDTO> levelDetailList) {
		
		LevelDetailDTO levelDetailDTO = new LevelDetailDTO();
		// 操作类型
		String operateType = null;
		// 重算次数
		int reCalcCount = 0;
		Map<String, Object> recordKbnInfo = campBaseDTO.getRecordKbnInfo();
		if(recordKbnInfo != null) {
			Map<String, Object> map = (Map)recordKbnInfo.get(String.valueOf(DroolsConstants.RECORDKBN_0));
			if(map != null && !map.isEmpty()) {
				operateType = (String)map.get("operateType");
				reCalcCount = (Integer)map.get("reCalcCount");
			}
		}
		if(operateType == null || "".equals(operateType)) {
			return;
		}
		// 操作类型
		levelDetailDTO.setOperateType(operateType);
		if(campBaseDTO.getOldLevelId() != 0) {
			// 通过会员等级ID取得会员等级代码
			String oldLevelCode = this.getMemLevelCode(String.valueOf(campBaseDTO.getOldLevelId()));
			levelDetailDTO.setMemberlevelOld(oldLevelCode);
		}
		if(campBaseDTO.getCurLevelId() != 0) {
			// 通过会员等级ID取得会员等级代码
			String newLevelCode = this.getMemLevelCode(String.valueOf(campBaseDTO.getCurLevelId()));
			levelDetailDTO.setMemberlevelNew(newLevelCode);
		}
		if(campBaseDTO.getChangeType() != null && !"".equals(campBaseDTO.getChangeType())) {
			// 变化类型
			levelDetailDTO.setChangeType(campBaseDTO.getChangeType());
		} else {
			// 变更前会员等级级别
			int oldMemLevelGrade = 0;
			// 变更后会员等级级别
			int memLevelGrade = 0;
			if(campBaseDTO.getOldLevelId() != 0) {
				// 取得变更前会员等级级别
				oldMemLevelGrade = this.getMemLevelGrade(String.valueOf(campBaseDTO.getOldLevelId()));
			}
			if(campBaseDTO.getCurLevelId() != 0) {
				// 取得变更后会员等级级别
				memLevelGrade = this.getMemLevelGrade(String.valueOf(campBaseDTO.getCurLevelId()));
			}
			// 变更后会员等级级别比变更前等级级别大的场合，表示会员升级，小的场合表示降级
			if(memLevelGrade > oldMemLevelGrade) {
				// 变化类型
				levelDetailDTO.setChangeType("1");
			} else if(memLevelGrade < oldMemLevelGrade) {
				// 变化类型
				levelDetailDTO.setChangeType("2");
			}
		}
		// 累积金额
		levelDetailDTO.setTotalAmount(String.valueOf(campBaseDTO.getCurTotalAmount()));
		// 会员卡号
		levelDetailDTO.setMemberCode(campBaseDTO.getMemCode());
		// 柜台号
		levelDetailDTO.setCounterCode(campBaseDTO.getCounterCode());
		// 员工编号
		levelDetailDTO.setEmployeeCode(campBaseDTO.getEmployeeCode());
		// 业务类型
		levelDetailDTO.setBizType(campBaseDTO.getTradeType());
		// 关联单据时间
		levelDetailDTO.setRelevantTicketDate(campBaseDTO.getTicketDate());
		// 关联单号
		levelDetailDTO.setRelevantNo(campBaseDTO.getBillId());
		// 变动渠道
		levelDetailDTO.setChannel(campBaseDTO.getChannel());
		// 重算次数
		levelDetailDTO.setReCalcCount(String.valueOf(reCalcCount));
		// 理由
		int reason = campBaseDTO.getReason();
		// 变化原因
		String reasonS = "";
		// 理由为规则计算的场合
		if(reason == 0 || reason == 2) {
			String allRules = campBaseDTO.getAllRules(DroolsConstants.RECORDKBN_0);
			if(allRules != null && !"".equals(allRules)) {
				String[] allRuleArray = allRules.split(",");
				StringBuffer buffer = new StringBuffer();
				for(int i = 0; i < allRuleArray.length; i++) {
					if(allRulesMap.get(allRuleArray[i]) != null) {
						buffer.append(i + 1).append(DroolsConstants.MQ_REASON_COMMA).
						append(allRulesMap.get(allRuleArray[i])).
						append(DroolsConstants.LINE_SPACE);
					}
				}
				reasonS = buffer.toString();
				campBaseDTO.emptyAllRulesByKbn(DroolsConstants.RECORDKBN_0);
			}
		} else if(reason == 1) { // 理由为初始数据导入的场合
			reasonS = (String) campBaseDTO.getExtArgs().get("MS_REASON");
			if (CherryChecker.isNullOrEmpty(reasonS, true)) {
				reasonS = DroolsConstants.MQ_INITDATA_REASON;
			}
		} else if(reason == 3) { // 理由为化妆次数使用的场合
			reasonS = DroolsConstants.MQ_USEDTIMES_REASON;
		} else if(reason == 4) { // 会员等级化妆次数重算的场合
			reasonS = DroolsConstants.MQ_RECALCMEMLEVEL_REASON;
		}
		if (reasonS.length() > DroolsConstants.REASON_MAX_LENGTH) {
			reasonS = reasonS.substring(0, DroolsConstants.REASON_MAX_LENGTH);
		}
		// 变化原因
		levelDetailDTO.setReason(reasonS);
		levelDetailList.add(levelDetailDTO);
	}
	
	/**
	 * 
	 * 设置化妆次数MQ明细业务
	 * 
	 * @param campBaseDTO 重算需要的参数
	 * @param btimesDetailList 化妆次数MQ明细业务数据List
	 * 
	 */
	@SuppressWarnings("unchecked")
	public void setBTimesDetailDTO(CampBaseDTO campBaseDTO, List<BTimesDetailDTO> btimesDetailList) {
		
		BTimesDetailDTO btimesDetailDTO = new BTimesDetailDTO();
		// 操作类型
		String operateType = null;
		// 重算次数
		int reCalcCount = 0;
		Map<String, Object> recordKbnInfo = campBaseDTO.getRecordKbnInfo();
		if(recordKbnInfo != null) {
			Map<String, Object> map = (Map)recordKbnInfo.get(String.valueOf(DroolsConstants.RECORDKBN_2));
			if(map != null && !map.isEmpty()) {
				operateType = (String)map.get("operateType");
				reCalcCount = (Integer)map.get("reCalcCount");
			}
		}
		if(operateType == null || "".equals(operateType)) {
			return;
		}
		// 操作类型
		btimesDetailDTO.setOperateType(operateType);
		// 变更前化妆次数
		btimesDetailDTO.setBtimesOld(String.valueOf(campBaseDTO.getOldBtimes()));
		// 变更后化妆次数
		btimesDetailDTO.setCurBtimesNew(String.valueOf(campBaseDTO.getCurBtimes()));
		// 化妆次数差分
		btimesDetailDTO.setDiffBtimes(String.valueOf(campBaseDTO.getCurBtimes()-campBaseDTO.getOldBtimes()));
		// 会员卡号
		btimesDetailDTO.setMemberCode(campBaseDTO.getMemCode());
		// 柜台号
		btimesDetailDTO.setCounterCode(campBaseDTO.getCounterCode());
		// 员工编号
		btimesDetailDTO.setEmployeeCode(campBaseDTO.getEmployeeCode());
		// 业务类型
		btimesDetailDTO.setBizType(campBaseDTO.getTradeType());
		// 关联单据时间
		btimesDetailDTO.setRelevantTicketDate(campBaseDTO.getTicketDate());
		// 关联单号
		btimesDetailDTO.setRelevantNo(campBaseDTO.getBillId());
		// 变动渠道
		btimesDetailDTO.setChannel(campBaseDTO.getChannel());
		// 重算次数
		btimesDetailDTO.setReCalcCount(String.valueOf(reCalcCount));
		// 理由
		int reason = campBaseDTO.getReason();
		// 变化原因
		String reasonS = "";
		// 理由为规则计算的场合
		if(reason == 0 || reason == 2) {
			String allRules = campBaseDTO.getAllRules(DroolsConstants.RECORDKBN_2);
			if(allRules != null && !"".equals(allRules)) {
				String[] allRuleArray = allRules.split(",");
				StringBuffer buffer = new StringBuffer();
				for(int i = 0; i < allRuleArray.length; i++) {
					if(allRulesMap.get(allRuleArray[i]) != null) {
						buffer.append(i + 1).append(DroolsConstants.MQ_REASON_COMMA).
						append(allRulesMap.get(allRuleArray[i])).
						append(DroolsConstants.LINE_SPACE);
					}
				}
				reasonS = buffer.toString();
				campBaseDTO.emptyAllRulesByKbn(DroolsConstants.RECORDKBN_2);
			}
		} else if(reason == 1) { // 理由为初始数据导入的场合
			reasonS = DroolsConstants.MQ_INITDATA_REASON;
		} else if(reason == 3) { // 理由为化妆次数使用的场合
			reasonS = DroolsConstants.MQ_USEDTIMES_REASON;
		} else if(reason == 4) { // 会员等级化妆次数重算的场合
			reasonS = DroolsConstants.MQ_RECALCMEMLEVEL_REASON;
		}
		if (reasonS.length() > DroolsConstants.REASON_MAX_LENGTH) {
			reasonS = reasonS.substring(0, DroolsConstants.REASON_MAX_LENGTH);
		}
		// 变化原因
		btimesDetailDTO.setReason(reasonS);
		btimesDetailList.add(btimesDetailDTO);
	}
	
	/**
	 * 
	 * 设置积分MQ明细业务
	 * 
	 * @param campBaseDTO 重算需要的参数
	 * @param pointDataMap 积分MQ明细业务
	 * 
	 */
	public void setPointDetailDTO(CampBaseDTO campBaseDTO, Map<String, Object> pointDataMap) {
		// 积分信息
		PointDTO pointDTO = campBaseDTO.getPointInfo();
		if (null != pointDTO) {
			// 单据的积分情况
			PointChangeDTO pointChange = pointDTO.getPointChange();
			if (null != pointChange && null != pointChange.getChangeDetailList()) {
				// 单据主记录的明细行列表
				List<Map<String, Object>> tradeDataList = (List<Map<String, Object>>) pointDataMap.get("TradeDataList");
				// 单据明细记录的明细行列表
				List<Map<String, Object>> detailDataList = (List<Map<String, Object>>) pointDataMap.get("DetailDataList");
				// 退货关联销售单据的明细行列表
				List<Map<String, Object>> detailSRDataList = (List<Map<String, Object>>) pointDataMap.get("DetailSRDataList");
				// 单据主记录的明细行
				Map<String, Object> tradeDataMap = new HashMap<String, Object>();
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
				// 柜台号是否为空
				boolean noCounterFlag = CherryChecker.isNullOrEmpty(campBaseDTO.getCounterCode(), true);
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
					if (!noCounterFlag) {
						// Ba卡号
						detailDataMap.put("Bacode", ConvertUtil.getString(campBaseDTO.getEmployeeCode()));
					} else {
						detailDataMap.put("Bacode", "");
					}
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
					// 关联退货单号
					String billCodeSR = changeDetail.getBillCodeSR();
					if (!CherryChecker.isNullOrEmpty(billCodeSR)) {
						boolean addFlag = true;
						for (Map<String, Object> detailSRDataMap : detailSRDataList) {
							if (billCodeSR.equals(detailSRDataMap.get("SRbillid"))) {
								addFlag = false;
								break;
							}
						}
						if (addFlag) {
							Map<String, Object> detailSRDataMap = new HashMap<String, Object>();
							// 原单单号
							detailSRDataMap.put("Billid", pointChange.getTradeNoIF());
							// 退货关联销售时退货对应的产生的单据号 
							detailSRDataMap.put("SRbillid", billCodeSR);
							// 退货的日期时间
							detailSRDataMap.put("ChangeTime", changeDetail.getTicketDateSR());
							// 修改次数
							detailSRDataMap.put("ModifyCount", String.valueOf(changeDetail.getModifiedTimesSR()));
							detailSRDataList.add(detailSRDataMap);
						}
					}
				}
			}
		}
	}
	/**
	 * 
	 * list按单据时间进行排序
	 * 
	 * @param list 需要排序的list
	 * 
	 */
	public void listSort(List<Map<String, Object>> list) {
		Collections.sort(list, new Comparator<Map<String, Object>>() {
		 	public int compare(Map<String, Object> map1, Map<String, Object> map2) {
            	String ticketDate1 = (String)map1.get("ticketDate");
            	String ticketDate2 = (String)map2.get("ticketDate");
            	if(ticketDate1.compareTo(ticketDate2) > 0) {
            		return 1;
            	} else {
            		return -1;
            	}
            }
		});
	}
	
	/**
	 * 
	 * list按处理时间进行排序
	 * 
	 * @param list 需要排序的list
	 * 
	 */
	public static void dateSort(List<String> list) {
		Collections.sort(list, new Comparator<String>() {
		 	public int compare(String str1, String str2) {
            	if(CherryChecker.compareDate(str1, str2) > 0) {
            		return 1;
            	} else {
            		return -1;
            	}
            }
		});
	}
	
	/**
	 * 
	 * 把list按时间分组存放
	 * 
	 * @param list 需要转换的list
	 * @return 按时间分组后的list
	 * 
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> convertListByDate(List<Map<String, Object>> list) {
		if(list != null && !list.isEmpty()) {
			List<Map<String, Object>> newList = new ArrayList<Map<String,Object>>();
			Map<String, Object> newMap = new HashMap<String, Object>();
			for(int i = 0; i < list.size(); i++) {
				Map<String, Object> map = list.get(i);
				String ticketDate = (String)map.get("ticketDate");
				ticketDate = ticketDate.substring(0,10);
				String newTicketDate = (String)newMap.get("ticketDate");
				if(newTicketDate == null || !newTicketDate.equals(ticketDate)) {
					newMap = new HashMap<String, Object>();
					newMap.put("ticketDate", ticketDate);
					newMap.put("list", new ArrayList<Map<String,Object>>());
					((List)newMap.get("list")).add(map);
					newList.add(newMap);
				} else {
					((List)newMap.get("list")).add(map);
				}
			}
			return newList;
		}
		return null;
	}
	
	/**
	 * 
	 * 根据履历区分设定基准点的会员等级、累积金额、化妆次数、积分、可兑换金额
	 * 
	 * @param campBaseDTO 查询条件
	 * 
	 */
	public void getReferenceRecord(CampBaseDTO campBaseDTO, String moveMongoDBFlag) throws Exception {
		if (pointFlag) {
			// 需要根据初始导入积分还原
			campBaseDTO.getExtArgs().remove("POINT_RESET");
		}
		// 查询基准点的累计金额, 等级, 化妆次数
		Map<String, Object> referenceRecord = binBEDRHAN01_Service.getReferenceRecord(campBaseDTO);
		if(referenceRecord == null && "1".equals(moveMongoDBFlag)) {
			// 从规则履历历史表查询基准点的累计金额, 等级, 化妆次数
			referenceRecord = binBEDRHAN01_Service.getReferenceRecordByHistory(campBaseDTO);
//			// 从MongoDB中查询重算的基准点信息
//			referenceRecord = getReferenceRecordByMongo(campBaseDTO);
		}
		if(referenceRecord != null) {
			// 履历区分为等级的场合
			if(DroolsConstants.RECORDKBN_0 == campBaseDTO.getRecordKbn()) {
				// 升级前等级
				String oldValue = (String) referenceRecord.get("oldValue");
				// 业务类型
				String tradeType = (String) referenceRecord.get("tradeType");
				// 单据号
				String billId = (String) referenceRecord.get("billId");
				// 单据产生日期
				String ticketDate = (String) referenceRecord.get("ticketDate");
				// 变化类型
				String changeType = (String) referenceRecord.get("changeType");
				int curLevelId = Integer.parseInt(referenceRecord.get("newValue").toString());
				// 基准点的会员等级
				campBaseDTO.setCurLevelId(curLevelId);
				// 当前会员等级
				campBaseDTO.setMemberLevel(curLevelId);
				if (!CherryChecker.isNullOrEmpty(oldValue)) {
					int oldValueInt = Integer.parseInt(oldValue);
					if (0 != oldValueInt) {
						campBaseDTO.setUpgradeFromLevel(oldValueInt);
					} else {
						campBaseDTO.setUpgradeFromLevel(null);
					}
				}
				// 会员等级调整日
				campBaseDTO.setLevelAdjustDay(ticketDate);
				// 会员等级状态
				campBaseDTO.setLevelStatus(CherryConstants.LEVELSTATUS_2);
				// 等级升降级区分
				campBaseDTO.setLevelChangeType(changeType);
				// 销售
				if ("NS".equals(tradeType)) {
					// 入会或者升级首单号
					campBaseDTO.setFirstBillId(billId);
				} else {
					campBaseDTO.setFirstBillId(null);
				}
				// 取得会员等级有效期开始日和结束日
				Map<String, Object> levelDateInfo = binOLCM31_BL.getLevelDateInfo(tradeType, ticketDate, curLevelId, campBaseDTO.getTicketDate(), campBaseDTO.getMemberInfoId());
				if (null != levelDateInfo && !levelDateInfo.isEmpty()) {
					// 等级有效期开始日
					campBaseDTO.setLevelStartDate((String) levelDateInfo.get("levelStartDate"));
					// 等级有效期结束日
					campBaseDTO.setLevelEndDate((String) levelDateInfo.get("levelEndDate"));
				}
			} else if(DroolsConstants.RECORDKBN_1 == campBaseDTO.getRecordKbn()) { // 履历区分为累积金额的场合
				// 基准点的累积金额
				campBaseDTO.setCurTotalAmount(Double.parseDouble(referenceRecord.get("newValue").toString()));
			} else if(DroolsConstants.RECORDKBN_2 == campBaseDTO.getRecordKbn()) { // 履历区分为化妆次数的场合
				// 基准点的化妆次数
				campBaseDTO.setCurBtimes(Integer.parseInt(referenceRecord.get("newValue").toString()));
			} else if(DroolsConstants.RECORDKBN_3 == campBaseDTO.getRecordKbn()) { // 履历区分为积分的场合
				// 基准点的积分
				campBaseDTO.setCurPoint(Double.parseDouble(referenceRecord.get("newValue").toString()));
			} else if(DroolsConstants.RECORDKBN_4 == campBaseDTO.getRecordKbn()) { // 履历区分为可兑换金额的场合
				// 基准点的可兑换金额
				campBaseDTO.setCurBtimesAmount(Double.parseDouble(referenceRecord.get("newValue").toString()));
			} else if(DroolsConstants.RECORDKBN_5 == campBaseDTO.getRecordKbn()) { // 履历区分为累计积分的场合
				PointDTO pointInfo = campBaseDTO.getPointInfo();
				// 基准点的累计积分
				pointInfo.setCurTotalPoint(Double.parseDouble(referenceRecord.get("newValue").toString()));
			} else if(DroolsConstants.RECORDKBN_6 == campBaseDTO.getRecordKbn()) { // 履历区分为累计兑换积分的场合
				PointDTO pointInfo = campBaseDTO.getPointInfo();
				// 基准点的累计兑换积分
				pointInfo.setCurTotalChanged(Double.parseDouble(referenceRecord.get("newValue").toString()));
			} else if(DroolsConstants.RECORDKBN_7 == campBaseDTO.getRecordKbn()) { // 履历区分为可兑换积分的场合
				PointDTO pointInfo = campBaseDTO.getPointInfo();
				// 基准点的可兑换积分
				pointInfo.setCurChangablePoint(Double.parseDouble(referenceRecord.get("newValue").toString()));
			} else if(DroolsConstants.RECORDKBN_8 == campBaseDTO.getRecordKbn()) { // 履历区分为累计失效积分的场合
				PointDTO pointInfo = campBaseDTO.getPointInfo();
				// 基准点的累计失效积分
				pointInfo.setTotalDisablePoint(Double.parseDouble(referenceRecord.get("newValue").toString()));
			} else if(DroolsConstants.RECORDKBN_9 == campBaseDTO.getRecordKbn()) { // 履历区分为上次清零的最后积分变化时间的场合
				PointDTO pointInfo = campBaseDTO.getPointInfo();
				// 基准点的上次清零的最后积分变化时间
				pointInfo.setPrePCBillTime((String) referenceRecord.get("newValue"));
				// 基准点的上次积分清零日
				pointInfo.setPreDisableDate((String) referenceRecord.get("ticketDate"));
			} 
			// 履历区分为等级的场合
		} else if (DroolsConstants.RECORDKBN_0 == campBaseDTO.getRecordKbn()) {
			int curLevelId = 0;
			// 取得会员初始采集信息
			Map<String, Object> MemInitialInfo = null;
			if (isNoClub) {
				// 取得会员初始采集信息
				MemInitialInfo = binOLCM31_BL.getMemInitialInfo(campBaseDTO.getMemberInfoId());
			} else {
				MemInitialInfo = binOLCM31_BL.getClubMemInitialInfo(campBaseDTO.getMemberInfoId(), campBaseDTO.getMemberClubId());
			}
			if (null != MemInitialInfo && !MemInitialInfo.isEmpty()) {
				// 初始采集日期
				Object initialMemLevel = MemInitialInfo.get("initialMemLevel");
				if (null != initialMemLevel) {
					curLevelId = Integer.parseInt(initialMemLevel.toString());
					if (0 != curLevelId) {
						// 会员等级状态
						campBaseDTO.setLevelStatus(CherryConstants.LEVELSTATUS_2);
					}
				}
			} else {
				MemInitialInfo = new HashMap<String, Object>();
			}
			// 基准点的会员等级
			campBaseDTO.setCurLevelId(curLevelId);
			campBaseDTO.setUpgradeFromLevel(null);
			// 会员等级调整日
			campBaseDTO.setLevelAdjustDay(null);
			// 等级升降级区分
			campBaseDTO.setLevelChangeType(null);
			// 入会或者升级首单号
			campBaseDTO.setFirstBillId(null);
			if (isNoClub) {
//				if (!"1".equals(campBaseDTO.getExtArgs().get("BDKBN"))) {
					if (istj) {
						String leStartDate = (String) MemInitialInfo.get("leStartDate");
						String leEndDate = (String) MemInitialInfo.get("leEndDate");
						String initStartDate = (String) MemInitialInfo.get("initStartDate");
						String initEndDate = (String) MemInitialInfo.get("initEndDate");
						if (iszdate) {
							if (null != initStartDate && null != initEndDate) {
								String endDateTemp = DateUtil.coverTime2YMD(initEndDate, DateUtil.DATE_PATTERN);
								String recalDate = DateUtil.coverTime2YMD(campBaseDTO.getTicketDate(), DateUtil.DATE_PATTERN);
								while (DateUtil.compareDate(recalDate, endDateTemp) > 0) {
									initStartDate = DateUtil.addDateByMonth(DateUtil.DATETIME_PATTERN, initStartDate, 12);
									initEndDate = DateUtil.addDateByMonth(DateUtil.DATETIME_PATTERN, initEndDate, 12);
									endDateTemp = DateUtil.addDateByMonth(DateUtil.DATE_PATTERN, endDateTemp, 12);
								}
								// 等级有效期开始日
								campBaseDTO.setLevelStartDate(initStartDate);
								// 等级有效期结束日
								campBaseDTO.setLevelEndDate(initEndDate);
							} else {
								// 等级有效期开始日
								campBaseDTO.setLevelStartDate(null);
								// 等级有效期结束日
								campBaseDTO.setLevelEndDate(null);
							}
						} else {
							if (null != leStartDate) {
								// 等级有效期开始日
								campBaseDTO.setLevelStartDate(leStartDate);
								// 等级有效期结束日
								campBaseDTO.setLevelEndDate(leEndDate);
							} else {
								// 等级有效期开始日
								campBaseDTO.setLevelStartDate(initStartDate);
								// 等级有效期结束日
								campBaseDTO.setLevelEndDate(initEndDate);
							}
						}
					} else {
						// 等级有效期开始日
						campBaseDTO.setLevelStartDate(null);
						// 等级有效期结束日
						campBaseDTO.setLevelEndDate(null);
					}
//				} else {
//					if (0 != curLevelId) {
//						String leStartDate = (String) MemInitialInfo.get("leStartDate");
//						if (null != leStartDate) {
//							// 等级有效期开始日
//							campBaseDTO.setLevelStartDate(leStartDate);
//							// 等级有效期结束日
//							campBaseDTO.setLevelEndDate((String) MemInitialInfo.get("leEndDate"));
//						} else {
//							// 等级有效期开始日
//							campBaseDTO.setLevelStartDate((String) MemInitialInfo.get("initStartDate"));
//							// 等级有效期结束日
//							campBaseDTO.setLevelEndDate((String) MemInitialInfo.get("initEndDate"));
//						}
//					}
//				}
			} else {
				// 等级有效期开始日
				campBaseDTO.setLevelStartDate(null);
				// 等级有效期结束日
				campBaseDTO.setLevelEndDate(null);
			}
			// 积分
		} else if (DroolsConstants.RECORDKBN_5 == campBaseDTO.getRecordKbn() 
				|| DroolsConstants.RECORDKBN_6 == campBaseDTO.getRecordKbn() 
				|| DroolsConstants.RECORDKBN_7 == campBaseDTO.getRecordKbn()) {
			// 需要根据初始导入积分还原
			campBaseDTO.getExtArgs().put("POINT_RESET", "1");
		} else if(DroolsConstants.RECORDKBN_1 == campBaseDTO.getRecordKbn() && isInitAmt) { // 履历区分为累积金额的场合
			// 基准点的累积金额
			campBaseDTO.setCurTotalAmount(campBaseDTO.getInitAmount());
		}
	}
	
	/**
	 * 
	 * 把相同会员的重算日期合并成一个List，且List中第一个保存重算日期最小的对象
	 * 
	 * @param reCalcInfoList 需要合并的List
	 * @return Map map中的key代表会员ID，值为合并后的重算信息List
	 * 
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> mergerReCalcInfo(List<Map<String, Object>> reCalcInfoList) {
		
		if(reCalcInfoList != null && !reCalcInfoList.isEmpty()) {
			Map<String, Object> mergerMap = new HashMap<String, Object>();
			for(Map<String, Object> map : reCalcInfoList) {
				String memberInfoId = map.get("memberInfoId").toString() + "_" + map.get("memberClubId").toString();
				// 相同会员做合并处理，合并时把最小的重算日期放在List的第一位
				if(mergerMap.containsKey(memberInfoId)) {
					List<Map<String, Object>> list = (List)mergerMap.get(memberInfoId);
					String reCalcDate = (String)list.get(0).get("reCalcDate");
					String _reCalcDate = (String)map.get("reCalcDate");
					if(_reCalcDate.compareTo(reCalcDate) < 0) {
						list.add(0, map);
					} else {
						list.add(map);
					}
				} else {
					List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
					list.add(map);
					mergerMap.put(memberInfoId, list);
				}
			}
			return mergerMap;
		} else {
			return null;
		}
	}
	
	/**
	 * 根据会员ID取得会员等级代码
	 * 
	 * @param memberInfoId 会员ID
	 * @param allLevelMap 所有的等级信息
	 * @return 会员等级代码
	 */
	@SuppressWarnings("unchecked")
	public String getMemLevelCode(String memberInfoId) {
		
		Map<String, Object> levelMap = (Map)allMemLevelMap.get(memberInfoId);
		if(levelMap != null) {
			return (String)levelMap.get("levelCode");
		}
		return null;
	}
	
	/**
	 * 根据会员ID取得会员等级级别
	 * 
	 * @param memberInfoId 会员ID
	 * @param allLevelMap 所有的等级信息
	 * @return 会员等级级别
	 */
	@SuppressWarnings("unchecked")
	public int getMemLevelGrade(String memberInfoId) {
		
		Map<String, Object> levelMap = (Map)allMemLevelMap.get(memberInfoId);
		if(levelMap != null && levelMap.get("grade") != null) {
			return (Integer)levelMap.get("grade");
		}
		return 0;
	}
	
	/**
	 * 根据等级ID和会员实体判断该等级是否需要保级
	 * 
	 * @param levelId 等级ID
	 * @param memberCode 会员卡号
	 * @return boolean 判断结果
	 * 			true:保级  false:不保级
	 */
	private boolean isKeepLevel(String levelId, CampBaseDTO campBaseDTO) {
		Map<String, Object> levelMap = (Map)allMemLevelMap.get(levelId);
		if(levelMap != null && levelMap.get("periodValidity") != null) {
			// 等级有效期
			Map<String, Object> periodValidity = (Map<String, Object>) levelMap.get("periodValidity");
			// 是否保级区分
			String keepLevelKbn = (String) periodValidity.get("keepLevelKbn");
			// 保级(全部会员)
			if ("1".equals(keepLevelKbn)) {
				return true;
				// 保级(部分会员)
			} else if ("2".equals(keepLevelKbn)) {
				// 卡号开头字符
				String cardStart = (String) periodValidity.get("cardStart");
				if (!CherryChecker.isNullOrEmpty(cardStart)) {
					// 会员卡号
					String memCard = campBaseDTO.getMemCode();
					String[] startArr = cardStart.split(",");
					for (String startChar : startArr) {
						String stc = startChar.trim();
						if (!"".equals(stc)) {
							// 不以该字符开头
							if (stc.indexOf("!") == 0) {
								if (stc.length() > 1) {
									stc = stc.substring(1);
									if (!memCard.toUpperCase().startsWith(stc.toUpperCase())) {
										return true;
									}
								}
								// 以该字符开头
							} else if (memCard.toUpperCase().startsWith(stc.toUpperCase())) {
								return true;
							}
						}
					}
				}
			}
		}
		return false;
	}
	
	/**
	 * 根据重算时间点和迁移时间点判断是否进行重算
	 * 
	 * @param map 
	 * @return 
	 */
	public boolean isRecalculation(Map<String, Object> map) throws Exception {
		String organizationInfoID = map.get(CherryBatchConstants.ORGANIZATIONINFOID).toString();
		String brandInfoID = map.get(CherryBatchConstants.BRANDINFOID).toString();
		String months = binOLCM14_BL.getConfigValue("1053", organizationInfoID, brandInfoID);
		if(months != null && !"".equals(months)) {
			// 迁移会员规则履历到MongoDB的时间点（月数）
			int monthCount = Integer.parseInt(months);
			// 取得业务日期
			String bussinessDate = binBEDRHAN01_Service.getBussinessDate(map);
			// 迁移会员规则履历到MongoDB的时间点
			String moveDate = DateUtil.addDateByMonth("yyyy-MM-dd", bussinessDate, -monthCount);
			// 重算日期
			String reCalcDate = (String)map.get("reCalcDate");
			// 重算时间点比迁移时间点小的场合
			if(reCalcDate.substring(0,10).compareTo(moveDate) < 0) {
				String memberInfoId = map.get("memberInfoId").toString();
				// 不是重算所有会员而且对迁移时间点以前的记录也重算的场合
				if(!"-9999".equals(memberInfoId) && binOLCM14_BL.isConfigOpen("1056", organizationInfoID, brandInfoID)) {
					// 查询会员规则履历是否正在迁移到MongoDB
					int count = binBEDRHAN01_Service.getMoveMemRuleRecordCount(map);
					if(count > 0) {
						BatchExceptionDTO batchExceptionDTO = new BatchExceptionDTO();
						batchExceptionDTO.setBatchName(this.getClass());
						batchExceptionDTO.setErrorCode("EDR00045");
						batchExceptionDTO.setErrorLevel(CherryBatchConstants.LOGGER_ERROR);
						// 会员ID
						batchExceptionDTO.addErrorParam(memberInfoId);
						throw new CherryBatchException(batchExceptionDTO);
					} else {
//						// 从MongoDB把重算时间点以后的履历迁移到数据库
//						moveMongoDBToDataBase(map);
						// 从规则履历历史表中把重算时间点以后的数据迁移到规则履历表中
						binBEDRHAN01_Service.moveHistoryToCurrent(map);
						// 删除会员规则履历历史
						binBEDRHAN01_Service.delMemRuleRecordHistory(map);
						binBEDRHAN01_Service.manualCommit();
						map.put("moveMongoDBFlag", "1");
						return true;
					}
				} else {
					BatchLoggerDTO batchLoggerDTO = new BatchLoggerDTO();
					batchLoggerDTO.setCode("EDR00046");
					// 会员ID
					batchLoggerDTO.addParam(memberInfoId);
					batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_ERROR);
					CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(this.getClass());
					cherryBatchLogger.BatchLogger(batchLoggerDTO);
					flag = CherryBatchConstants.BATCH_WARNING;
					return false;
				}
			}
		}
		return true;
	}
	
	/**
	 * 从MongoDB把重算时间点以后的履历迁移到数据库
	 * 
	 * @param map 查询条件
	 * @throws Exception 
	 */
	public void moveMongoDBToDataBase(Map<String, Object> map) throws Exception {
//		DBObject dbObject = new BasicDBObject();
//		dbObject.put("orgCode", (String)map.get("orgCode"));
//		dbObject.put("brandCode", (String)map.get("brandCode"));
//		dbObject.put("memberInfoId", Integer.parseInt(map.get("memberInfoId").toString()));
//		dbObject.put("ticketDate", new BasicDBObject("$gte", (String)map.get("reCalcDate")));
//		// 从MongoDB中查询重算时间点以后的所有会员规则履历
//		List<DBObject> resultList = MongoDB.findAll("MGO_RuleExecRecord", dbObject);
//		if(resultList != null && !resultList.isEmpty()) {
//			List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
//			for(DBObject result : resultList) {
//				Map<String, Object> param = (Map)Bean2Map.toHashMap(result);
//				list.add(param);
//			}
//			if(!list.isEmpty()) {
//				// 把从MongoDB取得的履历迁移到数据库
//				binBEDRHAN01_Service.addRuleExecRecord(list);
//				binBEDRHAN01_Service.manualCommit();
//				// 把已经迁移到数据库的履历从MongoDB中删除
//				MongoDB.removeAll("MGO_RuleExecRecord", dbObject);
//			}
//		}
		
		QMemRuleExecRecord qMemRuleExecRecord = QMemRuleExecRecord.memRuleExecRecord;
		BooleanBuilder booleanBuilder = new BooleanBuilder();
		booleanBuilder.and(qMemRuleExecRecord.orgCode.eq((String)map.get("orgCode")));
		booleanBuilder.and(qMemRuleExecRecord.brandCode.eq((String)map.get("brandCode")));
		booleanBuilder.and(qMemRuleExecRecord.memberInfoId.eq(Integer.parseInt(map.get("memberInfoId").toString())));
		booleanBuilder.and(qMemRuleExecRecord.ticketDate.goe((String)map.get("reCalcDate")));
		// 从MongoDB中查询重算时间点以后的所有会员规则履历
		Iterable<MemRuleExecRecord> result = memRuleExecRecordRepository.findAll(booleanBuilder);
		if(result != null) {
			Iterator<MemRuleExecRecord> it = result.iterator();
			List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
			while(it.hasNext()) {
				MemRuleExecRecord memRuleExecRecord = it.next();
				Map<String, Object> param = (Map)Bean2Map.toHashMap(memRuleExecRecord);
				list.add(param);
			}
			if(!list.isEmpty()) {
				// 把从MongoDB取得的履历迁移到数据库
				binBEDRHAN01_Service.addRuleExecRecord(list);
				// 把已经迁移到数据库的履历从MongoDB中删除
				memRuleExecRecordRepository.delete(result);
				binBEDRHAN01_Service.manualCommit();
			}
		}
	}
	
	/**
	 * 从MongoDB中查询重算的基准点信息
	 * 
	 * @param campBaseDTO 查询条件
	 * @return 基准点信息
	 * @throws Exception 
	 */
	public Map<String, Object> getReferenceRecordByMongo(CampBaseDTO campBaseDTO) throws Exception {
		
//		DBObject dbObject = new BasicDBObject();
//		dbObject.put("orgCode", campBaseDTO.getOrgCode());
//		dbObject.put("brandCode", campBaseDTO.getBrandCode());
//		dbObject.put("memberInfoId", campBaseDTO.getMemberInfoId());
//		dbObject.put("ticketDate", new BasicDBObject("$lt", campBaseDTO.getTicketDate()));
//		dbObject.put("recordKbn", campBaseDTO.getRecordKbn());
//		dbObject.put("billType", new BasicDBObject("$ne", "MB"));
//		dbObject.put("validFlag", "1");
//		// 履历区分为等级的场合
//		if(DroolsConstants.RECORDKBN_0 == campBaseDTO.getRecordKbn()) {
//			dbObject.put("changeType", new BasicDBObject("$in", new String[]{"1", "2"}));
//		}
//		// 从MongoDB中查询重算时间点以后的所有会员规则履历
//		List<DBObject> resultList = MongoDB.find("MGO_RuleExecRecord", dbObject, null, new BasicDBObject("TicketDate",-1), 0, 1);
//		if(resultList != null && !resultList.isEmpty()) {
//			DBObject result = resultList.get(0);
//			Map<String, Object> resultMap = new HashMap<String, Object>();
//			resultMap.put("oldValue", result.get("oldValue"));
//			resultMap.put("newValue", result.get("newValue"));
//			resultMap.put("tradeType", result.get("billType"));
//			resultMap.put("billId", result.get("billCode"));
//			resultMap.put("changeType", result.get("changeType"));
//			resultMap.put("ticketDate", result.get("ticketDate"));
//			return resultMap;
//		} else {
//			return null;
//		}
		QMemRuleExecRecord qMemRuleExecRecord = QMemRuleExecRecord.memRuleExecRecord;
		BooleanBuilder booleanBuilder = new BooleanBuilder();
		booleanBuilder.and(qMemRuleExecRecord.orgCode.eq(campBaseDTO.getOrgCode()));
		booleanBuilder.and(qMemRuleExecRecord.brandCode.eq(campBaseDTO.getBrandCode()));
		booleanBuilder.and(qMemRuleExecRecord.memberInfoId.eq(campBaseDTO.getMemberInfoId()));
		booleanBuilder.and(qMemRuleExecRecord.ticketDate.lt(campBaseDTO.getTicketDate()));
		booleanBuilder.and(qMemRuleExecRecord.recordKbn.eq(campBaseDTO.getRecordKbn()));
		booleanBuilder.and(qMemRuleExecRecord.billType.ne("MB"));
		booleanBuilder.and(qMemRuleExecRecord.validFlag.eq("1"));
		// 履历区分为等级的场合
		if(DroolsConstants.RECORDKBN_0 == campBaseDTO.getRecordKbn()) {
			booleanBuilder.and(qMemRuleExecRecord.changeType.in("1","2"));
		}
		// 从MongoDB中查询重算的基准点信息
		List<MemRuleExecRecord> result = memRuleExecRecordRepository.findAll(booleanBuilder, new PageRequest(0, 1, Direction.DESC, "TicketDate")).getContent();
		if(result != null && result.size() > 0) {
			MemRuleExecRecord memRuleExecRecord = result.get(0);
			Map<String, Object> resultMap = new HashMap<String, Object>();
			resultMap.put("oldValue", memRuleExecRecord.getOldValue());
			resultMap.put("newValue", memRuleExecRecord.getNewValue());
			resultMap.put("tradeType", memRuleExecRecord.getBillType());
			resultMap.put("billId", memRuleExecRecord.getBillCode());
			resultMap.put("changeType", memRuleExecRecord.getChangeType());
			resultMap.put("ticketDate", memRuleExecRecord.getTicketDate());
			return resultMap;
		} else {
			return null;
		}
	}
	/**
	 * 
	 * 设置默认等级
	 * 
	 * @param campBaseDTO 重算需要的参数
	 * @param levelDetailList 等级MQ明细业务数据List
	 * @param bussinessDate 业务日期
	 * @throws Exception 
	 * 
	 * 
	 */
	public void deftClubLevelSetting(CampBaseDTO campBaseDTO, List<LevelDetailDTO> levelDetailList, String bussinessDate) throws Exception {
		// 需要保级处理的或者当前等级不为空的不处理
		if (1 == campBaseDTO.getMemRegFlg() || 
				0 != campBaseDTO.getCurLevelId()) {
			return;
		}
		boolean upFlag = true;
		// 当前会员卡号
		String curMemCard = campBaseDTO.getMemCode();
		// 等级
		campBaseDTO.setRecordKbn(DroolsConstants.RECORDKBN_0);
		// 查询默认等级履历
		Map<String, Object> mbLevelInfo = binBEDRHAN01_Service.getMBLevelRecord(campBaseDTO);
		if (null != mbLevelInfo && !mbLevelInfo.isEmpty()) {
			// 会员卡号
			String memCode = (String) mbLevelInfo.get("memCode");
			// 默认等级
			int curLevelId = Integer.parseInt(mbLevelInfo.get("newValue").toString());
			// 单据时间
			String ticketDate = (String) mbLevelInfo.get("ticketDate");
			// 单据号
			String billId = (String) mbLevelInfo.get("billId");
			campBaseDTO.setBillId(billId);
			campBaseDTO.setMemCode(memCode);
			// 基准点的会员等级
			campBaseDTO.setCurLevelId(curLevelId);
			campBaseDTO.setUpgradeFromLevel(null);
			// 会员等级调整日
			campBaseDTO.setLevelAdjustDay(ticketDate);
			// 单据时间
			campBaseDTO.setTicketDate(ticketDate);
			// 等级升降级区分
			campBaseDTO.setLevelChangeType(DroolsConstants.UPKBN_1);
			// 入会或者升级首单号
			campBaseDTO.setFirstBillId(null);
			// 等级有效期开始日
			campBaseDTO.setLevelStartDate(null);
			// 等级有效期结束日
			campBaseDTO.setLevelEndDate(null);
			// 会员入会等级
			campBaseDTO.setGrantMemberLevel(curLevelId);
			// 会员等级状态
			campBaseDTO.setLevelStatus(CherryConstants.LEVELSTATUS_1);
			// 等级变化
			campBaseDTO.setChangeType(DroolsConstants.UPKBN_1);
			// 有效区分
			String validFlag = (String) mbLevelInfo.get("validFlag");
			if ("0".equals(validFlag)) {
				// 恢复默认等级履历
				binBEDRHAN01_Service.updateMBLevel(campBaseDTO);
				Map<String, Object> recordKbnInfo = campBaseDTO.getRecordKbnInfo();
				if(recordKbnInfo == null) {
					recordKbnInfo = new HashMap<String, Object>();
					campBaseDTO.setRecordKbnInfo(recordKbnInfo);
				}
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("operateType", DroolsConstants.OPERATETYPE_U);
				map.put("reCalcCount", mbLevelInfo.get("reCalcCount"));
				recordKbnInfo.put(String.valueOf(DroolsConstants.RECORDKBN_0), map);
				// 查询会员最早的开卡信息
				Map<String, Object> cardGrantInfo = binBEDRHAN01_Service.getClubCardGrantInfo(campBaseDTO);
				// 理由
				campBaseDTO.setReason(DroolsConstants.REASON_0);
				if (null != cardGrantInfo && !cardGrantInfo.isEmpty()) {
					// 柜台号
					String counterCode = (String) cardGrantInfo.get("counterCode");
					if (null != counterCode) {
						// 柜台号
						campBaseDTO.setCounterCode(counterCode.trim());
					}
					// 员工编号
					String baCode = (String) cardGrantInfo.get("baCode");
					if (null != baCode) {
						campBaseDTO.setEmployeeCode(baCode.trim());
					}
				} else {
					// 柜台号
					campBaseDTO.setCounterCode(null);
					// 员工编号
					campBaseDTO.setEmployeeCode(null);
				}
				// 所有规则
				campBaseDTO.addAllRules((String) mbLevelInfo.get("ruleIds"), DroolsConstants.RECORDKBN_0);
				// 业务类型
				campBaseDTO.setTradeType("MB");
				// 设置等级MQ明细业务
				this.setLevelDetailDTO(campBaseDTO, levelDetailList);
			}
		} else {
			upFlag = false;
			Map<String, Object> map = new HashMap<String, Object>();
			// 组织ID
			map.put("organizationInfoID", campBaseDTO.getOrganizationInfoId());
			// 品牌ID
			map.put("brandInfoID", campBaseDTO.getBrandInfoId());
			map.put("mClubId", campBaseDTO.getMemberClubId());
			// 取得系统默认等级
			int	memberLevel = binOLCM31_BL.getClubDefaultLevel(map);
			if (0 != memberLevel) {
				// 查询会员最早的开卡信息
				Map<String, Object> memGrantInfo = binBEDRHAN01_Service.getClubCardGrantInfo(campBaseDTO);
				if (null != memGrantInfo && !memGrantInfo.isEmpty()) {
					upFlag = true;
					// 柜台号
					String counterCode = (String) memGrantInfo.get("counterCode");
					if (null != counterCode) {
						// 柜台号
						counterCode = counterCode.trim();
					}
					campBaseDTO.setCounterCode(counterCode);
					// 员工编号
					String baCode = (String) memGrantInfo.get("baCode");
					if (null != baCode) {
						// 员工编号
						baCode = baCode.trim();
					}
					campBaseDTO.setEmployeeCode(baCode);

					String levelAdjustDay = (String) memGrantInfo.get("joinTime");
					if (null == levelAdjustDay) {
						levelAdjustDay = bussinessDate + " 00:00:00";
					}
					Date ticketDate = DateUtil.coverString2Date(levelAdjustDay, DateUtil.DATETIME_PATTERN);
					SimpleDateFormat dateFm = new SimpleDateFormat("yyyyMMddHHmmss");
					String ticketTime = dateFm.format(ticketDate);
					String prestr = campBaseDTO.getClubIdStr() + String.valueOf(campBaseDTO.getMemberInfoId());
					int length = prestr.length();
					if (length < 10) {
						StringBuffer buffer = new StringBuffer();
						for(int i = 0; i < (10-length); i++) { 
							buffer.append("0");
						}
						buffer.append(prestr);
						prestr = buffer.toString();
					} else if (length > 10) {
						prestr = prestr.substring(0,10);
					}
					String billId = "MZ" + prestr + ticketTime;
					campBaseDTO.setBillId(billId);
					// 基准点的会员等级
					campBaseDTO.setCurLevelId(memberLevel);
					campBaseDTO.setUpgradeFromLevel(null);
					// 会员等级调整日
					campBaseDTO.setLevelAdjustDay(levelAdjustDay);
					// 单据时间
					campBaseDTO.setTicketDate(levelAdjustDay);
					// 等级升降级区分
					campBaseDTO.setLevelChangeType(DroolsConstants.UPKBN_1);
					// 入会或者升级首单号
					campBaseDTO.setFirstBillId(null);
					// 等级有效期开始日
					campBaseDTO.setLevelStartDate(null);
					// 等级有效期结束日
					campBaseDTO.setLevelEndDate(null);
					// 会员入会等级
					campBaseDTO.setGrantMemberLevel(memberLevel);
					// 会员等级状态
					campBaseDTO.setLevelStatus(CherryConstants.LEVELSTATUS_1);
					// 等级变化
					campBaseDTO.setChangeType(DroolsConstants.UPKBN_1);
					// 理由
					campBaseDTO.setReason(DroolsConstants.REASON_0);
					// 取得规则ID
					int ruleId = binOLCM31_BL.getRuleIdByCode(CherryConstants.CONTENTCODE4);
					String ruleIdStr = String.valueOf(ruleId);
					if (!allRulesMap.containsKey(ruleIdStr)) {
						allRulesMap.put(ruleIdStr, CherryConstants.CONTENT4);
					}
					campBaseDTO.addRuleId(ruleIdStr);
					campBaseDTO.addAllRules(ruleIdStr, DroolsConstants.RECORDKBN_0);
					// 业务类型
					campBaseDTO.setTradeType("MB");
					Map<String, Object> recordKbnInfo = campBaseDTO.getRecordKbnInfo();
					if(recordKbnInfo == null) {
						recordKbnInfo = new HashMap<String, Object>();
						campBaseDTO.setRecordKbnInfo(recordKbnInfo);
					}
					// 增加等级变化履历
					binbedrcom01BL.addRuleExecRecord(campBaseDTO, DroolsConstants.RECORDKBN_0, true);
					// 设置等级MQ明细业务
					this.setLevelDetailDTO(campBaseDTO, levelDetailList);
				}
			}
		}
		if (upFlag) {
			// 更新会员默认等级
			binBEDRHAN01_Service.updateMemClubDeftLevel(campBaseDTO);
		}
		campBaseDTO.setMemCode(curMemCard);
	}
	
	/**
	 * 
	 * 设置默认等级
	 * 
	 * @param campBaseDTO 重算需要的参数
	 * @param levelDetailList 等级MQ明细业务数据List
	 * @param bussinessDate 业务日期
	 * @throws Exception 
	 * 
	 * 
	 */
	public void deftLevelSetting(CampBaseDTO campBaseDTO, List<LevelDetailDTO> levelDetailList, String bussinessDate) throws Exception {
		// 需要保级处理的或者当前等级不为空的不处理
		if (1 == campBaseDTO.getMemRegFlg() || 
				null != campBaseDTO.getExtArgs().get(DroolsConstants.KEEP_LEVELID) || 
				0 != campBaseDTO.getCurLevelId()) {
			return;
		}
		//组织代号
		String orgCode = campBaseDTO.getOrgCode();
		// 品牌代码，即品牌简称
		String brandCode = campBaseDTO.getBrandCode();
		// 是否设置默认等级
		boolean defLevelFlag = true;
		// 建档处理
		CampRuleExec_IF campRuleExec08 = binBEMQMES98_BL.getRuleExec(orgCode, brandCode, CherryConstants.HANDLERTYPE_RE08);
		if (null != campRuleExec08) {
			// 会员卡号
			String memCode = campBaseDTO.getMemCode();
			if (null != memCode && !memCode.startsWith("8") && !memCode.startsWith("9")) {
				defLevelFlag = false;
			}
		}
		if (defLevelFlag) {
			boolean upFlag = true;
			// 当前会员卡号
			String curMemCard = campBaseDTO.getMemCode();
			// 等级
			campBaseDTO.setRecordKbn(DroolsConstants.RECORDKBN_0);
			// 查询默认等级履历
			Map<String, Object> mbLevelInfo = binBEDRHAN01_Service.getMBLevelRecord(campBaseDTO);
			if (null != mbLevelInfo && !mbLevelInfo.isEmpty()) {
				// 会员卡号
				String memCode = (String) mbLevelInfo.get("memCode");
				// 默认等级
				int curLevelId = Integer.parseInt(mbLevelInfo.get("newValue").toString());
				// 单据时间
				String ticketDate = (String) mbLevelInfo.get("ticketDate");
				// 单据号
				String billId = (String) mbLevelInfo.get("billId");
				campBaseDTO.setBillId(billId);
				campBaseDTO.setMemCode(memCode);
				// 基准点的会员等级
				campBaseDTO.setCurLevelId(curLevelId);
				campBaseDTO.setUpgradeFromLevel(null);
				// 会员等级调整日
				campBaseDTO.setLevelAdjustDay(ticketDate);
				// 单据时间
				campBaseDTO.setTicketDate(ticketDate);
				// 等级升降级区分
				campBaseDTO.setLevelChangeType(DroolsConstants.UPKBN_1);
				// 入会或者升级首单号
				campBaseDTO.setFirstBillId(null);
				// 等级有效期开始日
				campBaseDTO.setLevelStartDate(null);
				// 等级有效期结束日
				campBaseDTO.setLevelEndDate(null);
				// 会员入会等级
				campBaseDTO.setGrantMemberLevel(curLevelId);
				// 会员等级状态
				campBaseDTO.setLevelStatus(CherryConstants.LEVELSTATUS_1);
				// 等级变化
				campBaseDTO.setChangeType(DroolsConstants.UPKBN_1);
				// 有效区分
				String validFlag = (String) mbLevelInfo.get("validFlag");
				if ("0".equals(validFlag)) {
					// 恢复默认等级履历
					binBEDRHAN01_Service.updateMBLevel(campBaseDTO);
					Map<String, Object> recordKbnInfo = campBaseDTO.getRecordKbnInfo();
					if(recordKbnInfo == null) {
						recordKbnInfo = new HashMap<String, Object>();
						campBaseDTO.setRecordKbnInfo(recordKbnInfo);
					}
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("operateType", DroolsConstants.OPERATETYPE_U);
					map.put("reCalcCount", mbLevelInfo.get("reCalcCount"));
					recordKbnInfo.put(String.valueOf(DroolsConstants.RECORDKBN_0), map);
					// 查询会员最早的开卡信息
					Map<String, Object> cardGrantInfo = binBEDRHAN01_Service.getCardGrantInfo(campBaseDTO);
					// 理由
					campBaseDTO.setReason(DroolsConstants.REASON_0);
					if (null != cardGrantInfo && !cardGrantInfo.isEmpty()) {
						// 柜台号
						String counterCode = (String) cardGrantInfo.get("counterCode");
						if (null != counterCode) {
							// 柜台号
							campBaseDTO.setCounterCode(counterCode.trim());
						}
						// 员工编号
						String baCode = (String) cardGrantInfo.get("baCode");
						if (null != baCode) {
							campBaseDTO.setEmployeeCode(baCode.trim());
						}
					} else {
						// 柜台号
						campBaseDTO.setCounterCode(null);
						// 员工编号
						campBaseDTO.setEmployeeCode(null);
					}
					// 所有规则
					campBaseDTO.addAllRules((String) mbLevelInfo.get("ruleIds"), DroolsConstants.RECORDKBN_0);
					// 业务类型
					campBaseDTO.setTradeType("MB");
					// 设置等级MQ明细业务
					this.setLevelDetailDTO(campBaseDTO, levelDetailList);
				}
			} else {
				upFlag = false;
				Map<String, Object> map = new HashMap<String, Object>();
				// 组织ID
				map.put("organizationInfoID", campBaseDTO.getOrganizationInfoId());
				// 品牌ID
				map.put("brandInfoID", campBaseDTO.getBrandInfoId());
				// 入会途径
				map.put("sourse", campBaseDTO.getChannelCode());
				// 取得等级(根据入会途径)
				int memberLevel = binOLCM31_BL.getLevelByChannel(map);
				if (0 == memberLevel) {
					// 取得系统默认等级
					memberLevel = binOLCM31_BL.getDefaultLevel(map);
				}
				if (0 != memberLevel) {
					// 查询会员最早的开卡信息
					Map<String, Object> memGrantInfo = binBEDRHAN01_Service.getMemGrantInfo(campBaseDTO);
					if (null != memGrantInfo && !memGrantInfo.isEmpty()) {
						upFlag = true;
						// 会员卡号
						String memCode = (String) memGrantInfo.get("memCode");
						campBaseDTO.setMemCode(memCode);
						// 柜台号
						String counterCode = (String) memGrantInfo.get("counterCode");
						if (null != counterCode) {
							// 柜台号
							counterCode = counterCode.trim();
						}
						campBaseDTO.setCounterCode(counterCode);
						// 员工编号
						String baCode = (String) memGrantInfo.get("baCode");
						if (null != baCode) {
							// 员工编号
							baCode = baCode.trim();
						}
						campBaseDTO.setEmployeeCode(baCode);
						// 开卡日期
						String grantDate = (String) memGrantInfo.get("grantDate");
						// 开卡时间
						String grantTime = (String) memGrantInfo.get("grantTime");
						String levelAdjustDay = null;
						if (grantDate != null && !"".equals(grantDate)) {
							if (grantDate.length() == 8 && CherryChecker.checkDate(grantDate)) {
								levelAdjustDay = grantDate.substring(0, 4) + "-" + grantDate.substring(4, 6) + "-" + grantDate.substring(6, 8);
							}
						}
						if (null == levelAdjustDay) {
							levelAdjustDay = bussinessDate;
						}
						boolean timeFlag = true;
						if (null != grantTime) {
							grantTime = grantTime.trim();
							if (!"".equals(grantTime)) {
								levelAdjustDay += " " + grantTime;
								timeFlag = false;
							}
						}
						if (timeFlag) {
							levelAdjustDay += " 00:00:00";
						}
						Date ticketDate = DateUtil.coverString2Date(levelAdjustDay, DateUtil.DATETIME_PATTERN);
						SimpleDateFormat dateFm = new SimpleDateFormat("yyyyMMddHHmmss");
						String ticketTime = dateFm.format(ticketDate);
						String prestr = String.valueOf(campBaseDTO.getMemberInfoId());
						int length = prestr.length();
						if (length < 10) {
							StringBuffer buffer = new StringBuffer();
							for(int i = 0; i < (10-length); i++) { 
								buffer.append("0");
							}
							buffer.append(prestr);
							prestr = buffer.toString();
						} else if (length > 10) {
							prestr = prestr.substring(0,10);
						}
						String billId = "MB" + prestr + ticketTime;
						campBaseDTO.setBillId(billId);
						// 基准点的会员等级
						campBaseDTO.setCurLevelId(memberLevel);
						campBaseDTO.setUpgradeFromLevel(null);
						// 会员等级调整日
						campBaseDTO.setLevelAdjustDay(levelAdjustDay);
						// 单据时间
						campBaseDTO.setTicketDate(levelAdjustDay);
						// 等级升降级区分
						campBaseDTO.setLevelChangeType(DroolsConstants.UPKBN_1);
						// 入会或者升级首单号
						campBaseDTO.setFirstBillId(null);
						// 等级有效期开始日
						campBaseDTO.setLevelStartDate(null);
						// 等级有效期结束日
						campBaseDTO.setLevelEndDate(null);
						// 会员入会等级
						campBaseDTO.setGrantMemberLevel(memberLevel);
						// 会员等级状态
						campBaseDTO.setLevelStatus(CherryConstants.LEVELSTATUS_1);
						// 等级变化
						campBaseDTO.setChangeType(DroolsConstants.UPKBN_1);
						// 理由
						campBaseDTO.setReason(DroolsConstants.REASON_0);
						// 取得规则ID
						int ruleId = binOLCM31_BL.getRuleIdByCode(CherryConstants.CONTENTCODE4);
						String ruleIdStr = String.valueOf(ruleId);
						if (!allRulesMap.containsKey(ruleIdStr)) {
							allRulesMap.put(ruleIdStr, CherryConstants.CONTENT4);
						}
						campBaseDTO.addRuleId(ruleIdStr);
						campBaseDTO.addAllRules(ruleIdStr, DroolsConstants.RECORDKBN_0);
						// 业务类型
						campBaseDTO.setTradeType("MB");
						Map<String, Object> recordKbnInfo = campBaseDTO.getRecordKbnInfo();
						if(recordKbnInfo == null) {
							recordKbnInfo = new HashMap<String, Object>();
							campBaseDTO.setRecordKbnInfo(recordKbnInfo);
						}
						// 增加等级变化履历
						binbedrcom01BL.addRuleExecRecord(campBaseDTO, DroolsConstants.RECORDKBN_0, true);
						// 设置等级MQ明细业务
						this.setLevelDetailDTO(campBaseDTO, levelDetailList);
					}
				}
			}
			if (upFlag) {
				// 更新会员默认等级
				binBEDRHAN01_Service.updateMemDeftLevel(campBaseDTO);
			}
			campBaseDTO.setMemCode(curMemCard);
		}
	}
	
	/**
	 * 比较第一个参数时间是否早于第二个参数时间
	 * 
	 * @param value1
	 *            时间1
	 * @param value2
	 *            时间2
	 * @return true : value1 早于 value2  false : value1 等于或大于value2
	 */
	private boolean isBefore(String value1, String value2) {
		Calendar cal1 = Calendar.getInstance();
		Calendar cal2 = Calendar.getInstance();
		cal1.setTime(DateUtil.coverString2Date(value1, DateUtil.DATETIME_PATTERN));
		cal2.setTime(DateUtil.coverString2Date(value2, DateUtil.DATETIME_PATTERN));
		return cal1.before(cal2);
	}
	
	/**
	 * 比较第一个参数时间和第二个参数时间的大小
	 * 
	 * @param value1
	 *            时间1
	 * @param value2
	 *            时间2
	 * @return int 小于0 : value1 小于 value2  
	 * 				0 : value1 等于value2
	 * 				大于0 : value1 大于value2
	 */
	private int compTime(String value1, String value2) {
		Calendar cal1 = Calendar.getInstance();
		Calendar cal2 = Calendar.getInstance();
		cal1.setTime(DateUtil.coverString2Date(value1, DateUtil.DATETIME_PATTERN));
		cal2.setTime(DateUtil.coverString2Date(value2, DateUtil.DATETIME_PATTERN));
		return cal1.compareTo(cal2);
	}
	
	private void callbackTmall(String mixMobile, Long recordId, String errCode, String brandCode) throws Exception {
		TmallMeiCrmCallbackPointChangeRequest req =
				new TmallMeiCrmCallbackPointChangeRequest();
		req.setMixMobile(mixMobile);
		req.setRecordId(recordId);
		if (CherryChecker.isNullOrEmpty(errCode)) {
			req.setResult(0L);
		} else {
			req.setResult(1L);
			req.setErrorCode(errCode);
		}
		TmallKeyDTO tmallKey = TmallKeys.getTmallKeyBybrandCode(brandCode);
		if (null == tmallKey) {
			throw new Exception("can not get brand keys!");
		}
		String appKey = tmallKey.getAppKey();
		String appSecret = tmallKey.getAppSecret();
		String sessionKey = tmallKey.getSessionKey();
		for (int i = 0; i < 4; i++) {
			try {
				TmallMeiCrmCallbackPointChangeResponse response = SignTool.pointChangeResponse(req, appKey, appSecret, sessionKey);
				if (response.isSuccess()) {
					break;
				}
				String errMsg = "Tmall response error: " + response.getSubCode() + " message: " + response.getSubMsg() + " 回调次数：" + (i + 1);
				if (i < 3) {
					logger.error(errMsg);
				} else {
					throw new Exception(errMsg);
				}
			} catch (Exception e) {
				String errMsg = "异常信息：" + e.getMessage() + " 回调次数：" + (i + 1);
				logger.error(errMsg,e);
				if (i == 3) {
					throw new Exception(e.getMessage());
				}
			}
		}
	}
}
