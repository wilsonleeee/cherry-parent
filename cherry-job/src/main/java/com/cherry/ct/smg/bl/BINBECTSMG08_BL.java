package com.cherry.ct.smg.bl;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.core.BatchLoggerDTO;
import com.cherry.cm.core.CherryBatchConstants;
import com.cherry.cm.core.CherryBatchLogger;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.ct.common.BINBECTCOM01;
import com.cherry.mq.mes.interfaces.CherryMessageHandler_IF;
import com.dianping.cat.Cat;
import com.dianping.cat.message.Transaction;

public class BINBECTSMG08_BL implements CherryMessageHandler_IF{
	
	@Resource(name = "binBECTCOM01")
	private BINBECTCOM01 binBECTCOM01;
	
	@Resource(name = "binBECTSMG07_BL")
	private BINBECTSMG07_BL binBECTSMG07_BL;
	
	@Override
	public void handleMessage(Map<String, Object> map) throws Exception {
		Transaction transaction = Cat.newTransaction("message", "BINBECTSMG08_BL");
		try{
			String couponCode = ConvertUtil.getString(map.get("couponCode"));
			// 通过电话告知验证码
			Map<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put("brandInfoId", map.get("brandInfoId"));
			paramMap.put("organizationInfoId", map.get("organizationInfoId"));
			paramMap.put("phoneNum", map.get("eventId"));
			paramMap.put("couponCode", couponCode);
			String runStatus = execPhoneCall(paramMap);
			if(!"0".equals(runStatus)){
				// 电话外呼失败的情况下记录日志
				BatchLoggerDTO batchLoggerDTO = new BatchLoggerDTO();
				batchLoggerDTO.setCode("ECT00097");
				batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_ERROR);
				batchLoggerDTO.addParam(ConvertUtil.getString(map.get("eventId")));
				batchLoggerDTO.addParam(runStatus);
				CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(this.getClass());
				cherryBatchLogger.BatchLogger(batchLoggerDTO);
			}else{
				BatchLoggerDTO batchLoggerDTO = new BatchLoggerDTO();
				batchLoggerDTO.setCode("ECT00100");
				batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_INFO);
				batchLoggerDTO.addParam(ConvertUtil.getString(map.get("eventId")));
				CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(this.getClass());
				cherryBatchLogger.BatchLogger(batchLoggerDTO);
			}
			transaction.setStatus(Transaction.SUCCESS);
		}catch (Exception t){
			transaction.setStatus(t);
			Cat.logError(t);
			throw t;
		}
		finally {
			transaction.complete();
		}
	}
	
	public String execPhoneCall(Map<String, Object> map) throws Exception {
		try{
			// 定义接口参数Map
			Map<String, Object> ifConfigMap = new HashMap<String, Object>();
			String brandInfoId = ConvertUtil.getString(map.get("brandInfoId"));
			String organizationInfoId = ConvertUtil.getString(map.get("organizationInfoId"));
			String phoneNum = ConvertUtil.getString(map.get("phoneNum"));
			String couponCode = ConvertUtil.getString(map.get("couponCode"));
			// 获取实时接口配置项
			Map<String, Object> tempMap = new HashMap<String, Object>();
			tempMap.put("brandInfoId", brandInfoId);
			tempMap.put("organizationInfoId", organizationInfoId);
			tempMap.put("commInterface", "TR");
			ifConfigMap = binBECTCOM01.getIfConfigInfo(tempMap, "PHONE");
			
			map.put("receiverCode", phoneNum);
			map.put("message", couponCode);
			map.put("commInterface", "TR");
			String runStatus = binBECTSMG07_BL.phoneCall(map, ifConfigMap);
			return runStatus;
		}catch(Exception ex){
			return "-1";
		}
	}

}
