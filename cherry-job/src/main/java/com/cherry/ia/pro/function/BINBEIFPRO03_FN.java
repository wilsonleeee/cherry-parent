package com.cherry.ia.pro.function;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.core.CherryBatchConstants;
import com.cherry.cm.util.ConvertUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.cm.core.CherryBatchException;
import com.cherry.ia.pro.bl.BINBEIFPRO03_BL;
import com.googlecode.jsonplugin.JSONException;
import com.opensymphony.module.propertyset.PropertySet;
import com.opensymphony.workflow.FunctionProvider;
import com.opensymphony.workflow.WorkflowException;

public class BINBEIFPRO03_FN implements FunctionProvider{
	
	@Resource
	private BINBEIFPRO03_BL binbeifpro03BL;
	
	private static Logger logger = LoggerFactory.getLogger(BINBEIFPRO03_FN.class.getName());
	
	@Override
	@SuppressWarnings("unchecked")
	public void execute(Map transientVars, Map args, PropertySet ps)
			throws WorkflowException {
		logger.info("******************************柜台产品下发BATCH处理开始***************************");
		try {
			Map<String, Object> resMap = new HashMap<String, Object>();
			int result = binbeifpro03BL.tran_batchCouProducts(transientVars);
			if(result == CherryBatchConstants.BATCH_SUCCESS) {
				// 备份产品下发数据/MQ下发
				Map<String, Object> flagMapMQ = binbeifpro03BL.tran_batchCntProductsMQSend(transientVars);
				resMap.putAll(flagMapMQ);
				result= ConvertUtil.getInt(resMap.get("flag"));
			}
			binbeifpro03BL.outMessage();
			transientVars.put("JobCode","BAT081");
			binbeifpro03BL.tran_programEnd(transientVars);
			ps.setInt("result", result);
		} catch (CherryBatchException e) {
			logger.error(e.toString(),e);
		} catch (JSONException e) {
			logger.error(e.toString(),e);
			e.printStackTrace();
		} catch (Exception e) {
			logger.error(e.toString(),e);
			e.printStackTrace();
		}
		logger.info("******************************柜台产品下发BATCH处理结束***************************");
	}

}
