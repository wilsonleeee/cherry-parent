package com.cherry.mb.tif.function;

import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.mb.tif.bl.BINBEMBTIF07_BL;
import com.opensymphony.module.propertyset.PropertySet;
import com.opensymphony.workflow.FunctionProvider;
import com.opensymphony.workflow.WorkflowException;

public class BINBEMBTIF07_FN implements FunctionProvider{
	
	private static Logger logger = LoggerFactory.getLogger(BINBEMBTIF07_FN.class.getName());
	
	/** 重新回调天猫积分处理BL */
	@Resource
	private BINBEMBTIF07_BL binBEMBTIF07_BL;
	
	@Override
	public void execute(Map transientVars, Map args, PropertySet ps)
			throws WorkflowException {
		try {
			logger.info("******************************重新回调天猫积分处理开始***************************");
			int flag = binBEMBTIF07_BL.tran_recallTmall(transientVars);
			ps.setInt("result", flag);
		} catch (Exception e) {
			// 打印错误信息
			logger.error(e.getMessage(),e);
			throw new WorkflowException();
		} finally {
			logger.info("******************************重新回调天猫积分处理结束***************************");
		}
	}

}
