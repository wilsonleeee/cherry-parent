/*	
 * @(#)BINBEMBARC04_BL.java     1.0 2013/11/07		
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

import com.cherry.cm.core.BatchLoggerDTO;
import com.cherry.cm.core.CherryBatchConstants;
import com.cherry.cm.core.CherryBatchLogger;
import com.cherry.mb.arc.service.BINBEMBARC04_Service;
import com.cherry.mq.mes.common.MessageConstants;
import com.mqhelper.interfaces.MQHelper_IF;

/**
 * 老后台会员资料导入新后台处理BL
 * 
 * @author WangCT
 * @version 1.0 2013/11/07
 */
public class BINBEMBARC04_BL {
	
	/** 老后台会员资料导入新后台处理Service */
	@Resource
	private BINBEMBARC04_Service binBEMBARC04_Service;
	
	/**MQHelper模块接口*/
	@Resource
	private MQHelper_IF mqHelperImpl;
	
	/**
	 * 老后台会员资料导入新后台处理
	 * 
	 * @param map 传入参数包括组织ID、品牌ID等
	 * @return BATCH处理标志
	 */
	public int memberInfoHandle(Map<String, Object> map) throws Exception {
		
		// BATCH处理标志
		int flag = CherryBatchConstants.BATCH_SUCCESS;
		// 总条数
		int totalCount = 0;
		// 成功条数
		int successCount = 0;
		// 失败条数
		int failCount = 0;
		CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(this.getClass());
		
		// 数据查询长度
		int dataSize = CherryBatchConstants.DATE_SIZE;
		
		try {
			// 把会员接口表中状态为处理中的数据全部更新成未处理状态
			binBEMBARC04_Service.updateGetStatusNull(map);
		} catch (Exception e) {
			BatchLoggerDTO batchLoggerDTO1 = new BatchLoggerDTO();
			batchLoggerDTO1.setCode("EMB00005");
			batchLoggerDTO1.setLevel(CherryBatchConstants.LOGGER_ERROR);
			cherryBatchLogger.BatchLogger(batchLoggerDTO1, e);
			return CherryBatchConstants.BATCH_WARNING;
		}
		
		// 查询需要处理数据的日期List
		List<Map<String, Object>> insertdateList = binBEMBARC04_Service.getInsertdateList(map);
		if(insertdateList != null && !insertdateList.isEmpty()) {
			for(int x = 0; x < insertdateList.size(); x++) {
				Map<String, Object> insertdateMap = insertdateList.get(x);
				try {
					// 按日期把会员接口表中未处理的数据全部更新成处理中状态
					binBEMBARC04_Service.updateGetStatus(insertdateMap);
				} catch (Exception e) {
					BatchLoggerDTO batchLoggerDTO1 = new BatchLoggerDTO();
					batchLoggerDTO1.setCode("EMB00004");
					batchLoggerDTO1.setLevel(CherryBatchConstants.LOGGER_ERROR);
					cherryBatchLogger.BatchLogger(batchLoggerDTO1, e);
					return CherryBatchConstants.BATCH_WARNING;
				}
				
				while (true) {
					// 查询件数设置
					map.put("COUNT", dataSize);
					// 查询未换过卡的会员数据List
					List<Map<String, Object>> memberInfoList = binBEMBARC04_Service.getMemberInfoList(map);
					// 会员数据不为空
					if (memberInfoList != null && !memberInfoList.isEmpty()) {
						// 统计总条数
						totalCount += memberInfoList.size();
						for(int i = 0; i < memberInfoList.size(); i++) {
							Map<String, Object> memberInfoMap = memberInfoList.get(i);
							memberInfoMap.putAll(map);
							try {
								// 把会员资料转换成会员单消息发送
								this.sendMemInfoMes(memberInfoMap);
								memberInfoMap.put("getStatus", "2");
								successCount++;
							} catch (Exception e) {
								memberInfoMap.put("getStatus", "3");
								failCount++;
								flag = CherryBatchConstants.BATCH_WARNING;
								BatchLoggerDTO batchLoggerDTO1 = new BatchLoggerDTO();
								batchLoggerDTO1.setCode("EMB00001");
								batchLoggerDTO1.setLevel(CherryBatchConstants.LOGGER_ERROR);
								cherryBatchLogger.BatchLogger(batchLoggerDTO1, e);
							}
						}
						try {
							// 批量更新处理过的会员数据的状态
							binBEMBARC04_Service.updateGetStatusEnd(memberInfoList);
						} catch (Exception e) {
							BatchLoggerDTO batchLoggerDTO1 = new BatchLoggerDTO();
							batchLoggerDTO1.setCode("EMB00002");
							batchLoggerDTO1.setLevel(CherryBatchConstants.LOGGER_ERROR);
							cherryBatchLogger.BatchLogger(batchLoggerDTO1, e);
							return CherryBatchConstants.BATCH_WARNING;
						}
						// 会员数据少于一次抽取的数量，跳出循环
						if(memberInfoList.size() < dataSize) {
							break;
						}
					} else {
						break;
					}
				}
				
				// 查询所有换卡信息
				List<Map<String, Object>> allMemberInfoList = binBEMBARC04_Service.getMemberChangeInfoList(map);
				if(allMemberInfoList != null && !allMemberInfoList.isEmpty()) {
					// 统计总条数
					totalCount += allMemberInfoList.size();
				}
				while(true) {
					if(allMemberInfoList != null && !allMemberInfoList.isEmpty()) {
						List<Map<String, Object>> _allMemberInfoList = new ArrayList<Map<String, Object>>();
						_allMemberInfoList.addAll(allMemberInfoList);
						List<Map<String, Object>> memberInfoList = new ArrayList<Map<String, Object>>();
						// 找出换卡顺序在最前面的会员List
						for(int i = 0; i < allMemberInfoList.size(); i++) {
							Map<String, Object> memberInfoMap = allMemberInfoList.get(i);
							String oldMemCode = (String)memberInfoMap.get("oldMemCode");
							boolean isOldMemCode = false;
							for(int j = 0; j < _allMemberInfoList.size(); j++) {
								String memcode = (String)_allMemberInfoList.get(j).get("memcode");
								if(memcode.equals(oldMemCode.trim())) {
									isOldMemCode = true;
									break;
								}
							}
							if(!isOldMemCode) {
								memberInfoList.add(memberInfoMap);
								allMemberInfoList.remove(i);
								i--;
							}
						}
						// 发送换卡顺序在最前面的会员List
						if(memberInfoList != null && !memberInfoList.isEmpty()) {
							for(int i = 0; i < memberInfoList.size(); i++) {
								Map<String, Object> memberInfoMap = memberInfoList.get(i);
								memberInfoMap.putAll(map);
								try {
									// 把会员资料转换成会员单消息发送
									this.sendMemInfoMes(memberInfoMap);
									memberInfoMap.put("getStatus", "2");
									successCount++;
								} catch (Exception e) {
									memberInfoMap.put("getStatus", "3");
									failCount++;
									flag = CherryBatchConstants.BATCH_WARNING;
									BatchLoggerDTO batchLoggerDTO1 = new BatchLoggerDTO();
									batchLoggerDTO1.setCode("EMB00001");
									batchLoggerDTO1.setLevel(CherryBatchConstants.LOGGER_ERROR);
									cherryBatchLogger.BatchLogger(batchLoggerDTO1, e);
								}
							}
							try {
								// 批量更新处理过的会员数据的状态
								binBEMBARC04_Service.updateGetStatusEnd(memberInfoList);
							} catch (Exception e) {
								BatchLoggerDTO batchLoggerDTO1 = new BatchLoggerDTO();
								batchLoggerDTO1.setCode("EMB00007");
								batchLoggerDTO1.setLevel(CherryBatchConstants.LOGGER_ERROR);
								cherryBatchLogger.BatchLogger(batchLoggerDTO1, e);
								return CherryBatchConstants.BATCH_WARNING;
							}
						} else {
							failCount += allMemberInfoList.size();
							flag = CherryBatchConstants.BATCH_WARNING;
							BatchLoggerDTO batchLoggerDTO1 = new BatchLoggerDTO();
							batchLoggerDTO1.setCode("EMB00046");
							batchLoggerDTO1.setLevel(CherryBatchConstants.LOGGER_ERROR);
							cherryBatchLogger.BatchLogger(batchLoggerDTO1);
							try {
								for(int i = 0; i < allMemberInfoList.size(); i++) {
									Map<String, Object> memberInfoMap = allMemberInfoList.get(i);
									memberInfoMap.put("getStatus", "3");
								}
								// 批量更新处理过的会员数据的状态
								binBEMBARC04_Service.updateGetStatusEnd(allMemberInfoList);
							} catch (Exception e) {
								BatchLoggerDTO batchLoggerDTO2 = new BatchLoggerDTO();
								batchLoggerDTO2.setCode("EMB00007");
								batchLoggerDTO2.setLevel(CherryBatchConstants.LOGGER_ERROR);
								cherryBatchLogger.BatchLogger(batchLoggerDTO2, e);
								return CherryBatchConstants.BATCH_WARNING;
							}
							break;
						}
					} else {
						break;
					}
				}
				
//				while (true) {
//					// 查询件数设置
//					map.put("COUNT", dataSize);
//					// 查询换过卡的会员数据List
//					List<Map<String, Object>> memberInfoList = binBEMBARC04_Service.getMemberChangeInfoList(map);
//					// 会员数据不为空
//					if (memberInfoList != null && !memberInfoList.isEmpty()) {
//						// 统计总条数
//						totalCount += memberInfoList.size();
//						for(int i = 0; i < memberInfoList.size(); i++) {
//							Map<String, Object> memberInfoMap = memberInfoList.get(i);
//							memberInfoMap.putAll(map);
//							try {
//								// 把会员资料转换成会员单消息发送
//								this.sendMemInfoMes(memberInfoMap);
//								memberInfoMap.put("getStatus", "2");
//								successCount++;
//							} catch (Exception e) {
//								memberInfoMap.put("getStatus", "3");
//								failCount++;
//								flag = CherryBatchConstants.BATCH_WARNING;
//								BatchLoggerDTO batchLoggerDTO1 = new BatchLoggerDTO();
//								batchLoggerDTO1.setCode("EMB00001");
//								batchLoggerDTO1.setLevel(CherryBatchConstants.LOGGER_ERROR);
//								cherryBatchLogger.BatchLogger(batchLoggerDTO1, e);
//							}
//						}
//						try {
//							// 批量更新处理过的会员数据的状态
//							binBEMBARC04_Service.updateGetStatusEnd(memberInfoList);
//						} catch (Exception e) {
//							BatchLoggerDTO batchLoggerDTO1 = new BatchLoggerDTO();
//							batchLoggerDTO1.setCode("EMB00007");
//							batchLoggerDTO1.setLevel(CherryBatchConstants.LOGGER_ERROR);
//							cherryBatchLogger.BatchLogger(batchLoggerDTO1, e);
//							return CherryBatchConstants.BATCH_WARNING;
//						}
//						// 会员数据少于一次抽取的数量，跳出循环
//						if(memberInfoList.size() < dataSize) {
//							break;
//						}
//					} else {
//						break;
//					}
//				}
			}
		}
		
		// 总件数
		BatchLoggerDTO batchLoggerDTO1 = new BatchLoggerDTO();
		batchLoggerDTO1.setCode("IIF00001");
		batchLoggerDTO1.setLevel(CherryBatchConstants.LOGGER_INFO);
		batchLoggerDTO1.addParam(String.valueOf(totalCount));
		// 成功总件数
		BatchLoggerDTO batchLoggerDTO2 = new BatchLoggerDTO();
		batchLoggerDTO2.setCode("IIF00002");
		batchLoggerDTO2.setLevel(CherryBatchConstants.LOGGER_INFO);
		batchLoggerDTO2.addParam(String.valueOf(successCount));
		// 失败件数
		BatchLoggerDTO batchLoggerDTO5 = new BatchLoggerDTO();
		batchLoggerDTO5.setCode("IIF00005");
		batchLoggerDTO5.setLevel(CherryBatchConstants.LOGGER_INFO);
		batchLoggerDTO5.addParam(String.valueOf(failCount));
		
		// 处理总件数
		cherryBatchLogger.BatchLogger(batchLoggerDTO1);
		// 成功总件数
		cherryBatchLogger.BatchLogger(batchLoggerDTO2);
		// 失败件数
		cherryBatchLogger.BatchLogger(batchLoggerDTO5);
		
		return flag;
		
	}
	
	
	/**
	 * 把会员资料转换成会员单消息发送
	 * 
	 * @param memberInfoMap 会员信息
	 */
	public void sendMemInfoMes(Map<String, Object> memberInfoMap) throws Exception {
		
		Map<String,Object> map = new HashMap<String, Object>();
		// MQ版本号
		map.put("Version", MessageConstants.MESSAGE_VERSION);
		// MQ消息体数据类型
		map.put("Type", MessageConstants.MESSAGE_TYPE_MEMBER_BA);
		// 消息主体数据key
		String[] mainLineKey = new String[]{"BrandCode","TradeType","SubType","Sourse","MachineCode","TradeNoIF","TradeCounterCode","TradeBAcode"};
		map.put("MainLineKey", mainLineKey);
		// 消息主体数据
		String[] mainData = new String[mainLineKey.length];
		// 品牌代码
		mainData[0] = (String)memberInfoMap.get("brandCode");
		// 业务类型
		mainData[1] = MessageConstants.MSG_MEMBER;
		// 老会员卡号
		String oldMemCode = convertDBCToSBC((String)memberInfoMap.get("oldMemCode"));
		if(oldMemCode != null && !"".equals(oldMemCode)) {
			oldMemCode = oldMemCode.trim();
		}
		// 会员卡号
		String memcode = convertDBCToSBC((String)memberInfoMap.get("memcode"));
		// 会员资料修改时间
		String modified = binBEMBARC04_Service.getForwardSYSDate();
		modified = modified.replaceAll("-", "").replaceAll(":", "").replaceAll(" ", "");
		// 卡号有效区分
		int status = (Integer)memberInfoMap.get("status");
		// 换卡和无效卡同时存在标志
		boolean multiFlag = false;
		// 换卡的场合
		if(oldMemCode != null && !"".equals(oldMemCode)) {
			if(status == -1) {
				multiFlag = true;
			}
			// 子类型
			mainData[2] = "2";
		} else {
			if(status == -1) {
				// 子类型
				mainData[2] = "3";
			} else {
				// 子类型
				mainData[2] = "1";
			}
		}
		// 数据来源
		mainData[3] = "WEB";
		// 机器号
		mainData[4] = "";
		// 单据号
		mainData[5] = "IF" + memcode + modified + "001";
		// 该笔业务发生的柜台
		mainData[6] = "";
		// 操作该笔业务的BA
		mainData[7] = "";
		map.put("MainDataLine", mainData);
		// 明细数据Key
		String[] detailLineKey = new String[]{"MemberCode","MemName","MemPhone","MemMobile","MemSex","MemProvince"
				,"Memcity","MemAddress","MemPostcode","MemBirthday","MemMail","MemGranddate","BAcode"
				,"Countercode","NewMemcode","MemChangeTime","MemLevel","ModifyBirthdayFlag","JoinTime"
				,"Referrer","MemAgeGetMethod","Version","Memo1","Active","ActiveDate","MemLevelExt","LevelStartDateExt","LevelEndDateExt","MemberPassword","Memo2"};
		map.put("DetailLineKey", detailLineKey);
		// 明细数据
		List<String[]> detailDatas = new ArrayList<String[]>();
		String[] detailData = new String[detailLineKey.length];
		
		// 换卡的场合
		if(oldMemCode != null && !"".equals(oldMemCode)) {
			// 会员号
			detailData[0] = oldMemCode;
			// 新卡号
			detailData[14] = memcode;
			// 会员换卡时间
			detailData[15] = (String)memberInfoMap.get("changCardTime");
		} else {
			// 会员号
			detailData[0] = memcode;
			// 新卡号
			detailData[14] = "";
			// 会员换卡时间
			detailData[15] = "";
		}
		// 会员名字
		detailData[1] = convertDBCToSBC((String)memberInfoMap.get("name"));
		// 会员电话
		detailData[2] = (String)memberInfoMap.get("telephone");
		// 会员手机号
		detailData[3] = (String)memberInfoMap.get("mobilephone");
		// 会员性别
		Object sex = memberInfoMap.get("sex");
		if(sex != null) {
			detailData[4] = String.valueOf(memberInfoMap.get("sex"));
		} else {
			detailData[4] = "";
		}
		// 会员省份
		detailData[5] = convertDBCToSBC((String)memberInfoMap.get("province"));
		// 会员城市
		detailData[6] = convertDBCToSBC((String)memberInfoMap.get("city"));
		// 会员地址
		detailData[7] = convertDBCToSBC((String)memberInfoMap.get("address"));
		// 会员邮编
		detailData[8] = (String)memberInfoMap.get("zip");
		// 会员生日
		detailData[9] = (String)memberInfoMap.get("birthday");
		// 会员邮箱
		detailData[10] = convertDBCToSBC((String)memberInfoMap.get("mail"));
		// 会员开卡时间
		detailData[11] = (String)memberInfoMap.get("granttime");
		// Ba卡号
		detailData[12] = (String)memberInfoMap.get("bacode");
		// 柜台号
		detailData[13] = (String)memberInfoMap.get("countercode");
		// 会员等级
		detailData[16] = "";
		// 是否更改生日的标志
		detailData[17] = "";
		// 入会时间
		detailData[18] = "";
		// 推荐会员
		detailData[19] = "";
		// 会员年龄获取方式
		detailData[20] = "";
		// 版本号
		detailData[21] = "";
		// 会员备注信息1
		detailData[22] = (String)memberInfoMap.get("memo1");
		// 是否激活
		detailData[23] = (String)memberInfoMap.get("active");
		// 激活时间
		detailData[24] = (String)memberInfoMap.get("activeDate");
		// 会员等级
		detailData[25] = (String)memberInfoMap.get("memberLevel");
		// 等级有效开始日期
		detailData[26] = (String)memberInfoMap.get("levelStartdate");
		// 等级有效结束日期
		detailData[27] = (String)memberInfoMap.get("levelEnddate");
		// 会员密码
		detailData[28] = (String)memberInfoMap.get("memberPassword");
		// 会员备注信息2
		detailData[29] = (String)memberInfoMap.get("memo2");
		
		detailDatas.add(detailData);
		map.put("DetailDataLine", detailDatas);
		Map<String,Object> MqLogMap = new HashMap<String, Object>();
		MqLogMap.put("BillType", mainData[1]);
		MqLogMap.put("BillCode", mainData[5]);
		MqLogMap.put("CounterCode", detailData[13]);
		MqLogMap.put("Txddate", modified.substring(2,8));
		MqLogMap.put("Txdtime", modified.substring(8));
		MqLogMap.put("Source", mainData[3]);
		MqLogMap.put("SendOrRece", "S");
		MqLogMap.put("ModifyCounts", "0");
		map.put("Mq_Log", MqLogMap);
		mqHelperImpl.sendData(map, "posToCherryMsgQueue", detailData[13]);
		// 换卡和无效卡同时存在的场合，发送完换卡消息后还需要发送无效卡消息
		if(multiFlag) {
			// 子类型
			mainData[2] = "3";
			// 单据号
			mainData[5] = "IF" + memcode + modified + "002";
			// 会员号
			detailData[0] = memcode;
			// 新卡号
			detailData[14] = "";
			// 会员换卡时间
			detailData[15] = "";
			MqLogMap.put("BillCode", mainData[5]);
			mqHelperImpl.sendData(map, "posToCherryMsgQueue", detailData[13]);
		}
	}
	
	/**
	 * 把字符串中的半角逗号全部转换成全角逗号
	 * 
	 * @param str 待转换的字符串
	 * @return 转换好的字符串
	 */
	public String convertDBCToSBC(String str) {
		if(str != null && !"".equals(str)) {
			return str.replaceAll(",", "，");
		}
		return str;
	}

}
