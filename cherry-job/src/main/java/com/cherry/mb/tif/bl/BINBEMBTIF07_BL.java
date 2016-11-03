package com.cherry.mb.tif.bl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.cm.core.BatchLoggerDTO;
import com.cherry.cm.core.CherryBatchConstants;
import com.cherry.cm.core.CherryBatchException;
import com.cherry.cm.core.CherryBatchLogger;
import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.PropertiesUtil;
import com.cherry.cm.core.TmallKeyDTO;
import com.cherry.cm.core.TmallKeys;
import com.cherry.cm.util.CherryBatchUtil;
import com.cherry.cm.util.SignTool;
import com.cherry.mb.tif.service.BINBEMBTIF01_Service;
import com.taobao.api.request.TmallMeiCrmCallbackPointChangeRequest;
import com.taobao.api.response.TmallMeiCrmCallbackPointChangeResponse;

public class BINBEMBTIF07_BL {
	
	private static Logger logger = LoggerFactory.getLogger(BINBEMBTIF07_BL.class.getName());
	
	/** BATCH处理标志 */
	private int flag = CherryBatchConstants.BATCH_SUCCESS;
	
	@Resource
	private BINBEMBTIF01_Service binBEMBTIF01_Service;

	/** 失败条数 */
	private int failCount = 0;
	
	/** 共通Batch Log处理*/
	private CherryBatchLogger cherryBatchLogger;
	
	/** 共通BatchLogger*/
	private BatchLoggerDTO batchLoggerDTO;
	
	/**
	 * 重新回调天猫积分处理
	 * 
	 * @param map 参数集合
	 * @return BATCH处理标志
	 */
	public int tran_recallTmall(Map<String, Object> map) throws Exception {
		// 共通Batch Log处理
		cherryBatchLogger = new CherryBatchLogger(this.getClass());
		// 共通BatchLogger
		batchLoggerDTO = new BatchLoggerDTO();
		int count = binBEMBTIF01_Service.getRecallPointCount(map);
		// 没有新增的积分信息
		if (0 == count) {
			// 未新增记录，不执行处理
			batchLoggerDTO.setCode("IMB00023");
			batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_INFO);
			batchLoggerDTO.addParam(PropertiesUtil.getMessage("PMB00018", null));
			cherryBatchLogger.BatchLogger(batchLoggerDTO);
		} else {
			batchLoggerDTO.setCode("IMB00024");
			batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_INFO);
			batchLoggerDTO.addParam(PropertiesUtil.getMessage("PMB00019", null));
			batchLoggerDTO.addParam(String.valueOf(count));
			cherryBatchLogger.BatchLogger(batchLoggerDTO);
			// 查询会员积分信息
			List<Map<String, Object>> pointList = binBEMBTIF01_Service.getRecallPointList(map);
			// 会员积分信息不为空
			if (!CherryBatchUtil.isBlankList(pointList)) {
				// 天猫积分回调处理
				callTmallPoint(pointList, map, 1);
			}
			batchLoggerDTO.clear();
			batchLoggerDTO.setCode("IMB00026");
			batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_INFO);
			batchLoggerDTO.addParam(PropertiesUtil.getMessage("PMB00019", null));
			batchLoggerDTO.addParam(String.valueOf(count));
			batchLoggerDTO.addParam(String.valueOf(failCount));
			cherryBatchLogger.BatchLogger(batchLoggerDTO);
		}
		logger.info("**********************重新处理积分变更表中失败的记录开始**************************");
		List<Map<String, Object>> failList = binBEMBTIF01_Service.getFailPointList(map);
		int zcount = 0;
		if (null != failList && !failList.isEmpty()) {
			zcount = failList.size();
			for (Map<String, Object> tmRecallInfo : failList) {
				Long recordId = null;
				try {
					recordId = Long.parseLong(tmRecallInfo.get("recordId").toString());
					tmRecallInfo.put("updatedBy", "BINBEMBTIF07");
					tmRecallInfo.put("updatePGM", "BINBEMBTIF07");
					tmRecallInfo.put("ptFlag", 2);
					binBEMBTIF01_Service.updateFailPointInfo(tmRecallInfo);
					String errCode = (String) tmRecallInfo.get("errorCode");
					String mixMobile = (String) tmRecallInfo.get("tmallMixMobile");
					callbackTmall(mixMobile, recordId, errCode, (String) map.get("brandCode"));
					binBEMBTIF01_Service.manualCommit();
				} catch (Exception e) {
					logger.error("******************************回调天猫积分处理结果发生异常，记录ID:" + recordId);
					logger.error(e.getMessage(),e);
				}
			}
		}
		logger.info("***********************************************处理的总件数：" + zcount);
		logger.info("**********************重新处理积分变更表中失败的记录结束**************************");
		return flag;
	}
	
	/**
	 *天猫积分回调处理
	 * 
	 * @param pointList 
	 * 			会员积分信息
	 * @param map 
	 * 			共通参数
	 * @param pageNum 
	 * 			批次
	 * @return 
	 * @throws CherryBatchException
	 * 
	 */
	public boolean callTmallPoint(List<Map<String, Object>> pointList, 
			Map<String, Object> map, int pageNum) throws CherryBatchException {
		boolean nextFlag = true;
		// 本批次处理总件数
		int size = pointList.size();
		// 本次失败件数
		int pointFailCount = 0;
		for (int i = 0; i < pointList.size(); i++) {
			Map<String, Object> tmRecallInfo = pointList.get(i);
			Long recordId = null;
			try {
				recordId = Long.parseLong(tmRecallInfo.get("recordId").toString());
				tmRecallInfo.put("updatedBy", "BINBEMBTIF07");
				tmRecallInfo.put("updatePGM", "BINBEMBTIF07");
				tmRecallInfo.put("tmallRecallFlag", 1);
				binBEMBTIF01_Service.updateTMUsedInfo(tmRecallInfo);
				String errCode = (String) tmRecallInfo.get("errorCode");
				String mixMobile = (String) tmRecallInfo.get("tmallMixMobile");
				callbackTmall(mixMobile, recordId, errCode, (String) map.get("brandCode"));
				binBEMBTIF01_Service.manualCommit();
			} catch (Exception e) {
				pointFailCount++;
				logger.error("******************************回调天猫积分处理结果发生异常，记录ID:" + recordId);
				logger.error(e.getMessage(),e);
			}
		}
		batchLoggerDTO.clear();
		batchLoggerDTO.setCode("IMB00025");
		batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_INFO);
		batchLoggerDTO.addParam(String.valueOf(pageNum));
		batchLoggerDTO.addParam(String.valueOf(size));
		batchLoggerDTO.addParam(String.valueOf(pointFailCount));
		cherryBatchLogger.BatchLogger(batchLoggerDTO);
		failCount += pointFailCount;
		return nextFlag;
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
				if (!response.isSuccess()) {
					String errMsg = "Tmall response error: " + response.getSubCode() + " message: " + response.getSubMsg() + " 回调次数：" + (i + 1);
					logger.error(errMsg);
				}				
				break;
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
