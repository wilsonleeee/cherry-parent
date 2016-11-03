package com.cherry.mb.vis.function;

import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.mb.vis.bl.BINBEMBVIS01_BL;
import com.opensymphony.module.propertyset.PropertySet;
import com.opensymphony.workflow.FunctionProvider;
import com.opensymphony.workflow.WorkflowException;

public class BINBEMBVIS01_FN implements FunctionProvider {
	
	private static Logger logger = LoggerFactory.getLogger(BINBEMBVIS01_FN.class.getName());
	
	/** 会员回访同步batch处理BL */
	@Resource
	private BINBEMBVIS01_BL binBEMBVIS01_BL;

	@SuppressWarnings("unchecked")
	@Override
	public void execute(Map transientVars, Map args, PropertySet ps)
			throws WorkflowException {
		try {
			logger.info("******************************会员回访同步BATCH处理开始***************************");
			int flag = binBEMBVIS01_BL.tran_BatchMemVist(transientVars);
			ps.setInt("result", flag);
		} catch (Exception e) {
			// 打印错误信息
			logger.error(e.getMessage(),e);
			throw new WorkflowException();
		} finally {
			logger.info("******************************会员回访同步BATCH处理结束***************************");
		}
	}

}
