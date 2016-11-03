package com.cherry.cp.act.function;

import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.cp.act.bl.BINCPMEACT03_BL;
import com.opensymphony.module.propertyset.PropertySet;
import com.opensymphony.workflow.FunctionProvider;
import com.opensymphony.workflow.WorkflowException;

public class BINCPMEACT03_FN implements FunctionProvider {
	
	private static Logger logger = LoggerFactory.getLogger(BINCPMEACT03_FN.class.getName());
	
	/** 导入会员活动和会员活动预约信息处理BL */
	@Resource
	private BINCPMEACT03_BL binCPMEACT03_BL;

	@Override
	public void execute(Map transientVars, Map args, PropertySet ps)
			throws WorkflowException {
		try {
			logger.info("******************************导入会员活动和会员活动预约信息处理开始***************************");
			int flag = binCPMEACT03_BL.tran_importCampaignInfo(transientVars);
			ps.setInt("result", flag);
		} catch (Exception e) {
			// 打印错误信息
			logger.error(e.getMessage(),e);
			throw new WorkflowException();
		} finally {
			logger.info("******************************导入会员活动和会员活动预约信息处理结束***************************");
		}
	}

}
