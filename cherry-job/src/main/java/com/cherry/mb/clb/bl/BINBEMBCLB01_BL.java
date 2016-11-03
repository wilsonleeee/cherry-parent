/*
 * @(#)BINBEMBCLB01_BL.java     1.0 2014/11/06
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
package com.cherry.mb.clb.bl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.cm.cmbussiness.service.BINOLCM09_Service;
import com.cherry.cm.core.BatchLoggerDTO;
import com.cherry.cm.core.CherryBatchConstants;
import com.cherry.cm.core.CherryBatchLogger;
import com.cherry.mb.clb.service.BINBEMBCLB01_Service;
import com.googlecode.jsonplugin.JSONUtil;

/**
 * 会员俱乐部下发(实时)处理BL
 * 
 * @author HUB
 * @version 1.0 2014/11/06
 */
public class BINBEMBCLB01_BL{
	
	/** 会员俱乐部下发(实时)处理Service */
	@Resource
	private BINBEMBCLB01_Service binBEMBCLB01_Service;
	
	@Resource(name = "binOLCM09_Service")
	private BINOLCM09_Service ser;
	
	/** BATCH处理标志 */
	private int flag = CherryBatchConstants.BATCH_SUCCESS;
	
	/** 会员积分失败条数 */
	private int failCount = 0;
	
	/** 共通Batch Log处理*/
	private CherryBatchLogger cherryBatchLogger;
	
	/** 共通BatchLogger*/
	private BatchLoggerDTO batchLoggerDTO;
	
	private static Logger logger = LoggerFactory.getLogger(BINBEMBCLB01_BL.class.getName());
	
