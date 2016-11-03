package com.cherry.mb.tif.bl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.cm.core.BatchLoggerDTO;
import com.cherry.cm.core.CherryBatchConstants;
import com.cherry.cm.core.CherryBatchLogger;
import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherrySecret;
import com.cherry.cm.core.TmallKeyDTO;
import com.cherry.cm.core.TmallKeys;
import com.cherry.cm.util.CherryBatchUtil;
import com.cherry.dr.cmbussiness.interfaces.BINBEDRCOM01_IF;
import com.cherry.dr.cmbussiness.util.DoubleUtil;
import com.cherry.dr.cmbussiness.util.DroolsConstants;
import com.cherry.mb.tif.service.BINBEMBTIF01_Service;

public class BINBEMBTIF05_BL {
	
	private static Logger logger = LoggerFactory.getLogger(BINBEMBTIF05_BL.class.getName());
	
	/** BATCH处理标志 */
	private int flag = CherryBatchConstants.BATCH_SUCCESS;
	
	@Resource
	private BINBEMBTIF01_Service binBEMBTIF01_Service;
	
	@Resource
	private BINBEDRCOM01_IF binbedrcom01BL;
	
	/** 处理总条数 */
	private int totalCount = 0;

	/** 失败条数 */
	private int failCount = 0;
	
