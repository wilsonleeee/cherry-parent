package com.cherry.mb.tif.bl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.cm.cmbussiness.interfaces.BINOLCM31_IF;
import com.cherry.cm.core.BatchLoggerDTO;
import com.cherry.cm.core.CherryBatchConstants;
import com.cherry.cm.core.CherryBatchLogger;
import com.cherry.cm.core.TmallKeyDTO;
import com.cherry.cm.core.TmallKeys;
import com.cherry.cm.util.CherryBatchUtil;
import com.cherry.mb.tif.service.BINBEMBTIF01_Service;

public class BINBEMBTIF04_BL {
	
	private static Logger logger = LoggerFactory.getLogger(BINBEMBTIF04_BL.class.getName());
	
	/** BATCH处理标志 */
	private int flag = CherryBatchConstants.BATCH_SUCCESS;
	
	@Resource
	private BINBEMBTIF01_Service binBEMBTIF01_Service;
	
	@Resource
	private BINOLCM31_IF binOLCM31_BL;
	
	/** 处理总条数 */
	private int totalCount = 0;

	/** 失败条数 */
	private int failCount = 0;
	
	/**
	 * 天猫积分兑换同步处理
	 * 
	 * @param map 参数集合
	 * @return BATCH处理标志
	 */
	public int tran_PointChange(Map<String, Object> map) throws Exception {
		TmallKeyDTO tmallKey = TmallKeys.getTmallKeyBybrandCode((String) map.get("brandCode"));
		if (null == tmallKey) {
			throw new Exception("can not get brand keys!");
		}
		map.put("mTmallKey", tmallKey);
		totalCount = 0;
		failCount = 0;
		try {
			// 去除会员BATCH执行状态
			binBEMBTIF01_Service.updateClearBatchExec(map);
			// 更新会员BATCH执行状态
			binBEMBTIF01_Service.updatePointBatchExec(map);
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
			// 取得积分兑换的会员信息List
			List<Map<String, Object>> memList = binBEMBTIF01_Service.getMemPointList(map);
			// 会员信息List不为空
			if (!CherryBatchUtil.isBlankList(memList)) {
				try {
					// 执行积分兑换处理
					executeMembers(memList, map);
				} catch (Exception e) {
					logger.error("Member point change exception：" + e.getMessage());
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
			binBEMBTIF01_Service.updateClearBatchExec(map);
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
	 * 执行同步处理
	 * 
	 * @param memSyncList 
	 * 			会员信息List
	 * @throws Exception 
	 * 
	 */
	public void executeMembers(List<Map<String, Object>> memList, Map<String, Object> map) throws Exception {
//		TmallKeyDTO mTmallKey = (TmallKeyDTO) map.get("mTmallKey");
//		String appKey = mTmallKey.getAppKey();
//		String appSecret = mTmallKey.getAppSecret();
//		String sessionKey = mTmallKey.getSessionKey();
		BatchLoggerDTO batchLoggerDTO = new BatchLoggerDTO();
		totalCount += memList.size();
		for (int i = 0; i < memList.size(); i++) {
			Map<String, Object> memberInfo = memList.get(i);
			List<Map<String, Object>> detailList = (List<Map<String, Object>>) memberInfo.get("detailList");
			if (null == detailList || detailList.isEmpty()) {
				continue;
			}
			int memberInfoId = Integer.parseInt(String.valueOf(detailList.get(0).get("memberInfoId")));
//			if (binOLCM31_BL.isMemReCalcExec(map)) {
//				batchLoggerDTO.clear();
//				batchLoggerDTO.setCode("EMI00005");
//				// 会员ID
//				batchLoggerDTO.addParam(String.valueOf(memberInfoId));
//				batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_ERROR);
//				CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(
//						this.getClass());
//				cherryBatchLogger.BatchLogger(batchLoggerDTO);
//				continue;
//			}
			try {
//				double totalPoint = Double.parseDouble(String.valueOf(memberInfo.get("totalPoint")));
				for (Map<String, Object> pointInfo : detailList) {
//					double point = Double.parseDouble(String.valueOf(pointInfo.get("point")));
//					totalPoint = DoubleUtil.add(totalPoint, point);
//					int type = Integer.parseInt(String.valueOf(pointInfo.get("type")));
//					String errCode = null;
//					if (1 == type && totalPoint < 0) {
//						pointInfo.put("ptResult", 1);
//						errCode = "deduct-fail:point-no-enough";
//						pointInfo.put("errorCode", errCode);
//					} else {
//						pointInfo.put("ptResult", 0);
//					}
					pointInfo.put("ptFlag", 1);
					pointInfo.put("ptResult", 0);
					commParamsForUp(pointInfo);
					String tmallPointId = String.valueOf(pointInfo.get("tmallPointId"));
					try {
//						String mixMobile = (String) pointInfo.get("tmallMixMobile");
//						Long recordId = Long.parseLong(String.valueOf(pointInfo.get("recordId")));
						binBEMBTIF01_Service.updateTmallPointPTFlag(pointInfo);
//						callbackTmall(mixMobile, recordId, errCode, appKey, appSecret, sessionKey);
//						if (null == errCode) {
							pointInfo.putAll(map);
							pointInfo.put("memCode", memberInfo.get("memCode"));
//							try {
							sendPointsMQ(pointInfo);
//							} catch (Exception e) {
//								batchLoggerDTO.clear();
//								batchLoggerDTO.setCode("EMI00007");
//								// 会员ID
//								batchLoggerDTO.addParam(String.valueOf(tmallPointId));
//								batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_ERROR);
//								CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(
//										this.getClass());
//								cherryBatchLogger.BatchLogger(batchLoggerDTO, e);
//							}
//						}
						binBEMBTIF01_Service.manualCommit();
					} catch (Exception e) {
						batchLoggerDTO.clear();
						batchLoggerDTO.setCode("EMI00006");
						// 会员ID
						batchLoggerDTO.addParam(String.valueOf(tmallPointId));
						batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_ERROR);
						CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(
								this.getClass());
						cherryBatchLogger.BatchLogger(batchLoggerDTO, e);
						try {
							// 事务回滚
							binBEMBTIF01_Service.manualRollback();
						} catch (Exception ex) {
						}
					}
				}
			}catch (Exception e) {
				// 失败件数加一
				failCount++;
				logger.error(e.getMessage(),e);
				batchLoggerDTO.clear();
				batchLoggerDTO.setCode("EMI00004");
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
	
//	private void callbackTmall(String mixMobile, Long recordId, String errCode, String appKey, String appSecret, String sessionKey) throws Exception {
//		TmallMeiCrmCallbackPointChangeRequest req =
//				new TmallMeiCrmCallbackPointChangeRequest();
//		req.setMixMobile(mixMobile);
//		req.setRecordId(recordId);
//		if (null == errCode) {
//			req.setResult(0L);
//		} else {
//			req.setResult(1L);
//			req.setErrorCode(errCode);
//		}
//		TmallMeiCrmCallbackPointChangeResponse response = SignTool.pointChangeResponse(req, appKey, appSecret, sessionKey);
//		if (!response.isSuccess()) {
//			throw new Exception("Tmall response error: " + response.getSubCode() + " msg: " + response.getSubMsg());
//		}
//	}
	
	/**
	 * 将消息发送到积分维护的MQ队列里
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	private void sendPointsMQ(Map map) throws Exception {
		String memberCode = (String) map.get("memCode");
		if (null == memberCode) {
			memberCode = binOLCM31_BL.getMemCard(map);
		}
		map.put("MemberCode", memberCode);
		double point = Double.parseDouble(String.valueOf(map.get("point")));
		String order_id = (String) map.get("orderId");
		String reason = null;
		String bizType = (String) map.get("bizType");
		if ("gift_exchange".equals(bizType)) {
			reason = "天猫礼品兑换，订单号：" + order_id;
		} else if ("cancel_exchange".equals(bizType)) {
			reason = "天猫取消兑换，订单号：" + order_id;
		} else if ("coupon_exchange".equals(bizType)) {
			reason = "天猫优惠券兑换";
		} else if ("cancel_coupon_exchange".equals(bizType)) {
			reason = "天猫取消优惠券兑换";
		} else if ("sign".equals(bizType)) {
			reason = "签到增加积分";
		} else if ("game".equals(bizType)) {
			reason = "玩游戏增加积分";
		} else {
			reason = "其他";
		}
		map.put("ModifyPoint", point);
		map.put("BusinessTime", map.get("pointTime"));
		map.put("Reason", reason);
		map.put("pointType", "2");
		map.put("Sourse", "Tmall");
		// 天猫记录ID
		map.put("TmRecordId", map.get("recordId"));
		binOLCM31_BL.sendPointsMQ(map);
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
		map.put(CherryBatchConstants.CREATEDBY, "BINBEMBTIF04");
		// 作成程序名
		map.put(CherryBatchConstants.CREATEPGM, "BINBEMBTIF04");
		// 更新者
		map.put(CherryBatchConstants.UPDATEDBY, "BINBEMBTIF04");
		// 更新程序名
		map.put(CherryBatchConstants.UPDATEPGM, "BINBEMBTIF04");
	}
}
