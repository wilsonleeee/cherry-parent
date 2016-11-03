package com.cherry.mb.vis.function;

import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.mb.vis.bl.BINBEMBVIS03_BL;
import com.opensymphony.module.propertyset.PropertySet;
import com.opensymphony.workflow.FunctionProvider;
import com.opensymphony.workflow.WorkflowException;

public class BINBEMBVIS03_FN implements FunctionProvider {
	
	private static Logger logger = LoggerFactory.getLogger(BINBEMBVIS03_FN.class.getName());
	
	/** 会员回访任务下发batch处理BL */
	@Resource
	private BINBEMBVIS03_BL binBEMBVIS03_BL;

	@Override
	public void execute(Map transientVars, Map args, PropertySet ps)
			throws WorkflowException {
		try {
			logger.info("******************************会员回访任务同步BATCH处理开始***************************");
			int flag = binBEMBVIS03_BL.tran_MemVisitTaskSyn(transientVars);
			ps.setInt("result", flag);
		} catch (Exception e) {
			// 打印错误信息
			logger.error(e.getMessage(),e);
			throw new WorkflowException();
		} finally {
			logger.info("******************************会员回访任务同步BATCH处理结束***************************");
		}
	}

}
