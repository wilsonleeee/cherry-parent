package com.cherry.mb.tif.bl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.cm.core.BatchLoggerDTO;
import com.cherry.cm.core.CherryBatchConstants;
import com.cherry.cm.core.CherryBatchLogger;
import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.TmallKeys;
import com.cherry.cm.util.CherryBatchUtil;
import com.cherry.mb.tif.service.BINBEMBTIF01_Service;

public class BINBEMBTIF06_BL {
	
	private static Logger logger = LoggerFactory.getLogger(BINBEMBTIF06_BL.class.getName());
	
	/** BATCH处理标志 */
	private int flag = CherryBatchConstants.BATCH_SUCCESS;
	
	@Resource
	private BINBEMBTIF01_Service binBEMBTIF01_Service;
	
	/** 处理总条数 */
	private int totalCount = 0;

	/** 失败条数 */
	private int failCount = 0;
	
	/**
	 * 天猫历史注册会员转假登录会员处理
	 * 
	 * @param map 参数集合
	 * @return BATCH处理标志
	 */
	public int tran_HisRegister(Map<String, Object> map) throws Exception {
		String tmallCounters = TmallKeys.getTmallCounters((String) map.get("brandCode"));
		if (!CherryChecker.isNullOrEmpty(tmallCounters)) {
			String[] arr = tmallCounters.split(",");
			map.put("mCounterCodeBelong", arr[0]);
		}
		totalCount = 0;
		failCount = 0;
		try {
			// 去除会员BATCH执行状态
			binBEMBTIF01_Service.updateClearRegisterExec(map);
			// 更新会员BATCH执行状态(历史注册会员)
			binBEMBTIF01_Service.updateHisRegBatchExec(map);
			// 提交事务
			binBEMBTIF01_Service.manualCommit();
		} catch (Exception e) {
			try {
				// 事务回滚
				binBEMBTIF01_Service.manualRollback();
			} catch (Exception ex) {	
				
			}
			logger.error(e.getMessage(),e);
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
		map.put(CherryBatchConstants.SORT_ID, "memRegisterId");
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
			// 取得历史注册的新会员信息List
			List<Map<String, Object>> memList = binBEMBTIF01_Service.getHisMemRegisterList(map);
			// 会员信息List不为空
			if (!CherryBatchUtil.isBlankList(memList)) {
				try {
					// 执行合并处理
					executeMembers(memList, map);
				} catch (Exception e) {
					logger.error("Member History Register exception：" + e.getMessage(),e);
				}
				// 会员信息少于一次抽取的数量，即为最后一页，跳出循环
				if(memList.size() < dataSize) {
					break;
				}
			} else {
				break;
			}
		}
		try {
			// 去除会员BATCH执行状态
			binBEMBTIF01_Service.updateClearRegisterExec(map);
			// 提交事务
			binBEMBTIF01_Service.manualCommit();
		} catch (Exception e) {
			try {
				// 事务回滚
				binBEMBTIF01_Service.manualRollback();
			} catch (Exception ex) {	
				
			}
			logger.error(e.getMessage(),e);
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
	 * 执行合并处理
	 * 
	 * @param memSyncList 
	 * 			会员信息List
	 * @throws Exception 
	 * 
	 */
	public void executeMembers(List<Map<String, Object>> memList, Map<String, Object> map) throws Exception {
		BatchLoggerDTO batchLoggerDTO = new BatchLoggerDTO();
		totalCount += memList.size();
		for (int i = 0; i < memList.size(); i++) {
			Map<String, Object> memberInfo = memList.get(i);
			int registerId = Integer.parseInt(String.valueOf(memberInfo.get("memRegisterId")));
			try {
				int count = binBEMBTIF01_Service.getMixMemCount(memberInfo);
				if (count > 0) {
					logger.error("会员表中存在相同加密手机号的会员,记录ID：" + registerId);
				}
				commParamsForUp(memberInfo);
				memberInfo.put("organizationInfoId", map.get("organizationInfoId"));
				memberInfo.put("brandInfoId", map.get("brandInfoId"));
				memberInfo.put("memInfoRegFlg", "1");
				memberInfo.put("memberCode", "TM" + String.format("%010d", registerId));
				memberInfo.put("counterCodeBelong", map.get("mCounterCodeBelong"));
				if ("1".equals(memberInfo.get("vaFlag"))) {
					memberInfo.put("tmBindTime", memberInfo.get("regBindTime"));
				} else {
					memberInfo.put("tmBindTime", null);
				}
				int memberId = binBEMBTIF01_Service.addMemberInfo(memberInfo);
				memberInfo.put("memberInfoId", memberId);
				binBEMBTIF01_Service.addMemCardInfo(memberInfo);
				binBEMBTIF01_Service.updateTempMemRegInfo(memberInfo);
				binBEMBTIF01_Service.updateTmallPointMemId(memberInfo);
				binBEMBTIF01_Service.manualCommit();
			}catch (Exception e) {
				// 失败件数加一
				failCount++;
				try {
					// 事务回滚
					binBEMBTIF01_Service.manualRollback();
				} catch (Exception ex) {	
					
				}
				logger.error(e.getMessage(),e);
				batchLoggerDTO.clear();
				batchLoggerDTO.setCode("EMI00009");
				// 会员ID
				batchLoggerDTO.addParam(String.valueOf(registerId));
				batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_ERROR);
				CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(
						this.getClass());
				cherryBatchLogger.BatchLogger(batchLoggerDTO, e);
				flag = CherryBatchConstants.BATCH_WARNING;
			}
		}
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
		map.put(CherryBatchConstants.CREATEDBY, "BINBEMBTIF06");
		// 作成程序名
		map.put(CherryBatchConstants.CREATEPGM, "BINBEMBTIF06");
		// 更新者
		map.put(CherryBatchConstants.UPDATEDBY, "BINBEMBTIF06");
		// 更新程序名
		map.put(CherryBatchConstants.UPDATEPGM, "BINBEMBTIF06");
	}
}
