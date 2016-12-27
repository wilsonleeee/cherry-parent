/*	
 * @(#)BINBEMBTIF02_BL.java     1.0 2015/06/24
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
package com.cherry.mb.tif.bl;

import com.cherry.cm.cmbussiness.interfaces.BINOLCM31_IF;
import com.cherry.cm.core.*;
import com.cherry.cm.util.CherryBatchUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.cm.util.DateUtil;
import com.cherry.dr.cmbussiness.interfaces.BINBEDRCOM01_IF;
import com.cherry.mb.tif.service.BINBEMBTIF01_Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 线上会员转正式会员处理BL
 * 
 * @author fxb
 * @version 1.0 2016/12/14
 */
public class BINBEMBTIF10_BL {
	
	private static Logger logger = LoggerFactory.getLogger(BINBEMBTIF10_BL.class.getName());
	
	/** BATCH处理标志 */
	private int flag = CherryBatchConstants.BATCH_SUCCESS;
	
	@Resource
	private BINBEMBTIF01_Service binBEMBTIF01_Service;

	@Resource
	private BINBEDRCOM01_IF binbedrcom01BL;

	/** 规则处理共通接口 **/
	@Resource
	private BINOLCM31_IF binOLCM31_BL;
	
	/** 处理总条数 */
	private int totalCount = 0;

	/** 失败条数 */
	private int failCount = 0;
	
	/**
	 * 线上会员转正式会员处理
	 * 
	 * @param map 参数集合
	 * @return BATCH处理标志
	 */
	public int tran_regMemToFormalMem(Map<String, Object> map) throws Exception {
		
		logger.info("入参：" + map);
		totalCount = 0;
		failCount = 0;
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
			// 取得需要转成正式会员的线上会员信息List
			List<Map<String, Object>> memList = binBEMBTIF01_Service.getRegMemList(map);
			// 会员信息List不为空
			if (!CherryBatchUtil.isBlankList(memList)) {
				try {
					executeMembers(memList, map);
				} catch (Exception e) {
					logger.error("regMemToFormalMem exception：" + e,e);
				}
				// 会员信息少于一次抽取的数量，即为最后一页，跳出循环
				if(memList.size() < dataSize) {
					break;
				}
			} else {
				break;
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
		batchLoggerDTO2.addParam(String.valueOf(totalCount - failCount));
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
		return flag;
	}
	
	/**
	 * 线上会员转正式会员处理
	 * 
	 * @param
	 *
	 * @throws Exception 
	 * 
	 */
	public void executeMembers(List<Map<String, Object>> memList, Map<String, Object> map) throws Exception {
		BatchLoggerDTO batchLoggerDTO = new BatchLoggerDTO();
		totalCount += memList.size();

		for (int i = 0; i < memList.size(); i++) {
			Map<String, Object> memberInfo = memList.get(i);
			commParamsForUp(memberInfo);
			memberInfo.putAll(map);
			int memberInfoId = Integer.parseInt(String.valueOf(memberInfo.get("memberInfoId")));
			try {
				//以会员的明文手机号为卡号查询会员持卡表
				Map<String,Object> memCardInfo = binBEMBTIF01_Service.getMemCardInfo(memberInfo);

				if (null != memCardInfo && !memCardInfo.isEmpty()){
					//如果存在相同的卡号，则添加一条记录到会员信息合并记录表
					Map<String,Object> mergeMap = new HashMap<String, Object>();
					mergeMap.putAll(memberInfo);
					mergeMap.putAll(memCardInfo);
					//判断是否已存在该会员的合并记录
					Map<String,Object> mergeInfo = binBEMBTIF01_Service.getMemMergeInfoByIdAndMemCode(mergeMap);
					if (mergeInfo == null){
						binBEMBTIF01_Service.addMemberMergeInfo(mergeMap);
					}
				}else{
					//更新会员信息
					String joinDate = updateMemberInfo(memberInfo);
					memberInfo.put("joinDate",DateUtil.getSpecificDate(joinDate,"yyyyMMdd"));
					//更新线上会员持卡表的卡号改为明文手机号
					binBEMBTIF01_Service.updateMemCardInfo(memberInfo);
					//更新会员对应电商订单主表的MemberCode
					memberInfo.put("newMemInfoId",memberInfo.get("memberInfoId"));
					memberInfo.put("newMemCode",memberInfo.get("mobilePhone"));
					binBEMBTIF01_Service.updateESOrderMain(memberInfo);
					//更新会员对应销售主表的MemberCode
					binBEMBTIF01_Service.updateSaleMaster(memberInfo);
					//更新会员积分对应表BIN_MemUsedDetail的MemCode
					binBEMBTIF01_Service.updateMemUsedDetail(memberInfo);

					//发重算积分MQ
					memberInfo.put("memberCode",memberInfo.get("mobilePhone"));
					sendReCalcMsg(memberInfo);
					//发新增会员的MQ到老后台
					sendMemberMQ(memberInfo);

				}

			}catch (Exception e) {
				// 失败件数加一
				failCount++;
				try {
					// 事务回滚
					binBEMBTIF01_Service.manualRollback();
				} catch (Exception ex) {
					
				}
				logger.error(e.toString(),e);
				batchLoggerDTO.clear();
				batchLoggerDTO.setCode("EMI00002");
				// 会员ID
				batchLoggerDTO.addParam(String.valueOf(memberInfoId));
				batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_ERROR);
				CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(
						this.getClass());
				cherryBatchLogger.BatchLogger(batchLoggerDTO, e);
				flag = CherryBatchConstants.BATCH_WARNING;
			}
		}
	}

