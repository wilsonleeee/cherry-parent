package com.cherry.mb.vis.function;

import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.mb.vis.bl.BINBEMBVIS04_BL;
import com.opensymphony.module.propertyset.PropertySet;
import com.opensymphony.workflow.FunctionProvider;
import com.opensymphony.workflow.WorkflowException;

/**
 * 生成会员回访任务batch处理FN
 * 
 * @author WangCT
 * @version 1.0 2014/12/18
 */
public class BINBEMBVIS04_FN implements FunctionProvider {
	
	private static Logger logger = LoggerFactory.getLogger(BINBEMBVIS04_FN.class.getName());
	
	/** 生成会员回访任务batch处理BL */
	@Resource
	private BINBEMBVIS04_BL binBEMBVIS04_BL;

	@Override
	public void execute(Map transientVars, Map args, PropertySet ps)
			throws WorkflowException {
		try {
			logger.info("******************************生成会员回访任务BATCH处理开始***************************");
			int flag = binBEMBVIS04_BL.tran_batchMemVistTask(transientVars);
			ps.setInt("result", flag);
		} catch (Exception e) {
			// 打印错误信息
			logger.error(e.getMessage(),e);
			throw new WorkflowException();
		} finally {
			logger.info("******************************生成会员回访任务BATCH处理结束***************************");
		}
	}

}
