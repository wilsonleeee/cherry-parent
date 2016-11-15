package com.cherry.webserviceout.alicloud.jstTrade.function;

import com.cherry.webserviceout.alicloud.jstTrade.bl.BINBAT125_BL;
import com.opensymphony.module.propertyset.PropertySet;
import com.opensymphony.workflow.FunctionProvider;
import com.opensymphony.workflow.WorkflowException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import java.util.Map;

public class BINBAT125_FN implements FunctionProvider{
	
	private static Logger logger = LoggerFactory.getLogger(BINBAT125_FN.class.getName());

	@Resource(name="binbat125_BL")
	private BINBAT125_BL binbat125_BL;
	
	@Override
	public void execute(Map transientVars, Map args, PropertySet ps)
			throws WorkflowException {
		try {
			logger.info("******************************天猫退款转销售MQBatch处理开始***************************");
			int flag = binbat125_BL.tran_batchBat125(transientVars);
			ps.setInt("result", flag);
		} catch (Exception e) {
			// 打印错误信息
			logger.error(e.getMessage(),e);
            throw new WorkflowException();
		} finally {
			logger.info("******************************天猫退款转销售MQBatch处理结束***************************");
		}
		
	}

}
