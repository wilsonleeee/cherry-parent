package com.cherry.middledbout.stand.allocation.function;

import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.middledbout.stand.allocation.bl.BINBAT125_BL;
import com.opensymphony.module.propertyset.PropertySet;
import com.opensymphony.workflow.FunctionProvider;
import com.opensymphony.workflow.WorkflowException;
/**
 * 标准接口：调拨确认单据(调出)数据导出到标准接口表(调拨单)FN
 * @author lzs
 *
 */
public class BINBAT125_FN implements FunctionProvider {
	private static Logger logger = LoggerFactory.getLogger(BINBAT125_FN.class.getName());
	/**调拨确认单据(调出)数据导出Batch处理BL **/
	@Resource(name = "binbat125_BL")
	private BINBAT125_BL binbat125_BL;

	@Override
	public void execute(Map transientVars, Map arg, PropertySet ps)
			throws WorkflowException {
		try {
			logger.info("**************************调拨确认单据(调出)数据导出Batch处理开始*******************************");
			// Job运行履历表的运行方式
			transientVars.put("RunType", "AT");
			
			int flag=binbat125_BL.tran_binbat125(transientVars);
			ps.setInt("result", flag);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			throw new WorkflowException();
		}
		finally{
			logger.info("***************************调拨确认单据(调出)数据导出Batch处理结束******************************");
		}
	}

}
