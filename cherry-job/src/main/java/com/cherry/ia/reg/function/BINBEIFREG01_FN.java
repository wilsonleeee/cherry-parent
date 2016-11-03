package com.cherry.ia.reg.function;

import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.cm.core.CherryBatchException;
import com.cherry.ia.reg.bl.BINBEIFREG01_BL;
import com.googlecode.jsonplugin.JSONException;
import com.opensymphony.module.propertyset.PropertySet;
import com.opensymphony.workflow.FunctionProvider;
import com.opensymphony.workflow.WorkflowException;

public class BINBEIFREG01_FN implements FunctionProvider{
	
	@Resource
	private BINBEIFREG01_BL binBEIFREG01_BL;
	
	private static Logger logger = LoggerFactory.getLogger(BINBEIFREG01_FN.class.getName());
	
	@Override
	@SuppressWarnings("unchecked")
	public void execute(Map transientVars, Map args, PropertySet ps)
			throws WorkflowException {
		logger.info("******************************区域信息下发(BINBEIFREG01)Batch处理开始***************************");
		try {
			int result = binBEIFREG01_BL.tran_batchRegion(transientVars);
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
		logger.info("******************************区域信息下发(BINBEIFREG01)Batch处理结束***************************");
	}

}