	/**
	 * 会员俱乐部下发(实时)处理
	 * 
	 * @param map 参数集合
	 * @return BATCH处理标志
	 */
	public int tran_batchClub(Map<String, Object> map) throws Exception{
		// 共通Batch Log处理
		cherryBatchLogger = new CherryBatchLogger(this.getClass());
		// 共通BatchLogger
		batchLoggerDTO = new BatchLoggerDTO();
		try {
			// 更新会员俱乐部BATCH执行标识
			binBEMBCLB01_Service.updateClubBatchFlag(map);
			// 提交事务
			binBEMBCLB01_Service.manualCommit();
		} catch (Exception e) {
			try {
				// 事务回滚
				binBEMBCLB01_Service.manualRollback();
			} catch (Exception ex) {	
				
			}
			logger.error(e.getMessage(),e);
		}
		// 取得需要下发的会员俱乐部列表
		List<Map<String, Object>> clubList = binBEMBCLB01_Service.getClubList(map);
		if (null == clubList || clubList.isEmpty()) {
			// 会员俱乐部没有发生变更，不执行下发
			batchLoggerDTO.setCode("IMB00021");
			batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_INFO);
			cherryBatchLogger.BatchLogger(batchLoggerDTO);
		} else {
			int size = clubList.size();
			for (Map<String, Object> clubMap : clubList) {
				clubMap.put("brandCode", map.get("brandCode"));
				commParams(clubMap);
				try {
					// 有效区分
					String validFlag = (String) clubMap.get("validFlag");
					// 有效
					if ("1".equals(validFlag)) {
						// 下发区分
						String sendFlag = (String) clubMap.get("sendFlag");
						// 取得活动柜台
						List<Map<String, Object>> cntList = getCntList(clubMap, map);
						if (null != cntList && !cntList.isEmpty()) {
							// 未下发
							if ("0".equals(sendFlag)) {
								// 新增下发
								addNewCounterClub(cntList);
							} else {
								// 取得已下发的会员俱乐部柜台关系列表
								List<Map<String, Object>> clubCounterList = binBEMBCLB01_Service.getClubCounterList(clubMap);
								if (null == clubCounterList || clubCounterList.isEmpty()) {
									// 新增下发
									addNewCounterClub(cntList);
								} else {
									List<Map<String, Object>> newcntList = new ArrayList<Map<String, Object>>();
									List<Map<String, Object>> upcntList = new ArrayList<Map<String, Object>>();
									for (Map<String, Object> newcnt : cntList) {
										// 俱乐部代号
										String clubCodeNew = (String) newcnt.get("clubCode");
										// 柜台号
										String counterIDNew = (String) newcnt.get("counterID");
										boolean isEqual = false;
										for (int i = 0; i < clubCounterList.size(); i++) {
											Map<String, Object> clubCounter = clubCounterList.get(i);
											// 俱乐部代号
											String clubCode = (String) clubCounter.get("clubCode");
											// 柜台号
											String counterCode = (String) clubCounter.get("counterID");
											if (clubCodeNew.equals(clubCode) && counterIDNew.equals(counterCode)) {
												// 已停用
												if ("0".equals(clubCounter.get("validFlag"))) {
													Map<String, Object> clubCounterMap = new HashMap<String, Object>();
													clubCounterMap.putAll(clubCounter);
													clubCounterMap.put("validFlag", "1");
													upcntList.add(clubCounterMap);
												}
												isEqual = true;
												clubCounterList.remove(i);
												break;
											}
										}
										if (!isEqual) {
											newcntList.add(newcnt);
										}
									}
									// ***********************新后台表更新*************************
									if (!clubCounterList.isEmpty()) {
										// 停用会员俱乐部与柜台对应关系
										binBEMBCLB01_Service.delCounterClub(clubCounterList);
									}
									if (!upcntList.isEmpty()) {
										// 更新会员俱乐部与柜台对应关系
										binBEMBCLB01_Service.updateCounterClub(upcntList);
									}
									if (!newcntList.isEmpty()) { 
										// 插入会员俱乐部与柜台对应关系表(品牌数据库) 
										binBEMBCLB01_Service.addCounterClub(newcntList);
									}
									// ***********************品牌数据库表更新*************************
									if (!clubCounterList.isEmpty()) {
										// 停用会员俱乐部与柜台对应关系 (品牌数据库)
										binBEMBCLB01_Service.invaildWitmemclub(clubCounterList);
									}
									if (!upcntList.isEmpty()) {
										// 启用会员俱乐部与柜台对应关系(品牌数据库)
										binBEMBCLB01_Service.updateWitClubCounterValid(upcntList);
//										cntList.addAll(upcntList);
									}
									if (!newcntList.isEmpty()) { 
										// 插入会员俱乐部与柜台对应关系表(品牌数据库) 
										binBEMBCLB01_Service.addWitCounterClub(newcntList);
									}
								}
							}
						}
						// 无效
					} else {
						// 取得已下发的会员俱乐部柜台关系列表
						clubMap.put("vdFlag", "1");
						List<Map<String, Object>> clubCounterList = binBEMBCLB01_Service.getClubCounterList(clubMap);
						if (null != clubCounterList && !clubCounterList.isEmpty()) {
							// 停用会员俱乐部与柜台对应关系
							binBEMBCLB01_Service.delCounterClub(clubCounterList);
							// 停用会员俱乐部与柜台对应关系 (品牌数据库)
							binBEMBCLB01_Service.invaildWitmemclub(clubCounterList);
						}
					}
					// 更新会员俱乐部下发标识
					binBEMBCLB01_Service.updateClubSendFlag(clubMap);
					// 更新俱乐部信息 (品牌数据库)
					int result = binBEMBCLB01_Service.updateWitClubInfo(clubMap);
					if (0 == result) {
						// 插入俱乐部信息 (品牌数据库)
						binBEMBCLB01_Service.insertWitClubInfo(clubMap);
					}
					binBEMBCLB01_Service.manualCommit();
					binBEMBCLB01_Service.witManualCommit();
				} catch (Exception e) {
					try {
						binBEMBCLB01_Service.manualRollback();
						binBEMBCLB01_Service.witManualRollback();
					} catch (Exception ex) {	
						
					}
					// 标记下发失败记录
					batchLoggerDTO.clear();
					batchLoggerDTO.setCode("EMB00056");
					batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_ERROR);
					batchLoggerDTO.addParam((String) clubMap.get("clubCode"));
					cherryBatchLogger.BatchLogger(batchLoggerDTO, e);
					flag = CherryBatchConstants.BATCH_WARNING;
					failCount++;
				}
			}
			// 全部更新完成
			batchLoggerDTO.clear();
			batchLoggerDTO.setCode("IMB00022");
			batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_INFO);
			batchLoggerDTO.addParam(String.valueOf(size));
			batchLoggerDTO.addParam(String.valueOf(failCount));
			cherryBatchLogger.BatchLogger(batchLoggerDTO);
			//******************停用已失效的对应关系*****************
			// 取得已失效的会员俱乐部柜台关系列表 
			List<Map<String, Object>> invaildList = binBEMBCLB01_Service.getInvaildClubCounterList(map);
			if (null != invaildList && !invaildList.isEmpty()) {
				try {
					// 停用会员俱乐部与柜台对应关系
					binBEMBCLB01_Service.delCounterClub(invaildList);
					// 停用会员俱乐部与柜台对应关系 (品牌数据库)
					binBEMBCLB01_Service.invaildWitmemclub(invaildList);
					binBEMBCLB01_Service.manualCommit();
					binBEMBCLB01_Service.witManualCommit();
				} catch (Exception e) {
					try {
						binBEMBCLB01_Service.manualRollback();
						binBEMBCLB01_Service.witManualRollback();
					} catch (Exception ex) {	
						
					}
					// 标记下发失败记录
					batchLoggerDTO.clear();
					batchLoggerDTO.setCode("EMB00058");
					batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_ERROR);
					cherryBatchLogger.BatchLogger(batchLoggerDTO, e);
					flag = CherryBatchConstants.BATCH_WARNING;
				}
			}
		}
		try {
			// 去除会员BATCH执行状态
			binBEMBCLB01_Service.clearClubBatchFlag(map);
			// 提交事务
			binBEMBCLB01_Service.manualCommit();
		} catch (Exception e) {
			try {
				// 事务回滚
				binBEMBCLB01_Service.manualRollback();
			} catch (Exception ex) {	
				
			}
			logger.error(e.getMessage(),e);
		}
		return flag;
	}
	
	/**
	 * 新增下发
	 * 
	 * @param map
	 * @return
	 * @throwsException 
	 */
	private void addNewCounterClub(List<Map<String, Object>> cntList) {
		// 插入会员俱乐部与柜台对应关系表
		binBEMBCLB01_Service.addCounterClub(cntList);
		// 插入会员俱乐部与柜台对应关系表(品牌数据库) 
		binBEMBCLB01_Service.addWitCounterClub(cntList);
	}
	
	/**
	 * 取得活动柜台
	 * 
	 * @param map
	 * @return
	 * @throwsException 
	 */
	private List<Map<String, Object>> getCntList(Map<String, Object> clubInfo, Map<String, Object> map) throws Exception {
		// 地点类型
		String locationType = (String) clubInfo.get("locationType");
		List<Map<String, Object>> cntList = new ArrayList<Map<String, Object>>();
		// 全部柜台
		if ("0".equals(locationType)) {
			Map<String, Object> resultMap = new HashMap<String, Object>();
			// 柜台号
			resultMap.put("counterID", "ALL");
			// 品牌代号
			resultMap.put("brandCode", map.get("brandCode"));
			// 俱乐部代号
			resultMap.put("clubCode", clubInfo.get("clubCode"));
			// 有效区分
			resultMap.put("validFlag", "1");
			resultMap.put("brandInfoId", map.get("brandInfoId"));
			resultMap.put("organizationInfoId", map.get("organizationInfoId"));
			commParams(resultMap);
			cntList.add(resultMap);
		} else {
			// 保存的地点
			String saveJson = (String) clubInfo.get("saveJson");
			// 品牌代号
			String brandCode = (String) map.get("brandCode");
			// 俱乐部代号
			String clubCode = (String) clubInfo.get("clubCode");
			List<Object> placeList = (List<Object>) JSONUtil.deserialize(saveJson);
			if (null != placeList) {
				for (int i = 0; i < placeList.size(); i++) {
					String place = String.valueOf(placeList.get(i));
					// 指定柜台
					if ("2".equals(locationType) || "4".equals(locationType) || "5".equals(locationType)) {
						Map<String, Object> resultMap = new HashMap<String, Object>();
						// 柜台号
						resultMap.put("counterID", place);
						// 品牌代号
						resultMap.put("brandCode", brandCode);
						// 俱乐部代号
						resultMap.put("clubCode", clubCode);
						// 有效区分
						resultMap.put("validFlag", "1");
						resultMap.put("brandInfoId", map.get("brandInfoId"));
						resultMap.put("organizationInfoId", map.get("organizationInfoId"));
						commParams(resultMap);
						cntList.add(resultMap);
						
					} else {
						List<Map<String, Object>> tempList = null;
						// 区域
						if ("1".equals(locationType)) {
							map.put("cityID", place);
							map.put("userID", clubInfo.get("userID"));
							// 根据区域市查询柜台
							tempList = ser.getCounterByIdCity(map);
							// 渠道
						} else {
							// 根据渠道ID查询柜台
							map.put("channelID", place);
							map.put("userID", clubInfo.get("userID"));
							tempList = ser.getCounterByIdChannel(map);
						}
						if (null != tempList && !tempList.isEmpty()) {
							for (Map<String, Object> temp : tempList) {
								// 品牌代号
								temp.put("brandCode", brandCode);
								// 俱乐部代号
								temp.put("clubCode", clubCode);
								// 有效区分
								temp.put("validFlag", "1");
								temp.put("brandInfoId", map.get("brandInfoId"));
								temp.put("organizationInfoId", map.get("organizationInfoId"));
								commParams(temp);
							}
							cntList.addAll(tempList);
						}
					}
				}
			}
		}
		return cntList;
	}
	
	/**
	 * 共通的参数设置(更新或者新增)
	 * 
	 * @param map 
	 * 			参数集合
	 * 
	 */
	private void commParams(Map<String, Object> map){
		// 作成者
		map.put(CherryBatchConstants.CREATEDBY, "BINBEMBARC02");
		// 作成程序名
		map.put(CherryBatchConstants.CREATEPGM, "BINBEMBARC02");
		// 更新者
		map.put(CherryBatchConstants.UPDATEDBY, "BINBEMBARC02");
		// 更新程序名
		map.put(CherryBatchConstants.UPDATEPGM, "BINBEMBARC02");
	}

}
