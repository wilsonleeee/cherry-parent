package com.cherry.chando.counter.function;

import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.chando.counter.bl.BINBAT137_BL;
import com.opensymphony.module.propertyset.PropertySet;
import com.opensymphony.workflow.FunctionProvider;
import com.opensymphony.workflow.WorkflowException;

/**
*
* 自然堂：调整BAS生效时间
*
* @author lzs
*
* @version  2016-02-16
*/
public class BINBAT137_FN implements FunctionProvider {

private static Logger logger = LoggerFactory.getLogger(BINBAT137_FN.class.getName());
	
	/** 调整BAS生效时间BL*/
	@Resource(name="binBAT137_BL")
	private BINBAT137_BL binBAT137_BL;

	@Override
	public void execute(Map transientVars, Map args, PropertySet ps)
			throws WorkflowException {
		try {
			logger.info("******************************调整BAS生效时间（自然堂）BATCH处理开始***************************");
			// Job运行履历表的运行方式
			transientVars.put("RunType", "AT");
			int flag = binBAT137_BL.tran_binBAT137(transientVars);
			ps.setInt("result", flag);
		} catch (Exception e) {
			// 打印错误信息
			logger.error(e.getMessage(),e);
			throw new WorkflowException();
		} finally {
			logger.info("******************************调整BAS生效时间（自然堂）BATCH处理结束***************************");
		}
	}

}
