package com.cherry.webserviceout.kingdee.counterInfo.function;

import java.util.Map;
import javax.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.cherry.webserviceout.kingdee.counterInfo.bl.BINBEKDCOU01_BL;
import com.opensymphony.module.propertyset.PropertySet;
import com.opensymphony.workflow.FunctionProvider;
import com.opensymphony.workflow.WorkflowException;

/**
*
* Kingdee接口：柜台下发FN
*
* @author ZhaoCF
*
* @version  2015-4-29
*/
public class BINBEKDCOU01_FN implements FunctionProvider {

private static Logger logger = LoggerFactory.getLogger(BINBEKDCOU01_FN.class.getName());
	
	/** 柜台下发BL */
	@Resource(name="binbekdcou01_BL")
	private BINBEKDCOU01_BL binbekdcou01_BL;

	@Override
	public void execute(Map transientVars, Map args, PropertySet ps)
			throws WorkflowException {
		try {
			logger.info("******************************柜台下发（韩束金蝶）BATCH处理开始***************************");
			// Job运行履历表的运行方式
			transientVars.put("RunType", "AT");
			int flag = binbekdcou01_BL.tran_batchCou01(transientVars);
			ps.setInt("result", flag);
		} catch (Exception e) {
			// 打印错误信息
			logger.error(e.getMessage(),e);
			throw new WorkflowException();
		} finally {
			logger.info("******************************柜台下发（韩束金蝶）BATCH处理开始***************************");
		}
	}

}