	/**
	 * 天猫会员合并处理
	 * 
	 * @param map 参数集合
	 * @return BATCH处理标志
	 */
	public int tran_MemMerge(Map<String, Object> map) throws Exception {
		TmallKeyDTO tmallKey = TmallKeys.getTmallKeyBybrandCode((String) map.get("brandCode"));
		if (null == tmallKey) {
			throw new Exception("can not get brand keys!");
		}
		map.put("mTmallKey", tmallKey);
		totalCount = 0;
		failCount = 0;
		try {
			// 去除会员BATCH执行状态(天猫会员合并表)
			binBEMBTIF01_Service.updateClearMergeExec(map);
			// 更新会员BATCH执行状态(天猫会员合并)
			binBEMBTIF01_Service.updateMergeBatchExec(map);
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
		map.put(CherryBatchConstants.SORT_ID, "tmallMergeId");
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
			// 取得需要合并的会员信息List
			List<Map<String, Object>> memList = binBEMBTIF01_Service.getMemMergeList(map);
			// 会员信息List不为空
			if (!CherryBatchUtil.isBlankList(memList)) {
				try {
					// 执行合并处理
					executeMembers(memList, map);
				} catch (Exception e) {
					logger.error("Member Merge exception：" + e.getMessage(),e);
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
			// 去除会员BATCH执行状态(天猫会员合并表)
			binBEMBTIF01_Service.updateClearMergeExec(map);
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
		TmallKeyDTO mTmallKey = (TmallKeyDTO) map.get("mTmallKey");
		String mixKey = mTmallKey.getMixKey();
		for (int i = 0; i < memList.size(); i++) {
			Map<String, Object> memberInfo = memList.get(i);
			int tmallMergeId = Integer.parseInt(String.valueOf(memberInfo.get("tmallMergeId")));
			try {
				int memberInfoId = Integer.parseInt(String.valueOf(memberInfo.get("memberInfoId")));
				int preMemId = Integer.parseInt(String.valueOf(memberInfo.get("preMemId")));
				Map<String, Object> newMemInfo = binBEMBTIF01_Service.getNewMemberInfo(memberInfo);
				if (null == newMemInfo || newMemInfo.isEmpty()) {
					logger.error("查询不到会员信息,会员ID：" + memberInfoId + "记录ID：" + tmallMergeId);
					continue;
				}
				String tmallMixMobile = (String) newMemInfo.get("tmallMixMobile");
				if (!CherryChecker.isNullOrEmpty(tmallMixMobile)) {
					logger.error("会员已存在加密后的手机号，会员ID：" + memberInfoId + "记录ID：" + tmallMergeId);
					continue;
				}
				String brandCode = (String) map.get("brandCode");
				String mobile = CherrySecret.decryptData(brandCode, (String) newMemInfo.get("mobilePhone"));
				String mixMobile = null;
				if (CherryChecker.isPhoneValid(mobile, "^(1[34578])[0-9]{9}$")) {
					mixMobile = DigestUtils.md5Hex(DigestUtils.md5Hex("tmall" + mobile + mixKey));
				}
				if (null == mixMobile || !mixMobile.equals(memberInfo.get("mixMobile"))) {
					logger.error("加密的手机号码不一致，会员ID：" + memberInfoId + "记录ID：" + tmallMergeId);
					continue;
				}
				commParamsForUp(memberInfo);
				// 删除假登陆会员信息
				binBEMBTIF01_Service.delPreMemberInfo(memberInfo);
				// 删除假登陆会员卡信息
				binBEMBTIF01_Service.delPreMemCode(memberInfo);
				// 更新新会员信息
				binBEMBTIF01_Service.updateNewMemInfo(memberInfo);
				// 取得假登陆会员积分信息
				Map<String, Object> prePointInfo = binBEMBTIF01_Service.getPrePointInfo(memberInfo);
				if (null != prePointInfo && !prePointInfo.isEmpty()) {
					if (null == newMemInfo.get("memberPointId")) {
						// 变更积分表中假登陆会员的ID
						binBEMBTIF01_Service.updatePointMemId(memberInfo);
					} else {
						double preTotalPoint = Double.parseDouble(String.valueOf(prePointInfo.get("totalPoint")));
						double preChangePoint = Double.parseDouble(String.valueOf(prePointInfo.get("changablePoint")));
						double totalPoint = Double.parseDouble(String.valueOf(newMemInfo.get("totalPoint")));
						double changePoint = Double.parseDouble(String.valueOf(newMemInfo.get("changablePoint")));
						memberInfo.put("newTotalPoint", DoubleUtil.add(totalPoint, preTotalPoint));
						memberInfo.put("newChangePoint", DoubleUtil.add(changePoint, preChangePoint));
						// 更新新会员的积分值
						binBEMBTIF01_Service.updateNewMemPoint(memberInfo);
						// 删除假登陆会员积分信息 
						binBEMBTIF01_Service.delPreMemPoint(memberInfo);
					}
				}
				// 变更会员使用化妆次数积分明细记录的假登陆会员的ID
				int uCount = binBEMBTIF01_Service.updateTmMemUsedMemId(memberInfo);
				// 变更积分变化记录的假登陆会员的ID
				int zCount = binBEMBTIF01_Service.updateTmPointChangeMemId(memberInfo);
				// 变更规则执行履历记录的假登陆会员的ID
				int rCount = binBEMBTIF01_Service.updateTmRuleRecordMemId(memberInfo);
				// 变更新会员注册表的假登陆会员的ID
				binBEMBTIF01_Service.updatePreMemRegister(memberInfo);
				// 变更天猫积分兑换履历表 的假登陆会员的ID
				binBEMBTIF01_Service.updatePreTmallPointMemId(memberInfo);
				memberInfo.put("mergeFlag", 1);
				// 更新天猫会员合并表执行结果
				binBEMBTIF01_Service.updateMemMergeResult(memberInfo);
				if (uCount > 0 || zCount > 0 || rCount > 0) {
					// 查询会员产生的最早业务时间
					String reCalcDate = binBEMBTIF01_Service.getMinBillDate(memberInfo);
					if (!CherryChecker.isNullOrEmpty(reCalcDate)) {
						memberInfo.put("reCalcDate", reCalcDate);
						memberInfo.put("organizationInfoId", map.get("organizationInfoId"));
						memberInfo.put("brandInfoId", map.get("brandInfoId"));
						// 更新重算信息
						updateReCalcInfo(memberInfo);
					}
				}
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
				batchLoggerDTO.setCode("EMI00008");
				// 会员ID
				batchLoggerDTO.addParam(String.valueOf(tmallMergeId));
				batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_ERROR);
				CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(
						this.getClass());
				cherryBatchLogger.BatchLogger(batchLoggerDTO, e);
				flag = CherryBatchConstants.BATCH_WARNING;
			}
		}
	}
	
	/**
	 * 更新重算信息
	 * 
	 * @param CampBaseDTO
	 * 			会员活动基础 DTO
	 * @throws Exception
	 */
	public void updateReCalcInfo(Map<String, Object> params) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		// 所属组织
		map.put("organizationInfoID", params.get("organizationInfoId"));
		// 所属品牌
		map.put("brandInfoID", params.get("brandInfoId"));
		// 会员ID
		map.put("memberInfoId", params.get("memberInfoId"));
		// 等级和化妆次数重算
		map.put("reCalcType", DroolsConstants.RECALCTYPE0);
		// 单据日期
		map.put("reCalcDate", params.get("reCalcDate"));
		// 查询是否已登录重算表
		int count = binbedrcom01BL.getBTReCalcCount(map);
		if (count == 0) {
			// 更新重算信息表
			binbedrcom01BL.insertReCalcInfo(map);
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
		map.put(CherryBatchConstants.CREATEDBY, "BINBEMBTIF05");
		// 作成程序名
		map.put(CherryBatchConstants.CREATEPGM, "BINBEMBTIF05");
		// 更新者
		map.put(CherryBatchConstants.UPDATEDBY, "BINBEMBTIF05");
		// 更新程序名
		map.put(CherryBatchConstants.UPDATEPGM, "BINBEMBTIF05");
	}

}
