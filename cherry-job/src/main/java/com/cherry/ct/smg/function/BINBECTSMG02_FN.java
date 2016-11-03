package com.cherry.ct.smg.function;

import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.ct.smg.bl.BINBECTSMG02_BL;
import com.opensymphony.module.propertyset.PropertySet;
import com.opensymphony.workflow.FunctionProvider;
import com.opensymphony.workflow.WorkflowException;

public class BINBECTSMG02_FN implements FunctionProvider {
	
	private static Logger logger = LoggerFactory.getLogger(BINBECTSMG02_FN.class.getName());
	
	/** 沟通任务动态调度管理BL */
	@Resource
	private BINBECTSMG02_BL binBECTSMG02_BL;

	@Override
	public void execute(Map transientVars, Map args, PropertySet ps)
			throws WorkflowException {
		try {
			logger.info("******************************沟通任务动态调度处理开始***************************");
			int flag = binBECTSMG02_BL.tran_ScheduleTask(transientVars);
			ps.setInt("result", flag);
		} catch (Exception e) {
			// 打印错误信息
			logger.error(e.getMessage(),e);
			throw new WorkflowException();
		} finally {
			logger.info("******************************沟通任务动态调度处理结束***************************");
		}
	}

}
