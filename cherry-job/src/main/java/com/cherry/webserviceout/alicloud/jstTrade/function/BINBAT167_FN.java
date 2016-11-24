package com.cherry.webserviceout.alicloud.jstTrade.function;

import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.webserviceout.alicloud.jstTrade.bl.BINBAT167_BL;
import com.opensymphony.module.propertyset.PropertySet;
import com.opensymphony.workflow.FunctionProvider;
import com.opensymphony.workflow.WorkflowException;

public class BINBAT167_FN implements FunctionProvider{
	
	private static Logger logger = LoggerFactory.getLogger(BINBAT167_FN.class.getName());

	@Resource(name="binBAT167_BL")
	private BINBAT167_BL binBAT167_BL;
	
	@Override
	public void execute(Map transientVars, Map args, PropertySet ps)
			throws WorkflowException {
		try {
			logger.info("******************************天猫订单转销售MQBatch处理开始***************************");
			int flag = binBAT167_BL.tran_batchBat124(transientVars);
			ps.setInt("result", flag);
		} catch (Exception e) {
			// 打印错误信息
			logger.error(e.getMessage(),e);
            throw new WorkflowException();
		} finally {
			logger.info("******************************天猫订单转销售MQBatch处理结束***************************");
		}
		
	}

}
