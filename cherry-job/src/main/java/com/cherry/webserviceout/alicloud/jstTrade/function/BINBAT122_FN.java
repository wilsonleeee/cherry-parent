package com.cherry.webserviceout.alicloud.jstTrade.function;

import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.webserviceout.alicloud.jstTrade.bl.BINBAT122_BL;
import com.opensymphony.module.propertyset.PropertySet;
import com.opensymphony.workflow.FunctionProvider;
import com.opensymphony.workflow.WorkflowException;

public class BINBAT122_FN implements FunctionProvider{
	
	private static Logger logger = LoggerFactory.getLogger(BINBAT122_FN.class.getName());
	
	@Resource(name="binbat122_BL")
	private BINBAT122_BL binbat122_BL;
	
	@Override
	public void execute(Map transientVars, Map args, PropertySet ps)
			throws WorkflowException {
		try {
			logger.info("******************************天猫销售订单导入Batch处理开始***************************");
			int flag = binbat122_BL.tran_getTrade(transientVars);
			ps.setInt("result", flag);
		} catch (Exception e) {
			// 打印错误信息
			logger.error(e.getMessage(),e);
            throw new WorkflowException();
		} finally {
			logger.info("******************************天猫销售订单导入Batch处理结束***************************");
		}
		
	}

}
