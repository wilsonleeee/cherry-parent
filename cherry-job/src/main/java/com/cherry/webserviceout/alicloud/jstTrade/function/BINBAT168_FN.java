package com.cherry.webserviceout.alicloud.jstTrade.function;

import com.cherry.webserviceout.alicloud.jstTrade.bl.BINBAT168_BL;
import com.opensymphony.module.propertyset.PropertySet;
import com.opensymphony.workflow.FunctionProvider;
import com.opensymphony.workflow.WorkflowException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import java.util.Map;

public class BINBAT168_FN implements FunctionProvider{
	
	private static Logger logger = LoggerFactory.getLogger(BINBAT168_FN.class.getName());

	@Resource(name="binbat168_BL")
	private BINBAT168_BL binbat168_BL;
	
	@Override
	public void execute(Map transientVars, Map args, PropertySet ps)
			throws WorkflowException {
		try {
			logger.info("******************************天猫退款转销售MQBatch处理开始***************************");
			int flag = binbat168_BL.tran_batchBat168(transientVars);
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
