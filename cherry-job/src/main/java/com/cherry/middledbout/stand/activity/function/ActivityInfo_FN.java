package com.cherry.middledbout.stand.activity.function;

import java.util.Map;
import javax.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.cherry.ia.cou.function.BINBEIFCOU02_FN;
import com.cherry.middledbout.stand.activity.bl.ActivityInfo_BL;
import com.opensymphony.module.propertyset.PropertySet;
import com.opensymphony.workflow.FunctionProvider;
import com.opensymphony.workflow.WorkflowException;

/**
 * 
 * 促销活动列表导出FN
 * 
 * @author ZhaoCF
 * 
 */
public class ActivityInfo_FN implements FunctionProvider {
	
	private static Logger logger = LoggerFactory.getLogger(BINBEIFCOU02_FN.class.getName());
	
	/** 促销活动列表导出BL */
	@Resource
	private ActivityInfo_BL activityInfo_BL;

	@Override
	public void execute(Map transientVars, Map args, PropertySet ps)
			throws WorkflowException {
		try {
			logger.info("******************************促销活动列表导出BATCH处理开始***************************");
			int flag = activityInfo_BL.tran_activityInfo(transientVars);
			ps.setInt("result", flag);
		} catch (Exception e) {
			// 打印错误信息
			logger.error(e.getMessage(),e);
			throw new WorkflowException();
		} finally {
			logger.info("******************************促销活动列表导出BATCH处理结束***************************");
		}
	}

}
