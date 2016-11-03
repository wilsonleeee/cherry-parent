package com.cherry.ia.pro.function;

import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.cm.core.CherryBatchException;
import com.cherry.ia.pro.bl.BINBEIFPRO05_BL;
import com.googlecode.jsonplugin.JSONException;
import com.opensymphony.module.propertyset.PropertySet;
import com.opensymphony.workflow.FunctionProvider;
import com.opensymphony.workflow.WorkflowException;

public class BINBEIFPRO05_FN implements FunctionProvider{
	
	@Resource
	private BINBEIFPRO05_BL binbeifpro05BL;
	
	private static Logger logger = LoggerFactory.getLogger(BINBEIFPRO05_FN.class.getName());
	
	@Override
	@SuppressWarnings("unchecked")
	public void execute(Map transientVars, Map args, PropertySet ps)
			throws WorkflowException {
		logger.info("******************************特价产品下发BATCH处理开始***************************");
		try {
			int result = binbeifpro05BL.tran_batchCouProducts(transientVars);
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
		logger.info("******************************特价产品下发BATCH处理结束***************************");
	}

}
