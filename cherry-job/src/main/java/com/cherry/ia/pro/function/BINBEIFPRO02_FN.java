package com.cherry.ia.pro.function;

import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.cm.core.CherryBatchException;
import com.cherry.ia.pro.bl.BINBEIFPRO02_BL;
import com.opensymphony.module.propertyset.PropertySet;
import com.opensymphony.workflow.FunctionProvider;
import com.opensymphony.workflow.WorkflowException;

public class BINBEIFPRO02_FN implements FunctionProvider{
	
	@Resource
	private BINBEIFPRO02_BL binbeifpro02BL;
	
	private static Logger logger = LoggerFactory.getLogger(BINBEIFPRO02_FN.class.getName());
	
	@Override
	@SuppressWarnings("unchecked")
	public void execute(Map transientVars, Map args, PropertySet ps)
			throws WorkflowException {
		logger.info("******************************产品下发BATCH处理开始***************************");
		try {
			int result = binbeifpro02BL.tran_batchProducts(transientVars);
			ps.setInt("result", result);
		} catch (CherryBatchException e) {
			logger.error(e.toString(),e);
		}
		logger.info("******************************产品下发BATCH处理结束***************************");
	}

}