	private String updateMemberInfo(Map<String, Object> memberInfo) {
		//更新会员信息表的MemInfoRegFlg改成0
		memberInfo.put("memInfoRegFlg","0");
		//接受消息
		memberInfo.put("isReceiveMsg","1");
		Map<String, Object> extMap = ConvertUtil.json2Map(TmallKeys.getExtJson((String)memberInfo.get("brandCode")));
		memberInfo.put("baCodeBelong",extMap.get("baCodeBelong"));
		memberInfo.put("counterCodeBelong",extMap.get("counterCodeBelong"));
		memberInfo.put("memName",memberInfo.get("nickname") == null ? null : CherryBatchUtil.mixStrsub(ConvertUtil.getString(memberInfo.get("nickname")),39));
		String joinDate = memberInfo.get("tmallBindTime") == null ? binBEMBTIF01_Service.getSYSDate().substring(0,10)
				: ConvertUtil.getString(memberInfo.get("tmallBindTime")).substring(0,10);
		memberInfo.put("joinDate",joinDate);
		Map<String, Object> organizationMap = binBEMBTIF01_Service.getOrganizationId(memberInfo);
		Map<String, Object> empMap = binBEMBTIF01_Service.getEmpId(memberInfo);
		memberInfo.put("organizationId",organizationMap.get("organizationId"));
		memberInfo.put("employeeId",empMap.get("employeeId"));
		binBEMBTIF01_Service.updateMemberInfo(memberInfo);
		return joinDate;
	}

	private void sendReCalcMsg(Map<String, Object> map) {
		Map<String,Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		// 业务日期
		String businessTime = getBusinessTime(paramMap,map);
		try {
			// 单据日期
			paramMap.put("reCalcTime", businessTime);
			paramMap.put("organizationInfoID",map.get("organizationInfoId"));
			paramMap.put("brandInfoID",map.get("brandInfoId"));
			// 保存并发送重算MQ
			binbedrcom01BL.saveAndSendReCalcMsg(paramMap);
		} catch (Exception e) {
			logger.error("重算积分失败:" + e,e);
		}
	}

	private String getBusinessTime(Map<String, Object> paramMap, Map<String, Object> map) {
		String businessTime;
		//取得最早的销售时间
		Map<String,Object> earliestSaleTimeMap = binBEMBTIF01_Service.getEarliestSaleTime(paramMap);
		if ((earliestSaleTimeMap == null || earliestSaleTimeMap.get("saleTime") == null)&& map.get("tmallBindTime") != null){
			businessTime = ConvertUtil.getString(map.get("tmallBindTime"));
		}else if ((earliestSaleTimeMap == null || earliestSaleTimeMap.get("saleTime") == null) && map.get("tmallBindTime") == null){
			return binBEMBTIF01_Service.getSYSDateTime();
		}else if (earliestSaleTimeMap != null && map.get("tmallBindTime") == null){
			businessTime =  ConvertUtil.getString(earliestSaleTimeMap.get("saleTime"));
		}else{
			if (DateUtil.compareDate(ConvertUtil.getString(earliestSaleTimeMap.get("saleTime")),ConvertUtil.getString(map.get("tmallBindTime"))) > 0){
				businessTime =  ConvertUtil.getString(map.get("tmallBindTime"));
			}else{
				businessTime =  ConvertUtil.getString(earliestSaleTimeMap.get("saleTime"));
			}
		}
		return businessTime;
	}

	/**
	 * 发送会员资料MQ消息
	 *
	 * @param map
	 * @throws Exception
	 */
	public void sendMemberMQ(Map<String, Object> map) throws Exception {

		Map<String, Object> memMqMap = new HashMap<String, Object>();
		Map<String, Object> extMap = ConvertUtil.json2Map(TmallKeys.getExtJson((String)map.get("brandCode")));
		// 品牌代码
		memMqMap.put("brandCode", map.get("brandCode"));
		// 组织代码
		memMqMap.put("orgCode", map.get("orgCode"));
		// 组织ID
		memMqMap.put("organizationInfoId", map.get("organizationInfoId"));
		// 品牌ID
		memMqMap.put("brandInfoId", map.get("brandInfoId"));
		// 子类型
		memMqMap.put("subType", "0");
		// 会员卡号
		memMqMap.put("memCode", map.get("memberCode"));
		// 手机
		memMqMap.put("mobilePhone", map.get("mobilePhone"));
		// 姓名
		memMqMap.put("memName", map.get("nickname") == null ? null : CherryBatchUtil.mixStrsub(ConvertUtil.getString(map.get("nickname")),39));
		// 开卡时间
		memMqMap.put("joinDate",map.get("joinDate"));
		// 发卡BA
		memMqMap.put("baCodeBelong", extMap.get("baCodeBelong"));
		//发卡柜台
		memMqMap.put("counterCodeBelong", extMap.get("counterCodeBelong"));
		// 版本号
		memMqMap.put("version", "1");
		// 会员信息登记区分
		memMqMap.put("memInfoRegFlg", "0");
		// 发送会员资料MQ消息
		binOLCM31_BL.sendMEMQMsg(memMqMap);

	}
	/**
	 * 共通的参数设置(更新或者新增)
	 * 
	 * @param map 
	 * 			参数集合
	 * 
	 */
	private void commParamsForUp(Map<String, Object> map){
		// 作成者
		map.put(CherryBatchConstants.CREATEDBY, "BINBEMBTIF10");
		// 作成程序名
		map.put(CherryBatchConstants.CREATEPGM, "BINBEMBTIF10");
		// 更新者
		map.put(CherryBatchConstants.UPDATEDBY, "BINBEMBTIF10");
		// 更新程序名
		map.put(CherryBatchConstants.UPDATEPGM, "BINBEMBTIF10");
	}
}
