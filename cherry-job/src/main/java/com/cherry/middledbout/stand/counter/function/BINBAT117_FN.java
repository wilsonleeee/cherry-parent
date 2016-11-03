package com.cherry.middledbout.stand.counter.function;

import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.middledbout.stand.counter.bl.BINBAT117_BL;
import com.opensymphony.module.propertyset.PropertySet;
import com.opensymphony.workflow.FunctionProvider;
import com.opensymphony.workflow.WorkflowException;

/**
*
* 标准接口：柜台信息导出FN
*
* @author ZhaoCF
*
* @version  2015-7-28
*/
public class BINBAT117_FN implements FunctionProvider {

private static Logger logger = LoggerFactory.getLogger(BINBAT117_FN.class.getName());
	
	/** 柜台信息导出BL*/
	@Resource(name="binBAT117_BL")
	private BINBAT117_BL binBAT117_BL;

	@Override
	public void execute(Map transientVars, Map args, PropertySet ps)
			throws WorkflowException {
		try {
			logger.info("******************************柜台信息导出（标准接口）BATCH处理开始***************************");
			// Job运行履历表的运行方式
			transientVars.put("RunType", "AT");
			int flag = binBAT117_BL.tran_binBAT117(transientVars);
			ps.setInt("result", flag);
		} catch (Exception e) {
			// 打印错误信息
			logger.error(e.getMessage(),e);
			throw new WorkflowException();
		} finally {
			logger.info("******************************柜台信息导出（标准接口）BATCH处理开始***************************");
		}
	}

}
